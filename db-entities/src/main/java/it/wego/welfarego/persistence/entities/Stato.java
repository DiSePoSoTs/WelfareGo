/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "STATO")
@NamedQueries({ @NamedQuery(name = "Stato.findAll", query = "SELECT s FROM Stato s"),
		@NamedQuery(name = "Stato.findByCodStato", query = "SELECT s FROM Stato s WHERE s.codStato = :codStato"),
		@NamedQuery(name = "Stato.findByDesStato", query = "SELECT s FROM Stato s WHERE s.desStato = :desStato"),
		@NamedQuery(name = "Stato.findByCodGruppo", query = "SELECT s FROM Stato s WHERE s.codGruppo = :codGruppo"),
		@NamedQuery(name = "Stato.findByCodCatast", query = "SELECT s FROM Stato s WHERE s.codCatast = :codCatast") })
public class Stato implements Serializable {

	public static final String COD_STATO_NON_DEFINITO = "999", COD_STATO_ITALIA = "100";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_STATO", nullable = false, length = 10)
	private String codStato;

	public String getCodStato() {
		return codStato;
	}

	@Basic(optional = false)
	@Column(name = "DES_STATO", nullable = false, length = 765)
	private String desStato;

	public String getDesStato() {
		return desStato;
	}

	@Column(name = "COD_GRUPPO", length = 20)
	private String codGruppo;

	@Column(name = "COD_CATAST", length = 20)
	private String codCatast;

	@Column(name = "COD_ISTAT")
	private String codIstat;

	@Column(name = "COD_CITT_INSIEL")
	private Integer codiceCittadinanzaInsiel;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stato")
	private List<Provincia> provinciaList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stato")
	@MapKey(name = "codProv")
	private Map<String, Provincia> provincieByCodProv;

	public Map<String, Provincia> getProvincieByCodProv() {
		return provincieByCodProv;
	}

	public Stato() {
	}

	public Stato(String codStato) {
		this.codStato = codStato;
	}

	public Stato(String codStato, String desStato) {
		this.codStato = codStato;
		this.desStato = desStato;
	}

	public Provincia getProvincia(String codProv) {
		return getProvincieByCodProv().get(Strings.padStart(codProv, 3, '0'));
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codStato != null ? codStato.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Stato)) {
			return false;
		}
		Stato other = (Stato) object;
		if ((this.codStato == null && other.codStato != null)
				|| (this.codStato != null && !this.codStato.equals(other.codStato))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Stato[codStato=" + codStato + "]";
	}
}
