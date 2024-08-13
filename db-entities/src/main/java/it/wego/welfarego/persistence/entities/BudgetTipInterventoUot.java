/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "BUDGET_TIP_INTERVENTO_UOT")
@NamedQueries({ @NamedQuery(name = "BudgetTipInterventoUot.findAll", query = "SELECT b FROM BudgetTipInterventoUot b"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByCodTipint", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.budgetTipInterventoUotPK.codTipint = :codTipint"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByCodAnno", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.budgetTipInterventoUotPK.codAnno = :codAnno"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByCodImpe", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.budgetTipInterventoUotPK.codImpe = :codImpe"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByIdParamUot", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.budgetTipInterventoUotPK.idParamUot = :idParamUot"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByBdgDispEur", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.bdgDispEur = :bdgDispEur"),
		@NamedQuery(name = "BudgetTipInterventoUot.findByBdgDispOre", query = "SELECT b FROM BudgetTipInterventoUot b WHERE b.bdgDispOre = :bdgDispOre") })
public class BudgetTipInterventoUot implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected BudgetTipInterventoUotPK budgetTipInterventoUotPK;

	@Column(name = "BDG_DISP_EUR", precision = 9, scale = 2)
	private BigDecimal bdgDispEur;

	public BigDecimal getBdgDispEur() {
		return bdgDispEur;
	}

	@Column(name = "BDG_DISP_ORE", precision = 9, scale = 2)
	private BigDecimal bdgDispOre;

	public BigDecimal getBdgDispOre() {
		return bdgDispOre;
	}

	public void setBdgDispEur(BigDecimal bdgDispEur) {
		this.bdgDispEur = bdgDispEur;
	}

	public void setBudgetTipInterventoUotPK(BudgetTipInterventoUotPK budgetTipInterventoUotPK) {
		this.budgetTipInterventoUotPK = budgetTipInterventoUotPK;
	}

	public void setBdgDispOre(BigDecimal bdgDispOre) {
		this.bdgDispOre = bdgDispOre;
	}

	public BudgetTipInterventoUotPK getBudgetTipInterventoUotPK() {
		return budgetTipInterventoUotPK;
	}

	@JoinColumn(name = "ID_PARAM_UOT", referencedColumnName = "ID_PARAM_INDATA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ParametriIndata parametriIndataUot;

	public ParametriIndata getParametriIndataUot() {
		return parametriIndataUot;
	}

	@JoinColumns({
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_ANNO", referencedColumnName = "COD_ANNO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_IMPE", referencedColumnName = "COD_IMPE", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private BudgetTipIntervento budgetTipIntervento;

	public BudgetTipIntervento getBudgetTipIntervento() {
		return budgetTipIntervento;
	}

	public BudgetTipInterventoUot() {
	}

	public BudgetTipInterventoUot(BudgetTipInterventoUotPK budgetTipInterventoUotPK) {
		this.budgetTipInterventoUotPK = budgetTipInterventoUotPK;
	}

	public BudgetTipInterventoUot(String codTipint, short codAnno, String codImpe, int idParamUot) {
		this.budgetTipInterventoUotPK = new BudgetTipInterventoUotPK(codTipint, codAnno, codImpe, idParamUot);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (budgetTipInterventoUotPK != null ? budgetTipInterventoUotPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof BudgetTipInterventoUot)) {
			return false;
		}
		BudgetTipInterventoUot other = (BudgetTipInterventoUot) object;
		if ((this.budgetTipInterventoUotPK == null && other.budgetTipInterventoUotPK != null)
				|| (this.budgetTipInterventoUotPK != null
						&& !this.budgetTipInterventoUotPK.equals(other.budgetTipInterventoUotPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.BudgetTipInterventoUot[budgetTipInterventoUotPK="
				+ budgetTipInterventoUotPK + "]";
	}
}
