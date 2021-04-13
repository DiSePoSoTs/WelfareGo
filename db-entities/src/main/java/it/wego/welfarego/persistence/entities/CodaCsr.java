/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author piergiorgio
 */
@Entity
@Getter
@Setter
@Table(name = "CODA_CSR")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CodaCsr.findAll", query = "SELECT c FROM CodaCsr c"),
		@NamedQuery(name = "CodaCsr.findByIdCoda", query = "SELECT c FROM CodaCsr c WHERE c.idCoda = :idCoda"),
		@NamedQuery(name = "CodaCsr.findByCodAna", query = "SELECT c FROM CodaCsr c WHERE c.codAna = :codAna"),
		@NamedQuery(name = "CodaCsr.findByCodPai", query = "SELECT c FROM CodaCsr c WHERE c.codPai = :codPai"),
		@NamedQuery(name = "CodaCsr.findByCodTipint", query = "SELECT c FROM CodaCsr c WHERE c.codTipint = :codTipint"),
		@NamedQuery(name = "CodaCsr.findByCntTipint", query = "SELECT c FROM CodaCsr c WHERE c.cntTipint = :cntTipint"),
		@NamedQuery(name = "CodaCsr.findByAzione", query = "SELECT c FROM CodaCsr c WHERE c.azione = :azione"),
		@NamedQuery(name = "CodaCsr.findByDtInscoda", query = "SELECT c FROM CodaCsr c WHERE c.dtInscoda = :dtInscoda"),
		@NamedQuery(name = "CodaCsr.findByDtCallcsr", query = "SELECT c FROM CodaCsr c WHERE c.dtCallcsr = :dtCallcsr"),
		@NamedQuery(name = "CodaCsr.findByDtInscsr", query = "SELECT c FROM CodaCsr c WHERE c.dtInscsr = :dtInscsr"),
		@NamedQuery(name = "CodaCsr.findByTestoErrore", query = "SELECT c FROM CodaCsr c WHERE c.testoErrore = :testoErrore") })
public class CodaCsr implements Serializable {

	public static final String SINCRONIZZAZIONE_INSERISCI_CARTELLA = "INSERISCI_CARTELLA";
	public static final String SINCRONIZZAZIONE_MODIFICA_CARTELLA = "MODIFICA_CARTELLA";
	public static final String SINCRONIZZAZIONE_INSERISCI_INTERVENTO = "INSERISCI_INTERVENTO";
	public static final String SINCRONIZZAZIONE_MODIFICA_INTERVENTO = "MODIFICA_INTERVENTO";
	public static final String SINCRONIZZAZIONE_CHIUDI_CARTELLA = "CHIUDI_CARTELLA";
	public static final String SINCRONIZZAZIONE_RIATTIVA_CARTELLA = "RIATTIVA_CARTELLA";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "codaCsrSequence")
	@SequenceGenerator(name = "codaCsrSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_CODA")
	private BigDecimal idCoda;

	@Column(name = "COD_ANA")
	private BigInteger codAna;

	@Column(name = "COD_PAI")
	private BigInteger codPai;

	@Column(name = "COD_TIPINT")
	private String codTipint;

	@Column(name = "CNT_TIPINT")
	private BigInteger cntTipint;

	@Column(name = "AZIONE")
	private String azione;

	@Column(name = "DT_INSCODA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInscoda;

	@Column(name = "DT_CALLCSR")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCallcsr;

	@Column(name = "DT_INSCSR")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInscsr;

	@Column(name = "TESTO_ERRORE")
	private String testoErrore;

	@Column(name = "NUM_TENTATIVI")
	private int numeroTentativi;

	@Column(name = "DT_AVVIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAvvio;

	public CodaCsr() {
	}

	public CodaCsr(BigDecimal idCoda) {
		this.idCoda = idCoda;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCoda != null ? idCoda.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CodaCsr)) {
			return false;
		}
		CodaCsr other = (CodaCsr) object;
		if ((this.idCoda == null && other.idCoda != null)
				|| (this.idCoda != null && !this.idCoda.equals(other.idCoda))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.entity.CodaCsr[ idCoda=" + idCoda + " ]";
	}

}
