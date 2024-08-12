/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.web.WebUtils;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.TipologieTreeBean;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.ListaAttesa;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 * @author Michele
 */
public class TipologieServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private Logger logger = LogManager.getLogger(TipologieServlet.class);
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
    private DecimalFormat df = (DecimalFormat)nf;
    
    public Object load(Map<String, String> parameters) {

        // creo nodo padre vuoto
        TipologieTreeBean root = new TipologieTreeBean();

        // creo l'EntityManager
        EntityManager em = Connection.getEntityManager();

        // mi collego al dao per le tipologie
        ParametriIndataDao dao = new ParametriIndataDao(em);
        List<ParametriIndata> parametriCI = dao.findByTipParam(Parametri.CLASSE_INTERVENTO);

        // mi collego al dao dei parametri (foglie)
        TipologiaInterventoDao tipIntDao = new TipologiaInterventoDao(em);

        // preparo nodi figli vuoti (le tipologie)
        List<TipologieTreeBean> nodi = new ArrayList<TipologieTreeBean>();

        // ciclo sulle tipologie del db
        for (ParametriIndata pin : parametriCI) {

            // imposto il padre
            TipologieTreeBean figlio = new TipologieTreeBean();
            // figlio.setPk(tp.getCodGrpTipint());
            figlio.setText(pin.getDesParam());
            figlio.setQtip(pin.getDesParam());
            figlio.setLeaf(false);

            // cerco eventuali figli su dao
            List<TipologiaIntervento> tipIntLista = tipIntDao.findByClasse(pin);

            // se ci sono figli
            if (tipIntLista.isEmpty() != true) {

                // preparo una lista di foglie vuote (i tipi intervento)
                List<TipologieTreeBean> foglie = new ArrayList<TipologieTreeBean>();

                // ciclo sulle foglie vuote
                for (TipologiaIntervento t : tipIntLista) {

                    // creo il bean per la foglia
                    TipologieTreeBean foglia = new TipologieTreeBean();
                    foglia.setPk(t.getCodTipint());
                    foglia.setText(t.getDesTipint());
                    foglia.setLeaf(true);
                    foglia.setQtip(t.getDesTipint());

                    // aggiungo il bean foglia alla lista foglie
                    foglie.add(foglia);
                }

                // aggiungo la lista foglie al bean
                figlio.setChildren(foglie);

            } else {
                figlio.setLeaf(true);
            }

            // aggiungo l'intero oggetto bean alla lista nodi
            nodi.add(figlio);
        }

        // aggiungo tutti i nodi dell'albero al root Bean
        root.setChildren(nodi);

        em.close();

        // ritorno la lista <Bean>
        return root;

    }

    public Object loadForm(Map<String, String> parameters) {
        String codTipint = parameters.get("codTipint");
        EntityManager em = Connection.getEntityManager();
        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        if (!codTipint.isEmpty()) {
            jsonBuilder.withData(new TipologiaInterventoDao(em).findByCodTipint(codTipint)).withTransformer(new JsonMapTransformer<TipologiaIntervento>() {
                @Override
                public void transformToMap(TipologiaIntervento tipologiaIntervento) {
                    put("cod_int_csr", tipologiaIntervento.getCodIntCsr());
                    put("cod_tipint_csr", tipologiaIntervento.getCodTipintCsr());
                    if (tipologiaIntervento.getCodListaAtt() != null) {
                        put("cod_lista_att", tipologiaIntervento.getCodListaAtt().getCodListaAtt().toString());
                    }
                    put("cod_tipint", tipologiaIntervento.getCodTipint());
                    if (tipologiaIntervento.getCodTmplChius() != null) {
                        put("cod_tmpl_chius", tipologiaIntervento.getCodTmplChius().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplComliq() != null) {
                        put("cod_tmpl_comliq", tipologiaIntervento.getCodTmplComliq().getCodTmpl().toString());
                    } else {
                        put("cod_tmpl_comliq", "null");
                    }
                    if (tipologiaIntervento.getCodTmplEse() != null) {
                        put("cod_tmpl_ese", tipologiaIntervento.getCodTmplEse().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplLettPag() != null) {
                        put("cod_tmpl_lett_pag", tipologiaIntervento.getCodTmplLettPag().getCodTmpl().toString());
                    } else {
                        put("cod_tmpl_lett_pag", "null");
                    }
                    if (tipologiaIntervento.getCodTmplVar() != null) {
                        put("cod_tmpl_var", tipologiaIntervento.getCodTmplVar().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplChiusMul() != null) {
                        put("cod_tmpl_chius_mul", tipologiaIntervento.getCodTmplChiusMul().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplEseMul() != null) {
                        put("cod_tmpl_ese_mul", tipologiaIntervento.getCodTmplEseMul().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplVarMul() != null) {
                        put("cod_tmpl_var_mul", tipologiaIntervento.getCodTmplVarMul().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getCodTmplRicevuta() != null) {
                        put("cod_tmpl_ricevuta", tipologiaIntervento.getCodTmplRicevuta().getCodTmpl().toString());
                    }
                    if (tipologiaIntervento.getTmplDocumentoDiAutorizzazione() != null) {
                        put("cod_tmpl_documento_di_autorizzazione", tipologiaIntervento.getTmplDocumentoDiAutorizzazione().getCodTmpl().toString());
                    }
                    put("flg_ricevuta", tipologiaIntervento.getFlgRicevuta());
                    put("flg_documento_di_autorizzazione", tipologiaIntervento.getFlgDocumentoDiAutorizzazione());
                    put("des_tipint", tipologiaIntervento.getDesTipint());
                    put("flg_fatt", Character.toString(tipologiaIntervento.getFlgFatt()));
                    put("flg_pagam", Character.toString(tipologiaIntervento.getFlgPagam()));
                    put("flg_pai", Character.toString(tipologiaIntervento.getFlgPai()));
                    put("ccele", String.valueOf(tipologiaIntervento.getCcele()));
                    if (tipologiaIntervento.getIdParamClasseTipint() != null) {
                        put("id_param_classe_tipint", tipologiaIntervento.getIdParamClasseTipint().getIdParamIndata().toString());
                    }
                    put("imp_std_costo", tipologiaIntervento.getImpStdCosto().toString());
                    put("imp_std_entr", tipologiaIntervento.getImpStdEntr().toString());
                    put("imp_std_spesa", tipologiaIntervento.getImpStdSpesa().toString());
                    if (tipologiaIntervento.getIdParamStruttura() != null) {
                        put("id_param_struttura", tipologiaIntervento.getIdParamStruttura().getIdParamIndata().toString());
                    }
                    if (tipologiaIntervento.getIdParamUniMis() != null) {
                        put("id_param_uni_mis", tipologiaIntervento.getIdParamUniMis().getIdParamIndata().toString());
                    }
                    if (tipologiaIntervento.getIdParamSrv() != null) {
                        put("id_param_srv", tipologiaIntervento.getIdParamSrv().getIdParamIndata().toString());
                    }
                    put("flg_vis", String.valueOf(tipologiaIntervento.getFlgVis()));
                    put("flg_app_tec", String.valueOf(tipologiaIntervento.getFlgAppTec()));
                    put("flgFineDurata", tipologiaIntervento.getFlgFineDurata());
                    put("flgDeveRestareAperto", tipologiaIntervento.getDeveRestareAperto());
                    put("flgRinnovo",tipologiaIntervento.getFlgRinnovo());
                    put("responsabileProcedimento", tipologiaIntervento.getResponsabileProcedimento());
                    put("ufficioDiRiferimento", tipologiaIntervento.getUfficioDiRiferimento());
                    put("aliquotaIva", tipologiaIntervento.getIpAliquotaIva() == null ? null : tipologiaIntervento.getIpAliquotaIva().getIdParamIndata().toString());
                }
            });
        } else {
            jsonBuilder.withData(Maps.newHashMap()); //empty data
        }
        Object res = jsonBuilder.buildResponse();

        em.close();

        return res;
    }

    private void insertIntervento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        df.setParseBigDecimal(true);
        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String idParamStruttura = request.getParameter("id_param_struttura");
        String codListaAtt = request.getParameter("cod_lista_att");

        try {
            ParametriIndataDao parametriIndataDao = new ParametriIndataDao(em);
            TipologiaInterventoDao dao = new TipologiaInterventoDao(em);
            dao.initTransaction();

            String codTipInt = request.getParameter("cod_tipint");
            TipologiaIntervento tipint = dao.findByCodTipint(codTipInt);
            if (tipint == null) {
                tipint = new TipologiaIntervento(codTipInt);
                dao.insert(tipint);
            }

            tipint.setDesTipint(request.getParameter("des_tipint"));
            tipint.setFlgPai(request.getParameter("flg_pai").charAt(0));
            tipint.setCodIntCsr(request.getParameter("cod_int_csr"));
            tipint.setCodTipintCsr(request.getParameter("cod_tipint_csr"));

            // lookup
            if (!StringUtils.isEmpty(request.getParameter("id_param_classe_tipint"))) {
                tipint.setIdParamClasseTipint(parametriIndataDao.getReference(request.getParameter("id_param_classe_tipint")));
            }

            // lookup
            if (!StringUtils.isEmpty(request.getParameter("id_param_srv"))) {
                tipint.setIdParamSrv(parametriIndataDao.getReference(request.getParameter("id_param_srv")));
            }

            if (!StringUtils.isEmpty(request.getParameter("imp_std_costo"))) {
                tipint.setImpStdCosto((BigDecimal) df.parse(request.getParameter("imp_std_costo")));
            }

            // lookup
            if (codListaAtt != null) {
                tipint.setCodListaAtt(em.getReference(ListaAttesa.class, Integer.valueOf(request.getParameter("cod_lista_att"))));
            } else {
                tipint.setCodListaAtt(null);
            }

            // lookup
            if (idParamStruttura != null) {
                tipint.setIdParamStruttura(parametriIndataDao.getReference(request.getParameter("id_param_struttura")));
            } else {
                tipint.setIdParamStruttura(null);
            }


            if (!StringUtils.isEmpty(request.getParameter("flg_fatt"))) {
                tipint.setFlgFatt(request.getParameter("flg_fatt").charAt(0));
            }

            if (!StringUtils.isEmpty(request.getParameter("flg_pagam"))) {
                tipint.setFlgPagam(request.getParameter("flg_pagam").charAt(0));
            }

            if (!StringUtils.isEmpty(request.getParameter("imp_std_spesa"))) {
                tipint.setImpStdSpesa((BigDecimal) df.parse(request.getParameter("imp_std_spesa")));
            }

            // @todo sistemare questi due campi che non hanno corrispondenza sul form
            if (!StringUtils.isEmpty(request.getParameter("ccele"))) {
                tipint.setCcele(request.getParameter("ccele"));
            }

            if (!StringUtils.isEmpty(request.getParameter("imp_std_entr"))) {
                tipint.setImpStdEntr((BigDecimal) df.parse(request.getParameter("imp_std_entr")));
            }

            // per i template tocca fare altri lookup
            TemplateDao templateDao = new TemplateDao(em);

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_ese"))) {
                tipint.setCodTmplEse(templateDao.getReference(request.getParameter("cod_tmpl_ese")));
            }

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_var"))) {
                tipint.setCodTmplVar(templateDao.getReference(request.getParameter("cod_tmpl_var")));
            }

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_chius"))) {
                tipint.setCodTmplChius(templateDao.getReference(request.getParameter("cod_tmpl_chius")));
            }

            // ora anche per i campi tmpl _mul
            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_ese_mul"))) {
                tipint.setCodTmplEseMul(templateDao.getReference(request.getParameter("cod_tmpl_ese_mul")));
            }

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_var_mul"))) {
                tipint.setCodTmplVarMul(templateDao.getReference(request.getParameter("cod_tmpl_var_mul")));
            }

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_chius_mul"))) {
                tipint.setCodTmplChiusMul(templateDao.getReference(request.getParameter("cod_tmpl_chius_mul")));
            }

            // tutti vogliono un template, questo vuole un intero...
            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_comliq"))) {
                String codTmpl = request.getParameter("cod_tmpl_comliq");
                tipint.setCodTmplComliq(Objects.equal("null", codTmpl) ? null : templateDao.getReference(codTmpl));
            }

            if (!StringUtils.isEmpty(request.getParameter("cod_tmpl_lett_pag"))) {
                String codTmpl = request.getParameter("cod_tmpl_lett_pag");
                tipint.setCodTmplLettPag(Objects.equal("null", codTmpl) ? null : templateDao.getReference(codTmpl));
            }

            if (!StringUtils.isEmpty(request.getParameter("id_param_uni_mis"))) {
                tipint.setIdParamUniMis(em.getReference(ParametriIndata.class, Integer.valueOf(request.getParameter("id_param_uni_mis"))));
            }


            /*RICEVUTA*/
            tipint.setFlgRicevuta(request.getParameter("flg_ricevuta"));

            String templateStr = request.getParameter("cod_tmpl_ricevuta");
            if (!Strings.isNullOrEmpty(templateStr)) {
                tipint.setCodTmplRicevuta(em.getReference(Template.class, Integer.valueOf(templateStr)));
            }
            if (Objects.equal(tipint.getFlgRicevuta(), TipologiaIntervento.FLG_RICEVUTA_S)) {
                Validate.notNull(tipint.getCodTmplRicevuta(), "il template della ricevuta e' obbligatorio, se il flag ricevuta e' impostato a 'S'");
            }

            tipint.setFlgDocumentoDiAutorizzazione(request.getParameter("flg_documento_di_autorizzazione").charAt(0));

            String cod_tmpl_documento_di_autorizzazione= request.getParameter("cod_tmpl_documento_di_autorizzazione");
            if (!Strings.isNullOrEmpty(cod_tmpl_documento_di_autorizzazione)) {
                tipint.setTmplDocumentoDiAutorizzazione(em.getReference(Template.class, Integer.valueOf(cod_tmpl_documento_di_autorizzazione)));
            }
            if (Objects.equal(tipint.getFlgDocumentoDiAutorizzazione(), TipologiaIntervento.FLG_RICEVUTA_S)) {
                Validate.notNull(tipint.getTmplDocumentoDiAutorizzazione(), "il template del documento di approvazione e' obbligatorio, se il flag ricevuta e' impostato a 'S'");
            }


            if (!request.getParameter("flg_vis").isEmpty()) {
                tipint.setFlgVis(request.getParameter("flg_vis").charAt(0));
            } else {
                tipint.setFlgVis('S');
            }
            if (!request.getParameter("flg_app_tec").isEmpty()) {
                tipint.setFlgAppTec(request.getParameter("flg_app_tec").charAt(0));
            } else {
                tipint.setFlgAppTec('N');
            }


            tipint.setDeveRestareAperto(Strings.emptyToNull(request.getParameter("flgDeveRestareAperto")));
            tipint.setFlgRinnovo(Strings.emptyToNull(request.getParameter("flgRinnovo")));

            String value = request.getParameter("flgFineDurata");
            if (!StringUtils.isBlank(value)) {
                Validate.isTrue(value.length() == 1);
                tipint.setFlgFineDurata(value.charAt(0));
            }

            tipint.setResponsabileProcedimento(Strings.emptyToNull(request.getParameter("responsabileProcedimento")));
            tipint.setUfficioDiRiferimento(Strings.emptyToNull(request.getParameter("ufficioDiRiferimento")));

            tipint.setIpAliquotaIva(new ParametriIndataDao(em).findByIdParamIndata(request.getParameter("aliquotaIva")));

            // inizio le transazioni con l'em
            dao.commitTransaction();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            logger.debug("TipologieServlet deleteIntervento() : ", e);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            data.setCode("KO");
            data.setMessage(e.getMessage());

            jsonObj.setSuccess(false);
            jsonObj.setData(data);

        } finally {

            em.close();
            String json = gson.toJson(jsonObj);
            out.write(json);
            out.flush();
            out.close();

        }

    }

    private void deleteIntervento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codTipint = request.getParameter("codTipint");

            TipologiaInterventoDao dao = new TipologiaInterventoDao(em);

            TipologiaIntervento tipint = dao.findByCodTipint(codTipint);

            em.getTransaction().begin();
            TipologiaIntervento managed = em.merge(tipint);
            em.remove(managed);
            em.getTransaction().commit();
            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);

            jsonObj.setData(data);

        } catch (Exception e) {


            logger.debug("TipologieServlet deleteIntervento() : ", e);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            data.setCode("KO");
            data.setMessage("Impossibile eliminare il dato poiché risultà in uso.");

            jsonObj.setSuccess(false);
            jsonObj.setData(data);

        } finally {

            em.close();
            String json = gson.toJson(jsonObj);
            out.write(json);
            out.flush();
            out.close();

        }


    }

    // Switcher per operazioni della servlet
    // <editor-fold defaultstate="collapsed" desc="Metodi Standard">
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        try {
            Map<String, String> parameters = WebUtils.getParametersMap(request);
            String action = parameters.get("action");

            Object obj = null;
            switch (Operation.valueOf(action.toUpperCase())) {
                case LOAD:
                    obj = load(parameters);
                    break;
                case LOADFORM:
                    obj = loadForm(parameters);
                    break;
                case INSERTINT:
                    insertIntervento(request, response);
                    break;
                case DELETEINT:
                    deleteIntervento(request, response);
                    break;

            }

            JsonBuilder.getGson().toJson(obj, out);

        } catch (Throwable e) {
            logger.error("error serving request", e);
            JsonBuilder.getGson().toJson(JsonBuilder.buildErrorResponse("si e' verificato un errore : " + e.toString()), out);
        } finally {
            out.flush();
            out.close();
        }
    }

    // lista operazioni disponibili
    private static enum Operation {

        LOAD, LOADFORM, LOADGRUPPI, INSERTGRUPPO, DELETEGRUPPO, INSERTINT, DELETEINT
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, METHOD_GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, METHOD_POST);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
