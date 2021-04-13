/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "TOPONOMASTICA_CIVICI")
@NamedQueries({ @NamedQuery(name = "ToponomasticaCivici.findAll", query = "SELECT t FROM ToponomasticaCivici t"),
		@NamedQuery(name = "ToponomasticaCivici.findByCodStato", query = "SELECT t FROM ToponomasticaCivici t WHERE t.toponomasticaCiviciPK.codStato = :codStato"),
		@NamedQuery(name = "ToponomasticaCivici.findByCodProv", query = "SELECT t FROM ToponomasticaCivici t WHERE t.toponomasticaCiviciPK.codProv = :codProv"),
		@NamedQuery(name = "ToponomasticaCivici.findByCodCom", query = "SELECT t FROM ToponomasticaCivici t WHERE t.toponomasticaCiviciPK.codCom = :codCom"),
		@NamedQuery(name = "ToponomasticaCivici.findByCodVia", query = "SELECT t FROM ToponomasticaCivici t WHERE t.toponomasticaCiviciPK.codVia = :codVia"),
		@NamedQuery(name = "ToponomasticaCivici.findByCodCiv", query = "SELECT t FROM ToponomasticaCivici t WHERE t.toponomasticaCiviciPK.codCiv = :codCiv"),
		@NamedQuery(name = "ToponomasticaCivici.findByDesCiv", query = "SELECT t FROM ToponomasticaCivici t WHERE t.desCiv = :desCiv") })
public class ToponomasticaCivici implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected ToponomasticaCiviciPK toponomasticaCiviciPK;

	@Basic(optional = false)
	@Column(name = "DES_CIV", nullable = false, length = 765)
	private String desCiv;

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_COM", referencedColumnName = "COD_COM", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_VIA", referencedColumnName = "COD_VIA", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private Toponomastica toponomastica;

	@JoinColumn(name = "ID_PARAM_UOT_RIF", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamUotRif;

	@Basic(optional = false)
	@Column(name = "COD_CIV", nullable = false, length = 20, insertable = false, updatable = false)
	private String codCiv;

	public ToponomasticaCivici() {
	}

	public ToponomasticaCivici(ToponomasticaCiviciPK toponomasticaCiviciPK) {
		this.toponomasticaCiviciPK = toponomasticaCiviciPK;
	}

	public ToponomasticaCivici(ToponomasticaCiviciPK toponomasticaCiviciPK, String desCiv) {
		this.toponomasticaCiviciPK = toponomasticaCiviciPK;
		this.desCiv = desCiv;
	}

	public ToponomasticaCivici(String codStato, String codProv, String codCom, String codVia, String codCiv) {
		this(codStato, codProv, codCom, codVia, codCiv, null);
	}

	public ToponomasticaCivici(String codStato, String codProv, String codCom, String codVia, String codCiv,
			String desCiv) {
		this(new ToponomasticaCiviciPK(codStato, codProv, codCom, codVia, codCiv), desCiv);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (toponomasticaCiviciPK != null ? toponomasticaCiviciPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ToponomasticaCivici)) {
			return false;
		}
		ToponomasticaCivici other = (ToponomasticaCivici) object;
		if ((this.toponomasticaCiviciPK == null && other.toponomasticaCiviciPK != null)
				|| (this.toponomasticaCiviciPK != null
						&& !this.toponomasticaCiviciPK.equals(other.toponomasticaCiviciPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.ToponomasticaCivici[toponomasticaCiviciPK="
				+ toponomasticaCiviciPK + "]";
	}

	public boolean getCessato() {
		return toponomasticaCiviciPK.getCodCiv().matches("^9.*");
	}
}
