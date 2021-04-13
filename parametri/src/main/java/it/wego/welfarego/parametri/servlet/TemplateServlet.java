/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Strings;
import com.sun.star.embed.UnreachableStateException;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.TemplateBean;
import it.wego.welfarego.parametri.model.json.JSONTemplate;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @author Michele
 */
public class TemplateServlet extends JsonServlet {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
//    private Logger logger = getLogger();

    // ritorno tutti i template
    public Object load() {

        // creo l'EntityManager
        EntityManager em = Connection.getEntityManager();
        try {
            // mi collego al dao
            TemplateDao dao = new TemplateDao(em);

            // carico la lista dei gruppi (roots)
            List<Template> listaDao = dao.findAll();

            List<TemplateBean> listaBean = new ArrayList<TemplateBean>();

            TemplateBean bean;

            for (Template tpl : listaDao) {
                bean = new TemplateBean();
                listaBean.add(bean.serialize(tpl));
            }
            return new JSONTemplate(true, listaBean);
        } finally {
            em.close();
        }


    }

    /**
     * Salvo il form in db e carico il template in base64 nel clob
     *
     * @param request
     * @param response
     * @return
     */
    public Object save() throws Exception {

//
//        Gson gson = new Gson();

//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String codTmpl = getParameter("cod_tmpl");
        String des_tmpl = getParameter("des_tmpl");

        String upload64 = getParameter("template_file_base64");
        String filename = getParameter("template_file_filename");

        try {

            // il nuovo template
            Template tmpl = new Template();

            // prima di tutto verifico se in db esiste già questo template
            TemplateDao dao = new TemplateDao(em);
            Template oldtmlp = dao.findByCodTemplate(Integer.valueOf(codTmpl));

            if (!Strings.isNullOrEmpty(filename)) {
                filename = filename.replaceAll("[^a-zA-Z0-9]+", "_").replaceFirst("[_]*....$", ".odt").replaceFirst("^[_]*", "");
//                tmpl.setClobTmpl(DynamicodtPreprocessor.processB64Odt(upload64));
                tmpl.setClobTmpl(upload64);
                tmpl.setNomeFile(filename);
            } else if (oldtmlp != null) {

                tmpl.setClobTmpl(oldtmlp.getClobTmpl());
                tmpl.setNomeFile(oldtmlp.getNomeFile());
            }


            tmpl.setCodTmpl(Integer.valueOf(codTmpl));
            tmpl.setDesTmpl(des_tmpl);

            em.getTransaction().begin();
            em.merge(tmpl);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;
        } catch (Exception e) {

//            e.printStackTrace();
//            logger.debug("TemplateServlet save() : " + e);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
//            data.setCode("KO");
//            data.setMessage(e.getMessage());
//
//            jsonObj.setSuccess(false);
//            jsonObj.setData(data);
            throw e;
        } finally {

            em.close();
//            String json = gson.toJson(jsonObj);
//            out.write(json);
//            out.flush();
//            out.close();

        }


    }

    private Object delete()
            throws Exception {

//        Gson gson = new Gson();

//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codTmpl = getParameter("codTmpl");

            TemplateDao dao = new TemplateDao(em);

            Template t = dao.findByCodTemplate(Integer.valueOf(codTmpl));

            em.getTransaction().begin();

            Template managed = em.merge(t);

            em.remove(managed);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;
        } catch (Exception e) {

//            e.printStackTrace();
//            logger.debug("TemplateServlet delete() : " + e);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
//            data.setCode("KO");
//            data.setMessage("Impossibile eliminare il dato poiché risultà in uso.");
//
//            jsonObj.setSuccess(false);
//            jsonObj.setData(data);
            throw e;
        } finally {

            em.close();
//            String json = gson.toJson(jsonObj);
//            out.write(json);
//            out.flush();
//            out.close();

        }

    }

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {

//        try {
//        Map<String, String> parameters = WebUtils.getParametersMap(request);
//        String action = getAction();

//            Object obj = null;
        switch (getAction(Operation.class)) {
            case LOAD:
                return load();
//                    break;
            case SAVE:
                return save();
//                    break;
            case DELETE:
                return delete();
//                    break;
            }

        throw new UnreachableStateException();
//        return JsonBuilder.newInstance().buildResponse();

//            JSonUtils.getGson().toJson(obj, out);

//        } catch (Throwable e) {
//            logger.error("error serving request", e);
//            out.write(JSonUtils.getGson().toJson(new JsonMessage(e)));
//        } finally {
//            out.flush();
//            out.close();
//        }
    }

    private static enum Operation {

        LOAD, SAVE, DELETE
    }
}
