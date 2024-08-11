/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Embeddable
@Getter
@Setter
public class PaiInterventoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer getCodPai() {
		return codPai;
	}

	public void setCodPai(Integer codPai) {
		this.codPai = codPai;
	}

	public void setCodTipint(String codTipint) {
		this.codTipint = codTipint;
	}

	public void setCntTipint(Integer cntTipint) {
		this.cntTipint = cntTipint;
	}

	public String getCodTipint() {
		return codTipint;
	}

	public Integer getCntTipint() {
		return cntTipint;
	}

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private Integer cntTipint;

	public PaiInterventoPK() {
	}

	public PaiInterventoPK(Integer codPai, String codTipint, Integer cntTipint) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaiInterventoPK other = (PaiInterventoPK) obj;
		if (this.codPai != other.codPai && (this.codPai == null || !this.codPai.equals(other.codPai))) {
			return false;
		}
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.cntTipint != other.cntTipint && (this.cntTipint == null || !this.cntTipint.equals(other.cntTipint))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + (this.codPai != null ? this.codPai.hashCode() : 0);
		hash = 79 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 79 * hash + (this.cntTipint != null ? this.cntTipint.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "[cod_Pai=" + codPai + ", cod_Tipint=" + codTipint + ", cnt_Tipint=" + cntTipint + "]";
	}
}
