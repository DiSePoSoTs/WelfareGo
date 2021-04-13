package it.wego.welfarego.cartellasociale;

import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.scheduler.WelfaregoScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author giuseppe
 */
public class WebappContextListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void contextInitialized(ServletContextEvent sce) {
        try {
            PersistenceAdapterFactory.setPersistenceUnit("welfaregoPU");
            //BreUtils.startServices();
            WelfaregoScheduler.getInstance().start();
        } catch (Exception ex) {
            logger.error("Errore durante l'inizializzazione", ex);
        }
        logger.info("Cartella Sociale : ready");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //BreUtils.stopServices();
        WelfaregoScheduler.getInstance().stop();
        logger.info("Cartella Sociale : stopped");
    }
}



