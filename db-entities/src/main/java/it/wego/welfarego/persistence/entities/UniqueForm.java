/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "UNIQUE_FORM")
@NamedQueries({ @NamedQuery(name = "UniqueForm.findAll", query = "SELECT u FROM UniqueForm u"),
		@NamedQuery(name = "UniqueForm.findByCodForm", query = "SELECT u FROM UniqueForm u WHERE u.codForm = :codForm"),
		@NamedQuery(name = "UniqueForm.findByDesForm", query = "SELECT u FROM UniqueForm u WHERE u.desForm = :desForm"),
		@NamedQuery(name = "UniqueForm.findByUrlForm", query = "SELECT u FROM UniqueForm u WHERE u.urlForm = :urlForm"),
		@NamedQuery(name = "UniqueForm.findByParamForm", query = "SELECT u FROM UniqueForm u WHERE u.paramForm = :paramForm"),
		@NamedQuery(name = "UniqueForm.findByClassForm", query = "SELECT u FROM UniqueForm u WHERE u.classForm = :classForm") })
public class UniqueForm implements Serializable {

	public static final String COD_FORM_GENERAZIONE_DOCUMENTO = "P080A020", COD_FORM_VALIDA_INTERVENTI = "P20A040",
			COD_FORM_PROTOCOLLA_DOMANDA = "P20A020", COD_FORM_VERIFICA_ESECUTIVITA = "P050A020",
			COD_FORM_DOCUMENTO_CHIUSURA = "P080A020", COD_FORM_DARIVEDERE = "P050A060", COD_FORM_RESPINTO = "P050A050",
			COD_FORM_APPROVAZIONE_TECNICA = "71";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_FORM", nullable = false, length = 10)
	private String codForm;

	public String getCodForm() {
		return codForm;
	}

	@Basic(optional = false)
	@Column(name = "DES_FORM", nullable = false, length = 765)
	private String desForm;

	public String getDesForm() {
		return desForm;
	}

	public String getUrlForm() {
		return urlForm;
	}

	public String getParamForm() {
		return paramForm;
	}

	public String getClassForm() {
		return classForm;
	}

	@Basic(optional = false)
	@Column(name = "URL_FORM", nullable = false, length = 255)
	private String urlForm;

	@Column(name = "PARAM_FORM", length = 765)
	private String paramForm;

	@Column(name = "CLASS_FORM", length = 255)
	private String classForm;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "form")
	private List<UniqueTasklist> uniqueTasklistList;

	public UniqueForm() {
	}

	public UniqueForm(String codForm) {
		this.codForm = codForm;
	}

	public UniqueForm(String codForm, String desForm, String urlForm) {
		this.codForm = codForm;
		this.desForm = desForm;
		this.urlForm = urlForm;
	}

	public List<UniqueTasklist> getUniqueTasklistList() {
		return uniqueTasklistList;
	}

	public void setUniqueTasklistList(List<UniqueTasklist> uniqueTasklistList) {
		this.uniqueTasklistList = uniqueTasklistList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codForm != null ? codForm.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof UniqueForm)) {
			return false;
		}
		UniqueForm other = (UniqueForm) object;
		if ((this.codForm == null && other.codForm != null)
				|| (this.codForm != null && !this.codForm.equals(other.codForm))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.UniqueForm[codForm=" + codForm + "]";
	}
}
