package it.wego.welfarego.services.budget.suddivisione_importi;

import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;


public class SuddividiImportiPerDate {

    private Logger logger = LoggerFactory.getLogger(SuddividiImportiPerDate.class);


    private PaiInterventoMeseDao paiInterventoMeseDao = null;
    private BudgetTipoInterventoDao budgetTipoInterventoDao = null;
    private PaiIntervento paiIntervento = null;
    private JSONArray datiDetermine = null;
    private JSONArray datiBudgetSelezionati = null;

    public SuddividiImportiPerDate(PaiInterventoMeseDao paiInterventoMeseDao, BudgetTipoInterventoDao budgetTipoInterventoDao, PaiIntervento paiIntervento, JSONArray datiDetermine, JSONArray datiBudgetSelezionati) {
        this.paiInterventoMeseDao = paiInterventoMeseDao;
        this.budgetTipoInterventoDao = budgetTipoInterventoDao;
        this.paiIntervento = paiIntervento;
        this.datiDetermine = datiDetermine;
        this.datiBudgetSelezionati = datiBudgetSelezionati;
    }

    public void applica_logica() throws JSONException {

        int num_budget_selezionati = datiBudgetSelezionati.length();

        switch (num_budget_selezionati) {
            case 1:
                JSONObject budget = (JSONObject) datiBudgetSelezionati.get(0);
                crea_proposta_singola(budget);
                break;
            case 2:
                JSONObject budget_1 = (JSONObject) datiBudgetSelezionati.get(0);
                JSONObject budget_2 = (JSONObject) datiBudgetSelezionati.get(1);
                crea_proposte(budget_1, budget_2);
                break;
            default:
                throw new IllegalArgumentException("troppi budget:" + num_budget_selezionati + datiBudgetSelezionati.toString());
        }

    }

    private void crea_proposte(JSONObject dto_budget_1, JSONObject dto_budget_2) throws JSONException {

        PaiInterventoPK interventoPK = paiIntervento.getPaiInterventoPK();
        ParametriIndata fascia = paiIntervento.getPai().getIdParamFascia();
        BudgetTipInterventoPK pk_budget_1 = getBudgetTipInterventoPK(dto_budget_1);
        BudgetTipInterventoPK pk_budget_2 = getBudgetTipInterventoPK(dto_budget_2);
        BigDecimal quantitaIntervento = paiIntervento.getQuantita();
        BigDecimal impStdCosto = paiIntervento.getTipologiaIntervento().getImpStdCosto();
        BigDecimal costo = impStdCosto.multiply(quantitaIntervento);


        int anno_erogazione_1 = (Integer) dto_budget_1.get("annoErogazione");
        BigDecimal giorni_su_anno_erogazione_budget_1 = calcola_giorni_intervento_su_anno_erogazione(anno_erogazione_1);
        BigDecimal spesaTot_1 = costo.multiply(giorni_su_anno_erogazione_budget_1);
        this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget_1, spesaTot_1, quantitaIntervento, fascia);


        int anno_erogazione_2 = (Integer) dto_budget_2.get("annoErogazione");
        BigDecimal giorni_su_anno_erogazione_budget_2 = calcola_giorni_intervento_su_anno_erogazione(anno_erogazione_2);
        BigDecimal spesaTot_2 = costo.multiply(giorni_su_anno_erogazione_budget_2);
        this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget_2, spesaTot_2, quantitaIntervento, fascia);
    }

    BigDecimal calcola_giorni_intervento_su_anno_erogazione(int anno_erogazione)  {
        BigDecimal giorni_intervento = null;

        LocalDate dt_avvio = new LocalDate(paiIntervento.getDtAvvio());
        LocalDate dt_fine = new LocalDate(paiIntervento.calculateDtFine());
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        LocalDate dt_inizio_anno_erogazione = LocalDate.parse("01/01/" + anno_erogazione, dtf);
        LocalDate dt_fine_anno_erogazione = LocalDate.parse("31/12/" + anno_erogazione, dtf);

        if (dt_avvio.getYear() == anno_erogazione) {
            int days = Days.daysBetween(dt_avvio, dt_fine_anno_erogazione).getDays();
            giorni_intervento = new BigDecimal(days + 1); // devo considerare anche in giorno di inizio
        } else if (dt_fine.getYear() == anno_erogazione) {
            int days = Days.daysBetween(dt_inizio_anno_erogazione, dt_fine).getDays();
            giorni_intervento = new BigDecimal(days + 1);
        } else {
            String nominativo = paiIntervento.getPai().getCartellaSociale().getAnagrafeSoc().getCognomeNome();
            logger.error("non avrei dovuto arrivare qui per: " + nominativo );

            throw new RuntimeException(nominativo);
        }


        return giorni_intervento;
    }

    void crea_proposta_singola(JSONObject dto_budget) throws JSONException {

        logger.info("crea_proposta_singola");

        PaiInterventoPK interventoPK = paiIntervento.getPaiInterventoPK();
        ParametriIndata fascia = paiIntervento.getPai().getIdParamFascia();

        BudgetTipInterventoPK pk_budget = getBudgetTipInterventoPK(dto_budget);

        String jdkDefaultTimeZone = TimeZone.getDefault().getID();
        String jodaDefaultTimeZone = DateTimeZone.getDefault().getID();

        // calcolo giorni intervento
        Date dtAvvio = paiIntervento.getDtAvvio();
        Date dtFine = paiIntervento.calculateDtFine();
        BigDecimal giorni = null;

        if(jdkDefaultTimeZone.equals(jodaDefaultTimeZone)){
            int days = Days.daysBetween(new LocalDate(dtAvvio), new LocalDate(dtFine)).getDays();
            giorni = new BigDecimal(days);
        } else {
            logger.warn("time zone diversi: jdkDefaultTimeZone: " + jdkDefaultTimeZone + ", jodaDefaultTimeZone: " + jodaDefaultTimeZone);
            DateTimeZone dtz = DateTimeZone.forID(jdkDefaultTimeZone);
            int days = Days.daysBetween(new LocalDate(dtAvvio, dtz), new LocalDate(dtFine, dtz)).getDays();
            giorni = new BigDecimal(days);
        }


        BigDecimal costo = paiIntervento.getTipologiaIntervento().getImpStdCosto();
        BigDecimal spesaTot = paiIntervento.getCostoPrev();
        BigDecimal quantita = paiIntervento.getQuantita();
        this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget, spesaTot, quantita, fascia);
    }

    private BudgetTipInterventoPK getBudgetTipInterventoPK(JSONObject dto_budget) throws JSONException {
        String codTipint = (String) dto_budget.get("codTipint");
        String codAnno = String.valueOf(dto_budget.get("codAnno"));
        String codImpegno = (String) dto_budget.get("codImpegno");
        BudgetTipIntervento ntt_budget = budgetTipoInterventoDao.findByKey(codTipint, codAnno, codImpegno);
        return ntt_budget.getBudgetTipInterventoPK();
    }


}
