/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
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
@Table(name = "PAI_INTERVENTO_CIV_OBB")
@NamedQueries({ @NamedQuery(name = "PaiInterventoCivObb.findAll", query = "SELECT p FROM PaiInterventoCivObb p"),
		@NamedQuery(name = "PaiInterventoCivObb.findByCodPai", query = "SELECT p FROM PaiInterventoCivObb p WHERE p.paiInterventoCivObbPK.codPai = :codPai"),
		@NamedQuery(name = "PaiInterventoCivObb.findByCodTipint", query = "SELECT p FROM PaiInterventoCivObb p WHERE p.paiInterventoCivObbPK.codTipint = :codTipint"),
		@NamedQuery(name = "PaiInterventoCivObb.findByCntTipint", query = "SELECT p FROM PaiInterventoCivObb p WHERE p.paiInterventoCivObbPK.cntTipint = :cntTipint"),
		@NamedQuery(name = "PaiInterventoCivObb.findByCodAnaCo", query = "SELECT p FROM PaiInterventoCivObb p WHERE p.paiInterventoCivObbPK.codAnaCo = :codAnaCo"),
		@NamedQuery(name = "PaiInterventoCivObb.findByImpCo", query = "SELECT p FROM PaiInterventoCivObb p WHERE p.impCo = :impCo") })
public class PaiInterventoCivObb implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiInterventoCivObbPK paiInterventoCivObbPK;

	@Basic(optional = false)
	@Column(name = "IMP_CO", nullable = false, precision = 9, scale = 2)
	private BigDecimal impCo;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_ANA_CO", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSoc;

	public PaiInterventoCivObb() {
	}

	public PaiInterventoCivObb(PaiInterventoCivObbPK paiInterventoCivObbPK) {
		this.paiInterventoCivObbPK = paiInterventoCivObbPK;
	}

	public PaiInterventoCivObb(PaiInterventoCivObbPK paiInterventoCivObbPK, BigDecimal impCo) {
		this.paiInterventoCivObbPK = paiInterventoCivObbPK;
		this.impCo = impCo;
	}

	public PaiInterventoCivObb(int codPai, String codTipint, int cntTipint, int codAnaCo) {
		this.paiInterventoCivObbPK = new PaiInterventoCivObbPK(codPai, codTipint, cntTipint, codAnaCo);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paiInterventoCivObbPK != null ? paiInterventoCivObbPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiInterventoCivObb)) {
			return false;
		}
		PaiInterventoCivObb other = (PaiInterventoCivObb) object;
		if ((this.paiInterventoCivObbPK == null && other.paiInterventoCivObbPK != null)
				|| (this.paiInterventoCivObbPK != null
						&& !this.paiInterventoCivObbPK.equals(other.paiInterventoCivObbPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiInterventoCivObb[paiInterventoCivObbPK="
				+ paiInterventoCivObbPK + "]";
	}

	public BigDecimal getImpCo() {
		return impCo;
	}

	public void setImpCo(BigDecimal impCo) {
		this.impCo = impCo;
	}

	public AnagrafeSoc getAnagrafeSoc() {
		return anagrafeSoc;
	}

	public void setAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
		this.anagrafeSoc = anagrafeSoc;
	}

}
