/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "TIPOLOGIA_PARAMETRI")
@NamedQueries({ @NamedQuery(name = "TipologiaParametri.findAll", query = "SELECT t FROM TipologiaParametri t"),
		@NamedQuery(name = "TipologiaParametri.findByTipParam", query = "SELECT t FROM TipologiaParametri t WHERE t.tipParam = :tipParam"),
		@NamedQuery(name = "TipologiaParametri.findByDesTipParam", query = "SELECT t FROM TipologiaParametri t WHERE t.desTipParam = :desTipParam"),
		@NamedQuery(name = "TipologiaParametri.findByFlgIndata", query = "SELECT t FROM TipologiaParametri t WHERE t.flgIndata = :flgIndata") })
public class TipologiaParametri implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "TIP_PARAM", nullable = false, length = 10)
	private String tipParam;

	public String getTipParam() {
		return tipParam;
	}

	public String getDesTipParam() {
		return desTipParam;
	}

	public char getFlgIndata() {
		return flgIndata;
	}

	@Basic(optional = false)
	@Column(name = "DES_TIP_PARAM", nullable = false, length = 765)
	private String desTipParam;

	@Basic(optional = false)
	@Column(name = "FLG_INDATA", nullable = false)
	private char flgIndata;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipParam")
	private List<Parametri> parametriList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipParam")
	@MapKey(name = "codParam")
	private Map<String, Parametri> parametriByCodParam;

	public Map<String, Parametri> getParametriByCodParam() {
		return parametriByCodParam;
	}

	public TipologiaParametri() {
	}

	public TipologiaParametri(String tipParam) {
		this.tipParam = tipParam;
	}

	public TipologiaParametri(String tipParam, String desTipParam, char flgIndata) {
		this.tipParam = tipParam;
		this.desTipParam = desTipParam;
		this.flgIndata = flgIndata;
	}

    public Parametri getParametroByCodParam(String codParam) {
        return getParametriByCodParam().get(codParam);
    }
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tipParam != null ? tipParam.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TipologiaParametri)) {
			return false;
		}
		TipologiaParametri other = (TipologiaParametri) object;
		if ((this.tipParam == null && other.tipParam != null)
				|| (this.tipParam != null && !this.tipParam.equals(other.tipParam))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.TipologiaParametri[tipParam=" + tipParam + "]";
	}
}
