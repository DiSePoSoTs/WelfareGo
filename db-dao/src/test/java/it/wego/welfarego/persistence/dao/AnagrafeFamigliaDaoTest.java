package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.testng.Assert.*;

public class AnagrafeFamigliaDaoTest {


    @Test
    public void findFamigliareAnagrafeComunale(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AnagrafeFamigliaDao anagrafeFamigliaDao = new AnagrafeFamigliaDao(entityManager);
        int codAna = 20367139;
        int codAnaFamCom = 20518501;

        AnagrafeFam anagrafeFam = null;
        anagrafeFam = anagrafeFamigliaDao.findByKey(codAna, codAnaFamCom);
        assertEquals(322, (int)anagrafeFam.getCodQual().getIdParamIndata());


        codAna = 20323639;
        codAnaFamCom = 20324739;
        anagrafeFam = anagrafeFamigliaDao.findByKey(codAna, codAnaFamCom);
        assertEquals(385, (int)anagrafeFam.getCodQual().getIdParamIndata());
    }

}