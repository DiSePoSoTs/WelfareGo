package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;

public class RinnovoAutomaticoteUtils {

    public  BudgetTipIntervento getBudgetPerCodiceImpegnoEAnno(List<BudgetTipIntervento> budgets, String codiceImpegnoToMatch, int year) {

        Short yearToMatch = Short.valueOf(year + "");

        return getBudgetPerCodiceImpegnoEAnno(budgets, codiceImpegnoToMatch, yearToMatch);
    }

    public BudgetTipIntervento getBudgetPerCodiceImpegnoEAnno(List<BudgetTipIntervento> budgets, String codiceImpegnoToMatch, Short yearToMatch) {
        if (budgets == null) {
            return null;
        }

        BudgetTipIntervento budget = null;

        for (BudgetTipIntervento x : budgets) {
            String codImpegnoBudget = x.getBudgetTipInterventoPK().getCodImpe();
            Short annoErogazioneBudget = x.getAnnoErogazione();
            if (match_per_anno_e_codice_impegno(codiceImpegnoToMatch, yearToMatch, codImpegnoBudget, annoErogazioneBudget)) {
                budget = x;
                break;
            }
        }

        if (budget == null) {
            for (BudgetTipIntervento x : budgets) {
                if (x.getAnnoErogazione().equals(yearToMatch)) {
                    budget = x;
                    break;
                }
            }
        }

        return budget;
    }

    public  boolean match_per_anno_e_codice_impegno(String codiceImpegnoInterventoPadre, Short yearAsShort, String codImpeBudgetCorrente, Short annoErogazione ) {

        return annoErogazione.equals(yearAsShort)
                && codiceImpegnoInterventoPadre != null
                && codiceImpegnoInterventoPadre.equalsIgnoreCase(codImpeBudgetCorrente);
    }

    public  BudgetTipInterventoPK getBudgetPadre(PaiIntervento interventoPadre, PaiInterventoMeseDao paiInterventoMeseDao) {
    	BudgetTipInterventoPK budgetPadrePk = null;

        PaiInterventoPK interventoPadrePK = interventoPadre.getPaiInterventoPK();
        Integer codPaiPadre = interventoPadrePK.getCodPai();
        String codTipIntPadre = interventoPadrePK.getCodTipint();
        Integer cntTipIntPadre = interventoPadrePK.getCntTipint();

        List<PaiInterventoMese> interventiMesePadre = paiInterventoMeseDao.findForPaiInt(codPaiPadre, codTipIntPadre, cntTipIntPadre);

        BudgetTipIntervento budgetPadre = null;

        if (!interventiMesePadre.isEmpty()) {
            PaiInterventoMese paiInterventoMese = interventiMesePadre.get(0);
            budgetPadre = paiInterventoMese.getBudgetTipIntervento();
        }
        
        if(budgetPadre != null) {
            budgetPadrePk = budgetPadre.getBudgetTipInterventoPK();
        }

        return budgetPadrePk;
    }

    public  BigDecimal calcolaBdgPrevQta(BigDecimal costoPrevisto, BigDecimal importoStandard) {
        return costoPrevisto.divide(importoStandard, MathContext.DECIMAL32);
    }

    public  BigDecimal calcolaImportoStandard(PaiIntervento nuovoIntervento) {

        TipologiaIntervento tipologiaIntervento = nuovoIntervento.getTipologiaIntervento();
        BigDecimal importoStandard = tipologiaIntervento.getImpStdCosto();

        Tariffa tariffaNuovoIntervento = nuovoIntervento.getTariffa();

        if (tariffaNuovoIntervento != null) {
            importoStandard = tariffaNuovoIntervento.getCosto();
        }

        return importoStandard;
    }


}
