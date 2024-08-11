package it.trieste.comune.ssc.json;

import java.util.Map;

public abstract class JsonMapTransformer<T> implements Map<String,Object> {
	
	public abstract Map<String, Object> transformToMap(Object o);

}
