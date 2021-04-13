/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.CronologiaPaiBean;
import java.util.Collection;

/**
 *
 * @author giuseppe
 */
public class JSONCronologiaPai {

    private boolean success;
    private Collection<CronologiaPaiBean> cronologia;
    private int total;

    public JSONCronologiaPai() {
    }

    public Collection<CronologiaPaiBean> getCronologia() {
        return cronologia;
    }

    public void setCronologia(Collection<CronologiaPaiBean> cronologia) {
        this.cronologia = cronologia;
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
