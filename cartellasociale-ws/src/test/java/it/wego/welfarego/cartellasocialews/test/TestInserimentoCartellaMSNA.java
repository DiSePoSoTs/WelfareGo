package it.wego.welfarego.cartellasocialews.test;

import java.io.File;

import org.junit.Test;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.SyncWS;
import it.wego.welfarego.cartellasocialews.beans.RicevutaCartella;

public class TestInserimentoCartellaMSNA extends AbstractTest {

    @Test
    public void testInserimentoCartellaMSNA() throws Throwable {
        logger.info("testing inserimentoCartellaSociale MSNA");
        try {

        	File csvFile = new File("C:\\Users\\GOBBOG\\git\\WelfareGo\\cartellasociale-ws\\src\\test\\resources\\MSNA.csv");
			
        	if(!csvFile .exists()) {
        		logger.error("Il file CSV dei MSNA non esiste");
        		return;
        	} 
        	
        	try {
                SyncWS.newInstance().withCsvFileMSNA(csvFile).sincronizzaMSNAWs();
            } catch (Throwable t) {
                logger.error("got error : ", t);
                throw t;
            } finally {

            }
        	
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        }
    	
    }
	
}
