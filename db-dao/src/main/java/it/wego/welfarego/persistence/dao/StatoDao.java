/*
 * To change this tgetEntityManager()plate, choose Tools | TgetEntityManager()plates
 * and open the tgetEntityManager()plate in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.Strings;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Stato;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

/**
 *
 * @author giuseppe
 */
public class StatoDao extends PersistenceAdapter {

    public StatoDao(EntityManager em) {
        super(em);
    }

    public List<Stato> findByDesStatoWithLike(String desStato) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Stato> criteria = cb.createQuery(Stato.class);
        EntityType<Stato> type = getEntityManager().getMetamodel().entity(Stato.class);
        Root<Stato> root = criteria.from(Stato.class);
        criteria.where(cb.like(cb.lower(root.get(type.getDeclaredSingularAttribute("desStato", String.class))), "%" + desStato.replace("'", "") + "%"));
        TypedQuery query = getEntityManager().createQuery(criteria);
        List<Stato> risultati = query.getResultList();
        return risultati;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Stato.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public Stato findByCodStato(String codStato) {
        TypedQuery<Stato> query = getEntityManager().createNamedQuery("Stato.findByCodStato", Stato.class);
        query.setParameter("codStato", codStato);
        return getSingleResult(query);
    }
    
    public Stato findByCodiceCatastale(String codCatasto){
    	 TypedQuery<Stato> query = getEntityManager().createNamedQuery("Stato.findByCodCatast", Stato.class);
    	 query.setParameter("codCatast", codCatasto);
    	 return getSingleResult(query);
    }

    public List<Stato> findAll() {
        return find(Stato.class, "SELECT s FROM Stato s");
    }

    public @Nullable
    Stato findByDesStatoLike(@Nullable String desStato) {
        if (Strings.isNullOrEmpty(desStato)) {
            return null;
        } else {
            return findOne(Stato.class, "SELECT s FROM Stato s WHERE s.desStato LIKE '" + desStato + "%'");
        }
    }
}
