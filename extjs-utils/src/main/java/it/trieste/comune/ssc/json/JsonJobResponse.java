package it.trieste.comune.ssc.json;

/**
 *
 * @author aleph
 */
public class JsonJobResponse extends JsonDataResponse {
    private Boolean completed;

    public JsonJobResponse(boolean success, String message) {
        super(success, message);
    }

    public JsonJobResponse(Object data) {
        super(data);
        completed=true;
    }

    public JsonJobResponse(boolean completed) {
        this.completed = completed;
    }
    
    public JsonJobResponse() {
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
}
