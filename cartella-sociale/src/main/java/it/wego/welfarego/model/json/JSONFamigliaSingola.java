/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

/**
 *
 * @author giuseppe
 */
public class JSONFamigliaSingola {

    private boolean success;
    private Object data;

    public JSONFamigliaSingola() {
    }

    public Object getFamiglia() {
        return data;
    }

    public void setFamiglia(Object famiglia) {
        this.data = famiglia;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
