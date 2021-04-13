package it.wego.welfarego.persistence.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.PaiIntervento;


/**
 * 
 */

/**
 * @author fabio Bonaccorso
 *
 */
public class InterventiAssociatiDao extends PersistenceAdapter {
	
	public InterventiAssociatiDao (EntityManager em){
		super(em);
	}
	
	/**
	 * Ritorna una singola associazione fra interventi  dal suo id 
	 * @param id
	 * @return
	 */
	public InterventiAssociati findById(Integer id){
		TypedQuery<InterventiAssociati> query = getEntityManager().createNamedQuery("InterventiAssociati.findById",InterventiAssociati.class);
		query.setParameter("id", id);
		return PersistenceAdapter.getSingleResult(query);
	}
	/**
	 * Ritorna tutti gli interventi figli di uno stesso padre
	 * @param padre
	 * @return
	 */
	public List<InterventiAssociati> findByInterventoPadre(PaiIntervento padre){
		TypedQuery<InterventiAssociati> query = getEntityManager().createQuery("SELECT i "+
	"from InterventiAssociati i "+
	"WHERE i.interventoPadre.paiInterventoPK.codPai= :codPai "+
	"AND i.interventoPadre.paiInterventoPK.codTipint=:codTipInt "+
	"AND i.interventoPadre.paiInterventoPK.cntTipint=:cntTipInt",InterventiAssociati.class);
	query.setParameter("codPai", padre.getPaiInterventoPK().getCodPai());
	query.setParameter("codTipInt", padre.getPaiInterventoPK().getCodTipint());
	query.setParameter("cntTipInt", padre.getPaiInterventoPK().getCntTipint());
	return query.getResultList();
	}
	
	/**
	 * Ritorna tutte le associazioni in cui un intervento è figlio.
	 * @param padre
	 * @return
	 */
	public InterventiAssociati findInterventoPadre(PaiIntervento figlio){
		return this.findInterventoPadre(figlio.getPaiInterventoPK().getCodPai(), figlio.getPaiInterventoPK().getCodTipint(), figlio.getPaiInterventoPK().getCntTipint());
	}
	
	/**
	 * Ritorna tutte le associazioni in cui un intervento è figlio.
	 * @param codPai
	 * @param codTipInt
	 * @param cntTipInt
	 * @return
	 */
	public InterventiAssociati findInterventoPadre(Integer codPai, String codTipInt, Integer cntTipInt)
	{
		TypedQuery<InterventiAssociati> query = getEntityManager().createQuery("SELECT i "
				+ "from InterventiAssociati i "
				+ "WHERE i.interventoFiglio.paiInterventoPK.codPai= :codPai "
				+ "AND i.interventoFiglio.paiInterventoPK.codTipint=:codTipInt "
				+ "AND i.interventoFiglio.paiInterventoPK.cntTipint=:cntTipInt", InterventiAssociati.class);
		query.setParameter("codPai", codPai);
		query.setParameter("codTipInt", codTipInt);
		query.setParameter("cntTipInt", cntTipInt);
		List<InterventiAssociati> risultati = query.getResultList();
		if (risultati.isEmpty() == false)
		{
			return risultati.get(0);
		}
		return null;
	}

}
