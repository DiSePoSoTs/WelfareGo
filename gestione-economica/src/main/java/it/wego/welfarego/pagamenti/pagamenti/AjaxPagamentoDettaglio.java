package it.wego.welfarego.pagamenti.pagamenti;

import com.google.common.base.Strings;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.pagamenti.pagamenti.service.PagametiService;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiInterventoPK;
import it.wego.welfarego.persistence.entities.PaiCdg;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.persistence.Query;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * Controller per le chiamate AJAX riguardanti la gestione della visualizzazione
 * e del salvataggio delle modifiche delle fatture già esistenti.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxPagamentoDettaglio extends AbstractAjaxController {
    private static final String PER_CASSA = "PER_CASSA";

    /**
     * Carica dalla base di dati i dati generali di un pagamento
     *
     * @param request  Richiesta del browser con l'id del mandato di pagamento
     *                 desiderato.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioDatiPagamento")
    public void dettaglioDatiPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", Integer.parseInt(request.getParameter("id")));
        Mandato mandato = (Mandato) q.getSingleResult();
        JSONObject jso = new JSONObject();
        jso.put("id", request.getParameter("id"));
        jso.put("decreto_impegno", mandato.getMandatoDettaglioList().get(0).getPaiInterventoMeseList().get(0).getPaiInterventoMesePK().getCodImp());
        jso.put("da_liquidare", mandato.getImporto());
        jso.put("cf_beneficiario", mandato.getCfBeneficiario());
        jso.put("cf_delegante", mandato.getCfDelegante());
        jso.put("iban_beneficiario", mandato.getIban());
        jso.put("nome_delegante", mandato.getNomeDelegante());
        jso.put("cognome_delegante", mandato.getCognomeDelegante());
        jso.put("note", mandato.getNote());
        jso.put("modalita_erogazione", mandato.getModalitaErogazione());
        JSONArray jsa = new JSONArray();
        jsa.put(jso);
        this.mandaRisposta(response, jsa, null);
    }//dettaglioDatiPagamento

    /**
     * Carica dalla base di dati le voci del pagamento.
     *
     * @param request  Richiesta del browser con l'id del mandato di pagamentođ
     *                 desiderato.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioVociPagamento")
    public void dettaglioVociPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
        PagametiService  pagametiService = new PagametiService();
        JSONArray jsa = new JSONArray();
        String id = request.getParameter("id");

        Mandato mandato = pagametiService.getMandato(id);

        //calcolo il totale delle assenze escluso i mesi presi in considerazione.
        for (MandatoDettaglio mandatoDettaglio : mandato.getMandatoDettaglioList()) {
            JSONObject jso = prepare_jsnoObject_from_MandatoDettaglio(mandato, mandatoDettaglio);
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }//dettaglioVociPagamento

    private JSONObject prepare_jsnoObject_from_MandatoDettaglio(Mandato mandato, MandatoDettaglio mandatoDettaglio) throws JSONException {
        PagametiService  pagametiService = new PagametiService();

        JSONObject jso = new JSONObject();
        jso.put("id", mandatoDettaglio.getIdManDettaglio());
        jso.put("id_mandato", mandato.getIdMan());
        jso.put("causale", mandato.getPaiIntervento().getTipologiaIntervento().getDesTipint());
        jso.put("unita_di_misura", mandato.getPaiIntervento().getIdParamUniMis().getDesParam());
        jso.put("quantita", mandatoDettaglio.getQtAssegnata());
        jso.put("importo_unitario", mandato.getPaiIntervento().getTipologiaIntervento().getImpStdSpesa());
        jso.put("importo_dovuto", mandatoDettaglio.getCostoTotale());
        jso.put("riduzione", mandatoDettaglio.getRiduzione());
        jso.put("aumento", mandatoDettaglio.getAumento());
        jso.put("mese", mandatoDettaglio.getMeseEff());

        double assenzeMensili = 0;
        for (PaiInterventoMese mese : mandatoDettaglio.getPaiInterventoMeseList()) {
            assenzeMensili += mese.getGgAssenza() == null ? 0d : mese.getGgAssenza().doubleValue();
        }
        jso.put("assenze_mensili", assenzeMensili);

        BigDecimal assenzeTotali = pagametiService.calcolaAssenzeTotali(mandatoDettaglio);
        double assenzeTotaliAsDouble = assenzeTotali == null ? 0d : assenzeTotali.doubleValue();
        for (PaiInterventoMese interventoMese : mandato.getPaiIntervento().getPaiInterventoMeseList()) {
            if (interventoMese.getGgAssenza() != null) {
                assenzeTotaliAsDouble += interventoMese.getGgAssenza().doubleValue();
            } else {
                assenzeTotaliAsDouble += 0;
            }
        }
        jso.put("assenze_totali", assenzeTotaliAsDouble);
        return jso;
    }


    /**
     * Salva nella base di dati i dati generali di un pagamento.
     *
     * @param request  Richiesta del browser con l'id delpagamento e dei relativi
     *                 dati.
     * @param response Risposta al browser
     * @throws IOException    In caso di problemi durante la scrittura del mesaggio
     *                        di risposta.
     * @throws JSONException  In caso di problemi durante la formazione della
     *                        risposta in formato JSON (non previsto)
     * @throws ParseException In caso che la data di scatenza non sia formattata
     *                        correttamente.
     */
    @ResourceMapping(value = "salvaDettaglioPagamento")
    @Transactional
    public void salvaDettaglioPagamento(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, ParseException {
//        EntityManager entityManager = Connection.getEntityManager();
//        getEntityManager().getTransaction().begin();
        JSONObject jsoPagamento = new JSONObject(request.getParameter("data"));
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", Integer.parseInt((String) jsoPagamento.get("id")));
        Mandato mandato = (Mandato) q.getSingleResult();
        mandato.setNote((String) jsoPagamento.get("note"));
        mandato.setCfDelegante((String) jsoPagamento.get("cf_delegante"));
        mandato.setIban(jsoPagamento.getString("iban_beneficiario"));
        mandato.setCognomeDelegante((String) jsoPagamento.get("cognome_delegante"));
        mandato.setNomeDelegante((String) jsoPagamento.get("nome_delegante"));
        mandato.setImporto(new BigDecimal(((Number) jsoPagamento.get("da_liquidare")).doubleValue()));
        mandato.setModalitaErogazione(jsoPagamento.has("modalita_erogazione") ? (String) jsoPagamento.get("modalita_erogazione") : "PER_CASSA");
        if (Strings.isNullOrEmpty(mandato.getModalitaErogazione())) {

            mandato.setModalitaErogazione("NULL");

        }
//        synchronized (entityManager)
//        {
//            EntityTransaction t = entityManager.getTransaction();
//        getEntityManager().getTransaction().begin();
//        getEntityManager().merge(mandato);
//        getEntityManager().getTransaction().commit();
//        }//synchronized
        JSONArray jsa = new JSONArray();
        jsa.put(jsoPagamento);
//        getEntityManager().getTransaction().commit();
        this.mandaRisposta(response, jsa, null);
    }//salvaDettaglioPagamento

    /**
     * Salva nella base di dati l'eventuame modifica dell'aumento o della
     * ridozione delle voci del pagamento.
     *
     * @param request  Richiesta del browser con i dati delle righe modificate.
     * @param response Risposta al browser
     * @param session  Sessione dalla quale recuperare l'iddentificativo dell'
     *                 utente connesso.
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaDettaglioVociPagamento")
    @Transactional
    public void salvaDettaglioVociPagamento(ResourceRequest request, ResourceResponse response, PortletSession session) throws Exception {
//        try {
//            EntityManager entityManager = Connection.getEntityManager();
        Query q = getEntityManager().createNamedQuery("Utenti.findByUsername");
        q.setParameter("username", UserLocalServiceUtil.getUserById(Long.parseLong(request.getRemoteUser())).getScreenName());
//            q.setParameter("username", "admin");
        Utenti utente = (Utenti) q.getSingleResult();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        JSONArray jsa = new JSONArray();
        q = getEntityManager().createNamedQuery("MandatoDettaglio.findByIdManDettaglio");
//            synchronized (entityManager) {
//                EntityTransaction t = entityManager.getTransaction();
//                t.begin();
        for (int i = 0; i < dati.length(); i++) {
            double aumentoOrig = 0, riduzioneOrig = 0;
            JSONObject jsoDettaglio = (JSONObject) dati.get(i);
            q.setParameter("idManDettaglio", Integer.parseInt((String) jsoDettaglio.get("id")));
            MandatoDettaglio dettaglio = (MandatoDettaglio) q.getSingleResult();
            if (jsoDettaglio.has("aumento") && !JSONObject.NULL.equals(jsoDettaglio.get("aumento"))) {
                aumentoOrig = dettaglio.getAumento() == null ? 0 : dettaglio.getAumento().doubleValue();
                dettaglio.setAumento(new BigDecimal(((Number) jsoDettaglio.get("aumento")).doubleValue()));
            }//if
            if (jsoDettaglio.has("riduzione") && !JSONObject.NULL.equals(jsoDettaglio.get("riduzione"))) {
                riduzioneOrig = dettaglio.getRiduzione() == null ? 0 : dettaglio.getRiduzione().doubleValue();
                dettaglio.setRiduzione(new BigDecimal(((Number) jsoDettaglio.get("riduzione")).doubleValue()));
            }//if
            //Aggiorno il CDG
            double totale = 0;
            List<PaiInterventoMese> interventi = dettaglio.getPaiInterventoMeseList();
            for (PaiInterventoMese intervento : interventi) {
                totale += intervento.getBdgPrevQta().doubleValue();
            }//for
            for (PaiInterventoMese intervento : interventi) {
                double proporzione = intervento.getBdgPrevQta().doubleValue() / totale;
                PaiCdg cdg = intervento.getPaiCdg();
                double modificaAumento = aumentoOrig - (dettaglio.getAumento() == null ? 0 : dettaglio.getAumento().doubleValue());
                double modificaRiduzione = riduzioneOrig - (dettaglio.getRiduzione() == null ? 0 : dettaglio.getRiduzione().doubleValue());
                cdg.setImpComplUsingFascia(new BigDecimal(cdg.getImpCompl().doubleValue() + ((modificaRiduzione - modificaAumento) * proporzione)));
                getEntityManager().persist(cdg);

                if (modificaAumento != 0 || modificaRiduzione != 0) {
                    PaiEvento evento = Pratica.serializePaiEvento(interventi.get(0).getPaiIntervento().getPai().getAnagrafeSoc(), interventi.get(0).getPaiIntervento().getPai(), interventi.get(0).getPaiIntervento(), "Inserimento Determina di variazione", utente);
                    evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
                    getEntityManager().merge(evento);
                    MapDatiSpecificiIntervento datoSpecificoDetermina = null;
                    for (MapDatiSpecificiIntervento map : intervento.getPaiIntervento().getMapDatiSpecificiInterventoList()) {
                        if ("ds_imp_var".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                            datoSpecificoDetermina = map;
                            break;
                        }//if
                    }//for
                    if (datoSpecificoDetermina == null) {
                        datoSpecificoDetermina = new MapDatiSpecificiIntervento();
                        MapDatiSpecificiInterventoPK pk = new MapDatiSpecificiInterventoPK();
                        pk.setCntTipint(intervento.getPaiInterventoMesePK().getCntTipint());
                        pk.setCodCampo("ds_imp_var");
                        pk.setCodPai(intervento.getPaiInterventoMesePK().getCodPai());
                        pk.setCodTipint(intervento.getPaiInterventoMesePK().getCodTipint());
                        datoSpecificoDetermina.setMapDatiSpecificiInterventoPK(pk);
                        datoSpecificoDetermina.setValCampo("0.0");
                    }//if
                    Double variazione = Double.parseDouble(datoSpecificoDetermina.getValCampo());
                    variazione += (modificaRiduzione - modificaAumento) * proporzione;
                    datoSpecificoDetermina.setValCampo(variazione.toString());
                    getEntityManager().merge(datoSpecificoDetermina);
                }//if
            }//for
            getEntityManager().merge(dettaglio);
            jsa.put(jsoDettaglio);
        }//for
//                t.commit();
//            }//synchronized
        this.mandaRisposta(response, jsa, null);
//        }//try
//        catch (Exception e) {
//            e.printStackTrace();
//        }//catch
    }//salvaDettaglioVociPagamento
}//AjaxPagamentoDettaglio

