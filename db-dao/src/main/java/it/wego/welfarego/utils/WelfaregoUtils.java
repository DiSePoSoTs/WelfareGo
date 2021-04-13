package it.wego.welfarego.utils;

import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.ConfigurationDao;

/**
 *
 * @author aleph
 */
public class WelfaregoUtils {

	public static final String PROPERTY_KEY_CONTEXT_PATH = "it.wego.wefarego.contextPath", CORE_PROPERTIES_NAME = "core";

	public static String getConfig(String key, String defaultValue) throws Exception {
		return new ConfigurationDao(PersistenceAdapterFactory.getPersistenceAdapter().getEntityManager()).getConfig(key, defaultValue);
	}

	public static String getConfig(String key) throws Exception {
		return new ConfigurationDao(PersistenceAdapterFactory.getPersistenceAdapter().getEntityManager()).getConfig(key);
	}
}
