/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.TemplateBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONTemplate {

    private boolean success;
    private List<TemplateBean> data;

    public JSONTemplate(boolean success, List<TemplateBean> d) {
        this.success = success;
        this.data = d;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<TemplateBean> getParams() {
        return data;
    }

    public void setParams(List<TemplateBean> d) {
        this.data = d;
    }
}
