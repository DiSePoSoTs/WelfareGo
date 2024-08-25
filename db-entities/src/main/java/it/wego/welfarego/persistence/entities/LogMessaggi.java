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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "LOG_MESSAGGI")
@NamedQueries({ @NamedQuery(name = "LogMessaggi.findAll", query = "SELECT l FROM LogMessaggi l"),
		@NamedQuery(name = "LogMessaggi.findByTsErr", query = "SELECT l FROM LogMessaggi l WHERE l.tsErr = :tsErr"),
		@NamedQuery(name = "LogMessaggi.findByDesErr", query = "SELECT l FROM LogMessaggi l WHERE l.desErr = :desErr"),
		@NamedQuery(name = "LogMessaggi.findByFlgTipoErr", query = "SELECT l FROM LogMessaggi l WHERE l.flgTipoErr = :flgTipoErr"),
		@NamedQuery(name = "LogMessaggi.findByCodErr", query = "SELECT l FROM LogMessaggi l WHERE l.codErr = :codErr") })
public class LogMessaggi implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "TS_ERR", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsErr;

	@Basic(optional = false)
	@Column(name = "DES_ERR", nullable = false, length = 765)
	private String desErr;

	@Basic(optional = false)
	@Column(name = "FLG_TIPO_ERR", nullable = false)
	private char flgTipoErr;

	@Basic(optional = false)
	@Column(name = "COD_ERR", nullable = false, length = 255)
	private String codErr;

	@JoinColumns({ @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	public LogMessaggi() {
	}

	public LogMessaggi(Date tsErr) {
		this.tsErr = tsErr;
	}

	public LogMessaggi(Date tsErr, String desErr, char flgTipoErr, String codErr) {
		this.tsErr = tsErr;
		this.desErr = desErr;
		this.flgTipoErr = flgTipoErr;
		this.codErr = codErr;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tsErr != null ? tsErr.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LogMessaggi)) {
			return false;
		}
		LogMessaggi other = (LogMessaggi) object;
		if ((this.tsErr == null && other.tsErr != null) || (this.tsErr != null && !this.tsErr.equals(other.tsErr))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.LogMessaggi[tsErr=" + tsErr + "]";
	}

}
