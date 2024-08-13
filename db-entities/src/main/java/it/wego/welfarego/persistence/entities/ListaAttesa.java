/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "LISTA_ATTESA")
@NamedQueries({ @NamedQuery(name = "ListaAttesa.findAll", query = "SELECT l FROM ListaAttesa l"),
		@NamedQuery(name = "ListaAttesa.findByCodListaAtt", query = "SELECT l FROM ListaAttesa l WHERE l.codListaAtt = :codListaAtt"),
		@NamedQuery(name = "ListaAttesa.findByDesListaAtt", query = "SELECT l FROM ListaAttesa l WHERE l.desListaAtt = :desListaAtt"),
		@NamedQuery(name = "ListaAttesa.findByFlgTipint", query = "SELECT l FROM ListaAttesa l WHERE l.flgTipint = :flgTipint"),
		@NamedQuery(name = "ListaAttesa.findByFlgCog", query = "SELECT l FROM ListaAttesa l WHERE l.flgCog = :flgCog"),
		@NamedQuery(name = "ListaAttesa.findByFlgNom", query = "SELECT l FROM ListaAttesa l WHERE l.flgNom = :flgNom"),
		@NamedQuery(name = "ListaAttesa.findByFlgCodFisc", query = "SELECT l FROM ListaAttesa l WHERE l.flgCodFisc = :flgCodFisc"),
		@NamedQuery(name = "ListaAttesa.findByFlgIsee", query = "SELECT l FROM ListaAttesa l WHERE l.flgIsee = :flgIsee"),
		@NamedQuery(name = "ListaAttesa.findByFlgDtNasc", query = "SELECT l FROM ListaAttesa l WHERE l.flgDtNasc = :flgDtNasc"),
		@NamedQuery(name = "ListaAttesa.findByFlgDtDom", query = "SELECT l FROM ListaAttesa l WHERE l.flgDtDom = :flgDtDom"),
		@NamedQuery(name = "ListaAttesa.findByFlgBina", query = "SELECT l FROM ListaAttesa l WHERE l.flgBina = :flgBina"),
		@NamedQuery(name = "ListaAttesa.findByFlgAs", query = "SELECT l FROM ListaAttesa l WHERE l.flgAs = :flgAs"),
		@NamedQuery(name = "ListaAttesa.findByFlgUot", query = "SELECT l FROM ListaAttesa l WHERE l.flgUot = :flgUot"),
		@NamedQuery(name = "ListaAttesa.findByFlgDistSan", query = "SELECT l FROM ListaAttesa l WHERE l.flgDistSan = :flgDistSan"),
		@NamedQuery(name = "ListaAttesa.findByFlgRef", query = "SELECT l FROM ListaAttesa l WHERE l.flgRef = :flgRef"),
		@NamedQuery(name = "ListaAttesa.findByFlgNumFigli", query = "SELECT l FROM ListaAttesa l WHERE l.flgNumFigli = :flgNumFigli") })
public class ListaAttesa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "COD_LISTA_ATT", nullable = false)
	private Integer codListaAtt;

	public char getFlgTipint() {
		return flgTipint;
	}

	public char getFlgCog() {
		return flgCog;
	}

	public char getFlgNom() {
		return flgNom;
	}

	public char getFlgCodFisc() {
		return flgCodFisc;
	}

	public char getFlgIsee() {
		return flgIsee;
	}

	public char getFlgDtNasc() {
		return flgDtNasc;
	}

	public char getFlgDtDom() {
		return flgDtDom;
	}

	public char getFlgBina() {
		return flgBina;
	}

	public char getFlgAs() {
		return flgAs;
	}

	public char getFlgUot() {
		return flgUot;
	}

	public char getFlgDistSan() {
		return flgDistSan;
	}

	public char getFlgRef() {
		return flgRef;
	}

	public char getFlgNumFigli() {
		return flgNumFigli;
	}

	@Basic(optional = false)
	@Column(name = "DES_LISTA_ATT", nullable = false, length = 765)
	private String desListaAtt;

	@Basic(optional = false)
	@Column(name = "FLG_TIPINT", nullable = false)
	private char flgTipint;

	@Basic(optional = false)
	@Column(name = "FLG_COG", nullable = false)
	private char flgCog;

	@Basic(optional = false)
	@Column(name = "FLG_NOM", nullable = false)
	private char flgNom;

	@Basic(optional = false)
	@Column(name = "FLG_COD_FISC", nullable = false)
	private char flgCodFisc;

	@Basic(optional = false)
	@Column(name = "FLG_ISEE", nullable = false)
	private char flgIsee;

	@Basic(optional = false)
	@Column(name = "FLG_DT_NASC", nullable = false)
	private char flgDtNasc;

	@Basic(optional = false)
	@Column(name = "FLG_DT_DOM", nullable = false)
	private char flgDtDom;

	public String getDesListaAtt() {
		return desListaAtt;
	}

	public Integer getCodListaAtt() {
		return codListaAtt;
	}

	@Basic(optional = false)
	@Column(name = "FLG_BINA", nullable = false)
	private char flgBina;

	@Basic(optional = false)
	@Column(name = "FLG_AS", nullable = false)
	private char flgAs;

	@Basic(optional = false)
	@Column(name = "FLG_UOT", nullable = false)
	private char flgUot;

	@Basic(optional = false)
	@Column(name = "FLG_DIST_SAN", nullable = false)
	private char flgDistSan;

	@Basic(optional = false)
	@Column(name = "FLG_REF", nullable = false)
	private char flgRef;

	@Basic(optional = false)
	@Column(name = "FLG_NUM_FIGLI", nullable = false)
	private char flgNumFigli;

	@OneToMany(mappedBy = "codListaAtt")
	private List<TipologiaIntervento> tipologiaInterventoList;

	public ListaAttesa() {
	}

	public ListaAttesa(Integer codListaAtt) {
		this.codListaAtt = codListaAtt;
	}

	public ListaAttesa(Integer codListaAtt, String desListaAtt, char flgTipint, char flgCog, char flgNom,
			char flgCodFisc, char flgIsee, char flgDtNasc, char flgDtDom, char flgBina, char flgAs, char flgUot,
			char flgDistSan, char flgRef, char flgNumFigli) {
		this.codListaAtt = codListaAtt;
		this.desListaAtt = desListaAtt;
		this.flgTipint = flgTipint;
		this.flgCog = flgCog;
		this.flgNom = flgNom;
		this.flgCodFisc = flgCodFisc;
		this.flgIsee = flgIsee;
		this.flgDtNasc = flgDtNasc;
		this.flgDtDom = flgDtDom;
		this.flgBina = flgBina;
		this.flgAs = flgAs;
		this.flgUot = flgUot;
		this.flgDistSan = flgDistSan;
		this.flgRef = flgRef;
		this.flgNumFigli = flgNumFigli;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codListaAtt != null ? codListaAtt.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ListaAttesa)) {
			return false;
		}
		ListaAttesa other = (ListaAttesa) object;
		if ((this.codListaAtt == null && other.codListaAtt != null)
				|| (this.codListaAtt != null && !this.codListaAtt.equals(other.codListaAtt))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.ListaAttesa[codListaAtt=" + codListaAtt + "]";
	}

}
