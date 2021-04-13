package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.tramite.strategie;

import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.scheduler.rinnovi.helper.RinnovoInterventoLogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.RinnovoAutomaticoteUtils;

import java.math.BigDecimal;

class CreaPropostaAnniMultipli {

    private static final Logger logger = LoggerFactory.getLogger(CreaPropostaAnniMultipli.class);

    private RinnovoAutomaticoteUtils rinnovoAutomaticoteUtils = null;
    
    public CreaPropostaAnniMultipli() {
    	rinnovoAutomaticoteUtils = new RinnovoAutomaticoteUtils();
    }
    
    public void creaProposta(PaiIntervento interventoPadre, PaiInterventoMeseDao paiInterventoMeseDao,
                             ParametriIndata idParamFasciaNuovoIntervento, PaiInterventoPK nuovoInterventoPK, BigDecimal importoStandard,
                             int annoAvvio, int annoFine, BudgetTipIntervento budgetAnnoAvvio, BudgetTipIntervento budgetAnnoFine,
                             BigDecimal costoPrimoAnno, BigDecimal costoSecondoAnno, BigDecimal costoPrevisto, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) {

        if (budgetAnnoAvvio != null && budgetAnnoFine != null) {
            rinnovoInterventoLogBuilder.setCasoPropostaAnniMultipli("caso_1");

        	BudgetTipInterventoPK budgetAnnoAvvioPK = budgetAnnoAvvio.getBudgetTipInterventoPK();
        	BudgetTipInterventoPK budgetAnnoFinePK = budgetAnnoFine.getBudgetTipInterventoPK();
            caso_1(paiInterventoMeseDao, nuovoInterventoPK, idParamFasciaNuovoIntervento, importoStandard, budgetAnnoAvvioPK, budgetAnnoFinePK, costoPrimoAnno, costoSecondoAnno, costoPrevisto, rinnovoInterventoLogBuilder);


        } else if (budgetAnnoAvvio != null && budgetAnnoFine == null) {
            rinnovoInterventoLogBuilder.setCasoPropostaAnniMultipli("caso_2");

        	BudgetTipInterventoPK budgetAnnoAvvioPk = budgetAnnoAvvio.getBudgetTipInterventoPK();
            caso_2(paiInterventoMeseDao
                    , nuovoInterventoPK
                    , idParamFasciaNuovoIntervento
                    , importoStandard
                    , budgetAnnoAvvioPk
                    , costoPrimoAnno
                    , costoSecondoAnno, costoPrevisto, rinnovoInterventoLogBuilder);


        } else if (budgetAnnoAvvio == null && budgetAnnoFine != null) {
            rinnovoInterventoLogBuilder.setCasoPropostaAnniMultipli("caso_3");

        	BudgetTipInterventoPK budgetAnnoFinePK = budgetAnnoFine.getBudgetTipInterventoPK();
            caso_2(paiInterventoMeseDao,
                    nuovoInterventoPK
                    , idParamFasciaNuovoIntervento
                    , importoStandard
                    , budgetAnnoFinePK
                    , costoPrimoAnno
                    , costoSecondoAnno, costoPrevisto, rinnovoInterventoLogBuilder);

        } else if (budgetAnnoAvvio == null && budgetAnnoFine == null) {
            rinnovoInterventoLogBuilder.setCasoPropostaAnniMultipli("caso_4");

            String codTipint = nuovoInterventoPK.getCodTipint();
            String msg_1 = String.format("Non esiste un budget per il tipo intervento:%s associato agli anni:(%s, %s)", codTipint, annoAvvio, annoFine);
            String msg_2 = String.format(", utilizzerò quello dell' intervento padre:%s", interventoPadre.getPaiInterventoPK());

            rinnovoInterventoLogBuilder.setNote(msg_1 + msg_2);

            BudgetTipInterventoPK budgetPadrePk = rinnovoAutomaticoteUtils.getBudgetPadre(interventoPadre, paiInterventoMeseDao);
            
            caso_2(paiInterventoMeseDao,
                    nuovoInterventoPK
                    , idParamFasciaNuovoIntervento
                    , importoStandard
                    , budgetPadrePk
                    , costoPrimoAnno
                    , costoSecondoAnno, costoPrevisto, rinnovoInterventoLogBuilder);
        }
    }

    private void caso_1(
            PaiInterventoMeseDao paiInterventoMeseDao
            , PaiInterventoPK nuovoInterventoPK
            , ParametriIndata idParamFasciaNuovoIntervento
            , BigDecimal importoStandard
            , BudgetTipInterventoPK budgetAnnoAvvioPk, BudgetTipInterventoPK budgetAnnoFinePk
            , BigDecimal costoPrimoAnno, BigDecimal costoSecondoAnno, BigDecimal costoPrevisto, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) {

        BigDecimal quantita = costoPrimoAnno.divide(importoStandard);
        rinnovoInterventoLogBuilder.setBudgetPerPropostePrimoAnno(budgetAnnoAvvioPk);
        paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetAnnoAvvioPk, costoPrimoAnno, quantita, idParamFasciaNuovoIntervento);

        quantita = costoSecondoAnno.divide(importoStandard);
        rinnovoInterventoLogBuilder.setBudgetPerProposteSecondoAnno(budgetAnnoFinePk);
        paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetAnnoFinePk, costoSecondoAnno, quantita, idParamFasciaNuovoIntervento);
    }




    private void caso_2(
            PaiInterventoMeseDao paiInterventoMeseDao
            , PaiInterventoPK nuovoInterventoPK
            , ParametriIndata idParamFasciaNuovoIntervento
            , BigDecimal importoStandard
            , BudgetTipInterventoPK budgetPk
            , BigDecimal costoPrimoAnno, BigDecimal costoSecondoAnno, BigDecimal costoPrevisto,
            RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) {
        
        BigDecimal quantita = costoPrimoAnno.divide(importoStandard);
        rinnovoInterventoLogBuilder.setBudgetPerPropostePrimoAnno(budgetPk);
        paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetPk, costoPrimoAnno, quantita, idParamFasciaNuovoIntervento);

        quantita = costoSecondoAnno.divide(importoStandard);
        rinnovoInterventoLogBuilder.setBudgetPerProposteSecondoAnno(budgetPk);
        paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetPk, costoSecondoAnno, quantita, idParamFasciaNuovoIntervento);
    }

	

}
