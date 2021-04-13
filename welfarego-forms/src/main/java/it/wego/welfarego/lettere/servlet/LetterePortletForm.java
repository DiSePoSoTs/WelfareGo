/**
 * 
 */
package it.wego.welfarego.lettere.servlet;

import java.util.Map;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;

/**
 * @author fabio
 *
 */
public class LetterePortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_LETTERE = "lettere.html";
	
	public LetterePortletForm() {
		super(TEMPLATE_LETTERE);
		
	}

	/* (non-Javadoc)
	 * @see it.wego.welfarego.abstracts.AbstractFtlPortletForm#loadViewData(java.util.Map)
	 */
	public Map<String, Object> loadViewData(Map<String, Object> ftlData)
			throws Exception {
		
		return ftlData;
	}

}
