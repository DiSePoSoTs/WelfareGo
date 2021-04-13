/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

/**
 *
 * @author giuseppe
 */
public class JSONFamiglia {

    private boolean success;
    private String message;
    private int totalCount;
    private Object famiglia;

    public JSONFamiglia() {
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

    public Object getFamiglia() {
        return famiglia;
    }

    public void setFamiglia(Object famiglia) {
        this.famiglia = famiglia;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
