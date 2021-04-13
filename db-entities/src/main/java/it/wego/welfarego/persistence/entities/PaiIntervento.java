package it.wego.welfarego.persistence.entities;

import com.google.common.base.*;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.joda.time.Days;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "PAI_INTERVENTO")
@NamedQueries({ @NamedQuery(name = "PaiIntervento.findAll", query = "SELECT p FROM PaiIntervento p"),
		@NamedQuery(name = "PaiIntervento.findByCodPaiInterventiComune", query = "SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.codPai = :codPai AND p.associazione.id=1"),
		@NamedQuery(name = "PaiIntervento.findByCodPaiInterventiEsterni", query = "SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.codPai = :codPai AND p.associazione.id != 1"),
		@NamedQuery(name = "PaiIntervento.findByCodPai", query = "SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.codPai = :codPai"),
		@NamedQuery(name = "PaiIntervento.findByCodTipint", query = "SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.codTipint = :codTipint"),
		@NamedQuery(name = "PaiIntervento.findByCntTipint", query = "SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.cntTipint = :cntTipint"),
		@NamedQuery(name = "PaiIntervento.findByStatoInt", query = "SELECT p FROM PaiIntervento p WHERE p.statoInt = :statoInt"),
		@NamedQuery(name = "PaiIntervento.findByDtEsec", query = "SELECT p FROM PaiIntervento p WHERE p.dtEsec = :dtEsec"),
		@NamedQuery(name = "PaiIntervento.findByDtChius", query = "SELECT p FROM PaiIntervento p WHERE p.dtChius = :dtChius"),
		@NamedQuery(name = "PaiIntervento.findByIndEsitoInt", query = "SELECT p FROM PaiIntervento p WHERE p.indEsitoInt = :indEsitoInt"),
		@NamedQuery(name = "PaiIntervento.findByNoteChius", query = "SELECT p FROM PaiIntervento p WHERE p.noteChius = :noteChius"),
		@NamedQuery(name = "PaiIntervento.findByCostoPrev", query = "SELECT p FROM PaiIntervento p WHERE p.costoPrev = :costoPrev"),
		@NamedQuery(name = "PaiIntervento.findByDtApe", query = "SELECT p FROM PaiIntervento p WHERE p.dtApe = :dtApe"),
		@NamedQuery(name = "PaiIntervento.findByDurMesi", query = "SELECT p FROM PaiIntervento p WHERE p.durMesi = :durMesi"),
		@NamedQuery(name = "PaiIntervento.findByDurSettimane", query = "SELECT p FROM PaiIntervento p WHERE p.durSettimane = :durSettimane"),
		@NamedQuery(name = "PaiIntervento.findByDtAvvio", query = "SELECT p FROM PaiIntervento p WHERE p.dtAvvio = :dtAvvio"),
		@NamedQuery(name = "PaiIntervento.findByDataAvvioProposta", query = "SELECT p FROM PaiIntervento p WHERE p.dataAvvioProposta = :dtAvvioProposta"),
		@NamedQuery(name = "PaiIntervento.findByQuantita", query = "SELECT p FROM PaiIntervento p WHERE p.quantita = :quantita"),
		@NamedQuery(name = "PaiIntervento.findByDtFine", query = "SELECT p FROM PaiIntervento p WHERE p.dtFine = :dtFine"),
		@NamedQuery(name = "PaiIntervento.findByProtocollo", query = "SELECT p FROM PaiIntervento p WHERE p.protocollo = :protocollo") })
public class PaiIntervento implements Serializable, Cloneable {

	public static final String RICHIESTA_APPUNTAMENTO = "AP001", INTERVENTO_DEFAULT = "AA000";

	public static final char STATO_INTERVENTO_LISTA_ATTESA = 'L', STATO_INTERVENTO_CHIUSO = 'C',
			STATO_INTERVENTO_APERTO = 'A', STATO_INTERVENTO_ESECUTIVO = 'E', STATO_INTERVENTO_RIMANDATO = 'R',
			STATO_INTERVENTO_SOSPESO = 'S';

