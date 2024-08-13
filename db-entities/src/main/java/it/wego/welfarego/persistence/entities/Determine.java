/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "DETERMINE")
@NamedQueries({ @NamedQuery(name = "Determine.findAll", query = "SELECT d FROM Determine d"),
		@NamedQuery(name = "Determine.findByIdDetermina", query = "SELECT d FROM Determine d WHERE d.idDetermina = :idDetermina"),
		@NamedQuery(name = "Determine.findByTsDetermina", query = "SELECT d FROM Determine d WHERE d.tsDetermina = :tsDetermina"),
		@NamedQuery(name = "Determine.findByNumDetermina", query = "SELECT d FROM Determine d WHERE d.numDetermina = :numDetermina") })
public class Determine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "determineSequence")
	@SequenceGenerator(name = "determineSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_DETERMINA", nullable = false)
	private Integer idDetermina;

	@Basic(optional = false)
	@Column(name = "TS_DETERMINA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsDetermina;

	@Column(name = "NUM_DETERMINA", length = 10)
	private String numDetermina;

	@OneToMany(mappedBy = "idDetermina")
	private List<PaiEvento> paiEventoList;

	public Determine() {
	}

	public Determine(Integer idDetermina) {
		this.idDetermina = idDetermina;
	}

	public Date getTsDetermina() {
		return tsDetermina;
	}

	public String getNumDetermina() {
		return numDetermina;
	}

	public Determine(Integer idDetermina, Date tsDetermina) {
		this.idDetermina = idDetermina;
		this.tsDetermina = tsDetermina;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDetermina != null ? idDetermina.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Determine)) {
			return false;
		}
		Determine other = (Determine) object;
		if ((this.idDetermina == null && other.idDetermina != null)
				|| (this.idDetermina != null && !this.idDetermina.equals(other.idDetermina))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Determine[idDetermina=" + idDetermina + "]";
	}

}
