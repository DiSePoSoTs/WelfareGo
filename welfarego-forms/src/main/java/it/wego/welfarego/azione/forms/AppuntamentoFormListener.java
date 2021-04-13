package it.wego.welfarego.azione.forms;

import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.json.GsonObject;
import it.wego.json.JsonForm;
import it.wego.json.JsonMessage;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import java.util.Date;

/**
 *
 * @author Muscas
 */
public class AppuntamentoFormListener extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Saveable,AbstractForm.Proceedable{

	@Override
	public Object load() {
		getLogger().debug("loading data");

		AppuntamentoDataModel data = new AppuntamentoDataModel();
		data.setNomeCognome("TODO");

		getLogger().debug("data loaded");
		return new JsonForm(data);
	}

	@Override
	public Object save() {
		getLogger().debug("saving data");
		initTransaction();

		// TODO

		//  insertEvento(task, Eventi.PAI_UPDATE_INTERVENTO);

		commitTransaction();
		getLogger().debug("data saved");
		return new JsonMessage("dati salvati");
	}

	@Override
	public Object proceed() {
		getLogger().debug("saving data and proceeding");
		initTransaction();

		save();

		//TODO 

		//insertEvento(task, Eventi.PAI_PROTOCOLLAZIONE_INTERVENTO);
		//new TaskDao(getEntityManager()).markQueued(task,TaskDao.FLAG_SI);
		//IntalioAdapter.executeJob();

		commitTransaction();
		IntalioAdapter.executeJob();
		getLogger().debug("data saved and proceeding");
		return new JsonMessage(true, "prooceed OK");

	}

	public static class AppuntamentoDataModel extends GsonObject {
		/*
		
		fieldLabel: 'Nome e cognome',
		name: 'nome_cognome',
		 * 
		fieldLabel: 'Data',
		name: 'data'
		 * 
		fieldLabel: 'Dalle ore',
		name: 'dalle_ore',
		 * 
		fieldLabel: 'Alle ore',
		name: 'alle_ore',
		 * 
		fieldLabel: 'Note',
		name: 'note'
		 * 
		name: 'dalle_ore_orig'
		}];
		 */

		private String nomeCognome, note;
		private Date data, dalleOre, alleOre;

		public Date getAlleOre() {
			return alleOre;
		}

		public void setAlleOre(Date alleOre) {
			this.alleOre = alleOre;
		}

		public Date getDalleOre() {
			return dalleOre;
		}

		public void setDalleOre(Date dalleOre) {
			this.dalleOre = dalleOre;
		}

		public Date getData() {
			return data;
		}

		public void setData(Date data) {
			this.data = data;
		}

		public String getNomeCognome() {
			return nomeCognome;
		}

		public void setNomeCognome(String nomeCognome) {
			this.nomeCognome = nomeCognome;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}
	}
}
