package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico;


import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.scheduler.RinnovoException;
import it.wego.welfarego.scheduler.rinnovi.helper.RinnovoInterventoLogBuilder;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;

import java.math.BigDecimal;
public class CreaProposteConBudgetDelPadre {
	
	private RinnovoAutomaticoteUtils rinnovoAutomaticoteUtils = null;
	private CalcolaCostoInterventoService calcolaCostoInterventoService = null;
	
	public CreaProposteConBudgetDelPadre() {
		rinnovoAutomaticoteUtils = new RinnovoAutomaticoteUtils();
		calcolaCostoInterventoService = new CalcolaCostoInterventoService();
	}
	
	public CreaProposteConBudgetDelPadre(RinnovoAutomaticoteUtils rinnovoAutomaticoteUtils, CalcolaCostoInterventoService calcolaCostoInterventoService) {
		this.rinnovoAutomaticoteUtils = rinnovoAutomaticoteUtils;
		this.calcolaCostoInterventoService = calcolaCostoInterventoService;
	}	
	
    public void crea_proposta(PaiIntervento interventoPadre, PaiIntervento nuovoIntervento, PaiInterventoMeseDao paiInterventoMeseDao, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) throws Exception {

    	BudgetTipInterventoPK budgetPadrePk = rinnovoAutomaticoteUtils.getBudgetPadre(interventoPadre, paiInterventoMeseDao);
		rinnovoInterventoLogBuilder.setBudgetPadrePk(budgetPadrePk);
        
    	if (budgetPadrePk != null) {

            BigDecimal importoStandard = rinnovoAutomaticoteUtils.calcolaImportoStandard(nuovoIntervento);
            BigDecimal bdgPrevEur = calcolaCostoInterventoService.calcolaBdgPrevEur(nuovoIntervento);
            BigDecimal bdgPrevQta = rinnovoAutomaticoteUtils.calcolaBdgPrevQta(bdgPrevEur, importoStandard);

            Pai pai = nuovoIntervento.getPai();
			ParametriIndata idParamFasciaNuovoIntervento = pai.getIdParamFascia();
            PaiInterventoPK nuovoInterventoPK = nuovoIntervento.getPaiInterventoPK();

			rinnovoInterventoLogBuilder.setBudgetPrevEur(bdgPrevEur);
			rinnovoInterventoLogBuilder.setBudgetPrevQta(bdgPrevQta);
            paiInterventoMeseDao.insertProp(nuovoInterventoPK, budgetPadrePk, bdgPrevEur, bdgPrevQta, idParamFasciaNuovoIntervento);

            nuovoIntervento.setCostoPrev(bdgPrevEur);

        } else {
            throw new RinnovoException("budget assente per intervento padre: " + interventoPadre.getPaiInterventoPK().toString(), null);
        }

    }











}
