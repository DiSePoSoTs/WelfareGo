package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Comune;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author giuseppe
 */
public class ComuneDao extends PersistenceAdapter {

    public ComuneDao(EntityManager em) {
        super(em);
    }

    public List<Comune> findByDesComuneWithLike(String codProv, String desComune) {
        Query query = getEntityManager().createQuery("SELECT c "
                + "FROM Comune c "
                + "WHERE c.comunePK.codProv = :codProv "
                + "AND LOWER(c.desCom) LIKE :desCom");
        query.setParameter("codProv", codProv);
        query.setParameter("desCom", "%" + desComune.toLowerCase() + "%");
        List<Comune> risultati = query.getResultList();
        return risultati;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Comune.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public int totalCount(String codCom) throws Exception {
        Query query = getEntityManager().createNamedQuery("Comune.findByCodCom");
        query.setParameter("codCom", codCom);
        try {
            return query.getResultList().size();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Comune findByCodCom(String codCom) {
        Query query = getEntityManager().createNamedQuery("Comune.findByCodCom");
        query.setParameter("codCom", codCom);
        Comune comune = (Comune) Connection.getSingleResult(query);
        return comune;
    }
    
    public Comune findByCodCatast(String codCatast) {
        Query query = getEntityManager().createNamedQuery("Comune.findByCodCatast");
        query.setParameter("codCatast", codCatast);
        
       List<Comune> comuni = (List<Comune>) query.getResultList();
       if(comuni.isEmpty()){
    	   return null;
       }
       else {
    	   return comuni.get(0);
       }
      
    }

    public List<Comune> findByName(String nomeComune) {
        Query query = getEntityManager().createQuery("SELECT c "
                + "FROM Comune c "
                + "WHERE UPPER(c.desCom) = :desCom");
        query.setParameter("desCom", nomeComune.toUpperCase().trim());
        List<Comune> risultati = query.getResultList();
        return risultati;
    }

    public List<Comune> findAll() {
        return find(Comune.class, "SELECT c FROM Comune c", (Condition) null);
    }
}
