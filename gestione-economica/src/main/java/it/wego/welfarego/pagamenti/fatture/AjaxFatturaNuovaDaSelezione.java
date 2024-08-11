package it.wego.welfarego.pagamenti.fatture;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.welfarego.pagamenti.AbstractAjaxController;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiCdg;
import it.wego.welfarego.persistence.entities.PaiCdgPK;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoMesePK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller per le chiamate AJAX riguardanti la gestione della sezione della
 * creazione delle nuove fatture selezionendo i vari dati direttamente dalla
 * base di dati popolata precedentemente dall'utente nella sezione delle
 * acquisizioni.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class AjaxFatturaNuovaDaSelezione extends AbstractAjaxController {

	/**
	 * Cerca nella base di dati tutte le quantità allegate ad un intervento
	 * ({@link PaiInterventoMese}) per creare le voci della fattura. Come chiave di
	 * ricerca si utilizzerà i dati che formano la chiave di
	 * {@link PaiInterventoMese}.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiInterventoMese}).
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "cercaVociFattura")
	public void cercaVociFattura(ResourceRequest request, ResourceResponse response) throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
//        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());
//        try {
		final JSONObject jsoIdPaiIntervento = new JSONObject(request.getParameter("id"));
		JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));
		List<PaiInterventoMese> interventi = cercaPaiInterventoMese(jsoIdPaiIntervento, jsoPeriodoRicerca,
				getEntityManager(), request);
		// JSONArray jsa = new JSONArray();
		// String id = null;
		Iterable<Map> data = Iterables.transform(Multimaps.index(interventi, new Function<PaiInterventoMese, String>() {
			public String apply(PaiInterventoMese paiInterventoMese) {
				return creaIdPaiInterventoMese(paiInterventoMese.getPaiInterventoMesePK());
			}
		}).asMap().entrySet(), new JsonMapTransformer<Map.Entry<String, Collection<PaiInterventoMese>>>() {
			@Override
			public void transformToMap(Map.Entry<String, Collection<PaiInterventoMese>> entry) {
				Iterable<PaiInterventoMese> paiInterventoMeseList = entry.getValue();
				PaiInterventoMese paiInterventoMeseCampione = paiInterventoMeseList.iterator().next();
				put("id", entry.getKey());
				put("id_fattura", jsoIdPaiIntervento.toString());
				put("tipo_servizio",
						paiInterventoMeseCampione.getPaiIntervento().getTipologiaIntervento().getDesTipint());
				put("unita_di_misura", paiInterventoMeseCampione.getPaiIntervento().getIdParamUniMis().getDesParam());

				ParametriIndata pi = paiInterventoMeseCampione.getIdParamFascia();
				BigDecimal percentualeRiduzione = null;
				if (pi != null) {
					percentualeRiduzione = paiInterventoMeseCampione.getIdParamFascia().getDecimalParam();
					put("idFascia", paiInterventoMeseCampione.getIdParamFascia().getIdParamIndata().toString());
					put("percentualeRiduzione", percentualeRiduzione.toString());
					put("descrizioneRiduzione", paiInterventoMeseCampione.getIdParamFascia().getDesParam());
				} else {
					percentualeRiduzione = new BigDecimal(BigInteger.ZERO);
					put("idFascia", null);
					put("percentualeRiduzione", "0");
					put("descrizioneRiduzione", "-");
				}
				percentualeRiduzione = (new BigDecimal("100").subtract(percentualeRiduzione))
						.divide(new BigDecimal("100"));

				BigDecimal quantita = BigDecimal.ZERO, importoSenzaIva = BigDecimal.ZERO,
						importoScontato = BigDecimal.ZERO, variazione = BigDecimal.ZERO, contributo = BigDecimal.ZERO;
				for (PaiInterventoMese paiInterventoMese : paiInterventoMeseList) {
					BigDecimal costoStandard = getImportoStandard(paiInterventoMese.getPaiIntervento(),
							getEntityManager());
					BigDecimal importoStandardScontato = costoStandard.multiply(percentualeRiduzione).setScale(2,
							RoundingMode.DOWN);
					if (paiInterventoMese.getBdgConsQta() != null) {
						quantita = quantita.add(paiInterventoMese.getBdgConsQta());
					}

					importoSenzaIva = importoSenzaIva.add(paiInterventoMese.getBdgConsEur());
					importoScontato = importoScontato.add(paiInterventoMese.getBdgConsQta()
							.multiply(importoStandardScontato).setScale(2, RoundingMode.HALF_UP));
					if (paiInterventoMese.getBdgConsVar() != null) {
						variazione = variazione.add(paiInterventoMese.getBdgConsVar());
					}

				}
				put("quantita", quantita.toString());
				put("importo_dovuto", importoSenzaIva.toString());
				put("importo_fascia", importoScontato.toString());
				put("variazione_straordinaria", variazione.toString());
				put("importo_unitario", paiInterventoMeseCampione.getPaiIntervento().getTipologiaIntervento()
						.getImpStdCosto().toString());
				put("mese", String.valueOf(paiInterventoMeseCampione.getPaiInterventoMesePK().getMeseEff()));
				put("importoSenzaIva", importoScontato.toString());
				ParametriIndata ipAliquotaIva = paiInterventoMeseCampione.getPaiIntervento().getTipologiaIntervento()
						.getIpAliquotaIva();

				BigDecimal ivaTotale = importoScontato.multiply(ipAliquotaIva.getDecimalPercentageParamAsDecimal()),
						importoConIva = importoScontato.add(ivaTotale);
				put("importoConIva", importoConIva.toString());
				put("ivaTotale", ivaTotale.toString());
				put("aliquotaIva", ipAliquotaIva.getDecimalPercentageParamAsDecimal().toString());
				put("totaleVariazioniConIva",
						variazione.multiply(ipAliquotaIva.getDecimalPercentageParamAsDecimal()).toString());
				put("contributoRiga", contributo.toString());

			}
		});
	}// cercaVociFattura

	/**
	 * Salva o più correttamente aggiorna i dettagli di una nuova fattura.
	 *
	 * @param request  Richiesta del browser contenente le chiavi di ricerca per
	 *                 {@link PaiInterventoMese}.
	 * @param response Risposta al browser contenente la copia dei dati trasmessi
	 *                 per confermare il corretto salvataggio dei dati.
	 * @param session  Sessione dalla quale recuperare l'iddentificativo dell'
	 *                 utente connesso.
	 * @throws IOException    In caso di problemi durante la scrittura del mesaggio
	 *                        di risposta.
	 * @throws JSONException  In caso di problemi durante la formazione della
	 *                        risposta in formato JSON (non previsto)
	 * @throws ParseException In caso una delle date presenti non sia nel formato
	 *                        italiano.
	 */
	@ResourceMapping(value = "salvaVociFattura")
	@Transactional
	public void salvaVociFattura(ResourceRequest request, ResourceResponse response, PortletSession session)
			throws IOException, JSONException, ParseException, JAXBException, Exception {
		JSONArray dati = new JSONArray(request.getParameter("data"));
		JSONArray jsa = new JSONArray();

		Query q = getEntityManager().createNamedQuery("Utenti.findByUsername");
		q.setParameter("username",
				UserLocalServiceUtil.getUserById(Long.parseLong(request.getRemoteUser())).getScreenName());
		Utenti utente = (Utenti) q.getSingleResult();
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery();
		Root r = c.from(PaiInterventoMese.class);
		for (int i = 0; i < dati.length(); i++) {
			double aumentoOrig = 0, riduzioneOrig = 0;
			JSONObject jsoInterventoMese = (JSONObject) dati.get(i);
			JSONObject jsoInterventoMeseId = new JSONObject((String) ((JSONObject) dati.get(i)).get("id"));
			Predicate p = cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoInterventoMeseId.get("codPai"));
			p = cb.and(p,
					cb.equal(r.get("paiInterventoMesePK").get("codTipint"), jsoInterventoMeseId.get("codTipint")));
			p = cb.and(p,
					cb.equal(r.get("paiInterventoMesePK").get("cntTipint"), jsoInterventoMeseId.get("cntTipint")));
			p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoInterventoMeseId.get("annoEff")));
			p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoInterventoMeseId.get("meseEff")));
			c.where(p);
			// prendo il primo, tanto se ce ne sono due, dovrebbero puntare allo stesso
			// dettaglio
			List<PaiInterventoMese> interventiMese = getEntityManager().createQuery(c).getResultList();
			Preconditions.checkArgument(!interventiMese.isEmpty(), "interventiMese not found");
			PaiInterventoMese interventoMese = interventiMese.get(0);
			Preconditions.checkNotNull(interventoMese.getIdFattDettaglio(), "fatturaDettaglio is null");

			// correzione dati fascia su riga
			BigDecimal fascia = BigDecimal.ZERO;
			if (interventoMese.getIdParamFascia() != null) {
				fascia = interventoMese.getIdParamFascia().getDecimalParam();
			}
			fascia = (new BigDecimal("100").subtract(fascia)).divide(new BigDecimal("100"));

			if (jsoInterventoMese.has("aumento") && !JSONObject.NULL.equals(jsoInterventoMese.get("aumento"))) {
				aumentoOrig = interventoMese.getIdFattDettaglio().getAumento() == null ? 0
						: interventoMese.getIdFattDettaglio().getAumento().doubleValue();
				interventoMese.getIdFattDettaglio().setAumento(
						(new BigDecimal(((Number) jsoInterventoMese.get("aumento")).doubleValue())).multiply(fascia));
			} // if
			if (jsoInterventoMese.has("riduzione") && !JSONObject.NULL.equals(jsoInterventoMese.get("riduzione"))) {
				riduzioneOrig = interventoMese.getIdFattDettaglio().getRiduzione() == null ? 0
						: interventoMese.getIdFattDettaglio().getRiduzione().doubleValue();
				interventoMese.getIdFattDettaglio().setRiduzione(
						new BigDecimal(((Number) jsoInterventoMese.get("riduzione")).doubleValue()).multiply(fascia));
			} // if
