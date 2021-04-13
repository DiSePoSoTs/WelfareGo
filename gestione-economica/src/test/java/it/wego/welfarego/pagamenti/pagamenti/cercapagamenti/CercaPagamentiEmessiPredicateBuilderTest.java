package it.wego.welfarego.pagamenti.pagamenti.cercapagamenti;

import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CercaPagamentiEmessiPredicateBuilderTest {
    @Test
    public void testGetFiltriDaApplicare() throws Exception {


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

//        String uot_struttura  = "6";
        String uot_struttura  = "";
//        String cognome  = "GIOVANNINI";
        String cognome  = "";
//        String nome  = "ORNELLA";
        String nome  = "";


        String filterDelegato  = "ala";
//        String filterDelegato  = null;

        String codTipint = "EC100";
//        String codTipint = "";

        String stato_pagamenti = "inviate";
//        String stato_pagamenti = null;

        String mese_di_competenza_mese  = "3";
        String mese_di_competenza_anno  = "2017";
        String periodo_considerato_dal_mese  = " ";
        String periodo_considerato_al_mese  = " ";
        String periodo_considerato_dal_anno  = "  ";
        String periodo_considerato_al_anno  = " ";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root r = criteriaQuery.from(Mandato.class);

        Join joinDelegato = r.join("paiIntervento").join("dsCodAnaBenef", JoinType.LEFT);

        CercaPagamentiEmessiPredicateBuilder cercaPagamentiEmessiPredicateBuilder = new CercaPagamentiEmessiPredicateBuilder(criteriaBuilder, r, mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_cod_tip_int(codTipint);
//        cercaPagamentiEmessiPredicateBuilder.filtra_per_periodo_con_join_su_dettagli_mandato();
        cercaPagamentiEmessiPredicateBuilder.filtra_per_uot(uot_struttura);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_cognome(cognome);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_nome(nome);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_tip_param_uguale_a_fs();
        cercaPagamentiEmessiPredicateBuilder.filtra_per_stato_pagamenti(stato_pagamenti);
        cercaPagamentiEmessiPredicateBuilder.filtra_per_delegato(filterDelegato);

        criteriaQuery.where(cercaPagamentiEmessiPredicateBuilder.getFiltriDaApplicare());
        TypedQuery query = entityManager.createQuery(criteriaQuery);
        Map<String, Object> parametri = cercaPagamentiEmessiPredicateBuilder.getParameters();
        Iterator<String> iterator = parametri.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            Object value = parametri.get(key);
            query.setParameter(key, value);
        }

//        query.setMaxResults(1);

//        System.setProperty("eclipselink.logging.level", "ALL");
//        System.setProperty("eclipselink.logging.parameters", "true");
//        System.out.println("_________eclipselink_logging_level aa");
        List<Mandato> mandatoList = query.getResultList();
        Mandato mandato = mandatoList.get(0);
        PaiIntervento intervento = mandato.getPaiIntervento();
        String _nome =  intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getNome();
        String _cognome =intervento.getPai().getAnagrafeSoc().getCartellaSociale().getAnagrafeSoc().getCognome();
        Integer _uot = intervento.getPai().getIdParamUot().getIdParamIndata();
        String _delCo = intervento.getDsCodAnaBenef().getCognome();
        String _delNo = intervento.getDsCodAnaBenef().getNome();
        String _delRs = intervento.getDsCodAnaBenef().getRagSoc();

//        r.get("paiIntervento").get("pai").get("idParamUot").get("idParamIndata")
        System.out.println(_nome + ", " + _cognome + ", " + _uot);
        System.out.println(_delCo + ", " + _delNo+ ", " + _delRs);

    }


    //        java.lang.ClassCastException: com.sun.proxy.$Proxy382 cannot be cast to org.eclipse.persistence.internal.jpa.EJBQueryImpl
//        at it.wego.welfarego.pagamenti.pagamenti.AjaxPagamento.prepara_ed_esegui_select_per_cerca_pagamenti_eseguiti(AjaxPagamento.java:712
//        EJBQueryImpl qjpaQuery = query.unwrap(EJBQueryImpl.class);
//    Class<? extends TypedQuery> cls = query.getClass();
//    DatabaseQuery databaseQueryInternal = ((EJBQueryImpl) query).getDatabaseQueryInternal();
//
//    getLogger().error("query.getClass(): " + cls);
//    getLogger().error("JPAQuery.class.isAssignableFrom(query.getClass()): " +JPAQuery.class.isAssignableFrom(cls));
//    getLogger().error("((EJBQueryImpl)query).getDatabaseQueryInternal(): " + databaseQueryInternal);
//    getLogger().error("JPAQuery.class.isAssignableFrom(databaseQueryInternal.getClass()): " + JPAQuery.class.isAssignableFrom(databaseQueryInternal.getClass()));
//
//        try {
//        Advised advised = (Advised) query;
//        getLogger().error("advised.getTargetSource().getTarget().getClass().getCanonicalName(): " + advised.getTargetSource().getTarget().getClass().getCanonicalName());
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//        JPAQuery qjpaQuery = (JPAQuery ) query.unwrap(query.getClass());
//        String sql = qjpaQuery.getDatabaseQuery().getSQLString();
//        getLogger().error("sql: " + sql);

//    public <T> T unwrap(Class<T> cls) {
//        if(cls.isAssignableFrom(this.getClass())) {
//            return this;
//        } else if(cls.isAssignableFrom(this.getDatabaseQueryInternal().getClass())) {
//            return this.getDatabaseQueryInternal();
//        } else {
//            throw new PersistenceException("Could not unwrap query to: " + cls);
//        }
//    }


}