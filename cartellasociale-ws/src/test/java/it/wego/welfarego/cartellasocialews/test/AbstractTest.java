/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews.test;

import com.google.common.base.Strings;
import it.wego.extjs.json.JsonBuilder;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Properties;
import javax.persistence.EntityManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class AbstractTest {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractTest.class);
    protected static EntityManager entityManager;
    protected static Properties testConfig;
    protected static CartellaSocialeWsClient cartellaSocialeWsClient;

    @BeforeClass
    public static void initGlobal() throws Exception {
        logger.info("preparing test (global)");
        try {
            testConfig = new Properties();
            testConfig.load(AbstractTest.class.getResourceAsStream("/testConfig.properties"));
            logger.info("test config {}", JsonBuilder.getGsonPrettyPrinting().toJson(testConfig));

            Properties properties = new Properties();
            properties.load(AbstractTest.class.getResourceAsStream("/dbConfig.properties"));
            logger.info("db config {}", JsonBuilder.getGsonPrettyPrinting().toJson(properties));
            entityManager = Connection.getEntityManager(properties);

            cartellaSocialeWsClient = CartellaSocialeWsClient.newInstance().withParameters(testConfig).withEntityManager(entityManager);
            if (!Strings.isNullOrEmpty(testConfig.getProperty("test.codInt"))) {
                String[] split = testConfig.getProperty("test.codInt").split(":");
                cartellaSocialeWsClient.withPaiIntervento(split[0], split[1], split[2]);
                logger.info("test paiIntervento = {}", cartellaSocialeWsClient.getDataUtils().getPaiIntervento());
                logger.info("test anagrafeSoc = {}", cartellaSocialeWsClient.getDataUtils().getAnagrafeSoc());
            } else if (!Strings.isNullOrEmpty(testConfig.getProperty("test.codAna"))) {
                cartellaSocialeWsClient.withAnagrafeSoc(testConfig.getProperty("test.codAna"));
                logger.info("test anagrafeSoc = {}", cartellaSocialeWsClient.getDataUtils().getAnagrafeSoc());
            }
        } catch (Exception e) {
            logger.error("got error : ", e);
            throw e;
        }
    }

    @AfterClass
    public static void globalCleanup() {
        if (entityManager != null && entityManager.isOpen()) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
        entityManager = null;
    }
}
