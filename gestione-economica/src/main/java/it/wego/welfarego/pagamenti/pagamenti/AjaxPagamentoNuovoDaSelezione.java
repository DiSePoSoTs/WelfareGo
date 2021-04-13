package it.wego.welfarego.pagamenti.pagamenti;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiInterventoPK;
import it.wego.welfarego.persistence.entities.PaiCdg;
import it.wego.welfarego.persistence.entities.PaiCdgPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoMesePK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller per le chiamate AJAX riguardanti la gestione generale dei
 * pagamenti.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxPagamentoNuovoDaSelezione extends AbstractAjaxController {
	private static final String PER_CASSA = "PER_CASSA", ACCREDITO = "ACCREDITO";
	
    /**
     * Cerca nella base di dati tutte le quantità allegate ad un intervento
     * ({@link PaiInterventoMese}) per creare le voci dei pagamenti. Come chiave
     * di ricerca si utilizzerà i dati che formano la chiave di
     * {@link PaiInterventoMese}.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link PaiInterventoMese}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaVociPagamento")
    public void cercaVociPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());
        final JSONObject jsoIdPaiIntervento = new JSONObject(request.getParameter("id"));
        JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));
        final List<PaiInterventoMese> paiInterventoMeseList = this.cercaPaiInterventoMese(jsoIdPaiIntervento, jsoPeriodoRicerca, getEntityManager());

        jsonBuilder.withData(paiInterventoMeseList).withTransformer(new JsonMapTransformer<PaiInterventoMese>() {
            private final BigDecimal bd50 = new BigDecimal(60), bd30 = new BigDecimal(30);
            private BigDecimal assenzeTotali = paiInterventoMeseList.isEmpty() ? BigDecimal.ZERO
                    : new PaiInterventoMeseDao(getEntityManager()).contaGiorniAssenza(paiInterventoMeseList.iterator().next().getPaiIntervento());
            private final Comparator<PaiInterventoMese> chronologicalComparator = new Comparator<PaiInterventoMese>() {
                public int compare(PaiInterventoMese o1, PaiInterventoMese o2) {
                    return ComparisonChain.start()
                            .compare(o1.getPaiInterventoMesePK().getAnnoEff(), o2.getPaiInterventoMesePK().getAnnoEff())
                            .compare(o1.getPaiInterventoMesePK().getMeseEff(), o2.getPaiInterventoMesePK().getMeseEff())
                            .result();
                }
            };

            @Override
            public void transformToMap(final PaiInterventoMese paiInterventoMese) {
                String id = creaIdPaiInterventoMese(paiInterventoMese.getPaiInterventoMesePK());
                getLogger().debug("processing pim {}", id);
                put("id", id);
                put("id_pagamento", jsoIdPaiIntervento.toString());
                getLogger().debug("assenze totali : {}", assenzeTotali);
                put("assenze_totali", assenzeTotali);

                BigDecimal assenzeMensili = BigDecimal.ZERO;
                
                for (PaiInterventoMese record : Iterables.filter(paiInterventoMeseList, new Predicate<PaiInterventoMese>() {
                    public boolean apply(PaiInterventoMese input) {
                        return chronologicalComparator.compare(input, paiInterventoMese) == 0;
                    }

                })) {
                    assenzeMensili = assenzeMensili.add(MoreObjects.firstNonNull(record.getGgAssenza(), BigDecimal.ZERO));
                }
                getLogger().debug("assenze mensili (mese corrente) : {}", assenzeMensili);

                BigDecimal assenzeNonScalate = BigDecimal.ZERO;
                for (PaiInterventoMese record : Iterables.filter(paiInterventoMeseList, new Predicate<PaiInterventoMese>() {
                    public boolean apply(PaiInterventoMese input) {
                        return chronologicalComparator.compare(input, paiInterventoMese) < 0;
                    }
                })) {
                    assenzeNonScalate = assenzeNonScalate.add(MoreObjects.firstNonNull(record.getGgAssenza(), BigDecimal.ZERO)).min(bd50);
                }
                BigDecimal assenzeDaScalare = assenzeNonScalate.add(assenzeMensili).subtract(bd50).max(BigDecimal.ZERO);
                getLogger().debug("assenze non scalate fino al mese scorso : {}", assenzeNonScalate);
                getLogger().debug("assenze da scalare questo mese : {}", assenzeDaScalare);


                put("assenze_mensili", assenzeMensili);
                put("causale", paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getDesTipint());
                put("unita_di_misura", paiInterventoMese.getPaiIntervento().getIdParamUniMis().getDesParam());
                put("quantita", paiInterventoMese.getBdgConsQta());
                BigDecimal bdgConsVar = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsVar(), BigDecimal.ZERO),
                        costoTotaleBase = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsEur(), paiInterventoMese.getBdgPrevEur()),
                        costoTotaleEffettivo = costoTotaleBase;
                getLogger().debug("costo totale base : {}", costoTotaleBase);

                costoTotaleEffettivo = costoTotaleEffettivo.add(bdgConsVar);
                getLogger().debug("costo totale scalate le variazioni straordinarie : {}", costoTotaleEffettivo);

               
                put("variazione_straordinaria", bdgConsVar);
                put("importo_unitario", paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getImpStdSpesa());
                put("riduzione", null);
                put("aumento", null);
                put("mese", paiInterventoMese.getPaiInterventoMesePK().getMeseEff());
                put("annoEff", paiInterventoMese.getPaiInterventoMesePK().getAnnoEff());
                put("importo_preventivato", paiInterventoMese.getBdgPrevEur());
                put("importo_consuntivato", paiInterventoMese.getBdgConsEur());
                put("importo_dovuto", costoTotaleEffettivo);

                put("variazione_straordinaria", paiInterventoMese.getBdgConsVar());
                put("causale_variazione_straordinaria", paiInterventoMese.getCausVar());


                put("codImp", paiInterventoMese.getPaiInterventoMesePK().getCodImp());
                put("anno", paiInterventoMese.getPaiInterventoMesePK().getAnno());
                put("note",paiInterventoMese.getNote());
            }
        }).withParameters(request.getParameterMap()).buildStoreResponse();
    }
    
    

    /**
     * Cerca i dati del nuovo pagamento. Il calcolo dei totali viene delegato
     * all'interfaccia utente. Come chiave di ricerca si utilizzerà i dati che
     * formano la chiave di {@link PaiIntervento}.
     *
     * @param request Richiesta del browser contenente la chiave di ricerca per
     * {@link PaiIntervento}).
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaDatiPagamento")
    public void cercaDatiPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        JSONObject jsoIdPaiIntervento = new JSONObject(request.getParameter("id"));
        JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));
        List<PaiInterventoMese> interventiMese = this.cercaPaiInterventoMese(jsoIdPaiIntervento, jsoPeriodoRicerca, getEntityManager());
        Preconditions.checkArgument(!interventiMese.isEmpty());
        PaiInterventoMese interventoMese0 = interventiMese.get(0);
        PaiIntervento paiIntervento = interventiMese.get(0).getPaiIntervento();
        JSONObject jso = new JSONObject();
        jso.put("id", request.getParameter("id"));
        jso.put("decreto_impegno", interventoMese0.getPaiInterventoMesePK().getCodImp());
        jso.put("da_liquidare", JSONObject.NULL);
        jso.put("cf_beneficiario", paiIntervento.getPai().getAnagrafeSoc().getCodFisc());
        jso.put("iban_beneficiario", MoreObjects.firstNonNull(paiIntervento.getIbanDelegatoObenef(), JSONObject.NULL));
        jso.put("nome_delegante", paiIntervento.getDsCodAnaBenef().getNome());
        jso.put("cognome_delegante", paiIntervento.getDsCodAnaBenef().getCognome());
        jso.put("cf_delegante", paiIntervento.getDsCodAnaBenef().getFlgPersFg().equals("G") ? paiIntervento.getDsCodAnaBenef().getPartIva() : paiIntervento.getDsCodAnaBenef().getCodFisc());
        jso.put("note", JSONObject.NULL);
     
        String modalitaErogazione = paiIntervento.shouldUseAccredito() ? ACCREDITO : PER_CASSA;

        jso.put("modalita_erogazione", modalitaErogazione);

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
    @ResourceMapping(value = "salvaPagamento")
    @Transactional
    public void salvaPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, Exception {

        Map<String, String> jsoPagamento = JsonBuilder.getGson().fromJson(request.getParameter("data"), JsonBuilder.MAP_OF_STRINGS);
        JSONObject jsoId = new JSONObject((String) jsoPagamento.get("id"));
        JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoId.get("periodoRicerca"));
        List<PaiInterventoMese> interventiMese = this.cercaPaiInterventoMese(jsoId, jsoPeriodoRicerca, getEntityManager());
        PaiIntervento intervento = interventiMese.get(0).getPaiIntervento();

        Mandato mandato = new Mandato();
        Calendar decorrenza = Calendar.getInstance();
        decorrenza.setTime(interventiMese.get(0).getBudgetTipIntervento().getDtDx());
        mandato.setAnnoDecr(decorrenza.get(Calendar.YEAR));
        mandato.setCapitoloDecr(interventiMese.get(0).getBudgetTipIntervento().getCodCap());
        mandato.setCfBeneficiario((String) jsoPagamento.get("cf_beneficiario"));
        mandato.setCfDelegante((String) jsoPagamento.get("cf_delegante"));
        mandato.setCodAnaBeneficiario(intervento.getDsCodAnaBenef());
        mandato.setCodAnaDelegante(intervento.getDsCodAnaBenef());
        String cognomeBeneficiario = null;
        if(intervento.getDsCodAnaBenef()!=null){
        	if(intervento.getDsCodAnaBenef().getFlgPersFg().equals("G")){
        		cognomeBeneficiario=intervento.getDsCodAnaBenef().getRagSoc();
        	}
        	else {
        		cognomeBeneficiario=intervento.getDsCodAnaBenef().getCognome();
        	}
        	
        }
        if(cognomeBeneficiario==null){
        	cognomeBeneficiario=" - ";
        }
        mandato.setCognomeBeneficiario(intervento.getPai().getAnagrafeSoc().getCognome());
        mandato.setCognomeDelegante((String) jsoPagamento.get("cognome_delegante"));
        mandato.setDataDecr(interventiMese.get(0).getBudgetTipIntervento().getDtDx()); //TODO
        mandato.setGruppo("TODO");
        mandato.setIban(jsoPagamento.get("iban_beneficiario"));
        mandato.setIdParamFascia(intervento.getPai().getIdParamFascia());
        ParametriIndata param = this.cercaStatoPagamento(getEntityManager(), "de");
        mandato.setIdParamStato(param);
        mandato.setImporto(new BigDecimal((jsoPagamento.get("da_liquidare"))));
        mandato.setIndirizzo(intervento.getDsCodAnaBenef() != null ? this.componiIndirizzo(intervento.getDsCodAnaBenef()) : "TOTO dato mancante?");
        if (jsoPeriodoRicerca.has("meseEff")) {
            mandato.setMeseRif((Integer) jsoPeriodoRicerca.get("meseEff"));
        }
        else {
            //se non ho il mese di riferimento ho sicuramente il periodo
            Calendar periodoDal = Calendar.getInstance();
            periodoDal.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            periodoDal.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffDal") - 1);
            periodoDal.set(Calendar.DAY_OF_MONTH, periodoDal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Calendar periodoAl = Calendar.getInstance();
            periodoDal.set(Calendar.HOUR_OF_DAY, 0);
            periodoDal.set(Calendar.MINUTE, 0);
            periodoDal.set(Calendar.SECOND, 0);
            periodoDal.set(Calendar.MILLISECOND, 0);
            periodoAl.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffAl"));
            periodoAl.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffAl") - 1);
            periodoAl.set(Calendar.DAY_OF_MONTH, 1);
            periodoAl.set(Calendar.HOUR_OF_DAY, 0);
            periodoAl.set(Calendar.MINUTE, 0);
            periodoAl.set(Calendar.SECOND, 0);
            periodoAl.set(Calendar.MILLISECOND, 0);
            mandato.setPeriodoDal(periodoDal.getTime());
            mandato.setPeriodoAl(periodoAl.getTime());
        }
        mandato.setModalitaErogazione((String) jsoPagamento.get("modalita_erogazione"));
        if(Strings.isNullOrEmpty(mandato.getModalitaErogazione())){
            mandato.setModalitaErogazione(intervento.shouldUseAccredito()?ACCREDITO:PER_CASSA);
            if (mandato.getModalitaErogazione()==null){
            	mandato.setModalitaErogazione("NULL");
            }
               	
        }
       
        mandato.setNomeBeneficiario(intervento.getPai().getAnagrafeSoc().getNome());
        mandato.setNomeDelegante((String) jsoPagamento.get("nome_delegante"));
        mandato.setNote((String) jsoPagamento.get("note"));
        mandato.setNumDecr(interventiMese.get(0).getBudgetTipIntervento().getNumDx());
        mandato.setNumeroMandato(null);
        mandato.setPaiIntervento(intervento);
        mandato.setTimbro(new Date());
        Map<String, MandatoDettaglio> map = Maps.newHashMap();

        for (PaiInterventoMese interventoMese : interventiMese) {
            String idInterventoMese = this.creaIdPaiInterventoMese(interventoMese.getPaiInterventoMesePK());
            MandatoDettaglio dettaglio = map.get(idInterventoMese);
            if (dettaglio == null) {
                dettaglio = new MandatoDettaglio();
                map.put(idInterventoMese, dettaglio);
                dettaglio.setAumento(BigDecimal.ZERO);

                dettaglio.setCostoTotale(BigDecimal.ZERO);
                dettaglio.setCostoUnitario(intervento.getTipologiaIntervento().getImpStdSpesa());

                dettaglio.setIdParamUnitaMisura(intervento.getIdParamUniMis());
                dettaglio.setMeseEff(interventoMese.getPaiInterventoMesePK().getMeseEff());
                dettaglio.setPaiIntervento(intervento);
                dettaglio.setQtAssegnata(BigDecimal.ZERO);
                dettaglio.setRiduzione(BigDecimal.ZERO);
                dettaglio.setTimbro(new Date());
            }
            dettaglio.setCostoTotale(dettaglio.getCostoTotale().add(interventoMese.getBdgConsEur()).add(MoreObjects.firstNonNull(interventoMese.getBdgConsVar(), BigDecimal.ZERO)));
            dettaglio.setQtAssegnata(dettaglio.getQtAssegnata().add(interventoMese.getBdgConsQta()));
            interventoMese.setIdManDettaglio(dettaglio);
            PaiCdg cdg = interventoMese.getPaiCdg();
            if (cdg == null) {
                //se non esistono righe in controllo di gestione, le creo
                cdg = new PaiCdg();
                PaiCdgPK cdgPK = new PaiCdgPK();
                cdgPK.setCntTipint(interventoMese.getPaiInterventoMesePK().getCntTipint());
                cdgPK.setCodPai(interventoMese.getPaiInterventoMesePK().getCodPai());
                cdgPK.setCodTipint(interventoMese.getPaiInterventoMesePK().getCodTipint());
                cdgPK.setCodAnno(interventoMese.getPaiInterventoMesePK().getAnno());
                cdgPK.setAnnoEff(interventoMese.getPaiInterventoMesePK().getAnnoEff());
                cdgPK.setMeseEff(interventoMese.getPaiInterventoMesePK().getMeseEff());
                cdgPK.setCodImpe(interventoMese.getPaiInterventoMesePK().getCodImp());
                cdg.setPaiCdgPK(cdgPK);
                cdg.setPaiInterventoMese(interventoMese);
                cdg.setCodAna(interventoMese.getPaiIntervento().getPai().getAnagrafeSoc());
                cdg.setCodCap(interventoMese.getBudgetTipIntervento().getCodCap());
                cdg.setCcele(interventoMese.getPaiIntervento().getTipologiaIntervento().getCcele());
                cdg.setImpStd(getImportoStandard(intervento, getEntityManager()));
                cdg.setImpVar(interventoMese.getBdgConsVar());
                cdg.setQtaPrev(interventoMese.getBdgPrevQta());
                interventoMese.setPaiCdg(cdg);
            }
            cdg.setImpComplUsingFascia(interventoMese.getBdgConsEur().add(interventoMese.getBdgConsVar() == null ? BigDecimal.ZERO : interventoMese.getBdgConsVar()));
            cdg.setQtaErog(interventoMese.getBdgConsQta());
            //TODO  paiinterventomese set pagato
            interventoMese.setGenerato(0);

        }

        getEntityManager().persist(mandato);
        for (MandatoDettaglio dettaglioMese : map.values()) {
            dettaglioMese.setIdMan(mandato);
            getEntityManager().persist(dettaglioMese);
        }

        getEntityManager().flush(); // we need id_mandato later

        jsoPagamento.put("id_pagam", mandato.getIdMan().toString());

        JsonBuilder.newInstance().withWriter(response.getWriter()).withData(jsoPagamento).buildStoreResponse();

    }

    /**
     * Salva o più correttamente aggiorna i dettagli di una nuovo pagamento.
     *
     * @param request Richiesta del browser contenente i dati delle voci di
     * pagamento.
     * @param response Risposta al browser contenente la copia dei dati
     * trasmessi per confermare il corretto salvataggio dei dati.
     * @param session Sessione dalla quale recuperare l'iddentificativo dell'
     * utente connesso.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaVociPagamento")
    @Transactional
    public void salvaVociPagamento(ResourceRequest request, ResourceResponse response, PortletSession session) throws Exception {
//        EntityManager entityManager = Connection.getEntityManager();
//        try {
        JSONArray dati = new JSONArray(request.getParameter("data"));
        JSONArray jsa = new JSONArray();
        PaiIntervento paiIntervento = null;
        Query q = getEntityManager().createNamedQuery("Utenti.findByUsername");
        q.setParameter("username", UserLocalServiceUtil.getUserById(Long.parseLong(request.getRemoteUser())).getScreenName());
//            q.setParameter("username", "admin");
        Utenti utente = (Utenti) q.getSingleResult();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiInterventoMese.class);
//            synchronized (entityManager) {
//            EntityTransaction t = entityManager.getTransaction();
//            t.begin();
        for (int i = 0; i < dati.length(); i++) {
            BigDecimal aumentoOrig = BigDecimal.ZERO, riduzioneOrig = BigDecimal.ZERO;
            JSONObject jsoInterventoMese = (JSONObject) dati.get(i);
            JSONObject jsoInterventoMeseId = new JSONObject((String) ((JSONObject) dati.get(i)).get("id"));
            javax.persistence.criteria.Predicate p = cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoInterventoMeseId.get("codPai"));
            p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codTipint"), jsoInterventoMeseId.get("codTipint")));
            p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("cntTipint"), jsoInterventoMeseId.get("cntTipint")));
            p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoInterventoMeseId.get("annoEff")));
            p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoInterventoMeseId.get("meseEff")));
            c.where(p);
            //prendo il primo, tanto se c'è ne sono due, dovrebbero puntare allo stesso dettaglio
            List<PaiInterventoMese> interventiMese = getEntityManager().createQuery(c).getResultList();
            PaiInterventoMese interventoMese = interventiMese.get(0);
            paiIntervento = interventoMese.getPaiIntervento();
            if (jsoInterventoMese.has("aumento") && !JSONObject.NULL.equals(jsoInterventoMese.get("aumento"))) {
                aumentoOrig = MoreObjects.firstNonNull(interventoMese.getIdFattDettaglio().getAumento(), BigDecimal.ZERO);
                interventoMese.getIdManDettaglio().setAumento(new BigDecimal(((Number) jsoInterventoMese.get("aumento")).toString()));
            }//if
            if (jsoInterventoMese.has("riduzione") && !JSONObject.NULL.equals(jsoInterventoMese.get("riduzione"))) {
                riduzioneOrig = MoreObjects.firstNonNull(interventoMese.getIdFattDettaglio().getRiduzione(), BigDecimal.ZERO);
                interventoMese.getIdManDettaglio().setRiduzione(new BigDecimal(((Number) jsoInterventoMese.get("riduzione")).toString()));
            }//if
            getEntityManager().merge(interventoMese.getIdManDettaglio());

            //Aggiorno il CDG
            BigDecimal totale = BigDecimal.ZERO;
            for (PaiInterventoMese intervento : interventiMese) {
                totale = totale.add(intervento.getBdgPrevQta());
            }//for
            for (PaiInterventoMese intervento : interventiMese) {
                BigDecimal proporzione = intervento.getBdgPrevQta().divide(totale, MathContext.DECIMAL32);
                PaiCdg cdg = intervento.getPaiCdg();
                BigDecimal modificaAumento = aumentoOrig.subtract(MoreObjects.firstNonNull(interventoMese.getIdFattDettaglio().getAumento(), BigDecimal.ZERO));
                BigDecimal modificaRiduzione = riduzioneOrig.subtract(MoreObjects.firstNonNull(interventoMese.getIdFattDettaglio().getRiduzione(), BigDecimal.ZERO));
                // this is done differently in another place .  . so somewhere there's an error . . 
                cdg.setImpComplUsingFascia((cdg.getImpCompl().subtract(modificaAumento).add(modificaRiduzione).multiply(proporzione)));
                getEntityManager().persist(cdg);

                if (!Objects.equal(modificaAumento, BigDecimal.ZERO) || !Objects.equal(modificaRiduzione, BigDecimal.ZERO)) {
                    PaiEvento evento = Pratica.serializePaiEvento(paiIntervento.getPai().getAnagrafeSoc(), paiIntervento.getPai(), paiIntervento, "Inserimento Determina di variazione", utente);
                    evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
                    getEntityManager().merge(evento);
                    MapDatiSpecificiIntervento datoSpecificoDetermina = null;
                    for (MapDatiSpecificiIntervento map : intervento.getPaiIntervento().getMapDatiSpecificiInterventoList()) {
                        if ("ds_imp_var".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                            datoSpecificoDetermina = map;
                            break;
                        }
                    }
                    if (datoSpecificoDetermina == null) {
                        datoSpecificoDetermina = new MapDatiSpecificiIntervento();
                        MapDatiSpecificiInterventoPK pk = new MapDatiSpecificiInterventoPK();
                        pk.setCntTipint(intervento.getPaiInterventoMesePK().getCntTipint());
                        pk.setCodCampo("ds_imp_var");
                        pk.setCodPai(intervento.getPaiInterventoMesePK().getCodPai());
                        pk.setCodTipint(intervento.getPaiInterventoMesePK().getCodTipint());
                        datoSpecificoDetermina.setMapDatiSpecificiInterventoPK(pk);
                        datoSpecificoDetermina.setValCampo("0.0");
                    }
                    BigDecimal variazione = Strings.isNullOrEmpty(datoSpecificoDetermina.getValCampo()) ? BigDecimal.ZERO : new BigDecimal(datoSpecificoDetermina.getValCampo());

                    variazione = variazione.add(modificaRiduzione.subtract(modificaAumento).multiply(proporzione));
                    datoSpecificoDetermina.setValCampo(variazione.toString());
                    getEntityManager().persist(datoSpecificoDetermina);
                }
            }
            jsa.put(jsoInterventoMese);
        }

        this.mandaRisposta(response, jsa, null);

    }

    /**
     * Cerca nella base di dati i {@link PaiInterventoMese} filtrati per id del
     * {@link PaiIntervento} e il periodo di riferimento. La lista conterrà
     * solamente i recort che non sono stati ancora associati a qualche fattura.
     *
     * @param jsoIdPai l'oggetto JSON contenente l'id del {@link PaiIntervento}
     * @param jsoPeriodoRicerca l'oggetto JSON contenente il filtro di ricerca
     * per periodo.
     * @param entityManager Gestore della dase di dati.
     * @return La lista dei {@link PaiInterventoMese} trovati nella base di
     * dati.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    private List<PaiInterventoMese> cercaPaiInterventoMese(JSONObject jsoIdPai, JSONObject jsoPeriodoRicerca, EntityManager entityManager) throws JSONException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiInterventoMese.class);
        javax.persistence.criteria.Predicate p = cb.equal(r.get("idManDettaglio"), cb.nullLiteral(MandatoDettaglio.class));
        p = cb.and(p, cb.notEqual(r.get("bdgConsQta"), cb.nullLiteral(BigDecimal.class)));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoIdPai.get("codPai")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codTipint"), jsoIdPai.get("codTipint")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("cntTipint"), jsoIdPai.get("cntTipint")));
        p = cb.and(p, r.get("paiIntervento").get("statoInt").in("E","C"));
        if (jsoPeriodoRicerca.has("meseEff")) {
            javax.persistence.criteria.Predicate mese = cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoPeriodoRicerca.get("meseEff"));
            javax.persistence.criteria.Predicate anno = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoPeriodoRicerca.get("annoEff"));
            p = cb.and(p, mese, anno);
        }//if
        else {
        	javax.persistence.criteria.Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"),(Integer) jsoPeriodoRicerca.get("meseEffDal"));
            javax.persistence.criteria.Predicate annoDal = cb.equal(r.get("paiInterventoMesePK").get("annoEff"),(Integer) jsoPeriodoRicerca.get("annoEffDal"));
            javax.persistence.criteria.Predicate annoDal2 = cb.greaterThan(r.get("paiInterventoMesePK").get("annoEff"),(Integer) jsoPeriodoRicerca.get("annoEffDal") );
            javax.persistence.criteria.Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffAl"));
            javax.persistence.criteria.Predicate annoAl = cb.equal(r.get("paiInterventoMesePK").get("annoEff"),(Integer)jsoPeriodoRicerca.get("annoEffAl"));
            javax.persistence.criteria.Predicate annoAl2 = cb.lessThan(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));
            //se non era specificato il mese sicuramete deve essere specificato il periodo (verificare il metodo precedente)
         /*   Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffDal"));
            Predicate annoDal = cb.ge(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffAl"));
            Predicate annoAl = cb.le(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));*/
            p =cb.and(p, cb.or(annoDal2, cb.and(meseDal, annoDal)), cb.or(annoAl2, cb.and(meseAl, annoAl)));
        }//else
        c.where(p);
        return ((List<PaiInterventoMese>) entityManager.createQuery(c).getResultList());
    }//cercaPaiInterventoMese

    /** 
     * Metodo che salva il primo step dei nuovi pagamenti selezionati
     * Gestione economica / Pagamenti / Da generare
     * @param request
     * @param response
     * @throws Exception
     */
    @ResourceMapping(value = "salvaNuoviPagamenti")
    @Transactional
    public void salvaNuoviPagamenti (ResourceRequest request,ResourceResponse response) throws Exception{
    	String [] ids = request.getParameterValues("data");
    	 final Comparator<PaiInterventoMese> chronologicalComparator = new Comparator<PaiInterventoMese>() {
             public int compare(PaiInterventoMese o1, PaiInterventoMese o2) {
                 return ComparisonChain.start()
                         .compare(o1.getPaiInterventoMesePK().getAnnoEff(), o2.getPaiInterventoMesePK().getAnnoEff())
                         .compare(o1.getPaiInterventoMesePK().getMeseEff(), o2.getPaiInterventoMesePK().getMeseEff())
                         .result();
             }
         };
         int generati=0;
         //check per vedere se manca un codice fiscale 
         String colpevole=null;
        if( ids!=null && ids.length>0){ 
    	for(String id : ids){
    		 JSONObject jsoIdPaiIntervento = new JSONObject(id);
    	     JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));
    	    List <PaiInterventoMese> interventiMese= this.cercaPaiInterventoMese(jsoIdPaiIntervento, jsoPeriodoRicerca, getEntityManager());
    		BigDecimal totale = BigDecimal.ZERO;

    	    //mi prendo ogni singolo pai intervento mese per calcolarne il costo totale da applicare alpagamento
    		for(final PaiInterventoMese mese : interventiMese){

                  BigDecimal bdgConsVar = MoreObjects.firstNonNull(mese.getBdgConsVar(), BigDecimal.ZERO),
                          costoTotaleBase = MoreObjects.firstNonNull(mese.getBdgConsEur(), mese.getBdgPrevEur()),
                          costoTotaleEffettivo = costoTotaleBase;
                  getLogger().debug("costo totale base : {}", costoTotaleBase);

                  costoTotaleEffettivo = costoTotaleEffettivo.add(bdgConsVar);
                  getLogger().debug("costo totale scalate le variazioni straordinarie : {}", costoTotaleEffettivo);

                  totale = totale.add(costoTotaleEffettivo);
           		}
    		// cominciamo a costruire il mandato 
    		//todo è stato fatto un casino fra cognomi beneficiario e delgante in realtà si devono invertire 
    	       Mandato mandato = new Mandato();
    	        Calendar decorrenza = Calendar.getInstance();
    	        PaiInterventoMese first = interventiMese.get(0);
    	       PaiIntervento intervento = first.getPaiIntervento();
    	        decorrenza.setTime(interventiMese.get(0).getBudgetTipIntervento().getDtDx());
    	        mandato.setAnnoDecr(decorrenza.get(Calendar.YEAR));
    	        mandato.setCapitoloDecr(interventiMese.get(0).getBudgetTipIntervento().getCodCap());
    	        if(intervento.getPai().getAnagrafeSoc().getCodFisc()==null){
    	        	colpevole = "CODICE FISCALE MANCANTE:" +intervento.getPai().getAnagrafeSoc().getNome() + " " + intervento.getPai().getAnagrafeSoc().getCognome();
    	        	break;
    	        }
    	        mandato.setCfBeneficiario(intervento.getPai().getAnagrafeSoc().getCodFisc() );
    	        mandato.setNomeBeneficiario(intervento.getPai().getAnagrafeSoc().getNome());
    	        mandato.setCognomeBeneficiario(intervento.getPai().getAnagrafeSoc().getCognome());
    	        mandato.setCodAnaBeneficiario(intervento.getPai().getAnagrafeSoc());
    	        mandato.setCodAnaDelegante(intervento.getDsCodAnaBenef());
    	        String cognomeDelegato=null;
    	        String cfDelegato =null;
    	        String nomeDelegato=null;
    	        if(intervento.getDsCodAnaBenef()!=null){
    	        	if(intervento.getDsCodAnaBenef().getFlgPersFg().equals("G")){
    	        		cognomeDelegato=intervento.getDsCodAnaBenef().getRagSoc();
    	        		cfDelegato=intervento.getDsCodAnaBenef().getPartIva();
    	        		nomeDelegato="-";
    	        		
    	        	}
    	        	else {
    	        		cognomeDelegato=intervento.getDsCodAnaBenef().getCognome();
    	        		cfDelegato=intervento.getDsCodAnaBenef().getCodFisc();
    	        		nomeDelegato=intervento.getDsCodAnaBenef().getNome();
    	        	}
    	        	
    	        }
    	        if(cognomeDelegato==null){
    	        	cognomeDelegato="DATO MANCANTE";
    	        }
    	        if(cfDelegato==null){
    	        	cfDelegato="DATO MANCANTE";
    	        }
    	        if(nomeDelegato==null){
    	        	nomeDelegato="DATO MANCANTE";
    	        }
    	        mandato.setCognomeDelegante(cognomeDelegato);
    	        mandato.setNomeDelegante(nomeDelegato);
    	        mandato.setCfDelegante(cfDelegato);

    	        mandato.setDataDecr(interventiMese.get(0).getBudgetTipIntervento().getDtDx()); //TODO
    	        mandato.setGruppo("TODO");
    	        if(intervento.getIbanDelegatoObenef()!=null && intervento.getIbanDelegatoObenef().length()>30){
    	        	colpevole = "IBAN CON PIU CARATTERI DEL PREVISTO:" +intervento.getPai().getAnagrafeSoc().getNome() + " " + intervento.getPai().getAnagrafeSoc().getCognome();
    	        	break;
    	        }
    	        mandato.setIban(intervento.getIbanDelegatoObenef());
    	        mandato.setIdParamFascia(intervento.getPai().getIdParamFascia());
    	        ParametriIndata param = this.cercaStatoPagamento(getEntityManager(), "de");
    	        mandato.setIdParamStato(param);
    	        mandato.setImporto(totale);
    	        mandato.setIndirizzo(intervento.getDsCodAnaBenef() != null ? this.componiIndirizzo(intervento.getDsCodAnaBenef()) : "TOTO dato mancante?");
    	        if (jsoPeriodoRicerca.has("meseEff")) {
    	            mandato.setMeseRif((Integer) jsoPeriodoRicerca.get("meseEff"));
    	        }
    	        else {
    	            //se non ho il mese di riferimento ho sicuramente il periodo
    	            Calendar periodoDal = Calendar.getInstance();
    	            periodoDal.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffDal"));
    	            periodoDal.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffDal") - 1);
    	            periodoDal.set(Calendar.DAY_OF_MONTH, periodoDal.getActualMaximum(Calendar.DAY_OF_MONTH));
    	            Calendar periodoAl = Calendar.getInstance();
    	            periodoDal.set(Calendar.HOUR_OF_DAY, 0);
    	            periodoDal.set(Calendar.MINUTE, 0);
    	            periodoDal.set(Calendar.SECOND, 0);
    	            periodoDal.set(Calendar.MILLISECOND, 0);
    	            periodoAl.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffAl"));
    	            periodoAl.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffAl") - 1);
    	            periodoAl.set(Calendar.DAY_OF_MONTH, 1);
    	            periodoAl.set(Calendar.HOUR_OF_DAY, 0);
    	            periodoAl.set(Calendar.MINUTE, 0);
    	            periodoAl.set(Calendar.SECOND, 0);
    	            periodoAl.set(Calendar.MILLISECOND, 0);
    	            mandato.setPeriodoDal(periodoDal.getTime());
    	            mandato.setPeriodoAl(periodoAl.getTime());
    	        }
    	       
    	        
    	        mandato.setModalitaErogazione(intervento.shouldUseAccredito() ? ACCREDITO : PER_CASSA);
    	        //default
    	        if (mandato.getModalitaErogazione()==null){
                	mandato.setModalitaErogazione("NULL");
                }
    	    
    	       
    	        mandato.setNote("Pagamento generato con procedura automatica");
    	        mandato.setNumDecr(interventiMese.get(0).getBudgetTipIntervento().getNumDx());
    	        mandato.setNumeroMandato(null);
    	        mandato.setPaiIntervento(intervento);
    	        mandato.setTimbro(new Date());
    	        Map<String, MandatoDettaglio> map = Maps.newHashMap();
    	        for (PaiInterventoMese interventoMese : interventiMese) {
    	            String idInterventoMese = this.creaIdPaiInterventoMese(interventoMese.getPaiInterventoMesePK());
    	            MandatoDettaglio dettaglio = map.get(idInterventoMese);
    	            if (dettaglio == null) {
    	                dettaglio = new MandatoDettaglio();
    	                map.put(idInterventoMese, dettaglio);

    	                dettaglio.setAumento(BigDecimal.ZERO);
    	                dettaglio.setCostoTotale(BigDecimal.ZERO);
    	                dettaglio.setCostoUnitario(intervento.getTipologiaIntervento().getImpStdSpesa());
    	                //prima salvo il mandato, poi aggancio i dettagli. Vedere loop sotto.

    	                dettaglio.setIdParamUnitaMisura(intervento.getIdParamUniMis());
    	                dettaglio.setMeseEff(interventoMese.getPaiInterventoMesePK().getMeseEff());
    	                dettaglio.setPaiIntervento(intervento);
    	                dettaglio.setQtAssegnata(BigDecimal.ZERO);
    	                dettaglio.setRiduzione(BigDecimal.ZERO);
    	                dettaglio.setTimbro(new Date());
    	            }
    	            dettaglio.setCostoTotale(dettaglio.getCostoTotale().add(interventoMese.getBdgConsEur()).add(MoreObjects.firstNonNull(interventoMese.getBdgConsVar(), BigDecimal.ZERO)));
    	            dettaglio.setQtAssegnata(dettaglio.getQtAssegnata().add(interventoMese.getBdgConsQta()));
    	            interventoMese.setIdManDettaglio(dettaglio);
    	            PaiCdg cdg = interventoMese.getPaiCdg();
    	            if (cdg == null) {
    	                //se non esistono righe in controllo di gestione, le creo
    	                cdg = new PaiCdg();
    	                PaiCdgPK cdgPK = new PaiCdgPK();
    	                cdgPK.setCntTipint(interventoMese.getPaiInterventoMesePK().getCntTipint());
    	                cdgPK.setCodPai(interventoMese.getPaiInterventoMesePK().getCodPai());
    	                cdgPK.setCodTipint(interventoMese.getPaiInterventoMesePK().getCodTipint());
    	                cdgPK.setCodAnno(interventoMese.getPaiInterventoMesePK().getAnno());
    	                cdgPK.setAnnoEff(interventoMese.getPaiInterventoMesePK().getAnnoEff());
    	                cdgPK.setMeseEff(interventoMese.getPaiInterventoMesePK().getMeseEff());
    	                cdgPK.setCodImpe(interventoMese.getPaiInterventoMesePK().getCodImp());
    	                cdg.setPaiCdgPK(cdgPK);
    	                cdg.setPaiInterventoMese(interventoMese);
    	                cdg.setCodAna(interventoMese.getPaiIntervento().getPai().getAnagrafeSoc());
    	                cdg.setCodCap(interventoMese.getBudgetTipIntervento().getCodCap());
    	                cdg.setCcele(interventoMese.getPaiIntervento().getTipologiaIntervento().getCcele());
    	                cdg.setImpStd(getImportoStandard(intervento, getEntityManager()));
    	                cdg.setImpVar(interventoMese.getBdgConsVar());
    	                cdg.setQtaPrev(interventoMese.getBdgPrevQta());
    	                interventoMese.setPaiCdg(cdg);
    	            }
    	            cdg.setImpComplUsingFascia(interventoMese.getBdgConsEur().add(interventoMese.getBdgConsVar() == null ? BigDecimal.ZERO : interventoMese.getBdgConsVar()));
    	            cdg.setQtaErog(interventoMese.getBdgConsQta());
    	            //pai intervento mese set generato = true
    	            interventoMese.setGenerato(0);

    	        }
    	        getEntityManager().persist(mandato);
    	        for (MandatoDettaglio dettaglioMese : map.values()) {
    	            dettaglioMese.setIdMan(mandato);
    	            getEntityManager().persist(dettaglioMese);
    	        }

    	        getEntityManager().flush(); // we need id_mandato later
    	        
    	     generati++;  
    	}//iterazione di tutti i pagamenti scelti
        }	
    	   JSONObject totale = new JSONObject();
    	   if(colpevole==null){
           totale.put("pagamenti_generati", generati);
    	   }
    	   else {
    		   totale.put("pagamenti_generati", colpevole);
    	   }
           response.getWriter().print(totale);
           response.getWriter().close();
    }
    
    
    /**
     * Crea una stringa rappresentante la chiave primaria del
     * {@link PaiInterventoMese}. La stringa in formato JSON verrà creata
     * ignorando i campi della chiave che rappresentano l'impegno per poter
     * gestire {@link PaiInterventoMese} diversi che però rappresentano la
     * stessa azione ma solo caricati su impegni diversi.
     *
     * @param pk Chiave del {@link PaiInterventoMese}
     * @return La stringa in formato JSON rappresentante il
     * {@link PaiInterventoMesePK}
     * @throws JSONException In caso di problemi con il parsing della del
     * periodo di ricerca.
     */
    private String creaIdPaiInterventoMese(PaiInterventoMesePK pk) {
        try {
            JSONObject jsoId = new JSONObject();
            jsoId.put("codPai", pk.getCodPai());
            jsoId.put("codTipint", pk.getCodTipint());
            jsoId.put("cntTipint", pk.getCntTipint());
            jsoId.put("annoEff", pk.getAnnoEff());
            jsoId.put("meseEff", pk.getMeseEff());
            jsoId.put("anno", pk.getAnno());
            jsoId.put("codImp", pk.getCodImp());
            return (jsoId.toString());
        }
        catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    /**
     * Metodo privato che torna la lista di pai intervento mese da un id .    
     * @param id
     * @return
     * @throws JSONException
     */
   private List<PaiInterventoMese> getPaiInterventoMeseFromId(String id) throws JSONException {
   	 JSONObject jsoIdPaiIntervento = new JSONObject(id);
        JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));

        // QUERY

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiInterventoMese.class);
        javax.persistence.criteria.Predicate p = cb.equal(r.get("idFattDettaglio"), cb.nullLiteral(FatturaDettaglio.class));
        p = cb.and(p, cb.equal(r.get("idManDettaglio"), cb.nullLiteral(MandatoDettaglio.class)));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoIdPaiIntervento.get("codPai")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codTipint"), jsoIdPaiIntervento.get("codTipint")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("cntTipint"), jsoIdPaiIntervento.get("cntTipint")));
        if (jsoPeriodoRicerca.has("meseEff")) {
            javax.persistence.criteria.Predicate mese = cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoPeriodoRicerca.get("meseEff"));
            javax.persistence.criteria.Predicate anno = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoPeriodoRicerca.get("annoEff"));
            p = cb.and(p, mese, anno);
        }
        else {
            //se non era specificato il mese sicuramete deve essere specificato il periodo (verificare il metodo precedente)
            javax.persistence.criteria.Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffDal"));
            javax.persistence.criteria.Predicate annoDal = cb.ge(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            javax.persistence.criteria.Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffAl"));
            javax.persistence.criteria.Predicate annoAl = cb.le(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));
            p = cb.and(p, meseDal, annoDal, meseAl, annoAl);
        }
        c.where(p);
        ArrayList<Order> order = new ArrayList<Order>();
        order.add(cb.asc(r.get("paiInterventoMesePK").get("annoEff")));
        order.add(cb.asc(r.get("paiInterventoMesePK").get("meseEff")));
        order.add(cb.asc(r.get("paiInterventoMesePK").get("codImp")));
        c.orderBy(order);

        List<PaiInterventoMese> paiInterventoMeseList = getEntityManager().createQuery(c).getResultList();
        return paiInterventoMeseList;
   }//
   
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
}

