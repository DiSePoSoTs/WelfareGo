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
public class MapDatiSpecificiInterventoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_PAI", nullable = false)
	private Integer codPai;

	@Basic(optional = false)
	@Column(name = "COD_TIPINT", nullable = false, length = 10)
	private String codTipint;

	@Basic(optional = false)
	@Column(name = "CNT_TIPINT", nullable = false)
	private Integer cntTipint;

	@Basic(optional = false)
	@Column(name = "COD_CAMPO", nullable = false, length = 20)
	private String codCampo;

	public MapDatiSpecificiInterventoPK() {
	}

	public MapDatiSpecificiInterventoPK(Integer codPai, String codTipint, Integer cntTipint, String codCampo) {
		this.codPai = codPai;
		this.codTipint = codTipint;
		this.cntTipint = cntTipint;
		this.codCampo = codCampo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) codPai;
		hash += (codTipint != null ? codTipint.hashCode() : 0);
		hash += (int) cntTipint;
		hash += (codCampo != null ? codCampo.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MapDatiSpecificiInterventoPK)) {
			return false;
		}
		MapDatiSpecificiInterventoPK other = (MapDatiSpecificiInterventoPK) object;
		if (this.codPai != other.codPai) {
			return false;
		}
		if ((this.codTipint == null && other.codTipint != null)
				|| (this.codTipint != null && !this.codTipint.equals(other.codTipint))) {
			return false;
		}
		if (this.cntTipint != other.cntTipint) {
			return false;
		}
		if ((this.codCampo == null && other.codCampo != null)
				|| (this.codCampo != null && !this.codCampo.equals(other.codCampo))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.MapDatiSpecificiInterventoPK[codPai=" + codPai + ", codTipint="
				+ codTipint + ", cntTipint=" + cntTipint + ", codCampo=" + codCampo + "]";
	}
}
