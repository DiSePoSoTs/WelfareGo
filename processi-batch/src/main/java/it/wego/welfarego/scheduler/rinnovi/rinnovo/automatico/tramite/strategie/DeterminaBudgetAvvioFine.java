package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.tramite.strategie;

import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.scheduler.dto.BudgetEAnniDto;
import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.RinnovoAutomaticoteUtils;
import it.wego.welfarego.services.interventi.DeterminaAnnoAvvioFine;

import java.util.List;

class DeterminaBudgetAvvioFine {

	
	private RinnovoAutomaticoteUtils rinnovoAutomaticoUtils;


	public DeterminaBudgetAvvioFine() {
		rinnovoAutomaticoUtils = new RinnovoAutomaticoteUtils();
	}

	public DeterminaBudgetAvvioFine(RinnovoAutomaticoteUtils creaProposte) {
		this.rinnovoAutomaticoUtils = creaProposte;
	}




	public BudgetEAnniDto determina_anni_e_budget_avvio_fine(PaiIntervento interventoPadre, PaiIntervento nuovoIntervento, BudgetTipoInterventoDao budgetTipoInterventoDao) {


		DeterminaAnnoAvvioFine determinaAnnoAvvioFine = new DeterminaAnnoAvvioFine(new InterventoDto(nuovoIntervento));
		int annoAvvio = determinaAnnoAvvioFine.getAnnoAvvio();
		int annoFine = determinaAnnoAvvioFine.getAnnoFine();

		String codiceImpegnoInterventoPadre = interventoPadre.getCodImpProroga();
		PaiInterventoPK nuovoInterventoPK = nuovoIntervento.getPaiInterventoPK();
		String codTipint = nuovoInterventoPK.getCodTipint();
		List<BudgetTipIntervento> budgets = budgetTipoInterventoDao.findByCodTipint(codTipint);

		BudgetTipIntervento budgetAnnoAvvio = rinnovoAutomaticoUtils.getBudgetPerCodiceImpegnoEAnno(budgets, codiceImpegnoInterventoPadre, annoAvvio);
		BudgetTipIntervento budgetAnnoFine = rinnovoAutomaticoUtils.getBudgetPerCodiceImpegnoEAnno(budgets, codiceImpegnoInterventoPadre, annoFine);
		
		BudgetEAnniDto  dto = new BudgetEAnniDto();
		dto.setAnnoAvvio(annoAvvio);
		dto.setAnnoFine(annoFine);
		dto.setBudgetAnnoAvvio(budgetAnnoAvvio);
		dto.setBudgetAnnoFine(budgetAnnoFine);
		return dto;
	}
	
	
}
