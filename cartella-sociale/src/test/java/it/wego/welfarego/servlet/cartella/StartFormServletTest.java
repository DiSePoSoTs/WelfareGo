package it.wego.welfarego.servlet.cartella;

import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Utenti;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class StartFormServletTest {
    @Test
    public void testRichiedi_approvazione() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        UtentiDao utentiDao = new UtentiDao(entityManager);
        Utenti ute = utentiDao.findByCodUte("24080651");
        PaiDao paiDao = new PaiDao(entityManager);
        Pai pai = paiDao.findLastPai(20323639);
        StartFormServlet startFormServlet = new StartFormServlet();
        startFormServlet.richiedi_approvazione(entityManager,ute, pai);
    }

}