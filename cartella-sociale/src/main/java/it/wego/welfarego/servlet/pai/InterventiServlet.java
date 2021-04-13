package it.wego.welfarego.servlet.pai;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.unique.intalio.IntalioManager;
import it.wego.unique.intalio.processmanagement.TInstanceInfo;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.intalio.WelfareGoIntalioManager;
import it.wego.welfarego.model.CivilmenteObbligatoBean;
import it.wego.welfarego.model.ContribuzioneBean;
import it.wego.welfarego.model.CronologiaInterventoBean;
import it.wego.welfarego.model.ImpegnoSpesaBean;
import it.wego.welfarego.model.PagamentoBean;
import it.wego.welfarego.model.json.JSONCronologiaIntervento;
import it.wego.welfarego.model.json.JSONInterventoList;
import it.wego.welfarego.model.json.JSONMessage;
import it.wego.welfarego.model.json.JSONPagamenti;
import it.wego.welfarego.model.json.JsonImporto;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.AssociazioneDao;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoUotDao;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.InterventiAssociatiDao;
import it.wego.welfarego.persistence.dao.MapDatiSpecificiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoCivObbDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Associazione;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoUot;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.Pai;

import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.serializer.InterventoSerializer;
import it.wego.welfarego.serializer.PagamentiSerializer;
import it.wego.welfarego.serializer.PaiInterventoCivObbSerializer;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.WelfaregoUtils;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author giuseppe
 */
