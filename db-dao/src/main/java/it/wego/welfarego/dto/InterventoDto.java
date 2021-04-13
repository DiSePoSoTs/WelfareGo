package it.wego.welfarego.dto;

import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;

import java.math.BigDecimal;
import java.util.Date;

public class InterventoDto {

    private Integer codPai;
    private String codTipint;
    private Integer cntTipint;
    private Integer durMesi = null;
    private Date dtAvvio = null;
    private Date dtFine = null;
    private BigDecimal quantita = null;
    private Tariffa tariffa = null;
    private TipologiaIntervento tipologiaIntervento = null;


    public InterventoDto(Integer codPai, String codTipint, Integer cntTipint){
        this.codPai = codPai;
        this.codTipint = codTipint;
        this.cntTipint = cntTipint;
    }

    public InterventoDto(PaiIntervento intervento){
        fill_properties(intervento);
    }

    public InterventoDto(PaiIntervento intervento, Integer mesiProroga) {
        intervento.setDurMesi(mesiProroga);
        fill_properties(intervento);
    }

    public InterventoDto(PaiIntervento intervento, Date al) {
        intervento.setDtFine(al);
        fill_properties(intervento);
    }

    public InterventoDto(PaiIntervento intervento, Date dtAvvio, Date al) {
        intervento.setDtAvvio(dtAvvio);
        intervento.setDtFine(al);
        fill_properties(intervento);
    }

    public Integer getDurMesi() {
        return durMesi;
    }

    public Date getDtAvvio() {
        return dtAvvio;
    }

    public Date getDtFine() {
        return dtFine;
    }

    public BigDecimal getQuantita() {
        return quantita;
    }

    public Tariffa getTariffa() {
        return tariffa;
    }

    public TipologiaIntervento getTipologiaIntervento() {
        return tipologiaIntervento;
    }

    public Integer getCodPai() {
        return codPai;
    }

    public String getCodTipint() {
        return codTipint;
    }

    public Integer getCntTipint() {
        return cntTipint;
    }

    public void setCodPai(Integer codPai) {
        this.codPai = codPai;
    }

    public void setCodTipint(String codTipint) {
        this.codTipint = codTipint;
    }

    public void setCntTipint(Integer cntTipint) {
        this.cntTipint = cntTipint;
    }

    private void fill_properties(PaiIntervento intervento) {
        durMesi = intervento.getDurMesi();
        dtAvvio = intervento.getDtAvvio();
        dtFine = intervento.calculateDtFine();
        quantita = intervento.getQuantita();
        tariffa = intervento.getTariffa();
        tipologiaIntervento = intervento.getTipologiaIntervento();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InterventoDto{");
        sb.append("codPai=").append(codPai);
        sb.append(", codTipint='").append(codTipint).append('\'');
        sb.append(", cntTipint=").append(cntTipint);
        sb.append('}');
        return sb.toString();
    }
}
