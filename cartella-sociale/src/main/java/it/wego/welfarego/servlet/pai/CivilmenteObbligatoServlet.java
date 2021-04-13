/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.gson.Gson;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.model.CivilmenteObbligatoBean;
import it.wego.welfarego.model.json.JSONCivilmenteObbligato;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoCivObbDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObbPK;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.CivilmenteObbligatoSerializer;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.Log;
import it.wego.welfarego.xsd.pratica.Pratica;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class CivilmenteObbligatoServlet extends HttpServlet {

    private static final String CIVOBB_OK = "OK";
    private static final String CIVOBB_KO = "KO";
    private static final String CIVOBB_NOT_FOUND = "NF";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String codPai = request.getParameter("codPai");
        String action = request.getParameter("action");
        EntityManager em = Connection.getEntityManager();
        JSONCivilmenteObbligato json = new JSONCivilmenteObbligato();
        Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
        try {
            PaiInterventoCivObbDao civObbDao = new PaiInterventoCivObbDao(em);
            if (action != null && action.equals("read")) {
                String offset = request.getParameter("start");
                String limit = request.getParameter("limit");
                String tipoIntervento = request.getParameter("codTipInt");
                String cntTipInt = request.getParameter("cntTipInt");
                List<PaiInterventoCivObb> interventi = null;
                if (cntTipInt != null && !"".equals(cntTipInt)) {
                    //Sto caricando i civilmente obbligati da un intervento già salvato in banca dati
                    interventi = civObbDao.findByCodPaiCodTipintCntTipint(Integer.valueOf(codPai), tipoIntervento, Integer.valueOf(cntTipInt), Integer.valueOf(limit), Integer.valueOf(offset));
                } else {
                    //L'intervento non è ancora stato salvato, quindi ritorno una lista vuota
                    interventi = new ArrayList<PaiInterventoCivObb>();
                }

                List<CivilmenteObbligatoBean> civilmenteObbligatoList = new ArrayList<CivilmenteObbligatoBean>();
                CivilmenteObbligatoSerializer serializer = new CivilmenteObbligatoSerializer();
                for (PaiInterventoCivObb civ : interventi) {
                    CivilmenteObbligatoBean bean = serializer.serialize(civ);
                    civilmenteObbligatoList.add(bean);
                }
                json.setCivilmenteObbligati(civilmenteObbligatoList);
                json.setSuccess(true);
                json.setCode(CIVOBB_OK);
            }
            if (action != null && action.equals("write")) {
                String cntTipInt = request.getParameter("cntTipInt");
                String importoMensile = request.getParameter("importoMensile");
                String codAnag = request.getParameter("codAnag");
                String codTipInt = request.getParameter("codTipInt");
                if (cntTipInt != null && !"".equals(cntTipInt)
                        && codPai != null && !"".equals(codPai)
                        && codAnag != null && !"".equals(codAnag)
                        && codTipInt != null && !"".equals(codTipInt)) {
                    em.getTransaction().begin();
                    PaiInterventoCivObb civObb = civObbDao.findByKey(Integer.valueOf(codPai), codTipInt, Integer.valueOf(cntTipInt), Integer.valueOf(codAnag));
                    if (civObb != null) {
                        civObb.setImpCo(new BigDecimal(importoMensile.replace(',','.')));
                    } else {
                        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                        PaiIntervento intervento = paiInterventoDao.findByKey(Integer.valueOf(codPai), codTipInt, Integer.valueOf(cntTipInt));
                        int cnt = 1;
                        //Verfico se l'evento è già stato salvato
                        if (intervento != null) {
                            cnt = intervento.getPaiInterventoPK().getCntTipint();
                        } else {
                            //...altrimenti tengo traccia del cnt e lo utilizzerò quando salvo l'intervento
                            Number lastCnt = paiInterventoDao.findMaxCnt(Integer.valueOf(codPai), codTipInt);
                            if (lastCnt != null) {
                                cnt = lastCnt.intValue() + 1;
                            }
                        }
                        civObb = new PaiInterventoCivObb();
                        PaiInterventoCivObbPK key = new PaiInterventoCivObbPK();
                        key.setCodPai(Integer.valueOf(codPai));
                        key.setCodTipint(codTipInt);
                        key.setCodAnaCo(Integer.valueOf(codAnag));
                        key.setCntTipint(cnt);
                        civObb.setPaiInterventoCivObbPK(key);
                        civObb.setImpCo(new BigDecimal(importoMensile.replace(',','.')));
                        civObbDao.insert(civObb);
                        json.setCntTipInt(cnt);
                    }
                    PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                    CartellaDao cartellaDao = new CartellaDao(em);
                    CartellaSociale cartella = cartellaDao.findByCodAna(Integer.valueOf(codAnag));
                    PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                    PaiIntervento intervento = paiInterventoDao.findByKey(Integer.valueOf(codPai), codTipInt, Integer.valueOf(cntTipInt));
                    Pai pai = intervento.getPai();
                    // CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                    PaiEvento evento = Pratica.serializePaiEvento(pai, intervento, cartella, PaiEvento.PAI_UPDATE_CIVILMENTE_OBBLIGATO, connectedUser);
                    paiEventoDao.insert(evento);
                    em.getTransaction().commit();
                    json.setSuccess(true);
                    json.setCode(CIVOBB_OK);
                    json.setMessage("Operazione eseguita correttamente");
                } else {
                    json.setMessage("Non è stato specificato il tipo di intervento");
                    json.setSuccess(false);
                    json.setCode(CIVOBB_KO);
                }
            }
            if (action != null && action.equals("delete")) {
                String cntTipInt = request.getParameter("cntTipInt");
                String codAnag = request.getParameter("codAnag");
                String codAnagOrigine = request.getParameter("codAnagOrigine");
                String codTipInt = request.getParameter("codTipInt");
                if (cntTipInt == null || cntTipInt.equals("")) {
                    json.setMessage("Operazione eseguita correttamente");
                    json.setSuccess(true);
                    json.setCode(CIVOBB_NOT_FOUND);
                } else {
                    PaiInterventoCivObb civObb = civObbDao.findByKey(Integer.valueOf(codPai), codTipInt, Integer.valueOf(cntTipInt), Integer.valueOf(codAnag));
                    em.getTransaction().begin();
                    PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                    CartellaDao cartellaDao = new CartellaDao(em);
                    CartellaSociale cartella = cartellaDao.findByCodAna(Integer.valueOf(codAnagOrigine));
                    PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                    PaiIntervento intervento = paiInterventoDao.findByKey(Integer.valueOf(codPai), codTipInt, Integer.valueOf(cntTipInt));
                    Pai pai = intervento.getPai();
                    CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                    PaiEvento evento = Pratica.serializePaiEvento(pai, intervento, cartella, PaiEvento.PAI_DELETE_CIVILMENTE_OBBLIGATO, connectedUser);
                    paiEventoDao.insert(evento);
                    civObbDao.delete(civObb);
                    em.getTransaction().commit();
                    json.setMessage("Operazione eseguita correttamente");
                    json.setSuccess(true);
                    json.setCode(CIVOBB_OK);
                }
            }
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            json.setSuccess(false);
            json.setCode(CIVOBB_KO);
            json.setMessage("Si è verificato un errore, contattare l'amministratore");
            Log.APP.error("Si è verificato un errore agendo sugli importi civilmente obbligati", ex);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            out.write(jsonString);
            out.flush();
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
