package it.wego.welfarego.model.json;


/**
 *
 * @author giuseppe
 */
public class JSONCondizione {

    private boolean success;
    private Object data;

    public JSONCondizione() {
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
