/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.PagamentoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONPagamenti {

    private String message;
    private boolean success;
    private List<PagamentoBean> pagamenti;

    public JSONPagamenti() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PagamentoBean> getPagamenti() {
        return pagamenti;
    }

    public void setPagamenti(List<PagamentoBean> pagamenti) {
        this.pagamenti = pagamenti;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
