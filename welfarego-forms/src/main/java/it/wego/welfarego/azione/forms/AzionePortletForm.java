package it.wego.welfarego.azione.forms;

import it.trieste.comune.ssc.json.JSonUtils;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.abstracts.AbstractFtlPortletForm;
import it.wego.welfarego.commons.utils.ToolsUtils;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class AzionePortletForm extends AbstractFtlPortletForm {

	private static final String TEMPLATE_AZIONE = "azione.html";

	public AzionePortletForm() {
		super(TEMPLATE_AZIONE);
	}

	@Override
	public Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception {
		if(isInLiferay()){
			ftlData.put("loadLiferayJs",true); 
		}else{
			getLogger().info("working outside liferay");
		}
		String taskId = getParameter("task_id");
		getLogger().debug("rendering azione view for task : "+taskId);
		UniqueTasklist task = null;
		Class<? extends AbstractForm> formClass = null;
		String codForm = "default", formData = "{}";
		getLogger().debug("loading form via task from db : " + taskId);
		if (taskId != null && !taskId.equals("")) {
			task = (new TaskDao(getEntityManager())).findTask(taskId);
			ftlData.put("taskId", taskId);
			if (task == null) {
				ftlData.put("taskIdError", true);
			} else {
				codForm = task.getForm().getUrlForm();
				formData = AbstractForm.getJsonResponse(
						  AbstractForm.Action.LOAD,
						  Collections.singletonMap("task_id", taskId),
						  formClass = (Class<? extends AbstractForm>) Class.forName(task.getForm().getClassForm()));
			}
		}

		//Properties properties = AzioneUtils.getFormProperties();
		//String formPath = "forms/" + properties.getProperty("forms." + codForm + ".form");
		String storesStr = ToolsUtils.getConfig("forms." + codForm + ".stores");
		getLogger().debug(String.format("codForm:[%s], storesStr:[%s]", codForm, storesStr) );

		ftlData.put("formConfig",  ToolsUtils.getConfig("forms." + codForm + ".config"));
		ftlData.put("codForm", codForm);
		ftlData.put("formData", formData);


		Map<String, String> stores = new HashMap<String, String>();
		if (storesStr != null && formClass != null) {

			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put("task_id", taskId);


			for (String store : JSonUtils.getGson().fromJson(storesStr, String[].class)) {
				parameters.put("table", store);
				String storeData = AbstractForm.getJsonResponse(AbstractForm.Action.LOAD, parameters, formClass);
				stores.put(store, storeData);
			}
		}


		ftlData.put("stores", stores);
		return ftlData;
	}
}
