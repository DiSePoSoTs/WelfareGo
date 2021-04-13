package it.wego.welfarego.dao.test;

import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaParametri;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ParametriIndataDaoTest {

    @Test
    public  void aa(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        ParametriIndataDao parametriIndataDao = new ParametriIndataDao(entityManager);

        List<ParametriIndata> tipParamAttivo = parametriIndataDao.findByTipParamAttivo(Parametri.CLASSE_INTERVENTO);
        System.out.println(tipParamAttivo);
        assertTrue(tipParamAttivo.size() > 1);
    }
}
