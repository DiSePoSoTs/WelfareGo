package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.Ente;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Struttura;
import it.wego.welfarego.persistence.entities.Tariffa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaiInterventoDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void find_interventi_aperti_che_terminano_entro_native_qery(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        List<InterventoDto> dtos = paiInterventoDao.find_interventi_aperti_che_terminano_entro_native(new Date());

        System.out.println(dtos.size());
        System.out.println(dtos.get(0).toString());

    }


    @Test
    public void findByKey() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        PaiDao paiDao = new PaiDao(entityManager);
        Pai pai = paiDao.findPai(22703995);
        List<PaiIntervento> paiInterventoList = pai.getPaiInterventoList();

        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        PaiIntervento paiIntervento = paiInterventoDao.findByKey(22703995, "MI005", 1);
        Tariffa tariffa = paiIntervento.getTariffa();
        Struttura struttura = tariffa.getStruttura();
        Ente ente = struttura.getEnte();

    }

    @Test
    public void aa(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        List<PaiIntervento> byStatus = paiInterventoDao.findByStatus(PaiIntervento.STATO_INTERVENTO_APERTO);
        System.out.println(byStatus);
    }



    @Test
    public void getInterventiPerProrogaAutomatica() throws ParseException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now = sdf.parse("05/11/2018");
        List<InterventoDto> list = pdao.getInterventiPerProrogaAutomatica_native(new Date());
        System.out.println(list.size());
        System.out.println(list.get(0));

    }

    @Test
    public void findByCodPai() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        PaiDao paiDao = new PaiDao(entityManager);

        List<PaiIntervento> interventiPerPai = pdao.findByCodPai(22457539);
        for (PaiIntervento paiIntervento : interventiPerPai) {
            String codTipint = paiIntervento.getPaiInterventoPK().getCodTipint();
            if (codTipint.equals("MI005")) {
                System.out.println(codTipint);
            }
        }
    }

    @Test
    public void getInterventiDaApprovare() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        PaiDao paiDao = new PaiDao(entityManager);
        Pai pai = paiDao.findLastPai(20323639);

        List<PaiIntervento> interventiDaApprovare = pdao.getInterventiDaApprovare(pai);
        System.out.println(interventiDaApprovare.size());
    }

    @Test
    public void findByStatus() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        List<PaiIntervento> paiInterventiByStatus = pdao.findByStatus(PaiIntervento.STATO_INTERVENTO_ESECUTIVO);
        System.out.println(paiInterventiByStatus);

    }


    @Test
    public void testFindByCodTipintEAssoziazione() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        String codTipint = "SCRT002E";
        Integer idAssocazione = 4;
        Date from = sdf.parse("01/09/2017");
        Date to = sdf.parse("31/12/2017");
        List<PaiIntervento> interventos = pdao.findByCodTipintEAssoziazione(codTipint, idAssocazione, from, to);
        System.out.println("__interventos: " + interventos);
    }

    @Test
    public void testCalcolaCostoIntervento() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        Integer codAna = 20323639;
        List<PaiIntervento> interventi = pdao.findInterventiForSocialCrtByUserVisible(codAna, 20, 2);
        System.out.println("__ interventi: " + interventi.size() + ", " + interventi.toString());

        Long totRecords = pdao.countInterventiForSocialCrtByUserVisible(codAna);
        System.out.println("__" + totRecords);
    }


    @Test
    public void contaTotRecords() {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
        Integer codAna = 20323639;
        Long totRecords = pdao.countInterventiForSocialCrtByUserVisible(codAna);
        System.out.println(totRecords);
    }
}