package it.wego.welfarego.cartellasocialews.test;

import org.junit.Test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.beans.RicevutaCartella;

public class TestInserimentoCartellaMSNA extends AbstractTest {

    @Test
    public void testInserimentoCartellaMSNA() throws Throwable {
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