//                entityManager.merge(interventoMese.getIdFattDettaglio()); should be unnecessary to merge

			// Aggiorno il CDG
			double totale = 0;
			for (PaiInterventoMese intervento : interventiMese) {
				totale += intervento.getBdgPrevQta().doubleValue();
			} // for
			for (PaiInterventoMese paiInterventoMese : interventiMese) {
				double proporzione = paiInterventoMese.getBdgPrevQta().doubleValue() / totale;
				PaiCdg cdg = paiInterventoMese.getPaiCdg();
				double modificaAumento = aumentoOrig - (interventoMese.getIdFattDettaglio().getAumento() == null ? 0
						: interventoMese.getIdFattDettaglio().getAumento().doubleValue());
				double modificaRiduzione = riduzioneOrig
						- (interventoMese.getIdFattDettaglio().getRiduzione() == null ? 0
								: interventoMese.getIdFattDettaglio().getRiduzione().doubleValue());
				cdg.setImpComplUsingFascia(new BigDecimal(
						(cdg.getImpCompl().doubleValue() - modificaAumento + modificaRiduzione) * proporzione));
				getEntityManager().persist(cdg);

				PaiEvento evento = Pratica.serializePaiEvento(
						paiInterventoMese.getPaiIntervento().getPai().getAnagrafeSoc(),
						paiInterventoMese.getPaiIntervento().getPai(), paiInterventoMese.getPaiIntervento(),
						"Inserimento Pai intervento mese in fattura", utente);
				getEntityManager().persist(evento);

			} // for
			jsa.put(jsoInterventoMese);
		} // for
		JsonBuilder.newInstance().withWriter(response.getWriter()).withData(jsa).buildResponse();
	}// salvaVociFattura

	/**
	 * Cerca nella base di dati tutte le eventuali persone civilmente obbligate a
	 * pagare la fattura che si sta realizzando.Come chiave di ricerca si utilizzerà
	 * l'id del {@link Pai}.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiIntervento}).
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "cercaQuoteObbligatiFatturazioni")
	public void cercaQuoteObbligatiFatturazioni(ResourceRequest request, ResourceResponse response) throws Exception {
//        EntityManager entityManager = Connection.getEntityManager();
		List<PaiIntervento> interventi = this.cercaPaiIntervento(request.getParameter("id"), getEntityManager());
		JSONArray jsa = new JSONArray();
		Map<String, JSONObject> civObbByCf = Maps.newLinkedHashMap();
		for (PaiIntervento intervento : interventi) {
			for (PaiInterventoCivObb quota : intervento.getPaiInterventoCivObbList()) {
				String cf = quota.getAnagrafeSoc().getCodFisc();
				if (!civObbByCf.containsKey(cf)) {
					JSONObject jso = new JSONObject();
					jso.put("id", quota.getAnagrafeSoc().getCodFisc());
					jso.put("codice_fiscale", cf);
					jso.put("importo", quota.getImpCo());
					jso.put("id_fattura", request.getParameter("id"));
					jsa.put(jso);
				} // if
				else {
					JSONObject jso = civObbByCf.get(cf);
					jso.put("importo", new BigDecimal(jso.get("importo").toString()).add(quota.getImpCo()));
				} // else
			} // for
		} // for
		this.mandaRisposta(response, jsa, null);
	}// cercaQuoteObbligatiFatturazioni

	/**
	 * Salva nella fattura le quote delle persone civilmente obbligate (in maniera
	 * denormalizzata direttamente nella fattura) eventualmente modificate
	 * dall'utente rispetto a quelle già presente nella base di dati.
	 *
	 * @param request  Richiesta del browser contenente la chiave del
	 *                 {@link PaiIntervento} e il Codice fiscale della persona.
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "salvaQuoteObbligatiFatturazioni")
	@Transactional
	public void salvaQuoteObbligatiFatturazioni(ResourceRequest request, ResourceResponse response)
			throws IOException, JSONException {
//        EntityManager entityManager = Connection.getEntityManager();
		JSONArray jsaDati = new JSONArray(request.getParameter("data"));
		// presuppongo almeno una riga altrimenti la tabell non esegue la
		// chiamata AJAX di sincronizzazione.
		Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
		q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")));
		Fattura f = (Fattura) q.getSingleResult();
		for (int i = 0; i < jsaDati.length(); i++) {
			JSONObject quota = (JSONObject) jsaDati.get(i);
			if (quota.get("codice_fiscale").equals(f.getCfObbligato1())) {
				f.setQuotaObbligato1(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
			} // if
			else if (quota.get("codice_fiscale").equals(f.getCfObbligato2())) {
				f.setQuotaObbligato2(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
			} // else if
			else if (quota.get("codice_fiscale").equals(f.getCfObbligato3())) {
				f.setQuotaObbligato3(new BigDecimal(((Number) quota.get("importo")).doubleValue()));
			} // else if
		} // for
		getEntityManager().merge(f);
		this.mandaRisposta(response, jsaDati, null);
	}// salvaQuoteObbligatiFatturazioni

	/**
	 * Cerca i dati della nuova fattura. Il calcolo dei totali viene delegato
	 * all'interfaccia utente. Come chiave di ricerca si utilizzerà i dati che
	 * formano la chiave di {@link PaiIntervento}.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiIntervento}).
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "cercaDatiFatturazioni")
	public void cercaDatiFatturazioni(ResourceRequest request, ResourceResponse response) throws Exception {
		// i valori null non vengono trasmessi (inseriti comunque per chiarezza e per
		// facilitare eventuali modifiche future)
		PaiIntervento intervento = this.cercaPaiIntervento(request.getParameter("id"), getEntityManager()).get(0);
		final JSONObject jsoId = new JSONObject((String) request.getParameter("id"));
		JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoId.get("periodoRicerca"));
		// calcolo il contributo
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery();
		Root r = c.from(PaiInterventoMese.class);
		Join pi = r.join("paiIntervento", JoinType.INNER);
		Join pai = pi.join("pai", JoinType.INNER);
		Join ti = pi.join("tipologiaIntervento", JoinType.INNER);
		Join cs = pai.join("codAna", JoinType.INNER);
		Join a = cs.join("anagrafeSoc", JoinType.INNER);
		Join fad = r.join("idFattDettaglio", JoinType.LEFT);
		Join fa = fad.join("idFatt", JoinType.LEFT);
		Join pinfs = r.join("idParamFascia", JoinType.LEFT);
		Predicate p = cb.equal(ti.get("flgFatt"), "S");
		p = cb.and(p, pi.get("statoInt").in("C", "E"));
		p = cb.and(p, pi.get("dtEsec").isNotNull());

		p = cb.and(p, cb.equal(a.get("codAna"), intervento.getPai().getAnagrafeSoc().getCodAna()));
		p = cb.and(p, cb.notEqual(r.get("bdgConsQta"), cb.nullLiteral(BigDecimal.class)));

		p = cb.and(p, cb.equal(fad.get("idFattDettaglio"), cb.nullLiteral(FatturaDettaglio.class)));
		p = cb.and(p, cb.or(pinfs.get("decimalParam").isNull(), cb.lessThan(pinfs.get("decimalParam"), 100)));

		if (jsoPeriodoRicerca.has("meseEff")) {
			Predicate mese = cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoPeriodoRicerca.get("meseEff"));
			Predicate anno = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoPeriodoRicerca.get("annoEff"));
			p = cb.and(p, mese, anno);
		} // if
		else {
			// se non era specificato il mese sicuramete deve essere specificato il periodo
			// (verificare il metodo precedente)
			Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"),
					(Integer) jsoPeriodoRicerca.get("meseEffDal"));
			Predicate annoDal = cb.ge(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffDal"));
			Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"),
					(Integer) jsoPeriodoRicerca.get("meseEffAl"));
			Predicate annoAl = cb.le(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffAl"));
			p = cb.and(p, meseDal, annoDal, meseAl, annoAl);
		} // else
		c.where(p);
		final List<PaiInterventoMese> interventiMese = getEntityManager().createQuery(c).getResultList();
		JsonBuilder.newInstance().withWriter(response.getWriter()).withData(Collections.singletonList(intervento))
				.withTransformer(new JsonMapTransformer<PaiIntervento>() {
					@Override
					public void transformToMap(PaiIntervento paiIntervento) {

						BigDecimal contributo = BigDecimal.ZERO;

						// 17/08/2017 Modificato modo per reperite l'aliquota IVA. Il modo originela è
						// palesement errato.
						for (PaiInterventoMese intMese : interventiMese) {
							if (intMese.getPaiIntervento().getTipologiaIntervento().getIpAliquotaIva() != null) {
								put("iva", intMese.getPaiIntervento().getTipologiaIntervento().getIpAliquotaIva()
										.getIdParamIndata());
								break;
							}
						}
						put("bollo", 0d);
						if (paiIntervento.isDsPrimoEventoVita()) {
							put("note",
									"in quanto primo intervento vita, la prima settimana non deve essere fatturata");
						} else {
//                    jso.put("note", JSONObject.NULL);
						}
						put("scadenza", DateTimeFormat.forPattern("dd/MM/yyyy")
								.print(DateTime.now().monthOfYear().addToCopy(1)));
						put("codice_fiscale", paiIntervento.getPai().getAnagrafeSoc().getCodFisc().toUpperCase());

						put("id", jsoId.toString());
					}
				}).buildStoreResponse();
	}// cercaDatiFatturazioni

	/**
	 * Cerca i dati delle fatture dei mesi precedent. Cerca tutte le fatture
	 * collegate allo stesso {@link PaiIntervento} che sono state generate ma non
	 * ancora emesse.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiIntervento}).
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "cercaMesiPrecedentiFatturazioni")
	public void cercaMesiPrecedentiFatturazioni(ResourceRequest request, ResourceResponse response)
			throws IOException, JSONException {
		JSONObject jsoId = new JSONObject(request.getParameter("id"));
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery();
		Root r = c.from(Fattura.class);
		Predicate p = cb.equal(r.get("idParamStato").get("idParam").get("codParam"), "de");
		p = cb.and(p, cb.equal(r.get("paiIntervento").get("paiInterventoPK").get("codPai"), jsoId.get("codPai")));
		c.where(p);
		List<Fattura> fatture = getEntityManager().createQuery(c).getResultList();
		JSONArray jsa = new JSONArray();
		for (Fattura fattura : fatture) {
			JSONObject jso = new JSONObject();
			jso.put("id", fattura.getIdFatt());
			jso.put("id_fattura", request.getParameter("id"));
			jso.put("mese", fattura.getMeseRif());
			jso.put("importo", fattura.getImportoTotale());
			jso.put("causale", fattura.getCausale());
			jso.put("inserimento", "No");
			jsa.put(jso);
		} // for
		this.mandaRisposta(response, jsa, null);
	}// cercaMesiPrecedentiFatturazioni

	/**
	 * Salva l'accoppiata di una nuova fattura con altre fatture tipicamente emesse
	 * nei mesi precedenti per inviarne solo una con un importo maggiore.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiIntervento}).
	 * @param response Risposta al browser
	 * @throws IOException   In caso di problemi durante la scrittura del mesaggio
	 *                       di risposta.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	@ResourceMapping(value = "salvaMesiPrecedentiFatturazioni")
	@Transactional
	public void salvaMesiPrecedentiFatturazioni(ResourceRequest request, ResourceResponse response)
			throws IOException, JSONException {
		JSONArray jsaDati = new JSONArray(request.getParameter("data"));

		if (((JSONObject) jsaDati.get(0)) != null && ((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")) != null
				&& !((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")).equals("")) {

			// presuppongo almeno una riga altrimenti la tabella non esegue la
			// chiamata AJAX di sincronizzazione.

			Query q = getEntityManager().createNamedQuery("Fattura.findByIdFatt");
			q.setParameter("idFatt", Integer.parseInt((String) ((JSONObject) jsaDati.get(0)).get("id_fattura")));
			Fattura f = (Fattura) q.getSingleResult();
			// trovata la fattura cerco ancora le fatture precedenti con la stessa query
			List<Fattura> fatturePrecedenti = f.getFatturaList();
			for (int i = 0; i < jsaDati.length(); i++) {
				if ("Si".equalsIgnoreCase((String) ((JSONObject) jsaDati.get(i)).get("inserimento"))) {
					q.setParameter("idFatt", ((JSONObject) jsaDati.get(i)).get("id"));
					Fattura fatturaPrecedente = (Fattura) q.getSingleResult();
					fatturaPrecedente.setIdParamStato(this.cercaStatoFattura(getEntityManager(), "an"));
					fatturePrecedenti.add(fatturaPrecedente);

				} // if

			} // for
			getEntityManager().persist(f);
		}
		this.mandaRisposta(response, jsaDati, null);
	}// salvaMesiPrecedentiFatturazioni

	/**
	 * Salva i dati di una nuova fattura.
	 *
	 * @param request  Richiesta del browser contenente la chiave di ricerca per
	 *                 {@link PaiIntervento}).
	 * @param response Risposta al browser
	 * @throws IOException    In caso di problemi durante la scrittura del mesaggio
	 *                        di risposta.
	 * @throws JSONException  In caso di problemi durante la formazione della
	 *                        risposta in formato JSON (non previsto)
	 * @throws ParseException In caso una delle date presenti non sia nel formato
	 *                        italiano.
	 */
	@ResourceMapping(value = "salvaFattura")
	@Transactional
	public void salvaFattura(ResourceRequest request, ResourceResponse response)
			throws IOException, JSONException, ParseException {
		JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());
		JSONObject jsoFattura = new JSONObject(request.getParameter("data"));
		JSONObject jsoId = new JSONObject((String) jsoFattura.get("id"));
		JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoId.get("periodoRicerca"));
		final Date currentDate = new Date();
		final Fattura fattura = new Fattura();
		fattura.setAnno(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
		fattura.setBollo(new BigDecimal(jsoFattura.get("bollo").toString()));
		fattura.setCausale((String) jsoFattura.get("causale"));
		fattura.setNote((String) jsoFattura.get("note"));
		fattura.setContributo(new BigDecimal(jsoFattura.get("contributo").toString()));
		fattura.setImportoTotale(new BigDecimal(jsoFattura.get("da_pagare").toString()));
		fattura.setCodFisc((String) jsoFattura.get("codice_fiscale"));
		fattura.setScadenza(
				ISODateTimeFormat.dateTimeParser().parseDateTime(jsoFattura.getString("scadenza")).toDate());
		fattura.setImpIva(new BigDecimal(jsoFattura.get("importo_iva").toString()));
		ParametriIndata statoFattura = this.cercaStatoFattura(getEntityManager(), "de");
		fattura.setIdParamStato(statoFattura);
		ParametriIndata paramIva = (ParametriIndata) getEntityManager()
				.createNamedQuery("ParametriIndata.findByIdParamIndata")
				.setParameter("idParamIndata", jsoFattura.get("iva")).getSingleResult();
		fattura.setIdParamIva(paramIva);
		fattura.setTimbro(currentDate);
		if (jsoPeriodoRicerca.has("meseEff")) {
			fattura.setMeseRif((Integer) jsoPeriodoRicerca.get("meseEff"));
		} // if
		else {
			// se non ho il mese di riferimento ho sicuramente il periodo
			Calendar periodoDal = Calendar.getInstance();
			periodoDal.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffDal"));
			periodoDal.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffDal") - 1);
			periodoDal.set(Calendar.DAY_OF_MONTH, periodoDal.getActualMaximum(Calendar.DAY_OF_MONTH));
			Calendar periodoAl = Calendar.getInstance();
			periodoDal.set(Calendar.HOUR_OF_DAY, 0);
			periodoDal.set(Calendar.MINUTE, 0);
			periodoDal.set(Calendar.SECOND, 0);
			periodoDal.set(Calendar.MILLISECOND, 0);
			periodoAl.set(Calendar.YEAR, (Integer) jsoPeriodoRicerca.get("annoEffAl"));
			periodoAl.set(Calendar.MONTH, (Integer) jsoPeriodoRicerca.get("meseEffAl") - 1);
			periodoAl.set(Calendar.DAY_OF_MONTH, 1);
			periodoAl.set(Calendar.HOUR_OF_DAY, 0);
			periodoAl.set(Calendar.MINUTE, 0);
			periodoAl.set(Calendar.SECOND, 0);
			periodoAl.set(Calendar.MILLISECOND, 0);
			fattura.setPeriodoDal(periodoDal.getTime());
			fattura.setPeriodoAl(periodoAl.getTime());
		} // else

		List<PaiInterventoMese> interventi = this.cercaPaiInterventoMese(jsoId, jsoPeriodoRicerca, getEntityManager(),
				request);
		PaiIntervento paiIntervento = interventi.get(0).getPaiIntervento();
		fattura.setPaiIntervento(paiIntervento);
		fattura.setCodAna(paiIntervento.getPai().getAnagrafeSoc());
		fattura.setCognome(paiIntervento.getPai().getAnagrafeSoc().getCognome());
		fattura.setNome(paiIntervento.getPai().getAnagrafeSoc().getNome());
		fattura.setIdParamFascia(
				(ParametriIndata) getEntityManager().createNamedQuery("ParametriIndata.findByIdParamIndata")
						.setParameter("idParamIndata", jsoId.get("idFascia")).getSingleResult());

		// salvo in anticipo i dati delle persone socialmente obbligate
		List<PaiInterventoCivObb> obbligati = paiIntervento.getPaiInterventoCivObbList();
		if (obbligati != null) {
			switch (obbligati.size()) {
			case 6:
			case 5:
			case 4:
			case 3:
				fattura.setCfObbligato3(obbligati.get(2).getAnagrafeSoc().getCodFisc());
				fattura.setCognomeObbl3(obbligati.get(2).getAnagrafeSoc().getCognome());
				fattura.setNomeObbl3(obbligati.get(2).getAnagrafeSoc().getNome());
				fattura.setIndirizzoObbl3(this.componiIndirizzo(obbligati.get(2).getAnagrafeSoc()));
				fattura.setQuotaObbligato3(obbligati.get(2).getImpCo());
			case 2:
				fattura.setCfObbligato2(obbligati.get(1).getAnagrafeSoc().getCodFisc());
				fattura.setCognomeObbl2(obbligati.get(1).getAnagrafeSoc().getCognome());
				fattura.setNomeObbl2(obbligati.get(1).getAnagrafeSoc().getNome());
				fattura.setIndirizzoObbl2(this.componiIndirizzo(obbligati.get(1).getAnagrafeSoc()));
				fattura.setQuotaObbligato2(obbligati.get(1).getImpCo());
			case 1:
				fattura.setCfObbligato1(obbligati.get(0).getAnagrafeSoc().getCodFisc());
				fattura.setCognomeObbl1(obbligati.get(0).getAnagrafeSoc().getCognome());
				fattura.setNomeObbl1(obbligati.get(0).getAnagrafeSoc().getNome());
				fattura.setIndirizzoObbl1(this.componiIndirizzo(obbligati.get(0).getAnagrafeSoc()));
				fattura.setQuotaObbligato1(obbligati.get(0).getImpCo());
			default:
				break;
			}// switch
		} // if
			// salvo ancora i riferimenti ai detagli della fattura

		fattura.setFatturaDettaglioList(Lists.newArrayList(
				Iterables.transform(Multimaps.index(interventi, new Function<PaiInterventoMese, String>() {
					public String apply(PaiInterventoMese paiInterventoMese) {
						return creaIdPaiInterventoMese(paiInterventoMese.getPaiInterventoMesePK());
					}
				}).asMap().entrySet(),
						new Function<Map.Entry<String, Collection<PaiInterventoMese>>, FatturaDettaglio>() {
							public FatturaDettaglio apply(Map.Entry<String, Collection<PaiInterventoMese>> entry) {
								PaiInterventoMese paiInterventoMeseCampione = entry.getValue().iterator().next();
								FatturaDettaglio fatturaDettaglio = new FatturaDettaglio();
								fatturaDettaglio.setAumento(BigDecimal.ZERO);
								fatturaDettaglio.setAnnoEff(
										(int) paiInterventoMeseCampione.getPaiInterventoMesePK().getAnnoEff());
								fatturaDettaglio.setCausVar(paiInterventoMeseCampione.getCausVar());
								fatturaDettaglio.setCodTipint(
										paiInterventoMeseCampione.getPaiIntervento().getTipologiaIntervento());
								fatturaDettaglio.setIdFatt(fattura);
								fatturaDettaglio.setIdParamUnitaMisura(
										paiInterventoMeseCampione.getPaiIntervento().getIdParamUniMis());
								fatturaDettaglio.setImporto(BigDecimal.ZERO);
								fatturaDettaglio.setQtInputata(BigDecimal.ZERO);
								fatturaDettaglio.setVarStraord(BigDecimal.ZERO);

								for (PaiInterventoMese paiInterventoMese : entry.getValue()) {
									// modifica importante in data 04/04/ faccio in modo di scontare ogni singolo
									// PIM.
									BigDecimal fascia = BigDecimal.ZERO;
									if (paiInterventoMeseCampione.getIdParamFascia() != null) {
										fascia = paiInterventoMeseCampione.getIdParamFascia().getDecimalParam();
									}
									fascia = (new BigDecimal("100").subtract(fascia)).divide(new BigDecimal("100"));
									BigDecimal importoStandard = getImportoStandard(
											paiInterventoMese.getPaiIntervento(), getEntityManager());
									BigDecimal daScontare = importoStandard.multiply(fascia).setScale(2,
											RoundingMode.DOWN);
									BigDecimal importo = paiInterventoMese.getBdgConsQta().multiply(daScontare);
									fatturaDettaglio.setImporto(fatturaDettaglio.getImporto().add(importo));
									if (paiInterventoMese.getBdgConsQta() != null) {
										fatturaDettaglio.setQtInputata(fatturaDettaglio.getQtInputata()
												.add(paiInterventoMese.getBdgConsQta()));
									}
									if (paiInterventoMese.getBdgConsVar() != null) {
										fatturaDettaglio.setVarStraord(fatturaDettaglio.getVarStraord()
												.add(paiInterventoMese.getBdgConsVar()));
									}
									paiInterventoMese.setIdFattDettaglio(fatturaDettaglio);
									paiInterventoMese.setGenerato(0);
									// correggi per la fascia di riga

									// fatturaDettaglio.setImporto(fatturaDettaglio.getImporto().multiply(fascia));
									fatturaDettaglio.setVarStraord(fatturaDettaglio.getVarStraord().multiply(fascia));

								}

//                        new BigDecimal(intervento.getBdgConsQta().doubleValue() * intervento.getPaiIntervento().getTipologiaIntervento().getImpStdEntr().doubleValue()));
								fatturaDettaglio.setMeseEff(
										(int) paiInterventoMeseCampione.getPaiInterventoMesePK().getMeseEff());
								fatturaDettaglio.setRiduzione(BigDecimal.ZERO);
								fatturaDettaglio.setTimbro(currentDate);
								for (PaiInterventoMese paiInterventoMese : entry.getValue()) {
									if (paiInterventoMese.getPaiCdg() == null) {
										PaiCdg cdg = new PaiCdg();
										PaiCdgPK cdgPK = new PaiCdgPK();
										cdgPK.setCntTipint(paiInterventoMese.getPaiInterventoMesePK().getCntTipint());
										cdgPK.setCodPai(paiInterventoMese.getPaiInterventoMesePK().getCodPai());
										cdgPK.setCodTipint(paiInterventoMese.getPaiInterventoMesePK().getCodTipint());
										cdgPK.setCodAnno(paiInterventoMese.getPaiInterventoMesePK().getAnno());
										cdgPK.setAnnoEff(paiInterventoMese.getPaiInterventoMesePK().getAnnoEff());
										cdgPK.setMeseEff(paiInterventoMese.getPaiInterventoMesePK().getMeseEff());
										cdgPK.setCodImpe(paiInterventoMese.getPaiInterventoMesePK().getCodImp());
										cdg.setPaiCdgPK(cdgPK);
										cdg.setPaiInterventoMese(paiInterventoMese);
										cdg.setCodAna(paiInterventoMese.getPaiIntervento().getPai().getAnagrafeSoc());
										cdg.setCodCap(paiInterventoMese.getBudgetTipIntervento().getCodCap());
										cdg.setCcele(paiInterventoMese.getPaiIntervento().getTipologiaIntervento()
												.getCcele());
										cdg.setImpStd(getImportoStandard(paiInterventoMese.getPaiIntervento(),
												getEntityManager()));
										cdg.setImpVar(paiInterventoMese.getBdgConsVar());
										cdg.setQtaPrev(paiInterventoMese.getBdgPrevQta());
										paiInterventoMese.setPaiCdg(cdg);
										getEntityManager().persist(cdg);
									}
									paiInterventoMese.getPaiCdg()
											.setImpComplUsingFascia(paiInterventoMese.getBdgConsEur().add(MoreObjects
													.firstNonNull(paiInterventoMese.getBdgConsVar(), BigDecimal.ZERO)));
									paiInterventoMese.getPaiCdg().setQtaErog(paiInterventoMese.getBdgConsQta());
									paiInterventoMese.setGenerato(0);
								}
								return fatturaDettaglio;
							}
						})));

		// patch per eliminare i dettagli fattura che hanno un importo pari a 0 (zero)
		// risultano 'poco comprensibili' ai destinatari.
		List<FatturaDettaglio> dettagli = fattura.getFatturaDettaglioList();
		for (int i = dettagli.size() - 1; i >= 0; i--) {
			FatturaDettaglio dettaglio = dettagli.get(i);
			if (dettaglio.getImporto().equals(BigDecimal.ZERO)) {
				dettagli.remove(dettaglio);
			}
		}
		// fine patch dettagli a 0

		getEntityManager().persist(fattura);
		// ritrasmetto i dati della fattura con il nuovo id appena assegnato.
		JSONArray risposta = new JSONArray();
		jsoFattura.put("id_fatt", fattura.getIdFatt());
		risposta.put(jsoFattura);
		jsonBuilder.withData(risposta).buildResponse();
	}// salvaFattura

	/**
	 * Cerca nella base di dati i {@link PaiIntervento} associato al pai.
	 *
	 * @param id            l'oggetto JSON in formato {@link String}.
	 * @param entityManager Gestore della base di dati.
	 * @return L'oggetto {@link PaiIntervento} trovato nella base di dati.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	private List<PaiIntervento> cercaPaiIntervento(String id, EntityManager entityManager) throws Exception {
		return new PaiDao(entityManager).findPai(new JSONObject(id).getString("codPai")).getPaiInterventoList();
	}// cercaPaiIntervento

	/**
	 * Cerca nella base di dati i {@link PaiInterventoMese} filtrati per id del
	 * {@link PaiIntervento} e il periodo di riferimento. La lista conterrà
	 * solamente i record che non sono stati ancora associati a qualche fattura e
	 * che il {@link PaiIntervento} è nello stato di esecutività.
	 *
	 * @param jsoIdPai          l'oggetto JSON contenente l'id del
	 *                          {@link PaiIntervento}
	 * @param jsoPeriodoRicerca l'oggetto JSON contenente il filtro di ricerca per
	 *                          periodo.
	 * @param entityManager     Gestore della base di dati.
	 * @return La lista dei {@link PaiInterventoMese} trovati nella base di dati.
	 * @throws JSONException In caso di problemi durante la formazione della
	 *                       risposta in formato JSON (non previsto)
	 */
	private List<PaiInterventoMese> cercaPaiInterventoMese(JSONObject jsoIdPai, JSONObject jsoPeriodoRicerca,
		EntityManager entityManager, ResourceRequest request) throws JSONException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery();
		Root r = c.from(PaiInterventoMese.class);
		Join pi = r.join("paiIntervento", JoinType.INNER);
		Join ti = pi.join("tipologiaIntervento", JoinType.INNER);
		Join pai = pi.join("pai", JoinType.INNER);
		Join pin = pai.join("idParamUot", JoinType.INNER);
		Join pinfs = r.join("idParamFascia", JoinType.LEFT);
		Join fad = r.join("idFattDettaglio", JoinType.LEFT);
		Join fa = fad.join("idFatt", JoinType.LEFT);

		Predicate p = cb.equal(fad.get("idFattDettaglio"), cb.nullLiteral(FatturaDettaglio.class));

		p = cb.and(p, cb.notEqual(r.get("bdgConsQta"), cb.nullLiteral(BigDecimal.class)));
		p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoIdPai.get("codPai")));
		p = cb.and(p, cb.equal(pinfs.get("idParamIndata"), jsoIdPai.get("idFascia")));
		p = cb.and(p, cb.equal(ti.get("flgFatt"), "S"));

		// deve essere entrato in esecutivit� (ANCHE SE CHIUSO)
		p = cb.and(p, pi.get("dtEsec").isNotNull());

		// filtro su fascia e fatturazione e data validit� isee
		p = cb.and(p, cb.or(pinfs.get("decimalParam").isNull(), cb.lessThan(pinfs.get("decimalParam"), 100)));

		p = cb.and(p, pi.get("statoInt").in("C", "E"));
		if (jsoPeriodoRicerca.has("meseEff")) {
			Predicate mese = cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoPeriodoRicerca.get("meseEff"));
			Predicate anno = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoPeriodoRicerca.get("annoEff"));
			p = cb.and(p, mese, anno);
		} // if
		else {
			// se non era specificato il mese sicuramete deve essere specificato il periodo
			// (verificare il metodo precedente)
			Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"),
					(Integer) jsoPeriodoRicerca.get("meseEffDal"));
			Predicate annoDal = cb.ge(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffDal"));
			Predicate annoDal2 = cb.greaterThan(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffDal"));

			Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"),
					(Integer) jsoPeriodoRicerca.get("meseEffAl"));
			Predicate annoAl = cb.le(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffAl"));
			Predicate annoAl2 = cb.lessThan(r.get("paiInterventoMesePK").get("annoEff"),
					(Integer) jsoPeriodoRicerca.get("annoEffAl"));
			p = cb.and(p, cb.or(annoDal2, cb.and(meseDal, annoDal)), cb.or(annoAl2, cb.and(meseAl, annoAl)));

		} // else

		String paramUotStr = Strings.emptyToNull(request.getParameter("uot_struttura"));
		if (paramUotStr != null) {
			try {
				p = cb.and(p, cb.equal(pin.get("idParamIndata"), Integer.parseInt(paramUotStr)));
			} // try
			catch (NumberFormatException ex) {
				getLogger().warn("", ex);
				// L'id dell'uot non è in unmerico
				// Eseguo la ricerca ignorando il parametro errato.
			} // catch
		} // if
		String codTipInt = request.getParameter("tipo_intervento");
		if (!Strings.isNullOrEmpty(codTipInt)) {
			p = cb.and(p, cb.equal(pi.get("paiInterventoPK").get("codTipint"), codTipInt));
		}
		c.where(p);
		return ((List<PaiInterventoMese>) entityManager.createQuery(c).getResultList());
	}// cercaPaiInterventoMese

	/**
	 * Crea una stringa rappresentante la chiave primaria del
	 * {@link PaiInterventoMese}. La stringa in formato JSON verrà creata ignorando
	 * i campi della chiave che rappresentano l'impegno per poter gestire
	 * {@link PaiInterventoMese} diversi che però rappresentano la stessa azione ma
	 * solo caricati su impegni diversi.
	 *
	 * @param pk Chiave del {@link PaiInterventoMese}
	 * @return La stringa in formato JSON rappresentante il
	 *         {@link PaiInterventoMesePK}
	 * @throws JSONException In caso di problemi con il parsing della del periodo di
	 *                       ricerca.
	 */
	private String creaIdPaiInterventoMese(PaiInterventoMesePK pk) {
		try {
			JSONObject jsoId = new JSONObject();
			jsoId.put("annoEff", pk.getAnnoEff());
			jsoId.put("cntTipint", pk.getCntTipint());
			jsoId.put("codPai", pk.getCodPai());
			jsoId.put("codTipint", pk.getCodTipint());
			jsoId.put("meseEff", pk.getMeseEff());
			return (jsoId.toString());
		} // creaIdPaiInterventoMese
		catch (JSONException ex) {
			throw new RuntimeException(ex);
		}
	}// creaIdPaiInterventoMese

	/**
	 * Ritorna il costo standard di un intervento.
	 * 
	 * @param i
	 * @param e
	 * @return
	 */
	private BigDecimal getImportoStandard(PaiIntervento i, EntityManager e) {
		BigDecimal importoStandard;
		// se ha una struttura
		if (i.getTariffa() != null) {
			importoStandard = i.getTariffa().getCosto();
		} else {
			importoStandard = i.getTipologiaIntervento().getImpStdCosto();
		}
		return importoStandard;
	}
}// AJAXFatturaNuovaDaSelezione
