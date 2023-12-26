/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import com.google.common.base.*;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.persistence.dao.*;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.xsd.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.Objects;

/**
 *
 * @author giuseppe
 */
public class InterventoSerializer {

	public static final String PARAM_COD_BENEFICIARIO = "codBeneficiario";
	public static final String PARAM_COD_RICHIEDENTE = "codRichiedente";
	private static List<ParametriIndata> esiti;
	private final EntityManager em;
	private ParametriIndataDao pdao = null;

	public InterventoSerializer(EntityManager em) {
		this.em = em;
		this.pdao = new ParametriIndataDao(em);
		esiti = pdao.findByTipParam(Parametri.ESITO_INTERVENTO);
	}

	public static Function<PaiIntervento, Map> getinterventoMinifiedSerializer() {
		return new JsonMapTransformer<PaiIntervento>() {
			@Override
			public void transformToMap(PaiIntervento intervento) {

				InterventiAssociati interventoPadre = intervento.getInterventoPadre();

				if (interventoPadre != null) {
					put("tipoLegame", interventoPadre.getTipoLegame());
				}

				Preconditions.checkNotNull(intervento);

				put("desInt", intervento.getTipologiaIntervento().getDesTipint());
				put("dataApertura", intervento.getDtApe());

				put("dataChiusura", intervento.getDtChius());
				put("cntTipint", String.valueOf(intervento.getPaiInterventoPK().getCntTipint()));
				put("tipo", intervento.getPaiInterventoPK().getCodTipint());
				List<PaiInterventoMese> interventiMese = intervento.getPaiInterventoMeseList();
				BigDecimal importoPrevisto = BigDecimal.ZERO;
				BigDecimal importoErogato = BigDecimal.ZERO;

				for (PaiInterventoMese mese : interventiMese) {
					importoPrevisto = importoPrevisto.add(mese.getBdgPrevEur());
					if (mese.getBdgConsEur() != null) {
						importoErogato = importoErogato.add(mese.getBdgConsEur());
					}
				}

				put("importoPrevisto", String.valueOf(importoPrevisto));
				put("importoErogato", String.valueOf(importoErogato));
				put("dataEsecutivita", intervento.getDtEsec());
				put("codPai", String.valueOf(intervento.getPai().getCodPai()));
				put("codAna", String.valueOf(intervento.getPai().getAnagrafeSoc().getCodAna()));
				put("flgRicevuta", intervento.getTipologiaIntervento().getFlgRicevuta());
				put("flgFineDurata", String.valueOf(intervento.getTipologiaIntervento().getFlgFineDurata()));
				put("sottoposto", intervento.getDataRichiestaApprovazione() == null ? "no" : "si");
				put("statoAttuale", intervento.getStatoAttuale() == null ? "" : intervento.getStatoAttuale());
				put("richiedente", intervento.getDsCodAnaRich() == null ? null : intervento.getDsCodAnaRich().getCodAna());
				if (intervento.getTariffa() != null) {
					put("tariffa", intervento.getTariffa().getId());
					put("struttura", intervento.getTariffa().getStruttura().getId());
				} else if (interventoPadre != null) {
					if (interventoPadre.getInterventoPadre().getTariffa() != null) {
						put("struttura", interventoPadre.getInterventoPadre().getTariffa().getStruttura().getId());
					}
				}

				put("dataAvvio", intervento.getDtAvvio());
				put("previstaProroga", intervento.getDurMesiProroga() == null ? "No" : "Si");
				put("motivazione", Strings.nullToEmpty(intervento.getMotivazione()));

				put("statoIntervento", intervento.getStatoInt());
				put("cntTipintPadre",interventoPadre != null? interventoPadre.getInterventoPadre().getPaiInterventoPK().getCntTipint() : "");
			}
		};
	}

