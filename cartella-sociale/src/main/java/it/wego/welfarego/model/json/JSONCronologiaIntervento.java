/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.CronologiaInterventoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONCronologiaIntervento {

    private boolean success;
    private List<CronologiaInterventoBean> cronologia;

    public JSONCronologiaIntervento() {
    }

    public List<CronologiaInterventoBean> getCronologia() {
        return cronologia;
    }

    public void setCronologia(List<CronologiaInterventoBean> cronologia) {
        this.cronologia = cronologia;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
