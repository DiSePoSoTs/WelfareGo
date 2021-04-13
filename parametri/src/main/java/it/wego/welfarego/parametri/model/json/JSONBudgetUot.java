/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.BudgetBean;
import it.wego.welfarego.parametri.model.BudgetUotBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONBudgetUot {

    private boolean success;
    private List<BudgetUotBean> data;

    public JSONBudgetUot(boolean success, List<BudgetUotBean> d) {
        this.success = success;
        this.data = d;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<BudgetUotBean> getParams() {
        return data;
    }

    public void setParams(List<BudgetUotBean> d) {
        this.data = d;
    }
}
