package it.wego.welfarego.listeattesa;

import it.wego.welfarego.abstracts.AbstractFtlPortletForm;
import it.wego.welfarego.commons.servlet.ComboBoxStoreHandler;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class ListeAttesaPortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_LISTE_ATTESA = "liste_attesa.html";

	public ListeAttesaPortletForm() {
		super(TEMPLATE_LISTE_ATTESA);
	}

	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		ftlData.put("storeData", ComboBoxStoreHandler.getStoreAsJsonArray(ComboBoxStoreHandler.Store.LISTA_ATTESA));
		return ftlData;
	}
}
