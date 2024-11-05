/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "MAP_DATI_SPEC_TIPINT")
@NamedQueries({ @NamedQuery(name = "MapDatiSpecTipint.findAll", query = "SELECT m FROM MapDatiSpecTipint m"),
		@NamedQuery(name = "MapDatiSpecTipint.findByCodTipint", query = "SELECT m FROM MapDatiSpecTipint m WHERE m.mapDatiSpecTipintPK.codTipint = :codTipint"),
		@NamedQuery(name = "MapDatiSpecTipint.findByCodCampo", query = "SELECT m FROM MapDatiSpecTipint m WHERE m.mapDatiSpecTipintPK.codCampo = :codCampo"),
		@NamedQuery(name = "MapDatiSpecTipint.findByRowCampo", query = "SELECT m FROM MapDatiSpecTipint m WHERE m.rowCampo = :rowCampo"),
		@NamedQuery(name = "MapDatiSpecTipint.findByColCampo", query = "SELECT m FROM MapDatiSpecTipint m WHERE m.colCampo = :colCampo") })
public class MapDatiSpecTipint implements Serializable {

	public short getRowCampo() {
		return rowCampo;
	}

	public TipologiaIntervento getTipologiaIntervento() {
		return tipologiaIntervento;
	}

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected MapDatiSpecTipintPK mapDatiSpecTipintPK;

	public MapDatiSpecTipintPK getMapDatiSpecTipintPK() {
		return mapDatiSpecTipintPK;
	}

	@Basic(optional = false)
	@Column(name = "ROW_CAMPO", nullable = false)
	private short rowCampo;

	public void setRowCampo(short rowCampo) {
		this.rowCampo = rowCampo;
	}

	public void setColCampo(short colCampo) {
		this.colCampo = colCampo;
	}

	public void setMapDatiSpecTipintPK(MapDatiSpecTipintPK mapDatiSpecTipintPK) {
		this.mapDatiSpecTipintPK = mapDatiSpecTipintPK;
	}

	@Basic(optional = false)
	@Column(name = "COL_CAMPO", nullable = false)
	private short colCampo;

	@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipologiaIntervento tipologiaIntervento;

	@JoinColumn(name = "COD_CAMPO", referencedColumnName = "COD_CAMPO", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private DatiSpecifici datiSpecifici;

	public short getColCampo() {
		return colCampo;
	}

	public DatiSpecifici getDatiSpecifici() {
		return datiSpecifici;
	}

	public MapDatiSpecTipint() {
	}

	public MapDatiSpecTipint(MapDatiSpecTipintPK mapDatiSpecTipintPK) {
		this.mapDatiSpecTipintPK = mapDatiSpecTipintPK;
	}

	public MapDatiSpecTipint(MapDatiSpecTipintPK mapDatiSpecTipintPK, short rowCampo, short colCampo) {
		this.mapDatiSpecTipintPK = mapDatiSpecTipintPK;
		this.rowCampo = rowCampo;
		this.colCampo = colCampo;
	}

	public MapDatiSpecTipint(String codTipint, String codCampo) {
		this.mapDatiSpecTipintPK = new MapDatiSpecTipintPK(codTipint, codCampo);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (mapDatiSpecTipintPK != null ? mapDatiSpecTipintPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MapDatiSpecTipint)) {
			return false;
		}
		MapDatiSpecTipint other = (MapDatiSpecTipint) object;
		if ((this.mapDatiSpecTipintPK == null && other.mapDatiSpecTipintPK != null)
				|| (this.mapDatiSpecTipintPK != null && !this.mapDatiSpecTipintPK.equals(other.mapDatiSpecTipintPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.MapDatiSpecTipint[mapDatiSpecTipintPK=" + mapDatiSpecTipintPK
				+ "]";
	}

}
