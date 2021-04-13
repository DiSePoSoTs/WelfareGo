package it.wego.welfarego.model.json;


import it.wego.welfarego.model.DettaglioBean;

/**
 *
 * @author giuseppe
 */
public class JSONOrario {

    private boolean success;
    private DettaglioBean data;

    public JSONOrario() {
    }

    public DettaglioBean getImpegno() {
        return data;
    }

    public void setImpegno(DettaglioBean impegno) {
        this.data = impegno;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
