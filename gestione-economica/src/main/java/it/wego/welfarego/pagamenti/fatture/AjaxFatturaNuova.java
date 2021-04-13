package it.wego.welfarego.pagamenti.fatture;

import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Controller per le chiamate AJAX riguardanti la gestione della creazione delle
 * nuove fatture selezionendo i vari dati direttamente dalla base di dati
 * popolata precedentemente dall'utente nella sezione delle acquisizioni.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxFatturaNuova extends AbstractAjaxController {

    /**
     * Esegue una ricerca di tutte le anagrafiche alle quali si può emettere una
     * nuova fattura.
     *
     * @param request Richiesta del browser contenente il filtro di ricerca
     * dell' anagrafica presente nella parametro <code>query</code>
     * @param response Risposta al browser nel formato richiesto da "Combobox
     * with Template and AJAX" di EXT-JS.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaAnagraficaFatture")
    public void cercaAnagraficaFatture(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
//        try {
        String filtroRicerca = request.getParameter("query").toLowerCase();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery(AnagrafeSoc.class);
        Root root = c.from(AnagrafeSoc.class);
        Join joinPai = root.join("cartellaSociale").join("paiList");
        Join joinIntervento = joinPai.join("paiInterventoList");
        Join ti = joinIntervento.join("tipologiaIntervento", JoinType.INNER);
        Predicate p = cb.like(cb.lower((Expression) root.get("nome")), "%" + filtroRicerca + "%");
        p = cb.or(p, cb.like(cb.lower((Expression) root.get("cognome")), "%" + filtroRicerca + "%"));
        p = cb.or(p, cb.like(cb.lower((Expression) root.get("codFisc")), "%" + filtroRicerca + "%"));
        p = cb.and(p, joinIntervento.get("statoInt").in("C","E"));
        p = cb.and(p, cb.equal(ti.get("flgFatt"), "S"));
         p = cb.and(p, joinIntervento.get("dtEsec").isNotNull());
        c.where(p);
        c.distinct(true);
        List<AnagrafeSoc> persone = getEntityManager().createQuery(c).getResultList();
        JSONObject risposta = new JSONObject();
        risposta.put("totalCount", Integer.toString(persone.size()));
        JSONArray jsaPersone = new JSONArray();
        for (AnagrafeSoc persona : persone) {
            JSONObject anagrafica = new JSONObject();
            anagrafica.put("id", persona.getCodAna().toString());
            anagrafica.put("nome", persona.getNome());
            anagrafica.put("cognome", persona.getCognome());
            anagrafica.put("codice_fiscale", persona.getCodFisc());
            jsaPersone.put(anagrafica);
        }//for
        risposta.put("persone", jsaPersone);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(risposta);
        response.getWriter().close();
//        } finally {
//            entityManager.close();
//        }
    }//cercaAnagraficaFatture

    /*
     * Cerca nella base di dati tutte le tipologie d'internvento che possono
     * essere scelte dall'utente come voci di una nuova fattura. L'importo
     * restituito è l'importo standard diminuito secondo la fascia di sconto
     * assiciata all'anagrafica. @param request Richiesta del browser contenente
     * l'id dell'anagrafica. @param response Risposta al browser nel formato
     * richiesto da "Combobox with Template and AJAX" di EXT-JS. @throws
     * IOException In caso di problemi durante la scrittura del mesaggio di
     * risposta. @throws JSONException In caso di problemi durante la formazione
     * della risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaTipologiaInterventiFatturazioniNuova")
    public void listaTipologiaInterventiFatturazioniNuova(ResourceResponse response, ResourceRequest request) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        Query q = getEntityManager().createNamedQuery("TipologiaIntervento.findByFlgFatt");
        q.setParameter("flgFatt", 'S');
        List<TipologiaIntervento> tipi = q.getResultList();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(Pai.class);
        Predicate p = cb.equal(r.get("dtChiusPai"), cb.nullLiteral(Date.class));
        p = cb.and(p, cb.equal(r.get("codAna").get("codAna"), Integer.parseInt(request.getParameter("id"))));
        c.where(p);
        Pai pai = (Pai) getEntityManager().createQuery(c).getSingleResult();
        JSONArray jsa = new JSONArray();
        for (TipologiaIntervento tipo : tipi) {
            //controllo per evitare una mancanza del dato nella base di dati
            if (tipo.getIdParamUniMis() != null) {
                JSONObject jso = new JSONObject();
                jso.put("id", tipo.getCodTipint());
                jso.put("label", tipo.getDesTipint());
                jso.put("unita_di_misura", tipo.getIdParamUniMis().getDesParam());
                double importoUnitario = tipo.getImpStdEntr() == null ? 0d : tipo.getImpStdEntr().doubleValue();
                BigDecimal percentualeRiduzione = BigDecimal.ZERO;
                if(pai.getIdParamFascia()!=null){
                	percentualeRiduzione=pai.getIdParamFascia().getDecimalParam();
                }
              //  importoUnitario = importoUnitario * (100 - pai.getIdParamFascia().getDecimalParam().doubleValue()) / 100;
                jso.put("importo_unitario", importoUnitario);
                jso.put("percentualeRiduzione", percentualeRiduzione.toString());
                jso.put("aliquotaIva", tipo.getIpAliquotaIva().getDecimalPercentageParamAsDecimal());
                jsa.put(jso);
            }//if
        }//for
        this.mandaRisposta(response, jsa, null);
    }//listaTipologiaInterventiFatturazioniNuova

    /**
     * Salva le voci di una nuova fattura.
     *
     * @param request Richiesta del browser con i dati delle voci fattura.
     * @param response Risposta al browser contenente la copia dei dati
     * trasmessi per confermare il corretto salvataggio dei dati.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     * @throws ParseException In caso una delle date presenti non sia nel
     * formato italiano.
     */
    @ResourceMapping(value = "salvaVociFatturazioniNuova")
    @Transactional
    public void salvaVociFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, ParseException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        JSONArray jsa = new JSONArray();
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) dati.get(0)).get("id_fattura")));
        Fattura fattura = (Fattura) q.getSingleResult();
        q = getEntityManager().createNamedQuery("TipologiaIntervento.findByCodTipint");
        for (int i = 0; i < dati.length(); i++) {
            JSONObject jsoVoce = (JSONObject) dati.get(i);
            q.setParameter("codTipint", jsoVoce.get("tipo_servizio_value"));
            TipologiaIntervento tipo = (TipologiaIntervento) q.getSingleResult();
            Calendar dataOdierna = Calendar.getInstance();
            FatturaDettaglio dettaglio = new FatturaDettaglio();
            dettaglio.setAnnoEff(dataOdierna.get(Calendar.YEAR));
            if (jsoVoce.has("aumento") && !JSONObject.NULL.equals(jsoVoce.get("aumento"))) {
                dettaglio.setAumento(new BigDecimal(((Number) jsoVoce.get("aumento")).doubleValue()));
            }//if
            dettaglio.setCausVar(null);
            dettaglio.setCodTipint(tipo);
            dettaglio.setIdFatt(fattura);
            dettaglio.setIdParamUnitaMisura(tipo.getIdParamUniMis());
            BigDecimal fascia = BigDecimal.ZERO;
            if(fattura.getIdParamFascia()!=null){
            fascia =fattura.getIdParamFascia().getDecimalParam();
            }
           BigDecimal percentualeRiduzione = (new BigDecimal("100").subtract(fascia)).divide(new BigDecimal("100"));
            dettaglio.setImporto(new BigDecimal(jsoVoce.get("importo_dovuto").toString()).multiply(percentualeRiduzione));
            dettaglio.setMeseEff(((Number) jsoVoce.get("mese")).intValue());
            dettaglio.setPaiInterventoMeseList(null);
            dettaglio.setQtInputata(new BigDecimal(((Number) jsoVoce.get("quantita")).doubleValue()));
            if (jsoVoce.has("riduzione") && !JSONObject.NULL.equals(jsoVoce.get("riduzione"))) {
                dettaglio.setRiduzione(new BigDecimal(((Number) jsoVoce.get("riduzione")).doubleValue()).multiply(percentualeRiduzione));
            }//if
            if (jsoVoce.has("aumento") && !JSONObject.NULL.equals(jsoVoce.get("aumento"))) {
                dettaglio.setAumento(new BigDecimal(((Number) jsoVoce.get("aumento")).doubleValue()).multiply(percentualeRiduzione));
            }//if
            dettaglio.setTimbro(dataOdierna.getTime());
            dettaglio.setVarStraord(null);
//            synchronized (entityManager) {
//                EntityTransaction t = entityManager.getTransaction();
//                t.begin();
            getEntityManager().merge(dettaglio);
            jsa.put(jsoVoce);
//                t.commit();
//            }//synchronized
        }//for
        this.mandaRisposta(response, jsa, null);
    }//salvaVociFatturazioniNuova

    /**
     * Cerca nella base di dati tutte le eventuali persone civilmente obbligate
     * a pagare la fattura che si sta realizzando. Come chiave di ricerca si
     * utilizzerà l'identificativo della {@link AnagrafeSoc}.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link AnagrafeSoc}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaQuoteObbligatiFatturazioniNuova")
    public void cercaQuoteObbligatiFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        List<PaiInterventoCivObb> obbligati = this.cercaObbligati(Integer.parseInt(request.getParameter("id")), getEntityManager());
        JSONArray jsa = new JSONArray();
        for (PaiInterventoCivObb obbligato : obbligati) {
            JSONObject jso = new JSONObject();
            jso.put("id", obbligato.getAnagrafeSoc().getCodFisc());
            jso.put("codice_fiscale", obbligato.getAnagrafeSoc().getCodFisc());
            jso.put("importo", obbligato.getImpCo());
            jso.put("id_fattura", request.getParameter("id"));
            jsa.put(jso);
        }//for
        this.mandaRisposta(response, jsa, null);
    }//cercaQuoteObbligatiFatturazioniNuova

    /**
     * Salva nella fattura le quote delle persone civilmente obbligate (in
     * maniera denormalizzata direttamente nella fattura) eventualmente
     * modificate dall'utente rispetto a quelle già presente nella base di dati.
     *
     * @param request Richiesta del browser contenente la lista delle quote
     * obbligate
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaQuoteObbligatiFatturazioniNuova")
    @Transactional
    public void salvaQuoteObbligatiFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        //presuppongo almeno una riga altrimenti la tabella non esegue la
        //chiamata AJAX di sincronizzazione.
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) dati.get(0)).get("id_fattura")));
        Fattura fattura = (Fattura) q.getSingleResult();
        for (int i = 0; i < dati.length(); i++) {
            JSONObject quota = (JSONObject) dati.get(i);
            if (quota.get("codice_fiscale").equals(fattura.getCfObbligato1())) {
                fattura.setQuotaObbligato1(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//if
            if (quota.get("codice_fiscale").equals(fattura.getCfObbligato2())) {
                fattura.setQuotaObbligato2(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//if
            if (quota.get("codice_fiscale").equals(fattura.getCfObbligato3())) {
                fattura.setQuotaObbligato3(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
            }//if
        }//for
//        synchronized (entityManager) {
//            EntityTransaction t = entityManager.getTransaction();
//            t.begin();
        getEntityManager().persist(fattura);
//            t.commit();
//        }//synchronized
        this.mandaRisposta(response, dati, null);
    }//salvaQuoteObbligatiFatturazioniNuova

    /**
     * Cerca i dati della nuova fattura. Il calcolo dei totali viene delegato
     * all'interfaccia utente. Come chiave di ricerca si utilizzerà l'
     * identificativo dell'anagrafica selezionata {@link AnagrafeSoc}.
     *
     * @param request Richiesta del browser contenente l'id della persona.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaDatiFatturazioniNuova")
    public void cercaDatiFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        Integer codAna = Integer.parseInt(request.getParameter("id"));
        //i valori null non vengono trasmessi (inseriti comunque per chiarezza e per facilitare eventuali modifiche future)
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root root = c.from(Pai.class);
        Join joinIntervento = root.join("paiInterventoList", JoinType.INNER);
        Join ti = joinIntervento.join("tipologiaIntervento", JoinType.INNER);
                Join cs = root.join("codAna", JoinType.INNER);
        Join a = cs.join("anagrafeSoc", JoinType.INNER);
        
        Predicate p = cb.equal(a.get("codAna"), codAna);
        p = cb.and(p, joinIntervento.get("statoInt").in("C","E"));
        p = cb.and(p, cb.equal(ti.get("flgFatt"), "S"));
        p = cb.and(p, joinIntervento.get("dtEsec").isNotNull());
        BigDecimal percentualRiduzione = BigDecimal.ZERO;
        c.distinct(true);
        c.where(p);
        Query q = getEntityManager().createQuery(c);
        AnagrafeSoc anagrafica;
        try {
            Pai pai = (Pai) q.getSingleResult();
            ParametriIndata pi = pai.getIdParamFascia();
            if(pi!=null){
            percentualRiduzione= pai.getIdParamFascia().getDecimalParam();
            }
            anagrafica = pai.getCodAna().getAnagrafeSoc();
        }//try
        catch (NoResultException e) {
            q = getEntityManager().createNamedQuery("AnagrafeSoc.findByCodAna");
            q.setParameter("codAna", codAna);
            anagrafica = (AnagrafeSoc) q.getSingleResult();
        }//else

        //calcolo il contributo
        cb = getEntityManager().getCriteriaBuilder();
        c = cb.createQuery();
        Root r = c.from(PaiInterventoMese.class);
        p = cb.equal(r.get("paiIntervento").get("tipologiaIntervento").get("flgFatt"), "S");
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("statoInt"), "E"));
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("pai").get("codAna").get("anagrafeSoc").get("codAna"), codAna));
        c.where(p);
        List<PaiInterventoMese> interventiMese = getEntityManager().createQuery(c).getResultList();
        double contributo = 0;
        //TODO CONTRIBUTO NON SERVE A NULLA ???? mah
        
        /*for (PaiInterventoMese interventoMese : interventiMese) {
            contributo += interventoMese.getBdgPrevEur().doubleValue();
        }//for
*/        JSONObject jso = new JSONObject();
        jso.put("id", "X");
        jso.put("cod_ana", codAna);
       
        jso.put("totale_fattura", JSONObject.NULL);
        jso.put("contributo", contributo);
        jso.put("iva", 326); //id nella base di dati dell'IVA ordinaria TODO da trovare il valore in mniera più intelligente
        jso.put("importo_iva", JSONObject.NULL);
        jso.put("bollo", 0d);
        jso.put("totale_periodo", JSONObject.NULL);
        jso.put("causale", JSONObject.NULL);
        jso.put("note", JSONObject.NULL);
        jso.put("da_pagare", JSONObject.NULL);
        jso.put("scadenza", JSONObject.NULL);
        jso.put("modalita_pagamento", JSONObject.NULL);
        jso.put("codice_fiscale", anagrafica.getCodFisc());
        JSONArray jsa = new JSONArray();
        jsa.put(jso);
        this.mandaRisposta(response, jsa, null);
    }//cercaDatiFatturazioniNuova

    /**
     * Cerca i dati delle fatture dei mesi precedent. Cerca tutte le fatture
     * collegate alla stessa {@link AnagrafeSoc} che sono state generate ma non
     * ancora emesse.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link AnagrafeSoc}.
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaMesiPrecedentiFatturazioniNuova")
    public void cercaMesiPrecedentiFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(Fattura.class);
        Predicate p = cb.equal(r.get("codAna").get("codAna"), Integer.parseInt(request.getParameter("id")));
        p = cb.and(p, cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "de"));
        c.where(p);
        List<Fattura> fatture = getEntityManager().createQuery(c).getResultList();
        JSONArray jsa = new JSONArray();
        for (Fattura fattura : fatture) {
            JSONObject jso = new JSONObject();
            jso.put("id", fattura.getIdFatt());
            jso.put("id_fattura", request.getParameter("id"));
            jso.put("mese", fattura.getMeseRif());
            jso.put("importo", fattura.getImportoTotale());
            jso.put("causale", fattura.getCausale());
            jso.put("inserimento", "No");
            jsa.put(jso);
        }//for
        this.mandaRisposta(response, jsa, null);
    }//cercaMesiPrecedentiFatturazioniNuova

    /**
     * Salva l'accoppiata di una nuova fattura con altre fatture tipicamente
     * emesse nei mesi precedenti per inviarne solo una con un importo maggiore.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link PaiIntervento}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaMesiPrecedentiFatturazioniNuova")
    @Transactional
    public void salvaMesiPrecedentiFatturazioniNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONArray jsaDati = new JSONArray(request.getParameter("data"));
        //presuppongo almeno una riga altrimenti la tabell non esegue la
        //chiamata AJAX di sincronizzazione.
        Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
        q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")));
        Fattura f = (Fattura) q.getSingleResult();
        //trovata la fattura cerco ancora le fatture precedenti
        ArrayList<Fattura> fatturePracedenti = new ArrayList<Fattura>();
        for (int i = 0; i < jsaDati.length(); i++) {
        	   if ("Si".equalsIgnoreCase((String) ((JSONObject) jsaDati.get(i)).get("inserimento"))) {
                   q.setParameter("idFatt", ((JSONObject) jsaDati.get(i)).get("id"));
                   Fattura fatturaPrecedente = (Fattura) q.getSingleResult();
                   fatturaPrecedente.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "an"));
                   fatturePracedenti.add(fatturaPrecedente);
                  
               }//if
               else {
               	  q.setParameter("idFatt", ((JSONObject) jsaDati.get(i)).get("id"));
                     Fattura fatturaPrecedente = (Fattura) q.getSingleResult();
                     fatturaPrecedente.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "de"));
               }//else 
          
        }//for
        f.setFatturaList(fatturePracedenti);
//        synchronized (entityManager) {
//            EntityTransaction t = entityManager.getTransaction();
//            t.begin();
        getEntityManager().persist(f);
//            t.commit();
//        }
        this.mandaRisposta(response, jsaDati, null);
    }//salvaMesiPrecedentiFatturazioniNuova

    /**
     * Salva i dati di una nuova fattura.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link PaiIntervento}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     * @throws ParseException In caso una delle date presenti non sia nel
     * formato italiano.
     */
    @ResourceMapping(value = "salvaFatturaNuova")
    @Transactional
    public void salvaFatturaNuova(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, ParseException {
//        EntityManager entityManager = Connection.getEntityManager();
        JSONObject jsoFattura = new JSONObject(request.getParameter("data"));
        Integer codAna = Integer.parseInt((String) jsoFattura.get("cod_ana"));
        Query q = getEntityManager().createNamedQuery("AnagrafeSoc.findByCodAna");
        q.setParameter("codAna", codAna);
        AnagrafeSoc beneficiario = (AnagrafeSoc) q.getSingleResult();

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiIntervento.class);
        Predicate p = cb.equal(r.get("pai").get("codAna").get("anagrafeSoc").get("codAna"), codAna);
        p = cb.and(p, cb.equal(r.get("tipologiaIntervento").get("flgFatt"), "S"));
        p = cb.and(p, cb.equal(r.get("statoInt"), "E"));
        c.where(p);
        c.orderBy(cb.desc(r.get("dtAvvio")));
        PaiIntervento intervento = ((List<PaiIntervento>) getEntityManager().createQuery(c).getResultList()).get(0);

        Fattura fattura = new Fattura();
        fattura.setAnno(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        fattura.setBollo(new BigDecimal(jsoFattura.getDouble("bollo")));
        fattura.setCausale((String) jsoFattura.get("causale"));
        fattura.setCodAna(beneficiario);
        fattura.setCodFisc((String) jsoFattura.get("codice_fiscale"));
        fattura.setCognome(beneficiario.getCognome());
        fattura.setContributo(new BigDecimal(((Number) jsoFattura.get("contributo")).doubleValue()));
        fattura.setIdParamFascia(intervento.getPai().getIdParamFascia());
        ParametriIndata param = (ParametriIndata) getEntityManager().createNamedQuery("ParametriIndata.findByIdParamIndata").setParameter("idParamIndata", jsoFattura.get("iva")).getSingleResult();
        fattura.setIdParamIva(param);
//        param = (ParametriIndata) entityManager.createNamedQuery("ParametriIndata.findByIdParamIndata").setParameter("idParamIndata", jsoFattura.get("modalita_pagamento")).getSingleResult();
//        fattura.setIdParamPagam(param);
        fattura.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "de"));
        fattura.setImpIva(new BigDecimal(jsoFattura.getDouble("importo_iva")));
        fattura.setImportoTotale(new BigDecimal(jsoFattura.getDouble("da_pagare")));
        fattura.setMeseRif(Calendar.getInstance().get(Calendar.MONTH) + 1);
        fattura.setNome(beneficiario.getNome());
        fattura.setNote((String) jsoFattura.get("note"));
        fattura.setPaiIntervento(intervento);
        fattura.setScadenza(ISODateTimeFormat.dateTimeParser().parseDateTime(jsoFattura.getString("scadenza")).toDate());
        fattura.setTimbro(new Date());

        //salvo in anticipo i dati delle persone socialmente obbligate
        List<PaiInterventoCivObb> obbligati = this.cercaObbligati(codAna, getEntityManager());
        if (obbligati != null) {
            switch (obbligati.size()) {
                case 6:
                case 5:
                case 4:
                case 3:
                    fattura.setCfObbligato3(obbligati.get(2).getAnagrafeSoc().getCodFisc());
                    fattura.setCognomeObbl3(obbligati.get(2).getAnagrafeSoc().getCognome());
                    fattura.setNomeObbl3(obbligati.get(2).getAnagrafeSoc().getNome());
                    fattura.setIndirizzoObbl3(this.componiIndirizzo(obbligati.get(2).getAnagrafeSoc()));
                    fattura.setQuotaObbligato3(obbligati.get(2).getImpCo());
                case 2:
                    fattura.setCfObbligato2(obbligati.get(1).getAnagrafeSoc().getCodFisc());
                    fattura.setCognomeObbl2(obbligati.get(1).getAnagrafeSoc().getCognome());
                    fattura.setNomeObbl2(obbligati.get(1).getAnagrafeSoc().getNome());
                    fattura.setIndirizzoObbl2(this.componiIndirizzo(obbligati.get(1).getAnagrafeSoc()));
                    fattura.setQuotaObbligato2(obbligati.get(1).getImpCo());
                case 1:
                    fattura.setCfObbligato1(obbligati.get(0).getAnagrafeSoc().getCodFisc());
                    fattura.setCognomeObbl1(obbligati.get(0).getAnagrafeSoc().getCognome());
                    fattura.setNomeObbl1(obbligati.get(0).getAnagrafeSoc().getNome());
                    fattura.setIndirizzoObbl1(this.componiIndirizzo(obbligati.get(0).getAnagrafeSoc()));
                    fattura.setQuotaObbligato1(obbligati.get(0).getImpCo());
                default:
                    break;
            }//switch
        }//if

        //il salvataggio dei mesi precedenti
        //e dei dettagli
        //verrà eventualmente realizzato con una chiamata separata.
