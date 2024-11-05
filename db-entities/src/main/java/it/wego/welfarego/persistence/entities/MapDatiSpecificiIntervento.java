/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "MAP_DATI_SPECIFICI_INTERVENTO")
@NamedQueries({
    @NamedQuery(name = "MapDatiSpecificiIntervento.findAll", query = "SELECT m FROM MapDatiSpecificiIntervento m"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByCodPai", query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.mapDatiSpecificiInterventoPK.codPai = :codPai"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByCodTipint", query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.mapDatiSpecificiInterventoPK.codTipint = :codTipint"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByCntTipint", query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.mapDatiSpecificiInterventoPK.cntTipint = :cntTipint"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByCodCampo", query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.mapDatiSpecificiInterventoPK.codCampo = :codCampo"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByCodValCampo", query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.codValCampo = :codValCampo"),
    @NamedQuery(name = "MapDatiSpecificiIntervento.findByValCampo",  query = "SELECT m FROM MapDatiSpecificiIntervento m WHERE m.valCampo = :valCampo")})
public class MapDatiSpecificiIntervento  implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MapDatiSpecificiInterventoPK mapDatiSpecificiInterventoPK = new MapDatiSpecificiInterventoPK();
    
    @Column(name = "COD_VAL_CAMPO", length = 20)
    private String codValCampo;
    
    @Basic(optional = false)
    @Column(name = "VAL_CAMPO", nullable = false, length = 4000)
    private String valCampo = " ";
    
    @JoinColumns({
        @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private PaiIntervento paiIntervento;
    
    @JoinColumn(name = "COD_CAMPO", referencedColumnName = "COD_CAMPO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DatiSpecifici datiSpecifici;
    
    // required for stupid query on pagamenti
    @Column(name = "COD_CAMPO", nullable = false, insertable = false, updatable = false)
    private String codCampo;

    public MapDatiSpecificiIntervento() {
    }

    public MapDatiSpecificiIntervento(MapDatiSpecificiInterventoPK mapDatiSpecificiInterventoPK) {
        this.mapDatiSpecificiInterventoPK = mapDatiSpecificiInterventoPK;
    }

    public MapDatiSpecificiIntervento(MapDatiSpecificiInterventoPK mapDatiSpecificiInterventoPK, String valCampo) {
        this.mapDatiSpecificiInterventoPK = mapDatiSpecificiInterventoPK;
        Preconditions.checkNotNull(valCampo);
        this.valCampo = valCampo;
    }

    public MapDatiSpecificiIntervento(Integer codPai, String codTipint, Integer cntTipint, String codCampo) {
        this.mapDatiSpecificiInterventoPK = new MapDatiSpecificiInterventoPK(codPai, codTipint, cntTipint, codCampo);
    }

    public MapDatiSpecificiIntervento(DatiSpecifici datiSpecifici) {
        this(null, null, null, datiSpecifici.getCodCampo());
        this.datiSpecifici = datiSpecifici;
    }

    public MapDatiSpecificiInterventoPK getMapDatiSpecificiInterventoPK() {
        return mapDatiSpecificiInterventoPK;
    }

    public void setMapDatiSpecificiInterventoPK(MapDatiSpecificiInterventoPK mapDatiSpecificiInterventoPK) {
        this.mapDatiSpecificiInterventoPK = mapDatiSpecificiInterventoPK;
    }

    public String getCodValCampo() {
        return codValCampo;
    }

    public void setCodValCampo(String codValCampo) {
        this.codValCampo = codValCampo;
    }

    public String getValCampo() {
        return valCampo;
    }

    public String getCodCampo() {
        return mapDatiSpecificiInterventoPK.getCodCampo();
    }

    public void setValCampo(String valCampo) {
        Preconditions.checkNotNull(valCampo);
        this.valCampo = valCampo;
    }

    public PaiIntervento getPaiIntervento() {
        return paiIntervento;
    }

    public void setPaiIntervento(PaiIntervento paiIntervento) {
        this.paiIntervento = paiIntervento;
    }

    public DatiSpecifici getDatiSpecifici() {
        return datiSpecifici;
    }

    public void setDatiSpecifici(DatiSpecifici datiSpecifici) {
        this.datiSpecifici = datiSpecifici;
        this.mapDatiSpecificiInterventoPK.setCodCampo(datiSpecifici.getCodCampo());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mapDatiSpecificiInterventoPK != null ? mapDatiSpecificiInterventoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MapDatiSpecificiIntervento)) {
            return false;
        }
        MapDatiSpecificiIntervento other = (MapDatiSpecificiIntervento) object;
        if ((this.mapDatiSpecificiInterventoPK == null && other.mapDatiSpecificiInterventoPK != null) || (this.mapDatiSpecificiInterventoPK != null && !this.mapDatiSpecificiInterventoPK.equals(other.mapDatiSpecificiInterventoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento[mapDatiSpecificiInterventoPK=" + mapDatiSpecificiInterventoPK + "]";
    }
    
    
}
