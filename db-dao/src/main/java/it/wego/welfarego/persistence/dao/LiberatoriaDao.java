package it.wego.welfarego.persistence.dao;
import it.wego.extjs.json.JsonSortInfo;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Liberatoria;


/**
 * 
 */

/**
 * @author Fabio Bonaccorso 
 * dao per la gestione delle liberatorie 
 *
 */
public class LiberatoriaDao extends PersistenceAdapter {

	public LiberatoriaDao (EntityManager em) {
		super(em);
	}
	





	public List<Object[]> countLiberatorieFirmateFromTo(String from, String to)  {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT(A.NOME),COUNT(*)AS NUMERO_LIB_FIRMATE FROM LIBERATORIA L ");
		sb.append(" JOIN ASSOCIAZIONE A ON A.ID=L.ID_ASSOCIAZIONE ");
		sb.append(" WHERE L.DT_FIRMA BETWEEN TO_DATE(?, 'DD/MM/YYYY') AND TO_DATE(?, 'DD/MM/YYYY') ");
		sb.append(" GROUP BY A.NOME ORDER BY A.NOME ");

		Query query = getEntityManager().createNativeQuery(sb.toString());
		query.setParameter(1, from);
		query.setParameter(2, to);

		List<Object[]> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<AnagrafeSoc> getLiberatoriaByAssociazione(Integer idAssociazione){
		 Query query = getEntityManager().createQuery("SELECT l.anagrafeSoc "
	                + "FROM Liberatoria l "
	                + "WHERE l.associazione.id = :id "
	                + "ORDER BY l.dtFirma ASC",AnagrafeSoc.class);
		 query.setParameter("id", idAssociazione);
		 return query.getResultList();
		
	}
	
	/**
	 * Ritorna la lista utenti inseriti da una associazione e che abbiano un determinato tipo di intervento.
	 * @param idAssociazione
	 * @param from
	 * @param to
	 * @param codTipInt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Liberatoria> getLiberatoriaByAssociazioneAndInterventoFromTo(Integer idAssociazione,Date from,Date to,String codTipInt){
		String queryString = "SELECT l "
                + "FROM Liberatoria l "
                + "WHERE l.associazione.id = :id " 
                + "AND (l.dtFirma between :from AND :to) ";
		if(codTipInt!=null){
			queryString= queryString+"AND EXISTS (SELECT p from  PaiIntervento p WHERE p.associazione.id = :id AND  p.paiInterventoPK.codTipint = :codTipint AND p.pai.rawCodAna = l.anagrafeSoc.codAna ) ";
		}
		queryString = queryString +"ORDER BY l.dtFirma ASC";
		Query query = getEntityManager().createQuery(queryString,Liberatoria.class);
		 query.setParameter("id", idAssociazione);
		 query.setParameter("from", from);
		 query.setParameter("to", to);
		 if(codTipInt!=null){
			 query.setParameter("codTipint",codTipInt );
		 }
		 return query.getResultList();
		
	}
	/**
	 * Ritorna una liberatoria relativa ad un utente e ad un associazione
	 * @param idAssociazione
	 * @param codAna
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  Liberatoria getLiberatoriaByAssociazioneAndUser(Integer idAssociazione,Integer codAna){
		 Query query = getEntityManager().createQuery("SELECT l "
	                + "FROM Liberatoria l "
	                + "WHERE l.associazione.id = :id AND  l.anagrafeSoc.codAna = :codAna "
	                ,Liberatoria.class);
		 query.setParameter("id", idAssociazione);
		 query.setParameter("codAna", codAna);
		 List<Liberatoria> libs = query.getResultList();
		 if(libs.isEmpty()){
			 return null;
		 }
		 else {
			 return libs.get(0);
		 }
		 
	}
	
	public List<Liberatoria> getLiberatoriaByUser(Integer codAna, String order, JsonSortInfo.Direction orderDirection)
	{
		String qs = "SELECT l FROM Liberatoria l "
				+ "WHERE l.anagrafeSoc.codAna = :codAna";
		if ("dataFirma".equals(order))
		{
			qs += " ORDER BY l.dtFirma " + orderDirection.name();
		}//if		
		else if ("associazione".equals(order))
		{
				qs += " ORDER BY l.associazione.nome " + orderDirection.name();
		}//else if
		else if ("utente".equals(order))
		{
				qs += " ORDER BY l.codUte.nome " + orderDirection.name();
		}//else if
		Query query = getEntityManager().createQuery(qs, Liberatoria.class);
		query.setParameter("codAna", codAna);
		return query.getResultList();
	}
}
