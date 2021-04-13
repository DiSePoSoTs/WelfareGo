package it.wego.welfarego.commons.listener;

import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.bre.utils.BreUtils;
import java.io.File;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Muscas
 */
public class WelfaregoFormsContextListener implements ServletContextListener {

    private static final String PERSISTENCE_UNIT = "welfaregoPU";
    private static Logger logger = LoggerFactory.getLogger(WelfaregoFormsContextListener.class);
    private static ServletContext servletContext;
    private static String defaultToolsRoot = "webapps/WelfaregoForms/", toolsRoot;
    private static String toolsClassesRoot;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("\n\n--------------------------------------------------------------------------------\n");
        logger.info("starting WelfaregoForms");
        try {
            PersistenceAdapterFactory.setPersistenceUnit(PERSISTENCE_UNIT);
            servletContext = sce.getServletContext();
            toolsRoot = servletContext.getRealPath("/");
            logger.debug("toolsroot by context = {}", toolsRoot);
            if (toolsRoot == null) {
                URL url = this.getClass().getResource("/log4j2.properties"); //should exist
                if (url != null) {
                    File file = new File(url.getPath());
                    toolsRoot = file.getParentFile().getParentFile().getParentFile().toString();
                    logger.debug("toolsroot by classpath = {}", toolsRoot);
                } else {
                    toolsRoot = defaultToolsRoot;
                    logger.debug("toolsroot by string = {}", toolsRoot);
                }
            }
            toolsClassesRoot = toolsRoot + File.separator + "WEB-INF" + File.separator + "classes";
            //BreUtils.startServices();
            IntalioAdapter.executeJob();
        } catch (Exception ex) {
            logger.error("Errore durante l'inizializzazione del listener", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("stopping WelfaregoForms");
        //BreUtils.stopServices();
        IntalioAdapter.shutdown();
    }

    public static String getToolsRoot() {
        if (toolsRoot == null) {
            logger.warn("required toosRoot before initialization, returning default {}", defaultToolsRoot);
            return defaultToolsRoot;
        }
        return toolsRoot;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static String getToolsClassesRoot() {
        return toolsClassesRoot;
    }
}
