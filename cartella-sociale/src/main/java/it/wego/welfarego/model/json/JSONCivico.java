package it.wego.welfarego.model.json;

import it.wego.welfarego.model.CivicoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONCivico {

    private List<CivicoBean> civico;
    private boolean success = true;
    private int total;

    public JSONCivico() {
    }

    public List<CivicoBean> getCivico() {
        return civico;
    }

    public void setCivico(List<CivicoBean> civico) {
        this.civico = civico;
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
