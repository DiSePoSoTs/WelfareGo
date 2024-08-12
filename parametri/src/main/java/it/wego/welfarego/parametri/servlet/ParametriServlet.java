/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JSonUtils;
import it.trieste.comune.ssc.json.JsonMessage;
import it.wego.web.WebUtils;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.ParametriFormBean;
import it.wego.welfarego.parametri.model.ParametriTreeBean;
import it.wego.welfarego.parametri.model.json.JSONParams;
import it.wego.welfarego.persistence.dao.ParametriDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TipologiaParametriDao;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaParametri;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 * @author Michele
 */
public class ParametriServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private Logger logger = LogManager.getLogger(ParametriServlet.class);

    public Object load(Map<String, String> parameters) {

        // creo nodo padre vuoto
        ParametriTreeBean root = new ParametriTreeBean();

        // creo l'EntityManager
        EntityManager em = Connection.getEntityManager();

        // mi collego al dao per le tipologie
        TipologiaParametriDao tipoDao = new TipologiaParametriDao(em);

        // carico la lista delle tipologie (roots)
        List<TipologiaParametri> tipologieLista = tipoDao.findAllOrderedByDesc();

        // mi collego al dao dei parametri (foglie)
        ParametriDao paramDao = new ParametriDao(em);

        // preparo nodi figli vuoti (le tipologie)
        List<ParametriTreeBean> nodi = new ArrayList<ParametriTreeBean>();

        // ciclo sulle tipologie del db
        for (TipologiaParametri tp : tipologieLista) {

            // imposto il padre
            ParametriTreeBean figlio = new ParametriTreeBean();
            // figlio.setPk(tp.getTipParam());
            figlio.setText(tp.getDesTipParam());
            figlio.setQtip(tp.getDesTipParam());
            figlio.setLeaf(false);

            // cerco eventuali figli su dao
            List<Parametri> paramLista = paramDao.findByTipParam(tp.getTipParam());

            // se ci sono figli
            if (paramLista.isEmpty() != true) {

                // preparo una lista di foglie vuote (i parametri)
                List<ParametriTreeBean> foglie = new ArrayList<ParametriTreeBean>();

                // ciclo sulle foglie vuote
                for (Parametri p : paramLista) {

                    // creo il bean per la foglia
                    ParametriTreeBean foglia = new ParametriTreeBean();
                    foglia.setPk(p.getIdParam().toString());
                    foglia.setTip_param(p.getTipParam().getTipParam());
                    foglia.setText(p.getCodParam());
                    foglia.setQtip(p.getCodParam());
                    foglia.setLeaf(true);

                    // aggiungo il bean foglia alla lista foglie
                    foglie.add(foglia);
                }

                // aggiungo la lista foglie al bean
                figlio.setChildren(foglie);

            } else {
                figlio.setChildren(Collections.<ParametriTreeBean>emptyList());
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

        EntityManager em = Connection.getEntityManager();

        ParametriFormBean bean = new ParametriFormBean();

        String idParam = parameters.get("idParam");
        String tipParam = parameters.get("tipParam");


        if (!idParam.isEmpty()) {

            ParametriDao pdao = new ParametriDao(em);
            Parametri parametro = pdao.findByKey(Integer.parseInt(idParam), tipParam);

            // prima parte del form
            bean.setIdParam(idParam);
            bean.setCodParam(parametro.getCodParam());
            bean.setTipParam(tipParam);
            bean.setFlg_attivo(String.valueOf(parametro.getFlgAttivo()));

            // seconda parte da parametri indata
            ParametriIndataDao indao = new ParametriIndataDao(em);

            // potrebbero non esserci parametri_indata per questo parametro
//            try {

            ParametriIndata pindata = indao.getLastParamIndata(Integer.parseInt(idParam));
            if (pindata != null) {
                bean.setIdParamIndata(pindata.getIdParamIndata().toString());
                bean.setDateParam(StringConversion.dateToItString(pindata.getDateParam()));
                bean.setDesParam(pindata.getDesParam());

                if (pindata.getDecimalParam() != null) {
                    bean.setDecimalParam(pindata.getDecimalParam().toString());
                } else {
                    bean.setDecimalParam("");
                }

                bean.setTxt1Param(pindata.getTxt1Param());
                bean.setTxt2Param(pindata.getTxt2Param());
                bean.setTxt3Param(pindata.getTxt3Param());
                bean.setTxt4Param(pindata.getTxt4Param());
            }
//            } catch (NoResultException e) {
//                // se il parametro indata non c'è
//                // semplicemente non lo carico nel bean
//            }

        }

        em.close();

        return new JSONParams(true, bean);

    }

    private void deleteParam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            em.getTransaction().begin();

            String idParam = request.getParameter("idParam");
            String tipParam = request.getParameter("tipParam");

            ParametriDao dao = new ParametriDao(em);
            // carico il parametro
            Parametri p = dao.findByKey(Integer.valueOf(idParam), tipParam);

            List<ParametriIndata> lista = p.getParametriIndataList();

            for (ParametriIndata pin : lista) {
                ParametriIndata managed = em.merge(pin);
                em.remove(managed);
            }

            // infine cancello il parametro
            // Parametri pm = em.merge(p);
            p.setParametriIndataList(null);
            em.remove(p);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Eliminazione eseguita correttamente.");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("ParametriServlet deleteParam() : " + e);

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

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String idParam = request.getParameter("id_param");
        String flgAttivo = request.getParameter("flg_attivo");
        String tipParam = request.getParameter("tip_param");
        String codParam = request.getParameter("cod_param");
        String desParam = request.getParameter("des_param");
        String dateParam = Strings.emptyToNull(request.getParameter("date_param"));
        String idParamIndata = request.getParameter("id_param_indata");
        String decimalParam = request.getParameter("decimal_param");
        String txt1Param = request.getParameter("txt1_param");
        String txt2Param = request.getParameter("txt2_param");
        String txt3Param = request.getParameter("txt3_param");
        String txt4Param = request.getParameter("txt4_param");

        try {


            // la tipologia
            TipologiaParametriDao tdao = new TipologiaParametriDao(em);
            TipologiaParametri tip = tdao.findByTipParam(tipParam);

            // è un parametro indata o no?
            char indata = tip.getFlgIndata();


            /**
             * Per quanto riguarda i parametri
             */
            ParametriDao dao = new ParametriDao(em);
            Parametri p;

            em.getTransaction().begin();


            // si tratta di un insert
            if (idParam.isEmpty()) {

                p = new Parametri();

                p.setTipParam(tip);

                // codice parametro
                p.setCodParam(codParam);
                p.setFlgAttivo(flgAttivo.charAt(0));
                // salvo la entity
                em.persist(p);

            } else {

                // si tratta di un update ma il flag indata è N carico i dati base
                p = dao.findByKey(Integer.valueOf(idParam), tipParam);
                p.setFlgAttivo(flgAttivo.charAt(0));

            }


            /**
             * Per quanto riguarda i parametri in data
             */
            ParametriIndata pin;
            ParametriIndataDao pdao = new ParametriIndataDao(em);

            // di nuovo se è un insert
            if (idParamIndata.isEmpty() || indata == 'S') {
                pin = new ParametriIndata();
            } else {
                // o se è un update
                pin = pdao.findByIdParam(Integer.valueOf(idParam));
            }

            // la descrizione è obbligatoria
            pin.setDesParam(desParam);

            // la data può non esserlo
            if (dateParam != null) // datefield passa null non ""
            {
                pin.setDateParam(StringConversion.itStringToDate(dateParam));
            }

            // imposto sempre una data valida per il campo dt_ini_val
            pin.setDtIniVal(new Date()); // automaticamente

            if (!decimalParam.isEmpty()) {
                pin.setDecimalParam(new BigDecimal(decimalParam));
            }

            pin.setTxt1Param(txt1Param);
            pin.setTxt2Param(txt2Param);
            pin.setTxt3Param(txt3Param);
            pin.setTxt4Param(txt4Param);

            pin.setIdParam(p);
            p.getParametriIndataList().add(pin);

            // se è un insert o un "parametri_indata Si"
            if (idParam.isEmpty()) {

                em.persist(p);
                em.persist(pin);

            } else {

                em.merge(pin);

            }

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("ParametriServlet save() : " + e);

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

    public Object proceed(Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
                case SAVE:
                    save(request, response);
                    break;
                case PROCEED:
                    obj = proceed(parameters);
                    break;
                case LOADFORM:
                    obj = loadForm(parameters);
                    break;
                case DELETEPARAM:
                    deleteParam(request, response);
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

        LOAD, SAVE, PROCEED, LOADFORM, DELETEPARAM
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
    }// </editor-fold>
}
