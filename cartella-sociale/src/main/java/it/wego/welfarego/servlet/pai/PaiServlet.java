package it.wego.welfarego.servlet.pai;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.PaiSerializer;
import it.wego.welfarego.services.PaiService;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.Log;
import it.wego.welfarego.xsd.pratica.Pratica;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author giuseppe
 */
public class PaiServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	/**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        String action = request.getParameter("action");
        String type = request.getParameter("type");
        Object jsonResponse = null;
        EntityManager em = Connection.getEntityManager();
        PaiSerializer paiSerializer = new PaiSerializer(em);
        Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
        try {

            if (Objects.equal(action, "read")) {

                int codAna = Integer.valueOf(request.getParameter("codAnag"));

                JsonBuilder jsonBuilder = JsonBuilder.newInstance().withTransformer(paiSerializer.getPaiSerializer());
                if (Objects.equal(type, "singolo")) {
                    CartellaDao cartellaDao = new CartellaDao(em);
                    CartellaSociale cartella = cartellaDao.findByCodAna(codAna);
                    if (cartella != null) {
                        PaiDao paiDao = new PaiDao(em);
                        List<Pai> pList = paiDao.findPaiByCodAna(codAna);
                        jsonBuilder.withParameters(getParameters()).withData(pList);
                    }
                } else if (Objects.equal(type, "famiglia")) {
                    AnagrafeSoc anagrafeSoc = em.find(AnagrafeSoc.class, codAna);
                    List<Pai> list = Lists.newArrayList();
                    for (AnagrafeSoc persona : anagrafeSoc.getAnagrafeSocListFromAnagrafeFamList()) {
                        int codAnaFam = persona.getCodAna();
                        CartellaDao cartellaDao = new CartellaDao(em);
                        CartellaSociale cartella = cartellaDao.findByCodAna(codAnaFam);
                        if (cartella != null) {
                            list.addAll(cartella.getPaiList());
                        }
                    }
                    jsonBuilder.withData(list);
                }
                jsonResponse = jsonBuilder.buildStoreResponse();




            } else if (Objects.equal(action, "write")) {
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                PaiDao paiDao = new PaiDao(em);
                boolean isOpen = paiDao.isOpenPai(codAna);
                if (!isOpen) {
                    em.getTransaction().begin();
                    Pai pai = paiSerializer.serializePai(request, action);
                    paiDao.insert(pai);
                    PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                    // intervento default, only for Pratica.serializePaiEvento
                    PaiIntervento paiIntervento = paiInterventoDao.createPaiInterventoDefault(pai);
                    PaiEvento evento = Pratica.serializePaiEvento(pai, paiIntervento, pai.getCartellaSociale(), PaiEvento.PAI_INSERT, connectedUser);
                    em.persist(evento);
                    em.getTransaction().commit();
                    em.refresh(paiIntervento);
                    CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPaiIntervento(paiIntervento).sincronizzaIntervento();
                    //Salvo su cartella sociale regionale
                    AnagrafeSoc anagrafe = pai.getAnagrafeSoc();
                    jsonResponse = JsonBuilder.buildResponse("Operazione eseguita correttamente");
                } else {
                    throw new Exception("Attenzione! Esiste già un PAI aperto. Chiudere prima il PAI");
                }
            } else if (Objects.equal(action, "copy")) {
                PaiDao paiDao = new PaiDao(em);
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                boolean isOpen = paiDao.isOpenPai(codAna);
                if (isOpen) {
                    throw new Exception("Attenzione! Esiste già un PAI aperto. Chiudere prima il PAI");
                }
                em.getTransaction().begin();
                Pai pai = paiSerializer.serializePai(request, action);
                paiDao.insert(pai);
                PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_INSERT, connectedUser);
                paiEventoDao.insert(evento);
                em.getTransaction().commit();
                jsonResponse = JsonBuilder.buildResponse("Operazione eseguita correttamente");
            } else if (Objects.equal(action, "update")) {


                jsonResponse = actionUpdate(request, action, em, paiSerializer, connectedUser);


            } else if (Objects.equal(action, "close")) {
                PaiDao paiDao = new PaiDao(em);
                int codPai = Integer.valueOf(request.getParameter("codPai"));
                Pai pai = paiDao.findPai(codPai);
                boolean isOpenInterventi = false;
                List<PaiIntervento> interventi = pai.getPaiInterventoList();
                if (interventi != null) {
                    for (PaiIntervento intervento : interventi) {
                        if (intervento.getDtChius() == null) {
                            isOpenInterventi = true;
                            break;
                        }
                    }
                }
                if (pai.getDtChiusPai() != null) {
                    throw new Exception("Il PAI è già chiuso.");
                }
                if (isOpenInterventi) {
                    throw new Exception("Per poter chiudere il PAI è necessario chiudere tutti gli interventi aperti");
                } else {
                    em.getTransaction().begin();
                    String dataChiusura = getParameter("dataChiusura");
                    if (!Strings.isNullOrEmpty(dataChiusura)) {
                        pai.setDtChiusPai(StringConversion.itStringToDate(dataChiusura));
                    } else {
                        pai.setDtChiusPai(new Date());
                    }
                    pai.setFlgStatoPai(Pai.STATO_CHIUSO);
                    PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                    CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                    PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_CLOSE, connectedUser);
                    paiEventoDao.insert(evento);
                    em.getTransaction().commit();
                }
                try {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Chiusura prematura PAI n. ").append(pai.getCodPai()).append(", ").append(pai.getAnagrafeSoc().getCognome()).append(", ").append(pai.getAnagrafeSoc().getNome());
                    jsonResponse = JsonBuilder.buildResponse("Operazione eseguita correttamente");
                } catch (Exception ex) {
                    jsonResponse = JsonBuilder.buildResponse("Non è stato possibile avviare il processo di generazione documentale. Contattare l'amministratore di sistema");
                    Log.APP.error("Non è stato possibile avviare il processo di generazione documentale", ex);
                }
            }
            return jsonResponse;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private Object actionUpdate(HttpServletRequest request, String action, EntityManager em, PaiSerializer paiSerializer, Utenti connectedUser) throws Exception {
        Pai pai = paiSerializer.serializePai(request, action);
        PaiService paiService = new PaiService(em);

        paiService.updatePai(pai, connectedUser);

        return JsonBuilder.buildResponse("Operazione eseguita correttamente");
    }
}
