package it.wego.welfarego.pagamenti.fatture;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import it.wego.extjs.json.JsonBuilder;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.pagamenti.ExportFileUtils;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller per le chiamate AJAX riguardanti la gestione generale delle
 * fatture.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxFattura extends AbstractAjaxController {

    /**
     * Percorso nel quale salvare il file CVS
     */
    private String percorso;

    private String ALIQUOTA_IVA = "iv";

    public void setPercorso(String percorso) {
        this.percorso = percorso;
    }

    /**
     * Carica dalla base di dati la lista di tutti gli importi IVA presente
     * permesi.
     *
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaValoriIvaFatturazioni")
    public void listaValoriIva(ResourceResponse response) throws IOException, JSONException {

        JSONArray jsa = new JSONArray();
        List<ParametriIndata> parametri = new ParametriIndataDao(getEntityManager()).findByTipParam(ALIQUOTA_IVA);
        for (ParametriIndata parametro : parametri) {

            JSONObject jso = new JSONObject();
            jso.put("id", parametro.getIdParamIndata());
            jso.put("label", parametro.getDesParam());
            jso.put("valore", parametro.getDecimalPercentageParamAsDecimal());
            jsa.put(jso);
        }

        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Recupara dalla base di dati la lista delle modalità dei pagamenti delle
     * fatture.
     *
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaModalitaPagamantiFatturazioni")
    public void listaModalitaPagamanti(ResourceResponse response) throws IOException, JSONException {
        JSONArray jsa = new JSONArray();
        Query q = getEntityManager().createQuery("SELECT distinct p FROM ParametriIndata p "
                + "WHERE p.idParam.tipParam.tipParam = 'mp' "
                + "AND p.dtIniVal = ( "
                + "SELECT MAX(p2.dtIniVal) FROM ParametriIndata p2 "
                + "WHERE p.idParam.idParam = p2.idParam.idParam "
                + "AND p2.dtIniVal <= :oggi"
                + ")");
        q.setParameter("oggi", new Date());
        List<ParametriIndata> parametri = q.getResultList();
        for (ParametriIndata parametro : parametri) {
            JSONObject jso = new JSONObject();
            jso.put("id", parametro.getIdParamIndata());
            jso.put("label", parametro.getDesParam());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Carica dalla base di dati la lista delle fatture filtrato sui dati
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
    @ResourceMapping(value = "cercaFatture")
    public void cercaFatture(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JSONObject jsoData = null;
        String tipInt = request.getParameter("tipo_intervento");
        String mese_di_competenza_mese = request.getParameter("mese_di_competenza_mese");
        String mese_di_competenza_anno = request.getParameter("mese_di_competenza_anno");
        String periodo_considerato_dal_mese = request.getParameter("periodo_considerato_dal_mese");
        String periodo_considerato_al_mese = request.getParameter("periodo_considerato_al_mese");
        String periodo_considerato_dal_anno = request.getParameter("periodo_considerato_dal_anno");
        String periodo_considerato_al_anno = request.getParameter("periodo_considerato_al_anno");
        String stato_fatturazioni = request.getParameter("stato_fatturazioni");


        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(Fattura.class);
        Join join = r.join("fatturaDettaglioList");
        Predicate p = null;
        if (!Strings.isNullOrEmpty(tipInt)) {
            p = cb.equal(join.get("codTipint").get("codTipint"), tipInt);
        } else {
            p = cb.isNotNull(join.get("codTipint").get("codTipint")); //TRUE
        }
        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            try {
                Calendar dataDal = Calendar.getInstance();
                dataDal.set(Calendar.YEAR, Integer.parseInt(mese_di_competenza_anno));
                dataDal.set(Calendar.MONTH, Integer.parseInt(mese_di_competenza_mese) - 1);
                dataDal.set(Calendar.DAY_OF_MONTH, 1);
                dataDal.set(Calendar.HOUR_OF_DAY, 0);
                dataDal.set(Calendar.MINUTE, 0);
                dataDal.set(Calendar.SECOND, 0);
                dataDal.set(Calendar.MILLISECOND, 0);
                Calendar dataAl = Calendar.getInstance();
                dataAl.set(Calendar.YEAR, Integer.parseInt(mese_di_competenza_anno));
                dataAl.set(Calendar.MONTH, Integer.parseInt(mese_di_competenza_mese) - 1);
                dataAl.set(Calendar.DAY_OF_MONTH, dataAl.getActualMaximum(Calendar.DAY_OF_MONTH));
                dataAl.set(Calendar.HOUR_OF_DAY, 0);
                dataAl.set(Calendar.MINUTE, 0);
                dataAl.set(Calendar.SECOND, 0);
                dataAl.set(Calendar.MILLISECOND, 0);
                Predicate mese = cb.equal(join.get("meseEff"), Integer.parseInt(mese_di_competenza_mese));
                Predicate anno = cb.equal(join.get("annoEff"), Integer.parseInt(mese_di_competenza_anno));
                Predicate periodoDal = cb.lessThanOrEqualTo(r.get("periodoDal"), dataAl.getTime());
                Predicate periodoAl = cb.greaterThanOrEqualTo(r.get("periodoAl"), dataDal.getTime());
                p = cb.and(p, cb.or(cb.and(mese, anno), cb.and(periodoDal, periodoAl)));
                jsoData = new JSONObject();
                jsoData.put("meseRif", Integer.parseInt(mese_di_competenza_mese));
                jsoData.put("anno", Integer.parseInt(mese_di_competenza_anno));
            }
            catch (NumberFormatException ex) {
                //Le date passate sono in formato non valido.
                //Eseguo la ricerca ignorandole.
            }
        }
        else {
            if (periodo_considerato_dal_mese != null && periodo_considerato_al_mese != null && periodo_considerato_dal_anno != null && periodo_considerato_al_anno != null) {
                try {
                    Calendar dataDal = Calendar.getInstance();
                    dataDal.set(Calendar.YEAR, Integer.parseInt(periodo_considerato_dal_anno));
                    dataDal.set(Calendar.MONTH, Integer.parseInt(periodo_considerato_dal_mese) - 1);
                    dataDal.set(Calendar.DAY_OF_MONTH, 1);
                    dataDal.set(Calendar.HOUR_OF_DAY, 0);
                    dataDal.set(Calendar.MINUTE, 0);
                    dataDal.set(Calendar.SECOND, 0);
                    dataDal.set(Calendar.MILLISECOND, 0);
                    Calendar dataAl = Calendar.getInstance();
                    dataAl.set(Calendar.YEAR, Integer.parseInt(periodo_considerato_al_anno));
                    dataAl.set(Calendar.MONTH, Integer.parseInt(periodo_considerato_al_mese) - 1);
                    dataAl.set(Calendar.DAY_OF_MONTH, dataAl.getActualMaximum(Calendar.DAY_OF_MONTH));
                    dataAl.set(Calendar.HOUR_OF_DAY, 0);
                    dataAl.set(Calendar.MINUTE, 0);
                    dataAl.set(Calendar.SECOND, 0);
                    dataAl.set(Calendar.MILLISECOND, 0);
                    Predicate meseDal = cb.ge(join.get("meseEff"), Integer.parseInt(periodo_considerato_dal_mese));
                    Predicate annoDal = cb.equal(join.get("annoEff"), Integer.parseInt(periodo_considerato_dal_anno));
                    Predicate annoDal2 = cb.greaterThan(join.get("annoEff"), Integer.parseInt(periodo_considerato_dal_anno));
                    Predicate meseAl = cb.le(join.get("meseEff"), Integer.parseInt(periodo_considerato_al_mese));
                    Predicate annoAl = cb.equal(join.get("annoEff"), Integer.parseInt(periodo_considerato_al_anno));
                    Predicate annoAl2 = cb.lessThan(join.get("annoEff"), Integer.parseInt(periodo_considerato_al_anno));
                    p = cb.and(p, cb.or(cb.and(cb.or(annoDal2, cb.and(meseDal, annoDal)), cb.or(annoAl2, cb.and(meseAl, annoAl)))));
                    jsoData = new JSONObject();
                    jsoData.put("meseRifDal", Integer.parseInt(periodo_considerato_dal_mese));
                    jsoData.put("annoDal", Integer.parseInt(periodo_considerato_dal_anno));
                    jsoData.put("meseRifAl", Integer.parseInt(periodo_considerato_al_mese));
                    jsoData.put("annoAl", Integer.parseInt(periodo_considerato_al_anno));
                }
                catch (NumberFormatException ex) {
                    //Le date passate sono in formato non valido.
                    //Eseguo la ricerca ignorandole.
                }
            }
        }
        String uot_struttura = request.getParameter("uot_struttura");
        if (uot_struttura != null) {
            try {
                p = cb.and(p, cb.equal(r.get("paiIntervento").get("pai").get("idParamUot").get("idParamIndata"), Integer.parseInt(uot_struttura)));
            }
            catch (NumberFormatException ex) {
                //L'id dell'uot non è in unmerico
                //Eseguo la ricerca ignorando il parametro errato.
            }
        }
        String cognome = Strings.emptyToNull(request.getParameter("cognome"));

        if (cognome != null) {
            cognome = cognome.toUpperCase();
            p = cb.and(p, cb.like(r.get("paiIntervento").get("pai").get("codAna").get("anagrafeSoc").get("cognome"), cognome + "%"));
        }

        String nome = Strings.emptyToNull(request.getParameter("nome"));
        if (nome != null) {
            nome = nome.toUpperCase();
            p = cb.and(p, cb.like(r.get("paiIntervento").get("pai").get("codAna").get("anagrafeSoc").get("nome"), nome + "%"));
        }

        p = cb.and(p, cb.equal(r.get("idParamStato").get("idParam").get("tipParam").get("tipParam"), "fs"));
        //almeno uno degli if deve essere settato
        ArrayList<Predicate> stati = new ArrayList<Predicate>();

        if (stato_fatturazioni.equals("da_emettere")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "de"));
        }
        if (stato_fatturazioni.equals("emesse")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "em"));
        }
        if (stato_fatturazioni.equals("da_inviare")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "di"));
        }
        if (stato_fatturazioni.equals("inviate")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "in"));
        }
        if (stato_fatturazioni.equals("pagate")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "pa"));
        }
        if (stato_fatturazioni.equals("annullate")) {
            stati.add(cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "an"));
        }
        p = cb.and(p, cb.or(stati.toArray(new Predicate[0])));
        c.distinct(true);
        c.where(p);
        Query q = getEntityManager().createQuery(c);
        List<Fattura> fatture = q.getResultList();




        JSONArray jsa = new JSONArray();
        for (Fattura fattura : fatture) {
            jsa.put(this.genetaFatturaJSO(fattura));
        }

        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Carica dalla base di dati tutte i dati presenti per poter generare la
     * fatture non ancora generate dal sistema. Al browser sarà restituita una
     * lista di {@link Fattura} che in verità ancora non esistono. L'utente
     * avrà poi la possibilità di scegliere quale di queste generarla.
     *
     * @param request  Richiesta del browser contenente i dati per il filtro di
     *                 ricerca.
     * @param response Risposta al browser
     * @throws IOException   In caso di problemi durante la scrittura del mesaggio
     *                       di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     *                       risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "cercaFattureDaGenerare")
    public void cercaFattureDaGenerare(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter()).withParameters(request.getParameterMap());
        final JSONObject jsoData = new JSONObject();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();

        Root r = c.from(Pai.class);
        Join cs = r.join("codAna", JoinType.INNER);
        Join a = cs.join("anagrafeSoc", JoinType.INNER);
        Join pin = r.join("idParamUot", JoinType.INNER);
        ListJoin pi = r.joinList("paiInterventoList", JoinType.INNER);
        ListJoin pim = pi.joinList("paiInterventoMeseList", JoinType.INNER);
        Join ti = pi.join("tipologiaIntervento", JoinType.INNER);
        Join fad = pim.join("idFattDettaglio", JoinType.LEFT);
        Join fa = fad.join("idFatt", JoinType.LEFT);
        Join mad = pim.join("idManDettaglio", JoinType.LEFT);
        Join ma = mad.join("idMan", JoinType.LEFT);
        Join pinfs = pim.join("idParamFascia", JoinType.LEFT);
        c.multiselect(r.get("codPai"),
                a.get("nome"),
                a.get("cognome"),
                pinfs.get("idParamIndata"),
                pinfs.get("desParam"));
        c.distinct(true);


        Predicate p = pi.get("statoInt").in("C", "E");

        String codTipInt = request.getParameter("tipo_intervento");
        if (!Strings.isNullOrEmpty(codTipInt)) {
            p = cb.and(p, cb.equal(pi.get("paiInterventoPK").get("codTipint"), codTipInt));
        }
        //prendo tutti quelli che sono ancora da generare
        p = cb.and(p, cb.equal(pim.get("generato"), 1));

        // BEGIN filtraggio per periodo
        if (request.getParameter("mese_di_competenza_mese") != null && request.getParameter("mese_di_competenza_anno") != null) {
            try {
                Predicate mese = cb.equal(pim.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(request.getParameter("mese_di_competenza_mese")));
                Predicate anno = cb.equal(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(request.getParameter("mese_di_competenza_anno")));
                p = cb.and(p, mese, anno);

                jsoData.put("meseEff", Integer.parseInt(request.getParameter("mese_di_competenza_mese")));
                jsoData.put("annoEff", Integer.parseInt(request.getParameter("mese_di_competenza_anno")));
            }
            catch (NumberFormatException ex) {
                getLogger().warn("", ex);
                //Le date passate sono in formato non valido.
                //Eseguo la ricerca ignorandole.
            }
        }
        else {
            if (request.getParameter("periodo_considerato_dal_mese") != null && request.getParameter("periodo_considerato_al_mese") != null && request.getParameter("periodo_considerato_dal_anno") != null && request.getParameter("periodo_considerato_al_anno") != null) {
                try {
                    Predicate meseDal = cb.ge(pim.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(request.getParameter("periodo_considerato_dal_mese")));
                    Predicate annoDal = cb.equal(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(request.getParameter("periodo_considerato_dal_anno")));
                    Predicate annoDal2 = cb.greaterThan(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(request.getParameter("periodo_considerato_dal_anno")));
                    Predicate meseAl = cb.le(pim.get("paiInterventoMesePK").get("meseEff"), Integer.parseInt(request.getParameter("periodo_considerato_al_mese")));
                    Predicate annoAl = cb.equal(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(request.getParameter("periodo_considerato_al_anno")));
                    Predicate annoAl2 = cb.lessThan(pim.get("paiInterventoMesePK").get("annoEff"), Integer.parseInt(request.getParameter("periodo_considerato_al_anno")));
                    p = cb.and(p, cb.or(annoDal2, cb.and(meseDal, annoDal)), cb.or(annoAl2, cb.and(meseAl, annoAl)));

                    jsoData.put("meseEffDal", Integer.parseInt(request.getParameter("periodo_considerato_dal_mese")));
                    jsoData.put("annoEffDal", Integer.parseInt(request.getParameter("periodo_considerato_dal_anno")));
                    jsoData.put("meseEffAl", Integer.parseInt(request.getParameter("periodo_considerato_al_mese")));
                    jsoData.put("annoEffAl", Integer.parseInt(request.getParameter("periodo_considerato_al_anno")));
                }
                catch (NumberFormatException ex) {
                    getLogger().warn("", ex);
                    //Le date passate sono in formato non valido.
                    //Eseguo la ricerca ignorandole.
                }
            }
        }
        // END filtraggio per periodo


        // BEGIN filtraggio per uot
        String paramUotStr = Strings.emptyToNull(request.getParameter("uot_struttura"));
        if (paramUotStr != null) {
            try {
                p = cb.and(p, cb.equal(pin.get("idParamIndata"), Integer.parseInt(paramUotStr)));
            }
            catch (NumberFormatException ex) {
                getLogger().warn("", ex);
                //L'id dell'uot non è in unmerico
                //Eseguo la ricerca ignorando il parametro errato.
            }
        }
        //begin filtraggio nome e cognome 
        String cognome = Strings.emptyToNull(request.getParameter("cognome"));

        if (cognome != null) {
            cognome = cognome.toUpperCase();
            p = cb.and(p, cb.like(a.get("cognome"), cognome + "%"));
        }

        String nome = Strings.emptyToNull(request.getParameter("nome"));
        if (nome != null) {
            nome = nome.toUpperCase();
            p = cb.and(p, cb.like(a.get("nome"), nome + "%"));
        }


        p = cb.and(p, cb.equal(fad.get("idFattDettaglio"), cb.nullLiteral(FatturaDettaglio.class)));
        p = cb.and(p, cb.notEqual(pim.get("bdgConsQta"), cb.nullLiteral(BigDecimal.class)));
        p = cb.and(p, cb.equal(ti.get("flgFatt"), "S"));
        //non deve essere ancora stato generato 
        p = cb.and(p, cb.equal(pim.get("generato"), 1));
        // deve essere entrato in esecutivit� (ANCHE SE CHIUSO)
        p = cb.and(p, pi.get("dtEsec").isNotNull());

        // filtro su fascia e fatturazione e data validit� isee
        p = cb.and(p, cb.or(pinfs.get("decimalParam").isNull(), cb.lessThan(pinfs.get("decimalParam"), 100)));

        c.where(p);
        ArrayList<Order> order = new ArrayList<Order>();
        order.add(cb.asc(a.get("cognome")));
        order.add(cb.asc(a.get("nome")));
        c.orderBy(order);

        List result = getEntityManager().createQuery(c).getResultList();

        for (Object o : result) {
            Object[] oggetto = (Object[]) o;
            Map record = Maps.newHashMap();
            Map recordId = Maps.newHashMap();
            recordId.put("codPai", (Integer) oggetto[0]);
            recordId.put("periodoRicerca", jsoData == null ? null : jsoData.toString());
            recordId.put("idFascia", (Integer) oggetto[3]);
            record.put("id", JsonBuilder.getGson().toJson(recordId));
            record.put("nome", (String) oggetto[1]);
            record.put("cognome", (String) oggetto[2]);
            record.put("fascia", (String) oggetto[4]);
            record.put("tipo_intervento", " ");
            record.put("importo", "-");
            record.put("n_fattura", "-");
            record.put("fattura_nota", "fattura");
            record.put("stato", "da generare");
            jsonBuilder.addRecord(record);
        }
        jsonBuilder.buildStoreResponse();
    }
    
    /*
     * Salva il nuovo stato dell'elenco delle {@link Fattura}
     *
     * @param request Richiesta del browser contenente i dati delle fatture con
     * il nuovo stato
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "salvaStatoFatture")
    @Transactional
    public void salvaStatoFatture(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
        JSONArray jsa = new JSONArray();
        JSONArray dati = new JSONArray(request.getParameter("data"));
        for (int i = 0; i < dati.length(); i++) {
            JSONObject jsoFattura = (JSONObject) dati.get(i);
            Integer idFattura = jsoFattura.getInt("id");
            String statoFattura = "Emetti".equals(jsoFattura.getString("azione")) ? "em" : "di";
            getLogger().debug("impostiamo stato fattura id '{}' a '{}'", idFattura, statoFattura);
            Fattura fattura = getEntityManager().getReference(Fattura.class, idFattura);
            Preconditions.checkNotNull(fattura, "fattura not found for id '%s'", idFattura);
            ParametriIndata stato = this.cercaStatoFattura(getEntityManager(), statoFattura);
            Preconditions.checkNotNull(stato);
            fattura.setIdParamStato(stato);
            if (Objects.equal(statoFattura, "em") && fattura.getNumFatt() == 0) {
                ParametriIndata numeroFatturaParam = new ParametriIndataDao(getEntityManager()).findOneByTipParamCodParam(Parametri.PARAMETRI_FATTURE, "numfatt");
                Preconditions.checkArgument(numeroFatturaParam != null && numeroFatturaParam.getDecimalParam() != null, "parametro numero fattura non impostato");
                BigDecimal currentNumFatt = numeroFatturaParam.getDecimalParam();
                numeroFatturaParam.setDecimalParam(currentNumFatt.add(BigDecimal.ONE));
                fattura.setNumFatt(currentNumFatt.intValueExact());
            }
            jsa.put(this.genetaFatturaJSO(fattura));
        }
        this.mandaRisposta(response, jsa, null);
    }

    @ResourceMapping(value = "anteprimaFatture")
    @Transactional
    public void anteprimaFatture(ResourceRequest request, ResourceResponse response) throws JSONException, ParseException, FileNotFoundException, PortalException, SystemException, IOException {
        //TODO
        JSONArray dati = new JSONArray(request.getParameter("data"));
        Date dataEmissione = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dataEmissione"));
        Date dataScadenza = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dataScadenza"));
        List<Fattura> fattureDaEmettere = new ArrayList<Fattura>();
        int emesse = 0;
        for (int i = 0; i < dati.length(); i++) {
            Integer id = (Integer) dati.get(i);
            Fattura fattura = getEntityManager().getReference(Fattura.class, id);
            if (fattura != null && fattura.getIdParamStato().equals(cercaStatoFattura(getEntityManager(), "de"))) {
                fattura.setScadenza(dataScadenza);
                fattura.setTimbro(dataEmissione);
                emesse++;
                fattureDaEmettere.add(fattura);
            }

        }

        List<Fattura> fattureNonZero = new ArrayList<Fattura>();
        for (Fattura f : fattureDaEmettere) {
            if (f.getImportoTotale().compareTo(BigDecimal.ZERO) > 0) {
                fattureNonZero.add(f);
            }
        }
        File file = ExportFileUtils.createFatturaExportFile(percorso, fattureNonZero, request, getEntityManager(), true);
        JSONObject totale = new JSONObject();
        totale.put("fileGenerato", file.getAbsolutePath());
        response.getWriter().print(totale);
        response.getWriter().close();

    }

    /**
     * Invia le fatture.<br /> Cerca nella base di dati tutte le fatture che
     * sono in attesa di essere inviate, crea un file CSV contenente i loro dati
     * e infine setta lo stato della fattura a inviata. Nel caso in cui alla
     * fattura sono assegnati anche delle persone civilmente obbligate a pagare
     * una retta, verranno inserite nel file CSV anche delle righe
     * corrispondenti a queste persone.
     *
     * @param response Risposta al browser contenente il numero di fatture
     *                 corettamente inviate.
     * @throws IOException     In caso di problemi durante la scrittura del mesaggio
     *                         di risposta.
     * @throws JSONException   In caso di problemi durante la formazione della
     *                         risposta in formato JSON (non previsto)
     * @throws SystemException
     * @throws PortalException
     * @throws ParseException
     */
    @ResourceMapping(value = "inviaFatture")
    @Transactional
    public void inviaFatture(ResourceRequest request, ResourceResponse response) throws IOException, JSONException, PortalException, SystemException, ParseException {
        JSONArray dati = new JSONArray(request.getParameter("data"));
        Date dataEmissione = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dataEmissione"));
        Date dataScadenza = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dataScadenza"));
        List<Fattura> fattureDaEmettere = new ArrayList<Fattura>();
        int emesse = 0;
        for (int i = 0; i < dati.length(); i++) {
            Integer id = (Integer) dati.get(i);
            Fattura fattura = getEntityManager().getReference(Fattura.class, id);

            if (fattura != null && fattura.getIdParamStato().equals(cercaStatoFattura(getEntityManager(), "de"))) {

                fattura.setIdParamStato(cercaStatoFattura(getEntityManager(), "in"));
                fattura.setTimbro(dataEmissione);
                fattura.setScadenza(dataScadenza);

                if (fattura.getNumFatt() == 0) {
                    ParametriIndata numeroFatturaParam = new ParametriIndataDao(getEntityManager()).findOneByTipParamCodParam(Parametri.PARAMETRI_FATTURE, "numfatt");
                    Preconditions.checkArgument(numeroFatturaParam != null && numeroFatturaParam.getDecimalParam() != null, "parametro numero fattura non impostato");
                    BigDecimal currentNumFatt = numeroFatturaParam.getDecimalParam();
                    numeroFatturaParam.setDecimalParam(currentNumFatt.add(BigDecimal.ONE));
                    fattura.setNumFatt(currentNumFatt.intValueExact());

                }
                emesse++;
                fattureDaEmettere.add(fattura);
            }

        }

        List<Fattura> fattureNonZero = new ArrayList<Fattura>();
        for (Fattura f : fattureDaEmettere) {
            if (f.getImportoTotale().compareTo(BigDecimal.ZERO) > 0) {
                fattureNonZero.add(f);
            }
        }

        File file = ExportFileUtils.createFatturaExportFile(percorso, fattureNonZero, request, getEntityManager(), false);

        JSONObject totale = new JSONObject();
        totale.put("fatture_inviate", emesse);
        totale.put("fileGenerato", file.getAbsolutePath());
        response.getWriter().print(totale);
        response.getWriter().close();
    }
    
    private JSONObject genetaFatturaJSO(Fattura fattura) throws JSONException {
        JSONObject jso = new JSONObject();
        jso.put("id", fattura.getIdFatt());
        jso.put("nome", fattura.getPaiIntervento().getPai().getAnagrafeSoc().getNome());
        jso.put("cognome", fattura.getPaiIntervento().getPai().getAnagrafeSoc().getCognome());
        jso.put("fascia", fattura.getIdParamFascia().getDesParam());
        jso.put("tipo_intervento", fattura.getPaiIntervento().getTipologiaIntervento().getDesTipint());
        jso.put("importo", fattura.getImportoTotale());
        jso.put("riscosso", fattura.getRiscosso());
        jso.put("n_fattura", fattura.getNumFatt());
        jso.put("fattura_nota_credito", fattura.getImportoTotale().signum() != -1 ? "fattura" : "nota di credito");
        jso.put("stato", fattura.getIdParamStato().getDesParam());
        jso.put("codice_stato", fattura.getIdParamStato().getIdParam().getCodParam());
        jso.put("azione", "-");
        jso.put("cf", fattura.getPaiIntervento().getPai().getAnagrafeSoc().getCodFisc());
        return (jso);
    }

}

