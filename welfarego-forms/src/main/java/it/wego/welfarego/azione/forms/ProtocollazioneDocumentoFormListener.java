package it.wego.welfarego.azione.forms;

import it.wego.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author aleph
 */
public class ProtocollazioneDocumentoFormListener extends GenericProtocolloFormListener implements AbstractForm.Proceedable{

	@Override
	public Object proceed() throws Exception {
		getLogger().debug("saving data and proceeding");
		initTransaction();

		ProtocolloDataModel data = getDataParameter(ProtocolloDataModel.class);
		PaiIntervento paiIntervento=getTask().getPaiIntervento();
		Pai pai=getTask().getCodPai();
		PaiDocumento documento = (new PaiDocumentoDao(getEntityManager())).findLastDoc(pai,paiIntervento,getCodTipDoc());
		Validate.notNull(documento,"documento non trovato");
		documento.setDtProt(data.getDataProtoc());
		documento.setNumProt(data.getNumero());

		insertEvento(getTask(), PaiEvento.PAI_PROTOCOLLAZIONE_DOCUMENTO);
		new TaskDao(getEntityManager()).markQueued(getTask(), TaskDao.FLAG_SI);

		commitTransaction();
		IntalioAdapter.executeJob();
		getLogger().debug("data saved and proceeding");
		return new JsonMessage("operazione completata");
	}
}
