/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import java.util.Collection;

/**
 *
 * @author giuseppe
 */
public class JSONImpegniSpesa {

    private boolean success;
    private String message;
    private int total;
    private Collection impegni;

    public JSONImpegniSpesa() {
    }

    public Collection getImpegni() {
        return impegni;
    }

    public void setImpegni(Collection impegni) {
        this.impegni = impegni;
    }

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
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
}
