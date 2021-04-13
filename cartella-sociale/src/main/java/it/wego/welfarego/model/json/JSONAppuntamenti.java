package it.wego.welfarego.model.json;

import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONAppuntamenti {

    private boolean success;
    private List appuntamenti;

    public JSONAppuntamenti() {
    }

    public List getAppuntamenti() {
        return appuntamenti;
    }

    public void setAppuntamenti(List appuntamenti) {
        this.appuntamenti = appuntamenti;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
