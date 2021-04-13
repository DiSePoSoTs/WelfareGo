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
public class BudgetTipInterventoUotPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "COD_ANNO", nullable = false)
	private short codAnno;

	@Basic(optional = false)
	@Column(name = "COD_IMPE", nullable = false)
	private String codImpe;

	@Basic(optional = false)
	@Column(name = "ID_PARAM_UOT", nullable = false)
	private int idParamUot;

	public BudgetTipInterventoUotPK() {
	}

	public BudgetTipInterventoUotPK(String codTipint, short codAnno, String codImpe, int idParamUot) {
		this.codTipint = codTipint;
		this.codAnno = codAnno;
		this.codImpe = codImpe;
		this.idParamUot = idParamUot;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BudgetTipInterventoUotPK other = (BudgetTipInterventoUotPK) obj;
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.codAnno != other.codAnno) {
			return false;
		}
		if ((this.codImpe == null) ? (other.codImpe != null) : !this.codImpe.equals(other.codImpe)) {
			return false;
		}
		if (this.idParamUot != other.idParamUot) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 41 * hash + this.codAnno;
		hash = 41 * hash + (this.codImpe != null ? this.codImpe.hashCode() : 0);
		hash = 41 * hash + this.idParamUot;
		return hash;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.BudgetTipInterventoUotPK[codTipint=" + codTipint + ", codAnno="
				+ codAnno + ", codImpe=" + codImpe + ", idParamUot=" + idParamUot + "]";
	}
}
