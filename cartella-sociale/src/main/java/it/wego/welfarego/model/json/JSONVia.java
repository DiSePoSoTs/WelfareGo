package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ViaBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONVia {

    private List<ViaBean> via;
    private int total;
    private boolean success;

    public JSONVia() {
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

    public List<ViaBean> getVia() {
        return via;
    }

    public void setVia(List<ViaBean> via) {
        this.via = via;
    }
}
