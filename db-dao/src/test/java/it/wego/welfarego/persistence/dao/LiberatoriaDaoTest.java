package it.wego.welfarego.persistence.dao;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.text.ParseException;
import java.util.List;

import static org.testng.Assert.*;

public class LiberatoriaDaoTest {

    @Test
    public  void aa() throws ParseException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        LiberatoriaDao ldao= new LiberatoriaDao(entityManager);
    }

}