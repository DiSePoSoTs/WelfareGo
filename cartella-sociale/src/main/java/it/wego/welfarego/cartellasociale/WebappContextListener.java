package it.wego.welfarego.cartellasociale;

import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.scheduler.WelfaregoScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author giuseppe
 */
@XmlRootElement(name = "entity-mappings",namespace="http://xmlns.jcp.org/xml/ns/persistence/orm")
public class WebappContextListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void contextInitialized(ServletContextEvent sce) {
        try {
            PersistenceAdapterFactory.setPersistenceUnit("welfaregoPU");
            WelfaregoScheduler.getInstance().start();
        } catch (Exception ex) {
            logger.error("Errore durante l'inizializzazione", ex);
        }
        logger.info("Cartella Sociale : ready");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        WelfaregoScheduler.getInstance().stop();
        logger.info("Cartella Sociale : stopped");
    }
}



