package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

public class AnagrafeSocDaoTest {

    @Test
    public void bb(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        EntityManager entityManager = factory.createEntityManager();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodAna(23050302);
        System.out.println(anagrafeSoc.getLuogoDomicilio());
        System.out.println(anagrafeSoc.getLuogoResidenza());
    }


    @Test
    public  void aa(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        EntityManager entityManager = factory.createEntityManager();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        Query q = entityManager.createNamedQuery("AnagrafeSoc.findByCodAnaFamCom");
        q.setParameter("codAnaFamCom", "3917562");
        List<AnagrafeSoc> resultList = q.getResultList();
        System.out.println(resultList.size());
    }


    @Test
    public void test_update_dati_iniziali_retificati() throws Exception {


        String cf_querciaUser = "CGNNMQ16T07L424e";
        String cf_SenaborRenata = "SNBRNT32L63L424I";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        EntityManager entityManager = factory.createEntityManager();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        AnagrafeSoc senaborRenata = anagrafeSocDao.findByCodFisc(cf_SenaborRenata);
        assertTrue(senaborRenata!= null);
    }


}