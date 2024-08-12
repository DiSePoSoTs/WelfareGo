package it.wego.welfarego.azione.forms;

import com.google.common.base.Strings;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author aleph
 */
public class ProtocollaDomandaForm extends GenericProtocolloFormListener implements AbstractForm.Proceedable {

    @Override
    public Object proceed() throws Exception {
        getLogger().debug("saving data and proceeding");
        initTransaction();

        UniqueTasklist task = getTask();
        ProtocolloDataModel dataModel = getDataParameter(ProtocolloDataModel.class);
        Pai pai = task.getCodPai();

        Validate.notNull(dataModel.getDataProtoc(), "la data di protocollo generale deve essere impostata");
        Validate.isTrue(!Strings.isNullOrEmpty(dataModel.getNumero()), "il numero di protocollo generale deve essere impostato");

        pai.setDtPg(dataModel.getDataProtoc());
        pai.setNumPg(dataModel.getNumero());
        getEntityManager().flush();

        insertEvento(PaiEvento.PAI_PROTOCOLLAZIONE_INTERVENTO);
        new TaskDao(getEntityManager()).markQueued(getTask(), TaskDao.FLAG_SI);

        commitTransaction();
        IntalioAdapter.executeJob();
        getLogger().debug("data saved and proceeding");
        return JsonBuilder.newInstance().buildResponse();
    }
}
