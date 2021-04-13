/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class TipologiaInterventoDao extends PersistenceAdapter {

    public TipologiaInterventoDao(EntityManager em) {
        super(em);
    }

    public List<TipologiaIntervento> findAll() {
        Query query = getEntityManager().createNamedQuery("TipologiaIntervento.findAll");
        List<TipologiaIntervento> tipi = query.getResultList();
        return tipi;
    }

    public TipologiaIntervento findByCodTipint(String codTipint) {
        Query query = getEntityManager().createNamedQuery("TipologiaIntervento.findByCodTipint");
        query.setParameter("codTipint", codTipint);
        TipologiaIntervento tipo = (TipologiaIntervento) Connection.getSingleResult(query);
        return tipo;
    }

    public List<TipologiaIntervento> findByCodGrpTipInt(String codGrpTipint) {

        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM TipologiaIntervento t "
                + "WHERE t.codGrpTipint.codGrpTipint = :codGrpTipint");

        query.setParameter("codGrpTipint", codGrpTipint);

        List<TipologiaIntervento> tipi = query.getResultList();

        return tipi;
    }

    public List<TipologiaIntervento> findByClasse(ParametriIndata idParamClasseTipint) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM TipologiaIntervento t "
                + "WHERE t.idParamClasseTipint = :idParamClasseTipint "
                + "ORDER BY t.desTipint");

        query.setParameter("idParamClasseTipint", idParamClasseTipint);

        List<TipologiaIntervento> tipi = query.getResultList();

        return tipi;
    }

    public List<TipologiaIntervento> findByClasseEVisibile(ParametriIndata idParamClasseTipint) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM TipologiaIntervento t "
                + "WHERE t.idParamClasseTipint = :idParamClasseTipint "
                + "AND t.flgVis = 'S'"
                + "ORDER BY t.desTipint");

        query.setParameter("idParamClasseTipint", idParamClasseTipint);

        List<TipologiaIntervento> tipi = query.getResultList();

        return tipi;
    }
    
    public List<TipologiaIntervento> findAllVisibili() {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM TipologiaIntervento t "
                + "WHERE t.flgVis = 'S'"
                + "ORDER BY t.desTipint");

     

        List<TipologiaIntervento> tipi = query.getResultList();

        return tipi;
    }
}
