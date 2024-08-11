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
public class PaiInterventoMesePK implements Serializable {

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
	@Column(name = "ANNO_EFF", nullable = false)
	private short annoEff;

	public int getCodPai() {
		return codPai;
	}

	public String getCodTipint() {
		return codTipint;
	}

	public int getCntTipint() {
		return cntTipint;
	}

	public short getMeseEff() {
		return meseEff;
	}

	public short getAnno() {
		return anno;
	}

	public String getCodImp() {
		return codImp;
	}

	public short getAnnoEff() {
		return annoEff;
	}

	@Basic(optional = false)
	@Column(name = "MESE_EFF", nullable = false)
	private short meseEff;

	@Basic(optional = false)
	@Column(name = "ANNO", nullable = false)
	private short anno;

	@Basic(optional = false)
	@Column(name = "COD_IMP", nullable = false)
	private String codImp;

	public PaiInterventoMesePK() {
	}

	public PaiInterventoMesePK(int codPai, String codTipint, int cntTipint, short annoEff, short meseEff, short anno,
			String codImp) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
		this.annoEff = annoEff;
		this.meseEff = meseEff;
		this.anno = anno;
		this.codImp = codImp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaiInterventoMesePK other = (PaiInterventoMesePK) obj;
		if (this.codPai != other.codPai) {
			return false;
		}
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.cntTipint != other.cntTipint) {
			return false;
		}
		if (this.annoEff != other.annoEff) {
			return false;
		}
		if (this.meseEff != other.meseEff) {
			return false;
		}
		if (this.anno != other.anno) {
			return false;
		}
		if ((this.codImp == null) ? (other.codImp != null) : !this.codImp.equals(other.codImp)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.codPai;
		hash = 53 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 53 * hash + this.cntTipint;
		hash = 53 * hash + this.annoEff;
		hash = 53 * hash + this.meseEff;
		hash = 53 * hash + this.anno;
		hash = 53 * hash + (this.codImp != null ? this.codImp.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {

		String msgTemplate = "cod_pai=%d and cod_Tipint='%s' and cnt_Tipint=%d and anno_eff=%d meseEff=%d , anno=%d , codImp='%s'";
		String msg = String.format(msgTemplate, codPai, codTipint, cntTipint, annoEff, meseEff, anno, codImp);

		return msg;
	}

	public void setCodPai(int codPai) {
		this.codPai = codPai;
	}

	public void setCodTipint(String codTipint) {
		this.codTipint = codTipint;
	}

	public void setCntTipint(int cntTipint) {
		this.cntTipint = cntTipint;
	}

	public void setAnnoEff(short annoEff) {
		this.annoEff = annoEff;
	}

	public void setMeseEff(short meseEff) {
		this.meseEff = meseEff;
	}

	public void setAnno(short anno) {
		this.anno = anno;
	}

	public void setCodImp(String codImp) {
		this.codImp = codImp;
	}
}
