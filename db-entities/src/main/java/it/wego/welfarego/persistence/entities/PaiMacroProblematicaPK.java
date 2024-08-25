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
 * @author aleph
 */
@Embeddable
public class PaiMacroProblematicaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	public Integer getCodPai() {
		return codPai;
	}

	@Column(name = "IP_MACRO_PROBLEMATICA", nullable = false)
	private Integer ipMacroProblematica;

	public Integer getIpMacroProblematica() {
		return ipMacroProblematica;
	}

	public PaiMacroProblematicaPK() {
	}

	public PaiMacroProblematicaPK(Integer codPai, Integer ipProblematica) {
		this.codPai = codPai;
		this.ipMacroProblematica = ipProblematica;
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
		
		final PaiMacroProblematicaPK other = (PaiMacroProblematicaPK) obj;
		
		if (this.codPai != other.codPai && (this.codPai == null || !this.codPai.equals(other.codPai))) {
			return false;
		}
		if ((this.ipMacroProblematica == null) ? (other.ipMacroProblematica != null) : !this.ipMacroProblematica.equals(other.ipMacroProblematica)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + (this.codPai != null ? this.codPai.hashCode() : 0);
		hash = 79 * hash + (this.ipMacroProblematica != null ? this.ipMacroProblematica.hashCode() : 0);
		
		return hash;
	}
}
