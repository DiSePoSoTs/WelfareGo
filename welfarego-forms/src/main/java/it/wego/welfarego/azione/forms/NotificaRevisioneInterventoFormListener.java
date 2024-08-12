package it.wego.welfarego.azione.forms;

import it.trieste.comune.ssc.json.JsonForm;
import it.trieste.comune.ssc.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.InterventoDataModel;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.TaskDao;

/**
 *
 * @author Fabio Bonaccorso 
 */
@SuppressWarnings("deprecation")
public class NotificaRevisioneInterventoFormListener  extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Proceedable{
	

	public Object load() throws Exception {
		getLogger().debug("handling load request");
		NotificaRevisioneInterventoDataModel data=loadInterventoData(NotificaRevisioneInterventoDataModel.class);
		data.setNote(getTask().getCampoFlow1());
		return new JsonForm(data);
	}
	
	
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		initTransaction();

		insertEvento(getTask(), PaiEvento.PAi_VISUALIZZA_NOTIFICA_REVISIONE_INTERVENTO);
		new TaskDao(getEntityManager()).markQueued(getTask()); 

		commitTransaction();

		return new JsonMessage("operazione completata");
	}
	
	public static class NotificaRevisioneInterventoDataModel extends InterventoDataModel {
		private String note;
       
		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

	}
}
