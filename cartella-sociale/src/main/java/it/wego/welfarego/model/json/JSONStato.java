package it.wego.welfarego.model.json;

import it.wego.welfarego.model.StatoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONStato {

    private int total;
    private boolean success;
    private List<StatoBean> stato;

    public JSONStato() {
    }

    public List<StatoBean> getStato() {
        return stato;
    }

    public void setStato(List<StatoBean> stato) {
        this.stato = stato;
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
