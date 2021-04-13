/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ReferenteBean;

/**
 *
 * @author giuseppe
 */
public class JSONReferenteSingolo {

    private boolean success=true;
    private ReferenteBean data;

    public JSONReferenteSingolo() {
    }

    public ReferenteBean getData() {
        return data;
    }

    public void setData(ReferenteBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
