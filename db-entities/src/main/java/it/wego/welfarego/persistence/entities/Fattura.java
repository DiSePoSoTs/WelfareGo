/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "FATTURA")
@NamedQueries({ @NamedQuery(name = "Fattura.findAll", query = "SELECT f FROM Fattura f"),
		@NamedQuery(name = "Fattura.findByAnno", query = "SELECT f FROM Fattura f WHERE f.anno = :anno"),
		@NamedQuery(name = "Fattura.findByNumFatt", query = "SELECT f FROM Fattura f WHERE f.numFatt = :numFatt"),
		@NamedQuery(name = "Fattura.findByCodFisc", query = "SELECT f FROM Fattura f WHERE f.codFisc = :codFisc"),
		@NamedQuery(name = "Fattura.findByNome", query = "SELECT f FROM Fattura f WHERE f.nome = :nome"),
		@NamedQuery(name = "Fattura.findByCognome", query = "SELECT f FROM Fattura f WHERE f.cognome = :cognome"),
		@NamedQuery(name = "Fattura.findByGruppo", query = "SELECT f FROM Fattura f WHERE f.gruppo = :gruppo"),
		@NamedQuery(name = "Fattura.findByMeseRif", query = "SELECT f FROM Fattura f WHERE f.meseRif = :meseRif"),
		@NamedQuery(name = "Fattura.findByPeriodoDal", query = "SELECT f FROM Fattura f WHERE f.periodoDal = :periodoDal"),
		@NamedQuery(name = "Fattura.findByPeriodoAl", query = "SELECT f FROM Fattura f WHERE f.periodoAl = :periodoAl"),
		@NamedQuery(name = "Fattura.findByScadenza", query = "SELECT f FROM Fattura f WHERE f.scadenza = :scadenza"),
		@NamedQuery(name = "Fattura.findByCfObbligato1", query = "SELECT f FROM Fattura f WHERE f.cfObbligato1 = :cfObbligato1"),
		@NamedQuery(name = "Fattura.findByNomeObbl1", query = "SELECT f FROM Fattura f WHERE f.nomeObbl1 = :nomeObbl1"),
		@NamedQuery(name = "Fattura.findByCognomeObbl1", query = "SELECT f FROM Fattura f WHERE f.cognomeObbl1 = :cognomeObbl1"),
		@NamedQuery(name = "Fattura.findByIndirizzoObbl1", query = "SELECT f FROM Fattura f WHERE f.indirizzoObbl1 = :indirizzoObbl1"),
		@NamedQuery(name = "Fattura.findByQuotaObbligato1", query = "SELECT f FROM Fattura f WHERE f.quotaObbligato1 = :quotaObbligato1"),
		@NamedQuery(name = "Fattura.findByCfObbligato2", query = "SELECT f FROM Fattura f WHERE f.cfObbligato2 = :cfObbligato2"),
		@NamedQuery(name = "Fattura.findByNomeObbl2", query = "SELECT f FROM Fattura f WHERE f.nomeObbl2 = :nomeObbl2"),
		@NamedQuery(name = "Fattura.findByCognomeObbl2", query = "SELECT f FROM Fattura f WHERE f.cognomeObbl2 = :cognomeObbl2"),
		@NamedQuery(name = "Fattura.findByIndirizzoObbl2", query = "SELECT f FROM Fattura f WHERE f.indirizzoObbl2 = :indirizzoObbl2"),
		@NamedQuery(name = "Fattura.findByQuotaObbligato2", query = "SELECT f FROM Fattura f WHERE f.quotaObbligato2 = :quotaObbligato2"),
		@NamedQuery(name = "Fattura.findByCfObbligato3", query = "SELECT f FROM Fattura f WHERE f.cfObbligato3 = :cfObbligato3"),
		@NamedQuery(name = "Fattura.findByNomeObbl3", query = "SELECT f FROM Fattura f WHERE f.nomeObbl3 = :nomeObbl3"),
		@NamedQuery(name = "Fattura.findByCognomeObbl3", query = "SELECT f FROM Fattura f WHERE f.cognomeObbl3 = :cognomeObbl3"),
		@NamedQuery(name = "Fattura.findByIndirizzoObbl3", query = "SELECT f FROM Fattura f WHERE f.indirizzoObbl3 = :indirizzoObbl3"),
		@NamedQuery(name = "Fattura.findByQuotaObbligato3", query = "SELECT f FROM Fattura f WHERE f.quotaObbligato3 = :quotaObbligato3"),
		@NamedQuery(name = "Fattura.findByImportoTotale", query = "SELECT f FROM Fattura f WHERE f.importoTotale = :importoTotale"),
		@NamedQuery(name = "Fattura.findByBollo", query = "SELECT f FROM Fattura f WHERE f.bollo = :bollo"),
		@NamedQuery(name = "Fattura.findByNote", query = "SELECT f FROM Fattura f WHERE f.note = :note"),
		@NamedQuery(name = "Fattura.findByTimbro", query = "SELECT f FROM Fattura f WHERE f.timbro = :timbro"),
		@NamedQuery(name = "Fattura.findByCausale", query = "SELECT f FROM Fattura f WHERE f.causale = :causale"),
		@NamedQuery(name = "Fattura.findByRiscosso", query = "SELECT f FROM Fattura f WHERE f.riscosso = :riscosso"),
		@NamedQuery(name = "Fattura.findByIdFatt", query = "SELECT f FROM Fattura f WHERE f.idFatt = :idFatt"),
		@NamedQuery(name = "Fattura.findByImpIva", query = "SELECT f FROM Fattura f WHERE f.impIva = :impIva"),
		@NamedQuery(name = "Fattura.findByContributo", query = "SELECT f FROM Fattura f WHERE f.contributo = :contributo") })
