package it.wego.persistence.objects;

import javax.persistence.Query;

/**
 *
 * @author aleph
 */
public interface Operation{
	
	/**
	 * restituisce la stringa JPQL che esprime questa operazione
	 * @return la stringa JPQL che esprime questa operazione
	 */
	@Override
	public String toString();

	/**
	 * imposta eventuali parametri dell'operazione
	 * @param query la query su cui impostare eventuali parametri
	 */
	public void setParameter(Query query);
	
}
