/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import com.google.common.base.Strings;

import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "TOPONOMASTICA")
@NamedQueries({ @NamedQuery(name = "Toponomastica.findAll", query = "SELECT t FROM Toponomastica t"),
		@NamedQuery(name = "Toponomastica.findByCodStato", query = "SELECT t FROM Toponomastica t WHERE t.toponomasticaPK.codStato = :codStato"),
		@NamedQuery(name = "Toponomastica.findByCodProv", query = "SELECT t FROM Toponomastica t WHERE t.toponomasticaPK.codProv = :codProv"),
		@NamedQuery(name = "Toponomastica.findByCodCom", query = "SELECT t FROM Toponomastica t WHERE t.toponomasticaPK.codCom = :codCom"),
		@NamedQuery(name = "Toponomastica.findByCodVia", query = "SELECT t FROM Toponomastica t WHERE t.toponomasticaPK.codVia = :codVia"),
		@NamedQuery(name = "Toponomastica.findByDesVia", query = "SELECT t FROM Toponomastica t WHERE t.desVia = :desVia") })
public class Toponomastica implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected ToponomasticaPK toponomasticaPK;

	@Basic(optional = false)
	@Column(name = "DES_VIA", nullable = false, length = 765)
	private String desVia;

	public ToponomasticaPK getToponomasticaPK() {
		return toponomasticaPK;
	}

	public List<ToponomasticaCivici> getToponomasticaCiviciList() {
		return toponomasticaCiviciList;
	}

	public String getDesVia() {
		return desVia;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "toponomastica")
	private List<ToponomasticaCivici> toponomasticaCiviciList;

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_COM", referencedColumnName = "COD_COM", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private Comune comune;

	public Comune getComune() {
		return comune;
	}

	@Basic(optional = false)
	@Column(name = "COD_VIA", nullable = false, length = 20, insertable = false, updatable = false)
	private String codVia;

	public String getCodVia() {
		return codVia;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "toponomastica")
	@MapKey(name = "codCiv")
	private Map<String, ToponomasticaCivici> toponomasticaCiviciByCodCivico;

	public Map<String, ToponomasticaCivici> getToponomasticaCiviciByCodCivico() {
		return toponomasticaCiviciByCodCivico;
	}

	public Toponomastica() {
	}

	public Toponomastica(ToponomasticaPK toponomasticaPK) {
		this.toponomasticaPK = toponomasticaPK;
	}

	public Toponomastica(ToponomasticaPK toponomasticaPK, String desVia) {
		this.toponomasticaPK = toponomasticaPK;
		this.desVia = desVia;
	}

	public Toponomastica(String codStato, String codProv, String codCom, String codVia, String desVia) {
		this(new ToponomasticaPK(codStato, codProv, codCom, codVia), desVia);
	}

	public Toponomastica(String codStato, String codProv, String codCom, String codVia) {
		this(codStato, codProv, codCom, codVia, null);
	}
	
    public  ToponomasticaCivici getToponomasticaCivici(String codCivico) {
        return getToponomasticaCiviciByCodCivico().get(Strings.padStart(codCivico, 5, '0'));
    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (toponomasticaPK != null ? toponomasticaPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Toponomastica)) {
			return false;
		}
		Toponomastica other = (Toponomastica) object;
		if ((this.toponomasticaPK == null && other.toponomasticaPK != null)
				|| (this.toponomasticaPK != null && !this.toponomasticaPK.equals(other.toponomasticaPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Toponomastica[toponomasticaPK=" + toponomasticaPK + "]";
	}
}
