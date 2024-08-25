/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author giuseppe
 */
@Embeddable
public class ProvinciaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_STATO", nullable = false, length = 10)
	private String codStato;

	@Basic(optional = false)
	@Column(name = "COD_PROV", nullable = false, length = 10)
	private String codProv;

	public String getCodProv() {
		return codProv;
	}

	public ProvinciaPK() {
	}

	public ProvinciaPK(String codStato, String codProv) {
		this.codStato = codStato;
		this.codProv = codProv;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codStato != null ? codStato.hashCode() : 0);
		hash += (codProv != null ? codProv.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ProvinciaPK)) {
			return false;
		}
		ProvinciaPK other = (ProvinciaPK) object;
		if ((this.codStato == null && other.codStato != null)
				|| (this.codStato != null && !this.codStato.equals(other.codStato))) {
			return false;
		}
		if ((this.codProv == null && other.codProv != null)
				|| (this.codProv != null && !this.codProv.equals(other.codProv))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.ProvinciaPK[codStato=" + codStato + ", codProv=" + codProv + "]";
	}

}