	public static Function<PaiIntervento, Map> getinterventoSocialCrtMinifiedSerializer() {
		return new JsonMapTransformer<PaiIntervento>() {
			@Override
			public void transformToMap(PaiIntervento intervento) {

				Preconditions.checkNotNull(intervento);
				put("desInt", intervento.getTipologiaIntervento().getDesTipint());
				put("dataApertura", intervento.getDtApe());

				put("dataChiusura", intervento.getDtChius());
				put("cntTipint", String.valueOf(intervento.getPaiInterventoPK().getCntTipint()));
				put("tipo", intervento.getPaiInterventoPK().getCodTipint());
				put("importoPrevisto", String.valueOf(intervento.getCostoPrev()));
				put("codPai", String.valueOf(intervento.getPai().getCodPai()));
				put("codAna", String.valueOf(intervento.getPai().getAnagrafeSoc().getCodAna()));
				put("flgFineDurata", String.valueOf(intervento.getTipologiaIntervento().getFlgFineDurata()));
				put("associazione", intervento.getAssociazione().getNome());
				put("dataAvvio", intervento.getDtAvvio());
				List<PaiEvento> pes = new PaiEventoDao(Connection.getEntityManager()).findByPaiIntervento(intervento,
						PaiEvento.PAI_APERTURA_INTERVENTO);
				if (pes.size() > 0 && pes.get(0).getCodUte() != null) {
					put("cognomeNomeOperatore", pes.get(0).getCodUte().getCognomeNome());

				}

				put("motivazione", Strings.nullToEmpty(intervento.getMotivazione()));

				put("statoIntervento", intervento.getStatoInt());
			}
		};
	}

	public static Function<PaiIntervento, Map> getinterventoSerializer() {

		return new JsonMapTransformer<PaiIntervento>() {
			@Override
			public void transformToMap(PaiIntervento paiIntervento) {

				put("classe",
						paiIntervento.getTipologiaIntervento().getIdParamClasseTipint().getIdParamIndata().toString());
				put("cntTipint", String.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint()));
				put("codAnag", String.valueOf(paiIntervento.getPai().getAnagrafeSoc().getCodAna()));
				if (paiIntervento.getDsCodAnaBenef() != null) {
					put("codBeneficiario", String.valueOf(paiIntervento.getDsCodAnaBenef().getCodAna()));
					String denominazioneBeneficiario = "DATO NON DISPONIBILE";
					if (paiIntervento.getDsCodAnaBenef().getFlgPersFg().equals("G")) {
						denominazioneBeneficiario = paiIntervento.getDsCodAnaBenef().getRagSoc();
					} else {
						denominazioneBeneficiario = paiIntervento.getDsCodAnaBenef().getNome() + " "
								+ paiIntervento.getDsCodAnaBenef().getCognome();
					}

					put("desBeneficiario", denominazioneBeneficiario);
				}
				if (paiIntervento.getDsCodAnaRich() != null) {
					put("codRichiedente", paiIntervento.getDsCodAnaRich().getCodAna());
				}
				put("dataApertura", StringConversion.dateToItString(paiIntervento.getDtApe()));
				if (paiIntervento.getDtChius() != null) {
					put("dataChiusura", StringConversion.dateToItString(paiIntervento.getDtChius()));
				}
				if (paiIntervento.getDtFine() != null) {
					put("dataFine", StringConversion.dateToItString(paiIntervento.getDtFine()));
				}
				if (paiIntervento.shouldUseSettimane()) {
					put("numeroSettimane", paiIntervento.getDurSettimane());
				}
				if (paiIntervento.getDataFineIndicativa() != null) {
					put("dataFineIndicativa", StringConversion.dateToItString(paiIntervento.getDataFineIndicativa()));
				}
				if (paiIntervento.getDtEsec() != null) {
					put("dataEsecutivita", StringConversion.dateToItString(paiIntervento.getDtEsec()));
				}
				if (paiIntervento.getDurMesi() != null) {
					put("durataMesi", String.valueOf(paiIntervento.getDurMesi()));
				}
				if (paiIntervento.getNumDetermina() != null) {
					put("numeroDetermina", paiIntervento.getNumDetermina());
				}
				if (paiIntervento.getProtocollo() != null) {
					put("protocollo", paiIntervento.getProtocollo());
				}
				if (paiIntervento.getIndEsitoInt() != null) {
					for (ParametriIndata es : esiti) {
						if (es.getIdParam().getCodParam().equals(paiIntervento.getIndEsitoInt())) {
							put("esito", es.getDesParam());

						}
					}
				}
				put("quantita", String.valueOf(paiIntervento.getQuantita()));
				put("codPai", String.valueOf(paiIntervento.getPaiInterventoPK().getCodPai()));
				put("cntTipint", String.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint()));
				put("cntTipIntHidden", String.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint()));
				put("tipo", paiIntervento.getPaiInterventoPK().getCodTipint());
				put("codTipInt", paiIntervento.getPaiInterventoPK().getCodTipint());
				put("codTipIntHidden", paiIntervento.getPaiInterventoPK().getCodTipint());
				put("stato", String.valueOf(paiIntervento.getStatoInt()));

