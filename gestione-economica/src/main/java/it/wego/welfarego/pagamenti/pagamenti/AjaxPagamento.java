package it.wego.welfarego.pagamenti.pagamenti;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.pagamenti.ExportFileUtils;
import it.wego.welfarego.pagamenti.FiltraPerDelegatoJpaPredicateBuilder;
import it.wego.welfarego.pagamenti.pagamenti.cercapagamenti.CercaPagamentiEmessiPredicateBuilder;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.xsd.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller per le chiamate AJAX riguardanti la gestione generale dei
 * pagamenti.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxPagamento extends AbstractAjaxController {

    public static final String DA_EMETTERE = "de";
    /**
     * Percorso nel quale salvare il file CSV
     */
    private String percorso;

    public void setPercorso(String percorso) {
        this.percorso = percorso;
    }

    /**
     * Recupara dalla base di dati la lista delle modalità dei pagamenti delle
     * fatture in entrata.
     *
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaModalitaPagamantiPagamenti")
    public void listaModalitaPagamanti(ResourceResponse response) throws IOException, JSONException {
        JsonBuilder jsonBuilder = getJsonBuilder(response.getWriter());

        jsonBuilder.withData(new ParametriIndataDao(getEntityManager()).findAllByTipParamAttivo(Parametri.MODALITA_PAGAMENTO)).withTransformer(new JsonMapTransformer<ParametriIndata>() {
            @Override
            public void transformToMap(ParametriIndata parametriIndata) {
                put("id", parametriIndata.getIdParamIndata());
                put("label", parametriIndata.getDesParam());
            }
        }).buildListResponse();
;
    }

    /**
     * Carica dalla base di dati la lista dei mandati di pagamento.
     *
     * @param request  Richiesta del browser contenente i dati per il filtro di
     *                 ricerca.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaPagamenti")
    public void cercaPagamenti(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        EntityManager entityManager = getEntityManager();

        String codTipint = Strings.emptyToNull(request.getParameter("tipo_intervento"));
        String mese_di_competenza_mese = request.getParameter("mese_di_competenza_mese");
        String mese_di_competenza_anno = request.getParameter("mese_di_competenza_anno");
        String uot_struttura = request.getParameter("uot_struttura");
        String cognome = Strings.emptyToNull(request.getParameter("cognome"));
        String nome = Strings.emptyToNull(request.getParameter("nome"));
        String filterDelegato = Strings.emptyToNull(request.getParameter("filtro_delegato"));
        String stato_pagamenti = request.getParameter("stato_pagamenti");

        String periodo_considerato_dal_mese = request.getParameter("periodo_considerato_dal_mese");
        String periodo_considerato_al_mese = request.getParameter("periodo_considerato_al_mese");
        String periodo_considerato_dal_anno = request.getParameter("periodo_considerato_dal_anno");
        String periodo_considerato_al_anno = request.getParameter("periodo_considerato_al_anno");

        List<Mandato> mandatoList = prepara_ed_esegui_select_per_cerca_pagamenti_eseguiti(entityManager, codTipint,
                mese_di_competenza_mese, mese_di_competenza_anno,
                uot_struttura, cognome, nome,
                filterDelegato, stato_pagamenti,
                periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);


        Collections.sort(mandatoList, new CustomComparatorMandato());

        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<String> it = parameterMap.keySet().iterator();
        String keys = "";
        while(it.hasNext()){
            String key = it.next();
            keys += key + ",  ";
        }

        PrintWriter writer = response.getWriter();
        JsonBuilder jsonBuilder = getJsonBuilder(writer);
        jsonBuilder = jsonBuilder.withParameters(parameterMap).withData(mandatoList).withTransformer(mandatoGsonTransformer);
        jsonBuilder.buildStoreResponse();

    }

    JsonBuilder getJsonBuilder(PrintWriter writer) {
        return JsonBuilder.newInstance().withWriter(writer);
    }


    /**
     * Carica dalla base di dati la lista dei mandati di pagamento che devono
     * essere ancora generati. Di fatto la ricerca viene svolta sulla tabella
     * degli interventi mese.
     *
     * @param request  Richiesta del browser contenente i dati per il filtro di
     *                 ricerca.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaPagamentiDaGenerare")
    public void cercaPagamentiDaGenerare(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JSONObject jsoData = new JSONObject();
        String tipInt = request.getParameter("tipo_intervento");
        String mese_di_competenza_mese = request.getParameter("mese_di_competenza_mese");
        String mese_di_competenza_anno = request.getParameter("mese_di_competenza_anno");
        String periodo_considerato_dal_mese = request.getParameter("periodo_considerato_dal_mese");
        String periodo_considerato_al_mese = request.getParameter("periodo_considerato_al_mese");
        String periodo_considerato_dal_anno = request.getParameter("periodo_considerato_dal_anno");
        String periodo_considerato_al_anno = request.getParameter("periodo_considerato_al_anno");
        String cognome = Strings.emptyToNull(request.getParameter("cognome"));
        String nome = Strings.emptyToNull(request.getParameter("nome"));
        String uot_struttura = request.getParameter("uot_struttura");
        String filterDelegato = request.getParameter("filtro_delegato");

        EntityManager entityManager = getEntityManager();

        List<PaiIntervento> paiInterventoList = prepara_ed_esegui_select_per_cerca_pagamenti_da_generare(entityManager, jsoData, tipInt, uot_struttura, filterDelegato, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, cognome, nome);

        Collections.sort(paiInterventoList, new CustomComparator());
        JSONArray jsa = new JSONArray();
        Set<String> idSet = Sets.newHashSet();
        JsonBuilder jsonBuilder = getJsonBuilder(response.getWriter());

        prepare_response_data(jsoData, paiInterventoList, idSet, jsonBuilder);

        jsonBuilder.withParameters(request.getParameterMap()).buildStoreResponse();
    }

    void prepare_response_data(JSONObject jsoData, List<PaiIntervento> pai, Set<String> idSet, JsonBuilder jsonBuilder) throws JSONException {
        for (PaiIntervento intervento : pai) {
            JSONObject jso = new JSONObject();
            JSONObject jsoId = new JSONObject();
            jsoId.put("codPai", intervento.getPaiInterventoPK().getCodPai());
            jsoId.put("codTipint", intervento.getPaiInterventoPK().getCodTipint());
            jsoId.put("cntTipint", intervento.getPaiInterventoPK().getCntTipint());
            jsoId.put("periodoRicerca", jsoData.toString());
            String id = jsoId.toString();
            if (!idSet.add(id)) {
                continue;
            }


            Map record = Maps.newHashMap();

            record.put("id", id);
            record.put("nome", intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getNome());
            record.put("cognome", intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getCognome());
            record.put("fascia", intervento.getPai().getIdParamFascia().getDesParam());
            record.put("tipo_intervento", intervento.getTipologiaIntervento().getDesTipint());
            record.put("mandato", "-");
            record.put("n_mandato", "-");
            record.put("data_chiusura", intervento.getDtChius() == null ? "" : Utils.dateToItString(intervento.getDtChius()));
            record.put("stato", "da generare");
            try {
                record.put("delegato", FiltraPerDelegatoJpaPredicateBuilder.getDelegato(intervento));
            } catch (Exception e) {
                getLogger().error(intervento.toString() + " | " + e.getMessage());
                record.put("delegato", "si e verificato un problema nel determinare il delegato");
            }

            jsonBuilder.addRecord(record);
        }
    }


    private Predicate filtra_per_nome(String nome, CriteriaBuilder criteriaBuilder, Root r) {
        Predicate p = null;
        if (nome != null) {
            nome = nome.toUpperCase();
            p = criteriaBuilder.like(r.get("pai").get("codAna").get("anagrafeSoc").get("nome"), nome + "%");
        }
        return p;
    }

    private Predicate filtra_per_cognome(String cognome, CriteriaBuilder criteriaBuilder, Root r) {
        Predicate p = null;
        if (cognome != null) {
            cognome = cognome.toUpperCase();
            p = criteriaBuilder.like(r.get("pai").get("codAna").get("anagrafeSoc").get("cognome"), cognome + "%");
        }
        return p;
    }

    private Predicate filtra_per_uot(String uot_struttura, CriteriaBuilder criteriaBuilder, Root r) {
        Predicate p = null;
        if (uot_struttura != null) {
            try {
                p = criteriaBuilder.equal(r.get("pai").get("idParamUot").get("idParamIndata"), Integer.parseInt(uot_struttura));
            }
            catch (NumberFormatException ex) {
                //L'id dell'uot non è in unmerico
                //Eseguo la ricerca ignorando il parametro errato.
                getLogger().warn("", ex);
            }
        }
        return p;
    }

    private Predicate filtra_per_periodo(String mese_di_competenza_mese, String mese_di_competenza_anno, String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno, JSONObject jsoData, CriteriaBuilder criteriaBuilder, ListJoin join) throws JSONException {
        Predicate p = null;
        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            try {
                Predicate mese = criteriaBuilder.equal(join.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(mese_di_competenza_mese));
                Predicate anno = criteriaBuilder.equal(join.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(mese_di_competenza_anno));
                p = criteriaBuilder.and(mese, anno);

                jsoData.put("meseEff", Integer.parseInt(mese_di_competenza_mese));
                jsoData.put("annoEff", Integer.parseInt(mese_di_competenza_anno));
            } catch (NumberFormatException ex) {
                //Le date passate sono in formato non valido, eseguo la ricerca ignorandole.
                getLogger().warn("", ex);
            }
        } else {
            if (periodo_considerato_dal_mese != null && periodo_considerato_al_mese != null && periodo_considerato_dal_anno != null && periodo_considerato_al_anno != null) {
                try {
                    Predicate meseDal = criteriaBuilder.ge(join.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(periodo_considerato_dal_mese));
                    Predicate annoDal = criteriaBuilder.equal(join.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(periodo_considerato_dal_anno));
                    Predicate annoDal2 = criteriaBuilder.greaterThan(join.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(periodo_considerato_dal_anno));
                    Predicate meseAl = criteriaBuilder.le(join.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(periodo_considerato_al_mese));
                    Predicate annoAl = criteriaBuilder.equal(join.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(periodo_considerato_al_anno));
                    Predicate annoAl2 = criteriaBuilder.lessThan(join.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(periodo_considerato_al_anno));
                    Predicate predicateDal = criteriaBuilder.or(annoDal2, criteriaBuilder.and(meseDal, annoDal));
                    Predicate predicateAl = criteriaBuilder.or(annoAl2, criteriaBuilder.and(meseAl, annoAl));

                    if (p != null) {
                        p = criteriaBuilder.and(p, predicateDal, predicateAl);
                    } else {
                        p = criteriaBuilder.and(predicateDal, predicateAl);
                    }

                    jsoData.put("meseEffDal", Integer.parseInt(periodo_considerato_dal_mese));
                    jsoData.put("annoEffDal", Integer.parseInt(periodo_considerato_dal_anno));
                    jsoData.put("meseEffAl", Integer.parseInt(periodo_considerato_al_mese));
                    jsoData.put("annoEffAl", Integer.parseInt(periodo_considerato_al_anno));
                } catch (NumberFormatException ex) {
                    //Le date passate sono in formato non valido, eseguo la ricerca ignorandole.
                    getLogger().warn("", ex);
                }
            }
        }
        return p;
    }

    private Predicate filtra_per_codice_tipo_intervento(String tipInt, CriteriaBuilder criteriaBuilder, Root r) {
        Predicate predicate = null;
        if (!Strings.isNullOrEmpty(tipInt)) {
            predicate = criteriaBuilder.equal(r.get("paiInterventoPK").get("codTipint"), tipInt);
        }
        return predicate;
    }

    /**
     * Salva il nuovo stato dell'elenco deli {@link Mandato}
     *
     * @param request  Richiesta del browser contenente i dati deli mandati di
     *                 pagamento con il nuovo stato
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaStatoPagamenti")
    @Transactional
    public void salvaStatoPagamenti(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        JSONArray jsa = new JSONArray();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");

        for (int i = 0; i < dati.length(); i++) {
            JSONObject jsoPagamento = (JSONObject) dati.get(i);
            q.setParameter("idMan", jsoPagamento.get("id"));
            Mandato m = (Mandato) q.getSingleResult();
            ParametriIndata stato = this.cercaStatoFattura(getEntityManager(), "Emetti".equals(jsoPagamento.getString("azione")) ? "em" : "di");
            m.setIdParamStato(stato);
            getEntityManager().merge(m);
            jsa.put(this.genetaMandatoJSO(m));

        }

        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Invia i pagamenti.<br /> Cerca nella base di dati tutti i pagamenti che
     * sono in attesa di essere inviati, crea un file CSV contenente i loro dati
     * e infine setta lo stato del Mandato a inviato.
     *
     * @param response Risposta al browser contenente il numero di fatture
     *                 corettamente inviate.
     * @throws IOException     In caso di problemi durante la scrittura del mesaggio
     *                         di risposta.
     * @throws JSONException   In caso di problemi durante la formazione della
     *                         risposta in formato JSON (non previsto)
     * @throws SystemException
     * @throws PortalException
     */
    @ResourceMapping(value = "inviaPagamenti")
    @Transactional
    public void inviaPagamenti(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, PortalException, SystemException {
        JSONArray dati = new JSONArray(request.getParameter("data"));
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");
        List<Mandato> mandatiDaEmettere = new ArrayList<Mandato>();
        for (int i = 0; i < dati.length(); i++) {
            Integer id = (Integer) dati.get(i);
            q.setParameter("idMan", id);
            Mandato m = (Mandato) q.getSingleResult();
            if (m.getIdParamStato().equals(cercaStatoPagamento(getEntityManager(), "de"))) {
                mandatiDaEmettere.add(m);
            }
        }

        if (mandatiDaEmettere != null && !mandatiDaEmettere.isEmpty()) {
            Calendar data = Calendar.getInstance();
            File file = new File(percorso + File.separator + "pag_" + data.get(Calendar.YEAR) + "_" + (data.get(Calendar.MONTH) + 1) + "_" + data.get(Calendar.DAY_OF_MONTH) + "_" + data.get(Calendar.HOUR_OF_DAY) + "_" + data.get(Calendar.MINUTE) + "_" + data.get(Calendar.SECOND) + ".xls");
            FileUtils.writeByteArrayToFile(file, ExportFileUtils.getPagamentiXlsFileData(mandatiDaEmettere));
            PaiDocumentoDao dao = new PaiDocumentoDao(getEntityManager());
            for (Mandato mandato : mandatiDaEmettere) {
                mandato.setIdParamStato(cercaStatoPagamento(getEntityManager(), "in"));
                PaiIntervento intervento = mandato.getPaiIntervento();
                dao.createDoc(intervento.getPai(), new UtentiDao(getEntityManager()).findByUsername(PortalUtil.getUser(request).getLogin()), "File pagamenti", Base64.encodeBase64String(FileUtils.readFileToByteArray(file)), file.getName());
            }

            JSONObject totale = new JSONObject();
            totale.put("pagamenti_inviati", mandatiDaEmettere.size());
            totale.put("fileGenerato", file.getAbsolutePath());
            response.getWriter().print(totale);
            response.getWriter().close();
        }
        else {
            JSONObject zero = new JSONObject();
            zero.put("pagamenti_inviati", 0);
            response.getWriter().print(zero);
            response.getWriter().close();
        }
    }

    /**
     * Metodo che ritorna in anteprima il file della ragioneria senza salvarlo e senza cambiare stati ai mandati per permettere le eventuali ultime correzioni
     * Gestione Economica / Pagamenti /Anteprima file ragioneria
     *
     * @param request
     * @param response
     * @throws JSONException
     * @throws IOException
     */
    @ResourceMapping(value = "anteprimaFilePagamenti")
    @Transactional
    public void anteprimaFilePagamenti(ResourceRequest request, ResourceResponse response) throws JSONException, IOException {
        JSONArray dati = new JSONArray(request.getParameter("data"));
        Query q = getEntityManager().createNamedQuery("Mandato.findByIdMan");

        List<Mandato> mandatiDaEmettere = new ArrayList<Mandato>();
        for (int i = 0; i < dati.length(); i++) {
            try{

                Integer id = (Integer) dati.get(i);
                q.setParameter("idMan", id);
                Mandato m = (Mandato) q.getSingleResult();
                if ("-".equalsIgnoreCase(m.getNomeDelegante())){
                    m.setNomeDelegante("");
                }
                if (m.getIdParamStato().equals(cercaStatoPagamento(getEntityManager(), DA_EMETTERE))) {
                    mandatiDaEmettere.add(m);
                }
            } catch(ClassCastException ex){
                getLogger().error(ex.getMessage() + " prova a fare qualche select su db e chiedi info sui parametri di ricerca....");
            }
        }
        if (mandatiDaEmettere != null && !mandatiDaEmettere.isEmpty()) {
            Calendar data = Calendar.getInstance();
            File file = new File(percorso + File.separator + "pag_" + data.get(Calendar.YEAR) + "_" + (data.get(Calendar.MONTH) + 1) + "_" + data.get(Calendar.DAY_OF_MONTH) + "_" + data.get(Calendar.HOUR_OF_DAY) + "_" + data.get(Calendar.MINUTE) + "_" + data.get(Calendar.SECOND) + ".xls");
            FileUtils.writeByteArrayToFile(file, ExportFileUtils.getPagamentiXlsFileData(mandatiDaEmettere));
            JSONObject totale = new JSONObject();
            totale.put("fileGenerato", file.getAbsolutePath());
            response.getWriter().print(totale);
            response.getWriter().close();

        }


    }

    /**
     * Genera un oggetto {@link JSONObject} con i dati previsti dalla lista deli
     * pagamenti visualizzata a seguito di una ricerca.
     *
     * @param mandato Mandato dal quale verranno presi i dati per creare
     *                l'oggetto JSON
     * @return L'oggetto JSON contenente i dati del mandato di pagamento.
     * @throws JSONException
     */
    @Deprecated
    private JSONObject genetaMandatoJSO(Mandato mandato) throws JSONException {
        JSONObject jso = new JSONObject();
        jso.put("id", mandato.getIdMan());
        jso.put("nome", mandato.getPaiIntervento().getPai().getCodAna().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getNome());
        jso.put("cognome", mandato.getPaiIntervento().getPai().getCodAna().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getCognome());
        jso.put("fascia", mandato.getPaiIntervento().getPai().getIdParamFascia().getDesParam());
        jso.put("tipo_intervento", mandato.getPaiIntervento().getTipologiaIntervento().getDesTipint());
        jso.put("importo", mandato.getImporto());
        jso.put("n_mandato", mandato.getNumeroMandato());
        jso.put("stato", mandato.getIdParamStato().getDesParam());
        jso.put("stato", mandato.getIdParamStato().getDesParam());
        jso.put("codice_stato", mandato.getIdParamStato().getIdParam().getCodParam());
        jso.put("azione", "-");
        return (jso);
    }

    final Function mandatoGsonTransformer = new JsonMapTransformer<Mandato>() {
        @Override
        public void transformToMap(Mandato mandato) {
            put("id", mandato.getIdMan());
            PaiIntervento intervento = mandato.getPaiIntervento();
            put("nome", intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getNome());
            put("cognome", intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getCognome());
            put("fascia", intervento.getPai().getIdParamFascia().getDesParam());
            put("tipo_intervento", intervento.getTipologiaIntervento().getDesTipint());
            put("importo", mandato.getImporto());
            put("n_mandato", mandato.getNumeroMandato());
            put("stato", mandato.getIdParamStato().getDesParam());
            put("stato", mandato.getIdParamStato().getDesParam());
            put("codice_stato", mandato.getIdParamStato().getIdParam().getCodParam());
            put("azione", "-");

            try {
                put("delegato", FiltraPerDelegatoJpaPredicateBuilder.getDelegato(intervento));
            } catch (Exception e) {
                getLogger().error(intervento.toString() + " | " + e.getMessage());
                put("delegato", "si e verificato un problema nel determinare il delegato");
            }

        }
    };

    public JsonBuilder aa() {
        return getJsonBuilder(null);
    }

    public class CustomComparator implements Comparator<PaiIntervento> {
        public int compare(PaiIntervento o1, PaiIntervento o2) {
            return o1.getPai().getAnagrafeSoc().getCognome().compareTo(o2.getPai().getAnagrafeSoc().getCognome());
        }
    }

    public class CustomComparatorMandato implements Comparator<Mandato> {
        public int compare(Mandato o1, Mandato o2) {
            return o1.getPaiIntervento().getPai().getAnagrafeSoc().getCognome().compareTo(o2.getPaiIntervento().getPai().getAnagrafeSoc().getCognome());
        }
    }

    List<PaiIntervento> prepara_ed_esegui_select_per_cerca_pagamenti_da_generare(EntityManager entityManager,
                                                                                 JSONObject jsoData,
                                                                                 String tipInt, String uot_struttura,
                                                                                 String filterDelegato,
                                                                                 String mese_di_competenza_mese, String mese_di_competenza_anno,
                                                                                 String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno,
                                                                                 String cognome, String nome) throws JSONException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root r = criteriaQuery.from(PaiIntervento.class);
        ListJoin join = r.joinList("paiInterventoMeseList");
        Join joinDelegato = r.join("dsCodAnaBenef", JoinType.LEFT);
        List<Predicate> elencoFiltriNonNull = new ArrayList<Predicate>();
        Predicate filtro_per_mandato_dettaglio = criteriaBuilder.equal(join.get("idManDettaglio"), criteriaBuilder.nullLiteral(MandatoDettaglio.class));
        if (filtro_per_mandato_dettaglio != null) {
            elencoFiltriNonNull.add(filtro_per_mandato_dettaglio);
        }

        Predicate filtro_per_cons_qta = criteriaBuilder.and(criteriaBuilder.notEqual(join.get("bdgConsQta"), criteriaBuilder.nullLiteral(BigDecimal.class)));
        if (filtro_per_cons_qta != null) {
            elencoFiltriNonNull.add(filtro_per_cons_qta);
        }

        Predicate filtro_per_tipologia_intervento = criteriaBuilder.equal(r.get("tipologiaIntervento").get("flgPagam"), "S");
        if (filtro_per_tipologia_intervento != null) {
            elencoFiltriNonNull.add(filtro_per_tipologia_intervento);
        }

        Predicate filtro_per_codice_tipo_intervento = filtra_per_codice_tipo_intervento(tipInt, criteriaBuilder, r);
        if (filtro_per_codice_tipo_intervento != null) {
            elencoFiltriNonNull.add(filtro_per_codice_tipo_intervento);
        }

        //prendo tutti quegli interventi che siano esecutivi oppure quegli interventi chiusi ma non automaticamente,
        // in questo modo posso anche prevedere i casi in cui l'intervento si conclude ma deve essere ancora liquidato.
        Predicate filtro_per_stato_int = r.get("statoInt").in("C", "E");
        if (filtro_per_stato_int != null) {
            elencoFiltriNonNull.add(filtro_per_stato_int);
        }

        //prendo tutti quelli che sono ancora da generare
        Predicate filtro_per_generato_1 = criteriaBuilder.equal(join.get("generato"), 1);
        if (filtro_per_generato_1 != null) {
            elencoFiltriNonNull.add(filtro_per_generato_1);
        }

        Predicate filtro_per_periodo = filtra_per_periodo(mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, jsoData, criteriaBuilder, join);
        if (filtro_per_periodo != null) {
            elencoFiltriNonNull.add(filtro_per_periodo);
        }

        Predicate filtro_per_uot = filtra_per_uot(uot_struttura, criteriaBuilder, r);
        if (filtro_per_uot != null) {
            elencoFiltriNonNull.add(filtro_per_uot);
        }

        Predicate filtro_per_cognome = filtra_per_cognome(cognome, criteriaBuilder, r);
        if (filtro_per_cognome != null) {
            elencoFiltriNonNull.add(filtro_per_cognome);
        }

        Predicate filtro_per_nome = filtra_per_nome(nome, criteriaBuilder, r);
        if (filtro_per_nome != null) {
            elencoFiltriNonNull.add(filtro_per_nome);
        }

        FiltraPerDelegatoJpaPredicateBuilder filtraPerDelegato = new FiltraPerDelegatoJpaPredicateBuilder(filterDelegato, criteriaBuilder, joinDelegato);
        filtraPerDelegato.invoke();
        Predicate filtro_per_delegato = filtraPerDelegato.getPredicate();

        if (filtro_per_delegato != null) {
            elencoFiltriNonNull.add(filtro_per_delegato);
        }


        Predicate[] filtriDaApplicare = new Predicate[elencoFiltriNonNull.size()];

        for (int i = 0; i < elencoFiltriNonNull.size(); i++) {
            filtriDaApplicare[i] = elencoFiltriNonNull.get(i);
        }


        criteriaQuery.where(filtriDaApplicare);
        criteriaQuery.distinct(true);

        TypedQuery query = entityManager.createQuery(criteriaQuery);

        String[] paroleDelFiltro = filtraPerDelegato.getParoleDelFiltro();
        for (int i = 0; i < paroleDelFiltro.length; i++) {
            query.setParameter("paramDelegato" + i, "%" + paroleDelFiltro[i].toLowerCase() + "%");
        }

        List<PaiIntervento> resultList = query.getResultList();

        return resultList;
    }

    List<Mandato> prepara_ed_esegui_select_per_cerca_pagamenti_eseguiti(EntityManager entityManager, String codTipint, String mese_di_competenza_mese, String mese_di_competenza_anno, String uot_struttura, String cognome, String nome, String filterDelegato, String stato_pagamenti, String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno) {


        String msgTemplate = "codTipint:%s, mese_di_competenza_mese:%s, mese_di_competenza_anno:%s, " +
                "uot_struttura:%s, cognome:%s, nome:%s, filterDelegato:%s" +
                ", stato_pagamenti:%s, " +
                "periodo_considerato_dal_mese:%s, periodo_considerato_al_mese:%s, periodo_considerato_dal_anno:%s, periodo_considerato_al_anno:%s";
        String logMessage = String.format(msgTemplate, codTipint, mese_di_competenza_mese, mese_di_competenza_anno, uot_struttura, cognome, nome, filterDelegato, stato_pagamenti, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);
        getLogger().debug(logMessage);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root tblMandato  = criteriaQuery.from(Mandato.class);

        CercaPagamentiEmessiPredicateBuilder cercaPagamentiEmessiPredicateBuilder =
                new CercaPagamentiEmessiPredicateBuilder(criteriaBuilder, tblMandato , mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_cod_tip_int(codTipint);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_periodo();
        cercaPagamentiEmessiPredicateBuilder.filtra_per_uot(uot_struttura);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_cognome(cognome);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_nome(nome);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_tip_param_uguale_a_fs();
        cercaPagamentiEmessiPredicateBuilder.filtra_per_stato_pagamenti(stato_pagamenti);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_delegato(filterDelegato);

        Predicate[] filtriDaApplicare = cercaPagamentiEmessiPredicateBuilder.getFiltriDaApplicare();
        criteriaQuery.where(filtriDaApplicare);
        criteriaQuery.distinct(true);
        TypedQuery query = entityManager.createQuery(criteriaQuery);
        Map<String, Object> parametri = cercaPagamentiEmessiPredicateBuilder.getParameters();

        Iterator<String> iterator = parametri.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = parametri.get(key);
            query.setParameter(key, value);
        }

        getLogger().debug("prima della select");
        List<Mandato> resultList = query.getResultList();

        return resultList;
    }
}

