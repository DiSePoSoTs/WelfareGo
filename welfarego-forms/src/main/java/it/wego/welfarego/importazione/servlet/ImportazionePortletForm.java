/**
 * 
 */
package it.wego.welfarego.importazione.servlet;

import java.util.Map;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;

/**
 * @author Fabio Bonaccorso
 * Portlet per la gestione delle importazioni di vecchi interventi
 *
 */
public class ImportazionePortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_IMPORTAZIONE = "importazione.html";
	
	public ImportazionePortletForm() {
		super(TEMPLATE_IMPORTAZIONE);
		
	}

	/* (non-Javadoc)
	 * @see it.wego.welfarego.abstracts.AbstractFtlPortletForm#loadViewData(java.util.Map)
	 */
	public Map<String, Object> loadViewData(Map<String, Object> ftlData)
			throws Exception {
		
		return ftlData;
	}

}
