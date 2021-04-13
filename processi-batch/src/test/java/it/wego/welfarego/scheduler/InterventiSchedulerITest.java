package it.wego.welfarego.scheduler;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InterventiSchedulerITest {


    @Test
    public void rinnova_AZ020() throws Exception {

        String persistenceUnitName = "welfaregoPUTest";
//        String persistenceUnitName = "welfaregoPU_PROD";
//        String persistenceUnitName = "PGSQL_wego_2";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnitName);
        PersistenceAdapterFactory.setPersistenceUnit(persistenceUnitName);
        EntityManager entityManager = factory.createEntityManager();
        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.createPersistenceAdapter();
        InterventiScheduler interventiScheduler = new InterventiScheduler(persistenceAdapter);

//        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
//        List<PaiIntervento> defectivePaiInt = new ArrayList<PaiIntervento>();
//        Thread thread = Thread.currentThread();
//        Date now = new Date();
//        PaiIntervento paiIntervento = paiInterventoDao.findByKey(24509624, "AZ020", 1);
//
//        interventiScheduler.rinnova_intervento_in_stato_di_esecutivita(paiIntervento, persistenceAdapter, entityManager, paiInterventoDao, now);
        interventiScheduler.run();

    }

    @Test
    public void chiudi_interventi() throws Exception {

        String persistenceUnitName = "welfaregoPUTest";
//        String persistenceUnitName = "PGSQL_wego_2";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnitName);
        PersistenceAdapterFactory.setPersistenceUnit(persistenceUnitName);
        EntityManager entityManager = factory.createEntityManager();
        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.createPersistenceAdapter();
        InterventiScheduler interventiScheduler = new InterventiScheduler(persistenceAdapter);

        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);

        List<InterventoDto> interventiDaChiudere = paiInterventoDao.find_interventi_aperti_che_terminano_entro_native(new Date());
        System.out.println(interventiDaChiudere.size());

    }



    @Test
    public void aa() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dal = sdf.parse("23/01/2019");
        Date al = sdf.parse("31/12/2019");
        System.out.println(Weeks.weeksBetween(new DateTime(dal), new DateTime(al)).getWeeks());
    }


}