/**
 * 
 */
package it.wego.welfarego.commons.servlet;

import java.util.Map;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;

/**
 * @author fabio Bonaccorso
 * Portlet da usare per vari test 
 *
 */
public class TestPortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_TEST = "test.html";
	
	public TestPortletForm() {
		super(TEMPLATE_TEST);
		
	}

	/* (non-Javadoc)
	 * @see it.wego.welfarego.abstracts.AbstractFtlPortletForm#loadViewData(java.util.Map)
	 */
	public Map<String, Object> loadViewData(Map<String, Object> ftlData)
			throws Exception {
		
		return ftlData;
	}

}