//        synchronized (entityManager) {
//            EntityTransaction t = entityManager.getTransaction();
//            t.begin();
        getEntityManager().persist(fattura);
//            t.commit();
//        }//synchronized
        JSONArray risposta = new JSONArray();
        jsoFattura.put("id_fatt", fattura.getIdFatt());
        risposta.put(jsoFattura);
        this.mandaRisposta(response, risposta, null);
    }//salvaFatturaNuova

    /**
     * Cerca nella base di dati la liste delle persone civilmente obbligate a
     * pagare le fatture. La ricerca viene effettuata solo sui {@link Pai}
     * attivi.
     *
     * @param codAna Identificativo della persona che ha usufruito del servizio.
     * @param entityManager Gestore della base di dati.
     * @return Lista delle persone civilmente obbligate a pagare la retta.
     */
    private List<PaiInterventoCivObb> cercaObbligati(Integer codAna, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiInterventoCivObb.class);
        Predicate p = cb.equal(r.get("paiIntervento").get("pai").get("codAna").get("codAna"), codAna);
        p = cb.and(p, cb.equal(r.get("paiIntervento").get("statoInt"), "E"));
        c.where(p);
        c.distinct(true);
        return (entityManager.createQuery(c).getResultList());
    }//cercaObbligati
}//AJAXFatturaNuovaDaSelezione

