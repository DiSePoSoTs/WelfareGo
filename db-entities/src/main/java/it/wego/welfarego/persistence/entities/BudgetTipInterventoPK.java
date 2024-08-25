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
public class BudgetTipInterventoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	public void setCodTipint(String codTipint) {
		this.codTipint = codTipint;
	}

	public void setCodAnno(short codAnno) {
		this.codAnno = codAnno;
	}

	public void setCodImpe(String codImpe) {
		this.codImpe = codImpe;
	}

	@Basic(optional = false)
	@Column(name = "COD_ANNO", nullable = false)
	private short codAnno;

	@Basic(optional = false)
	@Column(name = "COD_IMPE", nullable = false)
	private String codImpe;

	public String getCodTipint() {
		return codTipint;
	}

	public short getCodAnno() {
		return codAnno;
	}

	public String getCodImpe() {
		return codImpe;
	}

	public BudgetTipInterventoPK() {
	}

	public BudgetTipInterventoPK(String codTipint, short codAnno, String codImpe) {
		this.codTipint = codTipint;
		this.codAnno = codAnno;
		this.codImpe = codImpe;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BudgetTipInterventoPK other = (BudgetTipInterventoPK) obj;
		if ((this.codTipint == null) ? (other.codTipint != null) : !this.codTipint.equals(other.codTipint)) {
			return false;
		}
		if (this.codAnno != other.codAnno) {
			return false;
		}
		if ((this.codImpe == null) ? (other.codImpe != null) : !this.codImpe.equals(other.codImpe)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.codTipint != null ? this.codTipint.hashCode() : 0);
		hash = 53 * hash + this.codAnno;
		hash = 53 * hash + (this.codImpe != null ? this.codImpe.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "[cod_Tipint=" + codTipint + ", cod_Anno=" + codAnno + ", cod_Impe=" + codImpe + "]";
	}
}
