/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.web.WebUtils;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.parametri.model.BudgetBean;
import it.wego.welfarego.parametri.model.BudgetUotBean;
import it.wego.welfarego.parametri.model.json.JSONBudget;
import it.wego.welfarego.parametri.model.json.JSONBudgetUot;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoUot;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoUotPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @author Michele
 */
public class BudgetServlet extends JsonServlet {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Carico i budget in base al codTipint passato
     *
     * @param parameters
     * @return
     */
    public Object load(Map<String, String> parameters) {

        String codTipint = parameters.get("codTipint");

        if (!codTipint.isEmpty()) {
            // String tipoIntervento = parameters.get("tipo_intervento");
            EntityManager em = Connection.getEntityManager();
            BudgetTipoInterventoDao dao = new BudgetTipoInterventoDao(em);

            List<BudgetTipIntervento> budgets = dao.findByCodTipint(codTipint);

            List<BudgetBean> listaBudgetBean = new ArrayList<BudgetBean>();

            BudgetBean budgetBean;

            for (BudgetTipIntervento b : budgets) {
                budgetBean = new BudgetBean();
                listaBudgetBean.add(budgetBean.serialize(b));
            }

            em.close();

            return new JSONBudget(true, listaBudgetBean);
        }

        return null;
    }

    /**
     * Carico una lista di UOT Budget in base al Budget padre scelto
     *
     * @param request
     * @param response
     */
    private Object loadUot(HttpServletRequest request, HttpServletResponse response) {

        String codTipint = request.getParameter("codTipint");
        String codImpe = request.getParameter("codImpe");
        String codAnno = request.getParameter("codAnno");

        // creo l'EntityManager
        EntityManager em = Connection.getEntityManager();

        // mi collego al dao per le tipologie
        BudgetTipoInterventoDao dao = new BudgetTipoInterventoDao(em);

        // carico la lista dei gruppi (roots)
        List<BudgetTipInterventoUot> lista = dao.findByKey(codTipint, codAnno, codImpe).getBudgetTipInterventoUotList();
        List<BudgetUotBean> array = new ArrayList<BudgetUotBean>();

        BudgetUotBean bean;

        for (BudgetTipInterventoUot r : lista) {
            bean = new BudgetUotBean();
            bean.serialize(r);
            array.add(bean);
        }

        em.close();

        return new JSONBudgetUot(true, array);

    }

