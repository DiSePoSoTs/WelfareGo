package it.wego.welfarego.azione.forms;

import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.abstracts.AbstractServlet.AbstractFormFactory;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Muscas
 */
public class AzioneFormFactory implements AbstractFormFactory {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public AbstractForm createFormHandler(Map<String, String> parameters) throws Exception {
		String taskId = parameters.get("task_id");
		String formClass = parameters.get("formClass");
		if (formClass == null) {
			UniqueTasklist task = PersistenceAdapterFactory.getPersistenceAdapter().getEntityManager().find(UniqueTasklist.class, new BigDecimal(taskId));
			if (task == null) {
				throw new Exception("unable to find task for id : " + taskId);
			}
			formClass = task.getForm().getClassForm();
		}
		Class c = Class.forName(formClass);

		logger.debug(String.format("taskId:%s, formClass:%s, c.getCanonicalName():%s", taskId, formClass, c.getCanonicalName()));

		return ((AbstractForm) c.newInstance());
	}
}
