/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "TIPOLOGIA_INTERVENTO")
@NamedQueries({ @NamedQuery(name = "TipologiaIntervento.findAll", query = "SELECT t FROM TipologiaIntervento t"),
		@NamedQuery(name = "TipologiaIntervento.findByCodTipint", query = "SELECT t FROM TipologiaIntervento t WHERE t.codTipint = :codTipint"),
		@NamedQuery(name = "TipologiaIntervento.findByDesTipint", query = "SELECT t FROM TipologiaIntervento t WHERE t.desTipint = :desTipint"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgPai", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgPai = :flgPai"),
		@NamedQuery(name = "TipologiaIntervento.findByCcele", query = "SELECT t FROM TipologiaIntervento t WHERE t.ccele = :ccele"),
		@NamedQuery(name = "TipologiaIntervento.findByImpStdCosto", query = "SELECT t FROM TipologiaIntervento t WHERE t.impStdCosto = :impStdCosto"),
		@NamedQuery(name = "TipologiaIntervento.findByCodProcFo", query = "SELECT t FROM TipologiaIntervento t WHERE t.codProcFo = :codProcFo"),
		@NamedQuery(name = "TipologiaIntervento.findByCodProcEse", query = "SELECT t FROM TipologiaIntervento t WHERE t.codProcEse = :codProcEse"),
		@NamedQuery(name = "TipologiaIntervento.findByCodProcGest", query = "SELECT t FROM TipologiaIntervento t WHERE t.codProcGest = :codProcGest"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgFatt", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgFatt = :flgFatt"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgPagam", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgPagam = :flgPagam"),
		@NamedQuery(name = "TipologiaIntervento.findByCodIntCsr", query = "SELECT t FROM TipologiaIntervento t WHERE t.codIntCsr = :codIntCsr"),
		@NamedQuery(name = "TipologiaIntervento.findByCodTmplComliq", query = "SELECT t FROM TipologiaIntervento t WHERE t.codTmplComliq = :codTmplComliq"),
		@NamedQuery(name = "TipologiaIntervento.findByCodTipintCsr", query = "SELECT t FROM TipologiaIntervento t WHERE t.codTipintCsr = :codTipintCsr"),
		@NamedQuery(name = "TipologiaIntervento.findByImpStdEntr", query = "SELECT t FROM TipologiaIntervento t WHERE t.impStdEntr = :impStdEntr"),
		@NamedQuery(name = "TipologiaIntervento.findByImpStdSpesa", query = "SELECT t FROM TipologiaIntervento t WHERE t.impStdSpesa = :impStdSpesa"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgRdbfap", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgRdbfap = :flgRdbfap"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgAttivo", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgAttivo = :flgAttivo"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgVis", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgVis = :flgVis"),
		@NamedQuery(name = "TipologiaIntervento.findByFlgFineDurata", query = "SELECT t FROM TipologiaIntervento t WHERE t.flgFineDurata = :flgFineDurata") })
public class TipologiaIntervento implements Serializable {

	public static final String CLASSE_TIPOLOGIA_INTERVENTI_ECONOMICI = "07";

	public static final char FLG_FINE_DURATA_F = 'F', FLG_FINE_DURATA_D = 'D';

	public static final char FLG_PAGAMENTO_S = 'S', FLG_PAGAMENTO_N = 'N';

	public static final char FLG_FATTURA_S = 'S', FLG_FATTURA_N = 'N';

	public static final String FLG_RICEVUTA_S = "S", FLG_RICEVUTA_N = "N";

	public static final String FLG_RINNOVO_AUTOMATICO_S = "S", FLG_RINNOVO_AUTOMATICO_N = "N",
			FLG_RINNOVO_AUTOMATICO_PROROGA = "P", FLG_RINNOVO_AUTOMATICO_DETERMINA = "D",
			FLGRINNOVO_AUTOMATICO_BUDGET_PRECEDENTE = "B";

