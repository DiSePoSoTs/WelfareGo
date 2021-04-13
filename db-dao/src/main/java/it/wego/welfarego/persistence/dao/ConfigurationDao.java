package it.wego.welfarego.persistence.dao;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Configuration;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;

/**
 *
 * @author aleph
 */
public class ConfigurationDao extends PersistenceAdapter {

    public ConfigurationDao(EntityManager em) {
        super(em);
    }

    public @Nullable
    String getConfig(String key, @Nullable String defaultValue) {
        Configuration configuration = getEntityManager().find(Configuration.class, key);
        return configuration != null ? configuration.getValue() : defaultValue;
    }

    public @Nullable
    Integer getConfigInt(String key, @Nullable Integer defaultValue) {
        String value = getConfig(key, String.valueOf(defaultValue));
        return value == null ? null : Integer.valueOf(value);
    }

    public String getConfig(String key) {
        return getConfig(key, null);
    }

    public Map<String, String> getConfigWithPrefix(String configPrefix) {
        return Maps.transformValues(Maps.uniqueIndex(getEntityManager().createQuery("SELECT c.id,c.value FROM Configuration c WHERE c.id LIKE :prefix").setParameter("prefix", configPrefix + "%").getResultList(),
                new Function<Object[], String>() {
                    public String apply(Object[] input) {
                        return (String) input[0];
                    }
                }), new Function<Object[], String>() {
            public String apply(Object[] input) {
                return (String) input[1];
            }
        });
    }

    public void setConfig(String key, String value, String desc) throws Exception {
        Configuration config = new Configuration();
        config.setId(key);
        config.setValue(value);
        config.setDescription(desc);
        update(config);
    }

    public void setConfig(String key, String value) throws Exception {
        setConfig(key, value, "");
    }
}
