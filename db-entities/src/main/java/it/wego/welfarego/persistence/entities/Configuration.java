/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "CONFIGURATION")
@NamedQueries({ @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c"),
		@NamedQuery(name = "Configuration.findById", query = "SELECT c FROM Configuration c WHERE c.id = :id"),
		@NamedQuery(name = "Configuration.findByValue", query = "SELECT c FROM Configuration c WHERE c.value = :value"),
		@NamedQuery(name = "Configuration.findByDescription", query = "SELECT c FROM Configuration c WHERE c.description = :description") })
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String COD_TEMPLATE_DOMANDA_INTEGRATIVA = "cod.tmpl.domandaintegrativa",
			CS_SEARCH_LIMIT = "cs.search_limit";

	@Id
	@Basic(optional = false)
	@Column(name = "ID", nullable = false, length = 255)
	private String id;

	@Basic(optional = false)
	@Column(name = "VALUE", nullable = false, length = 4000)
	private String value;

	public String getValue() {
		return value;
	}

	@Column(name = "DESCRIPTION", length = 4000)
	private String description;

	public Configuration() {
	}

	public Configuration(String id) {
		this.id = id;
	}

	public Configuration(String id, String value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Configuration)) {
			return false;
		}
		Configuration other = (Configuration) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Configuration[id=" + id + "]";
	}
}
