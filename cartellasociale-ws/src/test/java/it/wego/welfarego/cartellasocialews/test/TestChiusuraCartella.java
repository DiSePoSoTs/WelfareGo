/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaChiudiCartella;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aleph
 */
@Ignore
public class TestChiusuraCartella extends AbstractTest {

    @Test
    public void testChiusuraCartella() throws Throwable {
        logger.info("testing chiusuraCartella");
        try {
            RicevutaChiudiCartella ricevutaChiudiCartella = cartellaSocialeWsClient.chiudiCartellaSociale();
            logger.info("got response : {}", JsonBuilder.getGsonPrettyPrinting().toJson(ricevutaChiudiCartella));
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    }
}