	public static final String APERTO = "Aperto", IN_APPROVAZIONE = "In approvazione", DETERMINA = "Da determinare",
			RESPINTO = "Respinto", GESTIONE_ECONOMICA = "In liquidazione", CHIUSO = "Chiuso", RIMANDATO = "Rimandato",
			SOSPESO = "Sospeso";

	public final static Set<String> codTipintFasciaRedd = Collections
			.unmodifiableSet(Sets.newHashSet("AZ014", "AZ011", "AZ018", "AZ016", "AZ017"));

	public final static Set<String> fap = Collections
			.unmodifiableSet(Sets.newHashSet("AZ008A", "AZ008B", "AZ007A", "AZ007B", "AZ009"));

	public final static Set<String> pid = Collections.unmodifiableSet(Sets.newHashSet("AZ018", "AZ016", "AZ017"));

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiInterventoPK paiInterventoPK;

	@Basic(optional = false)
	@Column(name = "STATO_INT", nullable = false)
	private char statoInt;

	@Column(name = "DT_ESEC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtEsec;

	@Column(name = "DT_CHIUS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtChius;

	@Column(name = "DT_SOSP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtSosp;

	@Column(name = "IND_ESITO_INT", length = 2)
	private String indEsitoInt;

	@Column(name = "NOTE_CHIUS", length = 3000)
	private String noteChius;

	@Column(name = "NOTE_SOSPENSIONE", length = 3000)
	private String noteSospensione;

	@Column(name = "NUM_DETERMINA", length = 20)
	private String numDetermina;

	@Column(name = "COSTO_PREV", precision = 9, scale = 2)
	private BigDecimal costoPrev;

	@Column(name = "DT_APE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtApe;

	@Column(name = "DT_RICH_APP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRichiestaApprovazione;

	@Column(name = "DUR_MESI")
	private Integer durMesi;

	@Column(name = "DUR_SETTIMANE")
	private Integer durSettimane;

	@Column(name = "DT_AVVIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAvvio;

	@Column(name = "QUANTITA")
	private BigDecimal quantita;

	@Column(name = "DT_FINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFine;

	@Column(name = "DT_FINE_INDIC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFineIndicativa;

	@Column(name = "DT_AVVIO_PROP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAvvioProposta;

	@OneToMany(mappedBy = "paiIntervento")
	private List<PaiDocumento> paiDocumentoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<Fattura> fatturaList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<Mandato> mandatoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<UniqueTasklist> uniqueTasklistList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<PaiInterventoMese> paiInterventoMeseList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<PaiInterventoCivObb> paiInterventoCivObbList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<PaiInterventoAnagrafica> paiInterventoAnagraficaList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<LogMessaggi> logMessaggiList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "interventoPadre")
	private List<InterventiAssociati> interventiFigli;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "interventoFiglio")
	private InterventiAssociati interventoPadre;

	@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipologiaIntervento tipologiaIntervento;

	@JoinColumn(name = "DS_ID_PARAM_FASCIA_REDD", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata dsIdParamFasciaRedd;

	// associazione di defauult = comune di trieste
	@JoinColumn(name = "ID_ASSOCIAZIONE", referencedColumnName = "ID")
	@ManyToOne
	private Associazione associazione;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Pai pai;

	@JoinColumn(name = "DS_COD_ANA_BENEF", referencedColumnName = "COD_ANA")
	@ManyToOne
	private AnagrafeSoc dsCodAnaBenef;

	@Column(name = "MESI_PROROGA")
	private Integer durMesiProroga;

	@Column(name = "COD_IMP_PROROGA")
	private String codImpProroga;

	@Column(name = "RINNOVATO", nullable = false)
	private Integer rinnovato = 1;

	@JoinColumn(name = "DS_COD_ANA_RICHIEDENTE", referencedColumnName = "COD_ANA")
	@ManyToOne
	private AnagrafeSoc dsCodAnaRich;

	@Column(name = "IBAN_DELEGATO")
	private String ibanDelegato;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<PaiEvento> paiEventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<MandatoDettaglio> mandatoDettaglioList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	private List<MapDatiSpecificiIntervento> mapDatiSpecificiInterventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiIntervento")
	@MapKey(name = "codCampo")
	private Map<String, MapDatiSpecificiIntervento> datiSpecificiByCod;

	@Column(name = "CSR_ID", length = 255)
	private String idCsr;

	@Column(name = "MOTIVAZIONE")
	private String motivazione;

	@Column(name = "DATI_ORIGINALI")
	private String datiOriginali;

	@Column(name = "PROTOCOLLO")
	private String protocollo;

	@Column(name = "STATO_ATTUALE")
	private String statoAttuale;

	@Column(name = "DT_FINE_PROROGA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFineProroga;

	@Column(name = "URGENTE", nullable = false)
	private char urgente = 'N';

	@Column(name = "APPROVAZIONE_TECNICA", nullable = false)
	private char approvazioneTecnica = 'N';

	@JoinColumn(name = "ID_TARIFFA", referencedColumnName = "ID")
	@ManyToOne
	private Tariffa tariffa;

	@Column(name = "TESTO_AUTORIZZAZIONE")
	private String testoAutorizzazione;

	@Column(name = "data_Creazione_Record")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCreazioneRecord;

	@Column(name = "utente_Creazione_Record")
	private String utenteCreazioneRecord;

	public PaiIntervento() {
	}

	public PaiIntervento(PaiInterventoPK paiInterventoPK) {
		this.paiInterventoPK = paiInterventoPK;
	}

	public PaiIntervento(PaiInterventoPK paiInterventoPK, char statoInt) {
		this.paiInterventoPK = paiInterventoPK;
		this.statoInt = statoInt;
	}

	public PaiIntervento(int codPai, String codTipint, int cntTipint) {
		this.paiInterventoPK = new PaiInterventoPK(codPai, codTipint, cntTipint);
	}

	public BigDecimal getImportoStandard() {
		BigDecimal importoStandard = null;

		Tariffa tariffa = getTariffa();

		// se ha una struttura
		if (tariffa != null) {
			importoStandard = tariffa.getCosto();
		} else {
			importoStandard = getTipologiaIntervento().getImpStdCosto();
		}

		return importoStandard;
	}

	public boolean isUltimoBatchRichiestaApprovazione() {
		Iterable<Date> dates = Iterables
				.filter(Iterables.transform(getPai().getPaiInterventoList(), new Function<PaiIntervento, Date>() {
					public Date apply(PaiIntervento input) {
						return input.getDataRichiestaApprovazione();
					}
				}), Predicates.notNull());
		if (Iterables.isEmpty(dates) || dataRichiestaApprovazione == null) {
			return false;
		} else {
			Date max = Ordering.natural().max(dates);
			return Objects.equal(max, dataRichiestaApprovazione);
		}
	}

	public @Nullable Date calculateDtFine() {
		if (getDtFine() != null) {
			return getDtFine();
		} else if (getDataFineIndicativa() != null) {
			return getDataFineIndicativa();
		} else if (getDurMesi() != null && getDtAvvio() != null) {
			Date fine = (new DateTime(getDtAvvio()).plusMonths(getDurMesi())).toDate();
			return fine;
		} else {
			return null;
		}
	}

	public boolean hasFasciaEsenzione() {
		return getDsIdParamFasciaRedd() != null;
	}

	public boolean hasFasciaEsenzioneTotale() {
		return hasFasciaEsenzione()
				&& getDsIdParamFasciaRedd().getDecimalPercentageParamAsDecimal().compareTo(BigDecimal.ZERO) == 0;
	}

	public void setIbanDelegato(String ibanDelegato) {
		this.ibanDelegato = Strings.emptyToNull(ibanDelegato);
	}

	/**
	 * restituisce il campo iban_delegato o l'iban del beneficiario, il primo
	 * non-null
	 *
	 * @return
	 */
	public String getIbanDelegatoObenef() {
		if (getIbanDelegato() != null) {
			return getIbanDelegato();
		} else if (getDsCodAnaBenef() != null && getDsCodAnaBenef().getIbanPagam() != null) {
			return getDsCodAnaBenef().getIbanPagam();
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paiInterventoPK != null ? paiInterventoPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiIntervento)) {
			return false;
		}
		PaiIntervento other = (PaiIntervento) object;
		return !((this.paiInterventoPK == null && other.paiInterventoPK != null)
				|| (this.paiInterventoPK != null && !this.paiInterventoPK.equals(other.paiInterventoPK)));
	}

	public String getValDatoSpecifico(String key) {
		MapDatiSpecificiIntervento mapDatiSpecifici = getDatiSpecificiByCod().get(key);
		return mapDatiSpecifici == null ? null : mapDatiSpecifici.getValCampo();
	}

	public String getCodValDatoSpecifico(String key) {
		MapDatiSpecificiIntervento mapDatiSpecifici = getDatiSpecificiByCod().get(key);
		return mapDatiSpecifici == null ? null : mapDatiSpecifici.getCodValCampo();
	}

	public @Nullable BigDecimal getDecimalDatoSpecifico(String key) {
		String val = Strings.emptyToNull(getValDatoSpecifico(key));
		return val == null ? null : new BigDecimal(val);
	}

	public @Nullable Date getDateDatoSpecifico(String key) {
		String val = Strings.emptyToNull(getValDatoSpecifico(key));
		try {
			return val == null ? null : new SimpleDateFormat("dd/MM/yyyy").parse(val);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}

	public BigDecimal getDsReddDich() {
		return getDecimalDatoSpecifico("ds_redd_dich");
	}

	public BigDecimal getDsParCalc() {
		return getDecimalDatoSpecifico("ds_par_calc");
	}

	public BigDecimal getDsOreSettBadante() {
		String str = Strings.emptyToNull(getValDatoSpecifico("ds_ore_sett_badante"));
		if (str == null) {
			return null;
		}
		try {
			if (str.equals(">54")) {
				return new BigDecimal(54);
			}
			return new BigDecimal(str);
		} catch (Exception ex) {
			str = str.split("-")[1];
			return new BigDecimal(str);
		}
	}

	public BigDecimal getDsAdl() {
		return getDecimalDatoSpecifico("ds_adl");
	}

	public String getDsCertGraveDisab() {
		return getValDatoSpecifico("ds_cert_grave_disab");
	}

	public String getDsDemenza() {
		return getValDatoSpecifico("ds_demenza");
	}

	public String getDsDisabilita() {

		return getCodValDatoSpecifico("ds_disabilita");
	}

	public String getDsTipUte2() {
		return getValDatoSpecifico("ds_tip_ute2");
	}

	public boolean getBenefAssVitaIndipendente() {
		return Iterables.any(getPai().getPaiInterventoList(), new Predicate<PaiIntervento>() {
			public boolean apply(PaiIntervento paiIntervento) {
				return !Objects.equal(paiIntervento.getStatoInt(), PaiIntervento.STATO_INTERVENTO_CHIUSO)
						&& Objects.equal(paiIntervento.getPaiInterventoPK().getCodTipint(),
								TipologiaIntervento.COD_TIPINT_FAP_SVI);
			}

			public boolean test(PaiIntervento input) {
				return apply(input) ;
			}
		});
	}

	public int getDurataGiorni() {
		if (getDtFine() != null) {
			return Days.daysBetween(new DateTime(getDtAvvio()), new DateTime(getDtFine())).getDays();
		} else {
			return Days.daysBetween(new DateTime(getDtAvvio()), new DateTime(getDtFine()).plusMonths(getDurMesi()))
					.getDays();
		}
	}

	public int getEta() {
		return getPai().getAnagrafeSoc().getEta();
	}

	public BigDecimal getDsSpesaMensAll() {
		return getDecimalDatoSpecifico("ds_spesa_mens_all");
	}

	public BigDecimal getDsSpeseCond() {
		return getDecimalDatoSpecifico("ds_spese_cond");
	}

	public boolean isDsPrimoEventoVita() {
		return getBooleanDatoSpecifico("ds_evento_vita_PID", "S");
	}

	private boolean getBooleanDatoSpecifico(String key, String trueValue) {
		String val = getCodValDatoSpecifico(key);
		return Objects.equal(val, trueValue);
	}

	public boolean isChiuso() {
		return getDtChius() != null;
	}
	
    public boolean isInterventoAccesso() {
        return getPaiInterventoPK().getCodTipint().matches("^AA[0-9]*$");
    }

	/**
	 * @return true se PAI_INTERVENTO.rinnovato !=1
	 */
	public boolean isRinnovato() {
		return !getRinnovato().equals(1);
	}

	public @Nullable Determine getDeterminaAssociata() {
		return Iterables.getFirst(
				Iterables.filter(Iterables.transform(getPaiEventoList(), new Function<PaiEvento, Determine>() {
					public Determine apply(PaiEvento input) {
						return input.getIdDetermina();
					}
				}), Predicates.notNull()), null);
	}

	public ParametriIndata getIdParamUniMis() {
		ParametriIndata unitaDiMisura = getTipologiaIntervento().getIdParamUniMis();
		if (unitaDiMisura != null && Objects.equal(getStatoInt(), PaiIntervento.STATO_INTERVENTO_ESECUTIVO)
				&& Parametri.UNITA_MISURA_SETTIMANALI.contains(unitaDiMisura.getIdParam().getCodParam())) {
			unitaDiMisura = unitaDiMisura.getIdParam().getTipParam()
					.getParametroByCodParam(
							Parametri.UNITA_MISURA_SETTIMANALI_TO_MENSILI.get(unitaDiMisura.getIdParam().getCodParam()))
					.getParametriIndataSingle();
		}
		return unitaDiMisura;

	}

	public String getLabelQuantita() {
		ParametriIndata unitaDiMisura = getTipologiaIntervento().getIdParamUniMis();
		String result = null;
		if (unitaDiMisura != null) {
			result = unitaDiMisura.getDesParam();
		}
		return result;
	}

	/**
	 *
	 * @return true se l'intervento deve conteggiare la fascia di reddito, false
	 *         otherwise
	 */
	public boolean shouldUseFascia() {
		return codTipintFasciaRedd.contains(getPaiInterventoPK().getCodTipint());
	}

	/**
	 * 
	 * @return true se l'intervento è di tipo pid quindi deve contare le settimane
	 *         come durata al poso dei mesi
	 */
	public boolean shouldUseSettimane() {
		return pid.contains(getPaiInterventoPK().getCodTipint());
	}

	public boolean hasActiveTask() {
		return Iterables.any(getUniqueTasklistList(), new Predicate<UniqueTasklist>() {
			public boolean apply(UniqueTasklist uniqueTasklist) {
				return Objects.equal(uniqueTasklist.getFlgEseguito(), UniqueTasklist.FLG_ESEGUITO_NO);
			}

			public boolean test(UniqueTasklist input) {
				return apply(input);
			}
		});
	}

	public boolean shouldUseAccredito() {
		// se non c'è il dato specifico e l'intervento è un fap ritorno di default true
		// ( utile per gli interventi importati da GDA senza dati specifici
		if (getDsTipoPagamento() == null && fap.contains(getPaiInterventoPK().getCodTipint()) == true) {
			return true;
		}
		return (getDsTipoPagamento() != null)
				&& (getDsTipoPagamento().equals("03") || getDsTipoPagamento().equals("04"));
	}

	public String getDsDettaglioTipoIntervento() {
		return getCodValDatoSpecifico("dett_tip_int_fap");
	}

	public String getDsPunteggioScalaKatz() {
		return getCodValDatoSpecifico("punteggio_scala_katz");
	}

	public String getDsRichiestaAssegnoAccompagnamento() {
		return getCodValDatoSpecifico("ds_rich_ass_acc");
	}

	public String getDsDisabilitaSensoriale() {
		return getCodValDatoSpecifico("ds_disp_senso_fap");
	}

	public BigDecimal getDsDurataUVD() {
		return getDecimalDatoSpecifico("dur_uvd");
	}

	public Date getDsDataUVD() {
		return getDateDatoSpecifico("data_uvd");
	}

	public String getDsMicroprestazione() {
		return getCodValDatoSpecifico("ds_microprestazione");
	}

	public String getDsMicroprestazione2() {
		return getCodValDatoSpecifico("ds_microprestazione2");
	}

	public String getDsMicroprestazione3() {
		return getCodValDatoSpecifico("ds_microprestazione3");
	}

	public String getDsMicroprestazione4() {
		return getCodValDatoSpecifico("ds_microprestazione4");
	}

	public String getDsMicroprestazione5() {
		return getCodValDatoSpecifico("ds_microprestazione5");
	}

	public String getDsMicroprestazione6() {
		return getCodValDatoSpecifico("ds_microprestazione6");
	}

	public String getDsStrutturaAcc1() {
		return getCodValDatoSpecifico("ds_strutt_acc1");
	}

	public String getDsStrutturaAcc2() {
		return getCodValDatoSpecifico("ds_strutt_acc2");
	}

	public String getDsStrutturaAcc3() {
		return getCodValDatoSpecifico("ds_strutt_acc3");
	}

	public String getDsStrutturaAcc4() {
		return getCodValDatoSpecifico("ds_strutt_acc4");
	}

	public String getDsStrutturaAcc5() {
		return getCodValDatoSpecifico("ds_strutt_acc5");
	}

	public String getDsStrutturaAcc6() {
		return getCodValDatoSpecifico("ds_strutt_acc6");
	}

	public String getDsStrutturaAcc7() {
		return getCodValDatoSpecifico("ds_strutt_acc7");
	}

	public String getDsStrutturaAcc8() {
		return getCodValDatoSpecifico("ds_strutt_acc8");
	}

	public String getDsStrutturaAcc9() {
		return getCodValDatoSpecifico("ds_strutt_acc9");
	}

	public String getDsStrutturaAcc10() {
		return getCodValDatoSpecifico("ds_strutt_acc10");
	}

	public String getDsStrutturaAcc11() {
		return getCodValDatoSpecifico("ds_strutt_acc11");
	}

	public String getDsStrutturaAcc12() {
		return getCodValDatoSpecifico("ds_strutt_acc12");
	}

	public String getDsFamigliaAffidataria() {
		return getCodValDatoSpecifico("ds_famiglia_aff");
	}

	public String getDsPresenzaPiuAddetti() {
		return getCodValDatoSpecifico("ds_pres_piu_addetti");
	}

	public String getDsTipoPagamento() {
		String result = getCodValDatoSpecifico("ds_tip_pagam");
		if (result == null) {
			result = getCodValDatoSpecifico("ds_tip_pagam_fap");
		}
		return result;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataApertura = "";
		try {
			dataApertura = sdf.format(dtApe);
		} catch (Exception ex) {

		}
		final StringBuffer sb = new StringBuffer("PaiIntervento{");
		sb.append("dtApe=").append(dataApertura);
		sb.append(", associazione=").append(associazione);
		sb.append(", codPai=").append(paiInterventoPK.getCodPai());
		sb.append(", codTipoInt=").append(paiInterventoPK.getCodTipint());
		sb.append(", cntTipoInt=").append(paiInterventoPK.getCntTipint());
		sb.append('}');
		return sb.toString();
	}

}
