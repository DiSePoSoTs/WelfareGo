package it.wego.welfarego.azione.forms;

import com.google.common.base.Strings;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.GenericDocumentoDataModel;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author aleph
 */
public class GenerazioneDocumentoFormListener extends GenericDocumentoFormListener implements AbstractForm.Loadable, AbstractForm.Proceedable {

    @Override
    public Object load() throws Exception {
        getLogger().debug("loading data");
        if (getParameters().containsKey("requireDocument")) {
            return prepareDavDocument(getTask().getCodTmpl().getDesTmpl());
        } else {
            GenericDocumentoDataModel res = loadData(GenericDocumentoDataModel.class);
//            if (Objects.equal(getTask().getCampoFlow1(), UniqueTasklist.GENERAZIONE_DOCUMENTO_STANDALONE)) {
//                res.setDescrizione(getTask().getCampoFlow2());
//            }
            getLogger().debug("data loaded");
            return JsonBuilder.newInstance().withData(res).buildResponse();
        }
    }

    @Override
    public Object proceed() throws Exception {
        getLogger().debug("generating document");

        UniqueTasklist task = getTask();
        Template template = task.getCodTmpl();
        Mandato mandato = Strings.isNullOrEmpty(getTask().getExtraParameterFromCampoFlow8("idMandato")) ? null : getEntityManager().find(Mandato.class, Integer.valueOf(getTask().getExtraParameterFromCampoFlow8("idMandato")));
        String data = mandato == null ? Pratica.getXmlCartellaSociale(task.getPaiIntervento()) : Pratica.getXmlCartellaSociale(mandato);
        byte[] documentResult = DynamicOdtUtils.newInstance()
                .withTemplateBase64(template.getClobTmpl())
                .withDataXml(data)
                .withConfig(new ConfigurationDao(getEntityManager()).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                .getResult();

        initTransaction();

        new PaiDocumentoDao(getEntityManager()).createDoc(task, getCodTipDoc(), Base64.encodeBase64String(documentResult), template.getDesTmpl().replaceAll(" ", "_") + ".odt");

        new TaskDao(getEntityManager()).markQueued(getTask());

        commitTransaction();
        IntalioAdapter.executeJob();

        getLogger().debug("documento generated and stored");
        return JsonBuilder.newInstance().withMessage("documento generato con successo").buildResponse();
    }
}
