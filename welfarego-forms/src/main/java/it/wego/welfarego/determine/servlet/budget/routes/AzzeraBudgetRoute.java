package it.wego.welfarego.determine.servlet.budget.routes;

import it.wego.welfarego.services.budget.BudgetService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AzzeraBudgetRoute {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject esegui(HttpServletRequest request) throws Exception {
        BudgetService budgetService = new BudgetService();

        String idEventoAsString = request.getParameter("idEvento");
        JSONObject jsonObject = azzera_budget(budgetService, idEventoAsString);
        return jsonObject;
    }

    JSONObject azzera_budget(BudgetService budgetService, String idEventoAsString) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        valida_input(idEventoAsString);
        Integer idEvento = Integer.valueOf(idEventoAsString);
        budgetService.azzera_budget_da_evento(idEvento);
        jsonObject.put("message", "azzerati i budget");
        return jsonObject;
    }


    void valida_input(String idEventoAsString) {
        if(idEventoAsString == null || idEventoAsString.trim().length() == 0){
            throw new IllegalArgumentException("l' id di riferimento per azzerare i budget non è arrivato al server.");
        }
    }
}
