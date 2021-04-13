package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class DettaglioBean {

//    private String id;
    private String as;
    private String ts;
    private String nome;
    private String note;
    private String dalleOre;
    private String alleOre;
    private String dataAppuntamento;
    private Integer tipo;
    private Integer tipoHidden;

    public DettaglioBean() {
    }

    public String getAlleOre() {
        return alleOre;
    }

    public void setAlleOre(String alleOre) {
        this.alleOre = alleOre;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getDalleOre() {
        return dalleOre;
    }

    public void setDalleOre(String dalleOre) {
        this.dalleOre = dalleOre;
    }

    public String getDataAppuntamento() {
        return dataAppuntamento;
    }

    public void setDataAppuntamento(String dataAppuntamento) {
        this.dataAppuntamento = dataAppuntamento;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoHidden() {
        return tipoHidden;
    }

    public void setTipoHidden(Integer tipoHidden) {
        this.tipoHidden = tipoHidden;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
