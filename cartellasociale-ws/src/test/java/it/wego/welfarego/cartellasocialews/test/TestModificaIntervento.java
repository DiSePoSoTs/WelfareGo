/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaModificaIntervento;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
@Ignore
public class TestModificaIntervento extends AbstractTest {

    @Test
    public void testModificaIntervento() throws Throwable {
        logger.info("testing modificaIntervento");
        try {
            RicevutaModificaIntervento modificaIntervento = cartellaSocialeWsClient.modificaIntervento();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(modificaIntervento));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
