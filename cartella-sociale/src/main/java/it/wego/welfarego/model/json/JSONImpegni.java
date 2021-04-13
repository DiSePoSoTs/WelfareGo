package it.wego.welfarego.model.json;


import it.wego.welfarego.model.ImpegnoBean;

/**
 *
 * @author giuseppe
 */
public class JSONImpegni {

    private boolean success;
    private ImpegnoBean impegno;

    public JSONImpegni() {
    }

    public ImpegnoBean getImpegno() {
        return impegno;
    }

    public void setImpegno(ImpegnoBean impegno) {
        this.impegno = impegno;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
