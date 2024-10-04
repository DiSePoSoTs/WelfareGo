package it.wego.welfarego.persistence.entities;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;

/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "ANAGRAFE_SOC", uniqueConstraints = { @UniqueConstraint(columnNames = { "PART_IVA" }),
		@UniqueConstraint(columnNames = { "COD_FISC" }) })
@NamedQueries({ @NamedQuery(name = "AnagrafeSoc.findAll", query = "SELECT a FROM AnagrafeSoc a"),
		@NamedQuery(name = "AnagrafeSoc.findByCodAna", query = "SELECT a FROM AnagrafeSoc a WHERE a.codAna = :codAna"),
		@NamedQuery(name = "AnagrafeSoc.findByFlgPersFg", query = "SELECT a FROM AnagrafeSoc a WHERE a.flgPersFg = :flgPersFg"),
		@NamedQuery(name = "AnagrafeSoc.findByNome", query = "SELECT a FROM AnagrafeSoc a WHERE a.nome = :nome"),
		@NamedQuery(name = "AnagrafeSoc.findByCognome", query = "SELECT a FROM AnagrafeSoc a WHERE a.cognome = :cognome"),
		@NamedQuery(name = "AnagrafeSoc.findByCodFisc", query = "SELECT a FROM AnagrafeSoc a WHERE a.codFisc = :codFisc"),
		@NamedQuery(name = "AnagrafeSoc.findByPartIva", query = "SELECT a FROM AnagrafeSoc a WHERE a.partIva = :partIva"),
		@NamedQuery(name = "AnagrafeSoc.findByRagSoc", query = "SELECT a FROM AnagrafeSoc a WHERE a.ragSoc = :ragSoc"),
		@NamedQuery(name = "AnagrafeSoc.findByCodAnaCom", query = "SELECT a FROM AnagrafeSoc a WHERE a.codAnaCom = :codAnaCom"),
		@NamedQuery(name = "AnagrafeSoc.findByCodAnaFamCom", query = "SELECT a FROM AnagrafeSoc a WHERE a.codAnaFamCom = :codAnaFamCom"),
		@NamedQuery(name = "AnagrafeSoc.findByCodAnaCivilia", query = "SELECT a FROM AnagrafeSoc a WHERE a.codAnaCivilia = :codAnaCivilia"),
		@NamedQuery(name = "AnagrafeSoc.findByDtNasc", query = "SELECT a FROM AnagrafeSoc a WHERE a.dtNasc = :dtNasc"),
		@NamedQuery(name = "AnagrafeSoc.findByFlgSex", query = "SELECT a FROM AnagrafeSoc a WHERE a.flgSex = :flgSex"),
		@NamedQuery(name = "AnagrafeSoc.findByNumTel", query = "SELECT a FROM AnagrafeSoc a WHERE a.numTel = :numTel"),
		@NamedQuery(name = "AnagrafeSoc.findByNumCell", query = "SELECT a FROM AnagrafeSoc a WHERE a.numCell = :numCell"),
		@NamedQuery(name = "AnagrafeSoc.findByFlgSms", query = "SELECT a FROM AnagrafeSoc a WHERE a.flgSms = :flgSms"),
		@NamedQuery(name = "AnagrafeSoc.findByEmail", query = "SELECT a FROM AnagrafeSoc a WHERE a.email = :email"),
		@NamedQuery(name = "AnagrafeSoc.findByFlgEmail", query = "SELECT a FROM AnagrafeSoc a WHERE a.flgEmail = :flgEmail"),
		@NamedQuery(name = "AnagrafeSoc.findByNote", query = "SELECT a FROM AnagrafeSoc a WHERE a.note = :note"),
		@NamedQuery(name = "AnagrafeSoc.findByZona", query = "SELECT a FROM AnagrafeSoc a WHERE a.zona = :zona"),
		@NamedQuery(name = "AnagrafeSoc.findBySottozona", query = "SELECT a FROM AnagrafeSoc a WHERE a.sottozona = :sottozona"),
		@NamedQuery(name = "AnagrafeSoc.findByCodAss", query = "SELECT a FROM AnagrafeSoc a WHERE a.codAss = :codAss"),
		@NamedQuery(name = "AnagrafeSoc.findByDistSan", query = "SELECT a FROM AnagrafeSoc a WHERE a.distSan = :distSan"),
		@NamedQuery(name = "AnagrafeSoc.findByEnteGestore", query = "SELECT a FROM AnagrafeSoc a WHERE a.enteGestore = :enteGestore"),
		@NamedQuery(name = "AnagrafeSoc.findByMedicoBase", query = "SELECT a FROM AnagrafeSoc a WHERE a.medicoBase = :medicoBase"),
		@NamedQuery(name = "AnagrafeSoc.findByIbanPagam", query = "SELECT a FROM AnagrafeSoc a WHERE a.ibanPagam = :ibanPagam"),
		@NamedQuery(name = "AnagrafeSoc.findByReddMens", query = "SELECT a FROM AnagrafeSoc a WHERE a.reddMens = :reddMens"),
		@NamedQuery(name = "AnagrafeSoc.findByFlgAccomp", query = "SELECT a FROM AnagrafeSoc a WHERE a.flgAccomp = :flgAccomp"),
		@NamedQuery(name = "AnagrafeSoc.findByPresso", query = "SELECT a FROM AnagrafeSoc a WHERE a.presso = :presso"),
		@NamedQuery(name = "AnagrafeSoc.findByCognomeConiuge", query = "SELECT a FROM AnagrafeSoc a WHERE a.cognomeConiuge = :cognomeConiuge"),
		@NamedQuery(name = "AnagrafeSoc.findByDtMorte", query = "SELECT a FROM AnagrafeSoc a WHERE a.dtMorte = :dtMorte"),
		@NamedQuery(name = "AnagrafeSoc.findByPercInvCiv", query = "SELECT a FROM AnagrafeSoc a WHERE a.percInvCiv = :percInvCiv"),
		@NamedQuery(name = "AnagrafeSoc.findByDtAggAb", query = "SELECT a FROM AnagrafeSoc a WHERE a.dtAggAb = :dtAggAb") })
