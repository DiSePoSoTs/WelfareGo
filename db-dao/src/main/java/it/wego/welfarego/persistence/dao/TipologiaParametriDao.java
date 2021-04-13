/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.TipologiaParametri;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class TipologiaParametriDao implements ORMDao {

    private EntityManager em;

    public TipologiaParametriDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public void update(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public void delete(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public List<TipologiaParametri> findAll() {

        Query query = em.createQuery("SELECT tp FROM TipologiaParametri tp");

        List<TipologiaParametri> tipParams = query.getResultList();

        return tipParams;
    }

    public List<TipologiaParametri> findAllOrderedByDesc() {
        Query query = em.createQuery("SELECT tp FROM TipologiaParametri tp ORDER BY tp.desTipParam");
        List<TipologiaParametri> tipParams = query.getResultList();
        return tipParams;
    }

    public TipologiaParametri findByTipParam(String tipParam) {
        Query query = em.createNamedQuery("TipologiaParametri.findByTipParam");
        query.setParameter("tipParam", tipParam);

        TipologiaParametri parametro = (TipologiaParametri) Connection.getSingleResult(query);
        return parametro;
    }
}