public class InterventiServlet extends JsonServlet {

	
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {

        String action = getAction();
        getLogger().debug(String.format("action:%s", action));

        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
        EntityManager entityManager = persistenceAdapter.getEntityManager();

        try {
            AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(entityManager);
            PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
            TipologiaInterventoDao tipologiaInterventoDao = new TipologiaInterventoDao(entityManager);
            String cntTipint = Strings.emptyToNull(getParameter("cntTipint"));
            
            Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
            String codPai = Strings.emptyToNull(getParameter("codPai")), codTipint = Optional.fromNullable(Strings.emptyToNull(getParameter("codTipint"))).or(Optional.fromNullable(Strings.emptyToNull(getParameter("tipo")))).orNull();
            String tipOld = Strings.emptyToNull(getParameter("codTipIntHidden"));
            boolean empty = !Strings.isNullOrEmpty(request.getParameter("empty"));


            if (Objects.equal(action, "write")) {
                getLogger().debug(String.format("sono in write"));
                return salvaIntervento(request, entityManager, anagrafeDao, paiInterventoDao, cntTipint, connectedUser, codPai, codTipint, tipOld);

            } else if (Objects.equal(action, "read")) {
                getLogger().debug(String.format("sono in read"));
                return JsonBuilder.newInstance()
                        .withParameters(getParameters())
                        .withData(Strings.isNullOrEmpty(codPai) ? Collections.<PaiIntervento>emptyList() : new PaiInterventoDao(entityManager).findByCodPai(codPai,"dtApe",false))
                        .withTransformer(InterventoSerializer.getinterventoMinifiedSerializer()).buildStoreResponse();
            }
            else if (Objects.equal(action, "readSocialCrt")) {
                getLogger().debug(String.format("sono in readSocialCrt"));
                return JsonBuilder.newInstance()
                        .withParameters(getParameters())
                        .withData(Strings.isNullOrEmpty(codPai) ? Collections.<PaiIntervento>emptyList() : new PaiInterventoDao(entityManager).findByCodPaiSocialCrt(Integer.valueOf(codPai)))
                        .withTransformer(InterventoSerializer.getinterventoSocialCrtMinifiedSerializer()).buildStoreResponse();
            }
            //sospensione intervento
            else if(Objects.equal(action, "sospendi")){
                getLogger().debug(String.format("sono in sospendi"));
                JSONMessage json = new JSONMessage();
                PaiIntervento pi = paiInterventoDao.findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                if(pi==null){
                    throw new IllegalArgumentException("Attenzione: Questo intervento non è ancora stato aperto, assicurati di salvarlo prima di sospenderlo!");
                }
                if(pi.getStatoInt()=='S'){
                    pi.setStatoInt(PaiIntervento.STATO_INTERVENTO_ESECUTIVO);
                    pi.setStatoAttuale(PaiIntervento.GESTIONE_ECONOMICA);
                    pi.setDtSosp(null);
                    paiInterventoDao.update(pi);
                    json.setMessage("Intervento rimesso in esecutività correttamente");
                    json.setSuccess(true);
                    PaiEventoDao pedao = new PaiEventoDao(entityManager);
                    PaiEvento evento = new PaiEvento();
                    evento.setCodPai(pi.getPai());
                    evento.setCodUte(connectedUser);
                    evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
                    evento.setDesEvento("Fine sospensione intervento");
                    evento.setPaiDox("x");
                    evento.setTsEvePai(new Date());
                    evento.setPaiIntervento(pi);
                    pedao.insert(evento);
                    return json;

                    //lo desospendo
                }
                else if(pi.getStatoInt()=='E'){
                    Date dataSospensione  =StringConversion.itStringToDate(getParameter("datasospensione"));
                    String motivazione = getParameter("motivazionesospensione");
                    pi.setDtSosp(dataSospensione);
                    pi.setNoteSospensione(motivazione);
                    pi.setStatoAttuale(PaiIntervento.SOSPESO);
                    pi.setStatoInt(PaiIntervento.STATO_INTERVENTO_SOSPESO);
                    paiInterventoDao.update(pi);
                    PaiEventoDao pedao = new PaiEventoDao(entityManager);
                    PaiEvento evento = new PaiEvento();
                    evento.setCodPai(pi.getPai());
                    evento.setCodUte(connectedUser);
                    evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
                    evento.setDesEvento("Sospensione intervento");
                    evento.setPaiDox("x");
                    evento.setPaiIntervento(pi);
                    evento.setTsEvePai(new Date());
                    pedao.insert(evento);

                    json.setMessage("Intervento sospeso correttamente");
                    json.setSuccess(true);
                    return json;

                    //lo risospendo
                }
                else {
                    throw new IllegalArgumentException("Attenzione: Questo intervento non è ancora stato aperto, assicurati di salvarlo prima di sospenderlo!");
                }
            }
            //azione che verifica che non vi siano interventi aperti e in caso lo comunica all' a. s ...idiot proof ( si spera )
            else if (Objects.equal(action, "verificaIntervento")){
                getLogger().debug(String.format("sono in verificaIntervento"));

                List<PaiIntervento> interventiStessoTipo = new PaiInterventoDao(entityManager).findByCodPaiCodTipint(Integer.valueOf(codPai), codTipint);
                boolean ancoraAperti = false;
                Iterator<PaiIntervento> i = interventiStessoTipo.iterator();
                while(i.hasNext()){
                    PaiIntervento pi =  i.next();
                    if(!pi.isChiuso()){
                        ancoraAperti=true;
                        break;
                    }
                }
                JSONMessage json = new JSONMessage();
                String message = ancoraAperti==true?"Attenzione! Esiste già un intervento non chiuso per questa tipologia ("+codTipint+ "). \n Duplica il vecchio intervento oppure chiudilo anticipatamente per salvare questo intervento." : "";

                json.setSuccess(true);
                json.setMessage(message);

                return json;
            }
            else if (Objects.equal(action, "clone")) {

                return duplica_intervento(entityManager, cntTipint, connectedUser, codPai, codTipint);

            } else if (Objects.equal(action, "cronologia")) {
                getLogger().debug(String.format("sono in cronologia"));

                PaiEventoDao dao = new PaiEventoDao(entityManager);

                List<CronologiaInterventoBean> cronologia = new ArrayList<CronologiaInterventoBean>();
                if (!empty) {
                    String tipoInt = request.getParameter("tipo");
                    PaiIntervento intervento = paiInterventoDao.findByKey(Integer.parseInt(codPai), tipoInt, cntTipint);
                    List<PaiEvento> eventi = dao.findByPaiIntervento(intervento);
                    for (PaiEvento evento : eventi) {
                        CronologiaInterventoBean bean = serializeCronologia(evento);
                        cronologia.add(bean);
                    }
                }
                JSONCronologiaIntervento json = new JSONCronologiaIntervento();
                json.setCronologia(cronologia);
                json.setSuccess(true);
                return json;

            }else if (Objects.equal(action, "calcolaCosto") ) {

                return calcolaCosto(request, tipologiaInterventoDao, codPai, empty);
            }
            else if (Objects.equal(action, "contribuzioni")) {
                Validate.notNull(codPai, "missing parameter: codPai");
                Validate.notNull(codTipint, "missing parameter: codTipint (tipo)");
                Validate.notNull(cntTipint, "missing parameter: cntTipint");

                PaiIntervento paiIntervento = new PaiInterventoDao(entityManager).findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                return JsonBuilder.newInstance().withData(Iterables.filter(Iterables.transform(paiIntervento.getPaiInterventoMeseList(), new Function<PaiInterventoMese, FatturaDettaglio>() {
                    public FatturaDettaglio apply(PaiInterventoMese paiInterventoMese) {
                        return paiInterventoMese.getIdFattDettaglio();
                    }
                }), Predicates.notNull())).withTransformer(new Function<FatturaDettaglio, ContribuzioneBean>() {
                    public ContribuzioneBean apply(FatturaDettaglio dettaglio) {
                        ContribuzioneBean bean = new ContribuzioneBean();
                        if (dettaglio.getTimbro() != null) {
                            bean.setData(dettaglio.getTimbro());
                        }
                        if (dettaglio.getImporto() != null) {
                            bean.setImporto(String.valueOf(dettaglio.getImporto()));
                        }
                        
                        if (dettaglio.getIdFatt() != null) {
                            bean.setIdFattura(String.valueOf(dettaglio.getIdFatt().getIdFatt()));
                        }
                        if (dettaglio.getIdFatt() != null) {
                            bean.setFattura(String.valueOf(dettaglio.getIdFatt().getNumFatt()));
                        }
                        //Forzo a zero il pagato
                        bean.setPagato("0");
                        return bean;
                    }
                }).buildStoreResponse();
            } else if (Objects.equal(action, "pagamenti")) {

                String tipo = request.getParameter("tipo");
                PaiIntervento intervento = null ;
                if(codPai!=null && codTipint!=null && cntTipint!=null){
                    intervento = paiInterventoDao.findByKey(Integer.parseInt(codPai), tipo, cntTipint);
                }
                List<PagamentoBean> pagamenti = new ArrayList<PagamentoBean>();
                Map<Integer, PagamentoBean> mandati = new HashMap<Integer, PagamentoBean>();
                if(intervento!=null){
                    for (PaiInterventoMese mese : intervento.getPaiInterventoMeseList()) {
                        if (mese.getIdManDettaglio() != null) {
                            mandati.put(mese.getIdManDettaglio().getIdManDettaglio(), PagamentiSerializer.serializePagamentoBean(mese.getIdManDettaglio()));
                        }
                    }
                    for (Map.Entry<Integer, PagamentoBean> entry : mandati.entrySet()) {
                        pagamenti.add(entry.getValue());
                    }
                }
                JSONPagamenti json = new JSONPagamenti();
                json.setSuccess(true);
                json.setPagamenti(pagamenti);
                return json;

            }//delete action
            else if(Objects.equal(action, "delete")){
                PaiIntervento pi = paiInterventoDao.findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                JSONMessage json = new JSONMessage();
                PaiEventoDao pedao = new PaiEventoDao(entityManager);
                Validate.notNull(pi, "Attenzione l'intervento designato per la cancellazione non esiste... aggiornare la pagina e verificare che non sia stato già cancellato!");
                Validate.isTrue(pi.getStatoInt()=='A', "Attenzione! L'intervento è in esecutività o è già chiuso quindi non può essere cancellato!");
                Validate.isTrue(pi.getDeterminaAssociata()==null ,"Attenzione,  è già stata prodotta una determina di esecutività per questo intervento  quindi non può essere cancellato!");

                //vedo se l'intervento non è stato già determinato

                List<InterventiAssociati> figli = pi.getInterventiFigli();
                if (figli != null)
                {
                    for (InterventiAssociati figlio : figli)
                    {
                        this.deleteIntervento(figlio.getInterventoFiglio(), entityManager, connectedUser, pedao);
                    }
                }
                this.deleteIntervento(pi, entityManager, connectedUser, pedao);

                json.setMessage("Operazione eseguita correttamente");
                json.setSuccess(true);
                return json;

            }//close action
            else if (Objects.equal(action, "close")) {
                String tipoInt = request.getParameter("tipo");
                String note = Strings.emptyToNull(request.getParameter("noteChiusura"));
                Validate.notNull(note, "E' necessario specificare le note di chiusura");
                String ipEsito = Strings.emptyToNull(getParameter("esito"));
                String codParamEsito = entityManager.find(ParametriIndata.class, Integer.valueOf(ipEsito)).getIdParam().getCodParam();
                String dataChiusura = Strings.emptyToNull(getParameter("dataChiusura"));
                Validate.notNull(dataChiusura, "E' necessario specificare la data di chiusura dell'intervento");
                JSONMessage json = new JSONMessage();
                PaiIntervento paiIntervento = paiInterventoDao.findByKey(Integer.parseInt(codPai), tipoInt, cntTipint);
                TaskDao taskDao = new TaskDao(entityManager);
                List<UniqueTasklist> tasks = taskDao.findAllActiveTaskByPaiIntervento(paiIntervento);
                entityManager.getTransaction().begin();
                for (UniqueTasklist task : tasks) {
                    task.setFlgEseguito("S");
                }
                paiInterventoDao.chiudiIntervento(paiIntervento, note, codParamEsito, StringConversion.itStringToDate(dataChiusura));
                PaiEventoDao paiEventoDao = new PaiEventoDao(entityManager);
                PaiEvento evento = Pratica.serializePaiEvento(paiIntervento.getPai(), paiIntervento, paiIntervento.getPai().getCartellaSociale(), PaiEvento.PAI_CHIUDI_INTERVENTO, connectedUser);
                evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
                paiEventoDao.insert(evento);
                CartellaSocialeWsClient.newInstance().withEntityManager(entityManager).loadConfigFromDatabase().withPaiIntervento(paiIntervento).sincronizzaIntervento();

                // TODO import_export csr
                entityManager.getTransaction().commit();
                json.setMessage("Operazione eseguita correttamente");
                json.setSuccess(true);
                return json;
            }
            Preconditions.checkArgument(false, "unhandled action = '%s'", action);
            throw new UnsupportedOperationException("unreacheable");
        }
        catch (Exception ex) {
        	throw ex;
        }
        finally {
            persistenceAdapter.close();
        }
    }


