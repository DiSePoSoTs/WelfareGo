package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class BudgetTipoInterventoDaoTest {


    @Test
    public void isInterventoSenzaBudget(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        Assert.assertTrue(budgetTipoInterventoDao.isInterventoSenzaBudget("AZ020"));

    }


    @Test
    public  void bb(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        List<BudgetTipIntervento> impegni = budgetTipoInterventoDao.findByCodTipint("");
    }


    @Test
    public  void aa(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        String codTipoInt = "AZ008A";
        String filtroAnni = "2017, 2016";
        filtroAnni= filtroAnni.replaceAll(", ", ",");
        String[] anniAsString = filtroAnni.split(",");
        List anni = Arrays.asList(anniAsString);
        List<BudgetTipIntervento> data = budgetTipoInterventoDao.findByCodTipInt_And_Cod_Anno(codTipoInt, anni, 100, 0);
        System.out.println(data);
    }
}