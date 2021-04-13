package it.wego.welfarego.model.json;

/**
 *
 * @author giuseppe
 */
public class JSONAnagraficaResponse {

    private boolean success;
    private String message;
    private int codAna;

    public JSONAnagraficaResponse() {
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

    public int getCodAnag() {
        return codAna;
    }

    public void setCodAna(int codAna) {
        this.codAna = codAna;
    }
}
