/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.csr;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author aleph
 */
public class StartSyncronizeCartellaSociale implements MessageListener{

    protected final static Logger logger = LoggerFactory.getLogger(StartSyncronizeCartellaSociale.class);
    protected static EntityManager entityManager;
    protected static CartellaSocialeWsClient cartellaSocialeWsClient;

    public void testSincronizzazione() throws Throwable {
        logger.info("testing inserimentoCartellaSociale");
        try {
            entityManager = Connection.getEntityManager();

            SyncronizeCartellaSociale scs = new SyncronizeCartellaSociale(entityManager);
            scs.sincronizzaCartellaSociale();
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                entityManager.close();
            }
            entityManager = null;
        }
    }

    public void receive(Message msg) {
        try {
            testSincronizzazione();
        } catch (Throwable ex) {
            java.util.logging.Logger.getLogger(StartSyncronizeCartellaSociale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