public class AnagrafeSoc implements Serializable {

	public static final Character RICHIESTA_ASSEGNO_ACCOMPAGNAMENTO_S = 'S', RICHIESTA_ASSEGNO_ACCOMPAGNAMENTO_N = 'N';

	public static final String PERSONA_FISICA_G = "G", PERSONA_FISICA_F = "F";

	public static final String FLG_SEX_F = "F", FLG_SEX_M = "M";

	private static final long serialVersionUID = 1L;

	public Integer getCodAna() {
		return codAna;
	}

	public Character getRichiestaAssegnoAccompagnamento() {
		return richiestaAssegnoAccompagnamento;
	}

	public BigDecimal getReddito() {
		return reddito;
	}

	public BigDecimal getReddMens() {
		return reddMens;
	}

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "anagrafeSocSequence")
	@SequenceGenerator(name = "anagrafeSocSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "COD_ANA", nullable = false)
	private Integer codAna;

	public Date getDataUpdateReddito() {
		return dataUpdateReddito;
	}

	public List<Mandato> getMandatoList() {
		return mandatoList;
	}

	public List<Mandato> getMandatoList1() {
		return mandatoList1;
	}

	public List<PaiCdg> getPaiCdgList() {
		return paiCdgList;
	}

	public String getCodAnaCivilia() {
		return codAnaCivilia;
	}

	public List<LogAnagrafe> getLogAnagrafeList() {
		return logAnagrafeList;
	}

	public List<PaiInterventoCivObb> getPaiInterventoCivObbList() {
		return paiInterventoCivObbList;
	}

	public List<PaiInterventoAnagrafica> getPaiInterventoAnagraficaList() {
		return paiInterventoAnagraficaList;
	}

	public List<Contatto> getContattoList() {
		return contattoList;
	}

	public Utenti getCreationUser() {
		return creationUser;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getEmail() {
		return email;
	}

	public String getSottozona() {
		return sottozona;
	}

	public Date getDataAggBatch() {
		return dataAggBatch;
	}

	public List<Fattura> getFatturaList() {
		return fatturaList;
	}

	public ParametriIndata getIdParamModPagam() {
		return idParamModPagam;
	}

	public String getIdCsr() {
		return idCsr;
	}

	public String getDiario() {
		return diario;
	}

	public Date getDataUltimaRetifica() {
		return dataUltimaRetifica;
	}

	public String getEnteGestore() {
		return enteGestore;
	}

	public String getFlgAccomp() {
		return flgAccomp;
	}

	public String getZona() {
		return zona;
	}

	public Short getPercInvCiv() {
		return percInvCiv;
	}

	public String getNote() {
		return note;
	}

	public String getNumTel() {
		return numTel;
	}

	public String getMedicoBase() {
		return medicoBase;
	}

	public ParametriIndata getIdParamStatoFis() {
		return idParamStatoFis;
	}

	public ParametriIndata getIdParamTipologiaResidenza() {
		return idParamTipologiaResidenza;
	}

	public ParametriIndata getIdParamTipAll() {
		return idParamTipAll;
	}

	public ParametriIndata getIdParamTit() {
		return idParamTit;
	}

	public char getFlgEmail() {
		return flgEmail;
	}

	public ParametriIndata getIdParamRedd() {
		return idParamRedd;
	}

	public ParametriIndata getIdParamStatoCiv() {
		return idParamStatoCiv;
	}

	public String getFlgSex() {
		return flgSex;
	}

	public char getFlgSms() {
		return flgSms;
	}

	public ParametriIndata getIdParamCondProf() {
		return idParamCondProf;
	}

	public ParametriIndata getIdParamPosAna() {
		return idParamPosAna;
	}

	public Date getDataRichiestaAssegnoAccompagnamento() {
		return dataRichiestaAssegnoAccompagnamento;
	}

	public String getDistSan() {
		return distSan;
	}

	public Date getDtAggAb() {
		return dtAggAb;
	}

	public ParametriIndata getCondFam() {
		return condFam;
	}

	public String getAttivitaLavoroStudio() {
		return attivitaLavoroStudio;
	}

	public String getCognomeConiuge() {
		return cognomeConiuge;
	}

	public Date getDtMorte() {
		return dtMorte;
	}

	public ParametriIndata getCodStatoNaz() {
		return codStatoNaz;
	}

	public Utenti getCodUteEducatore() {
		return codUteEducatore;
	}

	public String getCodAnaCom() {
		return codAnaCom;
	}

	public String getCodAss() {
		return codAss;
	}

	public ParametriIndata getCodSegnDa() {
		return codSegnDa;
	}

	public Stato getCodStatoCitt() {
		return codStatoCitt;
	}

	@Basic(optional = false)
	@Column(name = "FLG_PERS_FG", length = 1, nullable = false)
	private String flgPersFg = PERSONA_FISICA_F;

	public String getFlgPersFg() {
		return flgPersFg;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCodAna(Integer codAna) {
		this.codAna = codAna;
	}

	public void setFlgPersFg(String flgPersFg) {
		this.flgPersFg = flgPersFg;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public void setPartIva(String partIva) {
		this.partIva = partIva;
	}

	public void setRagSoc(String ragSoc) {
		this.ragSoc = ragSoc;
	}

	public void setCodAnaCom(String codAnaCom) {
		this.codAnaCom = codAnaCom;
	}

	public void setCodAnaFamCom(String codAnaFamCom) {
		this.codAnaFamCom = codAnaFamCom;
	}

	public void setCodAnaCivilia(String codAnaCivilia) {
		this.codAnaCivilia = codAnaCivilia;
	}

	public void setDtNasc(Date dtNasc) {
		this.dtNasc = dtNasc;
	}

	public void setDataRichiestaAssegnoAccompagnamento(Date dataRichiestaAssegnoAccompagnamento) {
		this.dataRichiestaAssegnoAccompagnamento = dataRichiestaAssegnoAccompagnamento;
	}

	public void setRichiestaAssegnoAccompagnamento(Character richiestaAssegnoAccompagnamento) {
		this.richiestaAssegnoAccompagnamento = richiestaAssegnoAccompagnamento;
	}

	public void setDataUpdateReddito(Date dataUpdateReddito) {
		this.dataUpdateReddito = dataUpdateReddito;
	}

	public void setReddito(BigDecimal reddito) {
		this.reddito = reddito;
	}

	public void setFlgSex(String flgSex) {
		this.flgSex = flgSex;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	public void setNumCell(String numCell) {
		this.numCell = numCell;
	}

	public void setFlgSms(char flgSms) {
		this.flgSms = flgSms;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFlgEmail(char flgEmail) {
		this.flgEmail = flgEmail;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setAttivitaLavoroStudio(String attivitaLavoroStudio) {
		this.attivitaLavoroStudio = attivitaLavoroStudio;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public void setSottozona(String sottozona) {
		this.sottozona = sottozona;
	}

	public void setCodAss(String codAss) {
		this.codAss = codAss;
	}

	public void setDistSan(String distSan) {
		this.distSan = distSan;
	}

	public void setEnteGestore(String enteGestore) {
		this.enteGestore = enteGestore;
	}

	public void setMedicoBase(String medicoBase) {
		this.medicoBase = medicoBase;
	}

	public void setPresso(String presso) {
		this.presso = presso;
	}

	public void setIbanPagam(String ibanPagam) {
		this.ibanPagam = ibanPagam;
	}

	public void setReddMens(BigDecimal reddMens) {
		this.reddMens = reddMens;
	}

	public void setFlgAccomp(String flgAccomp) {
		this.flgAccomp = flgAccomp;
	}

	public void setCognomeConiuge(String cognomeConiuge) {
		this.cognomeConiuge = cognomeConiuge;
	}

	public void setDtMorte(Date dtMorte) {
		this.dtMorte = dtMorte;
	}

	public void setPercInvCiv(Short percInvCiv) {
		this.percInvCiv = percInvCiv;
	}

	public void setDtAggAb(Date dtAggAb) {
		this.dtAggAb = dtAggAb;
	}

	public void setDataAggBatch(Date dataAggBatch) {
		this.dataAggBatch = dataAggBatch;
	}

	public void setFatturaList(List<Fattura> fatturaList) {
		this.fatturaList = fatturaList;
	}

	public void setMandatoList(List<Mandato> mandatoList) {
		this.mandatoList = mandatoList;
	}

	public void setMandatoList1(List<Mandato> mandatoList1) {
		this.mandatoList1 = mandatoList1;
	}

	public void setCartellaSociale(CartellaSociale cartellaSociale) {
		this.cartellaSociale = cartellaSociale;
	}

	public void setPaiCdgList(List<PaiCdg> paiCdgList) {
		this.paiCdgList = paiCdgList;
	}

	public void setLogAnagrafeList(List<LogAnagrafe> logAnagrafeList) {
		this.logAnagrafeList = logAnagrafeList;
	}

	public void setPaiInterventoCivObbList(List<PaiInterventoCivObb> paiInterventoCivObbList) {
		this.paiInterventoCivObbList = paiInterventoCivObbList;
	}

	public void setPaiInterventoAnagraficaList(List<PaiInterventoAnagrafica> paiInterventoAnagraficaList) {
		this.paiInterventoAnagraficaList = paiInterventoAnagraficaList;
	}

	public void setAnagrafeFamListAsSource(List<AnagrafeFam> anagrafeFamListAsSource) {
		this.anagrafeFamListAsSource = anagrafeFamListAsSource;
	}

	public void setAnagrafeFamListAsTarget(List<AnagrafeFam> anagrafeFamListAsTarget) {
		this.anagrafeFamListAsTarget = anagrafeFamListAsTarget;
	}

	public void setContattoList(List<Contatto> contattoList) {
		this.contattoList = contattoList;
	}

	public void setIdParamStatoFis(ParametriIndata idParamStatoFis) {
		this.idParamStatoFis = idParamStatoFis;
	}

	public void setIdParamTipologiaResidenza(ParametriIndata idParamTipologiaResidenza) {
		this.idParamTipologiaResidenza = idParamTipologiaResidenza;
	}

	public void setCodSegnDa(ParametriIndata codSegnDa) {
		this.codSegnDa = codSegnDa;
	}

	public void setCondFam(ParametriIndata condFam) {
		this.condFam = condFam;
	}

	public void setCodStatoNaz(ParametriIndata codStatoNaz) {
		this.codStatoNaz = codStatoNaz;
	}

	public void setIdParamRedd(ParametriIndata idParamRedd) {
		this.idParamRedd = idParamRedd;
	}

	public void setIdParamCondProf(ParametriIndata idParamCondProf) {
		this.idParamCondProf = idParamCondProf;
	}

	public void setIdParamStatoCiv(ParametriIndata idParamStatoCiv) {
		this.idParamStatoCiv = idParamStatoCiv;
	}

	public void setIdParamModPagam(ParametriIndata idParamModPagam) {
		this.idParamModPagam = idParamModPagam;
	}

	public void setIdParamTipAll(ParametriIndata idParamTipAll) {
		this.idParamTipAll = idParamTipAll;
	}

	public void setIdParamPosAna(ParametriIndata idParamPosAna) {
		this.idParamPosAna = idParamPosAna;
	}

	public void setIdParamTit(ParametriIndata idParamTit) {
		this.idParamTit = idParamTit;
	}

	public void setLuogoNascita(Luogo luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public void setLuogoDomicilio(Luogo luogoDomicilio) {
		this.luogoDomicilio = luogoDomicilio;
	}

	public void setLuogoResidenza(Luogo luogoResidenza) {
		this.luogoResidenza = luogoResidenza;
	}

	public void setIdCsr(String idCsr) {
		this.idCsr = idCsr;
	}

	public void setCodUteEducatore(Utenti codUteEducatore) {
		this.codUteEducatore = codUteEducatore;
	}

	public void setDiario(String diario) {
		this.diario = diario;
	}

	public void setCreationUser(Utenti creationUser) {
		this.creationUser = creationUser;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setDataUltimaRetifica(Date dataUltimaRetifica) {
		this.dataUltimaRetifica = dataUltimaRetifica;
	}

	@Column(name = "NOME", length = 765)
	private String nome;

	@Column(name = "COGNOME", length = 765)
	private String cognome;

	@Column(name = "COD_FISC", length = 16)
	private String codFisc;

	public String getCodFisc() {
		return codFisc;
	}

	public String getPartIva() {
		return partIva;
	}

	@Column(name = "PART_IVA", length = 11)
	private String partIva;

	@Column(name = "RAG_SOC", length = 765)
	private String ragSoc;

	public String getRagSoc() {
		return ragSoc;
	}

	@Column(name = "COD_ANA_COM", length = 10)
	private String codAnaCom;

	@Column(name = "COD_ANA_FAM_COM", length = 10)
	private String codAnaFamCom;

	public String getCodAnaFamCom() {
		return codAnaFamCom;
	}

	@Column(name = "COD_ANA_CIVILIA", length = 10)
	private String codAnaCivilia;

	public Date getDtNasc() {
		return dtNasc;
	}

	@Column(name = "DT_NASC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtNasc;

	@Column(name = "DT_RIC_ASS_ACC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRichiestaAssegnoAccompagnamento;

	@Column(name = "FLG_RIC_ASS_ACC")
	private Character richiestaAssegnoAccompagnamento;

	@Column(name = "DT_UPDATE_REDDITO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUpdateReddito;

	@Column(name = "REDDITO")
	private BigDecimal reddito;

	@Column(name = "FLG_SEX")
	private String flgSex;

	@Column(name = "NUM_TEL", length = 20)
	private String numTel;

	@Column(name = "NUM_CELL", length = 60)
	private String numCell;

	@Basic(optional = false)
	@Column(name = "FLG_SMS", nullable = false)
	private char flgSms;

	@Column(name = "EMAIL", length = 200)
	private String email;

	@Basic(optional = false)
	@Column(name = "FLG_EMAIL", nullable = false)
	private char flgEmail;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@Column(name = "ATTIVITA_PRINCIPALE", length = 3000)
	private String attivitaLavoroStudio;

	@Column(name = "ZONA", length = 1)
	private String zona;

	@Column(name = "SOTTOZONA", length = 2)
	private String sottozona;

	@Column(name = "COD_ASS", length = 2)
	private String codAss;

	@Column(name = "DIST_SAN", length = 1)
	private String distSan;

	@Column(name = "ENTE_GESTORE", length = 20)
	private String enteGestore;

	@Column(name = "MEDICO_BASE", length = 300)
	private String medicoBase;

	@Column(name = "PRESSO", length = 40)
	private String presso;

	public String getPresso() {
		return presso;
	}

	@Column(name = "IBAN_PAGAM", length = 30)
	private String ibanPagam;

	public String getIbanPagam() {
		return ibanPagam;
	}

	@Column(name = "REDD_MENS", precision = 9, scale = 2)
	private BigDecimal reddMens;

	@Column(name = "FLG_ACCOMP")
	private String flgAccomp;

	@Column(name = "COGNOME_CONIUGE", length = 765)
	private String cognomeConiuge;

	@Column(name = "DT_MORTE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtMorte;

	@Column(name = "PERC_INV_CIV")
	private Short percInvCiv;

	public String getNumCell() {
		return numCell;
	}

	@Column(name = "DT_AGG_AB")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAggAb;

	@Column(name = "DATA_AGG_BATCH")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAggBatch;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codAna")
	private List<Fattura> fatturaList;

	@OneToMany(mappedBy = "codAnaDelegante")
	private List<Mandato> mandatoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codAnaBeneficiario")
	private List<Mandato> mandatoList1;

	public CartellaSociale getCartellaSociale() {
		return cartellaSociale;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "anagrafeSoc")
	private CartellaSociale cartellaSociale;

	@OneToMany(mappedBy = "codAna")
	private List<PaiCdg> paiCdgList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codAna")
	private List<LogAnagrafe> logAnagrafeList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "anagrafeSoc")
	private List<PaiInterventoCivObb> paiInterventoCivObbList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "anagrafeSoc")
	private List<PaiInterventoAnagrafica> paiInterventoAnagraficaList;

	public List<AnagrafeFam> getAnagrafeFamListAsSource() {
		return anagrafeFamListAsSource;
	}

	public List<AnagrafeFam> getAnagrafeFamListAsTarget() {
		return anagrafeFamListAsTarget;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "anagrafeSocSource")
	private List<AnagrafeFam> anagrafeFamListAsSource;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "anagrafeSocTarget")
	private List<AnagrafeFam> anagrafeFamListAsTarget;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codAna")
	private List<Contatto> contattoList;

	@JoinColumn(name = "ID_PARAM_STATO_FIS", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamStatoFis;

	@JoinColumn(name = "ID_PARAM_TIPOLOGIA_RESIDENZA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamTipologiaResidenza; // tipParam : tr

	@JoinColumn(name = "COD_SEGN_DA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata codSegnDa;

	@JoinColumn(name = "COND_FAM", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata condFam;

	@JoinColumn(name = "COD_STATO_NAZ", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata codStatoNaz;

	@JoinColumn(name = "ID_PARAM_REDD", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamRedd;

	@JoinColumn(name = "ID_PARAM_COND_PROF", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamCondProf;

	@JoinColumn(name = "ID_PARAM_STATO_CIV", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamStatoCiv;

	@JoinColumn(name = "ID_PARAM_MOD_PAGAM", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamModPagam;

	@JoinColumn(name = "ID_PARAM_TIP_ALL", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamTipAll;

	@JoinColumn(name = "ID_PARAM_POS_ANA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamPosAna;

	@JoinColumn(name = "ID_PARAM_TIT", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamTit;

	public void setCodStatoCitt(Stato codStatoCitt) {
		this.codStatoCitt = codStatoCitt;
	}

	@JoinColumn(name = "COD_STATO_CITT", referencedColumnName = "COD_STATO")
	@ManyToOne
	private Stato codStatoCitt;

	@JoinColumn(name = "COD_LUOGO_NASC", referencedColumnName = "COD_LUOGO", nullable = false)
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinFetch(value = JoinFetchType.INNER)
	private Luogo luogoNascita = new Luogo();

	public Luogo getLuogoDomicilio() {
		return luogoDomicilio;
	}

	public Luogo getLuogoNascita() {
		return luogoNascita;
	}

	@JoinColumn(name = "COD_LUOGO_DOM", referencedColumnName = "COD_LUOGO", nullable = false)
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinFetch(value = JoinFetchType.INNER)
	private Luogo luogoDomicilio = new Luogo();

	public Luogo getLuogoResidenza() {
		return luogoResidenza;
	}

	@JoinColumn(name = "COD_LUOGO_RES", referencedColumnName = "COD_LUOGO", nullable = false)
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinFetch(value = JoinFetchType.INNER)
	private Luogo luogoResidenza = new Luogo();

	@Column(name = "CSR_ID", length = 255)
	private String idCsr;

	@JoinColumn(name = "COD_UTE_EDUCATORE", referencedColumnName = "COD_UTE")
	@ManyToOne
	private Utenti codUteEducatore;

	@Column(name = "diario", columnDefinition = "CLOB")
	@Lob
	private String diario;

	@JoinColumn(name = "CREATION_USER", referencedColumnName = "COD_UTE")
	@ManyToOne
	private Utenti creationUser;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "DATA_ULTIMA_RETIFICA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaRetifica;

	public AnagrafeSoc() {
	}

	public AnagrafeSoc(Integer codAna) {
		this.codAna = codAna;
	}

	public AnagrafeSoc(Integer codAna, String flgPersFg, char flgSms, char flgEmail) {
		this.codAna = codAna;
		this.flgPersFg = flgPersFg;
		this.flgSms = flgSms;
		this.flgEmail = flgEmail;
	}

	public int getEta() {
		Calendar dr = Calendar.getInstance();
		Calendar dn = Calendar.getInstance();
		dr.setTime(new Date());
		dn.setTime(getDtNasc());
		int eta = dr.get(Calendar.YEAR) - dn.get(Calendar.YEAR);
		if (dr.get(Calendar.DAY_OF_YEAR) <= dn.get(Calendar.DAY_OF_YEAR)) {
			eta--;
		}
		return eta;
	}

	public Luogo getLuogoDestinazione() {
		return getLuogoDomicilio() == null || getLuogoDomicilio().isEmpty() ? getLuogoResidenza() : getLuogoDomicilio();
	}

	public List<AnagrafeFam> getAnagrafeFamListAsAny() {
		return Lists.newArrayList(Iterables.concat(getAnagrafeFamListAsSource(), getAnagrafeFamListAsTarget()));
	}

	public List<AnagrafeSoc> getAnagrafeSocListFromAnagrafeFamList() {
		return Lists.newArrayList(Iterables.concat(
				Iterables.transform(getAnagrafeFamListAsSource(), AnagrafeFamToAnagrafeSocTargetFunction.INSTANCE),
				Iterables.transform(getAnagrafeFamListAsTarget(), AnagrafeFamToAnagrafeSocSourceFunction.INSTANCE)));
	}

	public List<Pai> getPaiList() {
		return getCartellaSociale() == null ? Collections.<Pai>emptyList() : getCartellaSociale().getPaiList();
	}

	public enum AnagrafeFamToAnagrafeSocSourceFunction
			implements com.google.common.base.Function<AnagrafeFam, AnagrafeSoc> {

		INSTANCE;

		public AnagrafeSoc apply(AnagrafeFam input) {
			return input.getAnagrafeSocSource();
		}
	}

	public enum AnagrafeFamToAnagrafeSocTargetFunction
			implements com.google.common.base.Function<AnagrafeFam, AnagrafeSoc> {

		INSTANCE;

		public AnagrafeSoc apply(AnagrafeFam input) {
			return input.getAnagrafeSocTarget();
		}
	}

	public void setStatoCittadinanza(Stato codStatoCitt) {
		setCodStatoCitt(codStatoCitt);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codAna != null ? codAna.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AnagrafeSoc)) {
			return false;
		}
		AnagrafeSoc other = (AnagrafeSoc) object;
		if ((this.codAna == null && other.codAna != null)
				|| (this.codAna != null && !this.codAna.equals(other.codAna))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.AnagrafeSoc[codAna=" + codAna + "]";
	}

	public String getCognomeNome() {
		return getCognome() + " " + getNome();
	}

}
