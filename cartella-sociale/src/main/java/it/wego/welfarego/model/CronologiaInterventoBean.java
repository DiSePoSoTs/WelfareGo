/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class CronologiaInterventoBean {

    private String data;
    private String evento;
    private String nomeOperatore;
    private String cognomeOperatore;
    private String note;

    public CronologiaInterventoBean() {
    }

    public String getCognomeOperatore() {
        return cognomeOperatore;
    }

    public void setCognomeOperatore(String cognomeOperatore) {
        this.cognomeOperatore = cognomeOperatore;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getNomeOperatore() {
        return nomeOperatore;
    }

    public void setNomeOperatore(String nomeOperatore) {
        this.nomeOperatore = nomeOperatore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
