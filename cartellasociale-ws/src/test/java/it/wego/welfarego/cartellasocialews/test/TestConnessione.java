/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author aleph
 */
public class TestConnessione extends AbstractTest {

	//@Test
	public void testConnessione() throws Throwable {
		logger.info("testing connection");
		try {
			cartellaSocialeWsClient.testConnection();
			logger.info("testing connection : OK");
		} catch (Throwable t) {
			logger.error("got error : ", t);
			throw t;
		}
	}
	
	
	@Test
	public void testSoapConnessione() throws Throwable {
		logger.info("testing connection");
		try {
			cartellaSocialeWsClient.testSOAPConnection();
			logger.info("testing connection : OK");
		} catch (Throwable t) {
			logger.error("got error : ", t);
			throw t;
		}
	}
}
