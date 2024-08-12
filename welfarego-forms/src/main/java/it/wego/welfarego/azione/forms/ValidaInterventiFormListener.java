package it.wego.welfarego.azione.forms;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gson.reflect.TypeToken;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.stores.InterventiStoreListener;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aleph
 */
public class ValidaInterventiFormListener extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Proceedable {

    @Override
    public Object load() throws Exception {
        getLogger().debug("handling load request");
        String table = getParameter("table");
        boolean requireDocument = getParameters().containsKey("requireDocument");

        if (requireDocument) {
            return prepareDavDocument(Documenti.TIPO_DOC_DOMANDA);
        } else if (table == null) {
            ValidaInterventiDataModel validaInterventiDataModel = new ValidaInterventiDataModel();
            UniqueTasklist task = getTask();
            Pai pai = task.getCodPai();
            Utenti utente = pai.getCodUteAs();
            AnagrafeSoc anagrafeSoc = pai.getAnagrafeSoc();

            validaInterventiDataModel.setCognomeUtente(anagrafeSoc.getCognome());
            validaInterventiDataModel.setNomeUtenteValid(anagrafeSoc.getNome());
            validaInterventiDataModel.setDataApert(pai.getDtApePai());
            validaInterventiDataModel.setAssistSocValid(utente.getCognome() + " " + utente.getNome());

            validaInterventiDataModel.setMotivazione(Strings.nullToEmpty(pai.getMotiv()));

            return JsonBuilder.newInstance().withData(validaInterventiDataModel).buildResponse();
        } else if (table.equals("messaggiBre")) {
            return BreUtils.getBreMessages(getEntityManager(), Iterables.filter(getTask().getCodPai().getPaiInterventoList(), new Predicate<PaiIntervento>() {
                public boolean apply(PaiIntervento paiIntervento) {
                    return Objects.equal(paiIntervento.getStatoInt(), PaiIntervento.STATO_INTERVENTO_APERTO);
                }
            }));
        } else if (table.equals("interventi")) {
            setParameter(InterventiStoreListener.PARAM_SOLO_INTERVENTI_ATTIVI, Boolean.TRUE.toString());
            setParameter(InterventiStoreListener.PARAM_BUDGET_PER_UOT, Boolean.TRUE.toString());
            setParameter(InterventiStoreListener.PARAM_NO_STATO_DETERMINE, Boolean.TRUE.toString());
            setParameter(InterventiStoreListener.PARAM_NO_TASK_ATTIVI, Boolean.TRUE.toString());
            return new InterventiStoreListener(getParameters()).load();
        } else {
            throw new Exception("Unknown table : " + table);
        }
    }

    /**
     * esito SI : pone in lista d'attesa tutti gli interventi associati al pai
     * aperti e con una tipologia intervento che lo prevede
     * (getTipologiaIntervento().getCodListaAtt() valorizzato) </br>
     *
     * esito RESPINGI : chiude tutti gli interventi associati che sono ancora
     * aperti </br>
     *
     * esito RIMANDA : imposta lo stato pai a R (rimandato)
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object proceed() throws Exception {
        getLogger().debug("handling prooceed request");
        initTransaction();

        UniqueTasklist task = getTask();
        ValidaInterventiDataModel validaInterventiDataModel = getDataParameter(ValidaInterventiDataModel.class);

        String note = validaInterventiDataModel.getNote();
        task.setCampoForm1((note != null && note.length() > 0) ? note : "nessuna nota");

        Pai pai = task.getCodPai();
        List<PaiIntervento> interventi = new PaiInterventoDao(getEntityManager()).findByCodPai(pai, PaiIntervento.STATO_INTERVENTO_APERTO);

        List<PaiInterventoPK> interventiApprovatiPkList = Strings.isNullOrEmpty(getParameter("interventi_selezionati")) ? Collections.<PaiInterventoPK>emptyList()
                : (List<PaiInterventoPK>) getGson().fromJson(getParameter("interventi_selezionati"), new TypeToken<List<PaiInterventoPK>>() {
        }.getType());

        String flagValidazione;

        insertEvento(PaiEvento.PAI_VALIDA_INTERVENTI);
        if (validaInterventiDataModel.getWaValidIntApp() == ValidaInterventiDataModel.Approvazione.conferma) {
            flagValidazione = "conferma";
            for (PaiIntervento paiIntervento : interventi) {
                if (interventiApprovatiPkList.contains(paiIntervento.getPaiInterventoPK())) {
                    if (paiIntervento.getTipologiaIntervento().getCodListaAtt() != null) {
                        paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_LISTA_ATTESA);
                    }
                } else {
                    flagValidazione = "conferma-avviaDocumentale";
                    paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                    paiIntervento.setDtChius(new Date());
                }
            }
            pai.setFlgStatoPai(Pai.STATO_APERTO); // riportiamo il pai in stato aperto dopo l'approvazione
        } else {
            flagValidazione = "rimanda";
            pai.setFlgStatoPai(Pai.STATO_RIFIUTATO);
        }

        new TaskDao(getEntityManager()).markQueued(task, flagValidazione);

        commitTransaction();
        IntalioAdapter.executeJob();
        getLogger().debug("handled prooceed request");
        return JsonBuilder.buildResponse("operazione completata");
    }

    public static class ValidaInterventiDataModel {

        public static enum Approvazione {

            rimanda, conferma
        }
        private String nomeUtenteValid, cognomeUtente, assistSocValid, note, motivazione;
        private Date dataApert;
        private Approvazione waValidIntApp;

        public String getAssistSocValid() {
            return assistSocValid;
        }

        public String getMotivazione() {
            return motivazione;
        }

        public void setMotivazione(String motivazione) {
            this.motivazione = motivazione;
        }

        public void setAssistSocValid(String assistSocValid) {
            this.assistSocValid = assistSocValid;
        }

        public String getCognomeUtente() {
            return cognomeUtente;
        }

        public void setCognomeUtente(String cognomeUtente) {
            this.cognomeUtente = cognomeUtente;
        }

        public Date getDataApert() {
            return dataApert;
        }

        public void setDataApert(Date dataApertPAI) {
            this.dataApert = dataApertPAI;
        }

        public String getNomeUtenteValid() {
            return nomeUtenteValid;
        }

        public void setNomeUtenteValid(String nomeUtenteValid) {
            this.nomeUtenteValid = nomeUtenteValid;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Approvazione getWaValidIntApp() {
            return waValidIntApp;
        }

        public void setWaValidIntApp(Approvazione waValidIntApp) {
            this.waValidIntApp = waValidIntApp;
        }
    }
}
