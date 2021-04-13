/**
 * 
 */
package it.wego.welfarego.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Struttura;

/**
 * @author Fabio Bonaccorso
 * Dao Per la gestione delle strutture 
 *
 */
public class StrutturaDao extends PersistenceAdapter {
	 
	public StrutturaDao(EntityManager em) {
	        super(em);
	    }
	/**
	 * Ritorna una singola struttura dal suo id 
	 * @param id
	 * @return
	 */
	public Struttura findById(Integer id){
		TypedQuery<Struttura> query = getEntityManager().createNamedQuery("Struttura.findById",Struttura.class);
		query.setParameter("id", id);
		return PersistenceAdapter.getSingleResult(query);
	}
	
	/**
	 * Torna la lista di strutture con quel nome 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Struttura> findStutturaByName(String name){
		Query  query =  getEntityManager().createQuery("Select s"
				+" FROM Struttura s"
				+" WHERE s.nome LIKE :nome");
		
		query.setParameter("nome", "%"+name+"%");
		return query.getResultList();
		
	}
	/**
	 * Ritorna tutte le strutture associate a un intervento.
	 * @param codTipint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Struttura> findStrutturaByCodTipInt(String codTipint){
		Query  query =  getEntityManager().createQuery("Select s"
				+" FROM Struttura s"
				+" WHERE s.intervento.codTipint = :intervento order by s.nome");
		query.setParameter("intervento", codTipint);
	     return query.getResultList();
	}
	
	

}
