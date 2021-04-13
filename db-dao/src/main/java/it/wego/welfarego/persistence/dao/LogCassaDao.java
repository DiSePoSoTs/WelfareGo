package it.wego.welfarego.persistence.dao;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.entities.LogCassa;

/**
 * 
 */

/**
 * @author Fabio Bonaccorso
 * Dao per la classe log cassa
 */
public class LogCassaDao extends PersistenceAdapter implements ORMDao {

	public LogCassaDao(EntityManager em){
		super(em);
	}
	/**
	 * Ritorna una lista di log della cassa basandosi sulla data di emissione 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<LogCassa> findLogCassaByDate(Date from,Date to){
		String query = "SELECT l FROM LogCassa l ORDER BY l.dataOperazione DESC";
		Condition condition = null;
		if(from!=null && to!=null){
			condition= between("l.dataOperazione", from, to);
		}
		if(from == null && to!=null){
			condition = before("l.dataOperazione", to);
		}
		if(from != null && to ==null){
			condition = after("l.dataOperazione", from);
		}
		return find(LogCassa.class, query, condition);		
	}
	
	
	public LogCassa findLastLog(){
		TypedQuery<LogCassa> query = getEntityManager().createQuery("SELECT l FROM LogCassa l ORDER BY l.dataOperazione DESC",LogCassa.class);
		query.setMaxResults(1);
		return query.getSingleResult();
	}
}
