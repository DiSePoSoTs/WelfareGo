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
@Table(name = "LOG_ANAGRAFE")
@NamedQueries({ @NamedQuery(name = "LogAnagrafe.findAll", query = "SELECT l FROM LogAnagrafe l"),
		@NamedQuery(name = "LogAnagrafe.findByTsEve", query = "SELECT l FROM LogAnagrafe l WHERE l.tsEve = :tsEve") })
public class LogAnagrafe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "TS_EVE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsEve;

	public Date getTsEve() {
		return tsEve;
	}

	public void setTsEve(Date tsEve) {
		this.tsEve = tsEve;
	}

	public Utenti getCodUte() {
		return codUte;
	}

	public void setCodUte(Utenti codUte) {
		this.codUte = codUte;
	}

	public Pai getCodPai() {
		return codPai;
	}

	public void setCodPai(Pai codPai) {
		this.codPai = codPai;
	}

	public AnagrafeSoc getCodAna() {
		return codAna;
	}

	public void setCodAna(AnagrafeSoc codAna) {
		this.codAna = codAna;
	}

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUte;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI")
	@ManyToOne
	private Pai codPai;

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc codAna;

	public LogAnagrafe() {
	}

	public LogAnagrafe(Date tsEve) {
		this.tsEve = tsEve;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tsEve != null ? tsEve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LogAnagrafe)) {
			return false;
		}
		LogAnagrafe other = (LogAnagrafe) object;
		if ((this.tsEve == null && other.tsEve != null) || (this.tsEve != null && !this.tsEve.equals(other.tsEve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.LogAnagrafe[tsEve=" + tsEve + "]";
	}

}
