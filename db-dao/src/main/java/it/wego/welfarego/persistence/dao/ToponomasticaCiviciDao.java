package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.ToponomasticaCivici;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author giuseppe
 */
public class ToponomasticaCiviciDao extends PersistenceAdapter {

    public ToponomasticaCiviciDao(EntityManager em) {
        super(em);
    }

    public List<ToponomasticaCivici> findByDesCiviciWithLike(String codVia, String desCiv) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM ToponomasticaCivici t "
                + "WHERE t.toponomasticaCiviciPK.codVia = :codVia "
                + "AND LOWER(t.desCiv) LIKE :desCiv");
        query.setParameter("codVia", codVia);
        query.setParameter("desCiv", "%" + desCiv.toLowerCase() + "%");
        List<ToponomasticaCivici> risultati = query.getResultList();
        return risultati;
    }

    public List<ToponomasticaCivici> findByDesCivici(String codVia, String desCiv) {
        Query query = getEntityManager().createQuery("SELECT t "
                + "FROM ToponomasticaCivici t "
                + "WHERE t.toponomasticaCiviciPK.codVia = :codVia "
                + "AND LOWER(t.desCiv) = :desCiv");
        query.setParameter("codVia", codVia);
        query.setParameter("desCiv", desCiv.toLowerCase());
        List<ToponomasticaCivici> risultati = query.getResultList();
        return risultati;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(ToponomasticaCivici.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    @Deprecated
    public ToponomasticaCivici findByCodCiv(String codCiv) {
        TypedQuery<ToponomasticaCivici> query = getEntityManager().createNamedQuery("ToponomasticaCivici.findByCodCiv", ToponomasticaCivici.class);
        query.setParameter("codCiv", codCiv);
        return PersistenceAdapter.getSingleResult(query);

    }

    public List<ToponomasticaCivici> findAll() {
        return find(ToponomasticaCivici.class, "SELECT t FROM ToponomasticaCivici t", (Condition) null);
    }
}
