package it.wego.welfarego.pagamenti.pagamenti.cercapagamenti;

import com.google.common.base.Strings;
import it.wego.persistence.ConditionUtils;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Mandato;
import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.internal.jpa.JPAQuery;
//import org.eclipse.persistence.jpa.JPAQuery;  org.eclipse.persistence:eclipselink
import org.eclipse.persistence.queries.DatabaseQuery;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;


public class CercaPagamentiEmessiConditionBuilderTest {
    @Test
    public void testGetConditions() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        String uot_struttura  = null;
        String cognome  = null;
        String nome  = null;
        String filterDelegato  = "ala";
        String codTipint = "EC100";
        String stato_pagamenti = "inviate";
        String mese_di_competenza_mese  = null;
        String mese_di_competenza_anno  = null;
        String periodo_considerato_dal_mese  = "1";
        String periodo_considerato_al_mese  = "3";
        String periodo_considerato_dal_anno  = "2018";
        String periodo_considerato_al_anno  = "2018";

        CercaPagamentiEmessiConditionBuilder cercaPagamentiEmessiConditionBuilder = new CercaPagamentiEmessiConditionBuilder();
        cercaPagamentiEmessiConditionBuilder.filtra_per_periodo(mese_di_competenza_mese, mese_di_competenza_anno, periodo_considerato_dal_mese, periodo_considerato_al_mese, periodo_considerato_dal_anno, periodo_considerato_al_anno);
        cercaPagamentiEmessiConditionBuilder.filtra_per_stato_pagamenti(stato_pagamenti);
        cercaPagamentiEmessiConditionBuilder.filtra_per_delegato("ala");
        cercaPagamentiEmessiConditionBuilder.filtra_per_cod_tip_int(codTipint);
        List<Condition> conditions = cercaPagamentiEmessiConditionBuilder.getConditions();

        String customSelect = "SELECT DISTINCT m FROM Mandato m LEFT JOIN m.mandatoDettaglioList md LEFT JOIN md.paiInterventoMeseList pim";
//        List<Mandato> mandatoList = persistenceAdapter.find(Mandato.class, customSelect, conditions);

        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("eclipselink.logging.level", "ALL");
        systemProperties.setProperty("eclipselink.logging.parameters", "true");
        TypedQuery<Mandato> query = ConditionUtils.createQuery(entityManager, Mandato.class, customSelect, conditions, null, null, null, null);
        query.getResultList();
//        JPAQuery jpaQuery = query.unwrap(JPAQuery.class);
        EJBQueryImpl qjpaQuery = query.unwrap(EJBQueryImpl.class);
        String sql = qjpaQuery.getDatabaseQuery().getSQLString();
        System.out.println("sql: \n\n\n" + sql  +"\n");

    }

}