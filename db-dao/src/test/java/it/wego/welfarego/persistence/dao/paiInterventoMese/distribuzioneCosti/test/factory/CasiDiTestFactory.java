package it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti.test.factory;

import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CasiDiTestFactory {
    public static DatiDto getCase(int caso_di_test) {
        switch (caso_di_test) {
            case 1:
                return caso1();
            case 2:
                return caso2();
            case 3:
                return caso3();
            case 4:
                return caso4();
            case 5:
                return caso5();
            case 6:
                return caso6();
            case 7:
                return caso7();
            case 8:
                return caso8();
            case 9:
                return caso9();
            case 10:
                return caso10();
            case 11:
                return caso11();
            case 12:
                return caso12();
            case 13:
                return caso13();
            case 14:
                return caso14();
            default:
                return null;
        }
    }




    /**
     * <pre>
     * DATI CHIAVE PER CONSIDERARE LE CASISTICHE
     * anno erogazione budget (2018, 2019)
     * anno/mese inizio intervento,
     * durata intervento
     *
     * CASI DI TEST
     * 1: 2018, 2018/8,  1
     * 2: (2018,2019),  2018/8,  6
     * 3: (2018, 2019), 2018/2, 13
     * 4: 2018, 2019/2,  6
     * 5: 2018, 2017/2,  6
     * 6: 2019, 2017/2,  6
     * 7: 2019, 2017/2, 28
     * 8: 2019, 2018/9,  1,
     * 9: 2019, 2018/9,  2,
     * 10: (2018,2019), 2018/9,  7
     * 11: (2018,2019), 2018/10,  2
     * 12: (2018, 2019), 2018/12, 12
     * 13: 2017, 2018/10, 12
     * 14: 2018, 2018/10, 12
     * </pre>
     */
    private static DatiDto caso14() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> datiAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 12;
        String dataInizioIntervento = "05/10/2018";

        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 10, 3);



        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short)2018);
        budgets.add(budget_2018);
        datiAttesi_perBudget.put(budget_2018, dati_attesi_2018);


        DatiDto caso = new DatiDto(paiIntervento, budgets, datiAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso13() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> datiAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 12;
        String dataInizioIntervento = "05/10/2018";

        DatiDto.DatiAttesi dati_attesi_2017 = new DatiDto().new DatiAttesi(2018, 10, 12);



        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2017 = getBudgetTipIntervento((short)2017);
        budgets.add(budget_2017);
        datiAttesi_perBudget.put(budget_2017, dati_attesi_2017);


        DatiDto caso = new DatiDto(paiIntervento, budgets, datiAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso12() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> datiAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 12;
        String dataInizioIntervento = "05/12/2018";

        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 12, 1);
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2019, 1, 11);



        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short)2018);
        budgets.add(budget_2018);
        datiAttesi_perBudget.put(budget_2018, dati_attesi_2018);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        datiAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, datiAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso11() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> datiAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 2;
        String dataInizioIntervento = "24/10/2018";

        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 10, 2);
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2018, 10, 2);



        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short)2018);
        budgets.add(budget_2018);
        datiAttesi_perBudget.put(budget_2018, dati_attesi_2018);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        datiAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, datiAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso10() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 7;
        String dataInizioIntervento = "24/09/2018";

        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 9, 4);
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2019, 1, 3);


        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short)2018);
        budgets.add(budget_2018);
        numeroIntervalliAttesi_perBudget.put(budget_2018, dati_attesi_2018);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }


    private static DatiDto caso9() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 2;
        String dataInizioIntervento = "24/09/2018";
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2018, 9, 2);



        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso8() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 1;
        String dataInizioIntervento = "24/09/2018";
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2018, 9, 1);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);


        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        System.out.println(caso);
        return caso;
    }

    private static DatiDto caso7() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 12;
        String dataInizioIntervento = "24/04/2017";
        DatiDto.DatiAttesi dati_attesi = new DatiDto().new DatiAttesi(2017, 4, 12);

        BudgetTipIntervento budget = getBudgetTipIntervento((short) 2019);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget);
        numeroIntervalliAttesi_perBudget.put(budget, dati_attesi);


        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso6() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 6;
        String dataInizioIntervento = "24/02/2017";
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2017, 2, 6);


        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);


        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso5() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 6;
        String dataInizioIntervento = "24/02/2017";
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2017, 2, 6);

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short) 2018);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget_2018);
        numeroIntervalliAttesi_perBudget.put(budget_2018, dati_attesi_2019);


        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso4() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 6;
        String dataInizioIntervento = "24/02/2019";
        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2019,2, 6);

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short) 2018);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget_2018);
        numeroIntervalliAttesi_perBudget.put(budget_2018, dati_attesi_2018);


        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso3() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();


        Integer durataIntervento = 13;
        String dataInizioIntervento = "24/02/2018";

        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 2, 11);
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2019, 1, 2);

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short) 2018);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget_2018);
        numeroIntervalliAttesi_perBudget.put(budget_2018, dati_attesi_2018);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso2() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> numeroIntervalliAttesi_perBudget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 6;
        String dataInizioIntervento = "24/08/2018";
        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 8, 5);
        DatiDto.DatiAttesi dati_attesi_2019 = new DatiDto().new DatiAttesi(2019, 1,1 );
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();

        BudgetTipIntervento budget_2018 = getBudgetTipIntervento((short)2018);
        budgets.add(budget_2018);
        numeroIntervalliAttesi_perBudget.put(budget_2018, dati_attesi_2018);

        BudgetTipIntervento budget_2019 = getBudgetTipIntervento((short) 2019);
        budgets.add(budget_2019);
        numeroIntervalliAttesi_perBudget.put(budget_2019, dati_attesi_2019);

        DatiDto caso = new DatiDto(paiIntervento, budgets, numeroIntervalliAttesi_perBudget);
        return caso;
    }

    private static DatiDto caso1() {
        Map<BudgetTipIntervento, DatiDto.DatiAttesi> dati_attesi_per_budget = new HashMap<BudgetTipIntervento, DatiDto.DatiAttesi>();
        Integer durataIntervento = 1;
        String dataInizioIntervento = "24/08/2018";
        short annoErogazione = 2018;
        DatiDto.DatiAttesi dati_attesi_2018 = new DatiDto().new DatiAttesi(2018, 8, 1);
        BudgetTipIntervento budget = getBudgetTipIntervento(annoErogazione);
        PaiIntervento paiIntervento = getPaiIntervento(durataIntervento, dataInizioIntervento);
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        budgets.add(budget);
        dati_attesi_per_budget.put(budget, dati_attesi_2018);
        DatiDto caso = new DatiDto(paiIntervento, budgets, dati_attesi_per_budget);
        return caso;
    }


    private static BudgetTipIntervento getBudgetTipIntervento(Short annoErogazione) {
        BudgetTipIntervento budget = new BudgetTipIntervento();
        BudgetTipInterventoPK pk = new BudgetTipInterventoPK();
        pk.setCodAnno(annoErogazione);
        budget.setBudgetTipInterventoPK(pk);
        budget.setAnnoErogazione(annoErogazione);
        return budget;
    }


    private static PaiIntervento getPaiIntervento(Integer durataIntervento, String dataInizioIntervento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        PaiIntervento intervento = new PaiIntervento();
        PaiInterventoPK pk = new PaiInterventoPK();
        intervento.setPaiInterventoPK(pk);
        intervento.setDurMesi(durataIntervento);
        try {
            intervento.setDtAvvio(sdf.parse(dataInizioIntervento));
        } catch (ParseException e) {
            throw new RuntimeException("", e);
        }
        return intervento;
    }
}
