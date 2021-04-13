package it.wego.welfarego.pagamenti;

import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Controller per le chiamate AJAX riguardanti il caricamento delle informazioni
 * utilizzate da tutti i filtri di ricerca della portlet
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxElenchi extends AbstractAjaxController {

    private void listaClasseTipoIntervento(ResourceResponse response) throws Exception {

        ParametriIndataDao parametriIndataDao = new ParametriIndataDao(getEntityManager());
        JsonBuilder.newInstance().withWriter(response.getWriter()).withData(
                parametriIndataDao.findByTipParamAttivo(Parametri.CLASSE_INTERVENTO)).withTransformer(new JsonMapTransformer<ParametriIndata>() {
            @Override
            public void transformToMap(ParametriIndata obj) {
                put("id", obj.getIdParamIndata());
                put("label", obj.getDesParam());
            }
        }).buildStoreResponse();
    }

    /**
     * Carica dalla base di dati la lista delle classi delle tipologie di
     * intervento e la restituisce in formato JSON. La risposta conterrà una
     * lista di chiavi associate a un testo da visualizzare.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaClasseTipoInterventoAcquisizioni")
    public void listaClasseTipoInterventoAcquisizioni(ResourceResponse response) throws Exception {
        //max_todo: modificare in modo che escano solamente le voci per WEGO
        listaClasseTipoIntervento(response);
    }

    /**
     * Carica dalla base di dati la lista completa di tipologie degli interventi
     * e la scerive response in formato JSON. La risposta conterrà una lista di
     * chiavi associate a un testo da visualizzare.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaTipoInterventoAcquisizioni")
    public void listaTipoInterventoAcquisizioni(ResourceResponse response) throws IOException, JSONException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(TipologiaIntervento.class);

        Predicate p = cb.notEqual(r.get("codTipint"), "AD009");
        c.where(p);
        c.orderBy(cb.asc(r.get("desTipint")));
        List<TipologiaIntervento> tipologie = (List<TipologiaIntervento>) getEntityManager().createQuery(c).getResultList();
        JSONArray jsa = new JSONArray();
        for (TipologiaIntervento tipo : tipologie) {
            JSONObject jso = new JSONObject();
            jso.put("id", tipo.getCodTipint());
            jso.put("label", tipo.getDesTipint());
            jso.put("classe", tipo.getIdParamClasseTipint().getIdParamIndata());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }
    
    /**
     * Metodo che dato un codice intervento mi restituisce tutti gli impegni associati a quellintervento 
     * @param request 
     * @param response
     * @throws JSONException
     * @throws IOException
     */
    @ResourceMapping(value ="listaImpegni")
    public void listImpegni (ResourceRequest request,ResourceResponse response) throws JSONException, IOException{
    	String codTipInt = request.getParameter("codTipInt");
    	BudgetTipoInterventoDao dao = new BudgetTipoInterventoDao(getEntityManager());
    		List<BudgetTipIntervento> impegni = dao.findByCodTipint(codTipInt);
    		JSONArray jsa = new JSONArray();
    		for(BudgetTipIntervento impegno : impegni){
    			 JSONObject jso = new JSONObject();
    			 jso.put("codiceImpegno", impegno.getBudgetTipInterventoPK().getCodImpe());
    			 jso.put("anno", impegno.getBudgetTipInterventoPK().getCodAnno());
    			 jsa.put(jso);
      }
    		this.mandaRisposta(response, jsa, null);
    }

    /**
     * Carica dalla base di dati la lista delle classi delle tipologie di
     * intervento e la restituisce in formato JSON. La risposta conterrà una
     * lista di chiavi associate a un testo da visualizzare.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaClasseTipoInterventoFatturazioni")
    public void listaClasseTipoInterventoFatturazioni(ResourceResponse response) throws Exception {
        listaClasseTipoIntervento(response);
    }

    /**
     * Carica dalla base di dati la lista completa di tipologie degli interventi
     * e la scerive response in formato JSON. La risposta conterrà una lista di
     * chiavi associate a un testo da visualizzare. La lista delle tipologie è
     * filtrata per le tipologie fatturabili.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaTipoInterventoFatturazioni")
    public void listaTipoInterventoFatturazioni(ResourceResponse response) throws IOException, JSONException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(TipologiaIntervento.class);
        Predicate p = cb.equal(r.get("flgFatt"), "S");
        c.where(p);
        c.orderBy(cb.asc(r.get("desTipint")));
        List<TipologiaIntervento> tipologie = (List<TipologiaIntervento>) getEntityManager().createQuery(c).getResultList();
        JSONArray jsa = new JSONArray();
        for (TipologiaIntervento tipo : tipologie) {
            JSONObject jso = new JSONObject();
            jso.put("id", tipo.getCodTipint());
            jso.put("label", tipo.getDesTipint());
            jso.put("classe", tipo.getIdParamClasseTipint().getIdParamIndata());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Carica dalla base di dati la lista delle classi delle tipologie di
     * intervento e la restituisce in formato JSON. La risposta conterrà una
     * lista di chiavi associate a un testo da visualizzare.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaClasseTipoInterventoPagamenti")
    public void listaClasseTipoInterventoPagamenti(ResourceResponse response) throws Exception {
        listaClasseTipoIntervento(response);
    }

    /**
     * Carica dalla base di dati la lista completa di tipologie degli interventi
     * e la scerive response in formato JSON. La risposta conterrà una lista di
     * chiavi associate a un testo da visualizzare. La lista delle tipologie è
     * filtrata per le tipologie pagabili.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaTipoInterventoPagamenti")
    public void listaTipoInterventoPagamenti(ResourceResponse response) throws IOException, JSONException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(TipologiaIntervento.class);
        Predicate p = cb.equal(r.get("flgPagam"), "S");
        c.where(p);
        c.orderBy(cb.asc(r.get("desTipint")));
        List<TipologiaIntervento> tipologie = (List<TipologiaIntervento>) getEntityManager().createQuery(c).getResultList();
        JSONArray jsa = new JSONArray();
        for (TipologiaIntervento tipo : tipologie) {
            JSONObject jso = new JSONObject();
            jso.put("id", tipo.getCodTipint());
            jso.put("label", tipo.getDesTipint());
            jso.put("classe", tipo.getIdParamClasseTipint().getIdParamIndata());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }

    /**
     * Carica dalla base di dati la lista completa delle UOT/struttute.
     *
     * @param response Risposta al browser
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @ResourceMapping(value = "listaUOT")
    public void listaUOT(ResourceResponse response) throws IOException, JSONException {
        JSONArray jsa = new JSONArray();
        Query q = getEntityManager().createQuery("SELECT p FROM ParametriIndata p "
                + "WHERE p.idParam.tipParam.tipParam = 'uo' "
                + "AND p.dtIniVal = ( "
                + "SELECT MAX(p2.dtIniVal) FROM ParametriIndata p2 "
                + "WHERE p.idParam.idParam = p2.idParam.idParam "
                + "AND p2.dtIniVal <= :oggi "
                + ") "
                + "ORDER BY p.desParam ");
        q.setParameter("oggi", new Date());
        List<ParametriIndata> parametri = q.getResultList();
        JSONObject jso = new JSONObject();
        jso.put("id", "");
        jso.put("label", "-");
        jsa.put(jso);
        for (ParametriIndata parametro : parametri) {
            jso = new JSONObject();
            jso.put("id", parametro.getIdParamIndata());
            jso.put("label", parametro.getDesParam());
            jsa.put(jso);
        }
        this.mandaRisposta(response, jsa, null);
    }
}

