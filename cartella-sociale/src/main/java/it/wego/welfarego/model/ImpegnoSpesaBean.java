/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class ImpegnoSpesaBean {

    private String aCarico;
    private String anno;
    private int capitolo;
    private String centroElementareDiCosto;
    private String id;
    private String impegno;
    private String importoDisponibile;
    private String importoComplessivo;
    private String uot;

    public ImpegnoSpesaBean() {
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public int getCapitolo() {
        return capitolo;
    }

    public void setCapitolo(int capitolo) {
        this.capitolo = capitolo;
    }

    public String getCentroElementareDiCosto() {
        return centroElementareDiCosto;
    }

    public void setCentroElementareDiCosto(String centroElementareDiCosto) {
        this.centroElementareDiCosto = centroElementareDiCosto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImpegno() {
        return impegno;
    }

    public void setImpegno(String impegno) {
        this.impegno = impegno;
    }

    public String getImportoComplessivo() {
        return importoComplessivo;
    }

    public void setImportoComplessivo(String importoComplessivo) {
        this.importoComplessivo = importoComplessivo;
    }

    public String getImportoDisponibile() {
        return importoDisponibile;
    }

    public void setImportoDisponibile(String importoDisponibile) {
        this.importoDisponibile = importoDisponibile;
    }

    public String getaCarico() {
        return aCarico;
    }

    public void setaCarico(String aCarico) {
        this.aCarico = aCarico;
    }

    public String getUot() {
        return uot;
    }

    public void setUot(String uot) {
        this.uot = uot;
    }
}
