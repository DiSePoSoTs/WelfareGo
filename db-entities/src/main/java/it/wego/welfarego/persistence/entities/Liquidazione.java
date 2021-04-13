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
@Table(name = "LIQUIDAZIONE")
@NamedQueries({ @NamedQuery(name = "Liquidazione.findAll", query = "SELECT l FROM Liquidazione l"),
		@NamedQuery(name = "Liquidazione.findByIdProg", query = "SELECT l FROM Liquidazione l WHERE l.idProg = :idProg"),
		@NamedQuery(name = "Liquidazione.findByCodTipint", query = "SELECT l FROM Liquidazione l WHERE l.codTipint = :codTipint"),
		@NamedQuery(name = "Liquidazione.findByCntTipint", query = "SELECT l FROM Liquidazione l WHERE l.cntTipint = :cntTipint"),
		@NamedQuery(name = "Liquidazione.findByMeseRif", query = "SELECT l FROM Liquidazione l WHERE l.meseRif = :meseRif"),
		@NamedQuery(name = "Liquidazione.findByPeriodoDal", query = "SELECT l FROM Liquidazione l WHERE l.periodoDal = :periodoDal"),
		@NamedQuery(name = "Liquidazione.findByPeriodoAl", query = "SELECT l FROM Liquidazione l WHERE l.periodoAl = :periodoAl"),
		@NamedQuery(name = "Liquidazione.findByNote", query = "SELECT l FROM Liquidazione l WHERE l.note = :note"),
		@NamedQuery(name = "Liquidazione.findByTimbro", query = "SELECT l FROM Liquidazione l WHERE l.timbro = :timbro") })
public class Liquidazione implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID_PROG", nullable = false)
	private Integer idProg;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private int cntTipint;

	@Column(name = "MESE_RIF")
	private Integer meseRif;

	@Column(name = "PERIODO_DAL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoDal;

	@Column(name = "PERIODO_AL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoAl;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@Column(name = "TIMBRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timbro;

	public Liquidazione() {
	}

	public Liquidazione(Integer idProg) {
		this.idProg = idProg;
	}

	public Liquidazione(Integer idProg, String codTipint, int cntTipint) {
		this.idProg = idProg;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProg != null ? idProg.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Liquidazione)) {
			return false;
		}
		Liquidazione other = (Liquidazione) object;
		if ((this.idProg == null && other.idProg != null)
				|| (this.idProg != null && !this.idProg.equals(other.idProg))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Liquidazione[idProg=" + idProg + "]";
	}

}
