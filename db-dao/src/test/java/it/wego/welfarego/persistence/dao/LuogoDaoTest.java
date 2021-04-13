package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.Luogo;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.testng.Assert.*;

/**
 * Created by max on 29/11/18.
 */
public class LuogoDaoTest {

    @Test
    public void aa(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        LuogoDao luogoDao = new LuogoDao(entityManager);
        String sedeLegaleStato = "100";
        String sedeLegaleProvincia = "032";
        String sedeLegaleComune = "032006";
        String sedeLegaleVia = "via del Teatro";
        String sedeLegaleCivico = "15";
        String sedeLegaleCap = "34121";

        Luogo luogo = luogoDao.newLuogo(
                sedeLegaleStato,
                sedeLegaleProvincia,
                sedeLegaleComune,
                sedeLegaleVia,
                sedeLegaleCivico,
                sedeLegaleCap);

        System.out.println(luogo);
    }

}