/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
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
@Table(name = "PAI_INTERVENTO_ANAGRAFICA")
@NamedQueries({
		@NamedQuery(name = "PaiInterventoAnagrafica.findAll", query = "SELECT p FROM PaiInterventoAnagrafica p"),
		@NamedQuery(name = "PaiInterventoAnagrafica.findByCodPai", query = "SELECT p FROM PaiInterventoAnagrafica p WHERE p.paiInterventoAnagraficaPK.codPai = :codPai"),
		@NamedQuery(name = "PaiInterventoAnagrafica.findByCodTipint", query = "SELECT p FROM PaiInterventoAnagrafica p WHERE p.paiInterventoAnagraficaPK.codTipint = :codTipint"),
		@NamedQuery(name = "PaiInterventoAnagrafica.findByCntTipint", query = "SELECT p FROM PaiInterventoAnagrafica p WHERE p.paiInterventoAnagraficaPK.cntTipint = :cntTipint"),
		@NamedQuery(name = "PaiInterventoAnagrafica.findByCodAnaSoc", query = "SELECT p FROM PaiInterventoAnagrafica p WHERE p.paiInterventoAnagraficaPK.codAnaSoc = :codAnaSoc"),
		@NamedQuery(name = "PaiInterventoAnagrafica.findByIdParamQualAnaSoc", query = "SELECT p FROM PaiInterventoAnagrafica p WHERE p.paiInterventoAnagraficaPK.idParamQualAnaSoc = :idParamQualAnaSoc") })
public class PaiInterventoAnagrafica implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiInterventoAnagraficaPK paiInterventoAnagraficaPK;

	@JoinColumn(name = "ID_PARAM_QUAL_ANA_SOC", referencedColumnName = "ID_PARAM_INDATA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ParametriIndata parametriIndata;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_ANA_SOC", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSoc;

	public PaiInterventoAnagrafica() {
	}

	public PaiInterventoAnagrafica(PaiInterventoAnagraficaPK paiInterventoAnagraficaPK) {
		this.paiInterventoAnagraficaPK = paiInterventoAnagraficaPK;
	}

	public PaiInterventoAnagrafica(int codPai, String codTipint, int cntTipint, int codAnaSoc, int idParamQualAnaSoc) {
		this.paiInterventoAnagraficaPK = new PaiInterventoAnagraficaPK(codPai, codTipint, cntTipint, codAnaSoc,
				idParamQualAnaSoc);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paiInterventoAnagraficaPK != null ? paiInterventoAnagraficaPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiInterventoAnagrafica)) {
			return false;
		}
		PaiInterventoAnagrafica other = (PaiInterventoAnagrafica) object;
		if ((this.paiInterventoAnagraficaPK == null && other.paiInterventoAnagraficaPK != null)
				|| (this.paiInterventoAnagraficaPK != null
						&& !this.paiInterventoAnagraficaPK.equals(other.paiInterventoAnagraficaPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiInterventoAnagrafica[paiInterventoAnagraficaPK="
				+ paiInterventoAnagraficaPK + "]";
	}

}
