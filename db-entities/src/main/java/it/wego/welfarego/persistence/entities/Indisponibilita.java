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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "INDISPONIBILITA")
public class Indisponibilita implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "indisponibilitaSequence")
	@SequenceGenerator(name = "indisponibilitaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_IND", nullable = false)
	private Integer idInd;

	public Integer getIdInd() {
		return idInd;
	}

	public Date getTsFineApp() {
		return tsFineApp;
	}

	public int getCodAs() {
		return codAs;
	}

	@Basic(optional = false)
	@Column(name = "TS_FINE_APP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsFineApp;

	@Basic(optional = false)
	@Column(name = "TS_INI_APP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsIniApp;

	public Date getTsIniApp() {
		return tsIniApp;
	}

	public void setTsIniApp(Date tsIniApp) {
		this.tsIniApp = tsIniApp;
	}

	@JoinColumn(name = "COD_AS", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti utenti;

	@Basic(optional = false)
	@Column(name = "COD_AS", nullable = false, insertable = false, updatable = false)
	private int codAs;

	public Indisponibilita() {
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.idInd != null ? this.idInd.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Indisponibilita other = (Indisponibilita) obj;
		if (this.idInd != other.idInd && (this.idInd == null || !this.idInd.equals(other.idInd))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Indisponibilita[idInd=" + idInd + "]";
	}
}
