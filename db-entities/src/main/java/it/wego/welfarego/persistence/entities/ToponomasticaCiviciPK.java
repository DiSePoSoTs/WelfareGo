/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Embeddable
@Getter
@Setter
public class ToponomasticaCiviciPK implements Serializable {

	private static final long serialVersionUID = 1L;
	public String getCodStato() {
		return codStato;
	}

	public String getCodProv() {
		return codProv;
	}

	public String getCodCom() {
		return codCom;
	}

	public String getCodVia() {
		return codVia;
	}

	@Basic(optional = false)
	@Column(name = "COD_STATO", nullable = false, length = 10)
	private String codStato;

	@Basic(optional = false)
	@Column(name = "COD_PROV", nullable = false, length = 10)
	private String codProv;

	@Basic(optional = false)
	@Column(name = "COD_COM", nullable = false, length = 10)
	private String codCom;

	@Basic(optional = false)
	@Column(name = "COD_VIA", nullable = false, length = 20)
	private String codVia;

	@Basic(optional = false)
	@Column(name = "COD_CIV", nullable = false, length = 20)
	private String codCiv;

	public String getCodCiv() {
		return codCiv;
	}

	public ToponomasticaCiviciPK() {
	}

	public ToponomasticaCiviciPK(String codStato, String codProv, String codCom, String codVia, String codCiv) {
		this.codStato = codStato;
		this.codProv = codProv;
		this.codCom = codCom;
		this.codVia = codVia;
		this.codCiv = codCiv;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codStato != null ? codStato.hashCode() : 0);
		hash += (codProv != null ? codProv.hashCode() : 0);
		hash += (codCom != null ? codCom.hashCode() : 0);
		hash += (codVia != null ? codVia.hashCode() : 0);
		hash += (codCiv != null ? codCiv.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ToponomasticaCiviciPK)) {
			return false;
		}
		ToponomasticaCiviciPK other = (ToponomasticaCiviciPK) object;
		if ((this.codStato == null && other.codStato != null)
				|| (this.codStato != null && !this.codStato.equals(other.codStato))) {
			return false;
		}
		if ((this.codProv == null && other.codProv != null)
				|| (this.codProv != null && !this.codProv.equals(other.codProv))) {
			return false;
		}
		if ((this.codCom == null && other.codCom != null)
				|| (this.codCom != null && !this.codCom.equals(other.codCom))) {
			return false;
		}
		if ((this.codVia == null && other.codVia != null)
				|| (this.codVia != null && !this.codVia.equals(other.codVia))) {
			return false;
		}
		if ((this.codCiv == null && other.codCiv != null)
				|| (this.codCiv != null && !this.codCiv.equals(other.codCiv))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.ToponomasticaCiviciPK[codStato=" + codStato + ", codProv="
				+ codProv + ", codCom=" + codCom + ", codVia=" + codVia + ", codCiv=" + codCiv + "]";
	}

}