    public Object duplica_intervento(EntityManager entityManager, String cntTipint, Utenti connectedUser, String codPai, String codTipint) throws Exception {
        getLogger().debug(String.format("sono in duplica_intervento"));

        Pai pai = new PaiDao(entityManager).findPai(codPai);
        if(codTipint.equals("EC100")){
            throw new IllegalArgumentException("Attenzione: Non si possono duplicare interventi di questo tipo.Usare il programma SIMIA. Grazie");
        }
        if(new InterventiAssociatiDao(entityManager).findInterventoPadre(Integer.parseInt(codPai), codTipint, Integer.parseInt(cntTipint)) != null){
            throw new IllegalArgumentException("ATTENZIONE non posso duplicare questo intervento dato che è associato ad un intervento principale. Duplicare l'intervento principale e associare successivamente i famigliari.");
        }
        if(pai==null || pai.getFlgStatoPai()==Pai.STATO_CHIUSO ){
            throw new IllegalArgumentException("ATTENZIONE questo PAI risulta chiuso: impossibile duplicare l'intervento.Aprire un altro PAI ed aprire un nuovo intervento");
        }
        entityManager.getTransaction().begin();
        PaiIntervento paiIntervento = null;
        try{
            PaiInterventoDao pid = new PaiInterventoDao(entityManager);
            pid.findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
            paiIntervento = pid.clonaIntervento(codPai, codTipint, cntTipint);
            PaiEvento evento = Pratica.serializePaiEvento(pai, paiIntervento, paiIntervento.getPai().getCartellaSociale(), PaiEvento.PAI_APERTURA_INTERVENTO_DUPLICATO, connectedUser);
            PaiEventoDao paiEventoDao = new PaiEventoDao(entityManager);
            paiEventoDao.insert(evento);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Problemi nella duplicazione dell'intervento \n Assicurarsi che i dati specifici dell'intervento originario siano stati compilati");
        }

        entityManager.getTransaction().commit();
        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        jsonBuilder = jsonBuilder.withData(paiIntervento);
        Function<PaiIntervento, ?> transformer = InterventoSerializer.getinterventoMinifiedSerializer();
        jsonBuilder = jsonBuilder.withTransformer(transformer);
        return jsonBuilder.buildStoreResponse();
    }



    Object calcolaCosto(HttpServletRequest request, TipologiaInterventoDao tipologiaInterventoDao, String codPai, boolean empty) {
        String codTipint = request.getParameter("tipo");
        String forfaitAsString = request.getParameter("forfait");
        String quantitaAsString = request.getParameter("quantita");
        String durataMesiAsString = request.getParameter("durataMesi");
        String dataAvvio = request.getParameter("dataAvvio");
        String dataFine = request.getParameter("dataFine");
        String dataAvvioProposta = request.getParameter("dataAvvioProposta");

        BigDecimal costo = BigDecimal.ZERO;

        TipologiaIntervento tipologiaIntervento = tipologiaInterventoDao.findByCodTipint(codTipint);

        // la tariffa non viene presa in considerazione qui, lato FE, il suo valore viene riportato nel campo quantita
        // e poi l'utente lo modifica come preferisce.
        /*
        Vedi InterventiForm, elemento tariffaGrid, listener select
        ...
        Ext.getCmp('wcs_interventoQuantita').setValue(record.data.costo);
        ...
         */
        Tariffa tariffa = null;

        Boolean forfait = Boolean.valueOf(forfaitAsString);

        if (!empty) {
            BigDecimal quantita = Strings.isNullOrEmpty(quantitaAsString) == true ? BigDecimal.ZERO : new BigDecimal(quantitaAsString);

            if (forfait) {
                costo = quantita;
            } else {

                Integer durataMesi = StringUtils.isBlank(durataMesiAsString) == true ? null : Integer.valueOf(durataMesiAsString);
                Date dal = StringUtils.isBlank(dataAvvio) == true ? StringConversion.itStringToDate(dataAvvioProposta) : StringConversion.itStringToDate(dataAvvio);
                Date al = StringUtils.isBlank(dataFine) == true ? null : StringConversion.itStringToDate(dataFine);
                CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
                costo = calcolaCostoInterventoService.calcolaBdgPrevEur(durataMesi, dal, al,quantita,tariffa, tipologiaIntervento);
            }
        }

        JsonImporto imp  = new JsonImporto();
        imp.setSuccess(true);
        imp.setImporto(costo);

        return imp;
    }

