package it.wego.welfarego.cartellasocialews;

import java.io.File;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartSyncWS {

    protected final static Logger logger = LoggerFactory.getLogger(StartSyncWS.class);
    protected static EntityManager entityManager;
    protected static CartellaSocialeWsClient cartellaSocialeWsClient;
    
    public static void Sincronizza(EntityManager em) {
        logger.info("start sincronizzazione");
        try {
            entityManager = em;
            SyncWS.newInstance().withEntityManager(entityManager).sincronizzaWS();
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
    
    public static void SincronizzaMSNA(File csvFile) {
    	
    	logger.info("start sincronizzazione");
    	if(!csvFile.exists()) {
    		logger.error("Il file CSV dei MSNA non esiste");
    		return;
    	} 
    	
    	try {
            SyncWS.newInstance().withCsvFileMSNA(csvFile).sincronizzaWS();
        } catch (Throwable t) {
            logger.error("got error : ", t);
            throw t;
        } finally {

        }
    }
}
