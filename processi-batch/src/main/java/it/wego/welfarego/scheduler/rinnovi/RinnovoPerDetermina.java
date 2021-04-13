package it.wego.welfarego.scheduler.rinnovi;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.*;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.scheduler.RinnovoException;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import it.wego.welfarego.services.interventi.ProrogaInterventoService;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RinnovoPerDetermina {
    static final String RINNOVO_PER_DETERMINA_LOG_TEMPLATE = "rinnovo per determina,\t\tinterventi (padre, figlio): {\"interventi\":{\"padre\":%s, \"figlio\":%s}}";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private PersistenceAdapter persistenceAdapter;
    private EntityManager entityManager;
    private PaiInterventoDao paiInterventoDao;

    RinnovoPerDetermina() {}

    public RinnovoPerDetermina(PersistenceAdapter persistenceAdapter, EntityManager entityManager, PaiInterventoDao paiInterventoDao) {
        this.persistenceAdapter = persistenceAdapter;
        this.entityManager = entityManager;
        this.paiInterventoDao = paiInterventoDao;
    }

    public void rinnova(PaiIntervento interventoPadre) throws RinnovoException {

        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();

        logger.info(String.format("rinnovo per determina: %s", SchedulerHelper.dumpPkIntervento(interventoPadre)));

        if(interventoPadre.getUrgente() != 'S' && interventoPadre.getUrgente() != 'N'){
            logger.warn("interventoPadre.getUrgente(): ["+interventoPadre.getUrgente() + "] lo imposto a N");
            interventoPadre.setUrgente('N');
            try {
                paiInterventoDao.update(interventoPadre);
                Integer codPaiInterventoPadre = interventoPadre.getPaiInterventoPK().getCodPai();
                String codTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCodTipint();
                Integer cntTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCntTipint();
                interventoPadre = paiInterventoDao.findByKey(codPaiInterventoPadre, codTipIntInterventoPadre, cntTipIntInterventoPadre);
            }catch (Exception ex ){
                throw new RinnovoException("non ho potuto aggiornare il campo urgente di: " + interventoPadre.getPaiInterventoPK().toString(), ex);
            }
        }

        String logMsg = "";
        try {
            //prima di tutto rinnoviamo l'intervento
            persistenceAdapter.initTransaction();
            BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);

            Integer codPaiInterventoPadre = interventoPadre.getPaiInterventoPK().getCodPai();
            String codTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCodTipint();
            Integer cntTipIntInterventoPadre = interventoPadre.getPaiInterventoPK().getCntTipint();

            // qui creo un nuovo intervento e lo salvo su db
            String cntTipint = cntTipIntInterventoPadre.toString();
            Integer durMesiProroga = interventoPadre.getDurMesiProroga();
            Date dtFineProroga = interventoPadre.getDtFineProroga();
            String codPai = codPaiInterventoPadre.toString();

            ProrogaInterventoService prorogaInterventoService = new ProrogaInterventoService(paiInterventoDao);
            PaiIntervento interventoFiglio = prorogaInterventoService.prorogaIntervento(interventoPadre);

            interventoPadre = paiInterventoDao.findByKey(codPaiInterventoPadre, codTipIntInterventoPadre, cntTipIntInterventoPadre);

            fill_interventi_associati(interventoFiglio, interventoPadre);

            CartellaSocialeWsClient cartellaSocialeWsClient = CartellaSocialeWsClient.newInstance().withEntityManager(entityManager);
            cartellaSocialeWsClient = cartellaSocialeWsClient.loadConfigFromDatabase();
            cartellaSocialeWsClient = cartellaSocialeWsClient.withPaiIntervento(interventoFiglio);
            cartellaSocialeWsClient.sincronizzaIntervento();

            persistenceAdapter.commitTransaction();

            // lo metto qui xhé cosi evito di creare N. interventi figli se qualcosa va male nella gestione del budget.
            interventoPadre.setRinnovato(PaiInterventoDao.RINNOVATO);
            paiInterventoDao.update(interventoPadre);

            logMsg = log_rinnova_per_determinia(interventoPadre, interventoFiglio);
            
            PaiInterventoPK paiInterventoFiglioPK = interventoFiglio.getPaiInterventoPK();
            String codTipIntInterventoFiglio = paiInterventoFiglioPK.getCodTipint();
            BigDecimal quantita = interventoFiglio.getQuantita();
            Integer durMesi = interventoFiglio.getDurMesi();
            Date dtAvvio = interventoFiglio.getDtAvvio();
            Date dtFine = interventoFiglio.getDtFine();
            BigDecimal impStdCosto = interventoFiglio.getTipologiaIntervento().getImpStdCosto();
            Pai paiNuovoIntervento = interventoFiglio.getPai();
            ParametriIndata idParamFascia = paiNuovoIntervento.getIdParamFascia();
            char flgFineDurata = interventoFiglio.getTipologiaIntervento().getFlgFineDurata();

            // gestione budget
            String codiceImpegnoInterventoPadre = interventoPadre.getCodImpProroga();
            DateTime fineAnno = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 12, 31, 23, 59);
            DateTime inizioAnno = new DateTime(Calendar.getInstance().get(Calendar.YEAR) + 1, 01, 01, 00, 00);



            if (is_intervento_su_piu_anni(interventoFiglio, dtAvvio, fineAnno, inizioAnno)) {

                BigDecimal costoFineAnno = BigDecimal.ZERO;
                BigDecimal costoAnnoNuovo = BigDecimal.ZERO;
                //mi calcolo il costo dell'intervento da qui a fine anno e da inizio anno nuovo a data fine,.
                if (flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_D) {
                    Integer mesiFineAnno = Months.monthsBetween(new DateTime(dtAvvio), fineAnno).getMonths();
                    costoFineAnno  = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(interventoFiglio, mesiFineAnno));
                    costoAnnoNuovo = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(interventoFiglio, durMesi - mesiFineAnno));
                } else {
                    Date dal = inizioAnno.toDate();
                    Date al = fineAnno.toDate();
                    costoFineAnno = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(interventoFiglio, dtAvvio, al));
                    costoAnnoNuovo = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(interventoFiglio, dal, dtFine));
                }
                interventoFiglio.setCostoPrev(costoFineAnno.add(costoAnnoNuovo));

                //mi trovo il budget fdi questo anno e il primo budget dell'anno precedente
                BudgetTipIntervento thisYear = null;
                BudgetTipIntervento nextYear = null;
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(dtAvvio);
                int yearAvvio = cal1.get(Calendar.YEAR);

                List<BudgetTipIntervento> budgets = budgetTipoInterventoDao.findByCodTipint(codTipIntInterventoFiglio);
                for (BudgetTipIntervento budgetTipIntervento : budgets) {
                    String codImpe = budgetTipIntervento.getBudgetTipInterventoPK().getCodImpe();
                    boolean stessoCodiceImpegno = codImpe.equals(codiceImpegnoInterventoPadre);
                    short annoErogazione = budgetTipIntervento.getAnnoErogazione();
                    boolean isAnnoErogazioneEqualsAnnoAvvio = annoErogazione == yearAvvio;
                    if (stessoCodiceImpegno && isAnnoErogazioneEqualsAnnoAvvio) {
                        thisYear = budgetTipIntervento;
                    } else if (annoErogazione == (yearAvvio + 1)) {
                        nextYear = budgetTipIntervento;
                    }
                }//ho scorso i budget


                if (thisYear == null && nextYear == null){
                    throw new RinnovoException(String.format("non si è potuto associare un budget per l'intervento figlio:%s", SchedulerHelper.dumpPkIntervento(interventoFiglio)),null);
                }

                //assegno i coasti al budget di quest'anno e anno nuovo
                if (thisYear != null) {
                    BudgetTipInterventoPK budgetTipInterventoPK = thisYear.getBudgetTipInterventoPK();
                    new PaiInterventoMeseDao(entityManager).insertProp(paiInterventoFiglioPK, budgetTipInterventoPK, costoFineAnno, costoFineAnno.divide(impStdCosto), idParamFascia);
                }
                if (nextYear != null) {
                    BudgetTipInterventoPK budgetTipInterventoPK = nextYear.getBudgetTipInterventoPK();
                    new PaiInterventoMeseDao(entityManager).insertProp(paiInterventoFiglioPK, budgetTipInterventoPK, costoAnnoNuovo, costoAnnoNuovo.divide(impStdCosto), idParamFascia);
                }

            }


            else {

                BigDecimal costo = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(interventoFiglio));
                interventoFiglio.setCostoPrev(costo);

                String codTipintNuovoIntervento = interventoFiglio.getTipologiaIntervento().getCodTipint();

                Calendar cal = Calendar.getInstance();
                cal.setTime(dtAvvio);
                int yearAvvio = cal.get(Calendar.YEAR);
                String  yearAvvioAsString = String.valueOf(cal.get(Calendar.YEAR));
                BudgetTipIntervento budgetTipIntervento = budgetTipoInterventoDao.findByKey(codTipintNuovoIntervento, yearAvvioAsString, codiceImpegnoInterventoPadre);
                if (budgetTipIntervento == null){
                    throw new RinnovoException(String.format("Non esistono budget per intervento:%s sull' anno:%s", codTipintNuovoIntervento, yearAvvio), null);
                }

                BudgetTipInterventoPK budgetTipInterventoPK = budgetTipIntervento.getBudgetTipInterventoPK();

                budgetTipInterventoPK = budgetTipIntervento.getBudgetTipInterventoPK();

                new PaiInterventoMeseDao(entityManager).insertProp(paiInterventoFiglioPK, budgetTipInterventoPK, costo, costo.divide(impStdCosto), idParamFascia);
            }

            paiInterventoDao.update(interventoFiglio);

            //una volta messo il budget faccio l'evento
            PaiEventoDao paiEventoDao = new PaiEventoDao(entityManager);
            //   pedao.insertEvento(interventoFiglio,"Proroga intervento precedente");
            PaiEvento evento = paiEventoDao.insertEvento(interventoFiglio, PaiEvento.PAI_CONFERMA_INTERVENTO);
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
            paiEventoDao.update(evento);

            String durata = "";
            if (flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_D) {
                durata = "durata mesi :" + durMesi;
            } else {
                durata = "Fino al:" + it.wego.welfarego.xsd.Utils.dateToItString(dtFine);
            }
            UniqueTasklist newTaskList = new UniqueTasklist("Notifica proroga intervento","N","N");
            newTaskList.setPai(paiNuovoIntervento);
            newTaskList.setPaiIntervento(interventoFiglio);
            newTaskList.setForm(entityManager.getReference(UniqueForm.class, "70"));
            newTaskList.setUot(paiNuovoIntervento.getIdParamUot().getIdParam().getCodParam());
            newTaskList.setRuolo(Utenti.Ruoli.CO_UOT.toString());
            newTaskList.setCampoFlow1("E' stato creato un intervento di proroga del tipo  " + interventoFiglio.getTipologiaIntervento().getDesTipint() + " con i seguenti dati :\n" +
                    "Data di partenza:" + dtAvvio + "\n" +
                    durata + "\n" +
                    "Importo assegnato euro: " + quantita);
            TaskDao tdao = new TaskDao(entityManager);
            tdao.insert(newTaskList);
            logger.warn("Prorogato intervento");

        } catch (Throwable th) {
            String pkIntervento = SchedulerHelper.dumpPkIntervento(interventoPadre);
            throw new RinnovoException("ERROR su " + pkIntervento, th);
        }

    }

    public boolean is_intervento_su_piu_anni(PaiIntervento interventoFiglio, Date dtAvvio, DateTime fineAnno, DateTime inizioAnno) {
        return new DateTime(interventoFiglio.calculateDtFine()).isAfter(fineAnno) && new DateTime(dtAvvio).isBefore(inizioAnno);
    }

    String log_rinnova_per_determinia(PaiIntervento interventoPadre, PaiIntervento interventoFiglio) {
        String logMsg = "";
        logMsg = String.format(RINNOVO_PER_DETERMINA_LOG_TEMPLATE, SchedulerHelper.dumpPkIntervento(interventoPadre), SchedulerHelper.dumpPkIntervento(interventoFiglio));
        logger.info(logMsg);
        return logMsg;
    }

    private void fill_interventi_associati(PaiIntervento interventoFiglio, PaiIntervento interventoPadre) {
        InterventiAssociati interventiAssociati = new InterventiAssociati();
        interventiAssociati.setInterventoPadre(interventoPadre);
        interventiAssociati.setInterventoFiglio(interventoFiglio);
        interventiAssociati.setTipoLegame("DETERMINA_PARZIALE");
        interventoFiglio.setInterventoPadre(interventiAssociati);
    }

}