    private Object salvaIntervento(HttpServletRequest request, EntityManager entityManager, AnagrafeSocDao anagrafeDao, PaiInterventoDao paiInterventoDao, String cntTipint, Utenti connectedUser, String codPai, String codTipint, String tipOld) throws Exception {

        AssociazioneDao associazioneDao = new AssociazioneDao(entityManager);
        Associazione comuneDiTrieste = associazioneDao.findById(1);// comune di trieste

        Map<String, String> parameters = Maps.newHashMap(getParameters());
        Preconditions.checkArgument(!(!Strings.isNullOrEmpty(getParameter("esito"))
                && !Strings.isNullOrEmpty(getParameter("dataChiusura"))), "Per chiudere un intervento è necessario cliccare sul pulsante Chiudi forzatamente intervento");
        parameters.remove("noteChiusura");
        parameters.remove("esito");
        parameters.remove("dataChiusura");
        parameters.remove("noteSospensioneRead");
        parameters.remove("dataSospensioneRead");

        JSONInterventoList json = new JSONInterventoList();
        List<ImpegnoSpesaBean> impegni = new ArrayList<ImpegnoSpesaBean>();
        List<CivilmenteObbligatoBean> importiCivilmenteObbligati = new ArrayList<CivilmenteObbligatoBean>();
        
        if (!Strings.isNullOrEmpty(getParameter("impegniStore"))) {
            impegni = getParameter("impegniStore", new TypeToken<List<ImpegnoSpesaBean>>() {
            }.getType());
        }
        
        if (!Strings.isNullOrEmpty(getParameter("importoCivilmentObbligato"))) {
            importiCivilmenteObbligati = getParameter("importoCivilmentObbligato", new TypeToken<List<CivilmenteObbligatoBean>>() {
            }.getType());
        }
        
        entityManager.getTransaction().begin();
        
        if (Boolean.parseBoolean(parameters.get("insertNewDelegato"))) {
            Map<String, String> params = getParameter("newDelegatoData", JsonBuilder.MAP_OF_STRINGS);
            AnagrafeSocSerializer anagrafeSocSerializer = new AnagrafeSocSerializer(entityManager);
            AnagrafeSoc newDelegato = anagrafeSocSerializer.requestParamsToAnagrafeSoc(params);
            Validate.isTrue(anagrafeDao.findByCodFisc(newDelegato.getCodFisc()) == null, "codice fiscale delegato gia' presente: " + newDelegato.getCodFisc());
            entityManager.persist(newDelegato);
            parameters.put(InterventoSerializer.PARAM_COD_BENEFICIARIO, newDelegato.getCodAna().toString());
        }
        
        InterventoSerializer serializer = new InterventoSerializer(entityManager);
        
        PaiIntervento paiIntervento = serializer.serializeIntervento(parameters);

        PaiEventoDao paiEventoDao = new PaiEventoDao(entityManager);
        PaiDao paiDao = new PaiDao(entityManager);
        Pai pai = paiDao.findPai(paiIntervento.getPaiInterventoPK().getCodPai());
        paiIntervento.setPai(pai);
        paiIntervento.setTipologiaIntervento(new TipologiaInterventoDao(entityManager).findByCodTipint(codTipint));
        if (Strings.isNullOrEmpty(cntTipint) || !codTipint.equals(tipOld))  {
            entityManager.persist(paiIntervento);
            if (Objects.equal(pai.getFlgStatoPai(), Pai.STATO_APERTO)) {
                pai.setFlgStatoPai(Pai.STATO_APERTO);
            } else {
                pai.setFlgStatoPai(Pai.STATO_RIFIUTATO);
            }
            CartellaSociale cartella = pai.getCartellaSociale();
            PaiEvento evento = Pratica.serializePaiEvento(pai, paiIntervento, cartella, PaiEvento.PAI_APERTURA_INTERVENTO, connectedUser);
            paiEventoDao.insert(evento);
        } else {
            CartellaSociale cartella = paiIntervento.getPai().getCartellaSociale();
            PaiEvento evento = Pratica.serializePaiEvento(pai, paiIntervento, cartella, PaiEvento.PAI_UPDATE_INTERVENTO, connectedUser);
            paiEventoDao.insert(evento);
        }

        BigDecimal aCarico = BigDecimal.ZERO;
        boolean budgetOk = true;
        boolean budgetAssegnatoAutomaticamente = false;
        //posso modificare il budget solo se l'intervento non è in stato aperto cioè è già stato mandato dal coordinatore e approvato.
        if(paiIntervento.getStatoInt()=='A' && paiIntervento.getStatoAttuale().equals(PaiIntervento.APERTO)==false){
            for (ImpegnoSpesaBean impegno : impegni) {
                if (impegno.getaCarico()!=null && !impegno.getaCarico().isEmpty()) {
                    BigDecimal ac = new BigDecimal(impegno.getaCarico().replace(',', '.'));
                    aCarico = aCarico.add(ac);
                    if (paiIntervento.getTipologiaIntervento() == null) {
                        paiIntervento.setTipologiaIntervento(entityManager.find(TipologiaIntervento.class, paiIntervento.getPaiInterventoPK().getCodTipint()));
                    }
                    boolean isUnderBudget = manageImpegno(entityManager, paiIntervento, impegno);
                    if(isUnderBudget==false){
                        budgetOk=false;
                    }
                }
            }
        }
        //se per qualche motivo lato client la spesa non è stata assegnata a nessun budget la assegno io lato server al primo budget disponibile
        if(aCarico.compareTo(BigDecimal.ZERO)==0 && paiIntervento.getQuantita().compareTo(BigDecimal.ZERO)!=0 && (paiIntervento.getStatoInt()=='A' || paiIntervento.getStatoInt()=='R')){
            //resettiamo i budget
            resetBudgets(paiIntervento, entityManager);
            PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
            List<BudgetTipIntervento> budgets = new BudgetTipoInterventoDao(entityManager).findByCodTipint(getParameter("tipo"));
            short year = (short) Calendar.getInstance().get(Calendar.YEAR);
            BigDecimal spesa = new BigDecimal(getParameter("costoTotale"));
            BigDecimal importoStandard = getImportoStandard(paiIntervento, entityManager);
            if(importoStandard.equals(BigDecimal.valueOf(0L))){

                throw new RuntimeException("l' importoStandard, preso da intervento/ tariffa/ costo o da intervento/ tipologia intervento importo standard di costo vale 0." );
            }

            for(BudgetTipIntervento budget : budgets ){
                if(budget.getAnnoErogazione()==year){
                    PaiInterventoMese proposta = null;
                    proposta = paiInterventoMeseDao.findProposta( paiIntervento.getPaiInterventoPK(),  budget.getBudgetTipInterventoPK(), paiIntervento.getPai().getIdParamFascia());
                    if(proposta!=null){
                        proposta.setBdgPrevEur(spesa);
                        proposta.setBdgPrevQta(spesa.divide(importoStandard, MathContext.DECIMAL32));
                        paiInterventoMeseDao.update(proposta);
                    }
                    else {
                        paiInterventoMeseDao.insertProp(
                                paiIntervento.getPaiInterventoPK(),
                                budget.getBudgetTipInterventoPK(),
                                spesa,
                                spesa.divide(importoStandard, MathContext.DECIMAL32),
                                paiIntervento.getPai().getIdParamFascia());
                    }
                    budgetAssegnatoAutomaticamente= true;
                    break;
                }
            }
            aCarico=spesa;
        }
        //solo se l'intervento è aperto o rimandato
        if(paiIntervento.getStatoInt()=='A'|| paiIntervento.getStatoInt()=='R'){
            paiIntervento.setCostoPrev(aCarico);
        }


        PaiInterventoCivObbDao paiIntCivObbDao = new PaiInterventoCivObbDao(entityManager);
        for (CivilmenteObbligatoBean importo : importiCivilmenteObbligati) {
            manageCivilmenteObbligato(importo, paiIntervento, paiIntCivObbDao);
        }
        //Aggiungo i dati specifici
        List<MapDatiSpecificiIntervento> datiSpecifici = serializer.serializeMapDatiSpecifici(paiIntervento, parameters);
        paiIntervento.setMapDatiSpecificiInterventoList(datiSpecifici);
        paiIntervento.setIbanDelegato(parameters.get("IBAN").toUpperCase(Locale.ITALIAN));

        // facciamo un controllo
        if(paiIntervento.getDtFine()!=null && !(paiIntervento.getDtAvvio().equals(paiIntervento.getDtFine()))){
            Preconditions.checkArgument((paiIntervento.getDtAvvio().before(paiIntervento.getDtFine())),"Attenzione! La data di fine non può essere antecedente alla data di avvio!");
        }

        if(paiIntervento.getAssociazione() == null){
            paiIntervento.setAssociazione(comuneDiTrieste);
        }

        entityManager.merge(paiIntervento);

        //se questo intervento non fa il giro oppure è uno spezxzatino... allora và in csr prima.
        if((new TipologiaInterventoDao(entityManager).findByCodTipint(codTipint).getFlgPagam()=='N' && new TipologiaInterventoDao(entityManager).findByCodTipint(codTipint).getFlgFatt()=='N') || (new TipologiaInterventoDao(entityManager).findByCodTipint(codTipint).getFlgRinnovo()==TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_DETERMINA)){
            CartellaSocialeWsClient.newInstance().withEntityManager(entityManager).loadConfigFromDatabase().withPaiIntervento(paiIntervento).sincronizzaIntervento();
        }
        entityManager.getTransaction().commit();

        if(tipOld!=null && !tipOld.equals(codTipint)){
            PaiEventoDao pedao = new PaiEventoDao(entityManager);
            String contOld = Strings.emptyToNull(getParameter("cntTipIntHidden"));
            PaiIntervento pi = paiInterventoDao.findByKey(Integer.parseInt(codPai), tipOld, contOld);
            Preconditions.checkNotNull(pi,"Attenzione l'intervento che si voleva cancellare non esiste. Il nuovo intervento è stato comunque salvato. Riaggiornare la pagina");
            Validate.isTrue(pi.getInterventiFigli().isEmpty(),"Attenzione non posso cancellare questo intervento in quanto vi sono interventi associati ad esso");
            Validate.isTrue(pi.getStatoInt()=='A', "Attenzione! L'intervento è in esecutività o è già chiuso quindi non può essere modificato!");
            Validate.isTrue(pi.getDeterminaAssociata()==null ,"Attenzione,  è già stata prodotta una determina di esecutività per questo intervento quindi non può essere modificato!");
            boolean isInListe=false;
            for(PaiEvento e : pedao.findByPaiIntervento(pi)){
                if(e.getDesEvento().equals(PaiEvento.PAI_APPROVA_INTERVENTO)){
                    isInListe=true;
                    break;
                }

            }

            TaskDao tdao = new TaskDao(entityManager);
            List<UniqueTasklist> listaTask = tdao.findAllActiveTaskByPaiIntervento(pi);
            for(UniqueTasklist task : listaTask){
                if(!task.getTaskid().equals(UniqueTasklist.STANDALONE_TASK_ID)){

                    WelfareGoIntalioManager welfareGoIntalioManager = new WelfareGoIntalioManager();
                    welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_TOKEN, WelfaregoUtils.getConfig(IntalioManager.AUTHENTICATION_TOKEN));
                    welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_URL, WelfaregoUtils.getConfig("PROPERTY_AUTHENTICATION_URL"));
                    welfareGoIntalioManager.setProperty(IntalioManager.TMS_URL, WelfaregoUtils.getConfig("PROPERTY_TMS_URL"));
                    welfareGoIntalioManager.setProperty(IntalioManager.COMPLETE_TASK_URL, WelfaregoUtils.getConfig("PROPERTY_COMPLETE_TASK_URL"));

                    welfareGoIntalioManager.setProperty(IntalioManager.INTALIO_URL,WelfaregoUtils.getConfig("PROPERTY_INTALIO_URL") );
                    welfareGoIntalioManager.setProperty(WelfareGoIntalioManager.START_PROCESS_URL, WelfaregoUtils.getConfig("PROPERTY_START_PROCESS_URL"));
                	
                    TInstanceInfo instanceInfo =  welfareGoIntalioManager.getInstanceByTaskId(task.getTaskid());
                    welfareGoIntalioManager.getInstanceManagementService().terminate(Long.valueOf(instanceInfo.getIid()).longValue());
                }
            }
            // TODO cancello l'intervento in csr

