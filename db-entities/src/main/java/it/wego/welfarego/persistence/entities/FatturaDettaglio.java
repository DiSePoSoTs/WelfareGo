/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "FATTURA_DETTAGLIO")
@NamedQueries({ @NamedQuery(name = "FatturaDettaglio.findAll", query = "SELECT f FROM FatturaDettaglio f"),
		@NamedQuery(name = "FatturaDettaglio.findByAnnoEff", query = "SELECT f FROM FatturaDettaglio f WHERE f.annoEff = :annoEff"),
		@NamedQuery(name = "FatturaDettaglio.findByMeseEff", query = "SELECT f FROM FatturaDettaglio f WHERE f.meseEff = :meseEff"),
		@NamedQuery(name = "FatturaDettaglio.findByQtInputata", query = "SELECT f FROM FatturaDettaglio f WHERE f.qtInputata = :qtInputata"),
		@NamedQuery(name = "FatturaDettaglio.findByImporto", query = "SELECT f FROM FatturaDettaglio f WHERE f.importo = :importo"),
		@NamedQuery(name = "FatturaDettaglio.findByAumento", query = "SELECT f FROM FatturaDettaglio f WHERE f.aumento = :aumento"),
		@NamedQuery(name = "FatturaDettaglio.findByRiduzione", query = "SELECT f FROM FatturaDettaglio f WHERE f.riduzione = :riduzione"),
		@NamedQuery(name = "FatturaDettaglio.findByTimbro", query = "SELECT f FROM FatturaDettaglio f WHERE f.timbro = :timbro"),
		@NamedQuery(name = "FatturaDettaglio.findByIdFattDettaglio", query = "SELECT f FROM FatturaDettaglio f WHERE f.idFattDettaglio = :idFattDettaglio"),
		@NamedQuery(name = "FatturaDettaglio.findByVarStraord", query = "SELECT f FROM FatturaDettaglio f WHERE f.varStraord = :varStraord"),
		@NamedQuery(name = "FatturaDettaglio.findByCausVar", query = "SELECT f FROM FatturaDettaglio f WHERE f.causVar = :causVar") })
