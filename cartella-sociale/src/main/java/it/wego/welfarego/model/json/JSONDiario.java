package it.wego.welfarego.model.json;


/**
 *
 * @author Fabio Bonaccorso
 * Diario 
 */
public class JSONDiario {

    private boolean success;
    private Object data;

    public JSONDiario() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
