/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class CronologiaPaiBean {

    private String intervento;
    private String tsEvePai;
    private String desEvento;
    private String operatore;

    public CronologiaPaiBean() {
    }

    public String getTsEvePai() {
        return tsEvePai;
    }

    public void setTsEvePai(String tsEvePai) {
        this.tsEvePai = tsEvePai;
    }

    public String getIntervento() {
        return intervento;
    }

    public void setIntervento(String intervento) {
        this.intervento = intervento;
    }

    public String getOperatore() {
        return operatore;
    }

    public void setOperatore(String operatore) {
        this.operatore = operatore;
    }

    public String getDesEvento() {
        return desEvento;
    }

    public void setDesEvento(String desEvento) {
        this.desEvento = desEvento;
    }
}
