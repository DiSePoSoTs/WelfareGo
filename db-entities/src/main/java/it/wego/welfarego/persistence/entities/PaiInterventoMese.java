/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "PAI_INTERVENTO_MESE")
@NamedQueries({ @NamedQuery(name = "PaiInterventoMese.findAll", query = "SELECT p FROM PaiInterventoMese p"),
		@NamedQuery(name = "PaiInterventoMese.findByCodPai", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.codPai = :codPai"),
		@NamedQuery(name = "PaiInterventoMese.findByCodTipint", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.codTipint = :codTipint"),
		@NamedQuery(name = "PaiInterventoMese.findByCntTipint", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.cntTipint = :cntTipint"),
		@NamedQuery(name = "PaiInterventoMese.findByAnnoEff", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.annoEff = :annoEff"),
		@NamedQuery(name = "PaiInterventoMese.findByMeseEff", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.meseEff = :meseEff"),
		@NamedQuery(name = "PaiInterventoMese.findByAnno", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.anno = :anno"),
		@NamedQuery(name = "PaiInterventoMese.findByCodImp", query = "SELECT p FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.codImp = :codImp"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgPrevEur", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgPrevEur = :bdgPrevEur"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgPrevQta", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgPrevQta = :bdgPrevQta"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgConsEur", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgConsEur = :bdgConsEur"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgConsQta", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgConsQta = :bdgConsQta"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgConsQtaBenef", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgConsQtaBenef = :bdgConsQtaBenef"),
		@NamedQuery(name = "PaiInterventoMese.findByBdgConsVar", query = "SELECT p FROM PaiInterventoMese p WHERE p.bdgConsVar = :bdgConsVar"),
		@NamedQuery(name = "PaiInterventoMese.findByCausVar", query = "SELECT p FROM PaiInterventoMese p WHERE p.causVar = :causVar"),
		@NamedQuery(name = "PaiInterventoMese.findByNote", query = "SELECT p FROM PaiInterventoMese p WHERE p.note = :note"),
		@NamedQuery(name = "PaiInterventoMese.findByFlgProp", query = "SELECT p FROM PaiInterventoMese p WHERE p.flgProp = :flgProp"),
		@NamedQuery(name = "PaiInterventoMese.findByGgAssenza", query = "SELECT p FROM PaiInterventoMese p WHERE p.ggAssenza = :ggAssenza"),
		@NamedQuery(name = "PaiInterventoMese.findByImpVar", query = "SELECT p FROM PaiInterventoMese p WHERE p.impVar = :impVar") })
public class PaiInterventoMese implements Serializable {

	public static final String FLG_STAMPATO_S = "S", FLG_STAMPATO_N = "N";

	public static final char FLG_PROPOSTA_S = 'S';

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiInterventoMesePK paiInterventoMesePK;

	@Basic(optional = false)
	@Column(name = "BDG_PREV_EUR", nullable = false, precision = 9, scale = 2)
	private BigDecimal bdgPrevEur;

	@Column(name = "BDG_PREV_QTA", precision = 9, scale = 2)
	private BigDecimal bdgPrevQta;

	@Column(name = "BDG_CONS_EUR", precision = 9, scale = 2)
	private BigDecimal bdgConsEur;

	@Column(name = "BDG_CONS_QTA", precision = 9, scale = 2)
	private BigDecimal bdgConsQta;

	@Column(name = "BDG_CONS_QTA_BENEF", precision = 9, scale = 2)
	private BigDecimal bdgConsQtaBenef;

	@Column(name = "BDG_CONS_VAR", precision = 9, scale = 2)
	private BigDecimal bdgConsVar;

	@Column(name = "BDG_CONS_RIENTR", precision = 9, scale = 2)
	private BigDecimal bdgConsRientr;

	@Column(name = "CAUS_VAR", length = 60)
	private String causVar;

	@Column(name = "NOTE", length = 1000)
	private String note;

	@Basic(optional = false)
	@Column(name = "FLG_PROP", nullable = false)
	private char flgProp;

	@Column(name = "GG_ASSENZA", precision = 5, scale = 2)
	private BigDecimal ggAssenza;

	@Column(name = "IMP_VAR", precision = 9, scale = 2)
	private BigDecimal impVar;

	@Column(name = "ACQUISITO", nullable = false)
	private Integer generato = 1;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "paiInterventoMese")
	private PaiCdg paiCdg;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "ID_MAN_DETTAGLIO", referencedColumnName = "ID_MAN_DETTAGLIO")
	@ManyToOne
	private MandatoDettaglio idManDettaglio;

	@JoinColumn(name = "ID_FATT_DETTAGLIO", referencedColumnName = "ID_FATT_DETTAGLIO")
	@ManyToOne
	private FatturaDettaglio idFattDettaglio;

	@JoinColumns({
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "ANNO", referencedColumnName = "COD_ANNO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_IMP", referencedColumnName = "COD_IMPE", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private BudgetTipIntervento budgetTipIntervento;

	@JoinColumn(name = "MOTIVAZIONE_VARIAZIONE_SPESA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata motivazioneVariazioneSpesa;

	@JoinColumn(name = "ID_PARAM_FASCIA", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamFascia;

	public PaiInterventoMese() {
	}

	public PaiInterventoMese(PaiInterventoMesePK paiInterventoMesePK) {
		this.paiInterventoMesePK = paiInterventoMesePK;
	}

	public PaiInterventoMese(PaiInterventoMesePK paiInterventoMesePK, BigDecimal bdgPrevEur, char flgProp) {
		this.paiInterventoMesePK = paiInterventoMesePK;
		this.bdgPrevEur = bdgPrevEur;
		this.flgProp = flgProp;
	}

	public PaiInterventoMese(int codPai, String codTipint, int cntTipint, short annoEff, short meseEff, short anno,
			String codImp) {
		this.paiInterventoMesePK = new PaiInterventoMesePK(codPai, codTipint, cntTipint, annoEff, meseEff, anno,
				codImp);
	}

	public PaiInterventoMese(int codPai, String codTipint, int cntTipint, int annoEff, int meseEff, int anno,
			String codImp) {
		this(codPai, codTipint, cntTipint, (short) annoEff, (short) meseEff, (short) anno, codImp);
	}

	public @Nullable BudgetTipInterventoUot getBudgetTipInterventoUot() {
		return getPaiIntervento().getPai().getIdParamUot() == null ? null
				: getBudgetTipIntervento().getBudgetTipInterventoUot(getPaiIntervento().getPai().getIdParamUot());
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paiInterventoMesePK != null ? paiInterventoMesePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiInterventoMese)) {
			return false;
		}
		PaiInterventoMese other = (PaiInterventoMese) object;
		if ((this.paiInterventoMesePK == null && other.paiInterventoMesePK != null)
				|| (this.paiInterventoMesePK != null && !this.paiInterventoMesePK.equals(other.paiInterventoMesePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		BigDecimal bdgDispEur = this.getBdgPrevEur();
		BigDecimal bdgPrevQta = this.getBdgPrevQta();

		String msgTemplate = "paiInterventoMesePK:[%s], bdgDispEur:%s, bdgPrevQta:%s";

		String msg = String.format(msgTemplate, paiInterventoMesePK, df.format(bdgDispEur), df.format(bdgPrevQta));
		return msg;
	}

}
