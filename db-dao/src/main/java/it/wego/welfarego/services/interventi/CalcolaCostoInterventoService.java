package it.wego.welfarego.services.interventi;

import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;

import java.math.BigDecimal;
import java.util.Date;

public class CalcolaCostoInterventoService {

	public static final BigDecimal SETTIMANE_IN_MESE = new BigDecimal(4);

	public BigDecimal calcolaBdgPrevEur(PaiIntervento intervento) {
		return calcolaBdgPrevEur(new InterventoDto(intervento));
	}

	public BigDecimal calcolaBdgPrevEur(InterventoDto intervento) {
		BigDecimal costo = null;

		Integer durMesi = intervento.getDurMesi();
		Date dtAvvio = intervento.getDtAvvio();
		Date dtFine = intervento.getDtFine();
		BigDecimal quantita = intervento.getQuantita();
		Tariffa tariffa = intervento.getTariffa();
		TipologiaIntervento tipologiaIntervento = intervento.getTipologiaIntervento();

		return calcolaBdgPrevEur(durMesi, dtAvvio, dtFine, quantita, tariffa, tipologiaIntervento);
	}

	public BigDecimal calcolaBdgPrevEur(Integer durMesi, Date dtAvvio, Date dtFine, BigDecimal quantita,
			Tariffa tariffa, TipologiaIntervento tipologiaIntervento) {
		BigDecimal costo;
		boolean isPerMesi = is_intervento_per_durata(tipologiaIntervento);
		boolean isConStruttura = tariffa != null;

		if (isPerMesi && isConStruttura) {
			costo = calcolaCostoInterventoConStrutturaPerDurata(quantita, durMesi, tipologiaIntervento, tariffa,
					dtAvvio);
		} else if (isPerMesi && !isConStruttura) {
			costo = calcolaCostoInterventoPerDurata(tipologiaIntervento, quantita, durMesi, dtAvvio);
		} else if (!isPerMesi && isConStruttura) {
			costo = calcolaCostoInterventoConStruttura(tipologiaIntervento, quantita, dtAvvio, dtFine, tariffa);
		} else if (!isPerMesi && !isConStruttura) {
			costo = calcolaCostoIntervento(tipologiaIntervento, quantita, dtAvvio, dtFine);
		} else {
			throw new RuntimeException(String.format("qui non dovrei arrivare mai"));
		}
		return costo;
	}

	BigDecimal calcolaCostoInterventoConStrutturaPerDurata(BigDecimal quantita, Integer durata, TipologiaIntervento ti,
			Tariffa tariffa, Date dtAvvio) {
		BigDecimal costo = null;
		BigDecimal costoStandard = tariffa.getCosto();

		if (is_costo_forfettario(tariffa)) {
			costo = costoStandard;

		} else {
			costo = quantita.multiply(costoStandard);

			if (is_quantita_mensile(ti)) {
				costo = costo.multiply(new BigDecimal(durata));

			} else if (is_quantita_settimanale(ti)) {
				costo = costo.multiply(new BigDecimal(durata));
				costo = costo.multiply(SETTIMANE_IN_MESE);

			} else if (is_quantita_giornaliera(ti)) {
				LocalDate jodaDtAvvio = new LocalDate(dtAvvio);
				LocalDate jodaDtFine = jodaDtAvvio.plusMonths(durata);
				int days = Days.daysBetween(jodaDtAvvio, jodaDtFine).getDays();
				costo = costo.multiply(new BigDecimal(days));
			} else {
				throw new RuntimeException(
						"la tipologia inetervento non è giornaliera, ne settimanale ne mensile. Tipologia intervento: "
								+ ti.getCodTipint() + " - " + ti.getDesTipint());
			}
		}

		return costo;
	}

	/**
	 * <pre>
	 se unità di misura euro && modalità selezione durata intervento== durata mesi
	 ==> interventi con (data_inizio, durata) --> costo_prev = quantita*durata mesi*importo standard di costo
	
	 se unità di misura euro && modalità selezione durata intervento== fine intervento
	 ==> interventi con (data_inizio, data_fine) --> costo_prev = quantita*giorni dell' intervento*importo standard di costo
	
	 quantità * importo_standard * durata
	 * </pre>
	 * 
	 * @param ti
	 * @param quantita
	 * @param durataAsInteger
	 * @param dtAvvio
	 * @return
	 */
	BigDecimal calcolaCostoInterventoPerDurata(TipologiaIntervento ti, BigDecimal quantita, Integer durataAsInteger,
			Date dtAvvio) {
		BigDecimal durata = new BigDecimal(durataAsInteger);
		BigDecimal importoStandardDiCosto = ti.getImpStdCosto();

		BigDecimal quantita_per_importo_standard_di_costo = quantita.multiply(importoStandardDiCosto);
		BigDecimal costo = null;

		if (is_quantita_mensile(ti) || is_intervento_per_durata(ti) && !is_quantita_settimanale(ti)) {
			costo = quantita_per_importo_standard_di_costo.multiply(durata);
		} else if (is_quantita_settimanale(ti)) {
			costo = quantita_per_importo_standard_di_costo.multiply(SETTIMANE_IN_MESE);
		} else if (!is_intervento_per_durata(ti) && is_quantita_giornaliera(ti)) {
			LocalDate jodaDtAvvio = new LocalDate(dtAvvio);
			LocalDate jodaDtFine = jodaDtAvvio.plusMonths(durataAsInteger);
			int days = Days.daysBetween(jodaDtAvvio, jodaDtFine).getDays();
			costo = quantita_per_importo_standard_di_costo.multiply(new BigDecimal(days));
		} else {
			throw new RuntimeException(
					"la tipologia intervento non è giornaliera, ne settimanale ne mensile. Tipologia intervento: "
							+ ti.getCodTipint() + " - " + ti.getDesTipint());
		}

		return costo;

	}

