/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.CivilmenteObbligatoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONCivilmenteObbligato {

    private boolean success;
    private String message;
    private String code;
    private int cntTipInt;
    private List<CivilmenteObbligatoBean> civilmenteObbligati;

    public JSONCivilmenteObbligato() {
    }

    public List<CivilmenteObbligatoBean> getCivilmenteObbligati() {
        return civilmenteObbligati;
    }

    public void setCivilmenteObbligati(List<CivilmenteObbligatoBean> civilmenteObbligati) {
        this.civilmenteObbligati = civilmenteObbligati;
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

    public int getCntTipInt() {
        return cntTipInt;
    }

    public void setCntTipInt(int cntTipInt) {
        this.cntTipInt = cntTipInt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
