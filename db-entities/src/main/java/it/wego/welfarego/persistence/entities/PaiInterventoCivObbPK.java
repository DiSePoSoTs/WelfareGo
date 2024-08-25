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
public class PaiInterventoCivObbPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private Integer cntTipint;

	@Basic(optional = false)
	@Column(name = "COD_ANA_CO", nullable = false)
	private Integer codAnaCo;

	public void setCodPai(Integer codPai) {
		this.codPai = codPai;
	}

	public void setCodTipint(String codTipint) {
		this.codTipint = codTipint;
	}

	public void setCntTipint(Integer cntTipint) {
		this.cntTipint = cntTipint;
	}

	public void setCodAnaCo(Integer codAnaCo) {
		this.codAnaCo = codAnaCo;
	}

	public Integer getCodPai() {
		return codPai;
	}

	public String getCodTipint() {
		return codTipint;
	}

	public Integer getCntTipint() {
		return cntTipint;
	}

	public Integer getCodAnaCo() {
		return codAnaCo;
	}

	public PaiInterventoCivObbPK() {
	}

	public PaiInterventoCivObbPK(Integer codPai, String codTipint, Integer cntTipint, Integer codAnaCo) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
		this.codAnaCo = codAnaCo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaiInterventoCivObbPK other = (PaiInterventoCivObbPK) obj;
		if (this.codPai != other.codPai && (this.codPai == null || !this.codPai.equals(other.codPai))) {
			return false;
		}
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.cntTipint != other.cntTipint && (this.cntTipint == null || !this.cntTipint.equals(other.cntTipint))) {
			return false;
		}
		if (this.codAnaCo != other.codAnaCo && (this.codAnaCo == null || !this.codAnaCo.equals(other.codAnaCo))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (this.codPai != null ? this.codPai.hashCode() : 0);
		hash = 29 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 29 * hash + (this.cntTipint != null ? this.cntTipint.hashCode() : 0);
		hash = 29 * hash + (this.codAnaCo != null ? this.codAnaCo.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiInterventoCivObbPK[codPai=" + codPai + ", codTipint="
				+ codTipint + ", cntTipint=" + cntTipint + ", codAnaCo=" + codAnaCo + "]";
	}
}
