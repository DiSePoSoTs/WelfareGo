/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaCartella;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
@Ignore
public class TestInserimentoCartella extends AbstractTest {

    @Test
    public void testInserimentoCartella() throws Throwable {
        logger.info("testing inserimentoCartellaSociale");
        try {
            RicevutaCartella ricevutaCartella = cartellaSocialeWsClient.inserimentoCartellaSociale();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(ricevutaCartella));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
