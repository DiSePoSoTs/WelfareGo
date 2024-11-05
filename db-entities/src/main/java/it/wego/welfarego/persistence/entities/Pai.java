/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;


import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
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
import java.util.Set;



/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "PAI")
@NamedQueries({ @NamedQuery(name = "Pai.findAll", query = "SELECT p FROM Pai p"),
		@NamedQuery(name = "Pai.findByCodPai", query = "SELECT p FROM Pai p WHERE p.codPai = :codPai"),
		@NamedQuery(name = "Pai.findByDtApePai", query = "SELECT p FROM Pai p WHERE p.dtApePai = :dtApePai"),
		@NamedQuery(name = "Pai.findByFlgStatoPai", query = "SELECT p FROM Pai p WHERE p.flgStatoPai = :flgStatoPai"),
		@NamedQuery(name = "Pai.findByFlgCsr", query = "SELECT p FROM Pai p WHERE p.flgCsr = :flgCsr"),
		@NamedQuery(name = "Pai.findByDtInvioCsr", query = "SELECT p FROM Pai p WHERE p.dtInvioCsr = :dtInvioCsr"),
		@NamedQuery(name = "Pai.findByFlgModCsr", query = "SELECT p FROM Pai p WHERE p.flgModCsr = :flgModCsr"),
		@NamedQuery(name = "Pai.findByDtChiusPai", query = "SELECT p FROM Pai p WHERE p.dtChiusPai = :dtChiusPai"),
		@NamedQuery(name = "Pai.findByMotivChius", query = "SELECT p FROM Pai p WHERE p.motivChius = :motivChius"),
		@NamedQuery(name = "Pai.findByNumPg", query = "SELECT p FROM Pai p WHERE p.numPg = :numPg"),
		@NamedQuery(name = "Pai.findByDtPg", query = "SELECT p FROM Pai p WHERE p.dtPg = :dtPg"),
		@NamedQuery(name = "Pai.findByNumNuc", query = "SELECT p FROM Pai p WHERE p.numNuc = :numNuc"),
		@NamedQuery(name = "Pai.findByNumFigli", query = "SELECT p FROM Pai p WHERE p.numFigli = :numFigli"),
		@NamedQuery(name = "Pai.findByNumFigliConv", query = "SELECT p FROM Pai p WHERE p.numFigliConv = :numFigliConv"),
		@NamedQuery(name = "Pai.findByProvvGiudiz", query = "SELECT p FROM Pai p WHERE p.provvGiudiz = :provvGiudiz"),
		@NamedQuery(name = "Pai.findByNoteCondFam", query = "SELECT p FROM Pai p WHERE p.noteCondFam = :noteCondFam"),
		@NamedQuery(name = "Pai.findByFlgAgeSan", query = "SELECT p FROM Pai p WHERE p.flgAgeSan = :flgAgeSan"),
		@NamedQuery(name = "Pai.findByFlgCarSa", query = "SELECT p FROM Pai p WHERE p.flgCarSa = :flgCarSa"),
		@NamedQuery(name = "Pai.findByFlgCarSs", query = "SELECT p FROM Pai p WHERE p.flgCarSs = :flgCarSs"),
		@NamedQuery(name = "Pai.findByFlgCarVol", query = "SELECT p FROM Pai p WHERE p.flgCarVol = :flgCarVol"),
		@NamedQuery(name = "Pai.findByFlgCarParr", query = "SELECT p FROM Pai p WHERE p.flgCarParr = :flgCarParr"),
		@NamedQuery(name = "Pai.findByFlgCarAltro", query = "SELECT p FROM Pai p WHERE p.flgCarAltro = :flgCarAltro"),
		@NamedQuery(name = "Pai.findByCarAltro", query = "SELECT p FROM Pai p WHERE p.carAltro = :carAltro"),
		@NamedQuery(name = "Pai.findByDtDiag", query = "SELECT p FROM Pai p WHERE p.dtDiag = :dtDiag"),
		@NamedQuery(name = "Pai.findByMotiv", query = "SELECT p FROM Pai p WHERE p.motiv = :motiv"),
		@NamedQuery(name = "Pai.findByIsee", query = "SELECT p FROM Pai p WHERE p.isee = :isee"),
		@NamedQuery(name = "Pai.findByDtScadIsee", query = "SELECT p FROM Pai p WHERE p.dtScadIsee = :dtScadIsee"),
		@NamedQuery(name = "Pai.findByFlgDemenza", query = "SELECT p FROM Pai p WHERE p.flgDemenza = :flgDemenza"),
		@NamedQuery(name = "Pai.findByAdl", query = "SELECT p FROM Pai p WHERE p.adl = :adl") })
