package it.wego.welfarego.pagamenti.pagamenti;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.liferay.portal.kernel.search.ParseException;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Controller per le chiamate AJAX riguardanti la gestione generale dei
 * pagamenti.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxPagamentoNuovo extends AbstractAjaxController {
	 private static final String PER_CASSA = "PER_CASSA", ACCREDITO = "ACCREDITO";

    /**
     * Esegue una ricerca di tutte le anagrafiche alle quali si può emettere una
     * nu mandato di pagamento.
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
    @ResourceMapping(value = "cercaAnagraficaPagamenti")
    public void cercaAnagraficaPagamenti(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        String filtroRicerca = request.getParameter("query").toLowerCase();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery(AnagrafeSoc.class);
        Root root = c.from(AnagrafeSoc.class);
        Join joinPai = root.join("cartellaSociale").join("paiList");
        Join joinIntervento = joinPai.join("paiInterventoList");
        Predicate p = cb.like(cb.lower((Expression) root.get("nome")), "%" + filtroRicerca + "%");
        p = cb.or(p, cb.like(cb.lower((Expression) root.get("cognome")), "%" + filtroRicerca + "%"));
        p = cb.or(p, cb.like(cb.lower((Expression) root.get("codFisc")), "%" + filtroRicerca + "%"));
        p = cb.and(p, cb.equal(joinIntervento.get("statoInt"), "E"));
        p = cb.and(p, cb.equal(joinIntervento.get("tipologiaIntervento").get("flgPagam"), "S"));
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
        }
        risposta.put("persone", jsaPersone);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(risposta);
        response.getWriter().close();
    }

    /**
     * Esegue una ricerca di tutti gli impegni associabili ad un'anagrafica.
     *
     * @param request Richiesta del browser contenente l'id dell'anagrafica.
     * @param response Risposta al browser nel formato richiesto da "Combobox"
     * di EXT-JS.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaDecretiNuovo")
    public void listaDecretiNuovo(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(BudgetTipIntervento.class);
        ListJoin join = r.join("tipologiaIntervento").joinList("paiInterventoList");
        Predicate p = cb.equal(join.get("dsCodAnaBenef").get("codAna"), request.getParameter("id"));
        p = cb.and(p, cb.equal(join.get("statoInt"), "E"));
        c.where(p);
        c.distinct(true);
        List<BudgetTipIntervento> budgetInterventi = getEntityManager().createQuery(c).getResultList();
        JSONObject risposta = new JSONObject();
        risposta.put("totalCount", Integer.toString(budgetInterventi.size()));
        JSONArray jsaImpegni = new JSONArray();
        for (BudgetTipIntervento budgetIntervento : budgetInterventi) {
            JSONObject impegno = new JSONObject();
            JSONObject id = new JSONObject();
            id.put("codImpe", budgetIntervento.getBudgetTipInterventoPK().getCodImpe());
            id.put("codAnno", budgetIntervento.getBudgetTipInterventoPK().getCodAnno());
            id.put("codTipint", budgetIntervento.getBudgetTipInterventoPK().getCodTipint());
            impegno.put("id", id.toString());
            impegno.put("label", budgetIntervento.getBudgetTipInterventoPK().getCodImpe());
            jsaImpegni.put(impegno);
        }
        this.mandaRisposta(response, jsaImpegni, null);
    }

    /*
     * Cerca nella base di dati tutte le tipologie d'internvento che possono
     * essere scelte dall'utente come voci di uno nuovo pagamento. L'importo
     * restituito è l'importo standard di spesa. @param request Richiesta del
     * browser contenente l'id dell'anagrafica. @param response Risposta al
     * browser nel formato richiesto da "Combobox with Template and AJAX" di
     * EXT-JS. @throws IOException In caso di problemi durante la scrittura del
     * mesaggio di risposta. @throws JSONException In caso di problemi durante
     * la formazione della risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaTipologiaInterventiPagamentiNuovo")
    public void listaTipologiaInterventiPagamentiNuovo(ResourceResponse response, ResourceRequest request) throws IOException, JSONException {

        Query q = getEntityManager().createNamedQuery("TipologiaIntervento.findByFlgPagam");
        q.setParameter("flgPagam", 'S');
        List<TipologiaIntervento> tipi = q.getResultList();
        JSONArray jsa = new JSONArray();
        for (TipologiaIntervento tipo : tipi) {
            JSONObject jso = new JSONObject();
            jso.put("id", tipo.getCodTipint());
            jso.put("label", tipo.getDesTipint());
            jso.put("unita_di_misura", tipo.getIdParamUniMis().getDesParam());
            jso.put("importo_unitario", tipo.getImpStdSpesa());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Cerca i dati del nuovo pagamento. Il calcolo dei totali viene delegato
     * all'interfaccia utente. Come chiave di ricerca si utilizzerà l'id dell'
     * {@link AnagrafeSoc}.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link AnagrafeSoc}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaDatiPagamentiNuovo")
    public void cercaDatiPagamentiNuovo(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        Integer codAna = Integer.parseInt(request.getParameter("id"));
        //i valori null non vengono trasmessi (inseriti comunque per chiarezza e per facilitare eventuali modifiche future)
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root root = c.from(PaiIntervento.class);
        Predicate p = cb.equal(root.get("pai").get("codAna").get("codAna"), codAna);
        p = cb.and(p, cb.equal(root.get("statoInt"), "E"));
        c.distinct(true);
        c.where(p);
        Query q = getEntityManager().createQuery(c);
        PaiIntervento intervento = (PaiIntervento) q.getResultList().get(0);
        AnagrafeSoc anagrafica = intervento.getPai().getCodAna().getAnagrafeSoc();

        JSONObject jso = new JSONObject();
        jso.put("id", "X");
        jso.put("decreto_impegno", JSONObject.NULL);
        jso.put("da_liquidare", JSONObject.NULL);
        jso.put("cf_beneficiario", anagrafica.getCodFisc());
        jso.put("cod_ana", codAna);
        jso.put("iban_beneficiario", MoreObjects.firstNonNull(intervento.getIbanDelegatoObenef(), JSONObject.NULL));
        for (MapDatiSpecificiIntervento map : intervento.getMapDatiSpecificiInterventoList()) {
            if ("ds_nome_deleg".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                jso.put("nome_delegante", map.getValCampo());
            }
            if ("ds_cogn_deleg".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                jso.put("cognome_delegante", map.getValCampo());
            }
            if ("ds_codfisc_deleg".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                jso.put("cf_delegante", map.getValCampo());
            }
        }
        jso.put("note", JSONObject.NULL);
        jso.put("modalita_erogazione", JSONObject.NULL);
        JSONArray jsa = new JSONArray();
        jsa.put(jso);
        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Salva i dati di una nuovo pagamanto
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
    @ResourceMapping(value = "salvaPagamentoNuovo")
    @Transactional
    public void salvaPagamentoNuovo(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        JSONObject jsoPagamento = new JSONObject(request.getParameter("data"));
        Integer codAna = Integer.parseInt((String) jsoPagamento.get("cod_ana"));
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiIntervento.class);
        Predicate p = cb.equal(r.get("pai").get("codAna").get("anagrafeSoc").get("codAna"), codAna);
        p = cb.and(p, cb.equal(r.get("tipologiaIntervento").get("flgFatt"), "S"));
        p = cb.and(p, cb.equal(r.get("statoInt"), "E"));
        c.where(p);
        c.orderBy(cb.desc(r.get("dtAvvio")));
        PaiIntervento intervento = ((List<PaiIntervento>) getEntityManager().createQuery(c).getResultList()).get(0);

        JSONObject idImpegno = new JSONObject((String) jsoPagamento.get("decreto_impegno"));
        cb = getEntityManager().getCriteriaBuilder();
        c = cb.createQuery();
        r = c.from(BudgetTipIntervento.class);
        p = cb.equal(r.get("budgetTipInterventoPK").get("codImpe"), idImpegno.get("codImpe"));
        p = cb.and(p, cb.equal(r.get("budgetTipInterventoPK").get("codAnno"), idImpegno.get("codAnno")));
        p = cb.and(p, cb.equal(r.get("budgetTipInterventoPK").get("codTipint"), idImpegno.get("codTipint")));
        c.where(p);
        BudgetTipIntervento budget = (BudgetTipIntervento) getEntityManager().createQuery(c).getSingleResult();

        Mandato mandato = new Mandato();
        Calendar decorrenza = Calendar.getInstance();
        decorrenza.setTime(budget.getDtDx());
        mandato.setAnnoDecr(decorrenza.get(Calendar.YEAR));
        mandato.setCapitoloDecr(budget.getCodCap());
        mandato.setCfBeneficiario((String) jsoPagamento.get("cf_beneficiario"));
        mandato.setCfDelegante((String) jsoPagamento.get("cf_delegante"));
        mandato.setCodAnaBeneficiario(intervento.getDsCodAnaBenef());
        mandato.setCognomeBeneficiario(intervento.getDsCodAnaBenef() != null ? intervento.getDsCodAnaBenef().getCognome() : "TOTO dato mancante?");
        mandato.setCognomeDelegante((String) jsoPagamento.get("cognome_delegante"));
        mandato.setDataDecr(budget.getDtDx()); //TODO
        mandato.setGruppo("TODO");
        mandato.setIban(intervento.getDsCodAnaBenef().getIbanPagam());
        mandato.setIdParamFascia(intervento.getPai().getIdParamFascia());
        ParametriIndata param = this.cercaStatoPagamento(getEntityManager(), "de");
        mandato.setIdParamStato(param);
        mandato.setImporto(new BigDecimal((jsoPagamento.get("da_liquidare").toString())));
        mandato.setIndirizzo(intervento.getDsCodAnaBenef() != null ? this.componiIndirizzo(intervento.getDsCodAnaBenef()) : "TOTO dato mancante?");
        mandato.setMeseRif(Calendar.getInstance().get(Calendar.MONTH) + 1);
        //TODO coreggere con il paramentro da form
        mandato.setModalitaErogazione((String) jsoPagamento.get("modalita_erogazione"));
        if(Strings.isNullOrEmpty(mandato.getModalitaErogazione())){
            mandato.setModalitaErogazione(intervento.shouldUseAccredito()?ACCREDITO:PER_CASSA);
            if(mandato.getModalitaErogazione()==null){
            	mandato.setModalitaErogazione("NULL");
            }
               	
        }
        mandato.setNomeBeneficiario(intervento.getDsCodAnaBenef() != null ? intervento.getDsCodAnaBenef().getNome() : "TOTO dato mancante?");
        mandato.setNomeDelegante((String) jsoPagamento.get("nome_delegante"));
        mandato.setNote((String) jsoPagamento.get("note"));
        mandato.setNumDecr(budget.getNumDx());
        mandato.setNumeroMandato(null);
        mandato.setPaiIntervento(intervento);
        mandato.setTimbro(new Date());


        getEntityManager().persist(mandato);
        JSONArray jsa = new JSONArray();
        jsoPagamento.put("id_pagam", mandato.getIdMan());
        jsa.put(jsoPagamento);
        this.mandaRisposta(response, jsa, null);
    }//salvaPagamentoNuovo

    /**
     * Salva o più correttamente aggiorna i dettagli di una nuova fattura. La
     * chiave di ricerca dei record saranno le chiavi dei vari
     * {@link PaiInterventoMese} dai quali sono stati copiati i dati iniziali
     * dei dettagli della fattura.
     *
     * @param request Richiesta del browser contenente le chiavi di ricerca per
     * {@link PaiInterventoMese}.
     * @param response Risposta al browser contenente la copia dei dati
     * trasmessi per confermare il corretto salvataggio dei dati.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     * @throws ParseException In caso una delle date presenti non sia nel
     * formato italiano.
     */
    @ResourceMapping(value = "salvaVociPagamentiNuovo")
    @Transactional
    public void salvaVociPagamentiNuovo(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JSONArray dati = new JSONArray(request.getParameter("data"));
        JSONArray jsa = new JSONArray();
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", (Integer.parseInt((String) ((JSONObject) dati.get(0)).get("id_pagam"))));
        Mandato mandato = (Mandato) q.getSingleResult();
        q = getEntityManager().createNamedQuery("TipologiaIntervento.findByCodTipint");
        for (int i = 0; i < dati.length(); i++) {
            JSONObject jsoVoce = (JSONObject) dati.get(i);
            q.setParameter("codTipint", jsoVoce.get("tipo_servizio_value"));
            TipologiaIntervento tipo = (TipologiaIntervento) q.getSingleResult();
            MandatoDettaglio dettaglio = new MandatoDettaglio();
            if (jsoVoce.has("aumento") && !JSONObject.NULL.equals(jsoVoce.get("aumento"))) {
                dettaglio.setAumento(new BigDecimal(((Number) jsoVoce.get("aumento")).toString()));
            }
            dettaglio.setCostoTotale(new BigDecimal(((Number) jsoVoce.get("importo_dovuto")).toString()));
            dettaglio.setCostoUnitario(new BigDecimal(((Number) jsoVoce.get("importo_unitario")).toString()));
            dettaglio.setIdMan(mandato);
            dettaglio.setIdParamUnitaMisura(tipo.getIdParamUniMis());
            dettaglio.setMeseEff(((Number) jsoVoce.get("mese")).shortValue());
            dettaglio.setNumeroMandato(null);
            dettaglio.setPaiIntervento(mandato.getPaiIntervento());
            dettaglio.setPaiInterventoMeseList(null);
            dettaglio.setQtAssegnata(new BigDecimal(((Number) jsoVoce.get("quantita")).toString()));
            if (jsoVoce.has("riduzione") && !JSONObject.NULL.equals(jsoVoce.get("riduzione"))) {
                dettaglio.setRiduzione(new BigDecimal(((Number) jsoVoce.get("riduzione")).toString()));
            }
            dettaglio.setTimbro(new Date());

            getEntityManager().merge(dettaglio);

            jsa.put(jsoVoce);

        }
        this.mandaRisposta(response, jsa, null);
    }
}

