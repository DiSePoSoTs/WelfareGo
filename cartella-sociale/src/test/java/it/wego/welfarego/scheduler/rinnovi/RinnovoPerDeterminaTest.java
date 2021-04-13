package it.wego.welfarego.scheduler.rinnovi;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.scheduler.RinnovoException;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.testng.Assert.assertTrue;


public class RinnovoPerDeterminaTest {

    private EntityManager entityManager = null;
    private PersistenceAdapter persistenceAdapter = null;
    private PaiInterventoDao paiInterventoDao = null;

    public RinnovoPerDeterminaTest() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        PersistenceAdapterFactory.setPersistenceUnit("welfaregoPUTest");

        persistenceAdapter = PersistenceAdapterFactory.createPersistenceAdapter();
        entityManager = factory.createEntityManager();
        paiInterventoDao = new PaiInterventoDao(entityManager);

    }


    @Test
    public void test_rinnova() throws RinnovoException {

//        errore su rinnovo_per_determina su {"cod_pai":24527116, "cod_tipint":"MI005", "cnt_tipint":2}
        int codPai = 24529963;
        String codTipInt = "MI005";
        int cntTipint = 1;
        PaiIntervento paiIntervento = paiInterventoDao.findByKey(codPai, codTipInt, cntTipint);

        RinnovoPerDetermina rinnovoPerDetermina = new RinnovoPerDetermina(persistenceAdapter, entityManager, paiInterventoDao);
        rinnovoPerDetermina.rinnova(paiIntervento);
    }


    @Test
    public void dumpDefectivePaiInt(){
        RinnovoPerDetermina rinnovoPerDetermina = new RinnovoPerDetermina();
        String logMsg = rinnovoPerDetermina.log_rinnova_per_determinia(getFakeIntervento(55), getFakeIntervento(56));
        assertTrue(logMsg.contains("rinnovo per determina"), logMsg);
        assertTrue(logMsg.contains("\"padre\":{\"codPai\":123,\"codTipint\":\"MI005\",\"cntTipint\":55}"), logMsg);
        assertTrue(logMsg.contains("\"figlio\":{\"codPai\":123,\"codTipint\":\"MI005\",\"cntTipint\":56}"), logMsg);
    }


    private PaiIntervento getFakeIntervento(int i) {

        PaiIntervento paiIntervento = new PaiIntervento();
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCntTipint(i);
        pk.setCodPai(123);
        pk.setCodTipint("MI005");
        paiIntervento.setPaiInterventoPK(pk);
        return  paiIntervento;
    }

}