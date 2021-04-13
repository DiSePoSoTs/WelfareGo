package it.wego.welfarego.determine.servlet.budget.routes;


import it.wego.welfarego.services.budget.BudgetService;
import it.wego.welfarego.services.dto.BudgetDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetBudgetsRoute {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject esegui(HttpServletRequest request) throws JSONException {
        BudgetService budgetService = new BudgetService();
        String codTipint = request.getParameter("codTipint");
        JSONObject jsonObject = get_budgets_per_tipo_intervento(budgetService, codTipint);
        return jsonObject;
    }

    JSONObject get_budgets_per_tipo_intervento(BudgetService budgetService, String codTipint) throws JSONException {

        valida_input(codTipint);

        List<BudgetDTO> budgets =  budgetService.get_budgets_per_tipo_intervento(codTipint);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("budgets", budgets);
        return  jsonObject;
    }

    void valida_input(String codTipint) {
        if(codTipint == null || codTipint.trim().length() == 0){
            throw new IllegalArgumentException("il codTipint non è arrivato al server.");
        }
    }
}
