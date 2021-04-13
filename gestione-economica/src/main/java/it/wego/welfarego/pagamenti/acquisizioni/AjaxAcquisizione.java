package it.wego.welfarego.pagamenti.acquisizioni;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.pagamenti.FiltraPerDelegatoJpaPredicateBuilder;
import it.wego.welfarego.pagamenti.fatture.FattureDao;
import it.wego.welfarego.pagamenti.fatture.FattureService;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiInterventoPK;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiCdg;
import it.wego.welfarego.persistence.entities.PaiCdgPK;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoMesePK;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.lang3.StringUtils;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller per le chiamate AJAX riguardanti la gestione delle acquisizioni.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
@Transactional
public class AjaxAcquisizione extends AbstractAjaxController {

    /**
     * Carica dalla base di dati la lista degli interventi filtrati sui dati
     * trasmessi dal browser.
     *
     * @param request  Richiesta del browser contenente i dati per il filtro di
     *                 ricerca.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaAcquisizioni")
    public void cercaAcquisizioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        String mese_di_competenza_mese = request.getParameter("mese_di_competenza_mese");
        String mese_di_competenza_anno = request.getParameter("mese_di_competenza_anno");
        String periodo_considerato_dal_mese = request.getParameter("periodo_considerato_dal_mese");
        String periodo_considerato_al_mese = request.getParameter("periodo_considerato_al_mese");
        String periodo_considerato_dal_anno = request.getParameter("periodo_considerato_dal_anno");
        String periodo_considerato_al_anno = request.getParameter("periodo_considerato_al_anno");
        String uot_struttura = request.getParameter("uot_struttura");
        String cognome = request.getParameter("cognome");
        String nome = request.getParameter("nome");
        String codTipInt = request.getParameter("tipo_intervento");
        String filterDelegato = request.getParameter("filtro_delegato");

        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        jsonBuilder = jsonBuilder.withWriter(response.getWriter());
        jsonBuilder = jsonBuilder.withParameters(request.getParameterMap());

        JSONObject jsoData = new JSONObject();

        List result = prepara_ed_esegui_select(getEntityManager(), codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterDelegato, jsoData);

        List<Object> risultatiAccorpati = accorpa_risultati(mese_di_competenza_mese, mese_di_competenza_anno, result);

        for (Object o : risultatiAccorpati) {
            Map record = fill_record(jsoData, o);
            jsonBuilder.addRecord(record);
        }

        jsonBuilder.buildStoreResponse();
    }

    private Map fill_record(JSONObject jsoData, Object o) throws JSONException {
        Object[] oggetto = (Object[]) o;

        BigDecimal previstoEuro = null;
        BigDecimal previstoQta = null;
        if (oggetto.length > 21) {
            previstoEuro = (BigDecimal) oggetto[21];
        }
        if (oggetto.length > 22) {
            previstoEuro = (BigDecimal) oggetto[22];
        }
        if (oggetto.length > 23) {
            BigDecimal previstoEur = (BigDecimal) oggetto[21];
            BigDecimal consuntivoEur = (BigDecimal) oggetto[23];
            if (consuntivoEur != null && !(consuntivoEur.compareTo(previstoEur) == 0)) {
                previstoEuro = consuntivoEur;
            } else {
                previstoEuro = previstoEur;
            }
        }
        if (oggetto.length > 24) {
            BigDecimal previstoQt = (BigDecimal) oggetto[22];
            BigDecimal consuntivoQta = (BigDecimal) oggetto[24];
            if (consuntivoQta != null && !(consuntivoQta.compareTo(previstoQt) == 0)) {
                previstoQta = consuntivoQta;
            } else {
                previstoQta = previstoQt;
            }
        }

        Map record = Maps.newHashMap();
        Map recordId = Maps.newHashMap();
        recordId.put("codPai", (Integer) oggetto[0]);
        recordId.put("codTipint", (String) oggetto[5]);
        recordId.put("cntTipint", (Integer) oggetto[4]);
        recordId.put("periodoRicerca", jsoData.toString());
        record.put("id", JsonBuilder.getGson().toJson(recordId));
        record.put("nome", (String) oggetto[1]);
        record.put("cognome", (String) oggetto[2]);
        record.put("uot_struttura", (String) oggetto[3]);
        record.put("tipo_intervento", (String) oggetto[8]);
        try {
            record.put("data_avvio", oggetto[12] != null ? FORMATO_DATA.format((Date) oggetto[12]) : "");
        } catch (ArrayIndexOutOfBoundsException e) {
            getLogger().error("Errore  di java noto su simple date format");

            record.put("data_avvio", oggetto[12] != null ? new SimpleDateFormat("dd/mm/yyyy").format((Date) oggetto[12]) : "");
        }
        try {
            record.put("data_inizio", oggetto[6] != null ? FORMATO_DATA.format((Date) oggetto[6]) : "");
        } catch (ArrayIndexOutOfBoundsException e) {
            getLogger().error("Errore  di java noto su simple date format");

            record.put("data_inizio", oggetto[6] != null ? new SimpleDateFormat("dd/mm/yyyy").format((Date) oggetto[6]) : "");
        }

        String durata = "";
        if (oggetto[14] != null) {
            durata = Integer.toString((Integer) oggetto[14]) + " settimane";
        } else {
            if (oggetto[13] != null) {
                durata = Integer.toString((Integer) oggetto[13]) + " mesi";
            }
        }
        record.put("durata_mesi", durata);
        if (previstoEuro != null && previstoQta != null) {
            record.put("previsto_euro", previstoEuro);
            record.put("previsto_quantita", previstoQta);
        } else {
            previstoEuro = BigDecimal.ZERO;
            previstoQta = BigDecimal.ZERO;
            FattureDao fattureDao = new FattureDao();

            List<PaiInterventoMese> pims = fattureDao.getPaiInterventoMeseFromId((String) record.get("id"));

            for (PaiInterventoMese i : pims) {
                previstoEuro = previstoEuro.add(i.getBdgConsEur() != null ? i.getBdgConsEur() : i.getBdgPrevEur());
                previstoQta = previstoQta.add(i.getBdgConsQta() != null ? i.getBdgConsQta() : i.getBdgPrevQta());
            }
            record.put("previsto_euro", previstoEuro);
            record.put("previsto_quantita", previstoQta);
        }
        try {
            record.put("data_fine", oggetto[7] != null ? FORMATO_DATA.format((Date) oggetto[7]) : "");
        } catch (ArrayIndexOutOfBoundsException e) {
            getLogger().error("Errore  di java noto su simple date format");

            record.put("data_fine", oggetto[7] != null ? new SimpleDateFormat("dd/mm/yyyy").format((Date) oggetto[7]) : "");
        }
        record.put("fascia", (String) oggetto[11]);


        try {
            record.put("data_isee", oggetto[15] != null ? FORMATO_DATA.format((Date) oggetto[15]) : "");
        } catch (ArrayIndexOutOfBoundsException e) {
            getLogger().error("Errore  di java noto su simple date format");

            record.put("data_isee", oggetto[15] != null ? new SimpleDateFormat("dd/mm/yyyy").format((Date) oggetto[15]) : "");
        }
        try {
            record.put("data_apertura", oggetto[16] != null ? FORMATO_DATA.format((Date) oggetto[16]) : "");
        } catch (ArrayIndexOutOfBoundsException e) {
            getLogger().error("Errore  di java noto su simple date format");

            record.put("data_apertura", oggetto[16] != null ? new SimpleDateFormat("dd/mm/yyyy").format((Date) oggetto[16]) : "");
        }
        record.put("tipo_pagfat", Objects.equal((Character) oggetto[10], TipologiaIntervento.FLG_PAGAMENTO_S) ? "pagamento"
                : (Objects.equal((Character) oggetto[9], TipologiaIntervento.FLG_FATTURA_S) ? "fatturazione" : "?"));

        record.put("delegato", AnagrafeSoc.PERSONA_FISICA_F.equals(oggetto[17]) ? oggetto[19] + " " + oggetto[18] : oggetto[20]);

        return record;
    }

    private List<Object> accorpa_risultati(String mese_di_competenza_mese, String mese_di_competenza_anno, List result) {
        HashMap<PaiInterventoPK, Object> cippa = new HashMap<PaiInterventoPK, Object>();
        List<Object> risultatiAccorpati = new ArrayList<Object>();

        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            for (Object o : result) {
                Object[] oggetto = (Object[]) o;
                //mi son creato la chiave del mio bel paiintervento..
                PaiInterventoPK chiave = new PaiInterventoPK((Integer) oggetto[0], (String) oggetto[5], (Integer) oggetto[4]);
                //vedo se criteriaQuery'è gia un pim
                Object fm = cippa.get(chiave);
                if (fm != null) {
                    Object[] fmOggetti = (Object[]) fm;
                    if (oggetto.length > 21) {
                        fmOggetti[21] = ((BigDecimal) (oggetto[21])).add((BigDecimal) fmOggetti[21]);
                    }
                    if (oggetto.length > 22) {
                        fmOggetti[22] = ((BigDecimal) (oggetto[22])).add((BigDecimal) fmOggetti[22]);
                    }
                    if (oggetto.length > 23) {

                        BigDecimal consuntivoPrecedente = (BigDecimal) fmOggetti[23];
                        BigDecimal consuntivoEurAttuale = (BigDecimal) oggetto[23];
                        if (consuntivoEurAttuale != null && consuntivoPrecedente != null) {
                            fmOggetti[23] = ((BigDecimal) (oggetto[23])).add((BigDecimal) fmOggetti[23]);
                        } else if (consuntivoEurAttuale == null && consuntivoPrecedente == null) {
                            fmOggetti[23] = null;
                        } else {
                            fmOggetti[23] = MoreObjects.firstNonNull(consuntivoEurAttuale, consuntivoPrecedente);
                        }
                    }
                    if (oggetto.length > 24) {
                        BigDecimal consuntivoPrecedente = (BigDecimal) fmOggetti[24];
                        BigDecimal consuntivoEurAttuale = (BigDecimal) oggetto[24];
                        if (consuntivoEurAttuale != null && consuntivoPrecedente != null) {
                            fmOggetti[24] = ((BigDecimal) (oggetto[24])).add((BigDecimal) fmOggetti[24]);
                        } else if (consuntivoEurAttuale == null && consuntivoPrecedente == null) {
                            fmOggetti[24] = null;
                        } else {
                            fmOggetti[24] = MoreObjects.firstNonNull(consuntivoEurAttuale, consuntivoPrecedente);
                        }
                    }
                    //si vede che criteriaQuery'è qualcosa da fareeeeee
                } else {
                    //prendiamo la chiave e la associamo joinAnagrafeSoc questo oggetto
                    cippa.put(chiave, o);
                }
            }
            risultatiAccorpati.addAll(cippa.values());
        } else {
            risultatiAccorpati = result;
        }
        return risultatiAccorpati;
    }

    List prepara_ed_esegui_select(EntityManager entityManager, String codTipInt, String mese_di_competenza_mese, String mese_di_competenza_anno, String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno, String uot_struttura, String cognome, String nome, String filterDelegato, JSONObject jsoData) throws JSONException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root r = criteriaQuery.from(Pai.class);
        Join cs = r.join("codAna", JoinType.INNER);
        Join joinAnagrafeSoc = cs.join("anagrafeSoc", JoinType.INNER);
        Join pin = r.join("idParamUot", JoinType.INNER);
        ListJoin paiInterventoList = r.joinList("paiInterventoList", JoinType.INNER);
        ListJoin pim = paiInterventoList.joinList("paiInterventoMeseList", JoinType.INNER);
        Join joinTipologiaIntervento = paiInterventoList.join("tipologiaIntervento", JoinType.INNER);
        Join joinFattDettaglio = pim.join("idFattDettaglio", JoinType.LEFT);
        Join fa = joinFattDettaglio.join("idFatt", JoinType.LEFT);
        Join joinManDettaglio = pim.join("idManDettaglio", JoinType.LEFT);
        Join ma = joinManDettaglio.join("idMan", JoinType.LEFT);
        Join joinParamFascia = pim.join("idParamFascia", JoinType.LEFT);
        Join joinDelegato = paiInterventoList.join("dsCodAnaBenef", JoinType.LEFT);
        //query dioiverse in base al fatto che si sia scelto di visualizzare il mese di competenza o un periodo

        prepara_CriteriaQuery(criteriaQuery, r, joinAnagrafeSoc, pin, paiInterventoList, pim, joinTipologiaIntervento, joinParamFascia, joinDelegato, mese_di_competenza_mese, mese_di_competenza_anno);


        List<javax.persistence.criteria.Predicate> filtri_da_applicare_as_list = new ArrayList<javax.persistence.criteria.Predicate>();


        javax.persistence.criteria.Predicate filtro_per_stato_intervento = paiInterventoList.get("statoInt").in("C", "E");
        filtri_da_applicare_as_list.add(filtro_per_stato_intervento);

        javax.persistence.criteria.Predicate filtro_escludi_la_cassa_a_mani = escludi_la_cassa_a_mani(criteriaBuilder, paiInterventoList);
        filtri_da_applicare_as_list.add(filtro_escludi_la_cassa_a_mani);

        javax.persistence.criteria.Predicate filtro_per_tipo_intervento = filtra_per_tipo_intervento(codTipInt, criteriaBuilder, paiInterventoList);
        if (filtro_per_tipo_intervento != null) {
            filtri_da_applicare_as_list.add(filtro_per_tipo_intervento);
        }

        javax.persistence.criteria.Predicate filtro_per_periodo = filtra_per_periodo(jsoData, criteriaBuilder, pim, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);
        if (filtro_per_periodo != null) {
            filtri_da_applicare_as_list.add(filtro_per_periodo);
        }

        javax.persistence.criteria.Predicate filtro_per_uot = filtra_per_uot(criteriaBuilder, pin, uot_struttura);
        if (filtro_per_uot != null) {
            filtri_da_applicare_as_list.add(filtro_per_uot);
        }

        javax.persistence.criteria.Predicate filtro_per_cognome = filtra_per_cognome(criteriaBuilder, joinAnagrafeSoc, cognome);
        if (filtro_per_cognome != null) {
            filtri_da_applicare_as_list.add(filtro_per_cognome);
        }

        javax.persistence.criteria.Predicate filtro_per_nome = filtra_per_nome(criteriaBuilder, joinAnagrafeSoc, nome);
        if (filtro_per_nome != null) {
            filtri_da_applicare_as_list.add(filtro_per_nome);
        }

        javax.persistence.criteria.Predicate filtro_per_fattura_dettaglio = criteriaBuilder.equal(joinFattDettaglio.get("idFattDettaglio"), criteriaBuilder.nullLiteral(FatturaDettaglio.class));
        if (filtro_per_fattura_dettaglio != null) {
            filtri_da_applicare_as_list.add(filtro_per_fattura_dettaglio);
        }

        javax.persistence.criteria.Predicate filtro_per_mandatoDettaglio = criteriaBuilder.equal(joinManDettaglio.get("idManDettaglio"), criteriaBuilder.nullLiteral(MandatoDettaglio.class));
        if (filtro_per_mandatoDettaglio != null) {
            filtri_da_applicare_as_list.add(filtro_per_mandatoDettaglio);
        }

        javax.persistence.criteria.Predicate filtro_per_pim_generato = criteriaBuilder.or(criteriaBuilder.equal(pim.get("generato"), 1), criteriaBuilder.equal(pim.get("generato"), 2));
        if (filtro_per_pim_generato != null) {
            filtri_da_applicare_as_list.add(filtro_per_pim_generato);
        }

        javax.persistence.criteria.Predicate filtro_per_data_esec = paiInterventoList.get("dtEsec").isNotNull(); // deve essere entrato in esecutività (ANCHE SE CHIUSO)
        if (filtro_per_data_esec != null) {
            filtri_da_applicare_as_list.add(filtro_per_data_esec);
        }


        FiltraPerDelegatoJpaPredicateBuilder filtraPerDelegato = new FiltraPerDelegatoJpaPredicateBuilder(filterDelegato, criteriaBuilder, joinDelegato);
        filtraPerDelegato = filtraPerDelegato.invoke();
        javax.persistence.criteria.Predicate filtro_per_delegato = filtraPerDelegato.getPredicate();


        if (filtro_per_delegato != null) {
            filtri_da_applicare_as_list.add(filtro_per_delegato);
        }

        javax.persistence.criteria.Predicate[] filtri_da_applicare = new javax.persistence.criteria.Predicate[filtri_da_applicare_as_list.size()];
        for (int i = 0; i < filtri_da_applicare_as_list.size(); i++) {
            filtri_da_applicare[i] = filtri_da_applicare_as_list.get(i);
        }

        javax.persistence.criteria.Predicate predicate = criteriaBuilder.and(filtri_da_applicare);

        criteriaQuery.where(predicate);

        ArrayList<Order> order = new ArrayList<Order>();
        criteriaQuery.orderBy(order);

        Query query = entityManager.createQuery(criteriaQuery);

        String[] paroleDelFiltro = filtraPerDelegato.getParoleDelFiltro();
        for (int i = 0; i < paroleDelFiltro.length; i++) {
            query.setParameter("paramDelegato" + i, "%" + paroleDelFiltro[i].toLowerCase() + "%");
        }

        return query.getResultList();
    }

    private javax.persistence.criteria.Predicate filtra_per_nome(CriteriaBuilder criteriaBuilder, Join a, String nome) {
        javax.persistence.criteria.Predicate predicate = null;
        nome = Strings.emptyToNull(nome);
        if (nome != null) {
            nome = nome.toUpperCase();
            predicate = criteriaBuilder.like(a.get("nome"), nome + "%");
        }
        return predicate;
    }

    private javax.persistence.criteria.Predicate filtra_per_cognome(CriteriaBuilder criteriaBuilder, Join a, String cognome) {
        javax.persistence.criteria.Predicate predicate = null;
        cognome = Strings.emptyToNull(cognome);
        if (cognome != null) {
            cognome = cognome.toUpperCase();
            predicate = criteriaBuilder.like(a.get("cognome"), cognome + "%");
        }
        return predicate;
    }

    private javax.persistence.criteria.Predicate filtra_per_uot(CriteriaBuilder criteriaBuilder, Join pin, String uot_struttura) {
        javax.persistence.criteria.Predicate predicate = null;
        String paramUotStr = Strings.emptyToNull(uot_struttura);
        if (paramUotStr != null) {
            try {
                predicate = criteriaBuilder.equal(pin.get("idParamIndata"), Integer.parseInt(paramUotStr));
            } catch (NumberFormatException ex) {
                getLogger().warn("", ex);
                //L'id dell'uot non è in unmerico, eseguo la ricerca ignorando il parametro errato.
            }
        }
        return predicate;
    }

    private javax.persistence.criteria.Predicate filtra_per_periodo(JSONObject jsoData, CriteriaBuilder criteriaBuilder,
                                         ListJoin pim, String mese_di_competenza_mese, String mese_di_competenza_anno,
                                         String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno) throws JSONException {

        javax.persistence.criteria.Predicate predicate = null;
        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            try {
                javax.persistence.criteria.Predicate mese = criteriaBuilder.equal(pim.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(mese_di_competenza_mese));
                javax.persistence.criteria.Predicate anno = criteriaBuilder.equal(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(mese_di_competenza_anno));
                predicate = criteriaBuilder.and(mese, anno);
                jsoData.put("meseEff", Integer.parseInt(mese_di_competenza_mese));
                jsoData.put("annoEff", Integer.parseInt(mese_di_competenza_anno));
            } catch (NumberFormatException ex) {
                getLogger().warn("", ex);
                //Le date passate sono in formato non valido, eseguo la ricerca ignorandole.
            }
        } else {
            if (periodo_considerato_dal_mese != null && periodo_considerato_al_mese != null && periodo_considerato_dal_anno != null && periodo_considerato_al_anno != null) {
                try {
                    int dalMese = Integer.parseInt(periodo_considerato_dal_mese);
                    int dalAnno = Integer.parseInt(periodo_considerato_dal_anno);
                    int alMese = Integer.parseInt(periodo_considerato_al_mese);
                    int alAnno = Integer.parseInt(periodo_considerato_al_anno);

                    javax.persistence.criteria.Predicate meseDal_ge = criteriaBuilder.ge(pim.get("paiInterventoMesePK").get("meseEff"), dalMese);
                    javax.persistence.criteria.Predicate annoDal_eq = criteriaBuilder.equal(pim.get("paiInterventoMesePK").get("annoEff"), dalAnno);
                    javax.persistence.criteria.Predicate annoDal2_gt = criteriaBuilder.greaterThan(pim.get("paiInterventoMesePK").get("annoEff"), dalAnno);

                    javax.persistence.criteria.Predicate meseAl_le = criteriaBuilder.le(pim.get("paiInterventoMesePK").get("meseEff"), alMese);
                    javax.persistence.criteria.Predicate annoAl_eq = criteriaBuilder.equal(pim.get("paiInterventoMesePK").get("annoEff"), alAnno);
                    javax.persistence.criteria.Predicate annoAl2_lt = criteriaBuilder.lessThan(pim.get("paiInterventoMesePK").get("annoEff"), alAnno);

                    javax.persistence.criteria.Predicate dal = criteriaBuilder.or(annoDal2_gt, criteriaBuilder.and(meseDal_ge, annoDal_eq));
                    javax.persistence.criteria.Predicate al = criteriaBuilder.or(annoAl2_lt, criteriaBuilder.and(meseAl_le, annoAl_eq));

                    if (predicate != null) {
                        predicate = criteriaBuilder.and(predicate, dal, al);
                    } else {
                        predicate = criteriaBuilder.and(dal, al);
                    }

                    jsoData.put("meseEffDal", dalMese);
                    jsoData.put("annoEffDal", dalAnno);
                    jsoData.put("meseEffAl", alMese);
                    jsoData.put("annoEffAl", alAnno);
                } catch (NumberFormatException ex) {
                    getLogger().warn("", ex);
                    //Le date passate sono in formato non valido, eseguo la ricerca ignorandole.
                }
            }
        }

        return predicate;
    }

    private javax.persistence.criteria.Predicate escludi_la_cassa_a_mani(CriteriaBuilder criteriaBuilder, ListJoin paiInterventoList) {
        return criteriaBuilder.notEqual(paiInterventoList.get("paiInterventoPK").get("codTipint"), "AD009");
    }

    private javax.persistence.criteria.Predicate filtra_per_tipo_intervento(String codTipInt, CriteriaBuilder criteriaBuilder, ListJoin paiInterventoList) {
        javax.persistence.criteria.Predicate predicate = null;
        if (!Strings.isNullOrEmpty(codTipInt)) {
            predicate = criteriaBuilder.equal(paiInterventoList.get("paiInterventoPK").get("codTipint"), codTipInt);
        }
        return predicate;
    }

    private void prepara_CriteriaQuery(CriteriaQuery criteriaQuery, Root r, Join a, Join pin, ListJoin paiInterventoList, ListJoin pim, Join joinTipologiaIntervento, Join joinParamFascia, Join joinDelegato, String mese_di_competenza_mese, String mese_di_competenza_anno) {
        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            criteriaQuery.multiselect(r.get("codPai"),
                    a.get("nome"),
                    a.get("cognome"),
                    pin.get("desParam"),
                    paiInterventoList.get("paiInterventoPK").get("cntTipint"),
                    paiInterventoList.get("paiInterventoPK").get("codTipint"),

                    paiInterventoList.get("dtEsec"),
                    paiInterventoList.get("dtChius"),

                    joinTipologiaIntervento.get("desTipint"),
                    joinTipologiaIntervento.get("flgFatt"),
                    joinTipologiaIntervento.get("flgPagam"),
                    joinParamFascia.get("desParam"),
                    paiInterventoList.get("dtAvvio"),
                    paiInterventoList.get("durMesi"),
                    paiInterventoList.get("durSettimane"),
                    r.get("dtScadIsee"),
                    paiInterventoList.get("dtApe"),
                    joinDelegato.get("flgPersFg"),
                    joinDelegato.get("nome"),
                    joinDelegato.get("cognome"),
                    joinDelegato.get("ragSoc"),
                    pim.get("bdgPrevEur"),
                    pim.get("bdgPrevQta"),
                    pim.get("bdgConsEur"),
                    pim.get("bdgConsQta")
            );
        } else {
            criteriaQuery.multiselect(
                    r.get("codPai"),
                    a.get("nome"),
                    a.get("cognome"),
                    pin.get("desParam"),
                    paiInterventoList.get("paiInterventoPK").get("cntTipint"),
                    paiInterventoList.get("paiInterventoPK").get("codTipint"),

                    paiInterventoList.get("dtEsec"),
                    paiInterventoList.get("dtChius"),

                    joinTipologiaIntervento.get("desTipint"),
                    joinTipologiaIntervento.get("flgFatt"),
                    joinTipologiaIntervento.get("flgPagam"),
                    joinParamFascia.get("desParam"),
                    paiInterventoList.get("dtAvvio"),
                    paiInterventoList.get("durMesi"),
                    paiInterventoList.get("durSettimane"),
                    r.get("dtScadIsee"),
                    paiInterventoList.get("dtApe"),

                    joinDelegato.get("flgPersFg"),
                    joinDelegato.get("nome"),
                    joinDelegato.get("cognome"),
                    joinDelegato.get("ragSoc")

            );
        }
        criteriaQuery.distinct(true);
    }

    /**
     * Carica dalla base di dati il dettaglio per mese di un
     * {@link PaiInterventoMese}
     *
     * @param request  Richiesta del browser contenente la stringa rappresentante
     *                 l'identificativo dell'intervento e le date corrispondenti di riferimento
     *                 indicate dall'utente.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "dettaglioAcquisizioni")
    public void dettaglioAcquisizioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        FattureDao fattureDao = new FattureDao();
        List<PaiInterventoMese> paiInterventoMeseList = fattureDao.getPaiInterventoMeseFromId( request.getParameter("id"));

        JSONArray jsonArray = new JSONArray();
        for (PaiInterventoMese paiInterventoMese : paiInterventoMeseList) {
            JSONObject paiInterventoMeseJson = generaPaiInterventoMeseJSON(paiInterventoMese);
            jsonArray.put(paiInterventoMeseJson);
        }

        this.mandaRisposta(response, jsonArray, null);
    }

    /**
     * Modifica le quantità <strong>erogata</strong> e <strong>beneficiata
     * </strong> dell'intervento selezionato
     *
     * @param request  Richiesta del browser contenente la stringa rappresentante
     *                 il record delle quantità da modificare e i nuovi valori.
     * @param response Risposta al browser (oggetto vuoto in caso di successo)
     * @param session  Sessione dalla quale recuperare l'iddentificativo dell'
     *                 utente connesso.
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "modificaAcquisizioni")
    @Transactional
    public void modificaAcquisizioni(ResourceRequest request, ResourceResponse response, PortletSession session) throws IOException, JSONException, Exception {
        ParametriIndataDao parametriIndataDao = new ParametriIndataDao(getEntityManager());
        String nonProseguire = Strings.emptyToNull(request.getParameter("nonProseguire"));
        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter()).withParameters(request.getParameterMap());
        Query q = getEntityManager().createNamedQuery("Utenti.findByUsername");
        q.setParameter("username", UserLocalServiceUtil.getUserById(Long.parseLong(request.getRemoteUser())).getScreenName());

        Utenti utente = (Utenti) q.getSingleResult();

        List<Map<String, String>> list = JsonBuilder.getGson().fromJson(request.getParameter("data"), JsonBuilder.LIST_OF_MAP_OF_STRINGS);
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("json request = {}", JsonBuilder.getGsonPrettyPrinting().toJson(list));
        }

        for (Map<String, String> jsonPaiInterventoMese : list) {

            JSONObject jsoIdPaiInterventoMese = new JSONObject((String) jsonPaiInterventoMese.get("id"));

            PaiInterventoMeseDao dao = new PaiInterventoMeseDao(getEntityManager());
            PaiInterventoMese paiInterventoMese = null;
            if (!jsoIdPaiInterventoMese.get("codImp").toString().equals(jsonPaiInterventoMese.get("codImp").toString())) {
                PaiInterventoMese old = dao.findByKey(jsoIdPaiInterventoMese.get("codPai").toString(),
                        jsoIdPaiInterventoMese.get("codTipint").toString(),
                        jsoIdPaiInterventoMese.get("cntTipint").toString(),
                        jsoIdPaiInterventoMese.get("annoEff").toString(),
                        jsoIdPaiInterventoMese.get("meseEff").toString(),
                        jsoIdPaiInterventoMese.get("anno").toString(),
                        jsoIdPaiInterventoMese.get("codImp").toString());
                BudgetTipoInterventoDao bdao = new BudgetTipoInterventoDao(getEntityManager());
                PaiInterventoMesePK newPk = new PaiInterventoMesePK(old.getPaiInterventoMesePK().getCodPai(), old.getPaiInterventoMesePK().getCodTipint(), old.getPaiInterventoMesePK().getCntTipint(), old.getPaiInterventoMesePK().getAnnoEff(), old.getPaiInterventoMesePK().getMeseEff(), Short.parseShort(jsonPaiInterventoMese.get("anno").toString()), jsonPaiInterventoMese.get("codImp").toString());
                paiInterventoMese = new PaiInterventoMese(newPk);
                paiInterventoMese.setFlgProp('N');
                paiInterventoMese.setBdgPrevEur(old.getBdgPrevEur());
                paiInterventoMese.setBdgPrevQta(old.getBdgPrevQta());
                paiInterventoMese.setPaiIntervento(old.getPaiIntervento());
                paiInterventoMese.setBudgetTipIntervento(bdao.findByKey(old.getPaiInterventoMesePK().getCodTipint(), jsoIdPaiInterventoMese.get("anno").toString(), jsoIdPaiInterventoMese.get("codImp").toString()));
                getEntityManager().remove(old);
                getEntityManager().persist(paiInterventoMese);

            } else {
                paiInterventoMese = dao.findByKey(jsoIdPaiInterventoMese.get("codPai").toString(),
                        jsoIdPaiInterventoMese.get("codTipint").toString(),
                        jsoIdPaiInterventoMese.get("cntTipint").toString(),
                        jsoIdPaiInterventoMese.get("annoEff").toString(),
                        jsoIdPaiInterventoMese.get("meseEff").toString(),
                        jsoIdPaiInterventoMese.get("anno").toString(),
                        jsoIdPaiInterventoMese.get("codImp").toString());
            }

            BigDecimal costoIntervento = paiInterventoMese.getPaiIntervento().getImportoStandard();

            BigDecimal quantitaErogataOrig = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsQta(), paiInterventoMese.getBdgPrevQta()),
                    quantitaBeneficiataOrig = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsQtaBenef(), paiInterventoMese.getBdgPrevQta()),
                    bdgConsEurOrig = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsEur(), paiInterventoMese.getBdgPrevEur());

            getLogger().debug("orig (quantitaErogata,quantitaBeneficiat,bdgConsEur) : {} , {} , {}", new Object[]{quantitaErogataOrig, quantitaBeneficiataOrig, bdgConsEurOrig});

            String quantitaErogataNewStr = Strings.emptyToNull(jsonPaiInterventoMese.get("qt_erogata")),
                    quantitaBeneficiataNewStr = Strings.emptyToNull(jsonPaiInterventoMese.get("qt_beneficiata")),
                    bdgConsEurNewStr = Strings.emptyToNull(jsonPaiInterventoMese.get("bdgConsEur"));
            BigDecimal quantitaErogataNew = quantitaErogataNewStr == null ? BigDecimal.ZERO : new BigDecimal(quantitaErogataNewStr),
                    quantitaBeneficiataNew = quantitaBeneficiataNewStr == null ? BigDecimal.ZERO : new BigDecimal(quantitaBeneficiataNewStr),
                    bdgConsEurNew = bdgConsEurNewStr == null ? BigDecimal.ZERO : new BigDecimal(bdgConsEurNewStr);
            getLogger().debug("request (quantitaErogata,quantitaBeneficiat,bdgConsEur) : {} , {} , {}", new Object[]{quantitaErogataNew, quantitaBeneficiataNew, bdgConsEurNew});

            if (Objects.equal(bdgConsEurNew, bdgConsEurOrig) && !Objects.equal(quantitaErogataNew, quantitaErogataOrig)) {
                getLogger().debug("update euro da qt");
                bdgConsEurNew = quantitaErogataNew.multiply(costoIntervento);
            } else if (!Objects.equal(bdgConsEurNew, bdgConsEurOrig) && Objects.equal(quantitaErogataNew, quantitaErogataOrig)) {
                getLogger().debug("update qt da euro");
                quantitaErogataNew = bdgConsEurNew.divide(costoIntervento, MathContext.DECIMAL32);
            } else {
                Preconditions.checkArgument(!(!Objects.equal(bdgConsEurNew, bdgConsEurOrig)
                        && !Objects.equal(quantitaErogataNew, quantitaErogataOrig)), "non e' possibile variare contemporaneamente consultivo euro e quantita'");
                getLogger().debug("no update qt/eur");
            }

            getLogger().debug("new (quantitaErogata,quantitaBeneficiat,bdgConsEur) : {} , {} , {}", new Object[]{quantitaErogataNew, quantitaBeneficiataNew, bdgConsEurNew});
            paiInterventoMese.setBdgConsEur(bdgConsEurNew);
            paiInterventoMese.setBdgConsQta(quantitaErogataNew);
            paiInterventoMese.setBdgConsQtaBenef(quantitaBeneficiataNew);
            jsonPaiInterventoMese.put("bdgConsEur", bdgConsEurNew.toString());
            jsonPaiInterventoMese.put("qt_erogata", quantitaErogataNew.toString());
            jsonPaiInterventoMese.put("qt_beneficiata", quantitaBeneficiataNew.toString());

            String variazioneStraordinariaStr = Strings.emptyToNull(jsonPaiInterventoMese.get("variazione_straordinaria"));
            if (variazioneStraordinariaStr != null) {
                BigDecimal variazioneStraordinaria = new BigDecimal(variazioneStraordinariaStr);
                paiInterventoMese.setBdgConsVar(variazioneStraordinaria);
            }
            String assenzeStr = Strings.emptyToNull(jsonPaiInterventoMese.get("assenze"));
            BigDecimal assenze = assenzeStr == null ? null : new BigDecimal(assenzeStr);
            paiInterventoMese.setGgAssenza(assenze);
            //scontiamo le assenze 
            if (paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgPagam() == 'S') {
                final PaiInterventoMese questoMese = paiInterventoMese;
                final Comparator<PaiInterventoMese> chronologicalComparator = new Comparator<PaiInterventoMese>() {
                    public int compare(PaiInterventoMese o1, PaiInterventoMese o2) {
                        return ComparisonChain.start()
                                .compare(o1.getPaiInterventoMesePK().getAnnoEff(), o2.getPaiInterventoMesePK().getAnnoEff())
                                .compare(o1.getPaiInterventoMesePK().getMeseEff(), o2.getPaiInterventoMesePK().getMeseEff())
                                .result();
                    }
                };
                //mi prendo tutti i pai intervento mese
                List<PaiInterventoMese> tuttiiMesi = paiInterventoMese.getPaiIntervento().getPaiInterventoMeseList();
                BigDecimal assenzeMensili = BigDecimal.ZERO;
                BigDecimal assenzeTotali = BigDecimal.ZERO;
                final BigDecimal bd50 = new BigDecimal(60);
                final BigDecimal bd30 = new BigDecimal(30);
                assenzeTotali.add(assenzeMensili);
                for (PaiInterventoMese record : Iterables.filter(tuttiiMesi, new Predicate<PaiInterventoMese>() {
                    public boolean apply(PaiInterventoMese input) {
                        return chronologicalComparator.compare(input, questoMese) == 0;
                    }

                }))
                    getLogger().debug("assenze mensili (mese corrente) : {}", assenzeMensili);
                assenzeMensili = assenzeMensili.add(MoreObjects.firstNonNull(paiInterventoMese.getGgAssenza(), BigDecimal.ZERO));
                assenzeTotali.add(assenzeMensili);
                BigDecimal assenzeNonScalate = BigDecimal.ZERO;
                for (PaiInterventoMese record : Iterables.filter(tuttiiMesi, new Predicate<PaiInterventoMese>() {
                    public boolean apply(PaiInterventoMese input) {
                        return chronologicalComparator.compare(input, questoMese) < 0;
                    }

                 })) {
                    assenzeNonScalate = assenzeNonScalate.add(MoreObjects.firstNonNull(record.getGgAssenza(), BigDecimal.ZERO)).min(bd50);
                }
                BigDecimal assenzeDaScalare = assenzeNonScalate.add(assenzeMensili).subtract(bd50).max(BigDecimal.ZERO);
                getLogger().debug("assenze non scalate fino al mese scorso : {}", assenzeNonScalate);
                getLogger().debug("assenze da scalare questo mese : {}", assenzeDaScalare);
                BigDecimal costoTotaleBase = MoreObjects.firstNonNull(paiInterventoMese.getBdgConsEur(), paiInterventoMese.getBdgPrevEur()),
                        costoTotaleEffettivo = costoTotaleBase;
                if (assenzeDaScalare.compareTo(BigDecimal.ZERO) > 0) {
                    // faccio i tre casi
                    //se il consuntivo è maggiore del preventivo
                    if (costoTotaleBase.compareTo(paiInterventoMese.getBdgPrevEur()) > 0) {
                        // applico la formula sul preventivo

                        BigDecimal daSottrarre = paiInterventoMese.getBdgPrevEur().multiply(assenzeDaScalare).divide(bd30, MathContext.DECIMAL32);
                        getLogger().info("Costo  previsto maggiore del preventivo... applico la formula e sottraggo " + daSottrarre);
                        costoTotaleEffettivo = costoTotaleEffettivo.subtract(daSottrarre);
                    } else if (costoTotaleBase.compareTo(paiInterventoMese.getBdgPrevEur()) == 0) {
                        getLogger().info("Costo previsto uguale al preventivo applico la formula al costo previsto ");
                        costoTotaleEffettivo = costoTotaleEffettivo.subtract(costoTotaleEffettivo.multiply(assenzeDaScalare).divide(bd30, MathContext.DECIMAL32));
                    } else {
                        getLogger().info("Costo previsto minore del preventivo...lascio tutto com'è e non fo nulla");
                        costoTotaleEffettivo = costoTotaleBase;
                    }

                    getLogger().info("costo totale scalate le assenze : {}", costoTotaleEffettivo);
                }

                paiInterventoMese.setBdgConsEur(costoTotaleEffettivo);
                paiInterventoMese.setBdgConsQta(costoTotaleEffettivo.divide(costoIntervento, MathContext.DECIMAL32));

            }

            paiInterventoMese.setCausVar((String) jsonPaiInterventoMese.get("causale"));
            paiInterventoMese.setNote((String) jsonPaiInterventoMese.get("note"));
            //se l'intervento non deve proseguire e deve essere solo salvato il pim setto generato a 2 cosi non compare nelle liste successive ( modifica voluta da Lombardo)
            if (nonProseguire != null) {
                paiInterventoMese.setGenerato(2);
            } else {
                paiInterventoMese.setGenerato(1);
            }
            if ((paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgFatt() == 'S' || paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgPagam() == 'S') && (nonProseguire == null)) {
                if (paiInterventoMese.getPaiCdg() == null) {
                    //se non esistono righe in controllo di gestione, le creo
                    PaiCdg cdg = new PaiCdg();
                    PaiCdgPK cdgPK = new PaiCdgPK();
                    cdgPK.setCntTipint(paiInterventoMese.getPaiInterventoMesePK().getCntTipint());
                    cdgPK.setCodPai(paiInterventoMese.getPaiInterventoMesePK().getCodPai());
                    cdgPK.setCodTipint(paiInterventoMese.getPaiInterventoMesePK().getCodTipint());
                    cdgPK.setCodAnno(paiInterventoMese.getPaiInterventoMesePK().getAnno());
                    cdgPK.setAnnoEff(paiInterventoMese.getPaiInterventoMesePK().getAnnoEff());
                    cdgPK.setMeseEff(paiInterventoMese.getPaiInterventoMesePK().getMeseEff());
                    cdgPK.setCodImpe(paiInterventoMese.getPaiInterventoMesePK().getCodImp());
                    cdg.setPaiCdgPK(cdgPK);
                    cdg.setPaiInterventoMese(paiInterventoMese);
                    cdg.setCodAna(paiInterventoMese.getPaiIntervento().getPai().getAnagrafeSoc());
                    cdg.setCodCap(paiInterventoMese.getBudgetTipIntervento().getCodCap());
                    cdg.setCcele(paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getCcele());
                    cdg.setImpStd(costoIntervento);
                    cdg.setImpVar(paiInterventoMese.getBdgConsVar());
                    cdg.setQtaPrev(paiInterventoMese.getBdgPrevQta());
                    paiInterventoMese.setPaiCdg(cdg);
                }
                paiInterventoMese.getPaiCdg().setImpComplUsingFascia(paiInterventoMese.getBdgConsEur());
                paiInterventoMese.getPaiCdg().setQtaErog(paiInterventoMese.getBdgConsQta());
            }


            if (!Objects.equal(paiInterventoMese.getBdgPrevEur(), paiInterventoMese.getBdgConsEur())
                    || !Objects.equal(MoreObjects.firstNonNull(paiInterventoMese.getBdgConsVar(), BigDecimal.ZERO), BigDecimal.ZERO)) {
                if (Objects.equal(paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgPagam(), TipologiaIntervento.FLG_PAGAMENTO_S)) {
                    getLogger().debug("cambiato consuntivo/variazione per pagamento, richiediamo determina di variazione");

                    getLogger().debug("disattiviamo determine di variazione precedenti");
                    List<PaiEvento> determineVariazioneList = new PaiEventoDao(getEntityManager()).findDetermineVariazione(paiInterventoMese.getPaiIntervento());
                    for (PaiEvento paiEvento : determineVariazioneList) {
                        paiEvento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
                    }

                    PaiEvento evento = Pratica.serializePaiEvento("Inserimento Determina di variazione", paiInterventoMese.getPaiIntervento(), utente);
                    evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
                    getEntityManager().persist(evento);
                    MapDatiSpecificiIntervento datoSpecificoDetermina = null;
                    for (MapDatiSpecificiIntervento map : paiInterventoMese.getPaiIntervento().getMapDatiSpecificiInterventoList()) {
                        if ("ds_imp_var".equals(map.getMapDatiSpecificiInterventoPK().getCodCampo())) {
                            datoSpecificoDetermina = map;
                            break;
                        }
                    }
                    if (datoSpecificoDetermina == null) {
                        datoSpecificoDetermina = new MapDatiSpecificiIntervento();
                        MapDatiSpecificiInterventoPK pk = new MapDatiSpecificiInterventoPK();
                        pk.setCntTipint(paiInterventoMese.getPaiInterventoMesePK().getCntTipint());
                        pk.setCodCampo("ds_imp_var");
                        pk.setCodPai(paiInterventoMese.getPaiInterventoMesePK().getCodPai());
                        pk.setCodTipint(paiInterventoMese.getPaiInterventoMesePK().getCodTipint());
                        datoSpecificoDetermina.setMapDatiSpecificiInterventoPK(pk);
                        datoSpecificoDetermina.setValCampo("0.0");
                    }
                    BigDecimal variazione = Strings.isNullOrEmpty(datoSpecificoDetermina.getValCampo()) ? BigDecimal.ZERO : new BigDecimal(datoSpecificoDetermina.getValCampo());

                    variazione = variazione.add(paiInterventoMese.getBdgConsEur()).subtract(paiInterventoMese.getBdgPrevEur()).add(paiInterventoMese.getBdgConsVar() == null ? BigDecimal.ZERO : paiInterventoMese.getBdgConsVar());
                    datoSpecificoDetermina.setValCampo(variazione.toString());

                }
            }
            String value = jsonPaiInterventoMese.get("motivazioneVariazioneSpesa");
            paiInterventoMese.setMotivazioneVariazioneSpesa(StringUtils.isBlank(value) ? null : parametriIndataDao.findByIdParamIndata(Integer.valueOf(value)));
            jsonBuilder.addRecord(jsonPaiInterventoMese);
        }
        Object jsonResponse = jsonBuilder.buildStoreResponse();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("json response = {}", JsonBuilder.getGsonPrettyPrinting().toJson(jsonResponse));
        }
        
    }

    /**
     * Trasforma un oggetto {@link PaiInterventoMese} in un oggetto JSON pronto
     * per essere trasmesso alla tabella EXT-JS.
     *
     * @param paiInterventoMese Oggeto per la trasformazione
     * @return Oggetto JSON contente i dati del passato come parametro.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    private JSONObject generaPaiInterventoMeseJSON(PaiInterventoMese paiInterventoMese) throws JSONException {
        JSONObject jso = new JSONObject();
        JSONObject jsoId = new JSONObject();
        jsoId.put("codPai", paiInterventoMese.getPaiInterventoMesePK().getCodPai());
        jsoId.put("codTipint", paiInterventoMese.getPaiInterventoMesePK().getCodTipint());
        jsoId.put("cntTipint", paiInterventoMese.getPaiInterventoMesePK().getCntTipint());
        jsoId.put("annoEff", paiInterventoMese.getPaiInterventoMesePK().getAnnoEff());
        jsoId.put("meseEff", paiInterventoMese.getPaiInterventoMesePK().getMeseEff());
        jsoId.put("anno", paiInterventoMese.getPaiInterventoMesePK().getAnno());
        jsoId.put("codImp", paiInterventoMese.getPaiInterventoMesePK().getCodImp());
        jso.put("anno", paiInterventoMese.getPaiInterventoMesePK().getAnno());
        jso.put("codImp", paiInterventoMese.getPaiInterventoMesePK().getCodImp());
        jso.put("id", jsoId.toString());
        jso.put("cod_tip_int", paiInterventoMese.getPaiInterventoMesePK().getCodTipint());
        jso.put("assenze", MoreObjects.firstNonNull(paiInterventoMese.getGgAssenza(), BigDecimal.ZERO));
        jso.put("mese_eff", paiInterventoMese.getPaiInterventoMesePK().getMeseEff());
        jso.put("tipo_servizio", paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getDesTipint());
        jso.put("unita_di_misura", paiInterventoMese.getPaiIntervento().getIdParamUniMis().getDesParam());
        jso.put("qt_prevista", paiInterventoMese.getBdgPrevQta());
        jso.put("qt_erogata", MoreObjects.firstNonNull(paiInterventoMese.getBdgConsQta(), paiInterventoMese.getBdgPrevQta()));
        jso.put("qt_beneficiata", MoreObjects.firstNonNull(paiInterventoMese.getBdgConsQtaBenef(), MoreObjects.firstNonNull(paiInterventoMese.getBdgConsQta(), paiInterventoMese.getBdgPrevQta())));
        jso.put("bdgPrevEur", paiInterventoMese.getBdgPrevEur());
        jso.put("bdgConsEur", MoreObjects.firstNonNull(paiInterventoMese.getBdgConsEur(), paiInterventoMese.getBdgPrevEur()));
        jso.put("assenze_totali", new PaiInterventoMeseDao(getEntityManager()).contaGiorniAssenza(paiInterventoMese.getPaiIntervento()));
        jso.put("liquidazione", 'N' == paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgPagam() ? "No" : "Si");
        jso.put("fatturazione", 'N' == paiInterventoMese.getPaiIntervento().getTipologiaIntervento().getFlgFatt() ? "No" : "Si");
        jso.put("variazione_straordinaria", paiInterventoMese.getBdgConsVar());
        jso.put("causale", paiInterventoMese.getCausVar());
        jso.put("note", paiInterventoMese.getNote());
        jso.put("motivazioneVariazioneSpesa", paiInterventoMese.getMotivazioneVariazioneSpesa() == null ? null : paiInterventoMese.getMotivazioneVariazioneSpesa().getIdParamIndata());
        return (jso);
    }

    @ResourceMapping(value = "motivazioneVariazioneSpesa")
    public void motivazioneVariazioneSpesa(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();

        Object storeResponse = JsonBuilder.newInstance().withData(new ParametriIndataDao(getEntityManager()).findByTipParam(Parametri.MOTIVAZIONI_VARIAZIONE_SPESA)).withTransformer(new JsonMapTransformer<ParametriIndata>() {
            @Override
            public void transformToMap(ParametriIndata obj) {
                put("id", obj.getIdParamIndata());
                put("label", obj.getDesParam());
            }
        }).buildStoreResponse();
        gson.toJson(storeResponse, writer);

    }

    /**
     * Metodo che salva le acquisizioni selezionate permettendo cosi di trovarle in pagamenti o fatture
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JSONException
     */
    @ResourceMapping(value = "salvaAcquisizioni")
    public void salvaAcquisizioni(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {

        FattureService fattureService = new FattureService();

        String[] dati = request.getParameterValues("data");
        int acquisiti = 0;
        List<String> acquisizioniConErrori = new ArrayList<String>();

        for (int i = 0; i < dati.length; i++) {
            String id = dati[i];
            List<String> errori = fattureService.salvaAcquisizione(getEntityManager(), id);
            acquisizioniConErrori.addAll(errori);
            acquisiti++;
        }


        JSONObject risposta = new JSONObject();
        risposta.put("acquisiti", acquisiti);
        risposta.put("acquisizioniConErrori", StringUtils.join(acquisizioniConErrori, "\n"));
        risposta.put("success", acquisizioniConErrori.size()==0);
        response.setCharacterEncoding("UTF-8");
        risposta.write(response.getWriter());
        response.getWriter().close();
    }

}

