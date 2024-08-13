/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.collect.Sets;
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
public class AnagrafeFamPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_ANA", nullable = false)
	private int codAna;

	@Basic(optional = false)
	@Column(name = "COD_ANA_FAM", nullable = false)
	private int codAnaFam;

	public int getCodAna() {
		return codAna;
	}

	public void setCodAna(int codAna) {
		this.codAna = codAna;
	}

	public int getCodAnaFam() {
		return codAnaFam;
	}

	public void setCodAnaFam(int codAnaFam) {
		this.codAnaFam = codAnaFam;
	}

	public AnagrafeFamPK() {
	}

	/**
	 *
	 * @param codAna    source anag
	 * @param codAnaFam target anag
	 */
	public AnagrafeFamPK(int codAna, int codAnaFam) {
		this.codAna = codAna;
		this.codAnaFam = codAnaFam;
	}

	@Override
	public int hashCode() {
		return codAna + codAnaFam;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AnagrafeFamPK)) {
			return false;
		}
		AnagrafeFamPK other = (AnagrafeFamPK) object;
		return Sets.newHashSet(codAna, codAnaFam, other.codAna, other.codAnaFam).size() <= 2; // assume codAna !=
																								// codAnaFam
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.AnagrafeFamPK[codAna=" + codAna + ", codAnaFam=" + codAnaFam
				+ "]";
	}
}
