package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Toponomastica;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author giuseppe
 */
public class ToponomasticaDao extends PersistenceAdapter {


    public ToponomasticaDao(EntityManager em) {
       super(em);
    }

    public List<Toponomastica> findByDesViaWithLike(String codCom, String desVia) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM Toponomastica t "
                + "WHERE t.toponomasticaPK.codCom = :codCom "
                + "AND LOWER(t.desVia) LIKE :desVia");
        query.setParameter("codCom", codCom);
        query.setParameter("desVia", "%" + desVia.toLowerCase() + "%");
        List<Toponomastica> risultati = query.getResultList();
        return risultati;
    }

    public List<Toponomastica> findByDesViaWithLike(String desVia) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM Toponomastica t "
                + "WHERE LOWER(t.desVia) LIKE :desVia");
        query.setParameter("desVia", "%" + desVia.toLowerCase() + "%");
        List<Toponomastica> risultati = query.getResultList();
        return risultati;
    }

    public List<Toponomastica> findByDesVia(String desVia) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM Toponomastica t "
                + "WHERE LOWER(t.desVia) = :desVia");
        query.setParameter("desVia", desVia.toLowerCase());
        List<Toponomastica> risultati = query.getResultList();
        return risultati;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Toponomastica.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public Toponomastica findByCodVia(String codVia) {
        TypedQuery<Toponomastica> query = getEntityManager().createNamedQuery("Toponomastica.findByCodVia", Toponomastica.class);
        query.setParameter("codVia", codVia);
        return PersistenceAdapter.getSingleResult(query);
    }
    
    public List<Toponomastica> findAll() {
        return find(Toponomastica.class, "SELECT t FROM Toponomastica t", (Condition) null);
    }
}
