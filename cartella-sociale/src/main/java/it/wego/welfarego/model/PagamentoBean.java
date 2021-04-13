/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;



/**
 *
 * @author giuseppe
 */
public class PagamentoBean {

    private String data;
    private String importo;
    private String idMandato;
    private String mandato;
    private String riscosso;
    private String modalitaErogazione;

    public PagamentoBean() {
    }

    public String getData() {
        return data;
    }

    public void setData(String string) {
        this.data = string;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }

    public String getMandato() {
        return mandato;
    }

    public void setMandato(String mandato) {
        this.mandato = mandato;
    }

    public String getRiscosso() {
        return riscosso;
    }

    public void setRiscosso(String riscosso) {
        this.riscosso = riscosso;
    }

    public String getIdMandato() {
        return idMandato;
    }

    public void setIdMandato(String idMandato) {
        this.idMandato = idMandato;
    }

    public String getModalitaErogazione() {
        return modalitaErogazione;
    }

    public void setModalitaErogazione(String modalitaErogazione) {
        this.modalitaErogazione = modalitaErogazione;
    }
}
