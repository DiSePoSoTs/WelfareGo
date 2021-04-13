package it.wego.welfarego.persistence.dao;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.welfarego.persistence.entities.*;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class PaiInterventoMeseDaoITest {


    @Test
    public  void confirmAllProps() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        PaiInterventoPK paiInterventoPK = getPaiInterventoPK_4();
        PaiIntervento paiIntervento = paiInterventoDao.findByKey(paiInterventoPK.getCodPai(), paiInterventoPK.getCodTipint(), paiInterventoPK.getCntTipint());
        paiInterventoMeseDao.confirmAllProps(paiIntervento);
    }



    @Test
    public void distribuisciCosti(){
        BigDecimal costoTot = new BigDecimal(7800);
        ArrayList<Integer> intervalliDiDistribuzione= Lists.newArrayList(Iterables.limit(Iterables.cycle(1), 7));
        Iterable<BigDecimal> bigDecimals = PaiInterventoMeseDao.distribuisciCosti(costoTot, intervalliDiDistribuzione, true);
        System.out.println(bigDecimals);
    }


    private PaiInterventoPK getPaiInterventoPK_0() {
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCodTipint("AZ016");
        pk.setCodPai(24559239);
        pk.setCntTipint(1);
        return pk;
    }
    private PaiInterventoPK getPaiInterventoPK_1() {
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCodTipint("AZ008A");
        pk.setCodPai(23941421);
        pk.setCntTipint(2);
        return pk;
    }

    private PaiInterventoPK getPaiInterventoPK_2() {
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCodTipint("AZ008A");
        pk.setCodPai(24657637);
        pk.setCntTipint(1);
        return pk;
    }

    private PaiInterventoPK getPaiInterventoPK_3() {
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCodTipint("AZ008A");
        pk.setCodPai(23146964);
        pk.setCntTipint(1);
        return pk;
    }

    private PaiInterventoPK getPaiInterventoPK_4() {
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCodTipint("AZ007A");
        pk.setCodPai(23759517);
        pk.setCntTipint(1);
        return pk;
    }



    @Test
    public void testFindForPaiInt() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
        int cntTipint = 2;
        String codTipint = "AZ016";
        int codPai = 24454781;
        List<PaiInterventoMese> forPaiInt = paiInterventoMeseDao.findForPaiInt(codPai, codTipint, cntTipint);
        System.out.println(forPaiInt.get(0));
    }


    @Test
    public void aa() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateTime dataInizio = new DateTime(sdf.parse("01/08/2018"));
        DateTime nuovaDataFine = new DateTime(sdf.parse("01/09/2018"));
        System.out.println(nuovaDataFine.isAfter(dataInizio));
    }

}