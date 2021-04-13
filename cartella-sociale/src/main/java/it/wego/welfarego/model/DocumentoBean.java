/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class DocumentoBean {

    private String dtDoc;
    private String nomeFile;
    private String codUteAut;
    private String paiIntervento;
    private String idDocumento;
    private String ver;
    private String azioni;
    private String tipologia;

    public DocumentoBean() {
    }

    public String getCodUteAut() {
        return codUteAut;
    }

    public void setCodUteAut(String codUteAut) {
        this.codUteAut = codUteAut;
    }

    public String getDtDoc() {
        return dtDoc;
    }

    public void setDtDoc(String dtDoc) {
        this.dtDoc = dtDoc;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getPaiIntervento() {
        return paiIntervento;
    }

    public void setPaiIntervento(String paiIntervento) {
        this.paiIntervento = paiIntervento;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getAzioni() {
        return azioni;
    }

    public void setAzioni(String azioni) {
        this.azioni = azioni;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}
