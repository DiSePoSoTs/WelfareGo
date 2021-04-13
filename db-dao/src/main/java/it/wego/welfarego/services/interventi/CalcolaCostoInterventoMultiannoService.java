package it.wego.welfarego.services.interventi;

import it.wego.welfarego.dto.CostiInterventoMultiAnno;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import org.joda.time.DateTime;
import org.joda.time.Months;

import java.math.BigDecimal;
import java.util.Date;

public class CalcolaCostoInterventoMultiannoService {
	
	private CalcolaCostoInterventoService calcolaCostoInterventoService;
	private DeterminaAnnoAvvioFine determinaAnnoAvvioFine;

	public CalcolaCostoInterventoMultiannoService() {
		calcolaCostoInterventoService = new CalcolaCostoInterventoService();
		determinaAnnoAvvioFine = new DeterminaAnnoAvvioFine();
	}

	public CostiInterventoMultiAnno invoke(PaiIntervento nuovoInterventoNtt) {

		BigDecimal costoPrimoAnno = null;
		BigDecimal costoSecondoAnno = null;

		InterventoDto nuovoIntervento = new InterventoDto(nuovoInterventoNtt);

		determinaAnnoAvvioFine.setIntervento(nuovoIntervento);
		int annoAvvio = determinaAnnoAvvioFine.getAnnoAvvio();
		int annoFine = determinaAnnoAvvioFine.getAnnoFine();
		DateTime fineAnno = new DateTime(annoAvvio, 12, 31, 23, 59);
		DateTime inizioAnno = new DateTime(annoFine, 01, 01, 00, 00);

		Tariffa tariffa = nuovoIntervento.getTariffa();
		Integer durMesi = nuovoIntervento.getDurMesi();
		Date dtAvvio = nuovoIntervento.getDtAvvio();
		Date dtFine = nuovoIntervento.getDtFine();
		BigDecimal quantita = nuovoIntervento.getQuantita();
		char flgFineDurata = nuovoIntervento.getTipologiaIntervento().getFlgFineDurata();

		Integer mesiFineAnno = Months.monthsBetween(new DateTime(dtAvvio), fineAnno).getMonths();
		boolean isPerMesi = flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_D;
		boolean isConStruttura = tariffa != null;

		TipologiaIntervento tipologiaIntervento = nuovoIntervento.getTipologiaIntervento();
		if (isPerMesi && isConStruttura) {
			costoPrimoAnno = calcolaCostoInterventoService.calcolaCostoInterventoConStrutturaPerDurata(quantita, mesiFineAnno, tipologiaIntervento, tariffa, dtAvvio);
			costoSecondoAnno = calcolaCostoInterventoService
					.calcolaCostoInterventoConStrutturaPerDurata(quantita, durMesi - mesiFineAnno, tipologiaIntervento, tariffa, dtAvvio);

		} else if (isPerMesi && !isConStruttura) {
			costoPrimoAnno = calcolaCostoInterventoService.calcolaCostoInterventoPerDurata(tipologiaIntervento,
					quantita, mesiFineAnno, dtAvvio);
			costoSecondoAnno = calcolaCostoInterventoService.calcolaCostoInterventoPerDurata(tipologiaIntervento,
					quantita, durMesi - mesiFineAnno, dtAvvio);

		} else if (!isPerMesi && isConStruttura) {
			costoPrimoAnno = calcolaCostoInterventoService.calcolaCostoInterventoConStruttura(tipologiaIntervento,
					quantita, dtAvvio, fineAnno.toDate(), tariffa);
			costoSecondoAnno = calcolaCostoInterventoService.calcolaCostoInterventoConStruttura(tipologiaIntervento,
					quantita, inizioAnno.toDate(), dtFine, tariffa);

		} else if (!isPerMesi && !isConStruttura) {
			costoPrimoAnno = calcolaCostoInterventoService.calcolaCostoIntervento(tipologiaIntervento, quantita,
					dtAvvio, fineAnno.toDate());
			costoSecondoAnno = calcolaCostoInterventoService.calcolaCostoIntervento(tipologiaIntervento, quantita,
					inizioAnno.toDate(), dtFine);

		} else {
			throw new RuntimeException(
					String.format("qui non dovrei arrivare mai, nuovoIntervento:%s", nuovoIntervento));
		}
 
		return new CostiInterventoMultiAnno(costoPrimoAnno, costoSecondoAnno);
	}

}
