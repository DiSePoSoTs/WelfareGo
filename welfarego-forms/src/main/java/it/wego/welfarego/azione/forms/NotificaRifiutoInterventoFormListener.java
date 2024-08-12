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
public class NotificaRifiutoInterventoFormListener  extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Proceedable{
	

	public Object load() throws Exception {
		getLogger().debug("handling load request");
		NotificaRifiutoInterventoDataModel data=loadInterventoData(NotificaRifiutoInterventoDataModel.class);
		data.setNote(getTask().getCampoFlow1());
		return new JsonForm(data);
	}
	
	
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		initTransaction();

		insertEvento(getTask(), PaiEvento.PAI_VISUALIZZA_NOTIFICA_RIFIUTO_INTERVENTO);
		new TaskDao(getEntityManager()).markQueued(getTask()); 

		commitTransaction();
		IntalioAdapter.executeJob();
		return new JsonMessage("operazione completata");
	}
	
	public static class NotificaRifiutoInterventoDataModel extends InterventoDataModel {
		private String note;
       
		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}
		
		
		
	}
}
