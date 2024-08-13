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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.codec.binary.Base64;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "TEMPLATE")
@NamedQueries({ @NamedQuery(name = "Template.findAll", query = "SELECT t FROM Template t"),
		@NamedQuery(name = "Template.findByCodTmpl", query = "SELECT t FROM Template t WHERE t.codTmpl = :codTmpl"),
		@NamedQuery(name = "Template.findByDesTmpl", query = "SELECT t FROM Template t WHERE t.desTmpl = :desTmpl"),
		@NamedQuery(name = "Template.findByNomeFile", query = "SELECT t FROM Template t WHERE t.nomeFile = :nomeFile") })
public class Template implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "COD_TMPL", nullable = false)
	private Integer codTmpl;

	@Basic(optional = false)
	@Column(name = "DES_TMPL", nullable = false, length = 765)
	private String desTmpl;

	@Column(name = "NOME_FILE", length = 765)
	private String nomeFile;

	public Integer getCodTmpl() {
		return codTmpl;
	}

	public void setClobTmpl(String clobTmpl) {
		this.clobTmpl = clobTmpl;
	}

	@Basic(optional = false)
	@Lob
	@Column(name = "CLOB_TMPL", nullable = false)
	private String clobTmpl;

	public void setCodTmpl(Integer codTmpl) {
		this.codTmpl = codTmpl;
	}

	public void setDesTmpl(String desTmpl) {
		this.desTmpl = desTmpl;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getClobTmpl() {
		return clobTmpl;
	}

	@OneToMany(mappedBy = "codTmpl")
	private List<UniqueTasklist> uniqueTasklistList;

	public String getDesTmpl() {
		return desTmpl;
	}

	@OneToMany(mappedBy = "codTmpl")
	private List<PaiEvento> paiEventoList;

	@OneToMany(mappedBy = "codTmplLettPag")
	private List<TipologiaIntervento> tipologiaInterventoList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplChius")
	private List<TipologiaIntervento> tipologiaInterventoList1;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplVar")
	private List<TipologiaIntervento> tipologiaInterventoList2;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplEse")
	private List<TipologiaIntervento> tipologiaInterventoList3;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplEseMul")
	private List<TipologiaIntervento> tipologiaInterventoList4;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplVarMul")
	private List<TipologiaIntervento> tipologiaInterventoList5;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codTmplChiusMul")
	private List<TipologiaIntervento> tipologiaInterventoList6;

	public Template() {
	}

	public Template(Integer codTmpl) {
		this.codTmpl = codTmpl;
	}

	public Template(Integer codTmpl, String desTmpl, String clobTmpl) {
		this.codTmpl = codTmpl;
		this.desTmpl = desTmpl;
		this.clobTmpl = clobTmpl;
	}

	public byte[] getClobTmplAsDecodedByteArray() {
		return Base64.decodeBase64(getClobTmpl());
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codTmpl != null ? codTmpl.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Template)) {
			return false;
		}
		Template other = (Template) object;
		if ((this.codTmpl == null && other.codTmpl != null)
				|| (this.codTmpl != null && !this.codTmpl.equals(other.codTmpl))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Template[codTmpl=" + codTmpl + "]";
	}
}
