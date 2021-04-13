/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author Fabio Bonaccorso
 */
@Embeddable
@Getter
@Setter
public class RicevutaCassaPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private Integer cntTipint;

	public RicevutaCassaPK() {
	}

	public RicevutaCassaPK(Integer codPai, Integer cntTipint) {
		this.codPai = codPai;
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
		final RicevutaCassaPK other = (RicevutaCassaPK) obj;
		if (this.codPai != other.codPai && (this.codPai == null || !this.codPai.equals(other.codPai))) {
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
		hash = 79 * hash + (this.cntTipint != null ? this.cntTipint.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.RicevutaCassaPK[codPai=" + codPai + ", cntTipint=" + cntTipint
				+ "]";
	}
}