            PaiInterventoDao dao = new PaiInterventoDao(entityManager);
            try{
                dao.deleteIntervento(pi);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            paiInterventoDao.deleteIntervento(pi);

            PaiEvento evento = new PaiEvento();
            evento.setCodPai(pi.getPai());
            evento.setCodUte(connectedUser);
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
            evento.setDesEvento("Cancellazione intervento " + pi.getTipologiaIntervento().getDesTipint());
            evento.setPaiDox("x");
            evento.setTsEvePai(new Date());
            pedao.insert(evento);
            //se l'intervento vecchio era in liste anche il nuovo lo dovrà essere quindi gli facciamo saltare tutti i passaggi .
            if(isInListe){
                PaiEvento e = Pratica.serializePaiEvento(PaiEvento.PAI_APPROVA_INTERVENTO, paiIntervento, connectedUser);
                e.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
                paiIntervento.setDataRichiestaApprovazione(new Date());
                pedao.insert(e);
            }

        }

        String message = "Operazione eseguita correttamente";
        if (!budgetOk && budgetAssegnatoAutomaticamente==false) {
            message += " (Attenzione: Budget negativo: la proposta di spesa è stata comunque segnata,il coordinatore può in caso rifutarla)";
        }
        if(budgetAssegnatoAutomaticamente){
            message += " (Dato che non è stato indicato un budget specifico su cui assegnare la spesa per l'intervento la si è assegnata automaticamente al primo budget disponibile.)";
        }
        json.setMessage(message);

