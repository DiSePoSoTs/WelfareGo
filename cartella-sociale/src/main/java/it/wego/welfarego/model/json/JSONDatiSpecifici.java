/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.DatoSpecifico;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONDatiSpecifici {

    private boolean success;
    private String message;
    private List<DatoSpecifico> components;

    public JSONDatiSpecifici() {
    }

    public List<DatoSpecifico> getComponents() {
        return components;
    }

    public void setComponents(List<DatoSpecifico> components) {
        this.components = components;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
