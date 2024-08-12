/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaIntervento;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
@Ignore
public class TestInserimentoIntervento extends AbstractTest {

    @Test
    public void testInserimentoIntervento() throws Throwable {
        logger.info("testing inserimentoIntervento");
        try {
            RicevutaIntervento inserimentoIntervento = cartellaSocialeWsClient.inserimentoIntervento();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(inserimentoIntervento));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
