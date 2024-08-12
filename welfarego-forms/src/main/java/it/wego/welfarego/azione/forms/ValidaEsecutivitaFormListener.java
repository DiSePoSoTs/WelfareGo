package it.wego.welfarego.azione.forms;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.abstracts.AbstractServlet;
import it.wego.welfarego.azione.models.VerificaImpegniGenericDataModel;
import it.wego.welfarego.azione.stores.ImpegniStoreListener;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author aleph
 */
public class ValidaEsecutivitaFormListener extends VerificaImpegniGenericFormListener implements AbstractForm.Loadable, AbstractForm.Proceedable {

    @Override
    public Object load() throws Exception {
        getLogger().debug("handling load request");
        String table = getParameter("table");

        if (getParameter("requireDocument") != null) {
            if (getParameter("requireDocument").equals("report")) {
                return prepareDavDocument(Documenti.TIPO_DOC_REPORT_DETERMINA);
            } else {
                return prepareDavDocument(Documenti.TIPO_DOC_TEMPLATE_DETERMINA);
            }
        } else if (table == null) {

            ValidaEsecutivitaDataModel data = loadData(ValidaEsecutivitaDataModel.class);
            data.setReportDoc(hasDocument(Documenti.TIPO_DOC_REPORT_DETERMINA));

            return JsonBuilder.newInstance().withData(data).buildResponse();
        } else if (table.equals("impegni")) {
            if (getParameter(AbstractServlet.METHOD_PROP).equals(AbstractServlet.METHOD_GET)) {
                return new ImpegniStoreListener(this).load();
            } else {
                return new ImpegniStoreListener(this).update();
            }
        } else if (table.equals("messaggiBre")) {
            return BreUtils.getBreMessages(getTask().getCodPai().getPaiInterventoList());
        }
        throw new Exception("unknown/wrong table name : " + table);
    }
    private static final Map<ValidaEsecutivitaDataModel.Esito, String> esito2esitoTask, esitoToEvento;

    static {
        Map<ValidaEsecutivitaDataModel.Esito, String> map = new EnumMap<ValidaEsecutivitaDataModel.Esito, String>(ValidaEsecutivitaDataModel.Esito.class);
        map.put(ValidaEsecutivitaDataModel.Esito.approva, TaskDao.FLAG_SI);
        map.put(ValidaEsecutivitaDataModel.Esito.respingi, TaskDao.FLAG_NO);
        map.put(ValidaEsecutivitaDataModel.Esito.rimanda, TaskDao.FLAG_R);
        esito2esitoTask = Collections.unmodifiableMap(map);

        map = new EnumMap<ValidaEsecutivitaDataModel.Esito, String>(ValidaEsecutivitaDataModel.Esito.class);
        map.put(ValidaEsecutivitaDataModel.Esito.approva, PaiEvento.PAI_APPROVA_INTERVENTO);
        map.put(ValidaEsecutivitaDataModel.Esito.respingi, PaiEvento.PAI_RESPINGI_INTERVENTO);
        map.put(ValidaEsecutivitaDataModel.Esito.rimanda, PaiEvento.PAI_RIMANDA_INTERVENTO);
        esitoToEvento = Collections.unmodifiableMap(map);

    }

    /**
     * esito APPROVA : conferma delle proposte di spesa (PaiInterventoMese),
     * stato intervento impostato a E esito RESPINGI : chiusura intervento
     * (stato C, data chiusura), rimozione proposte di spesa (PaiInterventoMese)
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object proceed() throws Exception {
        getLogger().debug("handling proceed request");
        initTransaction();

        PaiIntervento paiIntervento = getTask().getPaiIntervento();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());
        ValidaEsecutivitaDataModel data = getDataParameter(ValidaEsecutivitaDataModel.class);
        BigDecimal costoTot = getCostoTot(), propSpesaTot = paiInterventoMeseDao.findSumProp(paiIntervento.getPaiInterventoPK());
        Validate.isTrue(data.getEsito() != null, "il campo esito deve essere impostato per procedere");

        switch (data.getEsito()) {
            case approva:
                Validate.isTrue(costoTot.compareTo(propSpesaTot) == 0, "le proposte di spesa devono coprire il costo totale dell'intervento<br/>costo tot : " + costoTot + " E , prop spesa tot : " + propSpesaTot + " E");
                paiIntervento.setDtEsec(new Date());
//                paiInterventoMeseDao.updateRelQuantsProps(paiIntervento);
                paiInterventoMeseDao.confirmAllProps(paiIntervento);
                new PaiInterventoDao(getEntityManager()).passaInterventoInStatoEsecutivo(paiIntervento);
                break;
            case respingi:
                paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                paiIntervento.setDtChius(new Date());
                paiInterventoMeseDao.removeAllProps(paiIntervento);
                break;
        }

        new TaskDao(getEntityManager()).markQueued(getTask(), esito2esitoTask.get(data.getEsito()));
        insertEvento(getTask(), esitoToEvento.get(data.getEsito()));

        commitTransaction();
        IntalioAdapter.executeJob();
        return JsonBuilder.newInstance().buildResponse();
    }

    public static class ValidaEsecutivitaDataModel extends VerificaImpegniGenericDataModel {

        public static enum Esito {

            approva, rimanda, respingi
        }
        private Esito esito;
        private boolean reportDoc;

        public boolean isReportDoc() {
            return reportDoc;
        }

        public void setReportDoc(boolean reportDoc) {
            this.reportDoc = reportDoc;
        }

        public Esito getEsito() {
            return esito;
        }

        public void setEsito(Esito esito) {
            this.esito = esito;
        }
    }
}