	public static final String COD_TIPINT_FAP_SVI = "AZ009", COD_TIPINT_FAP_APA = "AZ008",
			COD_TIPINT_FAP_APAANZIANI = "AZ008A", COD_TIPINT_FAP_APADISABILI = "AZ008B", COD_TIPINT_FAP_CAF = "AZ007",
			COD_TIPINT_FAP_CAFANZIANI = "AZ007A", COD_TIPINT_FAP_CAFDISABILI = "AZ007B",
			COD_TIPINT_FAP_SOSTEGNO = "AZ010", COD_TIPINT_FAP_DOMICILIARITAINNOVATIVA = "AZ101";

	public static final String COD_TIPINT_FONDO_SOLIDARIETA = "AD013", COD_TIPINT_SAD = "AZ014";

	public static final Set<String> codTipintFapSet = Collections.unmodifiableSet(Sets.newHashSet(COD_TIPINT_FAP_SVI,
			COD_TIPINT_FAP_APA, COD_TIPINT_FAP_CAF, COD_TIPINT_FAP_APAANZIANI, COD_TIPINT_FAP_APADISABILI,
			COD_TIPINT_FAP_CAFANZIANI, COD_TIPINT_FAP_CAFDISABILI, COD_TIPINT_FAP_SOSTEGNO, COD_TIPINT_FAP_DOMICILIARITAINNOVATIVA));

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	public String getCodTipint() {
		return codTipint;
	}


	@Basic(optional = false)
	@Column(name = "DES_TIPINT", nullable = false, length = 765)
	private String desTipint;

	public String getDesTipint() {
		return desTipint;
	}


	@Basic(optional = false)
	@Column(name = "FLG_PAI", nullable = false)
	private char flgPai;

	@Basic(optional = false)
	@Column(name = "CCELE", nullable = false, length = 10)
	private String ccele;

	@Basic(optional = false)
	@Column(name = "IMP_STD_COSTO", nullable = false, precision = 9, scale = 2)
	private BigDecimal impStdCosto;

	public BigDecimal getImpStdCosto() {
		return impStdCosto;
	}

	@Column(name = "COD_PROC_FO", length = 10)
	private String codProcFo;

	@Column(name = "COD_PROC_ESE", length = 10)
	private String codProcEse;

	@Column(name = "COD_PROC_GEST", length = 10)
	private String codProcGest;

	@Basic(optional = false)
	@Column(name = "FLG_FATT", nullable = false)
	private char flgFatt;

	public char getFlgFatt() {
		return flgFatt;
	}


	@Basic(optional = false)
	@Column(name = "FLG_PAGAM", nullable = false)
	private char flgPagam;

	public char getFlgPagam() {
		return flgPagam;
	}


	@Column(name = "COD_INT_CSR", length = 20)
	private String codIntCsr;

	@Column(name = "CONTATORE_RICEVUTA", nullable = false)
	private Integer contatoreRicevuta = 0;

	@Column(name = "COD_TIPINT_CSR", length = 10)
	private String codTipintCsr;

	@Basic(optional = false)
	@Column(name = "IMP_STD_ENTR", nullable = false, precision = 9, scale = 2)
	private BigDecimal impStdEntr;

	public BigDecimal getImpStdEntr() {
		return impStdEntr;
	}

	@Basic(optional = false)
	@Column(name = "IMP_STD_SPESA", nullable = false, precision = 9, scale = 2)
	private BigDecimal impStdSpesa;

	@Column(name = "FLG_RDBFAP")
	private Character flgRdbfap;

	@Basic(optional = false)
	@Column(name = "FLG_ATTIVO", nullable = false)
	private char flgAttivo;

	@Basic(optional = false)
	@Column(name = "FLG_VIS", nullable = false)
	private char flgVis;

	@Column(name = "FLG_RINNOVO_AUTO", nullable = false)
	private String deveRestareAperto = FLG_RINNOVO_AUTOMATICO_N;

