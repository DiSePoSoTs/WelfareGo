package it.wego.welfarego.azione.forms;

import it.wego.json.JsonForm;
import it.wego.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.GenericDocumentoDataModel;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.TaskDao;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class ValidazioneDocumentoFormListener extends GenericDocumentoFormListener implements AbstractForm.Loadable, AbstractForm.Proceedable {
		
	@Override
	public Object load() throws Exception {
		getLogger().debug("handling load request");
		if (getParameters().containsKey("requireDocument")) {
			return prepareDavDocument();
		} else {
			ValidazioneDocumentoDataModel data = loadData(ValidazioneDocumentoDataModel.class);
			data.setNote("TODO (note)");
			return new JsonForm(data);
		}
	}
	private static final Map<ValidazioneDocumentoDataModel.EsitoVerifica, String> esito2esitoTask;

	static {
		// TODO verificare mappatura esito
		Map<ValidazioneDocumentoDataModel.EsitoVerifica, String> map = new EnumMap<ValidazioneDocumentoDataModel.EsitoVerifica, String>(ValidazioneDocumentoDataModel.EsitoVerifica.class);
		map.put(ValidazioneDocumentoDataModel.EsitoVerifica.approvato, TaskDao.FLAG_SI);
		map.put(ValidazioneDocumentoDataModel.EsitoVerifica.rivedere, TaskDao.FLAG_NO);
		esito2esitoTask = Collections.unmodifiableMap(map);
	}

	@Override
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		ValidazioneDocumentoDataModel data = getDataParameter(ValidazioneDocumentoDataModel.class);
		initTransaction();

		//TODO save data (note, ? )

		insertEvento(getTask(), PaiEvento.PAI_VERIFICA_DOCUMENTO);
		new TaskDao(getEntityManager()).markQueued(getTask(), esito2esitoTask.get(data.getEsitoVerifica()));

		commitTransaction();
		IntalioAdapter.executeJob();
		return new JsonMessage("operazione completata");
	}

	public static class ValidazioneDocumentoDataModel extends GenericDocumentoDataModel {

		private String note;
		private EsitoVerifica esitoVerifica;

		public static enum EsitoVerifica {

			approvato, rivedere
		};

		public EsitoVerifica getEsitoVerifica() {
			return esitoVerifica;
		}

		public void setEsitoVerifica(EsitoVerifica esitoVerifica) {
			this.esitoVerifica = esitoVerifica;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}
	}
}
