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
@Table(name = "APPUNTAMENTO")
public class Appuntamento implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "appuntamentoSequence")
	@SequenceGenerator(name = "appuntamentoSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_APP", nullable = false)
	private Integer idApp;
	@Basic(optional = false)
	@Column(name = "TS_FINE_APP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsFineApp;
	@Basic(optional = false)
	@Column(name = "TS_INI_APP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsIniApp;
	@Column(name = "NOTE", length = 3000)
	private String note;
	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUte; // utente servizio
	@JoinColumn(name = "COD_AS", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti utenti; // assistente sociale
	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false)
	@ManyToOne(optional = false)
	private Pai codPai;
	@Basic(optional = false)
	@Column(name = "COD_AS", nullable = false, insertable = false, updatable = false)
	private int codAs;

	public Appuntamento() {
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 31 * hash + (this.idApp != null ? this.idApp.hashCode() : 0);
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
		final Appuntamento other = (Appuntamento) obj;
		if (this.idApp != other.idApp && (this.idApp == null || !this.idApp.equals(other.idApp))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Appuntamento[idApp=" + idApp + "]";
	}
}
