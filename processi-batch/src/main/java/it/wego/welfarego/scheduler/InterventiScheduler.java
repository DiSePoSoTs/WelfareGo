package it.wego.welfarego.scheduler;

import com.google.common.collect.Lists;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.scheduler.chiusure.ChiusuraAutomatica;
import it.wego.welfarego.scheduler.rinnovi.RinnovoAutomatico;
import it.wego.welfarego.scheduler.rinnovi.RinnovoPerDetermina;
import it.wego.welfarego.scheduler.rinnovi.RinnovoPerProroga;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class InterventiScheduler implements Runnable {
    public static final String SCARTO_INTERVENTO_LOG_TEMPLATE = "scarto intervento: %s per questi motivi: [isOneMonthFromNow:%s, isNotRinnovato(già elaborato):%s, isStatoNonChiuso:%s]";
    private static final Logger logger = LoggerFactory.getLogger(InterventiScheduler.class);
    private static final Logger ok_logger = LoggerFactory.getLogger("ok_logger");
    private static final Logger loggerRinnoviScartati = LoggerFactory.getLogger("rinnoviScartati");

    private PersistenceAdapter persistenceAdapter = null;

    public InterventiScheduler() {
        persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
    }

    public InterventiScheduler(PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
    }


    public void run() {
        String uuid = UUID.randomUUID().toString();
        logger.info("starting scheduler: " + uuid);
        EntityManager entityManager = persistenceAdapter.getEntityManager();
        if (entityManager == null) {
            logger.error("entityManager is null");
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPU");
            entityManager = factory.createEntityManager();
            persistenceAdapter.setEntityManager(entityManager);
        }

        Date now = new Date();
        Thread thread = Thread.currentThread();
        List<PaiIntervento> defectivePaiInt = Lists.newArrayList();
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);

        try {

            logger.info("inizio la ChiusuraAutomatica");

            ChiusuraAutomatica.chiudi_interventi_terminati_adesso(persistenceAdapter, defectivePaiInt, entityManager, paiInterventoDao);

            logger.info("fine della ChiusuraAutomatica");

            interventi_in_esecutivita(persistenceAdapter, defectivePaiInt, thread, entityManager, paiInterventoDao, now);

            logger.warn(SchedulerHelper.interventi_aperti_senza_data_fine_o_durata(defectivePaiInt));

        } catch (Throwable t) {
            logger.error("error douring execution of scheduled operations", t);

        } finally {
            if (persistenceAdapter != null) {
                persistenceAdapter.close();
            }
        }

        logger.info("scheduler complete: " + uuid);
    }


    /*
    * p er portare un intervento in stato di esecutivita dalla sezione task list, si cerca l'intervento
    * doppio click,
    * nella maschera delle azioni inserisco un numero di determina ed un numero di protocollo es: 1,1
    * click su ok
    **/
    void interventi_in_esecutivita(PersistenceAdapter persistenceAdapter, List<PaiIntervento> defectivePaiInt, Thread thread, EntityManager entityManager, PaiInterventoDao paiInterventoDao, Date now) throws Exception {

        logger.info(String.format("eseguo la select per gli interventi da rinnovare "));
        List<InterventoDto> interventiPerProrogaAutomatica = paiInterventoDao.getInterventiPerProrogaAutomatica_native(now);

        logger.info(String.format("--------> elaboro gli interventi in stato esecutivo: %d <----------", interventiPerProrogaAutomatica.size()));

        for (InterventoDto interventoDto : interventiPerProrogaAutomatica) {

            PaiIntervento paiIntervento = paiInterventoDao.findByKey(interventoDto);

            logger.info(interventoDto.toString());

            if (thread.isInterrupted()) {
                break;
            }
            Date dataFine = paiIntervento.calculateDtFine();
            if (dataFine == null) {
                defectivePaiInt.add(paiIntervento);
                logger.trace("intervento aperto senza data fine o durata = {}", paiIntervento);
                continue;
            }


            try {
                rinnova_intervento_in_stato_di_esecutivita(paiIntervento, persistenceAdapter, entityManager, paiInterventoDao, now);
            } catch (RinnovoException rinnovoException) {
                logger.error("", rinnovoException);
            } catch (Throwable th) {
                logger.error("", th);
            }
        }
        logger.info("fine elaborazione interventi in stato esecutivo.");
    }

    void rinnova_intervento_in_stato_di_esecutivita(PaiIntervento paiIntervento, PersistenceAdapter persistenceAdapter
            , EntityManager entityManager, PaiInterventoDao paiInterventoDao, Date now) throws Exception {

        Date dataFine = paiIntervento.calculateDtFine();

        ChiusuraAutomatica.chiudi_intervento(paiIntervento, persistenceAdapter, now, dataFine);

        Date oneMonthFromNow = new DateTime(now).plusMonths(1).toDate();

        boolean isDaRinnovare = paiIntervento.isRinnovato() == false;
        boolean isOneMonthFromNow = dataFine.before(oneMonthFromNow);
        boolean isStatoNonChiuso = paiIntervento.getPai().getFlgStatoPai() != 'C';

        if (isOneMonthFromNow && isDaRinnovare && isStatoNonChiuso) {

            if (SchedulerHelper.is_Rinnovo_Automatico(paiIntervento)) {
                RinnovoAutomatico rinnovoAutomatico = new RinnovoAutomatico(persistenceAdapter, entityManager, paiInterventoDao);
                rinnovoAutomatico.rinnova(paiIntervento);

            } else if (SchedulerHelper.is_Rinnovo_Per_Proroga(paiIntervento)) {
                RinnovoPerProroga rinnovoPerProroga = new RinnovoPerProroga(persistenceAdapter, entityManager, paiInterventoDao);
                rinnovoPerProroga.rinnova(paiIntervento);

            } else if (SchedulerHelper.is_Rinnovo_Per_Determina_Parziale(paiIntervento)) {
                RinnovoPerDetermina rinnovoPerDetermina = new RinnovoPerDetermina(persistenceAdapter, entityManager, paiInterventoDao);
                rinnovoPerDetermina.rinnova(paiIntervento);

            } else {
                String flgRinnovo = paiIntervento.getTipologiaIntervento().getFlgRinnovo();
                Integer durMesiProroga = paiIntervento.getDurMesiProroga();
                String msgTemplate = "L'intervento %s non rientra dei casi dei rinnovi previsti. FlgRinnovo: %s, durata_mesi_proroga: %d";
                String errMsg = String.format(msgTemplate, SchedulerHelper.dumpPkIntervento(paiIntervento), flgRinnovo, durMesiProroga);
                loggerRinnoviScartati.info(errMsg);
            }
        } else {
            loggerRinnoviScartati.info(log_scarta_intervento(paiIntervento, isOneMonthFromNow, isDaRinnovare, isStatoNonChiuso));
        }
    }

    String log_scarta_intervento(PaiIntervento paiIntervento, boolean isOneMonthFromNow, boolean isNotRinnovato, boolean isStatoNonChiuso) {
        String logMsg = String.format(SCARTO_INTERVENTO_LOG_TEMPLATE, SchedulerHelper.dumpPkIntervento(paiIntervento), isOneMonthFromNow, isNotRinnovato, isStatoNonChiuso);
        return logMsg;
    }


}
