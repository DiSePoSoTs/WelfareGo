package it.wego.welfarego.azione.forms;

import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.json.JsonForm;
import it.wego.json.JsonMessage;
import it.wego.welfarego.azione.models.ProtocolloDataModel;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.Determine;
import it.wego.welfarego.persistence.entities.PaiEvento;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class RegistraDeterminaFormListener extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Proceedable{

	@Override
	public Object load() throws Exception {
		getLogger().debug("loading data");
		ProtocolloDataModel res = loadInterventoData(ProtocolloDataModel.class);
		res.setDataProtoc(new Date());
		res.setNumero("");
		getLogger().debug("data loaded");
		return new JsonForm(true, res);
	}

	@Override
	public Object proceed() throws Exception {
		getLogger().debug("saving data and proceeding");
		initTransaction();

		ProtocolloDataModel data = getDataParameter(ProtocolloDataModel.class);
		Determine determina = new Determine();
		determina.setTsDetermina(data.getDataProtoc());
		determina.setNumDetermina(data.getNumero());
		getEntityManager().persist(determina);

		PaiEvento evento = insertEvento(getTask(), PaiEvento.PAI_REGISTRAZIONE_DETERMINA);
		evento.setIdDetermina(determina);

		new TaskDao(getEntityManager()).markQueued(getTask(), TaskDao.FLAG_SI);

		commitTransaction();
		IntalioAdapter.executeJob();
		getLogger().debug("data saved and proceeding");
		return new JsonMessage(true, "prooceed OK");
	}
}
