package it.wego.welfarego.tasklist.forms;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;
import it.wego.welfarego.commons.servlet.ComboBoxStoreHandler;
import it.wego.welfarego.commons.utils.ToolsUtils;
import it.wego.welfarego.persistence.constants.Configurations;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class TasklistPortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_TASKLIST = "tasklist.html";

	public TasklistPortletForm() {
		super(TEMPLATE_TASKLIST);
	}

	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		ftlData.put("refreshTaskList", ToolsUtils.getConfig(Configurations.PROPERTY_KEY_REFRESH_TASKLIST, Boolean.FALSE.toString()));
		ftlData.put("comboUot", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.UOT));
		ftlData.put("comboAssist", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.ASSISTENTE));
		ftlData.put("comboPo", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.PO));
		ftlData.put("comboAttivita", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.ATTIVITA));
		ftlData.put("comboInterventi", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.INTERVENTI));
		return ftlData;
	}
}
