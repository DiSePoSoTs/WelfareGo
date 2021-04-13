/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "PAI_CDG")
@NamedQueries({ @NamedQuery(name = "PaiCdg.findAll", query = "SELECT p FROM PaiCdg p"),
		@NamedQuery(name = "PaiCdg.findByCodPai", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.codPai = :codPai"),
		@NamedQuery(name = "PaiCdg.findByCodTipint", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.codTipint = :codTipint"),
		@NamedQuery(name = "PaiCdg.findByCntTipint", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.cntTipint = :cntTipint"),
		@NamedQuery(name = "PaiCdg.findByTsEveCfg", query = "SELECT p FROM PaiCdg p WHERE p.tsEveCfg = :tsEveCfg"),
		@NamedQuery(name = "PaiCdg.findByQtaPrev", query = "SELECT p FROM PaiCdg p WHERE p.qtaPrev = :qtaPrev"),
		@NamedQuery(name = "PaiCdg.findByQtaErog", query = "SELECT p FROM PaiCdg p WHERE p.qtaErog = :qtaErog"),
		@NamedQuery(name = "PaiCdg.findByImpStd", query = "SELECT p FROM PaiCdg p WHERE p.impStd = :impStd"),
		@NamedQuery(name = "PaiCdg.findByImpCompl", query = "SELECT p FROM PaiCdg p WHERE p.impCompl = :impCompl"),
		@NamedQuery(name = "PaiCdg.findByCodAnno", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.codAnno = :codAnno"),
		@NamedQuery(name = "PaiCdg.findByCodCap", query = "SELECT p FROM PaiCdg p WHERE p.codCap = :codCap"),
		@NamedQuery(name = "PaiCdg.findByCodImpe", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.codImpe = :codImpe"),
		@NamedQuery(name = "PaiCdg.findByCcele", query = "SELECT p FROM PaiCdg p WHERE p.ccele = :ccele"),
		@NamedQuery(name = "PaiCdg.findByAnnoEff", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.annoEff = :annoEff"),
		@NamedQuery(name = "PaiCdg.findByMeseEff", query = "SELECT p FROM PaiCdg p WHERE p.paiCdgPK.meseEff = :meseEff"),
		@NamedQuery(name = "PaiCdg.findByImpVar", query = "SELECT p FROM PaiCdg p WHERE p.impVar = :impVar") })
public class PaiCdg implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiCdgPK paiCdgPK;

	@Column(name = "TS_EVE_CFG")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsEveCfg;

	@Basic(optional = false)
	@Column(name = "QTA_PREV", nullable = false, precision = 9, scale = 2)
	private BigDecimal qtaPrev;

	@Basic(optional = false)
	@Column(name = "QTA_EROG", nullable = false, precision = 9, scale = 2)
	private BigDecimal qtaErog;

	@Basic(optional = false)
	@Column(name = "IMP_STD", nullable = false, precision = 9, scale = 2)
	private BigDecimal impStd;

	@Basic(optional = false)
	@Column(name = "IMP_COMPL", nullable = false, precision = 9, scale = 2)
	private BigDecimal impCompl;

	@Basic(optional = false)
	@Column(name = "COD_CAP", nullable = false)
	private int codCap;

	@Basic(optional = false)
	@Column(name = "CCELE", nullable = false, length = 10)
	private String ccele;

	@Column(name = "IMP_VAR", precision = 9, scale = 2)
	private BigDecimal impVar;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "ANNO_EFF", referencedColumnName = "ANNO_EFF", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "MESE_EFF", referencedColumnName = "MESE_EFF", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_ANNO", referencedColumnName = "ANNO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_IMPE", referencedColumnName = "COD_IMP", nullable = false, insertable = false, updatable = false) })
	@OneToOne(optional = false)
	private PaiInterventoMese paiInterventoMese;

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA")
	@ManyToOne
	private AnagrafeSoc codAna;

	public PaiCdg() {
	}

	public PaiCdg(PaiCdgPK paiCdgPK) {
		this.paiCdgPK = paiCdgPK;
	}

	public PaiCdg(PaiCdgPK paiCdgPK, BigDecimal qtaPrev, BigDecimal qtaErog, BigDecimal impStd, BigDecimal impCompl,
			int codCap, String ccele) {
		this.paiCdgPK = paiCdgPK;
		this.qtaPrev = qtaPrev;
		this.qtaErog = qtaErog;
		this.impStd = impStd;
		this.impCompl = impCompl;
		this.codCap = codCap;
		this.ccele = ccele;
	}

	public PaiCdg(int codPai, String codTipint, int cntTipint, short codAnno, String codImpe, short annoEff,
			short meseEff) {
		this.paiCdgPK = new PaiCdgPK(codPai, codTipint, cntTipint, codAnno, codImpe, annoEff, meseEff);
	}

	private static final BigDecimal BD100 = new BigDecimal(100);

	/**
	 * set imp compl conteggiando (eventualmente) la fascia
	 *
	 * @param impCompl
	 */
	public void setImpComplUsingFascia(BigDecimal impCompl) {
		if (getPaiInterventoMese() != null && getPaiInterventoMese().getPaiIntervento().shouldUseFascia()
				&& getPaiInterventoMese().getPaiIntervento().getPai().getIdParamFascia() != null) {
			impCompl = impCompl
					.multiply(getPaiInterventoMese().getPaiIntervento().getPai().getIdParamFascia().getDecimalParam())
					.divide(BD100, MathContext.DECIMAL32);
		}
		setImpCompl(impCompl);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paiCdgPK != null ? paiCdgPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiCdg)) {
			return false;
		}
		PaiCdg other = (PaiCdg) object;
		if ((this.paiCdgPK == null && other.paiCdgPK != null)
				|| (this.paiCdgPK != null && !this.paiCdgPK.equals(other.paiCdgPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiCdg[paiCdgPK=" + paiCdgPK + "]";
	}
}
