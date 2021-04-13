package it.wego.welfarego.servlet.cartella;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * WELGO-3: verifica, correzione e tracciatura delle correzioni dei dati delle anagrafiche inseriti dalle cooperative
 * Caso d'uso:
 * Data un' anagrafica inserita da un operatore esterno al comune
 * Quando un operatore comunale clicca su "verifica" e poi accetta le sostituzioni dei dati
 * allora creo nota condivisa
 */
public class PopolaDatiServletITest {
    @Test
    public void testActionAnagrafica() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        EntityManager entityManager = factory.createEntityManager();

        PopolaDatiServlet popolaDatiServlet = new PopolaDatiServlet();

//        20297007	DMTSNT80A56L424F
//        20297147	BTTSWN87M09L424A
//        20297151	SMRDNY78E06L424D
//        20296731	PSNMRG86M46L424E
//        20296735	PSNMST88M61L424K
//        20296739	DRSLRD88E08L424O
//        20296743	TRVDNI86E67L424E
//        20296747	CHRMNL88T11L424G
//        20296751	HRVDRS84E68L424F
//        20296755	PRTRKE89A43L424Y
//        20296759	RPSDVD88B24L424Q

        // given: anagrafica inserita da soggetti != da soggetti associati al comune.
//        String codiceAnagraficoWellfarGo = "20344799";

        String codAnagString = "20344799";
        Object o = popolaDatiServlet.actionAnagrafica(codAnagString, entityManager);
        System.out.println(o);
    }

}