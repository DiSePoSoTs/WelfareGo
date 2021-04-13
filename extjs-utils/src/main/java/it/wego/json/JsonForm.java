package it.wego.json;

/**
 * 
 * @author aleph
 * @deprecated use instead extjs-utils
 */
@Deprecated
public class JsonForm extends GsonObject{

    Boolean success;
    Object data;

    public JsonForm() {
    }

    public JsonForm(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public JsonForm(Object data) {
        this(true, data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
