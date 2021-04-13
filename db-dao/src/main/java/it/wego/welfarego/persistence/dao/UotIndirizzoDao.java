package it.wego.welfarego.persistence.dao;

import static it.wego.persistence.ConditionBuilder.and;
import static it.wego.persistence.ConditionBuilder.between;
import static it.wego.persistence.ConditionBuilder.isEqual;
import static it.wego.persistence.ConditionBuilder.or;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.SimpleConditions.LikeCondition;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.entities.UotIndirizzo;

public class UotIndirizzoDao extends PersistenceAdapter {
	  
	public UotIndirizzoDao(EntityManager em) {
	        super(em);
	    }
    /**
     * Trova  la lista degli indirizzi 
     * @param indirizzo
     * @param civico
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<UotIndirizzo> findByIndirizzo(String indirizzo,String civico,String sub){
		  String query = "SELECT u FROM UotIndirizzo u";
		   Condition condition = null;
		   Condition conditionSubCivico=sub!=null?ConditionBuilder.equals("u.barrato", sub):null;
		   if(conditionSubCivico==null){
		   condition= and(ConditionBuilder.elike("u.indirizzo", "%"+indirizzo+"%"),ConditionBuilder.equals("u.civico", civico));
		   }
		   else {
			   condition= and(ConditionBuilder.elike("u.indirizzo", "%"+indirizzo+"%"),ConditionBuilder.equals("u.civico", civico),conditionSubCivico);
		   }
	       return find(UotIndirizzo.class, query, condition);		  

   	  
		
	}


}
