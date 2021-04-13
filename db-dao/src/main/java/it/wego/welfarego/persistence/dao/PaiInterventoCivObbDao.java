/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.utils.Connection;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class PaiInterventoCivObbDao implements ORMDao {

    private EntityManager em;

    public PaiInterventoCivObbDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Object object) throws Exception {
        try {
            em.persist(object);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void update(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public void delete(Object object) throws Exception {
        em.remove(object);
    }

    public List<PaiInterventoCivObb> findByCodPai(int codPai, int limit, int offset) {
        Query query = em.createNamedQuery("PaiInterventoCivObb.findByCodPai");
        query.setParameter("codPai", codPai);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<PaiInterventoCivObb> interventi = query.getResultList();
        return interventi;
    }

    public List<PaiInterventoCivObb> findByCodPaiCodTipintCntTipint(int codPai, String codTipint, int cntTipint, int limit, int offset) {
        Query query = em.createQuery("SELECT p "
                + "FROM PaiInterventoCivObb p "
                + "WHERE p.paiInterventoCivObbPK.codPai = :codPai "
                + "AND p.paiInterventoCivObbPK.codTipint = :codTipint "
                + "AND p.paiInterventoCivObbPK.cntTipint = :cntTipint ");
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipint);
        query.setParameter("cntTipint", cntTipint);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<PaiInterventoCivObb> interventi = query.getResultList();
        return interventi;
    }

    public PaiInterventoCivObb findByKey(int codPai, String codTipint, int cntTipint, int codAnaCo) {
        Query query = em.createQuery("SELECT p "
                + "FROM PaiInterventoCivObb p "
                + "WHERE p.paiInterventoCivObbPK.codPai = :codPai "
                + "AND p.paiInterventoCivObbPK.codTipint = :codTipint "
                + "AND p.paiInterventoCivObbPK.cntTipint = :cntTipint "
                + "AND p.paiInterventoCivObbPK.codAnaCo = :codAnaCo");
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipint);
        query.setParameter("cntTipint", cntTipint);
        query.setParameter("codAnaCo", codAnaCo);
        PaiInterventoCivObb intervento = (PaiInterventoCivObb) Connection.getSingleResult(query);
        return intervento;
    }
}
