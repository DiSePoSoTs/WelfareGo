/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class CivilmenteObbligatoBean {

    private String codTipInt;
    private String cntTipInt;
    private int codAnag;
    private String nome;
    private String cognome;
    private String codicefiscale;
    private String importoMensile;
    private int codPai;

    public CivilmenteObbligatoBean() {
    }

    public String getCntTipInt() {
        return cntTipInt;
    }

    public void setCntTipInt(String cntTipInt) {
        this.cntTipInt = cntTipInt;
    }

    public int getCodAnag() {
        return codAnag;
    }

    public void setCodAnag(int codAnag) {
        this.codAnag = codAnag;
    }

    public int getCodPai() {
        return codPai;
    }

    public void setCodPai(int codPai) {
        this.codPai = codPai;
    }

    public String getCodTipInt() {
        return codTipInt;
    }

    public void setCodTipInt(String codTipInt) {
        this.codTipInt = codTipInt;
    }

    public String getCodicefiscale() {
        return codicefiscale;
    }

    public void setCodicefiscale(String codicefiscale) {
        this.codicefiscale = codicefiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getImportoMensile() {
        return importoMensile;
    }

    public void setImportoMensile(String importoMensile) {
        this.importoMensile = importoMensile;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
