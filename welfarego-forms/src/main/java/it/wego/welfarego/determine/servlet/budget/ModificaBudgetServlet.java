package it.wego.welfarego.determine.servlet.budget;


import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.determine.servlet.budget.router.ModificaBudgetRouter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaBudgetServlet extends JsonServlet {

    private Logger logger = LoggerFactory.getLogger(ModificaBudgetServlet.class);

    /*
        url: wf.config.path.seleziona_budget_multipli,
        servletPath:/ModificaBudget,
        queryString:null,
        RequestURL:http://89.96.138.36/WelfaregoForms/ModificaBudget/azzerabudget,
        requestURI:/WelfaregoForms/ModificaBudget/azzerabudget
    * */
    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        ModificaBudgetRouter router = new ModificaBudgetRouter();
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject = router.instrada(request);

        } catch (Exception ex) {
            logger.error("errore_generico", ex);
            jsonObject.put("errore_generico", ex.getMessage() + ", " + ex.getCause() );
        }

        return jsonObject;
    }
}