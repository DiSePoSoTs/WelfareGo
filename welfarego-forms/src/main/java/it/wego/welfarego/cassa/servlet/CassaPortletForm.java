/**
 * 
 */
package it.wego.welfarego.cassa.servlet;

import java.util.Map;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;

/**
 * @author fabio Bonaccorso
 * Portlet per la gestione cassa a mani 
 *
 */
public class CassaPortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_CASSA = "cassa.html";
	
	public CassaPortletForm() {
		super(TEMPLATE_CASSA);
		
	}

	/* (non-Javadoc)
	 * @see it.wego.welfarego.abstracts.AbstractFtlPortletForm#loadViewData(java.util.Map)
	 */
	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		
		return ftlData;
	}

}
