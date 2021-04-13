package it.wego.welfarego.scheduler.rinnovi.helper;


import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;

import java.math.BigDecimal;

public class RinnovoInterventoLogBuilder {

    private  PaiIntervento interventoPadre = null;
    private  boolean intervento_di_rinnovo_assente = false;
    private  PaiIntervento nuovoIntervento = null;
    private boolean rinnovo_automatico_con_budget_intervento_padre = false;
    private BudgetTipInterventoPK budgetPadrePk = null;
    private BigDecimal bdgPrevEur = null;
    private BigDecimal bdgPrevQta = null;
    private boolean isAnnoSingolo = false;
    private String casoPropostaAnniMultipli = null;
    private String note = null;
    private String pkInterventoPadre= null;
    private String pkInterventoFiglio = null;
    private BudgetTipInterventoPK budgetAnnoAvvioPK = null;
    private BudgetTipInterventoPK budgetPerPropostePrimoAnno = null;
    private BudgetTipInterventoPK budgetPerProposteSecodoAnno = null;


    public RinnovoInterventoLogBuilder setInterventoPadre(PaiIntervento interventoPadre) {

        this.interventoPadre = interventoPadre;
        this.pkInterventoPadre = SchedulerHelper.dumpPkIntervento(interventoPadre);
        return this;
    }

    public RinnovoInterventoLogBuilder is_intervento_di_rinnovo_assente(boolean intervento_di_rinnovo_assente) {
        this.intervento_di_rinnovo_assente = intervento_di_rinnovo_assente;
        return this;
    }

    public RinnovoInterventoLogBuilder setInterventoFiglio(PaiIntervento nuovoIntervento) {
        this.nuovoIntervento = nuovoIntervento;
        this.pkInterventoFiglio = SchedulerHelper.dumpPkIntervento(nuovoIntervento);
        return this;
    }

    public void rinnovo_con_budget_delPadre(boolean rinnovo_automatico_con_budget_intervento_padre) {
        this.rinnovo_automatico_con_budget_intervento_padre = rinnovo_automatico_con_budget_intervento_padre;
    }

    public void setBudgetPadrePk(BudgetTipInterventoPK budgetPadrePk) {
        this.budgetPadrePk = budgetPadrePk;
    }

    public void setBudgetPrevEur(BigDecimal bdgPrevEur) {
        this.bdgPrevEur = bdgPrevEur;
    }

    public void setBudgetPrevQta(BigDecimal bdgPrevQta) {
        this.bdgPrevQta = bdgPrevQta;
    }

    public void isAnnoSingolo(boolean isAnnoSingolo) {
        this.isAnnoSingolo = isAnnoSingolo;
    }

    public void setCasoPropostaAnniMultipli(String casoPropostaAnniMultipli) {
        this.casoPropostaAnniMultipli = casoPropostaAnniMultipli;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public void setBudgetPerProposte(BudgetTipInterventoPK budgetAnnoAvvioPK) {
        this.budgetAnnoAvvioPK = budgetAnnoAvvioPK;
    }

    public void setBudgetPerPropostePrimoAnno(BudgetTipInterventoPK budgetPk) {
        this.budgetPerPropostePrimoAnno = budgetPk;
    }

    public void setBudgetPerProposteSecondoAnno(BudgetTipInterventoPK budgetPk) {
        this.budgetPerProposteSecodoAnno = budgetPk;
    }
}

