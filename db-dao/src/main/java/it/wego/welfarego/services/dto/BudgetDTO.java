package it.wego.welfarego.services.dto;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;

import java.math.BigDecimal;

public class BudgetDTO {

    private String codTipint;
    private String codImpegno;
    private short codAnno;
    private Integer codConto;
    private BigDecimal bdgDisponibileEuro;
    private Short annoErogazione;


    public BudgetDTO() {}

    public BudgetDTO(BudgetTipIntervento nttBudget) {
            this.codTipint = nttBudget.getBudgetTipInterventoPK().getCodTipint();
            this.codImpegno = nttBudget.getBudgetTipInterventoPK().getCodImpe();
            this.codAnno = nttBudget.getBudgetTipInterventoPK().getCodAnno();
            this.codConto = nttBudget.getCodConto();
            this.annoErogazione = nttBudget.getAnnoErogazione();
            this.bdgDisponibileEuro = nttBudget.getBdgDispEur();
    }


    public String getCodTipint() {
        return codTipint;
    }

    public String getCodImpegno() {
        return codImpegno;
    }

    public short getCodAnno() {
        return codAnno;
    }

    public Integer getCodConto() {
        return codConto;
    }

    public BigDecimal getBdgDisponibileEuro() {
        return bdgDisponibileEuro;
    }

    public Short getAnnoErogazione() {
        return annoErogazione;
    }
}
