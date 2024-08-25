/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 *
 * @author giuseppe
 */
@Embeddable
public class PaiInterventoAnagraficaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private int codPai;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private int cntTipint;

	@Basic(optional = false)
	@Column(name = "COD_ANA_SOC", nullable = false)
	private int codAnaSoc;

	@Basic(optional = false)
	@Column(name = "ID_PARAM_QUAL_ANA_SOC", nullable = false)
	private int idParamQualAnaSoc;

	public PaiInterventoAnagraficaPK() {
	}

	public PaiInterventoAnagraficaPK(int codPai, String codTipint, int cntTipint, int codAnaSoc,
			int idParamQualAnaSoc) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
		this.codAnaSoc = codAnaSoc;
		this.idParamQualAnaSoc = idParamQualAnaSoc;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) codPai;
		hash += (codTipint != null ? codTipint.hashCode() : 0);
		hash += (int) cntTipint;
		hash += (int) codAnaSoc;
		hash += (int) idParamQualAnaSoc;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiInterventoAnagraficaPK)) {
			return false;
		}
		PaiInterventoAnagraficaPK other = (PaiInterventoAnagraficaPK) object;
		if (this.codPai != other.codPai) {
			return false;
		}
		if ((this.codTipint == null && other.codTipint != null)
				|| (this.codTipint != null && !this.codTipint.equals(other.codTipint))) {
			return false;
		}
		if (this.cntTipint != other.cntTipint) {
			return false;
		}
		if (this.codAnaSoc != other.codAnaSoc) {
			return false;
		}
		if (this.idParamQualAnaSoc != other.idParamQualAnaSoc) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiInterventoAnagraficaPK[codPai=" + codPai + ", codTipint="
				+ codTipint + ", cntTipint=" + cntTipint + ", codAnaSoc=" + codAnaSoc + ", idParamQualAnaSoc="
				+ idParamQualAnaSoc + "]";
	}

}
