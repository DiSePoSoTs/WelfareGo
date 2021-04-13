/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class ContribuzioneBean {

    private Date data;
    private String importo;
    private String fattura;
    private String idFattura;
    private String pagato;

    public ContribuzioneBean() {
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFattura() {
        return fattura;
    }

    public void setFattura(String fattura) {
        this.fattura = fattura;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }

    public String getPagato() {
        return pagato;
    }

    public void setPagato(String pagato) {
        this.pagato = pagato;
    }

    public String getIdFattura() {
        return idFattura;
    }

    public void setIdFattura(String idFattura) {
        this.idFattura = idFattura;
    }
}
