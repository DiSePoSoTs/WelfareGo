package it.wego.welfarego.model;

import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class OrarioBean {

    private String id;
    private String desc;
    private String note;
    private String idImpegno;
    private Date fineImpegno;
    private Integer tipo;
    private String ora;
    private int giorno;

    public OrarioBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public int getGiorno() {
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getIdImpegno() {
        return idImpegno;
    }

    public void setIdImpegno(String idImpegno) {
        this.idImpegno = idImpegno;
    }

    public Date getFineImpegno() {
        return fineImpegno;
    }

    public void setFineImpegno(Date fineImpegno) {
        this.fineImpegno = fineImpegno;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
