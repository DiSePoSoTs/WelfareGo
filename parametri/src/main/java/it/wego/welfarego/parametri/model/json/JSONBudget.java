/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.BudgetBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONBudget {

    private boolean success;
    private List<BudgetBean> data;

    public JSONBudget(boolean success, List<BudgetBean> d) {
        this.success = success;
        this.data = d;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<BudgetBean> getParams() {
        return data;
    }

    public void setParams(List<BudgetBean> d) {
        this.data = d;
    }
}
