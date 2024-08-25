/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "CARTELLA_SOCIALE")
@NamedQueries({ @NamedQuery(name = "CartellaSociale.findAll", query = "SELECT c FROM CartellaSociale c"),
		@NamedQuery(name = "CartellaSociale.findByCodAna", query = "SELECT c FROM CartellaSociale c WHERE c.codAna = :codAna"),
		@NamedQuery(name = "CartellaSociale.findByDesCs", query = "SELECT c FROM CartellaSociale c WHERE c.desCs = :desCs"),
		@NamedQuery(name = "CartellaSociale.findByDtApCs", query = "SELECT c FROM CartellaSociale c WHERE c.dtApCs = :dtApCs"),
		@NamedQuery(name = "CartellaSociale.findByDtChCs", query = "SELECT c FROM CartellaSociale c WHERE c.dtChCs = :dtChCs"),
		@NamedQuery(name = "CartellaSociale.findByDtAggAll", query = "SELECT c FROM CartellaSociale c WHERE c.dtAggAll = :dtAggAll") })
public class CartellaSociale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_ANA", nullable = false)
	private Integer codAna;

	public Integer getCodAna() {
		return codAna;
	}

	@Column(name = "DES_CS", length = 765)
	private String desCs;

	@Basic(optional = false)
	@Column(name = "DT_AP_CS", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtApCs;

	@Column(name = "DT_CH_CS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtChCs;

	@Column(name = "DT_AGG_ALL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAggAll;

	public List<Pai> getPaiList() {
		return paiList;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codAna")
	private List<Pai> paiList;

	@JoinColumn(name = "ID_PARAM_TIP_ALL", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamTipAll;

	@JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false, insertable = false, updatable = false)
	@OneToOne(optional = false)
	private AnagrafeSoc anagrafeSoc;

	public void setAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
		this.anagrafeSoc = anagrafeSoc;
	}

	public AnagrafeSoc getAnagrafeSoc() {
		return anagrafeSoc;
	}

	public void setCodAna(Integer codAna) {
		this.codAna = codAna;
	}

	public void setDesCs(String desCs) {
		this.desCs = desCs;
	}

	public void setDtApCs(Date dtApCs) {
		this.dtApCs = dtApCs;
	}

	public void setDtChCs(Date dtChCs) {
		this.dtChCs = dtChCs;
	}

	public void setDtAggAll(Date dtAggAll) {
		this.dtAggAll = dtAggAll;
	}

	public void setPaiList(List<Pai> paiList) {
		this.paiList = paiList;
	}

	public void setIdParamTipAll(ParametriIndata idParamTipAll) {
		this.idParamTipAll = idParamTipAll;
	}

	public String getDesCs() {
		return desCs;
	}

	public Date getDtApCs() {
		return dtApCs;
	}

	public Date getDtChCs() {
		return dtChCs;
	}

	public Date getDtAggAll() {
		return dtAggAll;
	}

	public ParametriIndata getIdParamTipAll() {
		return idParamTipAll;
	}

	public CartellaSociale() {
	}

	public CartellaSociale(Integer codAna) {
		this.codAna = codAna;
	}

	public CartellaSociale(Integer codAna, Date dtApCs) {
		this.codAna = codAna;
		this.dtApCs = dtApCs;
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
		if (!(object instanceof CartellaSociale)) {
			return false;
		}
		CartellaSociale other = (CartellaSociale) object;
		if ((this.codAna == null && other.codAna != null)
				|| (this.codAna != null && !this.codAna.equals(other.codAna))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.CartellaSociale[codAna=" + codAna + "]";
	}
}
