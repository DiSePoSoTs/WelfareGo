package it.wego.welfarego.dao.test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.PaiIntervento;
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
    protected final static Properties config = new Properties();

    @BeforeClass
    public static void initGlobal() throws Exception {
        logger.info("preparing test (global)");
        try {
            config.load(AbstractTest.class.getResourceAsStream("/testConfig.properties"));
            Properties properties = new Properties();
            properties.load(AbstractTest.class.getResourceAsStream("/dbConfig.properties"));
            logger.info("db config {}", JsonBuilder.getGsonPrettyPrinting().toJson(properties));
            entityManager = Connection.getEntityManager(properties);
        } catch (Exception e) {
            logger.error("got error : ", e);
            throw e;
        }
    }

    protected static String requireParam(String key) {
        String value = Strings.emptyToNull(config.getProperty(key));
        Preconditions.checkNotNull(value);
        return value;
    }

    protected static PaiIntervento getPaiIntervento() {
        return new PaiInterventoDao(entityManager).findByKey(Integer.parseInt(requireParam("codPai")), requireParam("codTipint"), requireParam("cntTipint"));
    }

    protected static AnagrafeSoc getAnagrafeSoc() {
        return new AnagrafeSocDao(entityManager).findByCodAna(requireParam("codAna"));
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
