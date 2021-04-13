/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author giuseppe
 */
public class MapDatiSpecTipIntDao extends PersistenceAdapter {

    public MapDatiSpecTipIntDao(EntityManager em) {
        super(em);
    }


    public List<MapDatiSpecTipint> findByCodTipInt(String codTipint) {
        TypedQuery<MapDatiSpecTipint> query = getEntityManager().createQuery("SELECT m "
                + "FROM MapDatiSpecTipint m "
                + "WHERE m.mapDatiSpecTipintPK.codTipint = :codTipint "
                + "ORDER BY m.rowCampo ASC", MapDatiSpecTipint.class);

        query.setParameter("codTipint", codTipint);
        List<MapDatiSpecTipint> map = query.getResultList();
        return map;
    }

    public List<MapDatiSpecTipint> findAll() {
        return find(MapDatiSpecTipint.class,"SELECT m FROM MapDatiSpecTipint m");
    }
}
