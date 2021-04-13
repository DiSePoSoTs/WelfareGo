package it.wego.welfarego.azione.forms;

import it.wego.json.JsonForm;
import it.wego.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.GenericDocumentoDataModel;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.dao.TaskDao;

/**
 *
 * @author aleph
 */
public class RivediDocumentoFormListener extends GenericDocumentoFormListener implements AbstractForm.Loadable, AbstractForm.Proceedable {

	@Override
	public Object load() throws Exception {
		getLogger().debug("loading data");
		if (getParameter("requireDocument") != null) {
			return prepareDavDocument();
		} else {
			GenericDocumentoDataModel res = loadData(GenericDocumentoDataModel.class);
			return new JsonForm(res);
		}
	}

	@Override
	public Object proceed() throws Exception {
		getLogger().debug("proceeding");
		initTransaction();

		new TaskDao(getEntityManager()).markQueued(getTask());

		commitTransaction();
		IntalioAdapter.executeJob();

		getLogger().debug("documento generated and stored");
		return new JsonMessage("operazione completata");
	}
}
