package it.wego.welfarego.azione.forms;

import it.trieste.comune.ssc.json.JsonForm;
import it.trieste.comune.ssc.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.InterventoDataModel;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.TaskDao;

/**
 *
 * @author Fabio Bonaccorso 
 */
@SuppressWarnings("deprecation")
public class NotificaProrogaInterventoFormListener  extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Proceedable{
	

	public Object load() throws Exception {
		getLogger().debug("handling load request");
		NotificaProrogaInterventoDataModel data=loadInterventoData(NotificaProrogaInterventoDataModel.class);
		data.setNote(getTask().getCampoFlow1());
		return new JsonForm(data);
	}
	
	
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		initTransaction();

	
		new TaskDao(getEntityManager()).markQueued(getTask()); 

		commitTransaction();
		
		return new JsonMessage("operazione completata");
	}
	
	public static class NotificaProrogaInterventoDataModel extends InterventoDataModel {
		private String note;
       
		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}
		
		
		
	}
}