    private JSONGeneric insertUot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {

//        Gson gson = new Gson();
//
//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String codTipint = request.getParameter("codTipint");
        String codImpe = request.getParameter("codImpe");
        String codAnno = request.getParameter("codAnno");
       // String annoSpesa = request.getParameter("annoSpesa");
        String idUot = request.getParameter("id_param_uot");
        String dispEur = request.getParameter("bdg_disp_eur");
        String dispOre = request.getParameter("bdg_disp_ore");

        try {

            BudgetTipIntervento budget = new BudgetTipIntervento();

            BudgetTipoInterventoDao budgetdao = new BudgetTipoInterventoDao(em);

            // prendo il parametri in data che riguarda la uot selezionata
            ParametriIndataDao pdao = new ParametriIndataDao(em);
            ParametriIndata pin = pdao.findByIdParamIndata(Integer.valueOf(idUot));

            // prelevo il budget
            budget = budgetdao.findByKey(codTipint, codAnno, codImpe);

            // nuovo budget uot
            BudgetTipInterventoUot uot = new BudgetTipInterventoUot();

            BudgetTipInterventoUotPK uotpk = new BudgetTipInterventoUotPK();
            uotpk.setCodAnno(Short.valueOf(codAnno));
            uotpk.setCodImpe(codImpe);
            uotpk.setCodTipint(codTipint);
            uotpk.setIdParamUot(Integer.valueOf(idUot));

            // gli imposto la relazione con il budget impegno padre
            // uot.setBudgetTipIntervento(budget);
            uot.setBudgetTipInterventoUotPK(uotpk);

            // imposto id param indata per uot scelto in combo
            // uot.setParametriIndata(pin);
            // budget.getBudgetTipInterventoUotList().add(uot);

            // imposto budget euro e ore
            
            uot.setBdgDispEur(new BigDecimal(dispEur.replace(",", ".")));
            uot.setBdgDispOre(new BigDecimal(dispOre));

            em.getTransaction().begin();
            em.merge(uot);
            em.getTransaction().commit();

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

    private JSONGeneric insertBudget(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

//        Gson gson = new Gson();
//
//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        String codTipint = request.getParameter("cod_tipint");
        String dtDx = request.getParameter("dt_dx");
        String numDx = request.getParameter("num_dx");
        String codConto = request.getParameter("cod_conto");
        String codSconto = request.getParameter("cod_sconto");
        String codAnno = request.getParameter("cod_anno");
        String codCap = request.getParameter("cod_cap");
        String codImpe = request.getParameter("cod_impe");
        String bdgDispEur = request.getParameter("bdg_disp_eur");
        String bdgDispOre = request.getParameter("bdg_disp_ore");
        String annoSpesa = request.getParameter("annoSpesa");

        try {

            BudgetTipInterventoPK pk = new BudgetTipInterventoPK();
            BudgetTipIntervento bdg = new BudgetTipIntervento();

            if (codTipint != null) {
                pk.setCodTipint(codTipint);
            }


            pk.setCodAnno(Short.valueOf(codAnno));
            pk.setCodImpe(codImpe);

            bdg.setBudgetTipInterventoPK(pk);

            bdg.setDtDx(StringConversion.itStringToDate(dtDx));
            bdg.setNumDx(Integer.valueOf(numDx));
            bdg.setCodConto(Integer.valueOf(codConto));
            bdg.setCodSconto(Integer.valueOf(codSconto));
            bdg.setCodCap(Integer.valueOf(codCap));
            bdg.setAnnoErogazione(Short.valueOf(annoSpesa));

			// patch DOTCOM:
			// se i dati non sono valorizzati li setto a zero.
			if (bdgDispEur == null || bdgDispEur.isEmpty())
			{
				bdg.setBdgDispEur(BigDecimal.ZERO);
			}
			else
			{
				bdg.setBdgDispEur(new BigDecimal(bdgDispEur.replace(",",".")));
			}
			if (bdgDispOre == null || bdgDispOre.isEmpty())
			{
				bdg.setBdgDispOre(BigDecimal.ZERO);
			}
			else
			{
				bdg.setBdgDispOre(new BigDecimal(bdgDispOre));
			}

            em.getTransaction().begin();
            em.merge(bdg);
            em.getTransaction().commit();

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

    private Object deleteUot(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

//        Gson gson = new Gson();
//
//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codTipint = request.getParameter("codTipint");
            String codAnno = request.getParameter("codAnno");
            String codImpe = request.getParameter("codImpe");
            String idParamUot = request.getParameter("idParamUot");

            BudgetTipInterventoUotPK uotpk = new BudgetTipInterventoUotPK();
            uotpk.setCodAnno(Short.valueOf(codAnno));
            uotpk.setCodImpe(codImpe);
            uotpk.setCodTipint(codTipint);
            uotpk.setIdParamUot(Integer.valueOf(idParamUot));

            BudgetTipInterventoUot uot = new BudgetTipInterventoUot();
            uot.setBudgetTipInterventoUotPK(uotpk);

            em.getTransaction().begin();
            BudgetTipInterventoUot managed = em.merge(uot);
            em.remove(managed);
            em.getTransaction().commit();

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

    private Object deleteBudget(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

//        Gson gson = new Gson();

//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            String codTipint = request.getParameter("codTipint");
            String codAnno = request.getParameter("codAnno");
            String codImpe = request.getParameter("codImpe");

            BudgetTipoInterventoDao dao = new BudgetTipoInterventoDao(em);
            BudgetTipIntervento budget = dao.findByKey(codTipint, codAnno, codImpe);

            BudgetTipInterventoPK bdgpk = new BudgetTipInterventoPK();
            bdgpk.setCodAnno(Short.valueOf(codAnno));
            bdgpk.setCodImpe(codImpe);
            bdgpk.setCodTipint(codTipint);

            budget.setBudgetTipInterventoPK(bdgpk);

            em.getTransaction().begin();
            BudgetTipIntervento managed = em.merge(budget);
            em.remove(managed);
            em.getTransaction().commit();

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
            case INSERTUOT:
                return insertUot(request, response);
//                    break;
            case LOADUOT:
                return loadUot(request, response);
//                    break;
            case DELETEUOT:
                return deleteUot(request, response);
//                    break;
            case INSERTBUDGET:
                return insertBudget(request, response);
//                    break;
            case DELETEBUDGET:
                return deleteBudget(request, response);
//                    break;
            case EDITUOT:
                return null;
            // editUot(request, response);
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

        LOAD, SAVE, PROCEED, INSERTUOT, LOADUOT, DELETEUOT, INSERTBUDGET, DELETEBUDGET, EDITUOT
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
