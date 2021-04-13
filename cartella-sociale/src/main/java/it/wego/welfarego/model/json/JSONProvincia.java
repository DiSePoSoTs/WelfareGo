/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ProvinciaBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONProvincia {

    private int total;
    private boolean success;
    private List<ProvinciaBean> provincia;

    public JSONProvincia() {
    }

    public List<ProvinciaBean> getProvincia() {
        return provincia;
    }

    public void setProvincia(List<ProvinciaBean> provincia) {
        this.provincia = provincia;
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