				put("dataSospensioneRead",
						paiIntervento.getDtSosp() != null ? StringConversion.dateToItString(paiIntervento.getDtSosp())
								: "");

				put("noteSospensioneRead", Strings.nullToEmpty(paiIntervento.getNoteSospensione()));
				put("impStdCosto", paiIntervento.getTipologiaIntervento().getImpStdCosto().toString());
				put("dataAvvio", StringConversion.dateToItString(paiIntervento.getDtAvvio()));
				put("dataAvvioProposta", StringConversion.dateToItString(paiIntervento.getDataAvvioProposta()));
				if (paiIntervento.getIdParamUniMis() != null) {
					put("labelQuantita", paiIntervento.getLabelQuantita());
				} else {
					put("labelQuantita", "Quantità");
				}
				if (!Strings.isNullOrEmpty(paiIntervento.getIbanDelegatoObenef())) {
					put("IBAN", paiIntervento.getIbanDelegatoObenef());
				}
				put("motivazione", Strings.nullToEmpty(paiIntervento.getMotivazione()));
				put("noteChiusura", Strings.nullToEmpty(paiIntervento.getNoteChius()));
				put("datiOriginali", Strings.nullToEmpty(paiIntervento.getDatiOriginali()));

				put("flgFineDurata", String.valueOf(paiIntervento.getTipologiaIntervento().getFlgFineDurata()));
				put("urgente", Character.toString(paiIntervento.getUrgente()));
				put("approvazioneTecnica", Character.toString(paiIntervento.getApprovazioneTecnica()));

				if (paiIntervento.getTariffa() != null) {
					put("struttura", paiIntervento.getTariffa().getStruttura().getId());
					put("tariffa", paiIntervento.getTariffa().getId());
				} else if (paiIntervento.getInterventoPadre() != null) {
					if (paiIntervento.getInterventoPadre().getInterventoPadre().getTariffa() != null) {
						put("struttura", paiIntervento.getInterventoPadre().getInterventoPadre().getTariffa()
								.getStruttura().getId());
					}
				}

