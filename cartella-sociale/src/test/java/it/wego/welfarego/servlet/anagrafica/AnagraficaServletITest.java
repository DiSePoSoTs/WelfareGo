package it.wego.welfarego.servlet.anagrafica;

import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.NoteCondiviseDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.NoteCondivise;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class AnagraficaServletITest {
    @Test
    public  void test_anagraficaServlet() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        String codiceAnagraficoComune = null;
        String codiceFiscale = "DMTSNT80A56L424F";
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        VistaAnagrafe vistaAnagrafe = anagraficaServlet.getVistaAnagrafe(vistaAnagrafeDao, codiceAnagraficoComune, codiceFiscale);
        AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodFisc(codiceFiscale);
        List<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differences = vistaAnagrafeDao.getVistaAnagraficaToAnagrafeSocDifferences(vistaAnagrafe, anagrafeSoc);
        String messaggioEsteso = anagraficaServlet.preparaMessaggioEsteso(differences, null, null);
        System.out.println(messaggioEsteso.length() + " - " + messaggioEsteso);
    }


    @Test
    public void test_GetVistaAnagrafe(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
        String codiceAnagraficoComune = null;
        String codiceFiscale = "DMTSNT80A56L424F";
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        VistaAnagrafe vistaAnagrafe = anagraficaServlet.getVistaAnagrafe(vistaAnagrafeDao, codiceAnagraficoComune, codiceFiscale);
        assertTrue(vistaAnagrafe.getNome().equalsIgnoreCase("SAMANTHA"));

    }

    @Test
    public void retifica_anagrafica_inserita_da_utenti_non_associati_al_comune() throws Exception {
        //pre - condizioni
        String codiceAnagraficoComune = null;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        Utenti connectedUser = new UtentiDao(entityManager).findByCodUte("29");


        // given: anagrafica inserita da soggetti non associati al comune.
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        int cod_ana = 20297007;
        String codiceAnagraficoWellfarGo = cod_ana + "";
        String codiceFiscale = "DMTSNT80A56L424F";
        AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodFisc(codiceFiscale);
        assertTrue(anagrafeSoc.getDataUltimaRettifica() == null);

        //when dopo il verifica si accetta la sovrascrittura dei dati
        anagraficaServlet.actionSincronizzaDaAnagrafeSoc(codiceAnagraficoWellfarGo,codiceFiscale,codiceAnagraficoComune,entityManager, vistaAnagrafeDao, connectedUser);

        //then sincronizzo le anagrafiche, se è il caso, creo nota condivisa, valorizzo il campo data_retifica
        NoteCondiviseDao noteCondiviseDao = new NoteCondiviseDao(entityManager);
        List<NoteCondivise> noteCondivise = noteCondiviseDao.getNoteByCodAna(20297007);
        assertTrue(noteCondivise.size() == 1, noteCondivise.size() + "");

        anagrafeSoc = anagrafeSocDao.findByCodFisc(codiceFiscale);
        assertTrue(anagrafeSoc.getDataUltimaRettifica() != null);
    }

}
