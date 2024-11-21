/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaModificaAnagrafica;
import it.wego.welfarego.cartellasocialews.beans.RicevutaModificaProfilo;
import it.wego.welfarego.cartellasocialews.beans.RicevutaModificaProgetto;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
public class TestModificaCartella extends AbstractTest {

    @Test
    public void testModificaAnagrafica() throws Throwable {
        logger.info("testing modificaAnagrafica");
        try {
            RicevutaModificaAnagrafica ricevutaModificaAnagrafica = cartellaSocialeWsClient.modificaAnagrafica();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(ricevutaModificaAnagrafica));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }

    @Test
    public void testModificaProfilo() throws Throwable {
        logger.info("testing modificaProfilo");
        try {
            RicevutaModificaProfilo modificaProfilo = cartellaSocialeWsClient.modificaProfilo();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(modificaProfilo));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }

    @Test
    public void testModificaProgetto() throws Throwable {
        logger.info("testing modificaProgetto");
        try {
            RicevutaModificaProgetto modificaProgetto = cartellaSocialeWsClient.modificaProgetto();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(modificaProgetto));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
