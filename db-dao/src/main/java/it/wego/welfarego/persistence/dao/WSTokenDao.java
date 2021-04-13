package it.wego.welfarego.persistence.dao;
import static it.wego.persistence.ConditionBuilder.*;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.WSToken;

/**
 * 
 */

/**
 * @author Fabio Bonaccorso
 * Dao per la gestione dei token 
 *
 */
public class WSTokenDao extends PersistenceAdapter implements ORMDao {

	public WSTokenDao(EntityManager em){
		super(em);
	}
	/**
	 * Verifica che il token non sia scaduto.
	 * @param token
	 * @param date
	 * @return
	 */
	public WSToken findByTokenAndDate(String token,Date date){
		 String query = "SELECT t FROM WSToken t";
		 Condition condition = and(isEqual("t.token",token),after("t.tsScadenza",date));
		 WSToken result = findOne(WSToken.class, query, condition);   
		return result;
		
	}
	/**
	 * Ritorna tutti i token per gli utenti 
	 * @param u
	 * @return
	 */
	public List<WSToken> findTokenByUser(Utenti u){
		 String query = "SELECT t FROM WSToken t";
		 Condition cond = isEqual("t.codUte",u);
		 List<WSToken> result = find(WSToken.class, query, cond);
		 return result;
	}
	/**
	 * Mi torna l'oggetto token corrispondente alla string token in db
	 * @param token
	 * @return
	 */
	public WSToken findByToken(String token){
		String query = "SELECT t FROM WSToken t";
		Condition cond = isEqual("t.token",token);
		WSToken result = findOne(WSToken.class, query, cond);
		return result;
		
		
		
	}
}
