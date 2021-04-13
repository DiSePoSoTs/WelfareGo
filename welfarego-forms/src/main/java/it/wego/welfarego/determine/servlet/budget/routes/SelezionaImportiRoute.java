package it.wego.welfarego.determine.servlet.budget.routes;

import it.wego.welfarego.services.budget.BudgetService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.List;

public class SelezionaImportiRoute {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject esegui(HttpServletRequest request) throws Exception {
        BudgetService budgetService = new BudgetService();
        JSONObject jsonObject = new JSONObject();

        JSONObject json_in_request_body = getRequestBodyAsJSONObject(request);
        JSONArray datiBudgetSelezionati = (JSONArray) json_in_request_body.get("datiBudgetSelezionati");
        JSONArray datiDetermine = (JSONArray) json_in_request_body.get("datiDetermine");

        List<String> soggetti_da_controllare = budgetService.suddividi_importi_sui_budget(datiDetermine, datiBudgetSelezionati);

        jsonObject.put("soggetti_da_controllare", soggetti_da_controllare);
        return jsonObject;

    }


    public JSONObject getRequestBodyAsJSONObject(HttpServletRequest request) throws Exception {
        String str = "";
        String wholeStr = "";

        BufferedReader br = request.getReader();
        while ((str = br.readLine()) != null) {
            wholeStr += str;
        }

        return new JSONObject(wholeStr);
    }


}
