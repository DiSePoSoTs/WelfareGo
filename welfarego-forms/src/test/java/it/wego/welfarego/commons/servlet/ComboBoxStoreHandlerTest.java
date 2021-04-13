package it.wego.welfarego.commons.servlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ComboBoxStoreHandlerTest {

    
    @Test
    public void aa() throws Exception {
        String json = "{\"cod_param_unita_misura\":\"eu\",\"is_prorogabile\":\"N\",\"cod_tmpl_documento_di_autorizzazione\":\"1000\",\"nome_utente_ver_dati\":\"IOLE\",\"data_apert_pai\":\"01/12/17\",\"n_pai\":\"24255872\",\"spesa_unitaria\":\"1\",\"cognome_ut\":\"DELL'ANDREA\",\"assist_soc\":\"NISI GABRIELLA\",\"interv\":\"AZ008A\",\"descrizione\":\"Assegno per l'autonomia possibile anziani (FAP APA)\",\"imp_mens\":\"258\",\"durata\":\"12\",\"ext-gen1567\":\"\",\"data_avvio\":\"06/11/2018\",\"cost_tot\":\"3096\",\"data_avvio_proposta\":\"01/10/18\",\"ext-gen1570\":\"3096\",\"quantita_tot\":\"3096\",\"budg_rest\":\"0\",\"note\":\"-2/6 ADL NUOVO 2018, diagnosi cognitiva iniziale, il figlio attiva una coop per iniziare da ottobre Alma, ad integrazione dell'aiuto dei familiari, necessario idonea stimolazione, aiuto per igiene, uscite e socializzazione, piccolo disbrigo governo casa\",\"ns_cda4_verifica_impegni_acarico_sum\":\"3096\",\"ns_cda4_verifica_impegni_acarico_rem\":\"0\",\"ns_cda4_verifica_impegni_acarico_sum_qta\":\"3096\",\"ns_cda4_verifica_impegni_acarico_rem_qta\":\"0\",\"testo_autorizzazione\":\"testo autorizzazzione :-)\",\"esito\":\"approvato\",\"tipo_determina\":\"multipla\",\"singola\":\"true\",\"multipla\":\"true\"}";
        JSONObject jsonObject = new JSONObject(json);
        String testo_autorizzazione = jsonObject.getString("testo_autorizzazione");
        System.out.println(testo_autorizzazione);
//
    }
//    
//    @Test
//    public void testLoadPo() throws Exception {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory(\"welfaregoPUTest\");
//        EntityManager entityManager = factory.createEntityManager();
//        ComboBoxStoreHandler comboBoxStoreHandler = new ComboBoxStoreHandler(entityManager);
//        List<ComboBoxStoreHandler.ComboBoxDataModel> comboBoxDataModels = comboBoxStoreHandler.loadPo();
//        System.out.println(comboBoxDataModels);
//    }
//
//    @Test
//    public void testLoadAttivita() throws Exception {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory(\"welfaregoPUTest\");
//        EntityManager entityManager = factory.createEntityManager();
//        ComboBoxStoreHandler comboBoxStoreHandler = new ComboBoxStoreHandler(entityManager);
//        List<ComboBoxStoreHandler.ComboBoxDataModel> comboBoxDataModels = comboBoxStoreHandler.loadAttivita();
//        System.out.println(comboBoxDataModels);
//    }
//
//    @Test
//    public void testLoadInterventi() throws Exception {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory(\"welfaregoPUTest\");
//        EntityManager entityManager = factory.createEntityManager();
//        ComboBoxStoreHandler comboBoxStoreHandler = new ComboBoxStoreHandler(entityManager);
//        List<ComboBoxStoreHandler.ComboBoxDataModel> comboBoxDataModels = comboBoxStoreHandler.loadInterventi();
//        System.out.println(comboBoxDataModels);
//    }

}