	public String getDeveRestareAperto() {
		return deveRestareAperto;
	}

	@Column(name = "FLG_RINNOVO", nullable = false)
	private String flgRinnovo = FLG_RINNOVO_AUTOMATICO_N;

	public String getFlgRinnovo() {
		return flgRinnovo;
	}

	/**
	 * valori accettabili: {'F','D'}
	 */
	@Basic(optional = false)
	@Column(name = "FLG_FINE_DURATA", nullable = false)
	private char flgFineDurata;

	public char getFlgFineDurata() {
		return flgFineDurata;
	}


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipologiaIntervento")
	private List<BudgetTipIntervento> budgetTipInterventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTipint")
	private List<FatturaDettaglio> fatturaDettaglioList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipologiaIntervento")
	private List<PaiIntervento> paiInterventoList;

	@JoinColumn(name = "COD_LISTA_ATT", referencedColumnName = "COD_LISTA_ATT")
	@ManyToOne
	private ListaAttesa codListaAtt;

	@JoinColumn(name = "ID_PARAM_GRUTE", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamGrute;

	/**
	 * tip param 'cs'
	 */
	@JoinColumn(name = "ID_PARAM_SRV", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamSrv;

	@JoinColumn(name = "ID_PARAM_UNI_MIS", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamUniMis;

	public ParametriIndata getIdParamUniMis() {
		return idParamUniMis;
	}


	/**
	 * tip param 'ci'
	 */
	@JoinColumn(name = "ID_PARAM_CLASSE_TIPINT", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamClasseTipint;

	public ParametriIndata getIdParamClasseTipint() {
		return idParamClasseTipint;
	}


	/**
	 * tip param 'sa'
	 */
	@JoinColumn(name = "ID_PARAM_STRUTTURA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamStruttura;

	@JoinColumn(name = "COD_TMPL_LETT_PAG", referencedColumnName = "COD_TMPL")
	@ManyToOne
	private Template codTmplLettPag;

	public Template getCodTmplLettPag() {
		return codTmplLettPag;
	}


	@JoinColumn(name = "COD_TMPL_CHIUS", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplChius;

	public Template getCodTmplChius() {
		return codTmplChius;
	}


	@JoinColumn(name = "COD_TMPL_VAR", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplVar;

	@JoinColumn(name = "COD_TMPL_ESE", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplEse;

	@JoinColumn(name = "COD_TMPL_ESE_MUL", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplEseMul;

	@JoinColumn(name = "COD_TMPL_VAR_MUL", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplVarMul;

	@JoinColumn(name = "COD_TMPL_CHIUS_MUL", referencedColumnName = "COD_TMPL", nullable = false)
	@ManyToOne(optional = false)
	private Template codTmplChiusMul;

	@JoinColumn(name = "COD_TMPL_RICEVUTA", referencedColumnName = "COD_TMPL")
	@ManyToOne(optional = false)
	private Template codTmplRicevuta;

	@JoinColumn(name = "COD_TMPL_COMLIQ", referencedColumnName = "COD_TMPL")
	@ManyToOne(optional = false)
	private Template codTmplComliq;

	public Template getCodTmplComliq() {
		return codTmplComliq;
	}


	@Basic(optional = false)
	@Column(name = "FLG_RICEVUTA", nullable = false)
	private String flgRicevuta;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipologiaIntervento")
	private List<MapDatiSpecTipint> mapDatiSpecTipintList;

	public String getResponsabileProcedimento() {
		return responsabileProcedimento;
	}

	public String getUfficioDiRiferimento() {
		return ufficioDiRiferimento;
	}


	@Column(name = "RESP_PROC")
	private String responsabileProcedimento;

	@Column(name = "UFF_RIF")
	private String ufficioDiRiferimento;

	@Column(name = "FLG_APP_TEC")
	private char flgAppTec;

	@JoinColumn(name = "IP_ALIQUOTA_IVA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata ipAliquotaIva;

	public ParametriIndata getIpAliquotaIva() {
		return ipAliquotaIva;
	}


	@Column(name = "FLG_DOC_AUTORIZZAZIONE", nullable = false)
	private char flgDocumentoDiAutorizzazione = 'N';

	@JoinColumn(name = "COD_TMPL_DOC_AUTORIZZAZIONE", referencedColumnName = "COD_TMPL")
	@ManyToOne(optional = false)
	private Template tmplDocumentoDiAutorizzazione;

	public TipologiaIntervento() {
	}

	public TipologiaIntervento(String codTipint) {
		this.codTipint = codTipint;
	}

	public TipologiaIntervento(String codTipint, String desTipint, char flgPai, String ccele, BigDecimal impStdCosto,
			char flgFatt, char flgPagam, BigDecimal impStdEntr, BigDecimal impStdSpesa, char flgAttivo, char flgVis,
			char flgFineDurata) {
		this.codTipint = codTipint;
		this.desTipint = desTipint;
		this.flgPai = flgPai;
		this.ccele = ccele;
		this.impStdCosto = impStdCosto;
		this.flgFatt = flgFatt;
		this.flgPagam = flgPagam;
		this.impStdEntr = impStdEntr;
		this.impStdSpesa = impStdSpesa;
		this.flgAttivo = flgAttivo;
		this.flgVis = flgVis;
		this.flgFineDurata = flgFineDurata;
	}

	/**
	 * Questo metodo torna se l'intervento deve essere chiuso automaticamente o
	 * meno. Usiamo il vecchio flgRinnovoAutomatico che prima non serviva a nulla.
	 * Se si l'intervento non verra chiuso se no l'intervento verra chiuso
	 * automaticamente
	 * 
	 * @return
	 */
	public boolean deveRestareAperto() {
		return Objects.equal(getDeveRestareAperto(), FLG_RINNOVO_AUTOMATICO_S);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codTipint != null ? codTipint.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TipologiaIntervento)) {
			return false;
		}
		TipologiaIntervento other = (TipologiaIntervento) object;
		if ((this.codTipint == null && other.codTipint != null)
				|| (this.codTipint != null && !this.codTipint.equals(other.codTipint))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TipologiaIntervento[codTipint=" + codTipint + "]";
	}

	public boolean isFap() {
		return codTipintFapSet.contains(getCodTipint());
	}

	public boolean isFondoSolidarieta() {
		return Objects.equal(getCodTipint(), COD_TIPINT_FONDO_SOLIDARIETA);
	}

	public boolean isFapApa() {
		if (getCodTipint().equals(COD_TIPINT_FAP_APA) || getCodTipint().equals(COD_TIPINT_FAP_APAANZIANI)
				|| getCodTipint().equals(COD_TIPINT_FAP_APADISABILI)) {
			return true;
		}
		return false;
	}

	public boolean isFapCaf() {
		if (getCodTipint().equals(COD_TIPINT_FAP_CAF) || getCodTipint().equals(COD_TIPINT_FAP_CAFANZIANI)
				|| getCodTipint().equals(COD_TIPINT_FAP_CAFDISABILI)) {
			return true;
		}
		return false;
	}

	public boolean isFapSvi() {
		if (getCodTipint().equals(COD_TIPINT_FAP_SVI) || getCodTipint().equals(COD_TIPINT_FAP_SOSTEGNO)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFapDomiciliarita() {
		if (getCodTipint().equals(COD_TIPINT_FAP_DOMICILIARITAINNOVATIVA)) {
			return true;
		} else {
			return false;
		}
	}
	

	public boolean isSad() {
		return Objects.equal(getCodTipint(), COD_TIPINT_SAD);
	}

	public String getCcele() {
		return ccele;
	}

	public BigDecimal getImpStdSpesa() {
		return impStdSpesa;
	}

}
