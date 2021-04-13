/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class MapDatiSpecificiInterventoDao extends PersistenceAdapter {

    public MapDatiSpecificiInterventoDao(EntityManager em) {
        super(em);
    }


    public List<MapDatiSpecificiIntervento> findByPaiIntervento(PaiIntervento intervento) {
        Query query = getEntityManager().createQuery("SELECT m "
                + "FROM MapDatiSpecificiIntervento m "
                + "WHERE m.mapDatiSpecificiInterventoPK.codPai = :codPai "
                + "AND m.mapDatiSpecificiInterventoPK.codTipint = :codTipint "
                + "AND m.mapDatiSpecificiInterventoPK.cntTipint = :cntTipint ");

        query.setParameter("codPai", intervento.getPaiInterventoPK().getCodPai());
        query.setParameter("codTipint", intervento.getPaiInterventoPK().getCodTipint());
        query.setParameter("cntTipint", intervento.getPaiInterventoPK().getCntTipint());
        List<MapDatiSpecificiIntervento> map = query.getResultList();
        return map;
    }
    
    public MapDatiSpecificiIntervento findInterventoSimia(String codiceSimia){
    	   Query query = getEntityManager().createQuery("SELECT m "
                   + "FROM MapDatiSpecificiIntervento m "
                   + "WHERE m.mapDatiSpecificiInterventoPK.codTipint = 'EC100'"
                   + "AND  m.mapDatiSpecificiInterventoPK.codCampo = 'ds_simia' "
                   + "AND m.valCampo = :valCampo");
    	   query.setParameter("valCampo", codiceSimia);
    	   try {
    	  MapDatiSpecificiIntervento result =  (MapDatiSpecificiIntervento) query.getSingleResult();
    	  return result;
    	   }
    	   catch(NoResultException e){
    		   return null;
    	   }
	
    }

    public List<MapDatiSpecificiIntervento> createForTipInt(final String codTipint) {
        return Lists.newArrayList(Iterables.transform(new MapDatiSpecTipIntDao(getEntityManager()).findByCodTipInt(codTipint), new Function<MapDatiSpecTipint, MapDatiSpecificiIntervento>() {
            public MapDatiSpecificiIntervento apply(MapDatiSpecTipint datiSpecTipint) {
                MapDatiSpecificiIntervento mapDatiSpecificiIntervento = new MapDatiSpecificiIntervento(datiSpecTipint.getDatiSpecifici());
                mapDatiSpecificiIntervento.getMapDatiSpecificiInterventoPK().setCodTipint(codTipint);
                return mapDatiSpecificiIntervento;
            }
        }));
    }
    
    public static Predicate<MapDatiSpecificiIntervento> datiSpecificiPaiInterventoPredicate(List<MapDatiSpecificiIntervento> listaDatiSpecifici) {
    	 final Set<String> datiSpecificiPresenti = Sets.newHashSet(Iterables.transform(listaDatiSpecifici, new Function<MapDatiSpecificiIntervento, String>() {
             public String apply(MapDatiSpecificiIntervento input) {
                 return input.getCodCampo();
             }
         }));
    	 
    	 return ds -> datiSpecificiPresenti.contains(ds.getCodCampo());
    }

    public void fillDatiSpecificiForPaiIntervento(final PaiIntervento paiIntervento) {
        List<MapDatiSpecificiIntervento> datiSpecificiInterventoList = createForTipInt(paiIntervento.getPaiInterventoPK().getCodTipint());
        
        List<MapDatiSpecificiIntervento> datiSpecificiPaiIntervento = paiIntervento.getMapDatiSpecificiInterventoList(); 
        
        if (datiSpecificiPaiIntervento != null) {
            datiSpecificiInterventoList.removeIf(datiSpecificiPaiInterventoPredicate(datiSpecificiPaiIntervento));
            //devo aggiungere quelli di paiIntervento
            datiSpecificiInterventoList.addAll(datiSpecificiPaiIntervento);
        }
        // really necessary? should verifiy . . 
        for (MapDatiSpecificiIntervento datiSpecificiIntervento : datiSpecificiInterventoList) {
            datiSpecificiIntervento.getMapDatiSpecificiInterventoPK().setCodPai(paiIntervento.getPaiInterventoPK().getCodPai());
            datiSpecificiIntervento.getMapDatiSpecificiInterventoPK().setCntTipint(paiIntervento.getPaiInterventoPK().getCntTipint());
        }
        paiIntervento.setMapDatiSpecificiInterventoList(datiSpecificiInterventoList);
    }

    public List<MapDatiSpecificiIntervento> findAll() {
        return find(MapDatiSpecificiIntervento.class, "SELECT m FROM MapDatiSpecificiIntervento m");
    }
}
