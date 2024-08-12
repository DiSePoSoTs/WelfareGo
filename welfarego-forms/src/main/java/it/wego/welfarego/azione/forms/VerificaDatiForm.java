package it.wego.welfarego.azione.forms;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonResponse;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.abstracts.AbstractServlet;
import it.wego.welfarego.azione.models.ImpegnoSpesaBean;
import it.wego.welfarego.azione.models.VerificaImpegniGenericDataModel;
import it.wego.welfarego.azione.stores.ImpegniStoreListener;
import it.wego.welfarego.azione.stores.ImpegniUotStoreListener;
import it.wego.welfarego.bre.utils.BreMessage;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aleph
 *         <p>
 *         chiamata da TaskList / Verifica Dati Esecutività --> VerificaDatiPanel.js
 */
public class VerificaDatiForm extends VerificaImpegniGenericFormListener implements AbstractForm.Loadable, AbstractForm.Saveable, AbstractForm.Proceedable {
    private PaiIntervento interventoTrasformato = null;

    private boolean mayBeDetSingola() {
        return getTask().getPaiIntervento().getTipologiaIntervento().getCodTmplEse() != null;
    }

    private boolean mayBeDetMultipla() {
        return getTask().getPaiIntervento().getTipologiaIntervento().getCodTmplEseMul() != null;
    }


    public Object load() throws Exception {


        getLogger().debug("handling load request");
        String table = getParameter("table");
        String refreshImpegniInterventi = getParameter("nuovoIntervento");

        String limitStr = getParameter("limit");
        String pageStr = getParameter("page");
        String startStr = getParameter("start");
        String orderStr = getParameter("sort");

        String msgTemplate = "table:%s, refreshImpegniInterventi:%s, limitStr:%s, pageStr:%s, startStr:%s, orderStr:%s";
        getLogger().debug(String.format(msgTemplate, table, refreshImpegniInterventi, limitStr, pageStr, startStr, orderStr));

        if (getParameter("requireDocument") != null) {
            Object o = prepareDavDocument(Documenti.TIPO_DOC_TEMPLATE_DETERMINA);
            return o;

        } else if (table == null &&
                (
                        refreshImpegniInterventi == null ||
                                (!refreshImpegniInterventi.equals("nuovoInterventoImpegni") &&
                                        !refreshImpegniInterventi.equals("nuovoInterventoImpegniUot")))) {

            // qui  carico i dati per la scheda Azioni, metti un Thread.dumpStack in loadData....
            // i dati finiscono nel form tramite form.loadRecord(data); in GenericFormPanel.js
            VerificaDatiDataModel verificaDatiDataModel = loadData(VerificaDatiDataModel.class);
            verificaDatiDataModel.setSingola(mayBeDetSingola());
            verificaDatiDataModel.setMultipla(mayBeDetMultipla());
            Validate.isTrue(verificaDatiDataModel.isMultipla() || verificaDatiDataModel.isSingola(),
                    "errore configuratione tipologia intervento <i>" + getTask().getPaiIntervento().getTipologiaIntervento() + "</i><br/>un template esecutività, singola o multipla, deve essere valorizzato");

            JsonBuilder jsonBuilder = JsonBuilder.newInstance();
            jsonBuilder = jsonBuilder.withData(verificaDatiDataModel);
            JsonResponse jsonResponse = jsonBuilder.buildResponse();
            return jsonResponse;


        } else if ((table != null && table.equals("impegni")) ||
                (refreshImpegniInterventi != null && refreshImpegniInterventi.equals("nuovoInterventoImpegni"))) {
            if (getParameter(AbstractServlet.METHOD_PROP).equals(AbstractServlet.METHOD_GET)) {
                ImpegniStoreListener impegniStoreListener = new ImpegniStoreListener(this);
                Object load = impegniStoreListener.load();
                return load;
            } else {
                ImpegniStoreListener impegniStoreListener = new ImpegniStoreListener(this);
                Object update = impegniStoreListener.update();
                return update;
            }

        } else if ((table != null && table.equals("impegniuot")) || (refreshImpegniInterventi != null && refreshImpegniInterventi.equals("nuovoInterventoImpegniUot"))) {
            if (getParameter(AbstractServlet.METHOD_PROP).equals(AbstractServlet.METHOD_GET)) {
                ImpegniUotStoreListener impegniUotStoreListener = new ImpegniUotStoreListener(this);
                Object load = impegniUotStoreListener.load();
                return load;
            }
        } else if (table.equals("messaggiBre")) {
            List<BreMessage> breMessages = BreUtils.getBreMessages(new PaiInterventoDao(getEntityManager()).getLastRichiestaApprovazioneBatch(getTask().getCodPai()));
            return breMessages;
        }
        throw new Exception("unknown/wrong table name : " + table);
    }


