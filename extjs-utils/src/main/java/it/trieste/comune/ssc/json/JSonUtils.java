package it.trieste.comune.ssc.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author aleph
 * @deprecated use JsonBuilder
 */
@Deprecated
public class JSonUtils {

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	private static Gson gson,gsonpp;

	/**
	 * restituisce un oggetto Gson per serializzazione/deserializzazione JSON,
	 * con le impostazioni di default più comode
	 * @return  un oggetto Gson per serializzazione/deserializzazione JSON
	 */
	public static Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder()
					  .setDateFormat(DATE_FORMAT)
					  .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					  .create();
		}
		return gson;
	}
	/**
	 * restituisce un oggetto Gson per serializzazione/deserializzazione JSON,
	 * con le impostazioni di default più comode, incluso il
	 * Pretty Printing
	 * @return  un oggetto Gson per serializzazione/deserializzazione JSON
	 */
	public static Gson getGsonPrettyPrinting() {
		if (gsonpp == null) {
			gsonpp = getGsonPrettyPrintingBuilder()
					  .create();
		}
		return gsonpp;
	}
	
	
	public static GsonBuilder getGsonPrettyPrintingBuilder() {
		return new GsonBuilder()
					  .setDateFormat(DATE_FORMAT)
					  .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					  .setPrettyPrinting();
	}
	
	public static String toPrettyJson(Object obj){
		return getGsonPrettyPrinting().toJson(obj);
	}
}
