package it.trieste.comune.ssc.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;

import java.util.List;

public class JsonBuilder extends groovy.json.JsonBuilder {

	public static final Type MAP_OF_STRINGS = Map.class;
	
	public static final Type LIST_OF_MAP_OF_STRINGS = List.class;
	
	private PrintWriter pw;
	
	private Map<String,Object> data;
	
	
	public static JsonBuilder newInstance() {
		return new JsonBuilder();
	}

	public static Gson getGson() {
		return new Gson();
	}

	public static Gson getGsonPrettyPrinting() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	public void withSuccess(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public JsonResponse buildResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	public void withMessage(String join) {
		// TODO Auto-generated method stub
		
	}

	public void buildStoreResponse() {
		// TODO Auto-generated method stub
		
	}

	public void addRecord(Map record) {
		// TODO Auto-generated method stub
		
	}

	public JsonBuilder withWriter(PrintWriter writer) {
		this.pw = writer;
		return null;
	}

	public JsonBuilder withParameters(Map<String, String[]> parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object withData(Map<String, Object> jso) {
		this.data = jso;
		return null;
	}

	public JsonBuilder withError(Exception exception) {
		// TODO Auto-generated method stub
		return null;
	}
}
