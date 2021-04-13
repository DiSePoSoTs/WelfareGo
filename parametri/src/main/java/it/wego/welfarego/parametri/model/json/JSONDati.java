/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.DatiBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONDati {
    
    private boolean success;
    private List<DatiBean> dati;
    private int total = 0;

    public JSONDati(boolean success, List<DatiBean> dati, int total) {
        this.success = success;
        this.dati = dati;
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DatiBean> getUtenti() {
        return dati;
    }

    public void setUtenti(List<DatiBean> utenti) {
        this.dati = utenti;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }   
    
}