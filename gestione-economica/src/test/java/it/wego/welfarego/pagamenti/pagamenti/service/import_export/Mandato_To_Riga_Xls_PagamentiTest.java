package it.wego.welfarego.pagamenti.pagamenti.service.import_export;

import com.google.common.collect.Iterables;
import it.wego.welfarego.pagamenti.ExportFileUtils;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoMesePK;
import org.testng.annotations.Test;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Mandato_To_Riga_Xls_PagamentiTest {

    /*
24770951
24770953
24770955
24770957
24770959
24770961
24770963
24770965
24770967
24770969
24770971
24583115
24770973
24770975
24770977
24770979
24770981
24770983
24770985
24770987
24770989
24770991
24770993
24770995
24770997
24770999
24771001
24771003
24771007

    * */
    @Test
    public void aa(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        List<Mandato> mandatowithoutZero = new ArrayList<Mandato>();
        Mandato m = null;
        EntityManager entityManager = factory.createEntityManager();
        Query q = entityManager.createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", 24770951);
        m = (Mandato) q.getSingleResult();
        mandatowithoutZero.add(m);

        q = entityManager.createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", 24576017);
        m = (Mandato) q.getSingleResult();
        mandatowithoutZero.add(m);

        q = entityManager.createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", 24576019);
        m = (Mandato) q.getSingleResult();
        mandatowithoutZero.add(m);

        q = entityManager.createNamedQuery("Mandato.findByIdMan");
        q.setParameter("idMan", 24576021);
        m = (Mandato) q.getSingleResult();
        mandatowithoutZero.add(m);

        Mandato_To_Riga_Xls_Pagamenti mandato_to_riga_xls_pagamenti = new Mandato_To_Riga_Xls_Pagamenti();
        Iterable<Iterable> iterable = Iterables.transform(mandatowithoutZero, mandato_to_riga_xls_pagamenti);
        Iterable<Iterable> aaa = Iterables.concat(Arrays.asList(ExportFileUtils.intestazioniXlsPagamenti), iterable);
        Iterator<Iterable> iterator = aaa.iterator();
        while (iterator.hasNext()){
            Iterable next = iterator.next();
            System.out.println(next);
        }
    }


    private Mandato get_mandato_01() {
        Mandato mandato = new Mandato();
        mandato.setCfBeneficiario("CfBeneficiario");
        mandato.setCfDelegante("CfDelegante");

        List<PaiInterventoMese> paiInterventoMeseList = new ArrayList<PaiInterventoMese>();
        PaiIntervento paiIntervento = new PaiIntervento();
        paiIntervento.setPaiInterventoMeseList(paiInterventoMeseList);

        MandatoDettaglio mandatoDettaglio = new MandatoDettaglio();
        List<MandatoDettaglio> mandatoDettaglioList = new ArrayList<MandatoDettaglio>();
        mandatoDettaglioList.add(mandatoDettaglio);
        mandatoDettaglio.setPaiInterventoMeseList(paiInterventoMeseList);
        mandato.setMandatoDettaglioList(mandatoDettaglioList);


        PaiInterventoMese paiInterventoMese = new PaiInterventoMese(new PaiInterventoMesePK(3, "dfas", 43,(short)4,(short)5, (short) 2018, "cod_impe"));
        paiInterventoMeseList.add(paiInterventoMese);
        mandatoDettaglio.setPaiIntervento(paiIntervento);


        mandato.setNomeDelegante("-");
        mandato.setCognomeDelegante("Cognome Delegante");
        mandato.setModalitaErogazione("fadf");
        mandato.setCognomeBeneficiario("Cognome Beneficiario");



        return mandato;
    }

    private Mandato get_mandato_02() {
        Mandato mandato = new Mandato();

        mandato.setCfBeneficiario("CfBeneficiario");
        mandato.setCfDelegante("CfDelegante");

        List<PaiInterventoMese> paiInterventoMeseList = new ArrayList<PaiInterventoMese>();
        PaiIntervento paiIntervento = new PaiIntervento();
        paiIntervento.setPaiInterventoMeseList(paiInterventoMeseList);

        MandatoDettaglio mandatoDettaglio = new MandatoDettaglio();
        List<MandatoDettaglio> mandatoDettaglioList = new ArrayList<MandatoDettaglio>();
        mandatoDettaglioList.add(mandatoDettaglio);
        mandatoDettaglio.setPaiInterventoMeseList(paiInterventoMeseList);
        mandato.setMandatoDettaglioList(mandatoDettaglioList);


        PaiInterventoMese paiInterventoMese = new PaiInterventoMese(new PaiInterventoMesePK(3, "dfas", 43,(short)4,(short)5, (short) 2018, "cod_impe"));
        paiInterventoMeseList.add(paiInterventoMese);
        mandatoDettaglio.setPaiIntervento(paiIntervento);


        mandato.setNomeDelegante("Nome Delegante");
        mandato.setCognomeDelegante("Cognome Delegante");
        mandato.setModalitaErogazione("fadf");
        mandato.setCognomeBeneficiario("Cognome Beneficiario");



        return mandato;
    }

}