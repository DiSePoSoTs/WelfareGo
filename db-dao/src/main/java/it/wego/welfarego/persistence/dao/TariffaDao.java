package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Tariffa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Dao per la gestione delle tariffe.
 * @author DOTCOM s.r.l.
 */
public class TariffaDao extends PersistenceAdapter
{

	public TariffaDao(EntityManager em)
	{
		super(em);
	}
	
	/**
	 * Restituisce la lista delle tariffe in base alla strittura selezionata.
	 * @param idStruttura identificativo della struttura.
	 * @return Lista delle tariffe.
	 */
	public List<Tariffa> findTariffaByStruttura(Integer idStruttura)
	{
		TypedQuery<Tariffa> tariffeQuery = getEntityManager().createNamedQuery("Tariffa.findByStruttura", Tariffa.class);
		tariffeQuery.setParameter("id", idStruttura);
		return tariffeQuery.getResultList();
	}
}
