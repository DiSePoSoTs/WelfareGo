package it.wego.persistence;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class PersistenceAdapterFactory {

    private static PersistenceAdapterFactory defaultPersistenceAdapterFactory;
    private String persistenceUnit;
    private EntityManagerFactory entityManagerFactory;
    private static Logger logger = LoggerFactory.getLogger(PersistenceAdapterFactory.class);
    private static final ThreadLocal<PersistenceAdapter> sharedPersistenceAdapter = new ThreadLocal<PersistenceAdapter>();

    private static Map<String, PersistenceAdapterFactory> factoriesMap = new HashMap<String, PersistenceAdapterFactory>();

    private PersistenceAdapterFactory(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    private static PersistenceAdapterFactory getPersistenceAdapterFactory(String persistenceUnit) {
        PersistenceAdapterFactory factory = factoriesMap.get(persistenceUnit);
        if (factory == null) {
            factory = new PersistenceAdapterFactory(persistenceUnit);
            factoriesMap.put(persistenceUnit, factory);
        }
        return factory;
    }

    public static void setPersistenceUnit(String persistenceUnit) {
        defaultPersistenceAdapterFactory = getPersistenceAdapterFactory(persistenceUnit);
    }
    public static String getPersistenceUnit() {
        return defaultPersistenceAdapterFactory == null ? null : defaultPersistenceAdapterFactory.persistenceUnit;
    }

    public static PersistenceAdapter createPersistenceAdapter() {
        return defaultPersistenceAdapterFactory == null ? null : new PersistenceAdapter(defaultPersistenceAdapterFactory.entityManagerFactory.createEntityManager());
    }

    public static PersistenceAdapter getPersistenceAdapter() {
        PersistenceAdapter persistenceAdapter = sharedPersistenceAdapter.get();
        if (persistenceAdapter == null && defaultPersistenceAdapterFactory != null) {
            try {
                logger.debug("initializing thread shared persistence adapter");
                persistenceAdapter = createPersistenceAdapter();
                sharedPersistenceAdapter.set(persistenceAdapter);
            } catch (Exception ex) {
                logger.error("error initializing persistence adapter", ex);
            }
        }
        return persistenceAdapter;
    }

    public static void releasePersistenceAdapter() {
        PersistenceAdapter persistenceAdapter = sharedPersistenceAdapter.get();
        if (persistenceAdapter != null) {
            persistenceAdapter.close();
        }
    }

    public static void releasePersistenceAdapter(PersistenceAdapter persistenceAdapter) {
        if (persistenceAdapter == sharedPersistenceAdapter.get()) {
            logger.debug("releasing thread shared persistence adapter");
            sharedPersistenceAdapter.remove();
        }
    }
}
