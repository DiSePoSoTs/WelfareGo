package it.wego.welfarego.model.json;




/**
 *
 * @author Fabio Bonaccorso
 * Classe json della struttura
 */
public class JSONStruttura {

    private boolean success;
    
    private Object data;

    public JSONStruttura() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
