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
 * @author aleph
 */
@Embeddable
@Getter
@Setter
public class PaiMicroProblematicaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	@Column(name = "IP_MACRO_PROBLEMATICA", nullable = false)
	private Integer ipMacroProblematica;

	@Column(name = "IP_MICRO_PROBLEMATICA", nullable = false)
	private Integer ipMicroProblematica;

	public PaiMicroProblematicaPK() {
	}

	public PaiMicroProblematicaPK(Integer codPai, Integer ipMacroProblematica, Integer ipMicroProblematica) {
		this.codPai = codPai;
		this.ipMacroProblematica = ipMacroProblematica;
		this.ipMicroProblematica = ipMicroProblematica;
	}

	public PaiMicroProblematicaPK(PaiMacroProblematicaPK paiMacroProblematichePK, Integer ipMicroProblematica) {
		this(paiMacroProblematichePK.getCodPai(), paiMacroProblematichePK.getIpMacroProblematica(),
				ipMicroProblematica);
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final PaiMicroProblematicaPK other = (PaiMicroProblematicaPK) obj;
		
		if (this.codPai != other.codPai && (this.codPai == null || !this.codPai.equals(other.codPai))) {
			return false;
		}
		if ((this.ipMacroProblematica == null) ? (other.ipMacroProblematica != null) : !this.ipMacroProblematica.equals(other.ipMacroProblematica)) {
			return false;
		}
		if ((this.ipMicroProblematica == null) ? (other.ipMicroProblematica != null) : !this.ipMicroProblematica.equals(other.ipMicroProblematica)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + (this.codPai != null ? this.codPai.hashCode() : 0);
		hash = 79 * hash + (this.ipMacroProblematica != null ? this.ipMacroProblematica.hashCode() : 0);
		hash = 79 * hash + (this.ipMicroProblematica != null ? this.ipMicroProblematica.hashCode() : 0);
		return hash;
	}
}
