package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ComuneBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONComune {

    private List<ComuneBean> comune;
    private int total;
    private boolean success;

    public JSONComune() {
    }

    public List<ComuneBean> getComune() {
        return comune;
    }

    public void setComune(List<ComuneBean> comune) {
        this.comune = comune;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
