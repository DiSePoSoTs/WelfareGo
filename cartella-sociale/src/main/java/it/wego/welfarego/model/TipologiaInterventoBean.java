/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class TipologiaInterventoBean {

    private String name;
    private String value;
    private String impStdCosto;
    private String label;
    private Character flgFineDurata;

    public TipologiaInterventoBean() {
    }

    public String getImpStdCosto() {
        return impStdCosto;
    }

    public void setImpStdCosto(String impStdCosto) {
        this.impStdCosto = impStdCosto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Character getFlgFineDurata() {
        return flgFineDurata;
    }

    public void setFlgFineDurata(Character flgFineDurata) {
        this.flgFineDurata = flgFineDurata;
    }
}
