package it.wego.welfarego.persistence.dao;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.entities.RicevutaCassa;
import it.wego.welfarego.persistence.entities.RicevutaCassaPK;

/**
 * 
 */

/**
 * @author fabio Bonaccorso
 *
 */
public class RicevutaCassaDao extends PersistenceAdapter implements ORMDao {

	public RicevutaCassaDao(EntityManager em){
		super(em);
	}
	
	/**
	 * Ritorna una lista di ricevute basandosi dulla data di emissione
	 * @param from
	 * @param to
	 * @return
	 */
	public List<RicevutaCassa> findRicevuteByDate(Date from,Date to){
		String query = "SELECT r FROM RicevutaCassa r";
		Condition condition = null;
		if(from != null && to != null){
			condition = between("r.dataEmissione", from, to);
		}
		if(from ==null && to!=null){
			condition = before("r.dataEmissione",to);
		}
		if(from !=null && to==null){
			condition = after("r.dataEmissione", from);
		}
		return find(RicevutaCassa.class,query,condition);
	}
	
	public RicevutaCassa findRicevutaById(int codPai,int cntTipInt){
	return getEntityManager().find(RicevutaCassa.class, new RicevutaCassaPK(codPai, cntTipInt));
		
	}
	public RicevutaCassa findLastRicevuta(){
		TypedQuery<RicevutaCassa> query = getEntityManager().createQuery("SELECT r FROM RicevutaCassa r ORDER BY r.dataEmissione DESC",RicevutaCassa.class);
		query.setMaxResults(1);
		return query.getSingleResult();
		
	}
}

