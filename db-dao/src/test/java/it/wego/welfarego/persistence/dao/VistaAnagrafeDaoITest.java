package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.testng.Assert.*;


public class VistaAnagrafeDaoITest {

    @Test
    public void test_get_by_codice_fiscale(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
        vistaAnagrafeDao.findByCodiceFiscale("CVTSBN47M09A662W");
    }

    @Test
    public void testVistaAnagraficaToAnagrafeSoc() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);

        String codiceFiscale = "DMTSNT80A56L424F";
        AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodFisc(codiceFiscale);
        VistaAnagrafe vistaAnagrafe = vistaAnagrafeDao.findByCodiceFiscale(codiceFiscale);
        List<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differences = vistaAnagrafeDao.getVistaAnagraficaToAnagrafeSocDifferences(vistaAnagrafe, anagrafeSoc);
        assertTrue(differences.size() == 1);
    }

}