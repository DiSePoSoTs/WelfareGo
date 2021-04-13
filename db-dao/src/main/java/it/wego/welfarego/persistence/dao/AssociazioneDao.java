/**
 * 
 */
package it.wego.welfarego.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Associazione;

/**
 * @author Fabio Bonaccorso 
 * Dao per la gestione delle associazioni 
 *
 */
public class AssociazioneDao extends PersistenceAdapter{
	
	public AssociazioneDao(EntityManager em){
		super(em);
	}
	/**
	 * Ritorna un associazione in base al suo id 
	 * @param id
	 * @return
	 */
	 public Associazione findById(Integer id) {
	        TypedQuery<Associazione> query = getEntityManager().createNamedQuery("Associazione.findById", Associazione.class);
	        query.setParameter("id", id);
	        return PersistenceAdapter.getSingleResult(query);
	    }
	 /**
	  * ritorna tutte le associazione 
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Associazione> findAll(){
		 Query query = getEntityManager().createQuery("SELECT a FROM Associazione a ORDER BY a.nome ASC");
		 return query.getResultList();
	 }

}
