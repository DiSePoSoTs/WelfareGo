package it.wego.welfarego.scheduler.chiusure;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class ChiusuraAutomatica {

    private static final Logger logger = LoggerFactory.getLogger(ChiusuraAutomatica.class);
    private static final Logger loggerRinnoviScartati = LoggerFactory.getLogger("rinnoviScartati");
    private static final String ESITO_INT_CHIUSURA_AUTOMATICA = null;

    public static void chiudi_interventi_terminati_adesso(
            PersistenceAdapter persistenceAdapter
            , List<PaiIntervento> defectivePaiInt
            , EntityManager entityManager
            , PaiInterventoDao paiInterventoDao
    ) {
        Thread thread = Thread.currentThread();
        Date now = new Date();
        logger.debug("chiudiamo gli interventi terminati al {}", now);

        List<InterventoDto> interventiDaChiudere = paiInterventoDao.find_interventi_aperti_che_terminano_entro_native(now);

        for (InterventoDto interventoDto : interventiDaChiudere) {
            if (thread.isInterrupted()) {
                break;
            }

            PaiIntervento paiIntervento = paiInterventoDao.findByKey(interventoDto);
            Date dtFine = paiIntervento.calculateDtFine();
            if(dtFine.after(now)){
                continue;
            }

            persistenceAdapter.initTransaction();
            if (paiIntervento.getTipologiaIntervento().deveRestareAperto() == true) {
                loggerRinnoviScartati.info("TipologiaIntervento.FLG_RINNOVO_AUTO = S, non chiudo per l'intervento = {}", paiIntervento);
            } else {
                logger.debug("chiusura automatica intervento terminato = {}", paiIntervento);
                paiIntervento.setDtChius(now);
                paiIntervento.setStatoAttuale(PaiIntervento.CHIUSO);
                paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                if (StringUtils.isBlank(paiIntervento.getNoteChius())) {
                    paiIntervento.setNoteChius("procedura automatica: intervento concluso senza note particolari");
                }
                paiIntervento.setIndEsitoInt(ESITO_INT_CHIUSURA_AUTOMATICA);
                new PaiInterventoMeseDao(entityManager).chiudiProposteIntervento(paiIntervento);

            }
            persistenceAdapter.commitTransaction();

        }

    }




    /**
     * <pre>
     * Se un paiInterventoMese ha il paicdg non generato, non chiudiamo l'intervento, ci sono cose che devono essere mandate avanti.
     * Se un paiInterventoMese NON ha passato lo 'status da generare', non chiudiamo l'intervento.
     * </pre>
     *
     * @param paiIntervento
     * @param persistenceAdapter
     * @param now
     * @param dataFine
     */
    public static void chiudi_intervento(PaiIntervento paiIntervento, PersistenceAdapter persistenceAdapter, Date now, Date dataFine) {
        if (dataFine.before(now)) {
            persistenceAdapter.initTransaction();

            //primo check.. vediamo se tutti i mesi dell'intervento sono passati in acquisizione dati...
            List<PaiInterventoMese> list = paiIntervento.getPaiInterventoMeseList();
            boolean closeable = true;

            for (PaiInterventoMese pim : list) {
                // CONTROLLO 1: paicdg assente
                //se ha ancora un paicdg non generato.. significa che ancora ci sono cose che devono essere mandate avanti.. indi per cui non chiudiamo
                if (pim.getPaiCdg() == null) {
                    closeable = false;
                    break;
                }

                // CONTROLLO 2: status da generare
                //chek numero due vediamo se ha passato lo status 'da generare... se si '
                if (pim.getIdManDettaglio() == null && pim.getIdFattDettaglio() == null) {
                    closeable = false;
                    break;
                }
            }

            if (closeable) {
                paiIntervento.setDtChius(now);
                paiIntervento.setStatoAttuale(PaiIntervento.CHIUSO);
                paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                if (StringUtils.isBlank(paiIntervento.getNoteChius())) {
                    paiIntervento.setNoteChius("intervento concluso senza note particolari");
                }
                paiIntervento.setIndEsitoInt(ESITO_INT_CHIUSURA_AUTOMATICA); // null ??
            }
            persistenceAdapter.commitTransaction();

            logger.debug("chiusura automatica terminata su intervento: {}, closeable: {}", SchedulerHelper.dumpPkIntervento(paiIntervento), closeable);
        }
    }

}
