/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "MANDATO")
@NamedQueries({ @NamedQuery(name = "Mandato.findAll", query = "SELECT m FROM Mandato m"),
		@NamedQuery(name = "Mandato.findByNumeroMandato", query = "SELECT m FROM Mandato m WHERE m.numeroMandato = :numeroMandato"),
		@NamedQuery(name = "Mandato.findByAnnoDecr", query = "SELECT m FROM Mandato m WHERE m.annoDecr = :annoDecr"),
		@NamedQuery(name = "Mandato.findByCapitoloDecr", query = "SELECT m FROM Mandato m WHERE m.capitoloDecr = :capitoloDecr"),
		@NamedQuery(name = "Mandato.findByNumDecr", query = "SELECT m FROM Mandato m WHERE m.numDecr = :numDecr"),
		@NamedQuery(name = "Mandato.findByDataDecr", query = "SELECT m FROM Mandato m WHERE m.dataDecr = :dataDecr"),
		@NamedQuery(name = "Mandato.findByCognomeBeneficiario", query = "SELECT m FROM Mandato m WHERE m.cognomeBeneficiario = :cognomeBeneficiario"),
		@NamedQuery(name = "Mandato.findByNomeBeneficiario", query = "SELECT m FROM Mandato m WHERE m.nomeBeneficiario = :nomeBeneficiario"),
		@NamedQuery(name = "Mandato.findByCfBeneficiario", query = "SELECT m FROM Mandato m WHERE m.cfBeneficiario = :cfBeneficiario"),
		@NamedQuery(name = "Mandato.findByIndirizzo", query = "SELECT m FROM Mandato m WHERE m.indirizzo = :indirizzo"),
		@NamedQuery(name = "Mandato.findByGruppo", query = "SELECT m FROM Mandato m WHERE m.gruppo = :gruppo"),
		@NamedQuery(name = "Mandato.findByMeseRif", query = "SELECT m FROM Mandato m WHERE m.meseRif = :meseRif"),
		@NamedQuery(name = "Mandato.findByPeriodoDal", query = "SELECT m FROM Mandato m WHERE m.periodoDal = :periodoDal"),
		@NamedQuery(name = "Mandato.findByPeriodoAl", query = "SELECT m FROM Mandato m WHERE m.periodoAl = :periodoAl"),
		@NamedQuery(name = "Mandato.findByCfDelegante", query = "SELECT m FROM Mandato m WHERE m.cfDelegante = :cfDelegante"),
		@NamedQuery(name = "Mandato.findByCognomeDelegante", query = "SELECT m FROM Mandato m WHERE m.cognomeDelegante = :cognomeDelegante"),
		@NamedQuery(name = "Mandato.findByNomeDelegante", query = "SELECT m FROM Mandato m WHERE m.nomeDelegante = :nomeDelegante"),
		@NamedQuery(name = "Mandato.findByModalitaErogazione", query = "SELECT m FROM Mandato m WHERE m.modalitaErogazione = :modalitaErogazione"),
		@NamedQuery(name = "Mandato.findByIban", query = "SELECT m FROM Mandato m WHERE m.iban = :iban"),
		@NamedQuery(name = "Mandato.findByImporto", query = "SELECT m FROM Mandato m WHERE m.importo = :importo"),
		@NamedQuery(name = "Mandato.findByTimbro", query = "SELECT m FROM Mandato m WHERE m.timbro = :timbro"),
		@NamedQuery(name = "Mandato.findByIdMan", query = "SELECT m FROM Mandato m WHERE m.idMan = :idMan"),
		@NamedQuery(name = "Mandato.findByNote", query = "SELECT m FROM Mandato m WHERE m.note = :note") })