    public Object save() throws Exception {

        getLogger().debug("handling save request");
        initTransaction();

        it.wego.welfarego.azione.forms.VerificaDatiForm.VerificaDatiDataModel verificaDatiDataModel = getDataParameter(it.wego.welfarego.azione.forms.VerificaDatiForm.VerificaDatiDataModel.class);
        UniqueTasklist task = getTask();
        PaiIntervento paiIntervento = task.getPaiIntervento();
        Pai pai = paiIntervento.getPai();
        String note = verificaDatiDataModel.getNote();
        String causeRespingimento = verificaDatiDataModel.getCauseRespingimento();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());
        PaiEvento evento;
        switch (verificaDatiDataModel.getEsito()) {
            case rimandato:
                // se l'intervento è rimandato
                getLogger().debug("sono in rimanda");
                task.setCampoForm2((note != null && note.length() > 0) ? note : "nessuna nota");
                // cambio lo stato del pai per permettere di risottomettere l'intervento , da vedere se funzia

                paiIntervento.setDataRichiestaApprovazione(null);
                paiIntervento.setStatoAttuale(PaiIntervento.RIMANDATO);
                if (paiIntervento.getInterventiFigli() != null) {
                    for (InterventiAssociati ia : paiIntervento.getInterventiFigli()) {
                        PaiIntervento intervento = ia.getInterventoFiglio();
                        intervento.setDataRichiestaApprovazione(null);
                        intervento.setStatoAttuale(PaiIntervento.RIMANDATO);
                    }
                }
              /*PaiEventoDao dao = new PaiEventoDao(getEntityManager()); SERVIVA QUANDO C'ERA INTALIO
             PaiEvento esecutività = dao.findByPaiIntervento(paiIntervento, PaiEvento.AVVIO_ESECUTIVITA_INTERVENTO).get(0);
       	    dao.delete(esecutività);*/
                pai.setFlgStatoPai(Pai.STATO_RIFIUTATO);
                new TaskDao(getEntityManager())
                        .withPaiIntervento(paiIntervento)
                        .withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_DARIVEDERE))
                        .withRuolo(Utenti.ASSISTENTE_SOCIALE_UOT.name())
                        .withUot(paiIntervento.getPai().getIdParamUot().getIdParam().getCodParam()).withDesTask("Intervento non approvato-da rivedere ")
                        .withCampoFlow1((note != null && note.length() > 0) ? note : "nessuna nota").insertNewTask();
                insertEvento(PaiEvento.PAI_RIMANDA_INTERVENTO);
                commitTransaction();
                break;
            case respinto:
                //respingo l'intevento
                getLogger().debug("sono in respingi");
                task.setCampoForm2((note != null && note.length() > 0) ? note : "nessuna nota");
                //FIXME ONLY FOR TEST
                //    task.setEsito("DELETE");
                paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                paiIntervento.setDtChius(new Date());
                paiIntervento.setNoteChius(causeRespingimento);
                paiIntervento.setStatoAttuale(PaiIntervento.RESPINTO);

                paiInterventoMeseDao.removeAllProps(paiIntervento);
                //se l'intervento ha dei figli chiudo anche i figli
                if (paiIntervento.getInterventiFigli() != null) {
                    for (InterventiAssociati ia : paiIntervento.getInterventiFigli()) {
                        PaiIntervento intervento = ia.getInterventoFiglio();
                        intervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
                        intervento.setDtChius(new Date());
                        intervento.setNoteChius(causeRespingimento);
                        intervento.setStatoAttuale(PaiIntervento.RESPINTO);
                        paiInterventoMeseDao.removeAllProps(intervento);


                    }
                }
                //task di creazione avvertimento per assistente sociale
                insertEvento(PaiEvento.PAI_RESPINGI_INTERVENTO);
                new TaskDao(getEntityManager())
                        .withPaiIntervento(paiIntervento)
                        .withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_RESPINTO))
                        .withRuolo(Utenti.ASSISTENTE_SOCIALE_UOT.name())
                        .withUot(paiIntervento.getPai().getIdParamUot().getIdParam().getCodParam()).withDesTask("Notifica intervento respinto")
                        .withCampoFlow1((note != null && note.length() > 0) ? note : "nessuna nota").insertNewTask();
                //task di produzione lettera respinto
                new TaskDao(getEntityManager())
                        .withPaiIntervento(paiIntervento)
                        .withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_GENERAZIONE_DOCUMENTO))
                        .withRuolo(Utenti.OPERATORE_SEDE_CENTRALE.name())
                        .withUot(paiIntervento.getPai().getIdParamUot().getIdParam().getCodParam())
                        .withDesTask("Predisposizione documento Diniego intervento")
                        .withTemplate(new TemplateDao(getEntityManager()).findByCodTemplate(new ConfigurationDao(getEntityManager()).getConfig("cod.template.domandarespinta"))).insertNewTask();

                pai.setFlgStatoPai(Pai.STATO_APERTO);
                commitTransaction();

                break;
            case approvato:
                String trasformaIn = Strings.nullToEmpty(verificaDatiDataModel.getTrasformaIn());
                List<ImpegnoSpesaBean> impegni = null;
                boolean trasformazione = false;
                boolean haFigli = false;
                List<InterventiAssociati> ia = new ArrayList<InterventiAssociati>();
                if (paiIntervento.getInterventiFigli() != null && paiIntervento.getInterventiFigli().isEmpty() == false) {
                    ia = paiIntervento.getInterventiFigli();
                    haFigli = true;
                }
                if (!Strings.isNullOrEmpty(trasformaIn) && !trasformaIn.equals(paiIntervento.getPaiInterventoPK().getCodTipint())) {
                    Validate.isTrue(haFigli == false, "Attenzione non posso traformare un intervento che ha associati altri interventi");
                    getLogger().info("Si procederà con trasformazione intervento");
                    String datiVecchioIntervento = "Tipologia intervneto:" + paiIntervento.getTipologiaIntervento().getDesTipint() + "\n" + paiIntervento.getDatiOriginali();
                    paiIntervento = new PaiInterventoDao(getEntityManager()).trasformaIntervento(paiIntervento, trasformaIn);

                    impegni = new ArrayList<ImpegnoSpesaBean>();
                    if (!Strings.isNullOrEmpty(getParameter("impegniStore"))) {
                        impegni = getParameter("impegniStore", new TypeToken<List<ImpegnoSpesaBean>>() {
                        }.getType());
                    }
                    paiIntervento.setPai(pai);
                    paiIntervento.setTipologiaIntervento(new TipologiaInterventoDao(getEntityManager()).findByCodTipint(trasformaIn));
                    paiIntervento.setDatiOriginali("Intervento originale \n" + datiVecchioIntervento);
                    trasformazione = true;

                }
                paiIntervento.setQuantita(new BigDecimal(verificaDatiDataModel.getImpMens().replace(',', '.')));
                paiIntervento.setCostoPrev(getCostoTot());
                //QUESTE NOTE NON SERVONO A NULLA IN QUESTO CASO DATO CHE NON LE LEGGE NESSUNO.....
                //   task.setCampoForm2((note != null && note.length() > 0) ? note : "nessuna nota");

                if (!Strings.isNullOrEmpty(verificaDatiDataModel.getDurata())) {
                    paiIntervento.setDurMesi(Integer.parseInt(verificaDatiDataModel.getDurata()));
                }

                paiIntervento.setDtFine(verificaDatiDataModel.getDataFine());


                //  paiIntervento.setPai(pai);
                pai.setFlgStatoPai(Pai.STATO_APERTO);

                DateMidnight data1 = new DateMidnight(verificaDatiDataModel.getDataAvvio());
                DateMidnight data2 = new DateMidnight(new Date()).minusDays(1);


                if (data1.isBefore(data2)) {

                    Validate.isTrue(isPid(paiIntervento), "Attenzione!! Non si può mandare avanti un intervento con data di avvio precedente ad oggi, a meno che l'intervento in questione non sia del tipo PID.");
                }

                paiIntervento.setDtAvvio(verificaDatiDataModel.getDataAvvio());
                paiIntervento.setDtFine(verificaDatiDataModel.getDataFine());
                //check se è pid e quindi vuole durata settimanale
                if (paiIntervento.getDtFine() != null) {
                    Date dataAvvioIntervento = paiIntervento.getDtAvvio();
                    Date dataFine = paiIntervento.getDtFine();
                    int weeks = Weeks.weeksBetween(new LocalDate(dataAvvioIntervento), new LocalDate(dataFine)).getWeeks();
                    paiIntervento.setDurSettimane(weeks);
                }
                paiIntervento.setStatoAttuale(PaiIntervento.DETERMINA);
                if (!Strings.isNullOrEmpty(verificaDatiDataModel.getDurMesiProroga()) && !verificaDatiDataModel.getDurMesiProroga().equals("0")) {
                    //prima facciamo qualche check....
                    Validate.isTrue(haFigli == false, "Attenzione non posso prorogare un intervento che ha un intervento ");
                    if (paiIntervento.getTipologiaIntervento().getFlgRinnovo().equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_PROROGA)) {
                        //TODO DOVREMO GESTIRE ANCHE PROROGHE CON DATA FINE ?????
                        paiIntervento.setDurMesiProroga(Integer.valueOf(verificaDatiDataModel.getDurMesiProroga()));
                        paiIntervento.setDatiOriginali(paiIntervento.getDatiOriginali() + "\n ATTENZIONE al termine del suo periodo di esecutività l'intervento sarà rinnovato per " + verificaDatiDataModel.getDurMesiProroga() + " mesi.");
                        //adesso mi controllo gli impegni per vedere in che budget lo hanno piazzato
                        impegni = new ArrayList<ImpegnoSpesaBean>();
                        if (!Strings.isNullOrEmpty(getParameter("impegniStore"))) {
                            impegni = getParameter("impegniStore", new TypeToken<List<ImpegnoSpesaBean>>() {
                            }.getType());
                        }
                        for (ImpegnoSpesaBean impegno : impegni) {
                            if (!Strings.isNullOrEmpty(impegno.getCarico()) && !impegno.getCarico().equals("0") && paiIntervento.getCodImpProroga() == null) {
                                BudgetTipInterventoPK pk = new BudgetTipInterventoPK(paiIntervento.getPaiInterventoPK().getCodTipint(), Short.parseShort(impegno.getAnno()), impegno.getImpegno());
                                paiIntervento.setCodImpProroga(pk.getCodImpe());
                            }


                        }

                    }
                }
                if (trasformazione) {
                    insertEvento(pai, "Trasformato intervento :" + task.getPaiIntervento().getTipologiaIntervento().getDesTipint() + "  in intervento:" + paiIntervento.getTipologiaIntervento().getDesTipint());
                    //   insertEvento(paiIntervento,PaiEvento.AVVIO_ESECUTIVITA_INTERVENTO);
                    commitTransaction();
                    for (ImpegnoSpesaBean impegno : impegni) {
                        BudgetTipInterventoPK pk = new BudgetTipInterventoPK(paiIntervento.getPaiInterventoPK().getCodTipint(), Short.parseShort(impegno.getAnno()), impegno.getImpegno());
                        paiInterventoMeseDao.insertProp(paiIntervento.getPaiInterventoPK(), pk, new BigDecimal(impegno.getCarico().replaceAll(",", ".")), new BigDecimal(impegno.getQuantita()), pai.getIdParamFascia());


                    }
                    //dobbiamo prorogare l'intervento


                    interventoTrasformato = paiIntervento;
                } else {
                    // approvo tutti i figli
                    for (InterventiAssociati inter : ia) {
                        PaiIntervento figlio = inter.getInterventoFiglio();
                        figlio.setDtAvvio(paiIntervento.getDtAvvio());
                        figlio.setDurMesi(paiIntervento.getDurMesi());
                        figlio.setDtFine(paiIntervento.getDtFine());
                        // figlio.setQuantita(paiIntervento.getQuantita());
                        /**
                         * Abbiamo tolto UESTA FUNZIONALITÀ PER ESSERE più flessibili.
                         */
                 /*  BigDecimal costoFiglio = new PaiInterventoDao(getEntityManager()).calcolaCostoInterventoConStruttura(figlio.getPaiInterventoPK().getCodTipint(), figlio.getQuantita(), figlio.getDurMesi(), figlio.getDtAvvio(), figlio.getDtFine(), figlio.getIdStruttura(), figlio.getRiduzione(), figlio.getPaiInterventoPK().getCodPai(), figlio.getPaiInterventoPK().getCntTipint().toString(), false, figlio.getPrimoFiglio()=='S'?true:false, figlio.getSecondoFiglio()=='S'?true:false);
                   for(PaiInterventoMese pim : figlio.getPaiInterventoMeseList()){
        			   for(PaiInterventoMese pimPadre : paiIntervento.getPaiInterventoMeseList()){
        				   if(pimPadre.getBudgetTipIntervento().equals(pim.getBudgetTipIntervento())){
        					   pim.setBdgPrevEur(costoFiglio);
        					   pim.setBdgPrevQta(pimPadre.getBdgPrevQta());
        				   }
        			   }
        			   paiInterventoMeseDao.update(pim);
        		   }*/


                        figlio.setStatoAttuale(PaiIntervento.DETERMINA);

                    }
                    insertEvento(PaiEvento.PAI_UPDATE_INTERVENTO);
                    commitTransaction();
                }

                break;
        }


        return JsonBuilder.newInstance().buildResponse();
    }

    private static final Map<VerificaDatiDataModel.Esito, String> esito2esitoTask;
    private static final Map<VerificaDatiDataModel.TipoDetermina, String> tipDet2tipDetTask;

    static {

        Map<VerificaDatiDataModel.Esito, String> map = new EnumMap<VerificaDatiDataModel.Esito, String>(VerificaDatiDataModel.Esito.class);
        map.put(VerificaDatiDataModel.Esito.approvato, TaskDao.FLAG_SI);
        map.put(VerificaDatiDataModel.Esito.rimandato, TaskDao.FLAG_R);
        map.put(VerificaDatiDataModel.Esito.respinto, TaskDao.FLAG_INTERVENTO_RESPINTO);
        esito2esitoTask = Collections.unmodifiableMap(map);
        Map<VerificaDatiDataModel.TipoDetermina, String> map2 = new EnumMap<VerificaDatiDataModel.TipoDetermina, String>(VerificaDatiDataModel.TipoDetermina.class);
        map2.put(VerificaDatiDataModel.TipoDetermina.singola, "S");
        map2.put(VerificaDatiDataModel.TipoDetermina.multipla, "M");
        tipDet2tipDetTask = Collections.unmodifiableMap(map2);
    }


    public Object proceed_test() throws Exception {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("idDocumentoAutorizzazione", 4);
        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        jsonBuilder = jsonBuilder.withData(result);
        JsonResponse jsonResponse = jsonBuilder.buildResponse();
        return jsonResponse;
    }

    /*
    at it.wego.welfarego.abstracts.GenericAbstractServlet.doPost(GenericAbstractServlet.java:50)
	at it.wego.welfarego.abstracts.AbstractServlet.processRequest(AbstractServlet.java:64)
	at it.wego.welfarego.abstracts.AbstractForm.handleRequest(AbstractForm.java:156)
    ....
    formHandler = AzioneFormFactory.createFormHandler(parameters);
    ....
     */
    public Object proceed() throws Exception {
        Map<String, Integer> result = new HashMap<String, Integer>();

        getLogger().debug("VerificaDatiForm.proceed handling proceed request");
        String data = getParameter("data");
        JSONObject jsonObject = new JSONObject(data);
        String testo_autorizzazione = jsonObject.getString("testo_autorizzazione");

        UniqueTasklist task = getTask();
        VerificaDatiDataModel verificaDatiDataModel = getDataParameter(VerificaDatiDataModel.class);

        initTransaction();

        getLogger().debug("testo_autorizzazione: " + testo_autorizzazione);
        task.getPaiIntervento().setTestoAutorizzazione(testo_autorizzazione);

        save();

        boolean trasformazione = false;
        String trasformaIn = Strings.nullToEmpty(verificaDatiDataModel.getTrasformaIn());
        PaiIntervento paiIntervento = null;
        if (!Strings.isNullOrEmpty(trasformaIn) && !trasformaIn.equals(task.getPaiIntervento().getPaiInterventoPK().getCodPai().toString())) {
            paiIntervento = interventoTrasformato;
            trasformazione = true;
        } else {
            paiIntervento = task.getPaiIntervento();
        }

        PaiInterventoPK paiInterventoPK = paiIntervento.getPaiInterventoPK();
        EntityManager entityManager = getEntityManager();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);



        //TODO al momento le determine sono tutte multiple....

     /*   Validate.isTrue((verificaDatiDataModel.isSingola() && mayBeDetSingola()) || (verificaDatiDataModel.isMultipla() && mayBeDetMultipla()),
                "un'opzione tra determina singola e multipla deve essere indicata");*/

        //TODO verifica dati
        BigDecimal costoTot = getCostoTot(paiIntervento);
        BigDecimal propSpesaTot = paiInterventoMeseDao.findSumProp(paiInterventoPK);


        if (richiesta_approvata(verificaDatiDataModel)) {

            Validate.isTrue(costoTot.compareTo(propSpesaTot) == 0, "le proposte di spesa devono coprire il costo totale dell'intervento<br/>costo tot : " + costoTot + " E , prop spesa tot : " + propSpesaTot + " E");
            PaiEvento paiEvento = insertEvento(paiIntervento, PaiEvento.PAI_CONFERMA_INTERVENTO);

            boolean determina_singola = is_determina_singola(verificaDatiDataModel);
            getLogger().debug("determina_singola: " + determina_singola + " per intervento:" + paiIntervento.toString());

            if (determina_singola) {
                elabora_determina_singola(paiIntervento, entityManager, paiEvento);
            } else {
                elabora_determina_multipla(paiIntervento, paiEvento);
            }

            ConfigurationDao configurationDao = new ConfigurationDao(entityManager);
            Map<String, String> configWithPrefix = configurationDao.getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX);

            TipologiaIntervento tipologiaIntervento = paiIntervento.getTipologiaIntervento();

            PaiDocumento paiDocumento = crea_documento_di_approvazione(paiIntervento, entityManager, paiEvento, configWithPrefix, tipologiaIntervento);
            if(paiDocumento != null) {
                Integer idDocumento = paiDocumento.getIdDocumento();
                result.put("idDocumentoAutorizzazione", idDocumento);
            }

        } else {
            getLogger().debug("non approvato, niente da fare");
        }

        task.setCampoForm1(tipDet2tipDetTask.get(verificaDatiDataModel.getTipoDetermina()));
        new TaskDao(entityManager).markQueued(task, esito2esitoTask.get(verificaDatiDataModel.getEsito())); // TODO verificare sintassi esito

        commitTransaction();
        //IntalioAdapter.executeJob();
        //se è un intervento da trasformare cancelliamo il vecchio e saluti...
        if (trasformazione) {
            new PaiInterventoDao(entityManager).deleteIntervento(task.getPaiIntervento());
        }

        // non togliere queste righe, il front end si basa sul buildResponse per la gestione della response.
        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        jsonBuilder.withData(result);
        return jsonBuilder.buildResponse();
    }

    public PaiDocumento crea_documento_di_approvazione(PaiIntervento paiIntervento, EntityManager entityManager, PaiEvento paiEvento, Map<String, String> configWithPrefix, TipologiaIntervento tipologiaIntervento) throws Exception {
        if ('S' == tipologiaIntervento.getFlgDocumentoDiAutorizzazione()) {
            Template tmplDocumentoDiApprovazione = tipologiaIntervento.getTmplDocumentoDiAutorizzazione();
            DynamicOdtUtils dynamicOdtUtils = DynamicOdtUtils.newInstance();
            dynamicOdtUtils = dynamicOdtUtils.withTemplateBase64(tmplDocumentoDiApprovazione.getClobTmpl());
            String xmlCartellaSociale = Pratica.getXmlCartellaSociale(paiIntervento);
            dynamicOdtUtils = dynamicOdtUtils.withDataXml(xmlCartellaSociale);
            dynamicOdtUtils = dynamicOdtUtils.withConfig(configWithPrefix);
            byte[] documentResult = dynamicOdtUtils.getResult();

            String documentResultEncoded = Base64.encodeBase64String(documentResult);
            PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);
            PaiDocumento paiDocumento = paiDocumentoDao.createDoc(paiEvento, Documenti.TIPO_DOC_TEMPLATE_DOCUMENTO_APPROVAZIONE, documentResultEncoded, "documento_di_autorizzazione.odt");
            return paiDocumento;
        }
        return null;
    }

    public boolean is_determina_singola(VerificaDatiDataModel verificaDatiDataModel) {
        boolean determina_singola = verificaDatiDataModel.getTipoDetermina().equals(VerificaDatiDataModel.TipoDetermina.singola);
        return determina_singola;
    }

    public boolean richiesta_approvata(VerificaDatiDataModel verificaDatiDataModel) {
        return verificaDatiDataModel.getEsito().equals(VerificaDatiDataModel.Esito.approvato);
    }

    public void elabora_determina_multipla(PaiIntervento paiIntervento, PaiEvento paiEvento) throws Exception {
        getLogger().debug("determina multipla, settiamo solo flag");
        paiEvento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
        //eventi figli
        if (paiIntervento.getInterventiFigli() != null && paiIntervento.getInterventiFigli().isEmpty() == false) {
            for (InterventiAssociati ia : paiIntervento.getInterventiFigli()) {
                PaiEvento e = insertEvento(ia.getInterventoFiglio(), PaiEvento.PAI_CONFERMA_INTERVENTO);
                e.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
            }
        }
    }

    public void elabora_determina_singola(PaiIntervento paiIntervento, EntityManager entityManager, PaiEvento paiEvento) throws Exception {
        paiEvento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
        char statoIntervento = paiIntervento.getStatoInt();
        Template template = null;

        TipologiaIntervento tipologiaIntervento = paiIntervento.getTipologiaIntervento();
        if (statoIntervento == PaiIntervento.STATO_INTERVENTO_APERTO
                || statoIntervento == PaiIntervento.STATO_INTERVENTO_RIMANDATO) {
            getLogger().debug("*** La determina è di esecutività");
            template = tipologiaIntervento.getCodTmplEse();
        } else if (statoIntervento == PaiIntervento.STATO_INTERVENTO_CHIUSO) {
            getLogger().debug("*** La determina è di chiusura");
            template = tipologiaIntervento.getCodTmplChius();
        } else {
            getLogger().debug("*** La determina è di variazione");
            template = tipologiaIntervento.getCodTmplVar();
        }

//                                else if (statoIntervento == Interventi.STATO_INTERVENTO_ESECUTIVO) {
//                                    template = paiIntervento.getTipologiaIntervento().getCodTmplVar();
//                                }

//                WelfareGoIntalioManager welfareGoIntalioManager = new WelfareGoIntalioManager();
//                welfareGoIntalioManager.setProperty(WelfareGoIntalioManager.DYN_DOC_URL, ToolsUtils.getConfig("PROPERTY_DYN_DOC_URL"));
//                byte[] documentResult = welfareGoIntalioManager.generaDocumento(Base64.decodeBase64(template.getClobTmpl()), paiEvento.getPaiDox().getBytes(), "doc");
        ConfigurationDao configurationDao = new ConfigurationDao(entityManager);
        Map<String, String> configWithPrefix = configurationDao.getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX);

        DynamicOdtUtils dynamicOdtUtils = DynamicOdtUtils.newInstance();
        dynamicOdtUtils = dynamicOdtUtils.withTemplateBase64(template.getClobTmpl());
        dynamicOdtUtils = dynamicOdtUtils.withDataXml(paiEvento.getPaiDox());
        dynamicOdtUtils = dynamicOdtUtils.withConfig(configWithPrefix);
        byte[] documentResult = dynamicOdtUtils.getResult();

        String documentResultEncoded = Base64.encodeBase64String(documentResult);
        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);
        paiDocumentoDao.createDoc(paiEvento, Documenti.TIPO_DOC_TEMPLATE_DETERMINA, documentResultEncoded, "determina.doc");
    }

    /**
     * mI DICE QUALE INTERVENTO  può avere una data retroattiva e quale no...all'inizio dovevano essere solo 3 le eccezioni ( i pid ) poi sono diventate di più,...da pensare ad un modo per farlo meglio.. in futuro
     *
     * @param intervento
     * @return
     */

    private boolean isPid(PaiIntervento intervento) {
        String codiceInt = intervento.getPaiInterventoPK().getCodTipint();
        if (codiceInt.equals("EC001") || codiceInt.equals("EC002") || codiceInt.equals("EC004") || codiceInt.equals("EC005")
                || codiceInt.equals("AD009") || codiceInt.equals("MI101") || codiceInt.equals("MI102")
                ) {
            return false;
        } else {
            return true;
        }
    }

    public static class VerificaDatiDataModel extends VerificaImpegniGenericDataModel {

        private Esito esito;
        private TipoDetermina tipoDetermina;
        private boolean singola, multipla;
        public String note;
        public String causeRespingimento;


        public boolean isSingola() {
            return singola;
        }

        public boolean isMultipla() {
            return multipla;
        }

        public void setMultipla(boolean multipla) {
            this.multipla = multipla;
        }

        public void setSingola(boolean singola) {
            this.singola = singola;
        }

        public static enum Esito {

            approvato, rimandato, respinto
        }

        public static enum TipoDetermina {

            singola, multipla
        }

        public Esito getEsito() {
            return esito;
        }

        public void setEsito(Esito esito) {
            this.esito = esito;
        }

        public TipoDetermina getTipoDetermina() {
            return tipoDetermina;
        }

        public void setTipoDetermina(TipoDetermina tipoDetermina) {
            this.tipoDetermina = tipoDetermina;
        }

        public String getCauseRespingimento() {
            return causeRespingimento;
        }

        public void setCauseRespingimento(String causeRespingimento) {
            this.causeRespingimento = causeRespingimento;
        }

    }


    public BigDecimal getCostoTot(PaiIntervento paiIntervento) throws Exception {
        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        BigDecimal bigDecimal = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(paiIntervento));
        getLogger().info("## getCostoTot: " + bigDecimal + ", paiIntervento: " + paiIntervento.toString());
        return bigDecimal;
    }
}
