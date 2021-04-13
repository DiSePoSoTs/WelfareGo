package it.wego.welfarego.model.json;

import it.wego.welfarego.model.AnagraficaBean;

/**
 *
 * @author giuseppe
 */
public class JSONAnagrafica {

    private boolean success;
    private AnagraficaBean data;

    public JSONAnagrafica() {
    }

    public AnagraficaBean getAnagrafica() {
        return data;
    }

    public void setAnagrafica(AnagraficaBean anagrafica) {
        this.data = anagrafica;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
