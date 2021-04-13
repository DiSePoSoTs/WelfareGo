package it.wego.welfarego.model.json;

/**
 *
 * @author giuseppe
 */
public class JSONCondizioneResponse {

    private boolean success;
    private String message;

    public JSONCondizioneResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
