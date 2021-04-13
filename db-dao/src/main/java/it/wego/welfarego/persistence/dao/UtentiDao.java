package it.wego.welfarego.persistence.dao;

import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author giuseppe
 */
public class UtentiDao extends PersistenceAdapter implements ORMDao {

    public UtentiDao(EntityManager em) {
        super(em);
    }

    public List<Utenti> findAll(int limit, int start) {
        Query query = getEntityManager().createNamedQuery("Utenti.findAll");

        query.setMaxResults(limit);
        query.setFirstResult(start);

        List<Utenti> utenti = query.getResultList();
        return utenti;
    }

    public List<Utenti> findAll() {
        return getEntityManager().createNamedQuery("Utenti.findAll").getResultList();
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Utenti.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public List<Utenti> findByTipologia(String tipoUtente) {
        String query = "SELECT u FROM Utenti u LEFT JOIN u.idParamLvlAbil lvlab LEFT JOIN lvlab.idParam par";
        return find(Utenti.class, query, ConditionBuilder.equals("par.codParam", tipoUtente));
    }

    public Utenti findByCodUte(String codUte) {
        TypedQuery<Utenti> query = getEntityManager().createNamedQuery("Utenti.findByCodUte", Utenti.class);
        query.setParameter("codUte", Integer.valueOf(codUte));
        return getSingleResult(query);
    }

    public Utenti findByCodFisc(String codFisc) {
        TypedQuery<Utenti> query = getEntityManager().createNamedQuery("Utenti.findByCodFisc", Utenti.class);
        query.setParameter("codFisc", codFisc);
        return getSingleResult(query);
    }

    public Utenti findByUsername(String username) {
        TypedQuery<Utenti> query = getEntityManager().createQuery("SELECT u FROM Utenti u WHERE UPPER(u.username) = :username", Utenti.class);
        query.setParameter("username", username.toUpperCase());
        return getSingleResult(query);
    }

    public List<Utenti> findByIdParamUotAndTipUtente(String codUot, String codParam) {
        if (!StringUtils.isBlank(codUot)) {
            ParametriIndataDao parametriIndataDao = new ParametriIndataDao(getEntityManager());
            ParametriIndata idParamUot = parametriIndataDao.findByIdParamIndata(Integer.valueOf(codUot));
            Query query = getEntityManager().createQuery("SELECT u "
                    + "FROM Utenti u "
                    + "JOIN u.idParamLvlAbil b "
                    + "JOIN b.idParam c "
                    + "WHERE u.idParamUot = :idParamUot "
                    + "AND c.codParam = :codParam");
            query.setParameter("idParamUot", idParamUot);
            query.setParameter("codParam", codParam);
            List<Utenti> utenti = query.getResultList();
            return utenti;
        } else {
            return null;
        }
    }
}
