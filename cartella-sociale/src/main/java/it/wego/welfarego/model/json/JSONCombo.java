/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ComboBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONCombo {

    private List<ComboBean> rows;
    private boolean success;

    public JSONCombo() {
    }

    public List<ComboBean> getRows() {
        return rows;
    }

    public void setRows(List<ComboBean> rows) {
        this.rows = rows;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
