/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "CONTATTO")
@NamedQueries({ @NamedQuery(name = "Contatto.findAll", query = "SELECT c FROM Contatto c"),
		@NamedQuery(name = "Contatto.findByTsCont", query = "SELECT c FROM Contatto c WHERE c.tsCont = :tsCont"),
		@NamedQuery(name = "Contatto.findByNote", query = "SELECT c FROM Contatto c WHERE c.note = :note") })
public class Contatto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "TS_CONT", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsCont;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUte;

	@JoinColumn(name = "ID_PARAM_MOTIV", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamMotiv;

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc codAna;

	public Contatto() {
	}

	public Contatto(Date tsCont) {
		this.tsCont = tsCont;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tsCont != null ? tsCont.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Contatto)) {
			return false;
		}
		Contatto other = (Contatto) object;
		if ((this.tsCont == null && other.tsCont != null)
				|| (this.tsCont != null && !this.tsCont.equals(other.tsCont))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Contatto[tsCont=" + tsCont + "]";
	}

}