        // segue verifica degi interventi figli
        //
        String[] familiari = request.getParameterValues("familiari");
        HashMap<String, String> listaFamigliariSelezionati = new HashMap<String, String>();
        if (familiari != null)
        {
            log("Abbiamo trovato dei familiari selezionati");
            for(String familiare : familiari)
            {
                String[] familiareParam = familiare.split(";");
                String codFamiliare = familiareParam[0];
                String costoFamiliare = "0";
                if (familiareParam.length > 1)
                {
                    costoFamiliare = familiareParam[1];
                }
                listaFamigliariSelezionati.put(codFamiliare, costoFamiliare);
            }
        }

        List<InterventiAssociati> interventiFigli = paiIntervento.getInterventiFigli();
        if (interventiFigli != null)
        {
            log("ciclo sui famigliari già presenti");
            for (InterventiAssociati interventoFiglio : interventiFigli)
            {
                String codAnag = interventoFiglio.getInterventoFiglio().getPai().getAnagrafeSoc().getCodAna().toString();
                if (listaFamigliariSelezionati.containsKey(codAnag))
                {
                    // Se il famigliare selezionato è già presente lo devo semplicemente aggiornare.
                    log("Aggiornamento dell'intervento: codPai:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodPai()
                            + ", codTipInt:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodTipint()
                            + ", cntTipInt:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCntTipint());
                    interventoFiglio.getInterventoFiglio().setDtAvvio(paiIntervento.getDtAvvio());
                    interventoFiglio.getInterventoFiglio().setDataAvvioProposta(paiIntervento.getDataAvvioProposta());
                    interventoFiglio.getInterventoFiglio().setDtFine(paiIntervento.getDtFine());
                    interventoFiglio.getInterventoFiglio().setTariffa(paiIntervento.getTariffa());
                    interventoFiglio.getInterventoFiglio().setQuantita(new BigDecimal(listaFamigliariSelezionati.get(codAnag)));
                    paiInterventoDao.update(interventoFiglio);
                }
                else
                {
                    // Se il famigliare non è stato selezionat devo cancellarlo.
                    log("Cancellazione intervento: codPai:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodPai()
                            + ", codTipInt:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodTipint()
                            + ", cntTipInt:" + interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCntTipint());

                    PaiIntervento pi = paiInterventoDao.findByKey(interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodPai(), interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCodTipint(), interventoFiglio.getInterventoFiglio().getPaiInterventoPK().getCntTipint());
                    PaiEventoDao pedao = new PaiEventoDao(entityManager);
                    Validate.notNull(pi, "Attenzione l'intervento designato per la cancellazione non esiste... aggiornare la pagina e verificare che non sia stato già cancellato!");
                    Validate.isTrue(pi.getStatoInt()=='A', "Attenzione! L'intervento è in esecutività o è già chiuso quindi non può essere cancellato!");
                    Validate.isTrue(pi.getDeterminaAssociata()==null ,"Attenzione,  è già stata prodotta una determina di esecutività per questo intervento  quindi non può essere cancellato!");

                    //vedo se l'intervento non è stato già determinato

                    try
                    {
                        this.deleteIntervento(pi, entityManager, connectedUser, pedao);
                    }
                    catch(Exception e){
                        throw new IllegalStateException("Errore nella cancellazione di uno degli interventi associati");
                    }
                }

                //alla fine elilmino dalla lista i famigliari selezionati
                listaFamigliariSelezionati.remove(codAnag);
            }
        }
        // alla fine aggiungo i nuovi vamigliari selezionati, non presenti precedentemente
        Iterator<String> nuoviFamigliari = listaFamigliariSelezionati.keySet().iterator();
        while (nuoviFamigliari.hasNext())
        {
            String codFamiliare = nuoviFamigliari.next();
            log("Selezionato nuovo famigliare, creazione nuovo intervento, codFamiliare: " + codFamiliare);
            Pai paiFamiliare = returnPaiFamiliare(codFamiliare, entityManager,pai);

            Integer codPaiActual= paiIntervento.getPaiInterventoPK().getCodPai();
            String codTipIntActual = paiIntervento.getPaiInterventoPK().getCodTipint();
            String cntTipintActual = paiIntervento.getPaiInterventoPK().getCntTipint().toString();
            PaiIntervento interventoFiglio = returnPaiInterventoFiglia(paiFamiliare,paiIntervento,entityManager, new BigDecimal(listaFamigliariSelezionati.get(codFamiliare)));
            paiIntervento = paiInterventoDao.findByKey(codPaiActual, codTipIntActual, cntTipintActual);


            InterventiAssociati as = new InterventiAssociati();
            as.setInterventoPadre(paiIntervento);
            as.setInterventoFiglio(interventoFiglio);
            as.setTipoLegame(InterventiAssociati.RELAZIONE_PARENTALE);

            new InterventiAssociatiDao(entityManager).insert(as);
        }

        //se l'intervento prevede il dato disabilità allineaiamo questo dato specifico con quello del pai.
        if(paiIntervento.getDsDisabilita()!=null){
            pai.setIdParamCertificatoL104(new ParametriIndataDao(entityManager).findByIdParamIndata(Integer.valueOf(paiIntervento.getDsDisabilita())));
            paiDao.update(pai);

        }
        json.setSuccess(true);
        return json;
    }


