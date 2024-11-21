package it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti.test.factory.CasiDiTestFactory;
import it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti.test.factory.DatiDto;

import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class IntervalliDiDistribuzioneEvaluatorTest {

    @Test
    public void bb() {
        ArrayList<Integer> integers = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), 0));
        System.out.println(integers);
    }



    @Test
    public void distribuisci_costi_caso_01() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(1);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_02_multibudget() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(2);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
//            System.out.println("budget:" + budget.getAnnoErogazione());
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }


    @Test
    public void distribuisci_costi_caso_03() throws ParseException {

        DatiDto datiDto = CasiDiTestFactory.getCase(3);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }


    @Test
    public void distribuisci_costi_caso_04() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(4);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_05() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(5);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_06() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(6);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_07() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(7);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertEquals(distribuzioneCostiLocal.size() , numeroIntervalliAttesi_per_budget);
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_08() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(8);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_09() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(9);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }


    @Test
    public void distribuisci_costi_caso_10() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(10);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_11() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(11);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }


    @Test
    public void distribuisci_costi_caso_12() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(12);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_13() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(13);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

    @Test
    public void distribuisci_costi_caso_14() throws ParseException {

        // given
        DatiDto datiDto = CasiDiTestFactory.getCase(14);
        System.out.println(datiDto);
        boolean ignoraArmonizzazione = false;
        boolean useDurataMesi = true;
        List<Integer> distribuzioneCostiLocal = null;
        List<BudgetTipIntervento> budgetsPerIntervento = datiDto.getBudgetsPerIntervento();
        PaiIntervento paiIntervento = datiDto.getPaiIntervento();

        for (BudgetTipIntervento budget : budgetsPerIntervento) {

            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budget, distribuzioneCostiLocal);
            //when
            intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

            // then
            distribuzioneCostiLocal = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
            int numeroIntervalliAttesi_per_budget = datiDto.getNumeroIntervalliAttesi(budget);
            int annoAttesoInizioIntervento = datiDto.getAnnoPagamenti(budget);
            int meseAttesoInizioIntervento = datiDto.getMeseInizioPagamenti(budget);
            assertTrue(distribuzioneCostiLocal.size() == numeroIntervalliAttesi_per_budget, distribuzioneCostiLocal.size() + ", " + datiDto.getNumeroIntervalliAttesi(budget));
            assertEquals(intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti(), annoAttesoInizioIntervento);
            assertEquals(intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento() , meseAttesoInizioIntervento);
        }
    }

}




