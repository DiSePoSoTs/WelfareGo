package it.wego.welfarego.scheduler.dto;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;

public class BudgetEAnniDto {

	private BudgetTipIntervento budgetAnnoAvvio;
	private BudgetTipIntervento budgetAnnoFine;
	private int annoAvvio = 0;
	private int annoFine = 0;
	
	
	public BudgetTipIntervento getBudgetAnnoAvvio() {
		return budgetAnnoAvvio;
	}
	public void setBudgetAnnoAvvio(BudgetTipIntervento budgetAnnoAvvio) {
		this.budgetAnnoAvvio = budgetAnnoAvvio;
	}
	public BudgetTipIntervento getBudgetAnnoFine() {
		return budgetAnnoFine;
	}
	public void setBudgetAnnoFine(BudgetTipIntervento budgetAnnoFine) {
		this.budgetAnnoFine = budgetAnnoFine;
	}
	public int getAnnoAvvio() {
		return annoAvvio;
	}
	public void setAnnoAvvio(int annoAvvio) {
		this.annoAvvio = annoAvvio;
	}
	public int getAnnoFine() {
		return annoFine;
	}
	public void setAnnoFine(int annoFine) {
		this.annoFine = annoFine;
	}
	
	
}