    private boolean manageImpegno(EntityManager em, PaiIntervento intervento, ImpegnoSpesaBean impegno) throws Exception {

        BigDecimal spesa = new BigDecimal(impegno.getaCarico().replace(',', '.'));
        BudgetTipoInterventoUotDao budgetTipoInterventoDao = new BudgetTipoInterventoUotDao(em);
        BudgetTipoInterventoDao budgetTipoInterventoDao2 = new BudgetTipoInterventoDao(em);
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(em);

        int codUot = intervento.getPai().getCodUteAs().getIdParamUot().getIdParamIndata();
        Short anno = Short.valueOf(impegno.getAnno());
        String codiceUot = impegno.getUot();
        BigDecimal importoDisponibile = null;
        BigDecimal importoStandard = getImportoStandard(intervento, em);

        //se il codice
        if(codiceUot.equals("N/A")){
            BudgetTipIntervento budget = budgetTipoInterventoDao2.findByKey(intervento.getPaiInterventoPK().getCodTipint(), impegno.getAnno(), impegno.getImpegno());
            if (budget == null) {
                getLogger().warn("Attenzione! Non è associato un budget per l'intervento ");
                return false;
            }

            importoDisponibile= new PaiInterventoMeseDao(em).calculateBdgtDisp(budget);
            paiInterventoMeseDao.insertProp(
                    intervento.getPaiInterventoPK(),
                    budget.getBudgetTipInterventoPK(),
                    spesa,
                    spesa.divide(importoStandard, MathContext.DECIMAL32),
                    intervento.getPai().getIdParamFascia());

        }
        else {
            BudgetTipInterventoUot budget = budgetTipoInterventoDao.findByKey(intervento.getPaiInterventoPK().getCodTipint(), anno, impegno.getImpegno(), codUot);
            if (budget == null) {
                getLogger().warn("Attenzione! Alla UOT \"" + intervento.getPai().getCodUteAs().getIdParamUot().getDesParam() + "\" non è associato un budget per l'intervento ");
                return false;
            }
            //FIXME attenzione se metti due interventi mese sullo stesso budget ma per uot diverse il sistema sovrascrive...ma questo non dovrebbe verificarsi dato che non ci sono i PAI

            importoDisponibile = new PaiInterventoMeseDao(em).calculateBdgtDisp(budget.getBudgetTipIntervento());

            paiInterventoMeseDao.insertProp(
                    intervento.getPaiInterventoPK(),
                    budget.getBudgetTipIntervento().getBudgetTipInterventoPK(),
                    spesa,
                    spesa.divide(importoStandard, MathContext.DECIMAL32),
                    intervento.getPai().getIdParamFascia());
        }
        if (importoDisponibile.compareTo(spesa) >= 0) {
            getLogger().debug("OK la spesa copre");
            return true;
        } else {
            getLogger().debug("attenzione, budget negativo torno questo  ({})", importoDisponibile.subtract(spesa));
            return false;
        }
    }

    private void manageCivilmenteObbligato(CivilmenteObbligatoBean importo, PaiIntervento intervento, PaiInterventoCivObbDao paiIntCivObbDao) throws Exception {
        int codAna = importo.getCodAnag();
        int cntTipInt = intervento.getPaiInterventoPK().getCntTipint();
        int codPai = intervento.getPaiInterventoPK().getCodPai();
        String codTipInt = importo.getCodTipInt();
        PaiInterventoCivObb civObb = paiIntCivObbDao.findByKey(codPai, codTipInt, cntTipInt, codAna);
        boolean isNew = civObb == null;
        civObb = PaiInterventoCivObbSerializer.serialize(civObb, intervento, importo);
        if (isNew) {
            paiIntCivObbDao.insert(civObb);
        }
    }

    private CronologiaInterventoBean serializeCronologia(PaiEvento evento) {
        CronologiaInterventoBean bean = new CronologiaInterventoBean();
        bean.setCognomeOperatore(evento.getCodUte().getCognome());
        bean.setNomeOperatore(evento.getCodUte().getNome());
        bean.setData(StringConversion.timestampToString(evento.getTsEvePai()));
        bean.setEvento(evento.getDesEvento());
        return bean;
    }

    /**
     * Ritorna o crea un Pai per il familiare
     * @param familiare
     * @return
     * @throws Exception
     */
    private Pai returnPaiFamiliare(String familiare,EntityManager em,Pai paiGenitore) throws Exception{
        PaiDao pdao = new PaiDao(em);
        //cerchiamo se vi è un pai aperto...
        Pai paiFamiliare = pdao.findLastPai(Integer.valueOf(familiare));
        if(paiFamiliare!=null){
            return paiFamiliare;
        }
        CartellaDao cdao = new CartellaDao(em);
        AnagrafeSocDao adao= new AnagrafeSocDao(em);
        AnagrafeSoc anaFamiliare = adao.findByCodAna(familiare);
        CartellaSociale cs = cdao.findByCodAna(Integer.valueOf(familiare));
        if (cs==null){
            cs = new CartellaSociale(anaFamiliare.getCodAna(), new Date());
            cdao.insert(cs);
        }
        paiFamiliare = new Pai();

        paiFamiliare.setCartellaSociale(cs);
        paiFamiliare.setDtApePai(new Date());
        paiFamiliare.setFlgStatoPai('A');
        paiFamiliare.setIdParamFascia(paiGenitore.getIdParamFascia());
        paiFamiliare.setNumNuc(paiGenitore.getNumNuc());
        paiFamiliare.setCodUteAs(paiGenitore.getCodUteAs());
        paiFamiliare.setIdParamUot(paiGenitore.getIdParamUot());
        pdao.insert(paiFamiliare);
        PaiEvento evento = new PaiEvento();
        evento.setCodPai(paiFamiliare);
        evento.setDesEvento(PaiEvento.PAI_APERTURA);
        evento.setTsEvePai(new Date());
        evento.setCodUte(paiGenitore.getCodUteAs());
        evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
        evento.setPaiDox("x");
        evento.setBreDox("x");

        new PaiEventoDao(em).insert(evento);


        return paiFamiliare;

    }
    /**
     * Metodo che dato un intervento padre genera un intervento figlio dello stesso tipo.
     * @param paiFamiliare
     * @param paiIntervento
     * @param entityManager
     * @param costo costo dello specifico familiare.
     * @return
     * @throws Exception
     */
    private PaiIntervento returnPaiInterventoFiglia(Pai paiFamiliare,
                                                    PaiIntervento paiIntervento, EntityManager entityManager, BigDecimal costo) throws Exception {

        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        Integer codPai = paiIntervento.getPaiInterventoPK().getCodPai();
        String codTipInt = paiIntervento.getPaiInterventoPK().getCodTipint();
        Integer cntTipInt = paiIntervento.getPaiInterventoPK().getCntTipint();


        PaiIntervento figlio = paiInterventoDao.clonaInterventoPerFamiliare(paiIntervento, paiFamiliare);
        paiIntervento = paiInterventoDao.findByKey(codPai, codTipInt, cntTipInt);
        figlio.setDtAvvio(paiIntervento.getDtAvvio());
        figlio.setDtFine(paiIntervento.getDtFine());
        figlio.setDurMesi(paiIntervento.getDurMesi());
        figlio.setDurSettimane(paiIntervento.getDurSettimane());
        figlio.setTipologiaIntervento(new TipologiaInterventoDao(entityManager).findByCodTipint(codTipInt));
        figlio.setDsCodAnaBenef(paiIntervento.getDsCodAnaBenef().getCodAna().equals(paiIntervento.getPai().getAnagrafeSoc().getCodAna())? paiFamiliare.getAnagrafeSoc(): paiIntervento.getDsCodAnaBenef());
        figlio.setDsCodAnaRich(paiIntervento.getDsCodAnaRich());

        figlio.setDataAvvioProposta(paiIntervento.getDataAvvioProposta());
        figlio.setMapDatiSpecificiInterventoList(null);




        if(paiIntervento.getTipologiaIntervento().getFlgFineDurata()== TipologiaIntervento.FLG_FINE_DURATA_F && paiIntervento.getTipologiaIntervento().getIdParamUniMis().getDesParam().contains("euro")){
            figlio.setQuantita(costo);
        }
        else {
            figlio.setQuantita(paiIntervento.getQuantita());
        }
        figlio.setCostoPrev(calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(figlio)));
        paiInterventoDao.insert(figlio);
        List<MapDatiSpecificiIntervento> datiSpecificiPrec = paiIntervento.getMapDatiSpecificiInterventoList();
        MapDatiSpecificiInterventoDao mdao = new MapDatiSpecificiInterventoDao(entityManager);
        for(MapDatiSpecificiIntervento mp : datiSpecificiPrec){
            MapDatiSpecificiIntervento nuovo = new MapDatiSpecificiIntervento(figlio.getPaiInterventoPK().getCodPai(), figlio.getPaiInterventoPK().getCodTipint(), figlio.getPaiInterventoPK().getCntTipint(),mp.getCodCampo());
            nuovo.setCodValCampo(mp.getCodValCampo());
            nuovo.setValCampo(mp.getValCampo());
            mdao.insert(nuovo);

        }
        List<PaiInterventoMese> pims = paiIntervento.getPaiInterventoMeseList();
        PaiInterventoMeseDao pimdao = new PaiInterventoMeseDao(entityManager);

