package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.Associazione;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.testng.Assert.*;

public class AssociazioneDaoTest {
    @Test
    public void findById() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AssociazioneDao associazioneDao = new AssociazioneDao(entityManager);
        Associazione associazione = associazioneDao.findById(2);
        System.out.println(associazione);
    }


    @Test
    public void testFindAll() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AssociazioneDao associazioneDao = new AssociazioneDao(entityManager);
        List<Associazione> associazioneList = associazioneDao.findAll();
        System.out.println(associazioneList);
    }

}