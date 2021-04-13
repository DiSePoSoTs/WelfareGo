/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;


import it.wego.welfarego.parametri.model.StrutturaBean;

import java.util.List;

/**
 *
 * @author Fabio Bonaccorso 
 */
public class JSONStruttura {

    private boolean success;
    private List<StrutturaBean> data;

    public JSONStruttura(boolean success, List<StrutturaBean> d) {
        this.success = success;
        this.data = d;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StrutturaBean> getParams() {
        return data;
    }

    public void setParams(List<StrutturaBean> d) {
        this.data = d;
    }
}
