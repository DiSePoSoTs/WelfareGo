/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import it.wego.json.JSonUtils;
import it.wego.json.JsonMessage;
import it.wego.web.WebUtils;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.DatiBean;
import it.wego.welfarego.parametri.model.MapDatiBean;
import it.wego.welfarego.parametri.model.json.JSONDati;
import it.wego.welfarego.parametri.model.json.JSONMapDati;
import it.wego.welfarego.persistence.dao.DatiSpecificiDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.DatiSpecifici;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipintPK;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.wego.extjs.json.JsonBuilder;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 * @author Michele
 */
public class DatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private Logger logger = LogManager.getLogger(DatiServlet.class);

    public Object load(Map<String, String> parameters) {

        EntityManager em = Connection.getEntityManager();
        DatiSpecificiDao dao = new DatiSpecificiDao(em);

        Iterable<DatiSpecifici> datiSpecificiList = dao.findAll(null, null);
        String filter = parameters.get("filter");
        if (!StringUtils.isBlank(filter)) {
            final Pattern pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);

            datiSpecificiList = Iterables.filter(datiSpecificiList, new Predicate<DatiSpecifici>() {
                public boolean apply(DatiSpecifici input) {
                    return pattern.matcher(input.getCodCampo()).find() || pattern.matcher(input.getDesCampo()).find();
                }

            });
        }

        em.close();

        return JsonBuilder.newInstance().withParameters(parameters).withSourceData(datiSpecificiList).withTransformer(DatiBean.getDatiSpecificiToDatiBeanFunc()).buildStoreResponse();

    }

    public Object loadDisponibili(Map<String, String> parameters) {

        EntityManager em = Connection.getEntityManager();
        DatiSpecificiDao dao = new DatiSpecificiDao(em);

        String codTipint = parameters.get("codTipint");

        TipologiaInterventoDao tipintdao = new TipologiaInterventoDao(em);
        TipologiaIntervento tipint = tipintdao.findByCodTipint(codTipint);

        List<DatiSpecifici> lista = dao.findDisponibili(tipint);
        List<DatiBean> listaBeans = new ArrayList<DatiBean>();

        DatiBean bean;
        for (DatiSpecifici dato : lista) {
            bean = new DatiBean();
            listaBeans.add(bean.serialize(dato));
        }

        em.close();

        return new JSONDati(true, listaBeans, 0);
    }

    public Object loadRelazionati(Map<String, String> parameters) {

        EntityManager em = Connection.getEntityManager();
        DatiSpecificiDao dao = new DatiSpecificiDao(em);

        // determino la tipologia intervento
        String codTipint = parameters.get("codTipint");
        TipologiaInterventoDao tipintdao = new TipologiaInterventoDao(em);
        TipologiaIntervento tipint = tipintdao.findByCodTipint(codTipint);

        List<MapDatiSpecTipint> lista = dao.findRelazionati(tipint);
        List<MapDatiBean> listaBeans = new ArrayList<MapDatiBean>();

        MapDatiBean bean;
        for (MapDatiSpecTipint dato : lista) {
            bean = new MapDatiBean();
            listaBeans.add(bean.serialize(dato));
        }

        em.close();

        return new JSONMapDati(true, listaBeans, 0);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String codCampo = request.getParameter("cod_campo");
        String desCampo = request.getParameter("des_campo");
        String flgObb = request.getParameter("flg_obb");
        String tipoCampo = request.getParameter("tipo_campo");
        String valAmm = request.getParameter("val_amm");
        String flgEdit = request.getParameter("flg_edit");
        String flgVis = request.getParameter("flg_vis");
        String valDef = request.getParameter("val_def");
        String regExpr = request.getParameter("reg_expr");
        String codCampoCsr = request.getParameter("doc_campo_csr");
        String msgErrore = request.getParameter("mag_errore");
        String lunghezza = request.getParameter("lunghezza");
        String decimali = request.getParameter("decimali");

        try {

            DatiSpecifici dati = new DatiSpecifici();

            dati.setCodCampo(codCampo);
            dati.setCodCampoCsr(codCampoCsr);

            if (!"".equals(decimali)) {
                dati.setDecimali(Short.valueOf(decimali));
            }

            dati.setDesCampo(desCampo);
            dati.setFlgEdit(getCharIfNull(flgEdit));
            dati.setFlgObb(getCharIfNull(flgObb));
            dati.setFlgVis(getCharIfNull(flgVis));

            if (!"".equals(lunghezza)) {
                dati.setLunghezza(Short.valueOf(lunghezza));
            }

            dati.setMsgErrore(msgErrore);
            dati.setRegExpr(regExpr);
            dati.setTipoCampo(tipoCampo.toCharArray()[0]);
            dati.setValAmm(valAmm);
            dati.setValDef(valDef);

            em.getTransaction().begin();
            em.merge(dati);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            logger.error("error", e);

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

    private void associa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codCampo = request.getParameter("codCampo");
            String codTipint = request.getParameter("codTipint");

            MapDatiSpecTipintPK pk = new MapDatiSpecTipintPK();
            pk.setCodCampo(codCampo);
            pk.setCodTipint(codTipint);

            MapDatiSpecTipint md = new MapDatiSpecTipint();
            md.setMapDatiSpecTipintPK(pk);
            md.setColCampo((short) 0);
            md.setRowCampo((short) 0);

            em.getTransaction().begin();

            em.merge(md);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

           
            logger.error("error", e);
            
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

    private void disassocia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codCampo = request.getParameter("codCampo");
            String codTipint = request.getParameter("codTipint");

            MapDatiSpecTipintPK pk = new MapDatiSpecTipintPK();
            pk.setCodCampo(codCampo);
            pk.setCodTipint(codTipint);

            MapDatiSpecTipint md = new MapDatiSpecTipint();
            md.setMapDatiSpecTipintPK(pk);

            em.getTransaction().begin();

            MapDatiSpecTipint managed = em.merge(md);
            em.remove(managed);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

           
            logger.error("error", e);

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

    private void aggiorna(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            Map<String, String[]> params = request.getParameterMap();

            String codCampo = params.get("codCampo")[0];
            String codTipint = params.get("codTipint")[0];
            String colCampo = params.get("colCampo")[0];
            String rowCampo = params.get("rowCampo")[0];

            DatiSpecificiDao dao = new DatiSpecificiDao(em);

            MapDatiSpecTipintPK pk = new MapDatiSpecTipintPK();
            pk.setCodCampo(codCampo);
            pk.setCodTipint(codTipint);

            MapDatiSpecTipint md = new MapDatiSpecTipint();
            md.setColCampo(Short.valueOf(colCampo));
            md.setRowCampo(Short.valueOf(rowCampo));
            md.setMapDatiSpecTipintPK(pk);

            em.getTransaction().begin();
            em.merge(md);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("DatiServlet delete() : " + e);

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

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codCampo = request.getParameter("codCampo");

            DatiSpecificiDao dao = new DatiSpecificiDao(em);

            DatiSpecifici ds = dao.findByCodCampo(codCampo);

            em.getTransaction().begin();

            DatiSpecifici managed = em.merge(ds);

            em.remove(managed);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("DatiServlet delete() : " + e);

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
            throws ServletException, IOException {
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
                case SAVE:
                    save(request, response);
                    break;
                case DELETE:
                    delete(request, response);
                    break;
                case LOADDISP:
                    obj = loadDisponibili(parameters);
                    break;
                case LOADREL:
                    obj = loadRelazionati(parameters);
                    break;
                case ASSOCIA:
                    associa(request, response);
                    break;
                case DISASSOCIA:
                    disassocia(request, response);
                    break;
                case UPDATE:
                    aggiorna(request, response);
                    break;
            }

            JSonUtils.getGson().toJson(obj, out);

        } catch (Throwable e) {
            logger.error("error serving request", e);
            out.write(JSonUtils.getGson().toJson(new JsonMessage(e)));
        } finally {
            out.flush();
            out.close();
        }
    }

    private static enum Operation {

        LOAD, SAVE, DELETE, LOADDISP, LOADREL, ASSOCIA, DISASSOCIA, UPDATE
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

    /**
     * Funzione comoda per questo form, se null il parametro ritorna N
     * Altrimenti S
     *
     * @return
     */
    public Character getCharIfNull(String parametro) {
        if (parametro == null) {
            return 'N';
        } else {
            return 'S';
        }
    }
}
