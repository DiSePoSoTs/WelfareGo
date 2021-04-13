/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * @author giuseppe
 */
@Embeddable
@Getter
@Setter
public class IndisponibilitaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "COD_AS", nullable = false)
	private int codAs;

	@Basic(optional = false)
	@Column(name = "TS_INI_APP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsIniApp;

	public IndisponibilitaPK() {
	}

	public IndisponibilitaPK(int codAs, Date tsIniApp) {
		this.codAs = codAs;
		this.tsIniApp = tsIniApp;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) codAs;
		hash += (tsIniApp != null ? tsIniApp.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IndisponibilitaPK)) {
			return false;
		}
		IndisponibilitaPK other = (IndisponibilitaPK) object;
		if (this.codAs != other.codAs) {
			return false;
		}
		if ((this.tsIniApp == null && other.tsIniApp != null)
				|| (this.tsIniApp != null && !this.tsIniApp.equals(other.tsIniApp))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.IndisponibilitaPK[codAs=" + codAs + ", tsIniApp=" + tsIniApp
				+ "]";
	}

	public String serialize() {
		return "I" + codAs + "-" + tsIniApp.getTime();
	}

	public static IndisponibilitaPK deserialize(String string) {
		String[] split = string.substring(1).split("-");
		return new IndisponibilitaPK(Integer.valueOf(split[0]), new Date(Long.valueOf(split[1])));
	}

	public static boolean isIndisponibilitaPKSerialized(String string) {
		return string.matches("^I[0-9]+-[0-9]+$");
	}
}
