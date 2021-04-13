package it.wego.welfarego.servlet.anagrafica;

import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.LuogoDao;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 29/11/18.
 */
public class AnagraficaSocServletTest {

    @Test
    public void testSalva_anagrafica_persona_giuridica() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        LuogoDao luogoDao = new LuogoDao(entityManager);
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        AnagraficaSocServlet anagraficaSocServlet = new AnagraficaSocServlet();
        Map<String, String> data = new HashMap<String, String>();
//
        data.put("ragioneSociale", "ACEGAS");
        data.put("codiceAnagrafica", "24588413");
        data.put("IBAN", "IT88Q0760102400000003626357");
        data.put("codiceFiscale", "");
        data.put("partitaIva", "00930530324");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_STATO    ,  "100");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_PROVINCIA, "032");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_COMUNE   , "032006");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_VIA      , "via del Teatro");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_CIVICO   , "15");
        data.put(AnagraficaSocServlet.SEDE_LEGALE_CAP      , "34121");

        anagraficaSocServlet.salva_anagrafica_persona_giuridica(data, luogoDao, anagrafeSocDao, entityManager);
    }

}