        for(PaiInterventoMese pim : pims){
            PaiInterventoMese pimNuovo = new PaiInterventoMese(paiFamiliare.getCodPai(), figlio.getPaiInterventoPK().getCodTipint(),figlio.getPaiInterventoPK().getCntTipint(), pim.getPaiInterventoMesePK().getAnnoEff(), pim.getPaiInterventoMesePK().getMeseEff(), pim.getPaiInterventoMesePK().getAnno(), pim.getPaiInterventoMesePK().getCodImp());
            pimNuovo.setBdgPrevEur(figlio.getCostoPrev());
            pimNuovo.setBdgPrevQta(pim.getBdgPrevQta());
            pimNuovo.setFlgProp(PaiInterventoMese.FLG_PROPOSTA_S);
            pimNuovo.setIdParamFascia(figlio.getPai().getIdParamFascia());
            pimdao.insert(pimNuovo);

        }
        PaiEventoDao pemDao= new PaiEventoDao(entityManager);
        PaiEvento evento = new PaiEvento();
        evento.setCodPai(paiFamiliare);
        evento.setDesEvento(PaiEvento.PAI_APERTURA_INTERVENTO);
        evento.setTsEvePai(new Date());
        evento.setCodUte(figlio.getPai().getCodUteAs());
        evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
        evento.setPaiDox("x");
        evento.setBreDox("x");
        pemDao.insert(evento);
        return figlio;
    }
    /**
     * Ritorna il costo standard di un intervento.
     * @param i
     * @param e
     * @return
     */
    private BigDecimal getImportoStandard(PaiIntervento i,EntityManager e){
        BigDecimal importoStandard = null;
        //se ha una struttura
        if(i.getTariffa()!=null){
            importoStandard = i.getTariffa().getCosto();
        }
        else {
            importoStandard = i.getTipologiaIntervento().getImpStdCosto();
        }
        return importoStandard;
    }

    /**
     *
     * @param pi
     * @throws Exception
     */
    private void resetBudgets(PaiIntervento pi, EntityManager em) throws Exception{
        PaiInterventoMeseDao pdao = new PaiInterventoMeseDao(em);
        List<PaiInterventoMese> pims = pdao.findForPaiInt(pi.getPaiInterventoPK().getCodPai(),pi.getPaiInterventoPK().getCodTipint(), pi.getPaiInterventoPK().getCntTipint());
        for(PaiInterventoMese pim : pims){
            if(pim.getFlgProp()==PaiInterventoMese.FLG_PROPOSTA_S){
                pim.setBdgPrevEur(BigDecimal.ZERO);
                pim.setBdgPrevQta(BigDecimal.ZERO);
                pdao.update(pim);
            }
        }

    }

    private void deleteIntervento(PaiIntervento pi, EntityManager entityManager, Utenti connectedUser, PaiEventoDao pedao) throws Exception
    {
        TaskDao tdao = new TaskDao(entityManager);
        List<UniqueTasklist> listaTask = tdao.findAllActiveTaskByPaiIntervento(pi);
        for (UniqueTasklist task : listaTask)
        {
            if (!task.getTaskid().equals(UniqueTasklist.STANDALONE_TASK_ID))
            {
                WelfareGoIntalioManager welfareGoIntalioManager = new WelfareGoIntalioManager();
                welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_TOKEN, WelfaregoUtils.getConfig(IntalioManager.AUTHENTICATION_TOKEN));
                welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_URL, WelfaregoUtils.getConfig("PROPERTY_AUTHENTICATION_URL"));
                welfareGoIntalioManager.setProperty(IntalioManager.TMS_URL, WelfaregoUtils.getConfig("PROPERTY_TMS_URL"));
                welfareGoIntalioManager.setProperty(IntalioManager.COMPLETE_TASK_URL, WelfaregoUtils.getConfig("PROPERTY_COMPLETE_TASK_URL"));

                welfareGoIntalioManager.setProperty(IntalioManager.INTALIO_URL,WelfaregoUtils.getConfig("PROPERTY_INTALIO_URL") );
                welfareGoIntalioManager.setProperty(WelfareGoIntalioManager.START_PROCESS_URL, WelfaregoUtils.getConfig("PROPERTY_START_PROCESS_URL"));
            	
                TInstanceInfo instanceInfo = welfareGoIntalioManager.getInstanceByTaskId(task.getTaskid());
                welfareGoIntalioManager.getInstanceManagementService().terminate(Long.valueOf(instanceInfo.getIid()).longValue());
            }
        }
        // TODO cancello l'intervento in csr

        PaiInterventoDao dao = new PaiInterventoDao(entityManager);
        try
        {
            dao.deleteIntervento(pi);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        PaiEvento evento = new PaiEvento();
        evento.setCodPai(pi.getPai());
        evento.setCodUte(connectedUser);
        evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
        evento.setDesEvento("Cancellazione intervento " + pi.getTipologiaIntervento().getDesTipint());
        evento.setPaiDox("x");
        evento.setTsEvePai(new Date());
        pedao.insert(evento);
    }

}
