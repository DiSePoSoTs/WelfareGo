package it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti.test.factory;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatiDto {

    private Map<BudgetTipIntervento, DatiAttesi> dati_attesi_per_budget = new HashMap<BudgetTipIntervento, DatiAttesi>();
    private PaiIntervento paiIntervento;
    private List<BudgetTipIntervento> budgetsPerIntervento;

    public DatiDto() {
    }

    public DatiDto(PaiIntervento paiIntervento, List<BudgetTipIntervento> budgetsPerIntervento, Map<BudgetTipIntervento, DatiAttesi> numeroIntervalliAttesi_perBudget) {
        this.paiIntervento = paiIntervento;
        this.budgetsPerIntervento = budgetsPerIntervento;
        this.dati_attesi_per_budget = numeroIntervalliAttesi_perBudget;
    }

    public PaiIntervento getPaiIntervento() {
        return paiIntervento;
    }


    public Integer getNumeroIntervalliAttesi(BudgetTipIntervento budget) {
        return dati_attesi_per_budget.get(budget).numeroIntervalli;
    }

    public List<BudgetTipIntervento> getBudgetsPerIntervento() {
        return budgetsPerIntervento;
    }

    public int getAnnoPagamenti(BudgetTipIntervento budget) {
        return dati_attesi_per_budget.get(budget).anno_pagamenti;
    }

    public int getMeseInizioPagamenti(BudgetTipIntervento budget) {
        return dati_attesi_per_budget.get(budget).mese_inizio_pagamenti;
    }


    class DatiAttesi{
        int anno_pagamenti;
        int mese_inizio_pagamenti;
        int numeroIntervalli;

        DatiAttesi(int anno_pagamenti, int mese_inizio_pagamenti, int numero_pagamenti) {
            this.anno_pagamenti = anno_pagamenti;
            this.mese_inizio_pagamenti = mese_inizio_pagamenti;
            this.numeroIntervalli = numero_pagamenti;
        }
    }


    @Override
    public String toString() {
        String budget_e_dati_attesi = " [(";
        for(BudgetTipIntervento budget:this.budgetsPerIntervento){
            budget_e_dati_attesi += budget.getAnnoErogazione() + " - " + this.getAnnoPagamenti(budget) + "/" + this.getMeseInizioPagamenti(budget) + ", num pagamenti: " + this.getNumeroIntervalliAttesi(budget) + "), ";
        }
        budget_e_dati_attesi = budget_e_dati_attesi.trim();
        budget_e_dati_attesi = budget_e_dati_attesi.substring(0, budget_e_dati_attesi.length()-1);
        budget_e_dati_attesi += "]";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dtAvvio = paiIntervento.getDtAvvio();
        final StringBuffer sb = new StringBuffer("datiDto intervento (Data avvio, durata mesi): " + sdf.format(dtAvvio )+ ", " + paiIntervento.getDurMesi());
        sb.append(",  (budget - dati attesi): " + budget_e_dati_attesi);
        return sb.toString();
    }
}
