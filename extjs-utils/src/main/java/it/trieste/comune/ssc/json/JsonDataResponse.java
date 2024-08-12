package it.trieste.comune.ssc.json;

/**
 *
 * @author aleph
 */
public class JsonDataResponse extends JsonResponse {

    private Object data;

    public JsonDataResponse() {
    }

    public JsonDataResponse(String message) {
        super(message);
    }

    public JsonDataResponse(String message, Object data) {
        this(message);
        this.data = data;
    }

    public JsonDataResponse(boolean success, String message) {
        super(success, message);
    }

    public JsonDataResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
