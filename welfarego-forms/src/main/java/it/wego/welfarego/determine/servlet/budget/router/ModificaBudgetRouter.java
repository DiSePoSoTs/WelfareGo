package it.wego.welfarego.determine.servlet.budget.router;

import it.wego.welfarego.determine.servlet.budget.routes.AzzeraBudgetRoute;
import it.wego.welfarego.determine.servlet.budget.routes.GetBudgetsRoute;
import it.wego.welfarego.determine.servlet.budget.routes.SelezionaImportiRoute;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class ModificaBudgetRouter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject instrada(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        AzzeraBudgetRoute azzeraBudgetRoute = new AzzeraBudgetRoute();
        GetBudgetsRoute getBudgetsRoute = new GetBudgetsRoute();
        SelezionaImportiRoute selezionaImportiRoute = new SelezionaImportiRoute();


        String last_path_part = get_last_request_path_part(request);

        if ("azzerabudget".equalsIgnoreCase(last_path_part)) {
            jsonObject =  azzeraBudgetRoute.esegui(request);

        } else if ("budgets".equalsIgnoreCase(last_path_part)) {
            jsonObject = getBudgetsRoute.esegui(request);

        } else if ("suddividi-importi-sui-budget".equalsIgnoreCase(last_path_part)) {
            jsonObject = selezionaImportiRoute.esegui(request);

        } else {
            logger.error("caso non gestito: " + request.getRequestURI());
        }

        return jsonObject ;
    }

    public String get_last_request_path_part(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] path_parts = requestURI.split("/");
        String last_request_path_part = path_parts[path_parts.length - 1];
        return last_request_path_part;
    }


}