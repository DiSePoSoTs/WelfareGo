/**
 * 
 */
package it.wego.welfarego.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.NoteCondivise;

/**
 * @author Fabio Bonaccorso
 * Dao per le note condivise 
 *
 */
public class NoteCondiviseDao extends PersistenceAdapter{

	public NoteCondiviseDao(EntityManager em){
		super(em);
	}
	
	@SuppressWarnings("unchecked")
	public List<NoteCondivise> getNoteByCodAna(Integer codAna){
		Query query = getEntityManager().createQuery("SELECT n FROM NoteCondivise n WHERE n.anagrafeSoc.codAna = :codAna", NoteCondivise.class);
		query.setParameter("codAna", codAna);
		List<NoteCondivise> result = query.getResultList();
		return result;
	}
}