public class Pai implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Pai[codPai=" + codPai + ", codAna= " + this.rawCodAna + "]";
	}

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "paiSequence")
	@SequenceGenerator(name = "paiSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	public Integer getNumNuc() {
		return numNuc;
	}

	public String getMotiv() {
		return motiv;
	}

	public void setNumNuc(Integer numNuc) {
		this.numNuc = numNuc;
	}

	public void setDtApePai(Date dtApePai) {
		this.dtApePai = dtApePai;
	}

	public void setCodUteAs(Utenti codUteAs) {
		this.codUteAs = codUteAs;
	}

	public void setIdParamUot(ParametriIndata idParamUot) {
		this.idParamUot = idParamUot;
	}

	public void setIdParamFascia(ParametriIndata idParamFascia) {
		this.idParamFascia = idParamFascia;
	}

	public ParametriIndata getIdParamCertificatoL104() {
		return idParamCertificatoL104;
	}

	public void setIdParamCertificatoL104(ParametriIndata idParamCertificatoL104) {
		this.idParamCertificatoL104 = idParamCertificatoL104;
	}

	public char getFlgStatoPai() {
		return flgStatoPai;
	}

	public Integer getCodPai() {
		return codPai;
	}

	@Column(name = "COD_ANA", nullable = false)
	private Integer rawCodAna;

	@Basic(optional = false)
	@Column(name = "DT_APE_PAI", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtApePai;

	public Date getDtApePai() {
		return dtApePai;
	}

	public static final Character STATO_APERTO = 'A', STATO_CHIUSO = 'C', STATO_SOSPESO = 'S', STATO_RIFIUTATO = 'R';
	@Basic(optional = false)
	@Column(name = "FLG_STATO_PAI", nullable = false)
	private char flgStatoPai;

	public void setFlgStatoPai(char flgStatoPai) {
		this.flgStatoPai = flgStatoPai;
	}

	public void setDtChiusPai(Date dtChiusPai) {
		this.dtChiusPai = dtChiusPai;
	}

	public CartellaSociale getCodAna() {
		return codAna;
	}

	@Column(name = "FLG_CSR")
	private Character flgCsr;

	@Column(name = "DT_INVIO_CSR")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInvioCsr;

	@Column(name = "FLG_MOD_CSR")
	private Character flgModCsr;

	@Column(name = "DT_CHIUS_PAI")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtChiusPai;

	// never read . . wtf?
	@Column(name = "MOTIV_CHIUS", length = 765)
	private String motivChius;

	@Column(name = "NUM_PG", length = 10)
	private String numPg;

	@Column(name = "DT_PG")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtPg;

	@Column(name = "NUM_NUC")
	private Integer numNuc;

	@Column(name = "NUM_FIGLI")
	private Integer numFigli;

	@Column(name = "NUM_FIGLI_CONV")
	private Integer numFigliConv;

	@Column(name = "PROVV_GIUDIZ", length = 765)
	private String provvGiudiz;

	@Column(name = "NOTE_COND_FAM", length = 3000)
	private String noteCondFam;

	@Column(name = "FLG_AGE_SAN")
	private Character flgAgeSan;

	@Column(name = "FLG_CAR_SA")
	private Character flgCarSa;

	@Column(name = "FLG_CAR_SS")
	private Character flgCarSs;

	@Column(name = "FLG_CAR_VOL")
	private Character flgCarVol;

	@Column(name = "FLG_CAR_PARR")
	private Character flgCarParr;

	@Column(name = "FLG_CAR_ALTRO")
	private Character flgCarAltro;

	@Column(name = "CAR_ALTRO", length = 300)
	private String carAltro;

	@Column(name = "DT_DIAG")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtDiag;

	@Column(name = "MOTIV", length = 765)
	private String motiv;

	@Column(name = "ISEE", precision = 9, scale = 2)
	private BigDecimal isee;

	@Column(name = "ISEE2", precision = 9, scale = 2)
	private BigDecimal isee2;

	@Column(name = "ISEE3", precision = 9, scale = 2)
	private BigDecimal isee3;

	@Column(name = "DT_SCAD_ISEE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtScadIsee;

	@Column(name = "FLG_DEMENZA")
	private Character flgDemenza;

	@Column(name = "ADL")
	private Short adl;
	@Column(name = "HABITAT")
	private Character habitat;

	@OneToMany(mappedBy = "codPai")
	private List<PaiDocumento> paiDocumentoList;

	public List<PaiDocumento> getPaiDocumentoList() {
		return paiDocumentoList;
	}

	@JoinColumn(name = "COD_UTE_AS", referencedColumnName = "COD_UTE")
	@ManyToOne
	private Utenti codUteAs;

	public Utenti getCodUteAs() {
		return codUteAs;
	}

	@JoinColumn(name = "ID_PARAM_CERTIFICATO_L104", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamCertificatoL104;

	@JoinColumn(name = "ID_PARAM_PROVV_GIUDIZIARIO", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamProvvedimentoGiudiziario;

	@JoinColumn(name = "ID_PARAM_UOT", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamUot;

	public ParametriIndata getIdParamUot() {
		return idParamUot;
	}

	@JoinColumn(name = "ID_PARAM_FASCIA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamFascia;

	public ParametriIndata getIdParamFascia() {
		return idParamFascia;
	}

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private CartellaSociale codAna;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codPai")
	private List<UniqueTasklist> uniqueTasklistList;

	public List<UniqueTasklist> getUniqueTasklistList() {
		return uniqueTasklistList;
	}

	@OneToMany(mappedBy = "codPai")
	private List<LogAnagrafe> logAnagrafeList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codPai")
	private List<Appuntamento> appuntamentoList;

	public List<Appuntamento> getAppuntamentoList() {
		return appuntamentoList;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pai")
	private List<PaiIntervento> paiInterventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codPai")
	private List<PaiEvento> paiEventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pai")
	private List<PaiMacroProblematica> paiMacroProblematicaList;

	public void setCarAltro(String carAltro) {
		this.carAltro = carAltro;
	}

	public void setDtPg(Date dtPg) {
		this.dtPg = dtPg;
	}

	public void setDtDiag(Date dtDiag) {
		this.dtDiag = dtDiag;
	}

	public void setDtCambioFascia(Date dtCambioFascia) {
		this.dtCambioFascia = dtCambioFascia;
	}

	public void setMotivChius(String motivChius) {
		this.motivChius = motivChius;
	}

	public void setFlgCarSa(Character flgCarSa) {
		this.flgCarSa = flgCarSa;
	}

	public void setFlgCarSs(Character flgCarSs) {
		this.flgCarSs = flgCarSs;
	}

	public void setFlgCarParr(Character flgCarParr) {
		this.flgCarParr = flgCarParr;
	}

	public void setNumPg(String numPg) {
		this.numPg = numPg;
	}

	public void setNumFigli(Integer numFigli) {
		this.numFigli = numFigli;
	}

	public void setNumFigliConv(Integer numFigliConv) {
		this.numFigliConv = numFigliConv;
	}

	public void setFlgCarVol(Character flgCarVol) {
		this.flgCarVol = flgCarVol;
	}

	public void setMotiv(String motiv) {
		this.motiv = motiv;
	}

	public void setIsee3(BigDecimal isee3) {
		this.isee3 = isee3;
	}

	public void setFlgCarAltro(Character flgCarAltro) {
		this.flgCarAltro = flgCarAltro;
	}

	public void setIsee(BigDecimal isee) {
		this.isee = isee;
	}

	public void setIsee2(BigDecimal isee2) {
		this.isee2 = isee2;
	}

	public void setDtScadIsee(Date dtScadIsee) {
		this.dtScadIsee = dtScadIsee;
	}

	public void setHabitat(Character habitat) {
		this.habitat = habitat;
	}

	public void setCodAna(CartellaSociale codAna) {
		this.codAna = codAna;
	}

	public String getCarAltro() {
		return carAltro;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Character getStatoAperto() {
		return STATO_APERTO;
	}

	public static Character getStatoChiuso() {
		return STATO_CHIUSO;
	}

	public static Character getStatoSospeso() {
		return STATO_SOSPESO;
	}

	public static Character getStatoRifiutato() {
		return STATO_RIFIUTATO;
	}

	public Character getFlgCsr() {
		return flgCsr;
	}

	public Date getDtInvioCsr() {
		return dtInvioCsr;
	}

	public Character getFlgModCsr() {
		return flgModCsr;
	}

	public Date getDtChiusPai() {
		return dtChiusPai;
	}

	public String getMotivChius() {
		return motivChius;
	}

	public String getNumPg() {
		return numPg;
	}

	public Date getDtPg() {
		return dtPg;
	}

	public Integer getNumFigli() {
		return numFigli;
	}

	public Integer getNumFigliConv() {
		return numFigliConv;
	}

	public String getProvvGiudiz() {
		return provvGiudiz;
	}

	public String getNoteCondFam() {
		return noteCondFam;
	}

	public Character getFlgAgeSan() {
		return flgAgeSan;
	}

	public Character getFlgCarSa() {
		return flgCarSa;
	}

	public Character getFlgCarSs() {
		return flgCarSs;
	}

	public Character getFlgCarVol() {
		return flgCarVol;
	}

	public Character getFlgCarParr() {
		return flgCarParr;
	}

	public Character getFlgCarAltro() {
		return flgCarAltro;
	}

	public Date getDtDiag() {
		return dtDiag;
	}

	public BigDecimal getIsee() {
		return isee;
	}

	public BigDecimal getIsee2() {
		return isee2;
	}

	public BigDecimal getIsee3() {
		return isee3;
	}

	public Date getDtScadIsee() {
		return dtScadIsee;
	}

	public Character getFlgDemenza() {
		return flgDemenza;
	}

	public Character getHabitat() {
		return habitat;
	}

	public List<LogAnagrafe> getLogAnagrafeList() {
		return logAnagrafeList;
	}

	public List<PaiEvento> getPaiEventoList() {
		return paiEventoList;
	}

	public Date getDtCambioFascia() {
		return dtCambioFascia;
	}

	public Short getAdl() {
		return adl;
	}

	@Column(name = "DT_CAMBIO_FASCIA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCambioFascia;

	public Pai() {
	}

	public Pai(Integer codPai) {
		this.codPai = codPai;
	}

	public Pai(Integer codPai, Date dtApePai, char flgStatoPai) {
		this.codPai = codPai;
		this.dtApePai = dtApePai;
		this.flgStatoPai = flgStatoPai;
	}

	public List<PaiMacroProblematica> getPaiMacroProblematicaList() {
		if (paiMacroProblematicaList == null) {
			paiMacroProblematicaList = new ArrayList<PaiMacroProblematica>();
		}
		return paiMacroProblematicaList;
	}

	public void setPaiMacroProblematicaList(List<PaiMacroProblematica> paiMacroProblematicaList) {
		this.paiMacroProblematicaList = paiMacroProblematicaList;
	}

	public void setFasciaDefault(EntityManager em) {
		idParamFascia = em.getReference(ParametriIndata.class, Parametri.getIdFasciaDefault(em));
	}

	public ParametriIndata getIdParamProvvedimentoGiudiziario() {
		return idParamProvvedimentoGiudiziario;
	}

	public void setIdParamProvvedimentoGiudiziario(ParametriIndata idParamProvvedimentoGiudiziario) {
		this.idParamProvvedimentoGiudiziario = idParamProvvedimentoGiudiziario;
	}

	public void setCartellaSociale(CartellaSociale cartellaSociale) {
		this.codAna = cartellaSociale;
		this.rawCodAna = cartellaSociale.getCodAna();
	}

	public CartellaSociale getCartellaSociale() {
		return codAna;
	}

	public AnagrafeSoc getAnagrafeSoc() {
		return getCartellaSociale().getAnagrafeSoc();
	}

	public List<PaiIntervento> getPaiInterventoList() {
		if (paiInterventoList == null) {
			paiInterventoList = new ArrayList<PaiIntervento>();
		}
		return paiInterventoList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codPai != null ? codPai.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Pai)) {
			return false;
		}
		Pai other = (Pai) object;
		if ((this.codPai == null && other.codPai != null)
				|| (this.codPai != null && !this.codPai.equals(other.codPai))) {
			return false;
		}
		return true;
	}

	public static Predicate<UniqueTasklist> hasRichiestaApprovazioneAttivaPredicate() {

		final Set<String> codFormSet = new HashSet<String>(
				Arrays.asList(UniqueForm.COD_FORM_PROTOCOLLA_DOMANDA, UniqueForm.COD_FORM_VALIDA_INTERVENTI));
		return uniqueTasklist -> Objects.equals(uniqueTasklist.getFlgEseguito(), UniqueTasklist.FLG_ESEGUITO_NO)
				&& codFormSet.contains(uniqueTasklist.getForm().getCodForm());
	}

	public static List<UniqueTasklist> filterUniqueTasklist(List<UniqueTasklist> uniqueTaskLists, Predicate<UniqueTasklist> predicate) {
		return uniqueTaskLists.stream().filter(predicate).collect(Collectors.<UniqueTasklist>toList());
	}

    public boolean hasRichiestaApprovazioneAttiva() {
    	 return getUniqueTasklistList().stream().anyMatch(hasRichiestaApprovazioneAttivaPredicate());
    }
	
}
