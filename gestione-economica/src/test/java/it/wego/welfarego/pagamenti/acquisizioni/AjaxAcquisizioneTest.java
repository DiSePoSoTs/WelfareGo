package it.wego.welfarego.pagamenti.acquisizioni;

import org.json.JSONObject;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.testng.Assert.*;

public class AjaxAcquisizioneTest {

    @Test
    public void test_senza_filtro_per_delegato() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = null;
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 1135, list.size() + "");
    }


    @Test
    public void test_parole_non_ordinate() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "agenzia duemilauno";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 1, list.size() + "");
    }


    @Test
    public void test_punto_e_case_insensitive() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "ALMA";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 3, list.size() + "");
    }

    @Test
    public void test_problemi_con_meseUguale_a_2018() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "2018"; // <---- mese == 2018 ??? e funziona ??
        String periodo_considerato_dal_anno = "3";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "ALMA";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 21, list.size() + "");
    }

    @Test
    public void test_punto__case_insensitive__parole_non_ordinate() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "sRL iL";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 1, list.size() + "");
    }


    @Test
    public void test_cognome_nome() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "bader maria";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 4, list.size() + "");
    }

    @Test
    public void test_nome_cognome() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        AjaxAcquisizione ajaxAcquisizione = new AjaxAcquisizione();
        String mese_di_competenza_mese = null;
        String mese_di_competenza_anno = null;
        String periodo_considerato_dal_mese = "1";
        String periodo_considerato_al_mese = "3";
        String periodo_considerato_dal_anno = "2018";
        String periodo_considerato_al_anno = "2018";
        String uot_struttura = null;
        String cognome = null;
        String nome = null;
        String codTipInt = null;
        JSONObject jsoData = new JSONObject();
        String filterRagSoc = "  maria      bader  ";
        List list = ajaxAcquisizione.prepara_ed_esegui_select(entityManager, codTipInt, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno, uot_struttura, cognome, nome, filterRagSoc, jsoData);
        assertTrue(list.size() == 4, list.size() + "");
    }

}