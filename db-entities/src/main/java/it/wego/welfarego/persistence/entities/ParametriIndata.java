/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "PARAMETRI_INDATA")
@NamedQueries({ @NamedQuery(name = "ParametriIndata.findAll", query = "SELECT p FROM ParametriIndata p"),
		@NamedQuery(name = "ParametriIndata.findByIdParamIndata", query = "SELECT p FROM ParametriIndata p WHERE p.idParamIndata = :idParamIndata"),
		@NamedQuery(name = "ParametriIndata.findByDtIniVal", query = "SELECT p FROM ParametriIndata p WHERE p.dtIniVal = :dtIniVal"),
		@NamedQuery(name = "ParametriIndata.findByDesParam", query = "SELECT p FROM ParametriIndata p WHERE p.desParam = :desParam"),
		@NamedQuery(name = "ParametriIndata.findByDateParam", query = "SELECT p FROM ParametriIndata p WHERE p.dateParam = :dateParam"),
		@NamedQuery(name = "ParametriIndata.findByDecimalParam", query = "SELECT p FROM ParametriIndata p WHERE p.decimalParam = :decimalParam"),
		@NamedQuery(name = "ParametriIndata.findByTxt1Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt1Param = :txt1Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt2Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt2Param = :txt2Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt3Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt3Param = :txt3Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt4Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt4Param = :txt4Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt5Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt5Param = :txt5Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt6Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt6Param = :txt6Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt7Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt7Param = :txt7Param"),
		@NamedQuery(name = "ParametriIndata.findByTxt8Param", query = "SELECT p FROM ParametriIndata p WHERE p.txt8Param = :txt8Param") })
public class ParametriIndata implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "parametriIndataSequence")
	@SequenceGenerator(name = "parametriIndataSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_PARAM_INDATA", nullable = false)
	private Integer idParamIndata;

	@Basic(optional = false)
	@Column(name = "DT_INI_VAL", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniVal;

	@JoinColumn(name = "ID_PARAM", referencedColumnName = "ID_PARAM", nullable = false)
	@ManyToOne(optional = false)
	private Parametri idParam;

	// data params
	@Basic(optional = false)
	@Column(name = "DES_PARAM", nullable = false, length = 765)
	private String desParam;

	@Column(name = "DATE_PARAM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateParam;

	@Column(name = "DECIMAL_PARAM", precision = 16, scale = 6)
	private BigDecimal decimalParam;

	@Column(name = "TXT1_PARAM", length = 90)
	private String txt1Param;

	@Column(name = "TXT2_PARAM", length = 90)
	private String txt2Param;

	@Column(name = "TXT3_PARAM", length = 90)
	private String txt3Param;

	@Column(name = "TXT4_PARAM", length = 90)
	private String txt4Param;

	@Column(name = "TXT5_PARAM", length = 90)
	private String txt5Param;

	@Column(name = "TXT6_PARAM", length = 90)
	private String txt6Param;

	@Column(name = "TXT7_PARAM", length = 90)
	private String txt7Param;

	@Column(name = "TXT8_PARAM", length = 90)
	private String txt8Param;

	@Column(name = "piattaforma", length = 15)
	private String piattaforma;

	public ParametriIndata() {
	}

	public ParametriIndata(Integer idParamIndata) {
		this.idParamIndata = idParamIndata;
	}

	public ParametriIndata(Integer idParamIndata, Date dtIniVal, String desParam) {
		this.idParamIndata = idParamIndata;
		this.dtIniVal = dtIniVal;
		this.desParam = desParam;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idParamIndata != null ? idParamIndata.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ParametriIndata)) {
			return false;
		}
		ParametriIndata other = (ParametriIndata) object;
		if ((this.idParamIndata == null && other.idParamIndata != null)
				|| (this.idParamIndata != null && !this.idParamIndata.equals(other.idParamIndata))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("ParametriIndata{");
		sb.append("idParamIndata=").append(idParamIndata);
		sb.append(", desParam='").append(desParam).append('\'');
		sb.append('}');
		return sb.toString();
	}

	/**
	 * restituisce il decimalparam (assunto percentuale) in forma decimale, per
	 * moltiplicarlo direttamente es 30 -> 0.3, 55 -> 0.55 restituisce invariati i
	 * valori minori di 1, assumendo siano gia' nella forma desiderata (oppure 0)
	 * 0.2 -> 0.2 0 -> 0 gestisce correttamente segni e valore assoluto
	 *
	 * @return
	 */
	public BigDecimal getDecimalPercentageParamAsDecimal() {
		Preconditions.checkNotNull(decimalParam);
		if (BigDecimal.ONE.compareTo(decimalParam.abs()) > 0) { // gia' decimale, o 0
			return decimalParam;
		} else {
			return decimalParam.divide(BigDecimal.valueOf(100), MathContext.UNLIMITED);
		}
	}

	public BigDecimal getDecimalPercentageParamAsPercentage() {
		return getDecimalPercentageParamAsDecimal().multiply(BigDecimal.valueOf(100));
	}

}
