package it.wego.welfarego.services.gestione_economica;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigInteger;

public class AggiornaMandatiDaSicrawebServiceTest {

    @Test
    public void aa() throws Exception {
        AggiornaMandatiDaSicrawebService aggiornaMandatiDaSicrawebService = new AggiornaMandatiDaSicrawebService();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        Integer idMandato = 24751035;
        BigInteger numeroMandato = BigInteger.valueOf(99999L);
        aggiornaMandatiDaSicrawebService.set_numero_mandato_su_mandato(entityManager, idMandato, numeroMandato);

    }

}