				// I dati dei famigliari li suddivido per tipo:
				// 1 - intervento padre
				// 0 - intervento associato ad un famigliare (intervento figlio del papa)
				// -1 - interventi figlio
				ArrayList<HashMap<String, Object>> famigliari = new ArrayList<HashMap<String, Object>>();
				if (paiIntervento.getInterventoPadre() != null) {
					HashMap<String, Object> famigliare = new HashMap<String, Object>();
					famigliare.put("codAnag", paiIntervento.getInterventoPadre().getInterventoPadre().getPai()
							.getAnagrafeSoc().getCodAna());
					famigliare.put("importoPrevisto",
							paiIntervento.getInterventoPadre().getInterventoPadre().getQuantita());
					famigliare.put("tipo", "1");
					famigliari.add(famigliare);
					if (paiIntervento.getInterventoPadre().getInterventoPadre().getInterventiFigli() != null) {
						// aggiungo anche i figli del papà
						List<InterventiAssociati> figli = paiIntervento.getInterventoPadre().getInterventoPadre()
								.getInterventiFigli();
						for (InterventiAssociati figlio : figli) {
							famigliare = new HashMap<String, Object>();
							famigliare.put("codAnag",
									figlio.getInterventoFiglio().getPai().getAnagrafeSoc().getCodAna());
							famigliare.put("importoPrevisto", figlio.getInterventoFiglio().getQuantita());
							famigliare.put("tipo", "0");
							famigliari.add(famigliare);
						} 
					} 
				}
				if (paiIntervento.getInterventiFigli() != null) {
					List<InterventiAssociati> figli = paiIntervento.getInterventiFigli();
					for (InterventiAssociati figlio : figli) {
						HashMap<String, Object> famigliare = new HashMap<String, Object>();
						famigliare.put("codAnag", figlio.getInterventoFiglio().getPai().getAnagrafeSoc().getCodAna());
						famigliare.put("importoPrevisto", figlio.getInterventoFiglio().getQuantita());
						famigliare.put("tipo", "-1");
						famigliari.add(famigliare);
					}
				}
				if (famigliari.size() > 0) {
					put("famigliari", famigliari.toArray());
				}
			}
		};
	}
	
    public static Predicate<PaiIntervento> findByCodPaiCodTipintPredicate() {
        return p -> !p.isChiuso();
    }

	public PaiIntervento serializeIntervento(Map<String, String> parameters) throws Exception {
		PaiInterventoDao dao = new PaiInterventoDao(em);
		PaiIntervento paiIntervento;
		PaiInterventoPK key = new PaiInterventoPK();

		String codTipint = parameters.get("tipo");
		int codPai = Integer.valueOf(parameters.get("codPai"));
		String cntTipintString = Strings.emptyToNull(parameters.get("cntTipint"));
		String tipOld = Strings.emptyToNull(parameters.get("codTipIntHidden"));
		if (cntTipintString != null && codTipint.equals(tipOld)) {
			paiIntervento = dao.findByKey(codPai, codTipint, Integer.valueOf(cntTipintString));
			if (paiIntervento == null) {
				paiIntervento = new PaiIntervento();
				key.setCodTipint(codTipint);
				key.setCodPai(codPai);
				key.setCntTipint(Integer.valueOf(cntTipintString));
				paiIntervento.setPaiInterventoPK(key);
			}
		} else {
			Preconditions.checkArgument(!Iterables.any(dao.findByCodPaiCodTipint(codPai, codTipint), findByCodPaiCodTipintPredicate()),
					"Attenzione! Esiste già un intervento non chiuso per questa tipologia (%s)", codTipint);
			paiIntervento = new PaiIntervento();
			key.setCodTipint(codTipint);
			key.setCodPai(codPai);
			key.setCntTipint(dao.findMaxCnt(codPai, codTipint).intValue() + 1);
			paiIntervento.setPaiInterventoPK(key);
			paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_APERTO);
			paiIntervento.setStatoAttuale(PaiIntervento.APERTO);
		}

		String dataApertura = parameters.get("dataApertura");
		if (dataApertura != null && !"".equals(dataApertura)) {
			Date dtape = StringConversion.itStringToDate(dataApertura);
			paiIntervento.setDtApe(dtape);
		} else {
			paiIntervento.setDtApe(new Date());
		}
		String dataAvvioProposta = parameters.get("dataAvvioProposta");
		Preconditions.checkNotNull(Strings.emptyToNull(dataAvvioProposta),
				"Attenzione! Specifica una data di partenza intervento");
		if (!StringUtils.isBlank(dataAvvioProposta)) {
			paiIntervento.setDtAvvio(StringConversion.itStringToDate(dataAvvioProposta));
			// segno anche la data di avvio prevista
			paiIntervento.setDataAvvioProposta(StringConversion.itStringToDate(dataAvvioProposta));
		}
		String dataAvvio = parameters.get("dataAvvio");
		if (!StringUtils.isBlank(dataAvvio)) {
			paiIntervento.setDtAvvio(StringConversion.itStringToDate(dataAvvio));

		}

		String esito = parameters.get("esito");
		paiIntervento.setIndEsitoInt(esito);
		String dataChiusura = parameters.get("dataChiusura");
		if (dataChiusura != null && !"".equals(dataChiusura)) {
			Date dtchius = StringConversion.itStringToDate(dataChiusura);
			paiIntervento.setDtChius(dtchius);
		}
		String dataEsecutivita = parameters.get("dataEsecutivita");
		if (dataEsecutivita != null && !"".equals(dataEsecutivita)) {
			Date dtexec = StringConversion.itStringToDate(dataEsecutivita);
			paiIntervento.setDtEsec(dtexec);
		}
		String noteChiusura = parameters.get("noteChiusura");
		paiIntervento.setNoteChius(noteChiusura);
		String quantita = parameters.get("quantita");
		if (quantita != null && !"".equals(quantita)) {
			paiIntervento.setQuantita(new BigDecimal(quantita.replace(",", ".")));
		} else {
			paiIntervento.setQuantita(BigDecimal.ZERO);
		}

		String durataMesi = parameters.get("durataMesi");
		paiIntervento.setDurMesi(StringUtils.isBlank(durataMesi) ? null : Integer.valueOf(durataMesi));

		String numeroDetermina = parameters.get("numeroDetermina");
		paiIntervento.setNumDetermina(StringUtils.isBlank(numeroDetermina) ? null : numeroDetermina);

		String protocollo = parameters.get("protocollo");
		paiIntervento.setProtocollo(StringUtils.isBlank(protocollo) ? null : protocollo);

		String dataFineStr = parameters.get("dataFine");
		paiIntervento.setDtFine(StringUtils.isBlank(dataFineStr) ? null : StringConversion.itStringToDate(dataFineStr));

		String dataFineIndicativaStr = parameters.get("dataFineIndicativa");
		paiIntervento.setDataFineIndicativa(StringUtils.isBlank(dataFineIndicativaStr) ? null
				: StringConversion.itStringToDate(dataFineIndicativaStr));

		String tariffa = parameters.get("tariffa");
		if (!StringUtils.isBlank(tariffa) && tariffa != null) {
			Integer idTariffa = Integer.valueOf(tariffa);
			TariffaDao tariffaDao = new TariffaDao(em);
			paiIntervento.setTariffa(tariffaDao.findSafe(Tariffa.class, idTariffa));
		} 

		// check se è pid e quindi vuole durata settimanale
		if (paiIntervento.getDtFine() != null) {
			Date dataAvvioIntervento = paiIntervento.getDtAvvio();
			Date dataFine = paiIntervento.getDtFine();
			int weeks = Weeks.weeksBetween(new LocalDate(dataAvvioIntervento), new LocalDate(dataFine)).getWeeks();
			paiIntervento.setDurSettimane(weeks);
		}
		if (paiIntervento.getDurMesi() == null && paiIntervento.getDtAvvio() != null
				&& paiIntervento.getDtFine() != null) {
			// nota: la durata in questo caso e' in giorni, non in mesi . . . andra' gestito
			// piu' avanti . .
			paiIntervento.setDurMesi(Months
					.monthsBetween(new LocalDate(paiIntervento.getDtAvvio()), new LocalDate(paiIntervento.getDtFine()))
					.getMonths());

		}


		String codBeneficiario = parameters.get(PARAM_COD_BENEFICIARIO);
		if (!Strings.isNullOrEmpty(codBeneficiario)) {
			AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
			AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codBeneficiario));
			Validate.notNull(anagrafe, "unable to find beneficiario for key " + codBeneficiario);
			paiIntervento.setDsCodAnaBenef(anagrafe);
		} else {
			paiIntervento.setDsCodAnaBenef(null);
		}

		String codRichiedente = parameters.get(PARAM_COD_RICHIEDENTE);
		if (!Strings.isNullOrEmpty(codRichiedente) && !codRichiedente.equals("Il servizio")) {
			AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
			AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codRichiedente));
			Validate.notNull(anagrafe, "unable to find richiedente for key " + codRichiedente);
			paiIntervento.setDsCodAnaRich(anagrafe);
		} else {
			paiIntervento.setDsCodAnaRich(null);
		}

		String urgente = parameters.get("urgente");
		if (!Strings.isNullOrEmpty(urgente)) {
			paiIntervento.setUrgente('S');
		} else {
			paiIntervento.setUrgente('N');
		}

		String validato = parameters.get("approvazioneTecnica");
		if (!Strings.isNullOrEmpty(validato)) {
			paiIntervento.setApprovazioneTecnica('S');
		} else {
			paiIntervento.setApprovazioneTecnica('N');
		}

		if (paiIntervento.isInterventoAccesso()) {
			paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
			paiIntervento.setDtChius(paiIntervento.getDtApe());
		}
		// Salvo i dati originali proposti dall'assistente sociale e lo faccio solo
		// quando l'intervento viene inserito per la prima volta
		if (paiIntervento.getDatiOriginali() == null) {
			String datiOriginali = "Dati originali proposti dall'assistente sociale:\n" + "Data avvio:"
					+ Utils.dateToItString(paiIntervento.getDataAvvioProposta()) + "\n" + "Quantità:"
					+ paiIntervento.getQuantita() + " "
					+ new TipologiaInterventoDao(em).findByCodTipint(paiIntervento.getPaiInterventoPK().getCodTipint())
							.getIdParamUniMis().getDesParam()
					+ "\n" + (paiIntervento.getDtFine() == null ? "Durata:" + paiIntervento.getDurMesi() + "mesi"
							: "Data di fine intervento:" + Utils.dateToItString(paiIntervento.getDtFine()));
			paiIntervento.setDatiOriginali(datiOriginali);

		}

		paiIntervento.setMotivazione(MoreObjects.firstNonNull(parameters.get("motivazione"),
				Strings.nullToEmpty(paiIntervento.getMotivazione())));

		return paiIntervento;
	}

	public List<MapDatiSpecificiIntervento> serializeMapDatiSpecifici(PaiIntervento intervento, Map<String, String> parameters) {
		List<MapDatiSpecificiIntervento> datiSpecifici = new ArrayList<MapDatiSpecificiIntervento>();
		DatiSpecificiDao dsDao = new DatiSpecificiDao(em);
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			// Suppongo che i dati specifici inizino tutti con ds_
			if (key.startsWith("ds_") && !isHiddenComboValue(key) && !Strings.isNullOrEmpty(value)
					&& !value.equals(" ")) {
				DatiSpecifici ds = MoreObjects.firstNonNull(dsDao.findByCodCampo(key),
						dsDao.findByCodCampo(key.replaceFirst("^ds_", "")));
				MapDatiSpecificiIntervento map = new MapDatiSpecificiIntervento();
				MapDatiSpecificiInterventoPK pk = new MapDatiSpecificiInterventoPK();
				if (isCombo(ds)) {
					String hiddenvalue = parameters.get(key + "_hidden");

					map.setValCampo(hiddenvalue);
					map.setCodValCampo(value);
				} else {
					if (isNum(ds)) {
						value = value.replace(',', '.');
					}
					map.setValCampo(value);
				}
				pk.setCntTipint(intervento.getPaiInterventoPK().getCntTipint());
				pk.setCodPai(intervento.getPaiInterventoPK().getCodPai());
				pk.setCodTipint(intervento.getPaiInterventoPK().getCodTipint());
				pk.setCodCampo(ds.getCodCampo());
				map.setMapDatiSpecificiInterventoPK(pk);
				datiSpecifici.add(map);
			}
		}
		return datiSpecifici;
	}


	private boolean isCombo(DatiSpecifici ds) {
		return Objects.equals(ds.getTipoCampo(), DatiSpecifici.DS_COMBO);
	}

	private boolean isNum(DatiSpecifici ds) {
		return Objects.equals(ds.getTipoCampo(), DatiSpecifici.DS_NUM);
	}

	private boolean isHiddenComboValue(String key) {
		return key.contains("_hidden");
	}
}
