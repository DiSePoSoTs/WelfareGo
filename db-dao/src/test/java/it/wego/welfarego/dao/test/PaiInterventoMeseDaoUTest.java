package it.wego.welfarego.dao.test;

import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * classe creata per sistemare un problema in prod
 */
public class PaiInterventoMeseDaoUTest {


    @Test
    public void aa() {
        boolean useDecimal = false;
        BigDecimal spesaTot = null;
        List<Integer> distribuzioneCosti = null;
        try{
            PaiInterventoMeseDao.distribuisciCosti(spesaTot, distribuzioneCosti, useDecimal);
            fail("dovevo entrare in IllegalArgumentException");
        }catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().contains("distribuzioneCosti non deve esseree vuoto o null"), ex.getMessage());
        }catch (Exception ex){
            fail("dovevo entrare in IllegalArgumentException");
        }

        try{
            distribuzioneCosti = new ArrayList<Integer>();
            PaiInterventoMeseDao.distribuisciCosti(spesaTot, distribuzioneCosti, useDecimal);
            fail("dovevo entrare in IllegalArgumentException");
        }catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().contains("distribuzioneCosti non deve esseree vuoto o null"));
        }catch (Exception ex){
            fail("dovevo entrare in IllegalArgumentException");
        }
    }


    private Iterable<Integer> getDistribuzioneCosti() {
        List<Integer> dc = new ArrayList<Integer>();
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        dc.add(1);
        return dc;
    }

}
