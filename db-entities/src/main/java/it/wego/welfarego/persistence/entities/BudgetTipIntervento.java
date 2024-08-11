/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "BUDGET_TIP_INTERVENTO", uniqueConstraints = { @UniqueConstraint(columnNames = { "COD_IMPE" }) })
@NamedQueries({ @NamedQuery(name = "BudgetTipIntervento.findAll", query = "SELECT b FROM BudgetTipIntervento b"),
		@NamedQuery(name = "BudgetTipIntervento.findByCodTipint", query = "SELECT b FROM BudgetTipIntervento b WHERE b.budgetTipInterventoPK.codTipint = :codTipint"),
		@NamedQuery(name = "BudgetTipIntervento.findBy_CodTipInt_And_Cod_Anno", query = "SELECT b FROM BudgetTipIntervento b WHERE b.budgetTipInterventoPK.codTipint = :codTipint and b.budgetTipInterventoPK.codAnno in :anni"),

		@NamedQuery(name = "BudgetTipIntervento.findByCodAnno", query = "SELECT b FROM BudgetTipIntervento b WHERE b.budgetTipInterventoPK.codAnno = :codAnno"),
		@NamedQuery(name = "BudgetTipIntervento.findByCodImpe", query = "SELECT b FROM BudgetTipIntervento b WHERE b.budgetTipInterventoPK.codImpe = :codImpe"),
		@NamedQuery(name = "BudgetTipIntervento.findByCodConto", query = "SELECT b FROM BudgetTipIntervento b WHERE b.codConto = :codConto"),
		@NamedQuery(name = "BudgetTipIntervento.findByCodSconto", query = "SELECT b FROM BudgetTipIntervento b WHERE b.codSconto = :codSconto"),
		@NamedQuery(name = "BudgetTipIntervento.findByCodCap", query = "SELECT b FROM BudgetTipIntervento b WHERE b.codCap = :codCap"),
		@NamedQuery(name = "BudgetTipIntervento.findByNumDx", query = "SELECT b FROM BudgetTipIntervento b WHERE b.numDx = :numDx"),
		@NamedQuery(name = "BudgetTipIntervento.findByDtDx", query = "SELECT b FROM BudgetTipIntervento b WHERE b.dtDx = :dtDx"),
		@NamedQuery(name = "BudgetTipIntervento.findByBdgDispEur", query = "SELECT b FROM BudgetTipIntervento b WHERE b.bdgDispEur = :bdgDispEur"),
		@NamedQuery(name = "BudgetTipIntervento.findByBdgDispOre", query = "SELECT b FROM BudgetTipIntervento b WHERE b.bdgDispOre = :bdgDispOre") })
public class BudgetTipIntervento implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected BudgetTipInterventoPK budgetTipInterventoPK;

	public BudgetTipInterventoPK getBudgetTipInterventoPK() {
		return budgetTipInterventoPK;
	}

	@Basic(optional = false)
	@Column(name = "COD_CONTO", nullable = false)
	private int codConto;

	public int getNumDx() {
		return numDx;
	}

	public void setNumDx(int numDx) {
		this.numDx = numDx;
	}

	public Date getDtDx() {
		return dtDx;
	}

	public void setDtDx(Date dtDx) {
		this.dtDx = dtDx;
	}

	public int getCodConto() {
		return codConto;
	}

	public BigDecimal getBdgDispEur() {
		return bdgDispEur;
	}

	@Basic(optional = false)
	@Column(name = "COD_SCONTO", nullable = false)
	private int codSconto;

	@Column(name = "ANNO_EROGAZIONE")
	private Short annoErogazione;

	public Short getAnnoErogazione() {
		return annoErogazione;
	}

	@Basic(optional = false)
	@Column(name = "COD_CAP", nullable = false)
	private int codCap;

	public int getCodCap() {
		return codCap;
	}

	@Basic(optional = false)
	@Column(name = "NUM_DX", nullable = false)
	private int numDx;

	@Basic(optional = false)
	@Column(name = "DT_DX", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtDx;

	@Column(name = "BDG_DISP_EUR", precision = 9, scale = 2)
	private BigDecimal bdgDispEur;

	@Column(name = "BDG_DISP_ORE", precision = 9, scale = 2)
	private BigDecimal bdgDispOre;

	public BigDecimal getBdgDispOre() {
		return bdgDispOre;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "budgetTipIntervento")
	private List<PaiInterventoMese> paiInterventoMeseList;

	@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipologiaIntervento tipologiaIntervento;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "budgetTipIntervento")
	private List<BudgetTipInterventoUot> budgetTipInterventoUotList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "budgetTipIntervento")
	@MapKey(name = "parametriIndataUot")
	private Map<ParametriIndata, BudgetTipInterventoUot> budgetTipInterventoUotByUot;

	public BudgetTipIntervento() {
	}

	public BudgetTipIntervento(BudgetTipInterventoPK budgetTipInterventoPK) {
		this.budgetTipInterventoPK = budgetTipInterventoPK;
	}

	public BudgetTipIntervento(BudgetTipInterventoPK budgetTipInterventoPK, int codConto, int codSconto, int codCap,
			int numDx, Date dtDx) {
		this.budgetTipInterventoPK = budgetTipInterventoPK;
		this.codConto = codConto;
		this.codSconto = codSconto;
		this.codCap = codCap;
		this.numDx = numDx;
		this.dtDx = dtDx;
	}

	public BudgetTipIntervento(String codTipint, short codAnno, String codImpe) {
		this.budgetTipInterventoPK = new BudgetTipInterventoPK(codTipint, codAnno, codImpe);
	}

    public BudgetTipInterventoUot getBudgetTipInterventoUot(ParametriIndata parametriIndataUot) {
        return budgetTipInterventoUotByUot.get(parametriIndataUot);
    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (budgetTipInterventoPK != null ? budgetTipInterventoPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof BudgetTipIntervento)) {
			return false;
		}
		BudgetTipIntervento other = (BudgetTipIntervento) object;
		if ((this.budgetTipInterventoPK == null && other.budgetTipInterventoPK != null)
				|| (this.budgetTipInterventoPK != null
						&& !this.budgetTipInterventoPK.equals(other.budgetTipInterventoPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.BudgetTipIntervento[budgetTipInterventoPK="
				+ budgetTipInterventoPK + "]";
	}
}
