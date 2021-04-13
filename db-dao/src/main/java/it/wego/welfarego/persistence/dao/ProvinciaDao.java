/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Provincia;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author giuseppe
 */
public class ProvinciaDao extends PersistenceAdapter {

    public ProvinciaDao(EntityManager em) {
        super(em);
    }

    public List<Provincia> findByDesProvinciaWithLike(String codStato, String desProvincia) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Provincia p "
                + "WHERE p.provinciaPK.codStato = :codStato "
                + "AND LOWER(p.desProv) LIKE :desProv");
        query.setParameter("codStato", codStato);
        query.setParameter("desProv", "%" + desProvincia.toLowerCase() + "%");
        List<Provincia> province = query.getResultList();
        return province;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Provincia.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public Provincia findByCodProv(String codProv) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Provincia p "
                + "WHERE p.provinciaPK.codProv = :codProv");
        query.setParameter("codProv", codProv);
        Provincia provincia = (Provincia) Connection.getSingleResult(query);
        return provincia;
    }

    public List<Provincia> findAll() {
        return find(Provincia.class, "SELECT p FROM Provincia p", (Condition) null);
    }
}