public class Mandato implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "NUMERO_MANDATO", nullable = false)
	private BigInteger numeroMandato;

	@Basic(optional = false)
	@Column(name = "ANNO_DECR", nullable = false)
	private int annoDecr;

	@Basic(optional = false)
	@Column(name = "CAPITOLO_DECR", nullable = false)
	private int capitoloDecr;

	@Basic(optional = false)
	@Column(name = "NUM_DECR", nullable = false)
	private int numDecr;

	@Basic(optional = false)
	@Column(name = "DATA_DECR", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataDecr;

	@Basic(optional = false)
	@Column(name = "COGNOME_BENEFICIARIO", nullable = false, length = 765)
	private String cognomeBeneficiario;

	@Basic(optional = false)
	@Column(name = "NOME_BENEFICIARIO", nullable = false, length = 765)
	private String nomeBeneficiario;

	@Basic(optional = false)
	@Column(name = "CF_BENEFICIARIO", nullable = false, length = 16)
	private String cfBeneficiario;

	@Basic(optional = false)
	@Column(name = "INDIRIZZO", nullable = false, length = 765)
	private String indirizzo;

	@Basic(optional = false)
	@Column(name = "GRUPPO", nullable = false, length = 100)
	private String gruppo;

	@Basic(optional = false)
	@Column(name = "MESE_RIF", nullable = false)
	private int meseRif;

	@Column(name = "PERIODO_DAL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoDal;

	@Column(name = "PERIODO_AL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoAl;

	@Basic(optional = false)
	@Column(name = "CF_DELEGANTE", nullable = false, length = 16)
	private String cfDelegante;

	@Column(name = "COGNOME_DELEGANTE", length = 765)
	private String cognomeDelegante;

	@Column(name = "NOME_DELEGANTE", length = 765)
	private String nomeDelegante;

	@Column(name = "MODALITA_EROGAZIONE", length = 765)
	private String modalitaErogazione;

	@Column(name = "IBAN", length = 30)
	private String iban;

	@Basic(optional = false)
	@Column(name = "IMPORTO", nullable = false, precision = 9, scale = 4)
	private BigDecimal importo;

	@Column(name = "TIMBRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timbro;

	@Id
	@GeneratedValue(generator = "mandatoSequence")
	@SequenceGenerator(name = "mandatoSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Basic(optional = false)
	@Column(name = "ID_MAN", nullable = false)
	private Integer idMan;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@JoinColumn(name = "ID_PARAM_FASCIA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamFascia;

	@JoinColumn(name = "ID_PARAM_STATO", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamStato;

	@JoinColumns({ @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_ANA_DELEGANTE", referencedColumnName = "COD_ANA")
	@ManyToOne
	private AnagrafeSoc codAnaDelegante;

	@JoinColumn(name = "COD_ANA_BENEFICIARIO", referencedColumnName = "COD_ANA", nullable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc codAnaBeneficiario;

	@OneToMany(mappedBy = "idMan")
	private List<MandatoDettaglio> mandatoDettaglioList;

	public Mandato() {
	}

	public Mandato(Integer idMan) {
		this.idMan = idMan;
	}

	public Mandato(Integer idMan, BigInteger numeroMandato, int annoDecr, int capitoloDecr, int numDecr, Date dataDecr,
			String cognomeBeneficiario, String nomeBeneficiario, String cfBeneficiario, String indirizzo, String gruppo,
			int meseRif, String cfDelegante, BigDecimal importo) {
		this.idMan = idMan;
		this.numeroMandato = numeroMandato;
		this.annoDecr = annoDecr;
		this.capitoloDecr = capitoloDecr;
		this.numDecr = numDecr;
		this.dataDecr = dataDecr;
		this.cognomeBeneficiario = cognomeBeneficiario;
		this.nomeBeneficiario = nomeBeneficiario;
		this.cfBeneficiario = cfBeneficiario;
		this.indirizzo = indirizzo;
		this.gruppo = gruppo;
		this.meseRif = meseRif;
		this.cfDelegante = cfDelegante;
		this.importo = importo;
	}

	private BigInteger nullToZero(BigInteger bigInteger) {
		return bigInteger == null || bigInteger.equals(BigInteger.ZERO) ? null : bigInteger;
	}

	public BigInteger getNumeroMandato() {
		return nullToZero(numeroMandato);
	}

	public void setNumeroMandato(BigInteger numeroMandato) {
		this.numeroMandato = nullToZero(numeroMandato);
	}

	public boolean hasNumeroMandato() {
		return numeroMandato != null && !numeroMandato.equals(BigInteger.ZERO);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idMan != null ? idMan.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Mandato)) {
			return false;
		}
		Mandato other = (Mandato) object;
		if ((this.idMan == null && other.idMan != null) || (this.idMan != null && !this.idMan.equals(other.idMan))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Mandato[idMan=" + idMan + "]";
	}

	public String getCognomeNomeDelegante() {
		return Strings.nullToEmpty(getCognomeDelegante())
				+ (!Strings.isNullOrEmpty(getCognomeDelegante()) && !Strings.isNullOrEmpty(getNomeDelegante()) ? " "
						: "")
				+ Strings.nullToEmpty(getNomeDelegante());
	}

	public String getCognomeNomeBeneficiario() {
		return Strings.nullToEmpty(getCognomeBeneficiario())
				+ (!Strings.isNullOrEmpty(getCognomeBeneficiario()) && !Strings.isNullOrEmpty(getNomeBeneficiario())
						? " "
						: "")
				+ Strings.nullToEmpty(getNomeBeneficiario());
	}
}
