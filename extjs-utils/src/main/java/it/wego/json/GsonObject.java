package it.wego.json;

/**
 * classe base per oggetti da serializzare/deserializzare via gson
 * @author aleph
 */
@Deprecated
public abstract class GsonObject {
	@Override
	public String toString(){
		return JSonUtils.getGson().toJson(this);
	}
}
