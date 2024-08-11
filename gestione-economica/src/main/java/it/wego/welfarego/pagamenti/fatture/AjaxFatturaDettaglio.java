package it.wego.welfarego.pagamenti.fatture;

import com.google.common.collect.Maps;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.PaiCdg;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller per le chiamate AJAX riguardanti la gestione della visualizzazione
 * e del salvataggio delle modifiche delle fatture già esistenti.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxFatturaDettaglio extends AbstractAjaxController {

    /**
     * Carica dalla base di dati i dati generali di una fattura.
     *
     * @param request Richiesta del browser con l'id della fattura richiesta.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioDatiFatturazioni")
    public void dettaglioDatiFatturazioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        //i valori null non vengono trasmessi (inseriti comunque per chiarezza e per facilitare eventuali modifiche future)
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt(request.getParameter("id")));
        Fattura fattura = (Fattura) q.getSingleResult();
        Map<String, Object> jso = Maps.newHashMap();
        jso.put("id", fattura.getIdFatt());
        //TODO da ricalcolare???
        jso.put("contributo", fattura.getContributo() == null ? BigDecimal.ZERO : fattura.getContributo());
        jso.put("iva", fattura.getIdParamIva().getIdParamIndata());
        jso.put("importo_iva", fattura.getImpIva());
        jso.put("bollo", fattura.getBollo());
        jso.put("causale", fattura.getCausale());
        jso.put("note", fattura.getNote());
        jso.put("da_pagare", fattura.getImportoTotale());
        jso.put("scadenza", ISODateTimeFormat.dateTime().print(fattura.getScadenza().getTime()));
        jso.put("numeroFattura", fattura.getNumFatt());
        //        jso.put("modalita_pagamento", fattura.getIdParamPagam().getIdParamIndata());
        jso.put("codice_fiscale", fattura.getCodFisc());
        JsonBuilder.newInstance().withWriter(response.getWriter()).withData(jso).buildStoreResponse();
    }//dettaglioDatiFatturazioni

    /**
     * Carica dalla base di dati le voci della fattura.
     *
     * @param request Richiesta del browser con l'id della fattura richiesta.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioVociFattura")
    public void dettaglioVociFattura(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt(request.getParameter("id")));
        Fattura fattura = (Fattura) q.getSingleResult();

        for (FatturaDettaglio dettaglio : fattura.getFatturaDettaglioList()) {
            Map<String, Object> jso = Maps.newHashMap();
            jso.put("id", dettaglio.getIdFattDettaglio().toString());
            jso.put("id_fattura", fattura.getIdFatt());
            jso.put("tipo_servizio", dettaglio.getCodTipint().getDesTipint());
            jso.put("unita_di_misura", dettaglio.getIdParamUnitaMisura().getDesParam());
            ParametriIndata pi = null ; 
            if(!dettaglio.getPaiInterventoMeseList().isEmpty()){
               	pi = dettaglio.getPaiInterventoMeseList().get(0).getIdParamFascia();
            }
           else{
        	   pi =fattura.getIdParamFascia();
           }
            
            BigDecimal percentualeRiduzione = null;
            if (pi != null) {
                percentualeRiduzione = pi.getDecimalParam();
                jso.put("descrizioneRiduzione", pi.getDesParam());
            } else {
                percentualeRiduzione = BigDecimal.ZERO;
                jso.put("descrizioneRiduzione", "-");
            }

            jso.put("quantita", dettaglio.getQtInputata());

            BigDecimal costoStandard = getImportoStandard(dettaglio.getPaiIntervento(), getEntityManager());
            percentualeRiduzione = (new BigDecimal("100").subtract(percentualeRiduzione)).divide(new BigDecimal("100"));
            jso.put("importo_unitario", costoStandard.multiply(percentualeRiduzione).setScale(2, RoundingMode.DOWN));
            
            jso.put("importo_dovuto", dettaglio.getImportoSenzaIva());
         
            jso.put("importoSenzaIva", dettaglio.getImportoSenzaIva().toString());
            jso.put("importoConIva", dettaglio.getImportoConIva().toString());
            jso.put("ivaTotale", dettaglio.getIvaTotale().toString());
            jso.put("aliquotaIva", dettaglio.getIdParamIva().getDecimalPercentageParamAsDecimal().toString());
            jso.put("totaleVariazioniConIva", dettaglio.getTotaleVariazioniConIva().toString());

            jso.put("riduzione", dettaglio.getRiduzione());
            jso.put("aumento", dettaglio.getAumento());
            jso.put("variazione_straordinaria", dettaglio.getVarStraord());
            jso.put("mese", dettaglio.getMeseEff());
            jsonBuilder.addRecord(jso);
        }
        jsonBuilder.buildStoreResponse();
    }

    /**
     * Carica dalla base di dati le voci della fattura.
     *
     * @param request Richiesta del browser con l'id della fattura richiesta.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioMesiPrecedentiFatturazioni")
    public void dettaglioMesiPrecedentiFatturazioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt(request.getParameter("id")));
        Fattura fattura = (Fattura) q.getSingleResult();
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray();
        ArrayList<Integer> mesiPrecedentiAssegnati = new ArrayList<Integer>();
        mesiPrecedentiAssegnati.add(fattura.getIdFatt());
        for (Fattura mesePrecedente : fattura.getFatturaList()) {
            jso = new JSONObject();
            jso.put("id", mesePrecedente.getIdFatt());
            jso.put("id_fattura", fattura.getIdFatt());
            jso.put("mese", mesePrecedente.getMeseRif());
            jso.put("importo", mesePrecedente.getImportoTotale());
            jso.put("causale", mesePrecedente.getCausale());
            jso.put("inserimento", "Si");
            jsa.put(jso);
            mesiPrecedentiAssegnati.add(mesePrecedente.getIdFatt());
        }
        //nel caso che la fattura sia ancora modificabile aggiungo ancora le fatture
        //dei mesi precedenti, che non sono ancora assegnate.
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(Fattura.class);
        Predicate p = cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "de");
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("paiInterventoPK").get("codPai"), fattura.getPaiIntervento().getPai().getCodPai()));
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("paiInterventoPK").get("codTipint"), fattura.getPaiIntervento().getPaiInterventoPK().getCodTipint()));
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("paiInterventoPK").get("cntTipint"), fattura.getPaiIntervento().getPaiInterventoPK().getCntTipint()));
        c.where(p);
        List<Fattura> fatture = getEntityManager().createQuery(c).getResultList();
        for (Fattura mesiPrecedenti : fatture) {
            if (!mesiPrecedentiAssegnati.contains(mesiPrecedenti.getIdFatt())) {
                jso = new JSONObject();
                jso.put("id", mesiPrecedenti.getIdFatt());
                jso.put("id_fattura", fattura.getIdFatt());
                jso.put("mese", mesiPrecedenti.getMeseRif());
                jso.put("importo", mesiPrecedenti.getImportoTotale());
                jso.put("causale", mesiPrecedenti.getCausale());
                jso.put("inserimento", "No");
                jsa.put(jso);
            }//if
        }//for
        this.mandaRisposta(response, jsa, null);
    }//dettaglioMesiPrecedentiFatturazioni

    /**
     * Carica dalla base di dati le voci della fattura.
     *
     * @param request Richiesta del browser con l'id della fattura richiesta.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioQuoteObbligatiFatturazioni")
    public void dettaglioQuoteObbligatiFatturazioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt(request.getParameter("id")));
        Fattura fattura = (Fattura) q.getSingleResult();
        JSONArray jsa = new JSONArray();
        if (fattura.getCfObbligato1() != null) {
            JSONObject jso = new JSONObject();
            jso.put("id", fattura.getCfObbligato1());
            jso.put("codice_fiscale", fattura.getCfObbligato1());
            jso.put("importo", fattura.getQuotaObbligato1());
            jso.put("id_fattura", request.getParameter("id"));
            jsa.put(jso);
        }//if
        if (fattura.getCfObbligato2() != null) {
            JSONObject jso = new JSONObject();
            jso.put("id", fattura.getCfObbligato2());
            jso.put("codice_fiscale", fattura.getCfObbligato2());
            jso.put("importo", fattura.getQuotaObbligato2());
            jso.put("id_fattura", request.getParameter("id"));
            jsa.put(jso);
        }//if
        if (fattura.getCfObbligato3() != null) {
            JSONObject jso = new JSONObject();
            jso.put("id", fattura.getCfObbligato3());
            jso.put("codice_fiscale", fattura.getQuotaObbligato3());
            jso.put("importo", fattura.getIndirizzoObbl3());
            jso.put("id_fattura", request.getParameter("id"));
            jsa.put(jso);
        }//if
        this.mandaRisposta(response, jsa, null);
    }//dettaglioQuoteObbligatiFatturazioni

    /**
     * Salva nella base di dati i dati generali di una fattura.
     *
     * @param request Richiesta del browser con l'id della fattura e dei
     * relativi dati.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     * @throws ParseException In caso che la data di scatenza non sia formattata
     * correttamente.
     */
    @ResourceMapping(value = "salvaDettaglioFattura")
    @Transactional
    public void salvaDettaglioFattura(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, ParseException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONObject jsoFattura = new JSONObject(request.getParameter("data"));
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt((String) jsoFattura.get("id")));
        Fattura fattura = (Fattura) q.getSingleResult();
        fattura.setBollo(new BigDecimal(((Number) jsoFattura.get("bollo")).doubleValue()));
        fattura.setCausale((String) jsoFattura.get("causale"));
        fattura.setNote((String) jsoFattura.get("note"));
        fattura.setImportoTotale(new BigDecimal(((Number) jsoFattura.get("da_pagare")).doubleValue()));
        fattura.setCodFisc((String) jsoFattura.get("codice_fiscale"));

        fattura.setScadenza(ISODateTimeFormat.dateTimeParser().parseDateTime(jsoFattura.getString("scadenza")).toDate());
        fattura.setImpIva(new BigDecimal(((Number) jsoFattura.get("importo_iva")).doubleValue()));
        ParametriIndata param = (ParametriIndata) getEntityManager().createNamedQuery("ParametriIndata.findByIdParamIndata").setParameter("idParamIndata", jsoFattura.get("iva")).getSingleResult();
        fattura.setIdParamIva(param);
        getEntityManager().merge(fattura);
        JSONArray risposta = new JSONArray();
        risposta.put(jsoFattura);
        this.mandaRisposta(response, new JSONArray(), null);
    }//salvaDettaglioFattura

    /**
     * Salva nella base di dati l'eventuame modifica dell'aumento o della
     * ridozione delle voci della fattura.
     *
     * @param request Richiesta del browser con i dati delle righe modificate.
     * @param response Risposta al browser
     * @param session Sessione dalla quale recuperare l'iddentificativo dell'
     * utente connesso.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaDettaglioVociFattura")
    @Transactional
    public void salvaDettaglioVociFattura(ResourceRequest request, ResourceResponse response, PortletSession session) throws Exception {
//        try {
//            EntityManager entityManager = Connection.getEntityManager();
        Query q = getEntityManager().createNamedQuery("Utenti.findByUsername");
        q.setParameter("username", UserLocalServiceUtil.getUserById(Long.parseLong(request.getRemoteUser())).getScreenName());
//            q.setParameter("username", "admin");
        Utenti utente = (Utenti) q.getSingleResult();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        JSONArray jsa = new JSONArray();
        q = getEntityManager().createNamedQuery("FatturaDettaglio.findByIdFattDettaglio");
//            synchronized (entityManager) {
//                EntityTransaction t = entityManager.getTransaction();
//                t.begin();
        for (int i = 0; i < dati.length(); i++) {
            BigDecimal aumentoOrig = BigDecimal.ZERO, riduzioneOrig = BigDecimal.ZERO;
            JSONObject jsoDettaglio = (JSONObject) dati.get(i);
            q.setParameter("idFattDettaglio", Integer.parseInt((String) jsoDettaglio.get("id")));
            FatturaDettaglio dettaglio = (FatturaDettaglio) q.getSingleResult();
            if (jsoDettaglio.has("aumento") && !JSONObject.NULL.equals(jsoDettaglio.get("aumento"))) {
                aumentoOrig = dettaglio.getAumento() == null ? BigDecimal.ZERO : dettaglio.getAumento();
                dettaglio.setAumento(new BigDecimal(((Number) jsoDettaglio.get("aumento")).doubleValue()));
            }//if
            if (jsoDettaglio.has("riduzione") && !JSONObject.NULL.equals(jsoDettaglio.get("riduzione"))) {
                riduzioneOrig = dettaglio.getRiduzione() == null ? BigDecimal.ZERO : dettaglio.getRiduzione();
                dettaglio.setRiduzione(new BigDecimal(((Number) jsoDettaglio.get("riduzione")).doubleValue()));
            }//if
            //Aggiorno il CDG
            BigDecimal totale = BigDecimal.ZERO;
            List<PaiInterventoMese> interventi = dettaglio.getPaiInterventoMeseList();
            for (PaiInterventoMese intervento : interventi) {
                totale = totale.add(intervento.getBdgPrevQta());
            }//for
            for (PaiInterventoMese paiInterventoMese : interventi) {
                BigDecimal proporzione = paiInterventoMese.getBdgPrevQta().divide(totale, MathContext.DECIMAL32);
                PaiCdg cdg = paiInterventoMese.getPaiCdg();
                BigDecimal modificaAumento = aumentoOrig.subtract(dettaglio.getAumento() == null ? BigDecimal.ZERO : dettaglio.getAumento());
                BigDecimal modificaRiduzione = riduzioneOrig.subtract(dettaglio.getRiduzione() == null ? BigDecimal.ZERO : dettaglio.getRiduzione());
                cdg.setImpComplUsingFascia(cdg.getImpCompl().add(modificaRiduzione.subtract(modificaAumento).multiply(proporzione)));
                getEntityManager().persist(cdg);

                PaiEvento evento = Pratica.serializePaiEvento(paiInterventoMese.getPaiIntervento().getPai().getAnagrafeSoc(), paiInterventoMese.getPaiIntervento().getPai(), paiInterventoMese.getPaiIntervento(), "Modificata fattura collegata", utente);
                getEntityManager().merge(evento);
            }//for
            getEntityManager().merge(dettaglio);
            jsa.put(jsoDettaglio);
        }//for
        this.mandaRisposta(response, jsa, null);

    }//salvaDettaglioVociFattura

    /**
     * Salva l'aggiunta o la rimozione dalla fattura delle fatture dei mesi
     * precedenti. Le fatture che verranno tolte subiranno un cambio di stato da
     * <code>annullata</code> a
     * <code>da emettere</code>
     *
     * @param request Richiesta del browser con i dati delle righe modificate.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaDettaglioMesiPrecedentiFatturazioni")
    @Transactional
    public void salvaDettaglioMesiPrecedentiFatturazioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JSONArray jsaDati = new JSONArray(request.getParameter("data"));
        //presuppongo almeno una riga altrimenti la tabella non esegue la
        //chiamata AJAX di sincronizzazione.
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")));
        Fattura fattura = (Fattura) q.getSingleResult();
        //trovata la fattura cerco ancora le fatture precedenti
        List<Fattura> fatturePrecedenti = fattura.getFatturaList();
        for (int i = 0; i < jsaDati.length(); i++) {
            if ("Si".equals(((JSONObject) jsaDati.get(i)).get("inserimento"))) {
                q.setParameter("idFatt", ((JSONObject) jsaDati.get(i)).get("id"));
                Fattura fatturaPrecedente = (Fattura) q.getSingleResult();
                fatturaPrecedente.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "an"));
                fatturePrecedenti.add(fatturaPrecedente);
            }//if
            else {
                //se l'inserimento non vale si, vale "No", quindi rimuovo l'associazione.
                for (Fattura fatturaPrecedente : fatturePrecedenti) {
                    if (fatturaPrecedente.getIdFatt().equals(((JSONObject) jsaDati.get(i)).get("id"))) {
                        fatturePrecedenti.remove(fatturaPrecedente);
                        fatturaPrecedente.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "de"));
                        break;
                    }//if
                }//for
            }//else
        }//for
        getEntityManager().persist(fattura);
        this.mandaRisposta(response, jsaDati, null);
    }//salvaDettaglioMesiPrecedentiFatturazioni

    /**
     * Salva nella base di dati la modifica delle quote delle persone civilmente
     * obbligate a pagare la retta.
     *
     * @param request Richiesta del browser con l'id della fattura richiesta.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaDettaglioQuoteObbligatiFatturazioni")
    @Transactional
    public void salvaDettaglioQuoteObbligatiFatturazioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONArray jsaDati = new JSONArray(request.getParameter("data"));
        //presuppongo almeno una riga altrimenti la tabell non esegue la
        //chiamata AJAX di sincronizzazione.
        Integer idFattura = Integer.parseInt((String) ((JSONObject) jsaDati.get(0)).get("id_fattura"));
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", idFattura);
        Fattura f = (Fattura) q.getSingleResult();
        for (int i = 0; i < jsaDati.length(); i++) {
            JSONObject quota = (JSONObject) jsaDati.get(i);
            if (quota.get("codice_fiscale").equals(f.getCfObbligato1())) {
                f.setQuotaObbligato1(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//if
            else if (quota.get("codice_fiscale").equals(f.getCfObbligato2())) {
                f.setQuotaObbligato2(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//else if
            else if (quota.get("codice_fiscale").equals(f.getCfObbligato3())) {
                f.setQuotaObbligato3(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//else if
        }//for
//        EntityTransaction t = entityManager.getTransaction();
//        synchronized (entityManager) {
//            t.begin();
        getEntityManager().merge(f);
//            t.commit();
//        }//synchronized
        this.mandaRisposta(response, jsaDati, null);
    }//salvaDettaglioQuoteObbligatiFatturazioni
    
    /**
     * Ritorna il costo standard di un intervento.
     * @param i
     * @param e
     * @return
     */
    private BigDecimal getImportoStandard(PaiIntervento i,EntityManager e){
    	BigDecimal importoStandard;
    	//se ha una struttura 
    	if(i.getTariffa()!=null){
			importoStandard = i.getTariffa().getCosto();
    	}
    	else {
    	importoStandard = i.getTipologiaIntervento().getImpStdCosto();
    	}
    	return importoStandard;
    }
}//AjaxFatturaDettaglio

