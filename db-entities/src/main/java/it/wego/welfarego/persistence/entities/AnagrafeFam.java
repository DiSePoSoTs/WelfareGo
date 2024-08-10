/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "ANAGRAFE_FAM")
@NamedQueries({ @NamedQuery(name = "AnagrafeFam.findAll", query = "SELECT a FROM AnagrafeFam a"),
		@NamedQuery(name = "AnagrafeFam.findByCodAna", query = "SELECT a FROM AnagrafeFam a WHERE a.anagrafeFamPK.codAna = :codAna"),
		@NamedQuery(name = "AnagrafeFam.findByCodAnaFam", query = "SELECT a FROM AnagrafeFam a WHERE a.anagrafeFamPK.codAnaFam = :codAnaFam") })
public class AnagrafeFam implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected AnagrafeFamPK anagrafeFamPK;
	
	@JoinColumn(name = "COD_QUAL", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata codQual;
	
	@JoinColumn(name = "COD_ANA_FAM", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSocTarget;
	
	public AnagrafeSoc getAnagrafeSocTarget() {
		return anagrafeSocTarget;
	}

	public AnagrafeSoc getAnagrafeSocSource() {
		return anagrafeSocSource;
	}

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSocSource;

	public AnagrafeFam() {
	}

	public AnagrafeFam(AnagrafeFamPK anagrafeFamPK) {
		this.anagrafeFamPK = anagrafeFamPK;
	}

	public AnagrafeFam(int source, int target) {
		this.anagrafeFamPK = new AnagrafeFamPK(source, target);
		Preconditions.checkArgument(source != target,
				"non e' possibile avere relazioni autoreferenziali tra angrafeSoc");
	}

	public AnagrafeFam(int source, int target, ParametriIndata codQual) {
		this(source, target);
		Preconditions.checkNotNull(codQual);
		this.codQual = codQual;
	}

	public AnagrafeFam(AnagrafeSoc source, AnagrafeSoc target) {
		this(source.getCodAna(), target.getCodAna());
		this.anagrafeSocSource = source;
		this.anagrafeSocTarget = target;
	}

	public AnagrafeFam(AnagrafeSoc source, AnagrafeSoc target, ParametriIndata codQual) {
		this(source, target);
		Preconditions.checkNotNull(codQual);
		this.codQual = codQual;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (anagrafeFamPK != null ? anagrafeFamPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AnagrafeFam)) {
			return false;
		}
		AnagrafeFam other = (AnagrafeFam) object;
		if ((this.anagrafeFamPK == null && other.anagrafeFamPK != null)
				|| (this.anagrafeFamPK != null && !this.anagrafeFamPK.equals(other.anagrafeFamPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.AnagrafeFam[anagrafeFamPK=" + anagrafeFamPK + "]";
	}
}
