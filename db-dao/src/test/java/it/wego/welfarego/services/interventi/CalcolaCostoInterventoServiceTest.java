package it.wego.welfarego.services.interventi;

import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Date;

public class CalcolaCostoInterventoServiceTest {


    @Test
    public void calcolaBdgPrevEur(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPU_PROD");
        EntityManager entityManager = factory.createEntityManager();
        CalcolaCostoInterventoService service = new CalcolaCostoInterventoService();
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        TaskDao taskDao = new TaskDao(entityManager);
        UniqueTasklist task = taskDao.findTask("24886593");
        PaiIntervento paiIntervento = task.getPaiIntervento();
        BigDecimal bigDecimal = service.calcolaBdgPrevEur(paiIntervento);
        System.out.println(bigDecimal);
    }


    @Test
    public void aa(){
        CalcolaCostoInterventoService service = new CalcolaCostoInterventoService();
        TipologiaIntervento ti = new TipologiaIntervento();
        ParametriIndata idParam = new ParametriIndata();
        idParam.setDesParam("euro");
        ti.setIdParamUniMis(idParam);
        ti.setImpStdCosto(new BigDecimal(1));
        BigDecimal quantita = new BigDecimal(1);
        Integer durataAsInteger =2 ;
        Date dtAvvio = new Date();
        BigDecimal costo = service.calcolaCostoInterventoPerDurata(ti, quantita, durataAsInteger, dtAvvio);
        System.out.println(costo);
    }

}