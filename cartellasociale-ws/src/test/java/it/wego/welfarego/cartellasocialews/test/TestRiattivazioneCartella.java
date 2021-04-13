/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.wego.extjs.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaRiattivaCartella;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
@Ignore
public class TestRiattivazioneCartella extends AbstractTest {

    @Test
    public void testRiattivaCartella() throws Throwable {
        logger.info("testing riattivaCartella");
        try {
            RicevutaRiattivaCartella ricevutaRiattivaCartella = cartellaSocialeWsClient.riattivaCartellaSociale();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(ricevutaRiattivaCartella));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
