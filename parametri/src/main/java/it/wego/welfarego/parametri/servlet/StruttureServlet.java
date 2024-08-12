/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.web.WebUtils;

import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.StrutturaBean;
import it.wego.welfarego.parametri.model.json.JSONStruttura;
import it.wego.welfarego.persistence.dao.StrutturaDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.Struttura;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

/*
 * @author Michele
 */
public class StruttureServlet extends JsonServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *Caricamento strutture in base al codice intervento passato 
     *
     * @param parameters
     * @return
     */
    public Object load(Map<String, String> parameters) {

        String codTipint = parameters.get("codTipint");

        if (!codTipint.isEmpty()) {
            // String tipoIntervento = parameters.get("tipo_intervento");
            EntityManager em = Connection.getEntityManager();
            
           StrutturaDao dao = new StrutturaDao(em);
           List<Struttura> strutture = dao.findStrutturaByCodTipInt(codTipint);
           List<StrutturaBean> listaStruttureBean = new ArrayList<StrutturaBean>();
            StrutturaBean strutturaBean;

            for (Struttura s : strutture) {
               strutturaBean = new StrutturaBean();
              listaStruttureBean.add(strutturaBean.serialize(s));
            }

            em.close();

            return new JSONStruttura(true, listaStruttureBean);
        }

        return null;
    }

    

    private JSONGeneric insertStruttura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

//        Gson gson = new Gson();
//
//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String codTipint = request.getParameter("cod_tipint");
        String nome = request.getParameter("nome");
        String indirizzo = request.getParameter("indirizzo");
        String costo = request.getParameter("costo");
        
        
        String codCsr = request.getParameter("cod_csr");
        
        String id = Strings.emptyToNull(request.getParameter("id"));
       

        try {
        	Struttura s;
        	StrutturaDao sdao = new StrutturaDao(em);
        	if(id!=null){
        		s= sdao.findById(Integer.valueOf(id));
        	}
        	else{
        		s= new Struttura();
        	}
        	
        	s.setIndirizzo(indirizzo);
        	s.setNome(nome);
        	s.setIntervento(new TipologiaInterventoDao(em).findByCodTipint(codTipint));
        	s.setCsrId(codCsr);
        	if(s.getId()==null){
        		sdao.insert(s);
        	}
        	else{
        		sdao.update(s);
        	}


            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }

    }

   
    private Object deleteStruttura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

//        Gson gson = new Gson();

//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();
        String id = request.getParameter("id");

        try {
            StrutturaDao sdao = new StrutturaDao(em);
            Struttura daCancellare = sdao.findById(Integer.valueOf(id));
            if(daCancellare!=null){
            	sdao.delete(daCancellare);
            }
            
            

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }

    }

    public Object save(Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object proceed(Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {

//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();

//        try {
        Map<String, String> parameters = WebUtils.getParametersMap(request);
        String action = parameters.get("action");

//            Object obj = null;
        switch (Operation.valueOf(action.toUpperCase())) {
            case LOAD:
                return load(parameters);
//                    break;
            case SAVE:
                return save(parameters);
//                    break;
            case PROCEED:
                return proceed(parameters);
//                    break;
           
//        
//                    break;
            case INSERTSTRUTTURA:
                return insertStruttura(request, response);
//                    break;
            case DELETESTRUTTURA:
                return deleteStruttura(request, response);
//                    break;
        


        }

        return null;
//            JSonUtils.getGson().toJson(obj, out);
//
//        } catch (Throwable e) {
//            logger.error("error serving request", e);
//            out.write(JSonUtils.getGson().toJson(new JsonMessage(e)));
//        } finally {
//            out.flush();
//            out.close();
//        }
    }

    private static enum Operation {

        LOAD, SAVE, PROCEED, INSERTSTRUTTURA, DELETESTRUTTURA
        }
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response, METHOD_GET);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response, METHOD_POST);
//    }
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
}
