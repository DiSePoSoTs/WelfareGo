package it.wego.welfarego.services.budget.validatori.dto;

import org.json.JSONArray;

import java.util.List;

public class SuddividiImportiSuiBudgetValidatorDto {

    private JSONArray determine_da_processare;
    private List<String> persone_da_gestire_a_mano;

    public SuddividiImportiSuiBudgetValidatorDto() {
    }

    public SuddividiImportiSuiBudgetValidatorDto(JSONArray determine_da_processare, List<String> persone_da_gestire_a_mano) {
        this.determine_da_processare = determine_da_processare;
        this.persone_da_gestire_a_mano = persone_da_gestire_a_mano;
    }


    public JSONArray getDetermine_da_processare() {
        return determine_da_processare;
    }

    public List<String> getPersone_da_gestire_a_mano() {
        return persone_da_gestire_a_mano;
    }
}
