package it.trieste.comune.ssc.json;

import com.google.common.base.Function;
import java.util.HashMap;
import java.util.Map;
import static it.trieste.comune.ssc.json.ExtjsConst.*;

/**
 *
 * @author davide
 */
public abstract class JsonMapTransformer<E> implements Function<E, Map> {

    private Map map;

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public void putRecord(Object name, Object value) {
        put(NAME, name);
        put(VALUE, value);
    }

    public abstract void transformToMap(E obj);

    public Map apply(E input) {
        map = new HashMap();
        transformToMap(input);
        return map;
    }

    public static Map createRecord(final Object name, final Object value) {
        return new JsonMapTransformer() {

            public void transformToMap(Object obj) {
                putRecord(name, value);
            }
        }.apply(new Object());
    }
}
