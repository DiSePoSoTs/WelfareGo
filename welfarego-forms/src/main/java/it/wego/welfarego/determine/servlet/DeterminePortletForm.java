package it.wego.welfarego.determine.servlet;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;
import it.wego.welfarego.commons.servlet.ComboBoxStoreHandler;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class DeterminePortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_DETERMINE = "determine.html";

	public DeterminePortletForm() {
		super(TEMPLATE_DETERMINE);
	}

	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		ftlData.put("tipintStoreData", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.TIPOLOGIA_INTERVENTO));
		ftlData.put("classTipintStoreData", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.CLASSE_TIPOLOGIA_INTERVENTO));
		return ftlData;
	}
}
