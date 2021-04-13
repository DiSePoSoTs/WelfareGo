package it.wego.welfarego.services.budget.validatori;

import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.services.budget.validatori.dto.SuddividiImportiSuiBudgetValidatorDto;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Suddividi_Importi_Sui_Budget_Validator {

    private Logger logger = LoggerFactory.getLogger(Suddividi_Importi_Sui_Budget_Validator.class);

    private JSONArray datiBudgetSelezionati = null;
    private JSONArray datiDetermine = null;
    private PaiEventoDao paiEventoDao = null;

    public Suddividi_Importi_Sui_Budget_Validator(PaiEventoDao paiEventoDao, JSONArray datiBudgetSelezionati, JSONArray datiDetermine) {
        this.paiEventoDao = paiEventoDao;
        this.datiBudgetSelezionati = datiBudgetSelezionati;
        this.datiDetermine = datiDetermine;
        logger.info("datiBudgetSelezionati: " + datiBudgetSelezionati.toString());
        logger.info("datiDetermine: " + datiDetermine.toString());
    }

    public SuddividiImportiSuiBudgetValidatorDto valida_dati() throws JSONException {


        //vincolo:
        // numero minimo di budget applicabili
        if (datiBudgetSelezionati.length() == 0) {
            throw new IllegalArgumentException("non sono stati selezionati budget");
        }

        //vincolo:
        // numero massimo di budget applicabili
        if (datiBudgetSelezionati.length() > 2) {
            throw new IllegalArgumentException("Sono consentiti al massimo 2 budget, trovati: " + datiBudgetSelezionati.length() + ", " + datiBudgetSelezionati.toString());
        }


        if(datiBudgetSelezionati.length() == 1){
            SuddividiImportiSuiBudgetValidatorDto dto = new SuddividiImportiSuiBudgetValidatorDto(datiDetermine, new ArrayList<String>());
            return dto;
        }

        //vincolo:
        // per ogni elemento di determina controllare se gli anni di erogazione coincidono con gli
        // anni di inizio-fine dell' intervento.
        List<String> persone_da_gestire_a_mano = new ArrayList<String>();
        JSONArray determine_da_processare = new JSONArray();
        for (int i = 0; i < datiDetermine.length(); i++) {


            JSONObject dto_determine = (JSONObject) datiDetermine.get(i);
            String nominativo = dto_determine.get("cognome") + " " + dto_determine.get("nome");

            Integer idEvento = Integer.valueOf((String) dto_determine.get("id"));
            PaiEvento evento = paiEventoDao.findByIdEvento(idEvento);
            PaiIntervento paiIntervento = evento.getPaiIntervento();

            JSONObject dto_budget_1 = (JSONObject) datiBudgetSelezionati.get(0);
            JSONObject dto_budget_2 = (JSONObject) datiBudgetSelezionati.get(1);
            int anno_erogazione_1 = (Integer) dto_budget_1.get("annoErogazione");
            int anno_erogazione_2 = (Integer) dto_budget_2.get("annoErogazione");


            int anno_inizio = anno_erogazione_1;
            int anno_fine = anno_erogazione_2;

            if(anno_inizio>anno_fine){
                anno_inizio = anno_erogazione_2;
                anno_fine = anno_erogazione_1;
            }

            LocalDate dt_avvio = new LocalDate(paiIntervento.getDtAvvio());
            LocalDate dt_fine = new LocalDate(paiIntervento.calculateDtFine());

            boolean anno_avvio_ok = dt_avvio.getYear() == anno_inizio;
            boolean anno_fine_ok = dt_fine.getYear() == anno_fine;

            if (anno_avvio_ok && anno_fine_ok) {
                determine_da_processare.put(dto_determine);
            } else {
                persone_da_gestire_a_mano.add(nominativo);
            }

        }

        SuddividiImportiSuiBudgetValidatorDto dto = new SuddividiImportiSuiBudgetValidatorDto(determine_da_processare, persone_da_gestire_a_mano);
        return dto;

    }
}