public class FatturaDettaglio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "ANNO_EFF", nullable = false)
	private Integer annoEff;

	@Basic(optional = false)
	@Column(name = "MESE_EFF", nullable = false)
	private Integer meseEff;

	public Integer getMeseEff() {
		return meseEff;
	}

	@Basic(optional = false)
	@Column(name = "QT_INPUTATA", nullable = false, precision = 9, scale = 2)
	private BigDecimal qtInputata;

	/**
	 * importo imponibile, senza tasse/iva
	 */
	@Basic(optional = false)
	@Column(name = "IMPORTO", nullable = false, precision = 9, scale = 2)
	private BigDecimal importo;

	public BigDecimal getImporto() {
		return importo;
	}

	@Column(name = "AUMENTO", precision = 9, scale = 2)
	private BigDecimal aumento;

	public BigDecimal getAumento() {
		return aumento;
	}

	@Column(name = "RIDUZIONE", precision = 9, scale = 2)
	private BigDecimal riduzione;

	public BigDecimal getRiduzione() {
		return riduzione;
	}

	@Column(name = "TIMBRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timbro;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "fatturaDettaglioSequence")
	@SequenceGenerator(name = "fatturaDettaglioSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_FATT_DETTAGLIO", nullable = false)
	private Integer idFattDettaglio;

	@Column(name = "VAR_STRAORD", precision = 9, scale = 2)
	private BigDecimal varStraord;

	public BigDecimal getVarStraord() {
		return varStraord;
	}

	@Column(name = "CAUS_VAR", length = 60)
	private String causVar;

	public List<PaiInterventoMese> getPaiInterventoMeseList() {
		return paiInterventoMeseList;
	}

	@OneToMany(mappedBy = "idFattDettaglio")
	private List<PaiInterventoMese> paiInterventoMeseList;

	@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false)
	@ManyToOne(optional = false)
	private TipologiaIntervento codTipint;

	@JoinColumn(name = "ID_PARAM_UNITA_MISURA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamUnitaMisura;

	public Fattura getIdFatt() {
		return idFatt;
	}

	@JoinColumn(name = "ID_FATT", referencedColumnName = "ID_FATT", nullable = false)
	@ManyToOne(optional = false)
	private Fattura idFatt;

	public FatturaDettaglio() {
	}

	public FatturaDettaglio(Integer idFattDettaglio) {
		this.idFattDettaglio = idFattDettaglio;
	}

	public FatturaDettaglio(Integer idFattDettaglio, Integer annoEff, Integer meseEff, BigDecimal qtInputata,
			BigDecimal importo) {
		this.idFattDettaglio = idFattDettaglio;
		this.annoEff = annoEff;
		this.meseEff = meseEff;
		this.qtInputata = qtInputata;
		this.importo = importo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idFattDettaglio != null ? idFattDettaglio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof FatturaDettaglio)) {
			return false;
		}
		FatturaDettaglio other = (FatturaDettaglio) object;
		if ((this.idFattDettaglio == null && other.idFattDettaglio != null)
				|| (this.idFattDettaglio != null && !this.idFattDettaglio.equals(other.idFattDettaglio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.FatturaDettaglio[idFattDettaglio=" + idFattDettaglio + "]";
	}

	public Fattura getFattura() {
		return getIdFatt();
	}

	public @Nullable PaiIntervento getPaiIntervento() {
		return Iterables.getFirst(Sets.newHashSet(
				Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, PaiIntervento>() {
					public PaiIntervento apply(PaiInterventoMese paiInterventoMese) {
						return paiInterventoMese.getPaiIntervento();
					}
				})), null);
	}

	public @Nullable ParametriIndata getIdParamIva() {
		PaiIntervento paiIntervento = getPaiIntervento();
		return paiIntervento == null ? getFattura().getIdParamIva()
				: paiIntervento.getTipologiaIntervento().getIpAliquotaIva(); // TODO iva default?
	}

	public BigDecimal getImportoSenzaIva() {
		return getImporto();
	}

	public BigDecimal getIvaTotale() {
		return getImportoSenzaIva().multiply(getIdParamIva().getDecimalPercentageParamAsDecimal());
	}

	public BigDecimal getImportoConIva() {
		return getImportoSenzaIva().add(getIvaTotale());
	}

	public BigDecimal getTotaleVariazioniSenzaIva() {
		return MoreObjects.firstNonNull(getAumento(), BigDecimal.ZERO)
				.subtract(MoreObjects.firstNonNull(getRiduzione(), BigDecimal.ZERO))
				.add(MoreObjects.firstNonNull(getVarStraord(), BigDecimal.ZERO));
	}

	public BigDecimal getTotaleVariazioniConIva() {
		return getTotaleVariazioniSenzaIva()
				.multiply(BigDecimal.ONE.add(getIdParamIva().getDecimalPercentageParamAsDecimal()));
	}

	public BigDecimal getTotaleVariazioniNegativeSenzaIva() {
		BigDecimal res = MoreObjects.firstNonNull(getRiduzione(), BigDecimal.ZERO);
		if (getVarStraord() != null && getVarStraord().compareTo(BigDecimal.ZERO) < 0) {
			res = res.subtract(getVarStraord());
		}
		return res;
	}

	public BigDecimal getTotaleVariazioniPositiveSenzaIva() {
		BigDecimal res = MoreObjects.firstNonNull(getAumento(), BigDecimal.ZERO);
		if (getVarStraord() != null && getVarStraord().compareTo(BigDecimal.ZERO) > 0) {
			res = res.add(getVarStraord());
		}
		return res;
	}

	public BigDecimal getTotaleVariazioniPositiveConIva() {
		return getTotaleVariazioniPositiveSenzaIva()
				.multiply(BigDecimal.ONE.add(getIdParamIva().getDecimalPercentageParamAsDecimal()));
	}

	public BigDecimal getTotaleVariazioniNegativeConIva() {
		return getTotaleVariazioniNegativeSenzaIva()
				.multiply(BigDecimal.ONE.add(getIdParamIva().getDecimalPercentageParamAsDecimal()));
	}
}
