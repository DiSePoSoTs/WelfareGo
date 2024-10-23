/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "PAI_DOCUMENTO", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "COD_TIPDOC", "VER", "COD_PAI", "COD_TIPINT", "CNT_TIPINT" }) })
@NamedQueries({ @NamedQuery(name = "PaiDocumento.findAll", query = "SELECT p FROM PaiDocumento p"),
		@NamedQuery(name = "PaiDocumento.findByIdDocumento", query = "SELECT p FROM PaiDocumento p WHERE p.idDocumento = :idDocumento"),
		@NamedQuery(name = "PaiDocumento.findByCodTipdoc", query = "SELECT p FROM PaiDocumento p WHERE p.codTipdoc = :codTipdoc"),
		@NamedQuery(name = "PaiDocumento.findByVer", query = "SELECT p FROM PaiDocumento p WHERE p.ver = :ver"),
		@NamedQuery(name = "PaiDocumento.findByDtDoc", query = "SELECT p FROM PaiDocumento p WHERE p.dtDoc = :dtDoc"),
		@NamedQuery(name = "PaiDocumento.findByNomeFile", query = "SELECT p FROM PaiDocumento p WHERE p.nomeFile = :nomeFile"),
		@NamedQuery(name = "PaiDocumento.findByNumProt", query = "SELECT p FROM PaiDocumento p WHERE p.numProt = :numProt"),
		@NamedQuery(name = "PaiDocumento.findByDtProt", query = "SELECT p FROM PaiDocumento p WHERE p.dtProt = :dtProt") })
public class PaiDocumento implements Serializable {

	public String getBlobDoc() {
		return blobDoc;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public String getTipoDetermina() {
		return tipoDetermina;
	}

	public String getNumProt() {
		return numProt;
	}

	public Date getDtProt() {
		return dtProt;
	}

	public Utenti getCodUteAut() {
		return codUteAut;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "paiDocumentoSequence")
	@SequenceGenerator(name = "paiDocumentoSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_DOCUMENTO", nullable = false)
	private Integer idDocumento;

	public String getCodTipdoc() {
		return codTipdoc;
	}

	public PaiIntervento getPaiIntervento() {
		return paiIntervento;
	}

	public Pai getCodPai() {
		return codPai;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	@Basic(optional = false)
	@Column(name = "COD_TIPDOC", nullable = false, length = 20)
	private String codTipdoc;

	public BigInteger getVer() {
		return ver;
	}

	public void setCodTipdoc(String codTipdoc) {
		this.codTipdoc = codTipdoc;
	}

	public void setNumProt(String numProt) {
		this.numProt = numProt;
	}

	public void setDtProt(Date dtProt) {
		this.dtProt = dtProt;
	}

	public void setDtDoc(Date dtDoc) {
		this.dtDoc = dtDoc;
	}

	public void setBlobDoc(String blobDoc) {
		this.blobDoc = blobDoc;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public void setTipoDetermina(String tipoDetermina) {
		this.tipoDetermina = tipoDetermina;
	}

	public void setCodUteAut(Utenti codUteAut) {
		this.codUteAut = codUteAut;
	}

	public void setPaiIntervento(PaiIntervento paiIntervento) {
		this.paiIntervento = paiIntervento;
	}

	@Basic(optional = false)
	@Column(name = "VER", nullable = false)
	private BigInteger ver;

	public void setVer(BigInteger ver) {
		this.ver = ver;
	}

	public void setCodPai(Pai codPai) {
		this.codPai = codPai;
	}

	@Basic(optional = false)
	@Column(name = "DT_DOC", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtDoc;

	public Date getDtDoc() {
		return dtDoc;
	}

	@Basic(optional = false)
	@Lob
	@Column(name = "BLOB_DOC", nullable = false)
	private String blobDoc;

	@Basic(optional = false)
	@Column(name = "NOME_FILE", nullable = false, length = 765)
	private String nomeFile;

	@Column(name = "TIPO_DETERMINA", nullable = true, length = 50)
	private String tipoDetermina;

	@Column(name = "NUM_PROT", length = 20)
	private String numProt;

	@Column(name = "DT_PROT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtProt;

	@JoinColumn(name = "COD_UTE_AUT", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUteAut;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", updatable = false, insertable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", updatable = false, insertable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", updatable = false, insertable = false) })
	@ManyToOne
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI")
	@ManyToOne
	private Pai codPai;

	public PaiDocumento() {
	}

	public PaiDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public PaiDocumento(Integer idDocumento, String codTipdoc, BigInteger ver, Date dtDoc, String blobDoc,
			String nomeFile) {
		this.idDocumento = idDocumento;
		this.codTipdoc = codTipdoc;
		this.ver = ver;
		this.dtDoc = dtDoc;
		this.blobDoc = blobDoc;
		this.nomeFile = nomeFile;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDocumento != null ? idDocumento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiDocumento)) {
			return false;
		}
		PaiDocumento other = (PaiDocumento) object;
		if ((this.idDocumento == null && other.idDocumento != null)
				|| (this.idDocumento != null && !this.idDocumento.equals(other.idDocumento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.PaiDocumento[idDocumento=" + idDocumento + "]";
	}
}
