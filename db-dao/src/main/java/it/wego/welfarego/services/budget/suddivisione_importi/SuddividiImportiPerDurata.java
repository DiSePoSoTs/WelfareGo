package it.wego.welfarego.services.budget.suddivisione_importi;

import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SuddividiImportiPerDurata {

    private Logger logger = LoggerFactory.getLogger(SuddividiImportiPerDurata.class);

    private PaiInterventoMeseDao paiInterventoMeseDao = null;
    private BudgetTipoInterventoDao budgetTipoInterventoDao = null;
    private PaiIntervento paiIntervento = null;
    private JSONArray datiDetermine = null;
    private JSONArray datiBudgetSelezionati = null;

    public SuddividiImportiPerDurata(PaiInterventoMeseDao paiInterventoMeseDao, BudgetTipoInterventoDao budgetTipoInterventoDao, PaiIntervento paiIntervento, JSONArray datiDetermine, JSONArray datiBudgetSelezionati) {
        this.paiInterventoMeseDao = paiInterventoMeseDao;
        this.budgetTipoInterventoDao = budgetTipoInterventoDao;
        this.paiIntervento = paiIntervento;
        this.datiDetermine = datiDetermine;
        this.datiBudgetSelezionati = datiBudgetSelezionati;
    }



    public void applica_logica() throws JSONException {

        int num_budget_selezionati = datiBudgetSelezionati.length();

        switch (num_budget_selezionati){
            case  1:
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

    void crea_proposta_singola(JSONObject dto_budget) throws JSONException {

        PaiInterventoPK interventoPK = paiIntervento.getPaiInterventoPK();
        ParametriIndata fascia = paiIntervento.getPai().getIdParamFascia();
        BigDecimal quantitaIntervento = paiIntervento.getQuantita();

        BudgetTipInterventoPK pk_budget = getBudgetTipInterventoPK(dto_budget);

        // calcolo mesi intervento
        Date dtAvvio = paiIntervento.getDtAvvio();
        Date dtFine = paiIntervento.calculateDtFine();
        int months = Months.monthsBetween(new LocalDate(dtAvvio), new LocalDate(dtFine)).getMonths();

        BigDecimal spesaTot = paiIntervento.getCostoPrev();

        PaiInterventoMese paiInterventoMese = this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget, spesaTot, quantitaIntervento, fascia);

    }

    BigDecimal calcola_costo_per_proposta_singola(BigDecimal quantitaIntervento, int months) {
        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        BigDecimal costo = calcolaCostoInterventoService.calcolaBdgPrevEur(paiIntervento);
        return costo;
    }

    void crea_proposte(JSONObject dto_budget_1, JSONObject dto_budget_2) throws JSONException {

        logger.info("crea_proposte");

        PaiInterventoPK interventoPK = paiIntervento.getPaiInterventoPK();
        ParametriIndata fascia = paiIntervento.getPai().getIdParamFascia();

        BigDecimal quantitaIntervento = paiIntervento.getQuantita();
        BigDecimal impStdCosto = paiIntervento.getTipologiaIntervento().getImpStdCosto();
        BigDecimal costo = impStdCosto.multiply(quantitaIntervento);

        BudgetTipInterventoPK pk_budget_1 = getBudgetTipInterventoPK(dto_budget_1);
        BudgetTipInterventoPK pk_budget_2 = getBudgetTipInterventoPK(dto_budget_2);


        int anno_erogazione_1 = (Integer) dto_budget_1.get("annoErogazione");
        BigDecimal mesi_su_anno_erogazione_budget_1 = calcola_mesi_intervento_su_anno_erogazione(anno_erogazione_1);
        BigDecimal spesaTot_1 = costo.multiply(mesi_su_anno_erogazione_budget_1);
        this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget_1, spesaTot_1, quantitaIntervento, fascia);

        int anno_erogazione_2 = (Integer) dto_budget_2.get("annoErogazione");
        BigDecimal mesi_su_anno_erogazione_budget_2 = calcola_mesi_intervento_su_anno_erogazione(anno_erogazione_2);
        BigDecimal spesaTot_2 = costo.multiply(mesi_su_anno_erogazione_budget_2);
        this.paiInterventoMeseDao.insertProp(interventoPK, pk_budget_2, spesaTot_2, quantitaIntervento, fascia);
    }

    BigDecimal calcola_mesi_intervento_su_anno_erogazione(int anno_erogazione)  {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        BigDecimal mesi_intervento = null;

        Date dtAvvio = paiIntervento.getDtAvvio();
        Integer durMesi = paiIntervento.getDurMesi();


        String format = sdf.format(dtAvvio);

        // questo passaggio trasforma il 1/3 in 28/2 ...
        LocalDate jodaDt_avvio_1 = new LocalDate(dtAvvio);
        LocalDate jodaDt_avvio_2 = new LocalDate(dtAvvio.getTime());
        LocalDate jodaDt_avvio_3 = LocalDate.parse(format, dtf);
        LocalDate jodaDt_fine = jodaDt_avvio_3.plusMonths(durMesi);



        logger.info("");
        logger.info("------------------------------");
        logger.info(anno_erogazione + " @@__" + dtAvvio + ", " + durMesi +
                ", 1:" +jodaDt_avvio_1 + ", 2:" + jodaDt_avvio_2 + ", 3:" + jodaDt_avvio_3 + ", " +
                jodaDt_fine);



        LocalDate dt_inizio_anno_erogazione = LocalDate.parse("01/01/" + anno_erogazione, dtf);
        LocalDate dt_fine_anno_erogazione = LocalDate.parse("31/12/" + anno_erogazione, dtf);



        if (jodaDt_avvio_3.getYear() == anno_erogazione) {
            int months = Months.monthsBetween(jodaDt_avvio_3, dt_fine_anno_erogazione).getMonths();
            mesi_intervento= new BigDecimal(months+1);
        } else if (jodaDt_fine.getYear() == anno_erogazione) {
            int months = Months.monthsBetween(dt_inizio_anno_erogazione, jodaDt_fine).getMonths();
            mesi_intervento= new BigDecimal(months);
        } else {
            String nominativo = paiIntervento.getPai().getCartellaSociale().getAnagrafeSoc().getCognomeNome();
            logger.error("non avrei dovuto arrivare qui per: " + nominativo );

            throw new RuntimeException(nominativo);
        }

        return mesi_intervento;
    }

    BudgetTipInterventoPK getBudgetTipInterventoPK(JSONObject dto_budget) throws JSONException {
        String codTipint = (String) dto_budget.get("codTipint");
        String codAnno = String.valueOf(dto_budget.get("codAnno"));
        String codImpegno = (String) dto_budget.get("codImpegno");
        BudgetTipIntervento ntt_budget = budgetTipoInterventoDao.findByKey(codTipint, codAnno, codImpegno);
        return ntt_budget.getBudgetTipInterventoPK();
    }
}
