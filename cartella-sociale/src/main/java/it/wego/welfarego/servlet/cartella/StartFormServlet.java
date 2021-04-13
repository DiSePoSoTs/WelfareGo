/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.cartella;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import it.wego.conversions.StringConversion;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.bre.utils.BreMessage;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.model.json.JSONMessage;
import it.wego.welfarego.model.json.JSONStartProcess;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Configuration;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class StartFormServlet extends JsonServlet {


	private static final long serialVersionUID = 1L;
	
	private Logger logger = getLogger();

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
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        String action = request.getParameter("action");
        logger.debug("got request, action = {}", action);
        EntityManager em = Connection.getEntityManager();
        try {
            Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
            if (Objects.equal(action, "approvazione")) {
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findLastPai(codAna);
                if (pai != null) {
                    if (!isInterventiActive(pai)) {
                        logger.debug("nessun intervento attivo");
                        return serializeNessunInterventoAttivo(pai);
                    } else {
                        if (pai.getFlgStatoPai() == 'A' || pai.getFlgStatoPai() == 'R') {
                            if (pai.hasRichiestaApprovazioneAttiva()) {
                                return new BreMessage("ERROR", "esiste gia' una richiesta di approvazione non conclusa per il pai " + pai.getCodPai());
                            }
                            boolean allClosed = true;
                            for (PaiIntervento intervento : pai.getPaiInterventoList()) {
                                if (intervento.getDtChius() == null) {
                                    allClosed = false;
                                    break;
                                }
                            }
                            if (!allClosed) {
                                List<BreMessage> messages = BreUtils.getBreMessages(pai);
                                messages.addAll(BreUtils.getBreMessages((Collection) pai.getPaiInterventoList()));
                                logger.debug("returning messages");

                                return messages;
                            } else {
                                return serializeNessunInterventoAttivo(pai);
                            }
                        } else if (pai.getFlgStatoPai() == 'S') {
                            return serializeMancataApprovazione(pai);
                        } else if (pai.getFlgStatoPai() == 'C') {
                            return serializeApprovazionePaiChiuso(pai);
                        } else {
                            return serializeStatoSconosciuto(pai);
                        }
                    }
                } else {
                    return (serializeNessunPai());
                }
            } else if (Objects.equal(action, "trasferimento")) {
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findLastPai(codAna);
                if (pai == null || pai.getDtChiusPai() != null) {
                    throw new Exception("Non risultano PAI attivi. Non è possibile eseguire l'azione richiesta ");
                }
                PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                String motivazione = request.getParameter("motivazione");
                int motivazioneTrasferimento = Parametri.getCodiceMotivazioneTrasferimento(em);
                int motivazioneDecesso = Parametri.getCodiceMotivazioneDecesso(em);
                int motivazioneId = Integer.valueOf(motivazione).intValue();
                AnagrafeSoc ana = pai.getAnagrafeSoc();
                if (motivazioneId == motivazioneTrasferimento) {
                    if (ana.getLuogoResidenza().getComune() != null && ana.getLuogoResidenza().getComune().getDesCom().toUpperCase().equals("TRIESTE")) {
                        throw new Exception("E' necessario modificare la residenza. L'utente risulta residente nel comune di Trieste");
                    }
                }
                if (motivazioneId == motivazioneDecesso) {
                    if (ana.getDtMorte() == null) {
                        throw new Exception("Non è stata specificata la data di decesso dell'utente. Salvare la data di decesso nell'anagrafica e riprovare.");
                    }
                }
                List<PaiIntervento> interventi = pai.getPaiInterventoList();
                String note = request.getParameter("note");
                String datachiusuraString = request.getParameter("datachiusura");
                Date datachiusura = StringConversion.itStringToDate(datachiusuraString);
                em.getTransaction().begin();
                try {
                    if (interventi != null) {
                        for (PaiIntervento intervento : interventi) {
                            if (intervento.getDtChius() == null) {
                                paiInterventoDao.chiudiIntervento(intervento, "Chiusura forzata intervento per trasferimento/decesso", null, datachiusura);
                            }
                        }
                    }
                    pai.setDtChiusPai(datachiusura);
                    pai.setFlgStatoPai(Pai.STATO_CHIUSO);
                    pai.setMotivChius(motivazione);
                    pai.setMotiv(note);
                } catch (Exception ex) {
                    logger.error("Non è stato possibile chiudere gli interventi", ex);
                    throw new Exception("Non è stato possibile chiudere gli interventi. Contattare l'amministratore.");
                }
                CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                em.getTransaction().commit();
                try {
                    em.getTransaction().begin();
                    PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                    PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_TRASFERIMENTO, connectedUser);
                    paiEventoDao.insert(evento);
                    em.getTransaction().commit();
                } catch (Exception ex) {
                    logger.error("L'operazione è stata eseguita correttamente ma non è stato possibile salvare l'evento", ex);
                    throw new Exception("Non è stato possibile salvare l'evento.");
                }
                JSONMessage json = new JSONMessage();
                try {
                    String start_endpoint = Parametri.getURLDocumentaleIntalio(em);
                    String cod_tmpl_chius = Parametri.getCodiceTemplateChiusura(em);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Chiusura prematura PAI n. ").append(pai.getCodPai()).append(", ").append(pai.getAnagrafeSoc().getCognome()).append(", ").append(pai.getAnagrafeSoc().getNome());
                    String titolo = sb.toString();

                    json.setMessage("Operazione eseguita correttamente");
                    json.setSuccess(true);
                } catch (Exception ex) {
                    json.setMessage("Non è stato possibile avviare il processo di generazione documentale. Contattare l'amministratore di sistema");
                    logger.error("Non è stato possibile avviare il processo di generazione documentale", ex);
                    json.setSuccess(false);
                }

                return json;
            } else if (Objects.equal(action, "startprocess")) {
                em.getTransaction().begin();
                logger.debug("handling start process request");
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findLastPai(codAna);
                JSONStartProcess json = new JSONStartProcess();
                Preconditions.checkArgument(Objects.equal(pai.getFlgStatoPai(), Pai.STATO_APERTO) || Objects.equal(pai.getFlgStatoPai(), Pai.STATO_RIFIUTATO),
                        "impossibile avviare processo di approvazione per il pai %s in stato %s", pai.getCodPai(), pai.getFlgStatoPai());
                Preconditions.checkArgument(!pai.hasRichiestaApprovazioneAttiva(), "impossibile avviare processo di approvazione per il pai %s, esiste gia' una richiesta di approvazione non conclusa", pai.getCodPai());

                richiedi_approvazione(em, connectedUser, pai);
                logger.info("fine elaborazione interventi in approvazione");
                new PaiInterventoDao(em).setDataRichiestaApprovazionePerInterventiPendenti(new Date(), pai);
                em.getTransaction().commit();
                
                json.setMessage("Operazione eseguita con successo");
                json.setSuccess(true);

                return json;
            } else if (Objects.equal(action,"producidomanda")){
            	em.getTransaction().begin();
                logger.debug("handling produci domanda event");
                //prendo il codice anagrafica e trovo l'ultimo pai aperto 
                int codAna = Integer.valueOf(request.getParameter("codAnag"));
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findLastPai(codAna);
                PaiEventoDao paiEventoDao = new PaiEventoDao(em);
                List<PaiIntervento> interventi = pai.getPaiInterventoList();
                boolean interventiNonApprovati = interventiNonMandatiInApprovazione(interventi);
                //faccio un check delle condizioni 
                Preconditions.checkArgument(Objects.equal(pai.getFlgStatoPai(), Pai.STATO_APERTO) || Objects.equal(pai.getFlgStatoPai(), Pai.STATO_RIFIUTATO) || Objects.equal(pai.getFlgStatoPai(), Pai.STATO_SOSPESO),
                        "impossibile avviare processo di generazione domanda  per il pai %s in stato %s", pai.getCodPai(), pai.getFlgStatoPai());
                Preconditions.checkArgument((!interventiNonApprovati == false),"Attenzione, non vi sono nuovi interventi per questo PAI, per favore usare le domande generate precedentemente. Grazie");
                //processo di produzione della domanda 
                JSONStartProcess json = new JSONStartProcess();
                PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_PRODUCI_DOMANDA, connectedUser);
                paiEventoDao.insert(evento);
                try {
                    PaiDocumento doc = generaDocumentoDomanda(em, pai, evento);
                    json.setIdDoc(String.valueOf(doc.getIdDocumento()));
                    json.setCodPai(pai.getCodPai());
                } catch (Exception ex) {
                    logger.error("Errore generando la domanda : {}", ex.toString());
                    throw ex;
                }
                em.getTransaction().commit();
                json.setMessage("Operazione eseguita con successo");
                json.setSuccess(true);
                return json;
            }
            else {
                Preconditions.checkArgument(false, "unhandled action code = {}", action);
                return null; //unreacheable
            }
        } catch (Exception ex) {
            logger.error("Si è verificato un errore avviando il processo", ex);
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

    void richiedi_approvazione(EntityManager em, Utenti connectedUser, Pai pai) throws Exception {
        PaiEventoDao paiEventoDao = new PaiEventoDao(em);
        PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_RICHIESTA_APPROVAZIONE, connectedUser);
        paiEventoDao.insert(evento);

        PaiInterventoDao pidao = new PaiInterventoDao(em);
        List<PaiIntervento> interventiInApprovazione = pidao.getInterventiDaApprovare(pai);
        logger.info("inizio elaborazione interventi in approvazione: " + interventiInApprovazione.size());
        for(PaiIntervento i:interventiInApprovazione){
            //se l'intervento deve andare avanti ovvero se è settato il flag fattura o pagamento
            if((i.getTipologiaIntervento().getFlgFatt()=='S'|| i.getTipologiaIntervento().getFlgPagam()=='S') && (i.getInterventoPadre()==null)){
                // se richiede approvazione tecnica... creo il form apposito se no il form standard
                UniqueTasklist t = new UniqueTasklist();
                t.setPaiIntervento(i);
                t.setRuolo(Utenti.COORDINATORE_UOT.name());
                t.setUot(i.getPai().getIdParamUot().getIdParam().getCodParam());
                if(i.getTipologiaIntervento().getFlgAppTec()=='S'){
                    t.setForm(em.getReference(UniqueForm.class, UniqueForm.COD_FORM_APPROVAZIONE_TECNICA));
                    t.setDesTask("Approvazione tecnica");

                }
                else {
                    t.setForm(em.getReference(UniqueForm.class, UniqueForm.COD_FORM_VERIFICA_ESECUTIVITA));
                    t.setDesTask("Verifica dati esecutività");
                }


                em.persist(t);
                for(InterventiAssociati ia :i.getInterventiFigli()){
                    ia.getInterventoFiglio().setDataRichiestaApprovazione(new Date());
                    ia.getInterventoFiglio().setStatoAttuale(PaiIntervento.IN_APPROVAZIONE);
                    em.merge(ia.getInterventoFiglio());
                }

            }
        }
    }

    private Object serializeNessunInterventoAttivo(Pai pai) {

        String nome = pai.getAnagrafeSoc().getNome();
        String cognome = pai.getAnagrafeSoc().getCognome();
        String nc = cognome + " " + nome;
        BreMessage message = serializeErrorMessage("Non ci sono interventi attivi per il PAI di " + nc);
        return message;
    }

    private Object serializeNessunPai() {
        BreMessage message = serializeErrorMessage("Non è stato associato un assistente sociale e/o una UOT all\'utente (non esiste un PAI attivo per l'utente).");
        return message;
    }

    private Object serializeStatoSconosciuto(Pai pai) {
        BreMessage message = serializeErrorMessage("Il PAI si trova in uno stato non riconosciuto. Contattare l'amministratore. (codice PAI: " + String.valueOf(pai.getCodPai()) + ")");
        return message;
    }

    private Object serializeApprovazionePaiChiuso(Pai pai) {
        BreMessage message = serializeErrorMessage("Si sta cercando di chiedere l'approvazione di un PAI già chiuso (codice PAI: " + String.valueOf(pai.getCodPai()) + ")");
        return message;
    }

    private Object serializeMancataApprovazione(Pai pai) {
        String nome = pai.getAnagrafeSoc().getNome();
        String cognome = pai.getAnagrafeSoc().getCognome();
        String nc = nome + " " + cognome;
        BreMessage message = serializeErrorMessage("E' già stata chiesta l'approvazione per il PAI associato a " + nc);
        return message;
    }

    private BreMessage serializeErrorMessage(String string) {
        BreMessage m = new BreMessage();
        m.setMessage(string);
        m.setLevel("ERROR");
        return m;
    }

    private boolean isInterventiActive(Pai pai) {
        List<PaiIntervento> interventi = pai.getPaiInterventoList();
        int count = 0;
        for (PaiIntervento intervento : interventi) {
            if (!intervento.isInterventoAccesso()) {
                count++;
            }
        }
        return count > 0;
    }

    private PaiDocumento generaDocumentoDomanda(EntityManager em, Pai pai, PaiEvento evento) throws JAXBException, Exception {
        String URL = Parametri.getURLDocumentiDinamici(em);
        String xml = Pratica.getXmlCartellaSociale(pai.getAnagrafeSoc(), pai);
        TemplateDao templateDao = new TemplateDao(em);
        Template template;
        if (Iterables.any(pai.getPaiInterventoList(), new Predicate<PaiIntervento>() {
            public boolean apply(PaiIntervento paiIntervento) {

                return !paiIntervento.isInterventoAccesso() && paiIntervento.getDataRichiestaApprovazione() != null;
            }
        })) {
            getLogger().debug("carichiamo il template domanda integrativa");
            String codTemplateDomanda = new ConfigurationDao(em).getConfig(Configuration.COD_TEMPLATE_DOMANDA_INTEGRATIVA);
            template = templateDao.findByCodTemplate(Integer.valueOf(codTemplateDomanda));
        } else {
            getLogger().debug("carichiamo il template domanda normale");
            String codTemplateDomanda = Parametri.getCodiceTemplateDomanda(em);
            template = templateDao.findByCodTemplate(Integer.valueOf(codTemplateDomanda));
        }
        PaiDocumentoDao paiDocDao = new PaiDocumentoDao(em);
       
        byte[] domanda = DynamicOdtUtils.newInstance()
                .withTemplateBase64(template.getClobTmpl())
                .withDataXml(xml)
                .withConfig(new ConfigurationDao(em).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                .getResult();
        if (domanda != null) {
            String domandaPerDB = new String(Base64.encodeBase64(domanda));

            PaiDocumento doc = paiDocDao.createDoc(pai, evento.getCodUte(), Documenti.TIPO_DOC_DOMANDA, domandaPerDB, "domanda");

            doc.setNomeFile(doc.getNomeFile() + "_Ver_" + doc.getVer() + ".odt");
            paiDocDao.update(doc);

            return doc;
        } else {
            logger.error("Non è stato generato il template della domanda");
            throw new Exception("Non è stato generato il template per la domanda");
        }
    }
    
    /*
     * Controllo se vi sono interventi ancora non approvati, in quel caso produrre la domanda ha un senso 
     */
    private boolean interventiNonMandatiInApprovazione ( List<PaiIntervento> interventi) {
    	boolean interventiNonApprovati = false;
    	Iterator<PaiIntervento> i  = interventi.iterator();
    	while(i.hasNext()){
    		PaiIntervento intervento = i.next();
    		if((!intervento.isInterventoAccesso()) && (intervento.getDataRichiestaApprovazione()== null)){
    			interventiNonApprovati = true;
    			break;
    		}
    	}
    	return interventiNonApprovati;
    }
}


