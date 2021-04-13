package it.wego.welfarego.services.budget;

import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.services.budget.suddivisione_importi.SuddividiImportiPerDate;
import it.wego.welfarego.services.budget.suddivisione_importi.SuddividiImportiPerDurata;
import it.wego.welfarego.services.budget.validatori.Suddividi_Importi_Sui_Budget_Validator;
import it.wego.welfarego.services.budget.validatori.dto.SuddividiImportiSuiBudgetValidatorDto;
import it.wego.welfarego.services.dto.BudgetDTO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class BudgetService {


    private Logger logger = LoggerFactory.getLogger(BudgetService.class);


    public BudgetService() {
    }



    public void azzera_budget_da_evento(Integer idEvento) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Query query = entityManager.createNamedQuery("azzera_bdg_previsti_da_idEvento");
            query.setParameter(1, idEvento);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new RuntimeException("errore nel reset dei budget.", ex);
        }
    }

    public List<BudgetDTO> get_budgets_per_tipo_intervento(String codTipint) {
        List<BudgetDTO> dtoBudgets = new ArrayList<BudgetDTO>();
        EntityManager entityManager = getEntityManager();
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        List<BudgetTipIntervento> nttBudgets = budgetTipoInterventoDao.findByCodTipintOrdered(codTipint);

        for (BudgetTipIntervento nttBudget : nttBudgets) {
            dtoBudgets.add(new BudgetDTO(nttBudget));
        }

        return dtoBudgets;
    }


    public List<String> suddividi_importi_sui_budget(JSONArray datiDetermine, JSONArray datiBudgetSelezionati) throws Exception {

        EntityManager entityManager = getEntityManager();

        if(entityManager == null){
            logger.warn("entityManager era null");
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPU");
            entityManager = factory.createEntityManager();
            logger.warn(entityManager.toString());
        }

        PaiEventoDao paiEventoDao = get_PaiEventoDao(entityManager);

        Suddividi_Importi_Sui_Budget_Validator validator = new Suddividi_Importi_Sui_Budget_Validator(paiEventoDao, datiBudgetSelezionati, datiDetermine);
        SuddividiImportiSuiBudgetValidatorDto validatorDto = validator.valida_dati();

        JSONArray determine_da_processare = validatorDto.getDetermine_da_processare();

        for (int i = 0; i < determine_da_processare.length(); i++) {
            try {
                suddividi_importi_sui_budget_applica_logica(determine_da_processare, datiBudgetSelezionati, entityManager, paiEventoDao, i);
            }catch(Exception ex){
                JSONObject jsonObject = (JSONObject) datiDetermine.get(i);
                String nominativo = jsonObject.get("cognome") + " " + jsonObject.get("nome");
                throw new Exception(nominativo, ex);
            }
        }

        return validatorDto.getPersone_da_gestire_a_mano();

    }

    void suddividi_importi_sui_budget_applica_logica(JSONArray datiDetermine, JSONArray datiBudgetSelezionati, EntityManager entityManager, PaiEventoDao paiEventoDao, int i) throws JSONException {

        JSONObject dto_determine = (JSONObject) datiDetermine.get(i);
        Integer idEvento = Integer.valueOf((String) dto_determine.get("id"));

        azzera_budget_da_evento(idEvento);

        PaiEvento evento = paiEventoDao.findByIdEvento(idEvento);
        PaiIntervento paiIntervento = evento.getPaiIntervento();

        Date dtAvvio = evento.getPaiIntervento().getDtAvvio();
        LocalDate jodaDt_avvio_1 = new LocalDate(dtAvvio);

        logger.info("@@__dtAvvio: " + dtAvvio);
        logger.info("@@__jodaDt_avvio_1: " + jodaDt_avvio_1);
        logger.info("System.getProperty(\"user.timezone\"): " + System.getProperty("user.timezone"));

        TimeZone timeZone = TimeZone.getDefault();
        String java_time_zone = timeZone.getID();
        logger.info("timeZone.getID(): "+ java_time_zone);
        logger.info("timeZone.getDisplayName(): " + timeZone.getDisplayName());

        DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
        logger.info("defaultTimeZone.getID(): " + defaultTimeZone.getID());
        logger.info("defaultTimeZone.getName(0L)" + defaultTimeZone.getName(0L));

        logger.info("defaultTimeZone.getName(new GregorianCalendar().getTimeInMillis()):" + defaultTimeZone.getName(new GregorianCalendar().getTimeInMillis()));
        logger.info("defaultTimeZone.toTimeZone().getDisplayName():" + defaultTimeZone.toTimeZone().getDisplayName());


        DateTimeZone dateTimeZone_by_java = DateTimeZone.forID(java_time_zone);
        logger.info("DateTimeZone.forID(java_default_time_zone): " + dateTimeZone_by_java);




        DateTime currentTime = new DateTime();
        DateTimeZone currentZone = currentTime.getZone();
        logger.info(currentZone.getID());
        logger.info("currentZone.getName(0L): " + currentZone.getName(0L));


        DateTime jodaDt_avvio_2 = new DateTime(dtAvvio.getTime(), defaultTimeZone);
        logger.info("jodaDt_avvio: " + jodaDt_avvio_2);

        DateTime jodaDt_avvio_3 = new DateTime(dtAvvio.getTime(),  dateTimeZone_by_java);
        logger.info("jodaDt_avvio_3: " +jodaDt_avvio_3);



        char flgFineDurata = paiIntervento.getTipologiaIntervento().getFlgFineDurata();

        switch (flgFineDurata) {
            case 'F':
                get_SuddividiImportiperDate(entityManager, datiDetermine, datiBudgetSelezionati, paiIntervento);
                break;
            case 'D':
                get_SuddividiImportiPerDurata(entityManager, datiDetermine, datiBudgetSelezionati, paiIntervento);
                break;
            default:
                throw new IllegalArgumentException("tipologia intervento senza flag fine durata per id Evento: " + idEvento);
        }

    }

    PaiEventoDao get_PaiEventoDao(EntityManager entityManager) {
        return new PaiEventoDao(entityManager);
    }

    EntityManager getEntityManager() {
        return Connection.getEntityManager();
    }

    void get_SuddividiImportiPerDurata(EntityManager entityManager, JSONArray datiDetermine, JSONArray datiBudgetSelezionati, PaiIntervento paiIntervento) throws JSONException {

        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        SuddividiImportiPerDurata suddividiImportiPerDurata = new SuddividiImportiPerDurata(paiInterventoMeseDao, budgetTipoInterventoDao, paiIntervento, datiDetermine, datiBudgetSelezionati);
        suddividiImportiPerDurata.applica_logica();
    }

    void get_SuddividiImportiperDate(EntityManager entityManager, JSONArray datiDetermine, JSONArray datiBudgetSelezionati, PaiIntervento paiIntervento) throws JSONException {

        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);
        SuddividiImportiPerDate suddividiImportiPerDate = new SuddividiImportiPerDate(paiInterventoMeseDao, budgetTipoInterventoDao, paiIntervento, datiDetermine, datiBudgetSelezionati);
        suddividiImportiPerDate.applica_logica();
    }
}
