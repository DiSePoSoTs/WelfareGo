package it.wego.welfarego.persistence.dao;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;

/**
 * Classe introdotta per mitigare i problemi di lentezza delle queri eseguite in sequenza.
 *   
 * @author DOTCOM S.R.L.
 *
 */
public class PaiInterventoMeseDaoCached extends PaiInterventoMeseDao {

	private final BudgetTipIntervento budgetTipIntervento;
	
	//dati in cache
	private BigDecimal sumBdgtCons;
	private BigDecimal sumBdgEsentCons;
	private BigDecimal sumBdgtPrevDaDeterminare;
	private BigDecimal sumBdgtPrevDaAcquisire;
	private BigDecimal sumBdgtPrev;
	private BigDecimal sumBdgEsentPrev;
	private BigDecimal sumBdgtPrevAndConsQta;
	private BigDecimal sumBdgtConsQta;
	
	public PaiInterventoMeseDaoCached(BudgetTipIntervento budgetTipIntervento, EntityManager em) {
		super(em);
		this.budgetTipIntervento = budgetTipIntervento;
	}

	//metodi riscritti
	public BigDecimal calculateRealBudgetDisp() {
  	  return budgetTipIntervento.getBdgDispEur()
              .subtract(sumBdgtCons())
              .add(sumBdgEsentCons()).subtract(sumBdgtPrevDaDeterminare()).subtract(sumBdgtPrevDaAcquisire());
	}
	public Object calculateBdgtDisp() {
        return budgetTipIntervento.getBdgDispEur()
                .subtract(sumBdgtPrev())
                .subtract(sumBdgtCons())
                .add(sumBdgEsentPrev())
                .add(sumBdgEsentCons());
	}
	
	public Object calculateBdgtDispQta() {
        return budgetTipIntervento.getBdgDispOre().subtract(sumBdgtPrevAndConsQta());
	}
	public Object calculateBdgtDispQtaCons() {
		return budgetTipIntervento.getBdgDispOre().subtract(sumBdgtConsQta());
	}
	
	//metodi gestione cache

	private BigDecimal sumBdgtConsQta() {
		return this.sumBdgtConsQta != null ? this.sumBdgtConsQta : (this.sumBdgtConsQta = this.sumBdgtConsQta(this.budgetTipIntervento));
	}
	
	private BigDecimal sumBdgtPrevAndConsQta() {
		return this.sumBdgtPrevAndConsQta != null ? this.sumBdgtPrevAndConsQta : (this.sumBdgtPrevAndConsQta = this.sumBdgtPrevAndConsQta(this.budgetTipIntervento));
	}
	
	private BigDecimal sumBdgEsentPrev() {
		return this.sumBdgEsentPrev != null ? this.sumBdgEsentPrev : (this.sumBdgEsentPrev = this.sumBdgEsentPrev(this.budgetTipIntervento));
	}

	private BigDecimal sumBdgtPrev() {
		return this.sumBdgtPrev != null ? this.sumBdgtPrev : (this.sumBdgtPrev = this.sumBdgtPrev(this.budgetTipIntervento));
	}

	private BigDecimal sumBdgtPrevDaAcquisire() {
		return this.sumBdgtPrevDaAcquisire != null ? this.sumBdgtPrevDaAcquisire : (this.sumBdgtPrevDaAcquisire = this.sumBdgtPrevDaAcquisire(this.budgetTipIntervento));
	}

	private BigDecimal sumBdgtPrevDaDeterminare() {
		return this.sumBdgtPrevDaDeterminare != null ? this.sumBdgtPrevDaDeterminare : (this.sumBdgtPrevDaDeterminare = this.sumBdgtPrevDaDeterminare(this.budgetTipIntervento));
	}

	private BigDecimal sumBdgEsentCons() {
		return this.sumBdgEsentCons != null ? this.sumBdgEsentCons : (this.sumBdgEsentCons = this.sumBdgEsentCons(this.budgetTipIntervento));
	}

	private BigDecimal sumBdgtCons() {
		return this.sumBdgtCons != null ? this.sumBdgtCons : (this.sumBdgtCons = this.sumBdgtCons(this.budgetTipIntervento));
	}

}