public class Fattura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "ANNO", nullable = false, length = 4)
	private String anno;

	public String getAnno() {
		return anno;
	}

	@Basic(optional = false)
	@Column(name = "NUM_FATT", nullable = false)
	private int numFatt;

	public int getNumFatt() {
		return numFatt;
	}

	@Basic(optional = false)
	@Column(name = "COD_FISC", nullable = false, length = 16)
	private String codFisc;

	public String getCodFisc() {
		return codFisc;
	}

	@Basic(optional = false)
	@Column(name = "NOME", nullable = false, length = 765)
	private String nome;

	@Basic(optional = false)
	@Column(name = "COGNOME", nullable = false, length = 765)
	private String cognome;

	@Column(name = "GRUPPO", length = 100)
	private String gruppo;

	@Column(name = "MESE_RIF")
	private Integer meseRif;

	public Integer getMeseRif() {
		return meseRif;
	}

	@Column(name = "PERIODO_DAL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoDal;

	@Column(name = "PERIODO_AL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date periodoAl;

	public Date getPeriodoDal() {
		return periodoDal;
	}

	public void setPeriodoDal(Date periodoDal) {
		this.periodoDal = periodoDal;
	}

	public Date getPeriodoAl() {
		return periodoAl;
	}

	public void setPeriodoAl(Date periodoAl) {
		this.periodoAl = periodoAl;
	}

	@Basic(optional = false)
	@Column(name = "SCADENZA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date scadenza;

	public Date getScadenza() {
		return scadenza;
	}

	@Column(name = "CF_OBBLIGATO1", length = 16)
	private String cfObbligato1;

	public String getCfObbligato1() {
		return cfObbligato1;
	}

	@Column(name = "NOME_OBBL1", length = 765)
	private String nomeObbl1;

	@Column(name = "COGNOME_OBBL1", length = 765)
	private String cognomeObbl1;

	@Column(name = "INDIRIZZO_OBBL1", length = 765)
	private String indirizzoObbl1;

	@Column(name = "QUOTA_OBBLIGATO1", precision = 9, scale = 4)
	private BigDecimal quotaObbligato1;

	public BigDecimal getQuotaObbligato1() {
		return quotaObbligato1;
	}

	public String getCfObbligato2() {
		return cfObbligato2;
	}

	public BigDecimal getQuotaObbligato2() {
		return quotaObbligato2;
	}

	public String getCfObbligato3() {
		return cfObbligato3;
	}

	public BigDecimal getQuotaObbligato3() {
		return quotaObbligato3;
	}

	@Column(name = "CF_OBBLIGATO2", length = 16)
	private String cfObbligato2;

	@Column(name = "NOME_OBBL2", length = 765)
	private String nomeObbl2;

	@Column(name = "COGNOME_OBBL2", length = 765)
	private String cognomeObbl2;

	@Column(name = "INDIRIZZO_OBBL2", length = 765)
	private String indirizzoObbl2;

	@Column(name = "QUOTA_OBBLIGATO2", precision = 9, scale = 4)
	private BigDecimal quotaObbligato2;

	@Column(name = "CF_OBBLIGATO3", length = 16)
	private String cfObbligato3;

	@Column(name = "NOME_OBBL3", length = 765)
	private String nomeObbl3;

	@Column(name = "COGNOME_OBBL3", length = 765)
	private String cognomeObbl3;

	@Column(name = "INDIRIZZO_OBBL3", length = 765)
	private String indirizzoObbl3;

	public String getIndirizzoObbl3() {
		return indirizzoObbl3;
	}

	@Column(name = "QUOTA_OBBLIGATO3", precision = 9, scale = 4)
	private BigDecimal quotaObbligato3;

	@Basic(optional = false)
	@Column(name = "IMPORTO_TOTALE", nullable = false, precision = 9, scale = 4)
	private BigDecimal importoTotale;

	public void setImpIva(BigDecimal impIva) {
		this.impIva = impIva;
	}

	public void setIdParamIva(ParametriIndata idParamIva) {
		this.idParamIva = idParamIva;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public BigDecimal getBollo() {
		return bollo;
	}

	@Column(name = "BOLLO", precision = 9, scale = 4)
	private BigDecimal bollo;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@Column(name = "TIMBRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timbro;

	public Date getTimbro() {
		return timbro;
	}

	@Column(name = "CAUSALE", length = 3000)
	private String causale;

	public String getNote() {
		return note;
	}

	public String getCausale() {
		return causale;
	}

	@Column(name = "RISCOSSO", precision = 9, scale = 4)
	private BigDecimal riscosso;

	public BigDecimal getRiscosso() {
		return riscosso;
	}

	public Integer getIdFatt() {
		return idFatt;
	}

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "fatturaSequence")
	@SequenceGenerator(name = "fatturaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_FATT", nullable = false)
	private Integer idFatt;

	@Column(name = "IMP_IVA", precision = 9, scale = 2)
	private BigDecimal impIva;

	public BigDecimal getImpIva() {
		return impIva;
	}

	@Column(name = "CONTRIBUTO", precision = 9, scale = 4)
	private BigDecimal contributo;

	public BigDecimal getContributo() {
		return contributo;
	}

	@JoinTable(name = "FATTURA_MESI_PRECEDENTI", joinColumns = {
			@JoinColumn(name = "ID_FATT_PRINC", referencedColumnName = "ID_FATT", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ID_FATT_CORR", referencedColumnName = "ID_FATT", nullable = false) })
	@ManyToMany
	private List<Fattura> fatturaList;

	public void setFatturaList(List<Fattura> fatturaList) {
		this.fatturaList = fatturaList;
	}

	public List<Fattura> getFatturaList() {
		return fatturaList;
	}

	public String getNomeObbl1() {
		return nomeObbl1;
	}

	public void setNomeObbl1(String nomeObbl1) {
		this.nomeObbl1 = nomeObbl1;
	}

	public String getCognomeObbl1() {
		return cognomeObbl1;
	}

	public void setCognomeObbl1(String cognomeObbl1) {
		this.cognomeObbl1 = cognomeObbl1;
	}

	public String getIndirizzoObbl1() {
		return indirizzoObbl1;
	}

	public void setIndirizzoObbl1(String indirizzoObbl1) {
		this.indirizzoObbl1 = indirizzoObbl1;
	}

	public String getNomeObbl2() {
		return nomeObbl2;
	}

	public void setNomeObbl2(String nomeObbl2) {
		this.nomeObbl2 = nomeObbl2;
	}

	public String getCognomeObbl2() {
		return cognomeObbl2;
	}

	public void setCognomeObbl2(String cognomeObbl2) {
		this.cognomeObbl2 = cognomeObbl2;
	}

	public String getIndirizzoObbl2() {
		return indirizzoObbl2;
	}

	public void setIndirizzoObbl2(String indirizzoObbl2) {
		this.indirizzoObbl2 = indirizzoObbl2;
	}

	public String getNomeObbl3() {
		return nomeObbl3;
	}

	public void setNomeObbl3(String nomeObbl3) {
		this.nomeObbl3 = nomeObbl3;
	}

	public String getCognomeObbl3() {
		return cognomeObbl3;
	}

	public void setCognomeObbl3(String cognomeObbl3) {
		this.cognomeObbl3 = cognomeObbl3;
	}

	public void setCfObbligato1(String cfObbligato1) {
		this.cfObbligato1 = cfObbligato1;
	}

	public void setCfObbligato2(String cfObbligato2) {
		this.cfObbligato2 = cfObbligato2;
	}

	public void setCfObbligato3(String cfObbligato3) {
		this.cfObbligato3 = cfObbligato3;
	}

	public void setIndirizzoObbl3(String indirizzoObbl3) {
		this.indirizzoObbl3 = indirizzoObbl3;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public AnagrafeSoc getCodAna() {
		return codAna;
	}

	public void setCodAna(AnagrafeSoc codAna) {
		this.codAna = codAna;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public void setMeseRif(Integer meseRif) {
		this.meseRif = meseRif;
	}

	public void setContributo(BigDecimal contributo) {
		this.contributo = contributo;
	}

	public void setIdParamFascia(ParametriIndata idParamFascia) {
		this.idParamFascia = idParamFascia;
	}

	public void setPaiIntervento(PaiIntervento paiIntervento) {
		this.paiIntervento = paiIntervento;
	}

	public ParametriIndata getIdParamIva() {
		return idParamIva;
	}

	@JoinColumn(name = "ID_PARAM_IVA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamIva;

	@JoinColumn(name = "ID_PARAM_PAGAM", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamPagam;

	@JoinColumn(name = "ID_PARAM_FASCIA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamFascia;

	public ParametriIndata getIdParamFascia() {
		return idParamFascia;
	}

	@JoinColumn(name = "ID_PARAM_STATO", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamStato;

	public ParametriIndata getIdParamStato() {
		return idParamStato;
	}

	public void setIdParamStato(ParametriIndata idParamStato) {
		this.idParamStato = idParamStato;
	}

	@JoinColumns({ @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	public PaiIntervento getPaiIntervento() {
		return paiIntervento;
	}

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false)
	@ManyToOne(optional = false)
	private AnagrafeSoc codAna;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idFatt")
	private List<FatturaDettaglio> fatturaDettaglioList;

	public void setFatturaDettaglioList(List<FatturaDettaglio> fatturaDettaglioList) {
		this.fatturaDettaglioList = fatturaDettaglioList;
	}

	public List<FatturaDettaglio> getFatturaDettaglioList() {
		return fatturaDettaglioList;
	}

	public Fattura() {
	}

	public Fattura(Integer idFatt) {
		this.idFatt = idFatt;
	}

	public Fattura(Integer idFatt, String anno, int numFatt, String codFisc, String nome, String cognome, Date scadenza,
			BigDecimal importoTotale) {
		this.idFatt = idFatt;
		this.anno = anno;
		this.numFatt = numFatt;
		this.codFisc = codFisc;
		this.nome = nome;
		this.cognome = cognome;
		this.scadenza = scadenza;
		this.importoTotale = importoTotale;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idFatt != null ? idFatt.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Fattura)) {
			return false;
		}
		Fattura other = (Fattura) object;
		if ((this.idFatt == null && other.idFatt != null)
				|| (this.idFatt != null && !this.idFatt.equals(other.idFatt))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Fattura[idFatt=" + idFatt + "]";
	}

	public boolean hasEsenzioneIva() {
		return Objects.equals(getIdParamIva().getDecimalParam(), BigDecimal.ZERO);
	}

	public abstract class FatturaObbligati {

		public abstract String getNome();

		public abstract String getCognome();

		public abstract String getCodiceFiscale();

		public abstract String getIndirizzo();

		public abstract BigDecimal getQuota();

		public abstract boolean isSoggetto();
	}

	private FatturaObbligati buildFatturaObbligati(final boolean isSoggetto, final String nome, final String cognome,
			final String codiceFiscale, final String indirizzo, final BigDecimal quota) {
		return new FatturaObbligati() {
			@Override
			public String getNome() {
				return nome;
			}

			@Override
			public String getCognome() {
				return cognome;
			}

			@Override
			public String getCodiceFiscale() {
				return codiceFiscale;
			}

			@Override
			public String getIndirizzo() {
				return indirizzo;
			}

			@Override
			public BigDecimal getQuota() {
				return quota;
			}

			@Override
			public boolean isSoggetto() {
				return isSoggetto;
			}
		};
	}

	public static Predicate<FatturaObbligati> fatturaObbligatiPredicate() {
		return fo -> fo.getQuota() != null && !BigDecimal.ZERO.equals(fo.getQuota());
	}

	public static List<FatturaObbligati> filterFatturaObbligati(List<FatturaObbligati> fattureObbligati, Predicate<FatturaObbligati> predicate) {
		return fattureObbligati.stream().filter(predicate).collect(Collectors.<FatturaObbligati>toList());
	}
	
	public List<FatturaObbligati> getFatturaObbligati() {
		
		List<FatturaObbligati> listaFattObb = Arrays.asList(
				buildFatturaObbligati(true, nome, cognome, codFisc, "", importoTotale),
				buildFatturaObbligati(false, nomeObbl1, cognomeObbl1, cfObbligato1, indirizzoObbl1, quotaObbligato1),
				buildFatturaObbligati(false, nomeObbl2, cognomeObbl2, cfObbligato2, indirizzoObbl2, quotaObbligato2),
				buildFatturaObbligati(false, nomeObbl3, cognomeObbl3, cfObbligato3, indirizzoObbl3, quotaObbligato3));
		
		return filterFatturaObbligati(listaFattObb, fatturaObbligatiPredicate());
	}


	public List<PaiInterventoMese> getPaiInterventoMeseList() {
		return Lists.newArrayList(Iterables.concat(Iterables.transform(getFatturaDettaglioList(),
				new Function<FatturaDettaglio, Iterable<PaiInterventoMese>>() {
					public Iterable<PaiInterventoMese> apply(FatturaDettaglio fatturaDettaglio) {
						return fatturaDettaglio.getPaiInterventoMeseList();
					}
				})));
	}

	public Integer getMeseDiRiferimento() {
		return MoreObjects.firstNonNull(getMeseRif(), getFatturaDettaglioList().iterator().next().getMeseEff());
	}

	public void setNumFatt(int numFatt) {
		this.numFatt = numFatt;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public void setTimbro(Date timbro) {
		this.timbro = timbro;
	}

	public void setQuotaObbligato1(BigDecimal quotaObbligato1) {
		this.quotaObbligato1 = quotaObbligato1;
	}

	public void setQuotaObbligato2(BigDecimal quotaObbligato2) {
		this.quotaObbligato2 = quotaObbligato2;
	}

	public void setQuotaObbligato3(BigDecimal quotaObbligato3) {
		this.quotaObbligato3 = quotaObbligato3;
	}

	public void setBollo(BigDecimal bollo) {
		this.bollo = bollo;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}
}
