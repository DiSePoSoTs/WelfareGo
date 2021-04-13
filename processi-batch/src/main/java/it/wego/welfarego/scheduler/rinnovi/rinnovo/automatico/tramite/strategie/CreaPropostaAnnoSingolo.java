package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.tramite.strategie;

import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.scheduler.RinnovoException;
import it.wego.welfarego.scheduler.rinnovi.helper.RinnovoInterventoLogBuilder;

import java.math.BigDecimal;

class CreaPropostaAnnoSingolo {

    CreaPropostaAnnoSingolo (){

    }  

    public void crea_proposta(
            PaiIntervento nuovoIntervento, BudgetTipIntervento budgetAnnoAvvio, BudgetTipInterventoPK budgetPadrePK
            , BigDecimal bdgPrevEur, ParametriIndata idParamFasciaNuovoIntervento, BigDecimal importoStandard, BigDecimal bdgPrevQta
            , PaiInterventoMeseDao paiInterventoMeseDao,
            RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) throws RinnovoException {

        PaiInterventoPK nuovoInterventoPK = nuovoIntervento.getPaiInterventoPK();
        BudgetTipInterventoPK budgetAnnoAvvioPK = null;

        if (budgetAnnoAvvio == null) {
            budgetAnnoAvvioPK  = budgetPadrePK;
        }else{
            budgetAnnoAvvioPK = budgetAnnoAvvio.getBudgetTipInterventoPK();
        }

        if (budgetAnnoAvvioPK == null ){
            throw new RuntimeException("budgetAnnoAvvioPK non valorizzato per " + nuovoInterventoPK);
        }

        rinnovoInterventoLogBuilder.setBudgetPerProposte(budgetAnnoAvvioPK);

        paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetAnnoAvvioPK, bdgPrevEur, bdgPrevQta, idParamFasciaNuovoIntervento);
    }



    
    
}
