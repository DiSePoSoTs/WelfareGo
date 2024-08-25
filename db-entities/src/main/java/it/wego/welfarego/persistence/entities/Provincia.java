/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "PROVINCIA")
@NamedQueries({ @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"),
		@NamedQuery(name = "Provincia.findByCodStato", query = "SELECT p FROM Provincia p WHERE p.provinciaPK.codStato = :codStato"),
		@NamedQuery(name = "Provincia.findByCodProv", query = "SELECT p FROM Provincia p WHERE p.provinciaPK.codProv = :codProv"),
		@NamedQuery(name = "Provincia.findByDesProv", query = "SELECT p FROM Provincia p WHERE p.desProv = :desProv") })
public class Provincia implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected ProvinciaPK provinciaPK;

	@Basic(optional = false)
	@Column(name = "DES_PROV", nullable = false, length = 765)
	private String desProv;

	public String getDesProv() {
		return desProv;
	}

	@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Stato stato;

	public Stato getStato() {
		return stato;
	}

	public ProvinciaPK getProvinciaPK() {
		return provinciaPK;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provincia")
	private List<Comune> comuneList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provincia")
	@MapKey(name = "codCom")
	private Map<String, Comune> comuniByCodCom;

	public Map<String, Comune> getComuniByCodCom() {
		return comuniByCodCom;
	}

	@Basic(optional = false)
	@Column(name = "COD_PROV", nullable = false, length = 10, insertable = false, updatable = false)
	private String codProv;

	public String getCodProv() {
		return codProv;
	}

	public Provincia() {
	}

	public Provincia(ProvinciaPK provinciaPK) {
		this.provinciaPK = provinciaPK;
	}

	public Provincia(ProvinciaPK provinciaPK, String desProv) {
		this.provinciaPK = provinciaPK;
		this.desProv = desProv;
	}

	public Provincia(String codStato, String codProv) {
		this(codStato, codProv, null);
	}

	public Provincia(String codStato, String codProv, String desProv) {
		this(new ProvinciaPK(codStato, codProv), desProv);
	}

	public Comune getComune(String codCom) {
		return Optional.fromNullable(getComuniByCodCom().get(Strings.padStart(codCom, 6, '0')))
				.or(Optional.fromNullable(getComuniByCodCom().get(getCodProv() + Strings.padStart(codCom, 3, '0'))))
				.orNull();
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (provinciaPK != null ? provinciaPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Provincia)) {
			return false;
		}
		Provincia other = (Provincia) object;
		if ((this.provinciaPK == null && other.provinciaPK != null)
				|| (this.provinciaPK != null && !this.provinciaPK.equals(other.provinciaPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Provincia[provinciaPK=" + provinciaPK + "]";
	}
}
