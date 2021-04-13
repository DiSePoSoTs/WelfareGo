/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.ListeBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONListe {

	private boolean success;
	private List<ListeBean> data;

	public JSONListe(boolean success, List<ListeBean> d) {
		this.success = success;
		this.data = d;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<ListeBean> getParams() {
		return data;
	}

	public void setParams(List<ListeBean> d) {
		this.data = d;
	}
}
