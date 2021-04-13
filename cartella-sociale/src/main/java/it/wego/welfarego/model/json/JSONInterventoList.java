/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.InterventoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONInterventoList {

    private boolean success;
    private String message;
    private int total;
    private List<InterventoBean> data;

    public JSONInterventoList() {
    }

    public List<InterventoBean> getData() {
        return data;
    }

    public void setData(List<InterventoBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
