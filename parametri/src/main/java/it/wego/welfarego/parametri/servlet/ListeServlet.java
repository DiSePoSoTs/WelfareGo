/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.gson.Gson;
import it.trieste.comune.ssc.json.JSonUtils;
import it.trieste.comune.ssc.json.JsonMessage;
import it.wego.web.WebUtils;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.ListeBean;
import it.wego.welfarego.parametri.model.json.JSONListe;
import it.wego.welfarego.persistence.dao.ListaAttesaDao;
import it.wego.welfarego.persistence.entities.ListaAttesa;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 * @author Michele
 */
public class ListeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private Logger logger = LogManager.getLogger(ListeServlet.class);

    // ritorno tutti i template
    public Object load(Map<String, String> parameters) {

        // creo l'EntityManager
        EntityManager em = Connection.getEntityManager();

        // mi collego al dao per le tipologie
        ListaAttesaDao dao = new ListaAttesaDao(em);

        // carico la lista dei gruppi (roots)
        List<ListaAttesa> listaDao = dao.findAll();

        List<ListeBean> array = new ArrayList<ListeBean>();

        ListeBean bean;

        for (ListaAttesa r : listaDao) {
            bean = new ListeBean();
            array.add(bean.serialize(r));
        }

        em.close();

        return new JSONListe(true, array);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            /**
             * Per quanto riguarda i parametri
             */
            ListaAttesa l = new ListaAttesa();

            l.setCodListaAtt(Integer.valueOf(request.getParameter("cod_lista_att")));
            l.setDesListaAtt(request.getParameter("des_lista_att"));
            l.setFlgAs(getCharIfNull(request.getParameter("flg_as")));
            l.setFlgBina(getCharIfNull(request.getParameter("flg_bina")));
            l.setFlgCodFisc(getCharIfNull(request.getParameter("flg_cod_fisc")));
            l.setFlgCog(getCharIfNull(request.getParameter("flg_cog")));
            l.setFlgDistSan(getCharIfNull(request.getParameter("flg_dist_san")));
            l.setFlgDtDom(getCharIfNull(request.getParameter("flg_dt_dom")));
            l.setFlgDtNasc(getCharIfNull(request.getParameter("flg_dt_nasc")));
            l.setFlgIsee(getCharIfNull(request.getParameter("flg_isee")));
            l.setFlgNom(getCharIfNull(request.getParameter("flg_nom")));
            l.setFlgNumFigli(getCharIfNull(request.getParameter("flg_num_figli")));
            l.setFlgRef(getCharIfNull(request.getParameter("flg_ref")));
            l.setFlgTipint(getCharIfNull(request.getParameter("flg_tipint")));
            l.setFlgUot(getCharIfNull(request.getParameter("flg_out")));

            em.getTransaction().begin();

            em.merge(l);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {
            
            e.printStackTrace();
            logger.debug("ListeServlet save() : " + e);
            
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
    
    
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {
            em.getTransaction().begin();

            String codListaAtt = request.getParameter("codListaAtt");

            ListaAttesaDao dao = new ListaAttesaDao(em);
            
            // carico l'utente
            ListaAttesa la = dao.findByKey(Integer.valueOf(codListaAtt));

            // cancello l'utente
            em.remove(la);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Eliminazione eseguita correttamente.");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            logger.debug("ListeServlet delete() : " + e);
            
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
    

    /**
     * Funzione comoda per questo form, 
     * se null il parametro ritorna N
     * Altrimenti S
     * @return 
     */
    public Character getCharIfNull(String parametro) {
        if (parametro == null) {
            return 'N';
        } else {
            return 'S';
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
                case DELETE:
                    delete(request, response);
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

        LOAD, SAVE, DELETE
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
