/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

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


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "MANDATO_DETTAGLIO")
@NamedQueries({ @NamedQuery(name = "MandatoDettaglio.findAll", query = "SELECT m FROM MandatoDettaglio m"),
		@NamedQuery(name = "MandatoDettaglio.findByNumeroMandato", query = "SELECT m FROM MandatoDettaglio m WHERE m.numeroMandato = :numeroMandato"),
		@NamedQuery(name = "MandatoDettaglio.findByMeseEff", query = "SELECT m FROM MandatoDettaglio m WHERE m.meseEff = :meseEff"),
		@NamedQuery(name = "MandatoDettaglio.findByQtAssegnata", query = "SELECT m FROM MandatoDettaglio m WHERE m.qtAssegnata = :qtAssegnata"),
		@NamedQuery(name = "MandatoDettaglio.findByCostoUnitario", query = "SELECT m FROM MandatoDettaglio m WHERE m.costoUnitario = :costoUnitario"),
		@NamedQuery(name = "MandatoDettaglio.findByCostoTotale", query = "SELECT m FROM MandatoDettaglio m WHERE m.costoTotale = :costoTotale"),
		@NamedQuery(name = "MandatoDettaglio.findByAumento", query = "SELECT m FROM MandatoDettaglio m WHERE m.aumento = :aumento"),
		@NamedQuery(name = "MandatoDettaglio.findByRiduzione", query = "SELECT m FROM MandatoDettaglio m WHERE m.riduzione = :riduzione"),
		@NamedQuery(name = "MandatoDettaglio.findByTimbro", query = "SELECT m FROM MandatoDettaglio m WHERE m.timbro = :timbro"),
		@NamedQuery(name = "MandatoDettaglio.findByIdManDettaglio", query = "SELECT m FROM MandatoDettaglio m WHERE m.idManDettaglio = :idManDettaglio") })
public class MandatoDettaglio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "NUMERO_MANDATO", nullable = false)
	private BigInteger numeroMandato;

	@Basic(optional = false)
	@Column(name = "MESE_EFF", nullable = false)
	private int meseEff;

	public int getMeseEff() {
		return meseEff;
	}

	@Basic(optional = false)
	@Column(name = "QT_ASSEGNATA", nullable = false, precision = 9, scale = 4)
	private BigDecimal qtAssegnata;

	@Basic(optional = false)
	@Column(name = "COSTO_UNITARIO", nullable = false, precision = 9, scale = 4)
	private BigDecimal costoUnitario;

	@Basic(optional = false)
	@Column(name = "COSTO_TOTALE", nullable = false, precision = 9, scale = 4)
	private BigDecimal costoTotale;

	@Column(name = "AUMENTO", precision = 9, scale = 4)
	private BigDecimal aumento;

	@Column(name = "RIDUZIONE", precision = 9, scale = 4)
	private BigDecimal riduzione;

	@Column(name = "TIMBRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timbro;

	public void setMeseEff(int meseEff) {
		this.meseEff = meseEff;
	}

	public void setQtAssegnata(BigDecimal qtAssegnata) {
		this.qtAssegnata = qtAssegnata;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public void setCostoTotale(BigDecimal costoTotale) {
		this.costoTotale = costoTotale;
	}

	public void setTimbro(Date timbro) {
		this.timbro = timbro;
	}

	public void setPaiInterventoMeseList(List<PaiInterventoMese> paiInterventoMeseList) {
		this.paiInterventoMeseList = paiInterventoMeseList;
	}

	public void setIdParamUnitaMisura(ParametriIndata idParamUnitaMisura) {
		this.idParamUnitaMisura = idParamUnitaMisura;
	}

	public void setPaiIntervento(PaiIntervento paiIntervento) {
		this.paiIntervento = paiIntervento;
	}

	public void setIdMan(Mandato idMan) {
		this.idMan = idMan;
	}

	@Id
	@GeneratedValue(generator = "mandatoDettaglioSequence")
	@SequenceGenerator(name = "mandatoDettaglioSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Basic(optional = false)
	@Column(name = "ID_MAN_DETTAGLIO", nullable = false)
	private Integer idManDettaglio;

	public void setAumento(BigDecimal aumento) {
		this.aumento = aumento;
	}

	public void setRiduzione(BigDecimal riduzione) {
		this.riduzione = riduzione;
	}

	@OneToMany(mappedBy = "idManDettaglio")
	private List<PaiInterventoMese> paiInterventoMeseList;

	public List<PaiInterventoMese> getPaiInterventoMeseList() {
		return paiInterventoMeseList;
	}

	@JoinColumn(name = "ID_PARAM_UNITA_MISURA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamUnitaMisura;

	@JoinColumns({ @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "ID_MAN", referencedColumnName = "ID_MAN")
	@ManyToOne
	private Mandato idMan;

	public MandatoDettaglio() {
	}

	public MandatoDettaglio(Integer idManDettaglio) {
		this.idManDettaglio = idManDettaglio;
	}

	public MandatoDettaglio(Integer idManDettaglio, BigInteger numeroMandato, int meseEff, BigDecimal qtAssegnata,
			BigDecimal costoUnitario, BigDecimal costoTotale) {
		this.idManDettaglio = idManDettaglio;
		this.numeroMandato = numeroMandato;
		this.meseEff = meseEff;
		this.qtAssegnata = qtAssegnata;
		this.costoUnitario = costoUnitario;
		this.costoTotale = costoTotale;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idManDettaglio != null ? idManDettaglio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MandatoDettaglio)) {
			return false;
		}
		MandatoDettaglio other = (MandatoDettaglio) object;
		if ((this.idManDettaglio == null && other.idManDettaglio != null)
				|| (this.idManDettaglio != null && !this.idManDettaglio.equals(other.idManDettaglio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.MandatoDettaglio[idManDettaglio=" + idManDettaglio + "]";
	}

	public BigDecimal getQtAssegnata() {
		return qtAssegnata;
	}

	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	public BigDecimal getCostoTotale() {
		return costoTotale;
	}

	public BigDecimal getAumento() {
		return aumento;
	}

	public BigDecimal getRiduzione() {
		return riduzione;
	}

	public Date getTimbro() {
		return timbro;
	}

	public Integer getIdManDettaglio() {
		return idManDettaglio;
	}

	public ParametriIndata getIdParamUnitaMisura() {
		return idParamUnitaMisura;
	}

	public PaiIntervento getPaiIntervento() {
		return paiIntervento;
	}

	public Mandato getIdMan() {
		return idMan;
	}
}
