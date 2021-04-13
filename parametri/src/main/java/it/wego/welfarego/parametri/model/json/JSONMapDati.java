/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model.json;

import it.wego.welfarego.parametri.model.MapDatiBean;
import java.util.List;

/**
 *
 * @author Michele
 */
public class JSONMapDati {
    
    private boolean success;
    private List<MapDatiBean> dati;
    private int total = 0;

    public JSONMapDati(boolean success, List<MapDatiBean> dati, int total) {
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

    public List<MapDatiBean> getUtenti() {
        return dati;
    }

    public void setUtenti(List<MapDatiBean> utenti) {
        this.dati = utenti;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }   
    
}