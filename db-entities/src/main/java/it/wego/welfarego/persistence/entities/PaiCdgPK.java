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
public class PaiCdgPK implements Serializable {

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
	@Column(name = "COD_ANNO", nullable = false)
	private short codAnno;

	@Basic(optional = false)
	@Column(name = "COD_IMPE", nullable = false)
	private String codImpe;

	@Basic(optional = false)
	@Column(name = "ANNO_EFF", nullable = false)
	private short annoEff;

	@Basic(optional = false)
	@Column(name = "MESE_EFF", nullable = false)
	private short meseEff;

	public PaiCdgPK() {
	}

	public PaiCdgPK(int codPai, String codTipint, int cntTipint, short codAnno, String codImpe, short annoEff,
			short meseEff) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
		this.codAnno = codAnno;
		this.codImpe = codImpe;
		this.annoEff = annoEff;
		this.meseEff = meseEff;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaiCdgPK other = (PaiCdgPK) obj;
		if (this.codPai != other.codPai) {
			return false;
		}
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.cntTipint != other.cntTipint) {
			return false;
		}
		if (this.codAnno != other.codAnno) {
			return false;
		}
		if ((this.codImpe == null) ? (other.codImpe != null) : !this.codImpe.equals(other.codImpe)) {
			return false;
		}
		if (this.annoEff != other.annoEff) {
			return false;
		}
		if (this.meseEff != other.meseEff) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + this.codPai;
		hash = 89 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 89 * hash + this.cntTipint;
		hash = 89 * hash + this.codAnno;
		hash = 89 * hash + (this.codImpe != null ? this.codImpe.hashCode() : 0);
		hash = 89 * hash + this.annoEff;
		hash = 89 * hash + this.meseEff;
		return hash;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiCdgPK[codPai=" + codPai + ", codTipint=" + codTipint
				+ ", cntTipint=" + cntTipint + ", codAnno=" + codAnno + ", codImpe=" + codImpe + ", annoEff=" + annoEff
				+ ", meseEff=" + meseEff + "]";
	}
}
