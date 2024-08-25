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


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "DATI_SPECIFICI")
@NamedQueries({ @NamedQuery(name = "DatiSpecifici.findAll", query = "SELECT d FROM DatiSpecifici d"),
		@NamedQuery(name = "DatiSpecifici.findByCodCampo", query = "SELECT d FROM DatiSpecifici d WHERE d.codCampo = :codCampo"),
		@NamedQuery(name = "DatiSpecifici.findByDesCampo", query = "SELECT d FROM DatiSpecifici d WHERE d.desCampo = :desCampo"),
		@NamedQuery(name = "DatiSpecifici.findByValDef", query = "SELECT d FROM DatiSpecifici d WHERE d.valDef = :valDef"),
		@NamedQuery(name = "DatiSpecifici.findByRegExpr", query = "SELECT d FROM DatiSpecifici d WHERE d.regExpr = :regExpr"),
		@NamedQuery(name = "DatiSpecifici.findByCodCampoCsr", query = "SELECT d FROM DatiSpecifici d WHERE d.codCampoCsr = :codCampoCsr"),
		@NamedQuery(name = "DatiSpecifici.findByMsgErrore", query = "SELECT d FROM DatiSpecifici d WHERE d.msgErrore = :msgErrore"),
		@NamedQuery(name = "DatiSpecifici.findByLunghezza", query = "SELECT d FROM DatiSpecifici d WHERE d.lunghezza = :lunghezza"),
		@NamedQuery(name = "DatiSpecifici.findByDecimali", query = "SELECT d FROM DatiSpecifici d WHERE d.decimali = :decimali") })
public class DatiSpecifici implements Serializable {

	public static final char DS_COMBO = 'L', DS_NUM = 'I', DS_TESTO = 'T', DS_TESTO_MULTI = 'X', DS_DATA = 'D';
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_CAMPO", nullable = false, length = 20)
	private String codCampo;

	public String getCodCampo() {
		return codCampo;
	}

	@Basic(optional = false)
	@Column(name = "DES_CAMPO", nullable = false, length = 60)
	private String desCampo;

	public char getFlgObb() {
		return flgObb;
	}

	public void setFlgObb(char flgObb) {
		this.flgObb = flgObb;
	}

	public void setTipoCampo(char tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public void setValAmm(String valAmm) {
		this.valAmm = valAmm;
	}

	public void setFlgEdit(char flgEdit) {
		this.flgEdit = flgEdit;
	}

	public void setFlgVis(char flgVis) {
		this.flgVis = flgVis;
	}

	public void setValDef(String valDef) {
		this.valDef = valDef;
	}

	public void setRegExpr(String regExpr) {
		this.regExpr = regExpr;
	}

	public void setMsgErrore(String msgErrore) {
		this.msgErrore = msgErrore;
	}

	public void setLunghezza(short lunghezza) {
		this.lunghezza = lunghezza;
	}

	public void setCodCampo(String codCampo) {
		this.codCampo = codCampo;
	}

	public void setDesCampo(String desCampo) {
		this.desCampo = desCampo;
	}

	public void setCodCampoCsr(String codCampoCsr) {
		this.codCampoCsr = codCampoCsr;
	}

	public void setDecimali(Short decimali) {
		this.decimali = decimali;
	}

	public char getTipoCampo() {
		return tipoCampo;
	}

	public String getValAmm() {
		return valAmm;
	}

	public char getFlgEdit() {
		return flgEdit;
	}

	public char getFlgVis() {
		return flgVis;
	}

	public String getValDef() {
		return valDef;
	}

	public String getRegExpr() {
		return regExpr;
	}

	public String getMsgErrore() {
		return msgErrore;
	}

	public short getLunghezza() {
		return lunghezza;
	}

	public List<MapDatiSpecTipint> getMapDatiSpecTipintList() {
		return mapDatiSpecTipintList;
	}

	public List<MapDatiSpecificiIntervento> getMapDatiSpecificiInterventoList() {
		return mapDatiSpecificiInterventoList;
	}

	@Basic(optional = false)
	@Column(name = "FLG_OBB", nullable = false)
	private char flgObb;

	@Basic(optional = false)
	@Column(name = "TIPO_CAMPO", nullable = false)
	private char tipoCampo;

	@Lob
	@Column(name = "VAL_AMM")
	private String valAmm;

	@Basic(optional = false)
	@Column(name = "FLG_EDIT", nullable = false)
	private char flgEdit;

	public String getCodCampoCsr() {
		return codCampoCsr;
	}

	@Basic(optional = false)
	@Column(name = "FLG_VIS", nullable = false)
	private char flgVis;

	@Column(name = "VAL_DEF", length = 60)
	private String valDef;

	@Column(name = "REG_EXPR", length = 255)
	private String regExpr;

	@Column(name = "COD_CAMPO_CSR", length = 60)
	private String codCampoCsr;

	@Column(name = "MSG_ERRORE", length = 255)
	private String msgErrore;

	@Basic(optional = false)
	@Column(name = "LUNGHEZZA", nullable = false)
	private short lunghezza;

	public String getDesCampo() {
		return desCampo;
	}

	public Short getDecimali() {
		return decimali;
	}

	@Column(name = "DECIMALI")
	private Short decimali;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "datiSpecifici")
	private List<MapDatiSpecTipint> mapDatiSpecTipintList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "datiSpecifici")
	private List<MapDatiSpecificiIntervento> mapDatiSpecificiInterventoList;

	public DatiSpecifici() {
	}

	public DatiSpecifici(String codCampo) {
		this.codCampo = codCampo;
	}

	public DatiSpecifici(String codCampo, String desCampo, char flgObb, char tipoCampo, char flgEdit, char flgVis,
			short lunghezza) {
		this.codCampo = codCampo;
		this.desCampo = desCampo;
		this.flgObb = flgObb;
		this.tipoCampo = tipoCampo;
		this.flgEdit = flgEdit;
		this.flgVis = flgVis;
		this.lunghezza = lunghezza;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codCampo != null ? codCampo.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DatiSpecifici)) {
			return false;
		}
		DatiSpecifici other = (DatiSpecifici) object;
		if ((this.codCampo == null && other.codCampo != null)
				|| (this.codCampo != null && !this.codCampo.equals(other.codCampo))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.DatiSpecifici[codCampo=" + codCampo + "]";
	}

	public String getCodCampoDsNormalized() {
		if (!codCampo.startsWith("ds_")) {
			return "ds_" + codCampo;
		} else {
			return codCampo;
		}
	}
}
