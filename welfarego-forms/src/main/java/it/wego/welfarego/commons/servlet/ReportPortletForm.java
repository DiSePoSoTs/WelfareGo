package it.wego.welfarego.commons.servlet;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;
import it.wego.welfarego.commons.utils.ReportUtils;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class ReportPortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_REPORT = "reports.html";

	public ReportPortletForm() {
		super(TEMPLATE_REPORT);
	}

	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		ftlData.put("reportArray", ReportUtils.serializeReportArray());
		ftlData.put("tipologieInterventiArray", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.TIPOLOGIA_INTERVENTO));
		ftlData.put("uotArray", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.UOT));
		ftlData.put("giuridicheArray",ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.PERSONE_GIURIDICHE));
		return ftlData;
	}
}