package it.wego.welfarego.scheduler.rinnovi;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.scheduler.RinnovoException;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import it.wego.welfarego.scheduler.rinnovi.helper.RinnovoInterventoLogBuilder;
import it.wego.welfarego.services.interventi.ProrogaInterventoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.RinnovoAutomaticoteUtils;
import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.CreaProposteConBudgetDelPadre;
import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.tramite.strategie.CreaProposteTramiteStrategie;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public class RinnovoAutomatico {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Logger loggerRinnoviScartati = LoggerFactory.getLogger("rinnoviScartati");
    private PersistenceAdapter persistenceAdapter;
    private EntityManager entityManager;
    private PaiInterventoDao paiInterventoDao;

    private CreaProposteTramiteStrategie creaProposteTramiteStrategie = null;

    private CreaProposteConBudgetDelPadre creaProposteConBudgetDelPadre;


    public RinnovoAutomatico(PersistenceAdapter persistenceAdapter, EntityManager entityManager, PaiInterventoDao paiInterventoDao) {
        this.persistenceAdapter = persistenceAdapter;
        this.entityManager = entityManager;
        this.paiInterventoDao = paiInterventoDao;
        this.creaProposteConBudgetDelPadre = new CreaProposteConBudgetDelPadre();
        creaProposteTramiteStrategie = new CreaProposteTramiteStrategie();
    }

    public void rinnova(PaiIntervento interventoPadre) throws Exception {

        String pkInterventoPadre = SchedulerHelper.dumpPkIntervento(interventoPadre);
        logger.info("__pkInterventoPadre: " + pkInterventoPadre);

        RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder = new RinnovoInterventoLogBuilder();
        rinnovoInterventoLogBuilder.setInterventoPadre(interventoPadre);

        BudgetTipoInterventoDao budgetTipoInterventoDao = getBudgetTipoInterventoDao();
        PaiInterventoMeseDao paiInterventoMeseDao = getPaiInterventoMeseDao();

        non_rielaborare_questo_intervento(interventoPadre);

        fill_campo_urgente(interventoPadre);

        Gson gson = getGson();

        try {

            // mi serve per il metodo clone chiamato dopo...
            entityManager.detach(interventoPadre);

            PaiIntervento nuovoIntervento = qui_ciccia(interventoPadre, budgetTipoInterventoDao, paiInterventoMeseDao, rinnovoInterventoLogBuilder);

            String json = gson.toJson(rinnovoInterventoLogBuilder);
            logger.info(json);

            if(nuovoIntervento != null){
                creo_task(nuovoIntervento);
            }



        } catch (Throwable th) {

            String json = gson.toJson(rinnovoInterventoLogBuilder);
            throw new RinnovoException("ERROR su " + pkInterventoPadre + " json: " + json, th);

        }


    }

    Gson getGson() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {

                    public boolean shouldSkipClass(Class<?> clazz) {
                        boolean shouldSkipClass = false;
                        if (clazz == PaiInterventoMese.class ||
                                clazz == TipologiaIntervento.class ||
                                clazz == Parametri.class ||
                                clazz == Pai.class ||
                                clazz == Mandato.class ||
                                clazz == PaiDocumento.class ||
                                clazz == CartellaSociale.class
                                ) {
                            shouldSkipClass = true;
                        }
                        return shouldSkipClass;
                    }

                    public boolean shouldSkipField(FieldAttributes f) {

                        boolean shouldSkipField = false;
                        if (
                                f.getName().equalsIgnoreCase(("paiDocumentoList")) ||
                                        f.getName().equalsIgnoreCase(("fatturaList")) ||
                                        f.getName().equalsIgnoreCase(("mandatoList")) ||
                                        f.getName().equalsIgnoreCase(("uniqueTasklistList")) ||
                                        f.getName().equalsIgnoreCase(("paiInterventoMeseList")) ||
                                        f.getName().equalsIgnoreCase(("paiInterventoCivObbList")) ||
                                        f.getName().equalsIgnoreCase(("paiInterventoAnagraficaList")) ||
                                        f.getName().equalsIgnoreCase(("logMessaggiList")) ||
                                        f.getName().equalsIgnoreCase(("interventiFigli")) ||
                                        f.getName().equalsIgnoreCase(("interventoPadre")) ||
                                        f.getName().equalsIgnoreCase(("tipologiaIntervento")) ||
                                        f.getName().equalsIgnoreCase(("dsIdParamFasciaRedd")) ||
                                        f.getName().equalsIgnoreCase(("associazione")) ||
                                        f.getName().equalsIgnoreCase(("pai")) ||
                                        f.getName().equalsIgnoreCase(("dsCodAnaBenef")) ||
                                        f.getName().equalsIgnoreCase(("durMesiProroga")) ||
                                        f.getName().equalsIgnoreCase(("codImpProroga")) ||
                                        f.getName().equalsIgnoreCase(("rinnovato")) ||
                                        f.getName().equalsIgnoreCase(("dsCodAnaRich")) ||
                                        f.getName().equalsIgnoreCase(("ibanDelegato")) ||
                                        f.getName().equalsIgnoreCase(("paiEventoList")) ||
                                        f.getName().equalsIgnoreCase(("mandatoDettaglioList")) ||
                                        f.getName().equalsIgnoreCase(("mapDatiSpecificiInterventoList")) ||
                                        f.getName().equalsIgnoreCase(("datiSpecificiByCod")) ||
                                        f.getName().equalsIgnoreCase(("idCsr")) ||
                                        f.getName().equalsIgnoreCase(("motivazione")) ||
                                        f.getName().equalsIgnoreCase(("datiOriginali")) ||
                                        f.getName().equalsIgnoreCase(("protocollo")) ||
                                        f.getName().equalsIgnoreCase(("statoAttuale")) ||
                                        f.getName().equalsIgnoreCase(("dtFineProroga")) ||
                                        f.getName().equalsIgnoreCase(("urgente")) ||
                                        f.getName().equalsIgnoreCase(("approvazioneTecnica")) ||
                                        f.getName().equalsIgnoreCase(("tariffa")) ||
                                        f.getName().equalsIgnoreCase(("testoAutorizzazione")) ||
                                        f.getName().equalsIgnoreCase(("dataCreazioneRecord")) ||
                                        f.getName().equalsIgnoreCase(("utenteCreazioneRecor)"))
                                ) {
                            shouldSkipField = true;
                        }
                        return shouldSkipField;
                    }

                })
                /**
                 * Use serializeNulls method if you want To serialize null values
                 * By default, Gson does not serialize null values
                 */
                .create();
    }

    PaiInterventoMeseDao getPaiInterventoMeseDao() {
        return new PaiInterventoMeseDao(entityManager);
    }

    BudgetTipoInterventoDao getBudgetTipoInterventoDao() {
        return new BudgetTipoInterventoDao(entityManager);
    }

    void non_rielaborare_questo_intervento(PaiIntervento interventoPadre) throws RinnovoException {
        try {
            // evito di creare n interventi figli se qualcosa va male
            // tutto sto casino per il fatto che le transazioni sono a livello di dao e non a livello di logica applicativa....
            interventoPadre.setRinnovato(PaiInterventoDao.RINNOVATO);
            paiInterventoDao.update(interventoPadre);
        } catch (Exception e) {
            throw new RinnovoException("", e);
        }
    }


    void fill_campo_urgente(PaiIntervento interventoPadre) throws RinnovoException {
        if (interventoPadre.getUrgente() != 'S' && interventoPadre.getUrgente() != 'N') {
            logger.warn("interventoPadre.getUrgente(): [" + interventoPadre.getUrgente() + "] lo imposto a N");
            interventoPadre.setUrgente('N');
            try {
                paiInterventoDao.update(interventoPadre);
                Integer codPaiInterventoPadre = interventoPadre.getPaiInterventoPK().getCodPai();
                String codTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCodTipint();
                Integer cntTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCntTipint();
                interventoPadre = paiInterventoDao.findByKey(codPaiInterventoPadre, codTipIntInterventoPadre, cntTipIntInterventoPadre);
            } catch (Exception ex) {
                throw new RinnovoException("non ho potuto aggiornare il campo urgente di: " + interventoPadre.getPaiInterventoPK().toString(), ex);
            }
        }
    }

    PaiIntervento qui_ciccia(PaiIntervento interventoPadre, BudgetTipoInterventoDao budgetTipoInterventoDao, PaiInterventoMeseDao paiInterventoMeseDao, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) throws Exception {
        String logMsg = null;

        boolean intervento_di_rinnovo_assente = intervento_di_rinnovo_assente(interventoPadre);

        rinnovoInterventoLogBuilder.is_intervento_di_rinnovo_assente(intervento_di_rinnovo_assente);

        PaiIntervento nuovoIntervento = null;

        if (intervento_di_rinnovo_assente) {

            persistenceAdapter.initTransaction();


            nuovoIntervento = crea_intervento_proroga(interventoPadre);
            nuovoIntervento.setDataCreazioneRecord(new Date());
            nuovoIntervento.setUtenteCreazioneRecord("rinnovo_automatico");

            rinnovoInterventoLogBuilder.setInterventoFiglio(nuovoIntervento);

            String controlloCoerenza = controllaCoerenza(nuovoIntervento);

            if (controlloCoerenza == null) {

                sincronizza_intervento_con_coda_csr(nuovoIntervento);

                nuovoIntervento.setDtEsec(nuovoIntervento.getDtApe());

                persistenceAdapter.commitTransaction();

                if (budgetTipoInterventoDao.isInterventoSenzaBudget(nuovoIntervento)) {
                    logger.info("scarto la creazione delle proposte per:" + nuovoIntervento);
                } else {
                    crea_e_conferma_nuove_proposte(interventoPadre, nuovoIntervento, paiInterventoMeseDao, budgetTipoInterventoDao, rinnovoInterventoLogBuilder);
                }

                paiInterventoDao.passaInterventoInStatoEsecutivo(nuovoIntervento);

                paiInterventoDao.update(nuovoIntervento);

            } else {
                persistenceAdapter.rollbackTransaction();
                String msgTemplate = "rinnovo automatico fallito,\t\tinterventi (padre, figlio): {\"interventi\":{\"padre\":%s, \"figlio\":%s}} " + controlloCoerenza;
                String pkInterventoPadre = SchedulerHelper.dumpPkIntervento(interventoPadre);
                String pkInterventoFiglio = "";
                logMsg = String.format(msgTemplate, pkInterventoPadre, pkInterventoFiglio);
                logger.error(logMsg);
            }
        }

        return nuovoIntervento;
    }


    PaiIntervento crea_intervento_proroga(PaiIntervento interventoPadre) throws Exception {
        ProrogaInterventoService prorogaInterventoService = getProrogaInterventoService();
        return prorogaInterventoService.prorogaIntervento(interventoPadre);
    }

    void sincronizza_intervento_con_coda_csr(PaiIntervento nuovoIntervento) throws NoSuchAlgorithmException, KeyManagementException {
        CartellaSocialeWsClient cartellaSocialeWsClient = CartellaSocialeWsClient.newInstance().withEntityManager(entityManager);
        cartellaSocialeWsClient = cartellaSocialeWsClient.loadConfigFromDatabase();
        cartellaSocialeWsClient = cartellaSocialeWsClient.withPaiIntervento(nuovoIntervento);
        cartellaSocialeWsClient.sincronizzaIntervento();
    }

    ProrogaInterventoService getProrogaInterventoService() {
        return new ProrogaInterventoService(paiInterventoDao);
    }

    boolean intervento_di_rinnovo_assente(PaiIntervento interventoPadre) {

        PaiInterventoPK paiInterventoPK = interventoPadre.getPaiInterventoPK();
        Integer codPai = paiInterventoPK.getCodPai();
        String codTipInt = paiInterventoPK.getCodTipint();
        boolean ok = true;

        String message;
        List<PaiIntervento> interventiPresenti = paiInterventoDao.findByCodPaiCodTipint(codPai, codTipInt);
        for (PaiIntervento i : interventiPresenti) {
            Date dtFine = interventoPadre.calculateDtFine();
            if (i.equals(interventoPadre) == false && i.getStatoInt() != 'C' && i.getDtAvvio().after(dtFine)) {
                ok = false;
                message = "Esiste già un intervento aperto dello stesso tipo posteriore alla data di chiusura dell' intervento in input: " + i.getPaiInterventoPK().toString();
                loggerRinnoviScartati.info(message);
                break; // no return ...
            }
        }
        return ok;
    }

    void crea_e_conferma_nuove_proposte(PaiIntervento interventoPadre, PaiIntervento nuovoIntervento, PaiInterventoMeseDao paiInterventoMeseDao, BudgetTipoInterventoDao budgetTipoInterventoDao, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) throws Exception {

        boolean rinnovo_automatico_con_budget_intervento_padre = is_rinnovo_automatico_con_budget_intervento_padre(interventoPadre);

        rinnovoInterventoLogBuilder.rinnovo_con_budget_delPadre(rinnovo_automatico_con_budget_intervento_padre);

        if (rinnovo_automatico_con_budget_intervento_padre) {
            logger.info("budget_intervento_padre");
            creaProposteConBudgetDelPadre.crea_proposta(interventoPadre, nuovoIntervento, paiInterventoMeseDao, rinnovoInterventoLogBuilder);

            paiInterventoMeseDao.confirmWithoutArmonizzazione(nuovoIntervento);

        } else {

            creaProposteTramiteStrategie.crea_proposte(
                    interventoPadre, nuovoIntervento, paiInterventoMeseDao, budgetTipoInterventoDao, rinnovoInterventoLogBuilder);

            paiInterventoMeseDao.confirmWithoutArmonizzazione(nuovoIntervento);
        }
    }

    boolean is_rinnovo_automatico_con_budget_intervento_padre(PaiIntervento interventoPadre) {
        return TipologiaIntervento.FLGRINNOVO_AUTOMATICO_BUDGET_PRECEDENTE.equals(interventoPadre.getTipologiaIntervento().getFlgRinnovo());
    }

    String durata_nuovo_intervento(PaiIntervento nuovoIntervento) {
        Integer durMesi = nuovoIntervento.getDurMesi();
        Date dtFine = nuovoIntervento.getDtFine();
        char flgFineDurata = nuovoIntervento.getTipologiaIntervento().getFlgFineDurata();

        String durata;
        if (flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_D) {
            durata = "durata mesi :" + durMesi;
        } else {
            durata = "Fino al:" + it.wego.welfarego.xsd.Utils.dateToItString(dtFine);
        }
        return durata;
    }

    void creo_task(PaiIntervento nuovoIntervento) throws Exception {
        boolean ok = true;
        String message = "";
        String durata_nuovo_intervento = durata_nuovo_intervento(nuovoIntervento);
        PaiInterventoPK paiInterventoPK = nuovoIntervento.getPaiInterventoPK();
        Integer codPai = paiInterventoPK.getCodPai();
        String codTipInt = paiInterventoPK.getCodTipint();
        Integer cntTipInt = paiInterventoPK.getCntTipint();
        Date dtAvvio = nuovoIntervento.getDtAvvio();
        BigDecimal quantita = nuovoIntervento.getQuantita();
        String desTipint = nuovoIntervento.getTipologiaIntervento().getDesTipint();

        String campoFlow1_ok = "E' stato creato un intervento di proroga del tipo  " + desTipint + " con i seguenti dati :\n" +
                "Data di partenza:" + dtAvvio + "\n" +
                durata_nuovo_intervento + "\n" +
                "Importo assegnato euro: " + quantita;
        String campoFlow1_ko = "Non sono riuscito a prorogare l'intervento per questo motivo : Messaggio " + message;

        String titolo = ok == true ? "Notifica proroga intervento riuscita" : "Notifica proroga intervento fallita";
        String campoFlow1 = ok == true ? campoFlow1_ok : campoFlow1_ko;

        UniqueTasklist newTaskList = new UniqueTasklist(titolo, "N", "N");
        PaiIntervento old = paiInterventoDao.findByKey(codPai, codTipInt, cntTipInt);
        paiInterventoDao.update(old);
        newTaskList.setPai(old.getPai());
        newTaskList.setPaiIntervento(old);
        newTaskList.setForm(entityManager.getReference(UniqueForm.class, "70"));
        newTaskList.setUot(old.getPai().getIdParamUot().getIdParam().getCodParam());
        newTaskList.setRuolo(Utenti.Ruoli.CO_UOT.toString());
        newTaskList.setCampoFlow1(campoFlow1);
        TaskDao tdao = new TaskDao(entityManager);
        tdao.insert(newTaskList);
    }


    String controllaCoerenza(PaiIntervento intervento) {
        String unitaMis = intervento.getTipologiaIntervento().getIdParamUniMis().getDesParam();
        char flgDurata = intervento.getTipologiaIntervento().getFlgFineDurata();
        String message = null;
        //primo check se è durata mensile ma i mesi sono 0 allora qualcosa non và.
        if (flgDurata == TipologiaIntervento.FLG_FINE_DURATA_D && intervento.getDurMesi() == 0) {
            message = "L'intervento prevede una durata mesi ma nella proroga i mesi sono 0 controllare che la tipologia durata dell'intervento  non sia cambiata e duplicare a mano l'intervento ";
        }
        if (flgDurata == TipologiaIntervento.FLG_FINE_DURATA_F && intervento.getDtFine() == null) {
            message = "L'intervento prevede una durata fine  ma nella proroga  non vi è durata fine controllare che la tipologia durata dell'intervento non sia cambiata  e duplicare a mano l'intervento";
        }
        if (unitaMis.contains("sett") && (intervento.getDurSettimane() == null || intervento.getDurSettimane() == 0)) {
            message = "L'intervento prevede una durata settimanale ma nella proroga le settimane non sono state valorizzate. controllare che la tipologia durata del'intervento non sia cambiata e  duplicare a mano l'intervento ";
        }

        return message;
    }
}