	BigDecimal calcolaCostoInterventoConStruttura(TipologiaIntervento ti, BigDecimal quantita, Date dal, Date al,
			Tariffa tariffa) {

		BigDecimal costo = null;

		BigDecimal costoStandard = tariffa.getCosto();

		if (is_costo_forfettario(tariffa)) {
			costo = costoStandard;

		} else {
			costo = quantita.multiply(costoStandard);

			if (is_quantita_mensile(ti)) {
				Integer numMesi = org.joda.time.Months.monthsBetween(new DateTime(dal), new DateTime(al).plusDays(1))
						.getMonths();
				costo = costo.multiply(new BigDecimal(numMesi));

			} else if (is_quantita_settimanale(ti)) {
				Integer numSettimane = Weeks.weeksBetween(new DateTime(dal), new DateTime(al)).getWeeks();
				costo = costo.multiply(new BigDecimal(numSettimane));

			} else if (is_quantita_giornaliera(ti)) {
				Integer numGiorni = Days.daysBetween(new DateTime(dal).withZone(DateTimeZone.forID("Europe/Rome")),
						new DateTime(al).withZone(DateTimeZone.forID("Europe/Rome")).plusDays(1)).getDays();
				costo = quantita.multiply(new BigDecimal(numGiorni));

			} else {
				throw new RuntimeException(
						"la tipologia intervento non è giornaliera, ne settimanale ne mensile. Tipologia intervento: "
								+ ti.getCodTipint() + " - " + ti.getDesTipint());
			}

		}

		return costo;
	}

	BigDecimal calcolaCostoIntervento(TipologiaIntervento ti, BigDecimal quantita, Date dal, Date al) {

		BigDecimal costo = quantita.multiply(ti.getImpStdCosto());

		if (is_quantita_mensile(ti) || is_intervento_per_durata(ti) && !is_quantita_settimanale(ti)) {
			Integer numMesi = org.joda.time.Months.monthsBetween(new DateTime(dal), new DateTime(al).plusDays(1))
					.getMonths();
			costo = costo.multiply(new BigDecimal(numMesi));

		} else if (is_quantita_settimanale(ti)) {
			Integer numSettimane = Weeks.weeksBetween(new DateTime(dal), new DateTime(al)).getWeeks();
			costo = costo.multiply(new BigDecimal(numSettimane));

		} else if (!is_intervento_per_durata(ti) && is_quantita_giornaliera(ti)) {
			Integer numGiorni = Days.daysBetween(new DateTime(dal).withZone(DateTimeZone.forID("Europe/Rome")),
					new DateTime(al).withZone(DateTimeZone.forID("Europe/Rome")).plusDays(1)).getDays();
			costo = costo.multiply(new BigDecimal(numGiorni));

		} else {
			throw new RuntimeException(
					"la tipologia intervento non è giornaliera, ne settimanale ne mensile. Tipologia intervento: "
							+ ti.getCodTipint() + " - " + ti.getDesTipint());
		}

		return costo;

	}

	boolean is_costo_forfettario(Tariffa tariffa) {
		return 'S' == tariffa.getForfait();
	}

	boolean is_intervento_per_durata(TipologiaIntervento ti) {
		return ti.getFlgFineDurata() == TipologiaIntervento.FLG_FINE_DURATA_D;
	}

	boolean is_quantita_settimanale(TipologiaIntervento ti) {
		return ti.getIdParamUniMis().getDesParam().contains("sett");
	}

	boolean is_quantita_mensile(TipologiaIntervento ti) {
		return ti.getIdParamUniMis().getDesParam().contains("mens");
	}

	boolean is_quantita_giornaliera(TipologiaIntervento ti) {
		return ti.getIdParamUniMis().getDesParam().contains("euro");
	}

}
