package it.wego.welfarego.persistence.validators;

import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.exception.BudgetAssentiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class PaiInterventoBudgetValidator {

	private static Logger logger = LoggerFactory
			.getLogger("it.wego.welfarego.persistence.validators.PaiInterventoBudgetValidator");

	public static void verificaEsistenzaBudget(List<PaiInterventoMese> proposte, String cognomeNome,
			boolean escludiVerificaEsistenzaBudget) throws BudgetAssentiException {

		boolean esisteBudget = false;

		if (escludiVerificaEsistenzaBudget) {
			return;
		}

		if (proposte == null || proposte.size() == 0) {
			String msgTemplate = "Si è verificato un errore: Assegnare un budget all'intervento dell'utente %s prima di proseguire.";
			throw new RuntimeException(String.format(msgTemplate, cognomeNome));
		}

		logger.debug("proposte.size()" + proposte.size());

		for (PaiInterventoMese proposta : proposte) {

			boolean esisteBdgPrevEur = proposta.getBdgPrevEur() != null
					&& proposta.getBdgPrevEur().compareTo(new BigDecimal(0)) > 0;
			boolean esisteBdgPrevistoQuantita = proposta.getBdgPrevQta() != null
					&& proposta.getBdgPrevQta().compareTo(new BigDecimal(0)) > 0;

			if (esisteBdgPrevEur || esisteBdgPrevistoQuantita) {
				logger.debug("proposta: " + proposta.getPaiInterventoMesePK());
				esisteBudget = true;
				break;
			}
		}

		if (!esisteBudget) {
			throw new BudgetAssentiException(
					"non ci sono budget assegnati all' intervento dell'utente :" + cognomeNome);
		}
	}

}
