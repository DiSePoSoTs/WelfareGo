package it.trieste.comune.ssc.json;

/**
 *
 * @author aleph
 */
public class JsonResponse {

    private Boolean success = true;
    private String message;

    public JsonResponse() {
    }

    public JsonResponse(String message) {
        this.message = message;
    }

    public JsonResponse(boolean success, String message) {
        this(message);
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
