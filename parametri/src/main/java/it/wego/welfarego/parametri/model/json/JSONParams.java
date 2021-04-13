/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.ParametriFormBean;

/**
 *
 * @author Boss
 */
public class JSONParams {
    
    private boolean success;
    private ParametriFormBean data;

    public JSONParams(boolean success, ParametriFormBean params) {
        this.success = success;
        this.data = params;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ParametriFormBean getParams() {
        return data;
    }

    public void setParams(ParametriFormBean params) {
        this.data = params;
    }

    
}
