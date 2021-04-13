package it.wego.welfarego.scheduler.rinnovi;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.scheduler.RinnovoException;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import it.wego.welfarego.services.interventi.ProrogaInterventoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;

public class RinnovoPerProroga {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private PersistenceAdapter persistenceAdapter;
    private EntityManager entityManager;
    private PaiInterventoDao paiInterventoDao;

    public RinnovoPerProroga(PersistenceAdapter persistenceAdapter, EntityManager entityManager, PaiInterventoDao paiInterventoDao) {
        this.persistenceAdapter = persistenceAdapter;
        this.entityManager = entityManager;
        this.paiInterventoDao = paiInterventoDao;
    }

    public void rinnova(PaiIntervento interventoPadre) throws RinnovoException {
        logger.info(String.format("rinnovo automatico: %s", SchedulerHelper.dumpPkIntervento(interventoPadre)));

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
            PaiInterventoMese mese = null;
            BudgetTipIntervento budget = null;

            persistenceAdapter.initTransaction();
            Integer codPai = interventoPadre.getPaiInterventoPK().getCodPai();
            String codTipInt = interventoPadre.getPaiInterventoPK().getCodTipint();
            Integer cntTipInt = interventoPadre.getPaiInterventoPK().getCntTipint();
            PaiIntervento old = paiInterventoDao.findByKey(codPai, codTipInt, cntTipInt);
            ProrogaInterventoService prorogaInterventoService = new ProrogaInterventoService(paiInterventoDao);
            PaiIntervento nuovoIntervento = prorogaInterventoService.prorogaIntervento(interventoPadre);

            CartellaSocialeWsClient cartellaSocialeWsClient = CartellaSocialeWsClient.newInstance().withEntityManager(entityManager);
            cartellaSocialeWsClient = cartellaSocialeWsClient.loadConfigFromDatabase();
            cartellaSocialeWsClient = cartellaSocialeWsClient.withPaiIntervento(nuovoIntervento);
            cartellaSocialeWsClient.sincronizzaIntervento();

            persistenceAdapter.commitTransaction();

            old.setRinnovato(PaiInterventoDao.RINNOVATO);
            paiInterventoDao.update(old);

            String msgTemplate = "rinnovo per proroga,\t\tinterventi (padre, figlio): {\"interventi\":{\"padre\":%s, \"figlio\":%s}}";
            String pkInterventoFiglio = SchedulerHelper.dumpPkIntervento(nuovoIntervento);
            String pkInterventoPadre = SchedulerHelper.dumpPkIntervento(interventoPadre);
            logMsg = String.format(msgTemplate, pkInterventoPadre, pkInterventoFiglio);
            logger.info(logMsg);


            //duplico il budget
            PaiInterventoMeseDao pdao = new PaiInterventoMeseDao(entityManager);
            List<PaiInterventoMese> mesi = pdao.findForPaiInt(codPai, codTipInt, cntTipInt);
            if (!mesi.isEmpty()) {
                budget = mesi.get(0).getBudgetTipIntervento();
            }
            BigDecimal quantita = nuovoIntervento.getQuantita();
            Pai pai = nuovoIntervento.getPai();
            Integer durMesi = nuovoIntervento.getDurMesi();
            if (budget != null) {
                BigDecimal durMesiNuovoIntervento = new BigDecimal(durMesi);
                BigDecimal spesa = quantita.multiply(durMesiNuovoIntervento);
                PaiInterventoPK paiInterventoPK = nuovoIntervento.getPaiInterventoPK();
                BudgetTipInterventoPK budgetTipInterventoPK = budget.getBudgetTipInterventoPK();
                BigDecimal impStdCosto = nuovoIntervento.getTipologiaIntervento().getImpStdCosto();
                BigDecimal divide = spesa.divide(impStdCosto, MathContext.DECIMAL32);
                ParametriIndata idParamFascia = pai.getIdParamFascia();
                pdao.insertProp(paiInterventoPK, budgetTipInterventoPK, spesa, divide, idParamFascia);
            }
            old.setRinnovato(PaiInterventoDao.RINNOVATO);
            paiInterventoDao.update(old);
            PaiEventoDao pedao = new PaiEventoDao(entityManager);

            Utenti codUteAs = interventoPadre.getPai().getCodUteAs();
            PaiEvento evento = pedao.insertEvento(nuovoIntervento, PaiEvento.PAI_CONFERMA_INTERVENTO);
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
            pedao.update(evento);


            UniqueTasklist newTaskList = new UniqueTasklist(
                    "Notifica proroga intervento",
                    "N",
                    "N");
            newTaskList.setPai(pai);
            newTaskList.setPaiIntervento(nuovoIntervento);
            newTaskList.setForm(entityManager.getReference(UniqueForm.class, "70"));
            newTaskList.setUot(pai.getIdParamUot().getIdParam().getCodParam());
            newTaskList.setRuolo(Utenti.Ruoli.CO_UOT.toString());
            String desTipint = nuovoIntervento.getTipologiaIntervento().getDesTipint();
            Date dtAvvio = nuovoIntervento.getDtAvvio();
            newTaskList.setCampoFlow1("E' stato creato un intervento di proroga del tipo  " + desTipint + " con i seguenti dati :\n" +
                    "Data di partenza:" + dtAvvio + "\n" +
                    "Durata mesi: " + durMesi + "\n" +
                    "Importo assegnato euro: " + quantita);
            TaskDao tdao = new TaskDao(entityManager);
            tdao.insert(newTaskList);

            logger.warn("Prorogato intervento");
        } catch (Throwable th) {
            String pkInterventoPadre = SchedulerHelper.dumpPkIntervento(interventoPadre);
            throw new RinnovoException("ERROR su " + pkInterventoPadre, th);
        }


    }

}
