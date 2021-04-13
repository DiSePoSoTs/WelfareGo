package it.wego.welfarego.tasklist.model;

import com.google.common.base.Strings;
import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class DettaglioBean {
    
    private String nome, note, dalleOre, alleOre, date;
    private Integer tipo;
    private String idAs, idTs, codPai, tipoHidden;
    
    public String getAlleOre() {
        return alleOre;
    }
    
    public void setAlleOre(String alleOre) {
        this.alleOre = alleOre;
    }
    
    public String getCodPai() {
        return codPai;
    }

    public Integer getCodPaiAsInt() {
        return Strings.isNullOrEmpty(codPai) ? null : Integer.valueOf(codPai);
    }
    
    public void setCodPai(String codPai) {
        this.codPai = codPai;
    }
    
    public String getDalleOre() {
        return dalleOre;
    }
    
    public void setDalleOre(String dalleOre) {
        this.dalleOre = dalleOre;
    }
    
    public String getData() {
        return date;
    }
    
    public void setData(String data) {
        this.date = data;
    }
    
    public String getIdAs() {
        return idAs;
    }

    public Integer getIdAsAsInt() {
        return Strings.isNullOrEmpty(idAs) ? null : Integer.valueOf(idAs);
    }
    
    public void setIdAs(String idAs) {
        this.idAs = idAs;
    }
    
    public String getIdTs() {
        return idTs;
    }
    
    public Long getIdTsAsLong() {
        return Strings.isNullOrEmpty(idTs) ? null : Long.parseLong(idTs);
    }
    
    public Date getIdTsAsDate() {
        Long idTsAsLong = getIdTsAsLong();
        return idTsAsLong == null ? null : new Date(idTsAsLong);
    }
    
    public void setIdTs(String idTs) {
        this.idTs = idTs;
    }
    
    public void setIdTsFromLong(Long idTs) {
        this.idTs = idTs.toString();
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
    
    public String getTipoHidden() {
        return tipoHidden;
    }
    
    public void setTipoHidden(String tipoHidden) {
        this.tipoHidden = tipoHidden;
    }
}
