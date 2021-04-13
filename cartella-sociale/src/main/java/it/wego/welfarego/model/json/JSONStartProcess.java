/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

/**
 *
 * @author giuseppe
 */
public class JSONStartProcess {

    private boolean success;
    private String message;
    private String idDoc;
    private Integer codPai;


    public JSONStartProcess() {
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
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

    public Integer getCodPai() {
        return codPai;
    }

    public void setCodPai(Integer codPai) {
        this.codPai = codPai;
    }
}
