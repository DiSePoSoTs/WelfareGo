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


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "COMUNE")
@NamedQueries({ @NamedQuery(name = "Comune.findAll", query = "SELECT c FROM Comune c"),
		@NamedQuery(name = "Comune.findByCodStato", query = "SELECT c FROM Comune c WHERE c.comunePK.codStato = :codStato"),
		@NamedQuery(name = "Comune.findByCodProv", query = "SELECT c FROM Comune c WHERE c.comunePK.codProv = :codProv"),
		@NamedQuery(name = "Comune.findByCodCom", query = "SELECT c FROM Comune c WHERE c.comunePK.codCom = :codCom"),
		@NamedQuery(name = "Comune.findByDesCom", query = "SELECT c FROM Comune c WHERE c.desCom = :desCom"),
		@NamedQuery(name = "Comune.findByCodCatast", query = "SELECT c FROM Comune c WHERE c.codCatast = :codCatast") })
public class Comune implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected ComunePK comunePK;

	@Basic(optional = false)
	@Column(name = "DES_COM", nullable = false, length = 765)
	private String desCom;

	public String getDesCom() {
		return desCom;
	}

	@Column(name = "COD_CATAST", length = 4)
	private String codCatast;

	public ComunePK getComunePK() {
		return comunePK;
	}

	public String getCodCatast() {
		return codCatast;
	}

	public String getCodIstat() {
		return codIstat;
	}

	public List<Toponomastica> getToponomasticaList() {
		return toponomasticaList;
	}

	@Column(name = "COD_ISTAT")
	private String codIstat;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "comune")
	private List<Toponomastica> toponomasticaList;

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private Provincia provincia;

	public Provincia getProvincia() {
		return provincia;
	}

	public Map<String, Toponomastica> getToponomasticaByCodVia() {
		return toponomasticaByCodVia;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "comune")
	@MapKey(name = "codVia")
	private Map<String, Toponomastica> toponomasticaByCodVia;

	@Basic(optional = false)
	@Column(name = "COD_COM", nullable = false, length = 10, insertable = false, updatable = false)
	private String codCom;

	public String getCodCom() {
		return codCom;
	}

	public Comune() {
	}

	public Comune(ComunePK comunePK) {
		this.comunePK = comunePK;
	}

	public Comune(ComunePK comunePK, String desCom) {
		this.comunePK = comunePK;
		this.desCom = desCom;
	}

	public Comune(String codStato, String codProv, String codCom) {
		this(codStato, codProv, codCom, null);
	}

	public Comune(String codStato, String codProv, String codCom, String desCom) {
		this(new ComunePK(codStato, codProv, codCom), desCom);
	}

    public Toponomastica getToponomastica(String codVia) {
        return getToponomasticaByCodVia().get(Strings.padStart(codVia, 5, '0'));
    }

	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (comunePK != null ? comunePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Comune)) {
			return false;
		}
		Comune other = (Comune) object;
		if ((this.comunePK == null && other.comunePK != null)
				|| (this.comunePK != null && !this.comunePK.equals(other.comunePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Comune[comunePK=" + comunePK + "]";
	}
}
