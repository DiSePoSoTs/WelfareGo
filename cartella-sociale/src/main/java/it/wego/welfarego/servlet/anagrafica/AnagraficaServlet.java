/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonResponse;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.model.json.JSONAnagraficaResponse;
import it.wego.welfarego.model.json.JSONMessage;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.AssociazioneDao;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.LiberatoriaDao;
import it.wego.welfarego.persistence.dao.LoaAnagrafeDao;
import it.wego.welfarego.persistence.dao.NoteCondiviseDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeFamPK;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Liberatoria;
import it.wego.welfarego.persistence.entities.LogAnagrafe;
import it.wego.welfarego.persistence.entities.NoteCondivise;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.serializer.NoteSerializer;
import it.wego.welfarego.serializer.PaiSerializer;
import it.wego.welfarego.services.AnagraficheSyncService;
import it.wego.welfarego.servlet.SessionConstants;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AnagraficaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	public static final String COMUNE_DI_TRIESTE = "Comune di Trieste";

	private static final String MESSAGE_VISTA_ANAGRAFE_NESSUNA_MODIFICA = "Non sono state apportate modifiche ai dati";
	
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		JSONAnagraficaResponse json = new JSONAnagraficaResponse();
		EntityManager entityManager = Connection.getEntityManager();
		String codAnag = Strings.emptyToNull(request.getParameter("codAna"));
		String action = Strings.emptyToNull(request.getParameter("action"));
		Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
		Preconditions.checkNotNull(connectedUser, "e' necessario essere loggati per svolgere questa operazione!");
		try {
			VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
			String codiceAnagraficoComune = Strings.emptyToNull(request.getParameter("anagraficaCodiceAnagrafico"));
			String codiceFiscale = Strings.emptyToNull(request.getParameter("anagraficaCodiceFiscale"));

			if (Objects.equal(action, "write")) {
				AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(entityManager);
				AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(entityManager);
				String comuneResidenza = "";
				String viaResidenza = "";
				String messaggioAggiuntivo = "";
				AnagrafeSoc anagrafe = null;
				if (codAnag != null && Integer.valueOf(codAnag) > 0) {
					AnagrafeSoc originale = anagrafeDao.findByCodAna(Integer.valueOf(codAnag));
					if (originale != null && originale.getLuogoResidenza().getComune() != null) {
						comuneResidenza = originale.getLuogoResidenza().getComune().getDesCom().toUpperCase();
					}
					if (originale != null && originale.getLuogoResidenza().getVia() != null) {
						viaResidenza = originale.getLuogoResidenza().getVia().getToponomasticaPK().getCodVia();
					}
					anagrafe = serializer.serializeAnagrafica(request);
					if (anagrafe.getLuogoResidenza().getComune() != null && comuneResidenza.equals("TRIESTE")
							&& !anagrafe.getLuogoResidenza().getComune().getDesCom().toUpperCase().equals("TRIESTE")) {
						messaggioAggiuntivo = messaggioAggiuntivo + "<br>E' stato modificato il comune di residenza.";
					}
					if (anagrafe.getLuogoResidenza().getVia() != null && comuneResidenza.equals("TRIESTE") && !anagrafe
							.getLuogoResidenza().getVia().getToponomasticaPK().getCodVia().equals(viaResidenza)) {
						messaggioAggiuntivo = messaggioAggiuntivo + "<br>E' stata modificata la via di residenza.";
					}
					if (!messaggioAggiuntivo.equals("")) {
						messaggioAggiuntivo = messaggioAggiuntivo
								+ "<br /><br />Verificare se è necessario cambiare la UOT e l'assistente sociale.";
					}
				} else {
					String cf = request.getParameter("anagraficaCodiceFiscale");
					String codAnaCom = request.getParameter("anagraficaCodiceAnagrafico");
					AnagrafeSoc a = anagrafeDao.findByCodFiscCodAnaCom(cf, codAnaCom);
					Preconditions.checkArgument(a == null,
							"Attenzione! E' già presente in banca dati un utente con lo stesso codice fiscale o lo stesso codice anagrafica comunale");
					anagrafe = serializer.serializeAnagrafica(request);
				}
				CartellaDao cartellaDao = new CartellaDao(entityManager);
				PaiSerializer paiSer = new PaiSerializer(entityManager);
				CartellaSociale cartella = null;
				entityManager.getTransaction().begin();
				if (codAnag != null && !"".equals(codAnag) && Integer.valueOf(codAnag) > 0) {
					// Potrei avere un utente in anagrafica per cui non ho ancora creato una
					// cartella sociale
					// Es. famigliare/referente di una persona già censita
					cartella = cartellaDao.findByCodAna(anagrafe.getCodAna());
					if (cartella == null) {
						cartella = new CartellaSociale();
						cartella.setCodAna(anagrafe.getCodAna());
						cartella.setDtApCs(new Date());
						cartellaDao.insert(cartella);
					}
				} else {
					anagrafeDao.insert(anagrafe);
					cartella = new CartellaSociale();
					cartella.setCodAna(anagrafe.getCodAna());
					cartella.setDtApCs(new Date());
					cartellaDao.insert(cartella);
				}

				Pai pai = new PaiDao(entityManager).findLastPai(anagrafe.getCodAna());

				if (pai != null) {
					pai = paiSer.serializePaiAnagrafica(request, cartella);
				} else {
					if (Strings.isNullOrEmpty(request.getParameter("anagraficaUot"))
							&& Strings.isNullOrEmpty(request.getParameter("anagraficaAssistenteSociale"))) {
					} else {
						messaggioAggiuntivo += "<br />attenzione: non e' possibile assegnare uot e assistente sociale senza creare prima un pai per l'utente";
					}

				}
				LoaAnagrafeDao logAnagrafeDao = new LoaAnagrafeDao(entityManager);
				LogAnagrafe log = getLogAnagrafe(anagrafe, pai, connectedUser);
				logAnagrafeDao.insert(log);
				// dobbiamo sincronizzare la famiglia ....
				AnagrafeFamigliaDao famigliaDao = new AnagrafeFamigliaDao(entityManager);

				// mi prendo la lista dei familiari ( che non sono morti )
				List<VistaAnagrafe> famigliaAnagrafica = famigliaDao
						.findFamigliareAnagrafeComunaleNonMorti(anagrafe.getCodAna());
				// mi prendo la famiglia sociale
				List<AnagrafeFam> famigliaSociale = famigliaDao.getAnagrafeFamListMerge(anagrafe);

				for (VistaAnagrafe familiareAnagrafe : famigliaAnagrafica) {
					// per prima cosa evitiamo di pescare se stesso... perchè non so se il dao già
					// si occupi di cio
					if (!Integer.valueOf(anagrafe.getCodAnaCom()).equals(familiareAnagrafe.getNumeroIndividuale())) {
						// step 1 vediamo se il parente dell'utente in anagrafe c'è su welfarego
						AnagrafeSoc familiareSoc = anagrafeDao.findByCodFiscCodAnaCom(
								familiareAnagrafe.getCodiceFiscale(),
								familiareAnagrafe.getNumeroIndividuale().toString());
						// se il familiare è presente in welfarego vedo se c'è la relazione di parentela
						if (familiareSoc != null) {
							boolean parentelaPresente = false;
							for (AnagrafeFam familiareSociale : famigliaSociale) {
								if (familiareSociale.getAnagrafeSocTarget().getCodAna()
										.equals(familiareSoc.getCodAna())) {
									parentelaPresente = true;
								}

							}
							// se la parentela non c'è e non stiamo tentando di accoppiare l'utente con se
							// stesso .. la creo
							if (!parentelaPresente) {
								AnagrafeFam nuovoMembro = createAnagrafeFam(anagrafe.getCodAna(), familiareSoc,
										entityManager);
								famigliaDao.insert(nuovoMembro);

							}
						}
						// se invece l'utente non è presente in welfarego ..lo creo e poi lo accoppio
						// col parente
						else {
							familiareSoc = copiaVistaInAnagrafe(entityManager,
									familiareAnagrafe.getNumeroIndividuale());
							anagrafeDao.insert(familiareSoc);
							AnagrafeFam nuovoMembro = createAnagrafeFam(anagrafe.getCodAna(), familiareSoc,
									entityManager);
							famigliaDao.insert(nuovoMembro);

						}
					}
				}

				CartellaSocialeWsClient.newInstance().withEntityManager(entityManager).loadConfigFromDatabase()
						.withAnagrafeSoc(anagrafe).sincronizzaCartellaSociale();
				LiberatoriaDao ldao = new LiberatoriaDao(entityManager);

				if (Strings.emptyToNull(request.getParameter("anagraficaLiberatoria")) != null) {
					// verifichiamo prima che esista una liberatoria per il comune

					Liberatoria l = ldao.getLiberatoriaByAssociazioneAndUser(1, anagrafe.getCodAna());
					if (l == null) {
						l = new Liberatoria();
						l.setAnagrafeSoc(anagrafe);
						l.setCodUte(connectedUser);
						l.setAssociazione(new AssociazioneDao(entityManager).findById(1));
						l.setDtFirma(new Date());
						ldao.insert(l);
					}
				}
				entityManager.getTransaction().commit();

				json.setMessage("Operazione eseguita correttamente" + messaggioAggiuntivo);
				json.setSuccess(true);
				json.setCodAna(anagrafe.getCodAna());
				return json;
			} else if (Objects.equal(action, "verifica")) {
				return actionVerifica(entityManager, codAnag, vistaAnagrafeDao, codiceAnagraficoComune, codiceFiscale);

			} else if (Objects.equal(action, "sincronizzaDaAnagrafeSoc")) {
				String codiceFiscaleWellfargo = Strings.emptyToNull(request.getParameter("codiceFiscale"));
				return actionSincronizzaDaAnagrafeSoc(codAnag, codiceFiscaleWellfargo, codiceAnagraficoComune,
						entityManager, vistaAnagrafeDao, connectedUser);

			} else if (Objects.equal(action, "listNote")) {
				List<NoteCondivise> n = new NoteCondiviseDao(entityManager).getNoteByCodAna(Integer.valueOf(codAnag));
				getLogger().debug("le note condivise per codAnag: " + codAnag + " sono: " + n.size());

				return JsonBuilder.newInstance().withParameters(getParameters()).withData(n)
						.withTransformer(NoteSerializer.getNoteMinifiedSerializer()).buildStoreResponse();

			} else if (Objects.equal(action, "salvaNota")) {
				JSONMessage jsonNota = new JSONMessage();
				String titolo = Strings.nullToEmpty(request.getParameter("titolo"));
				String esteso = Strings.nullToEmpty(request.getParameter("esteso"));
				String codAna = Strings.nullToEmpty(request.getParameter("codAna"));
				NoteCondivise c = new NoteCondivise();
				c.setAnagrafeSoc(new AnagrafeSocDao(entityManager).findByCodAna(codAna));
				c.setTitolo(titolo);
				c.setEsteso(esteso);
				c.setDtInserimento(new Date());
				c.setCodUte(connectedUser);
				new NoteCondiviseDao(entityManager).insert(c);
				jsonNota.setMessage("Nota inserita con successo");
				jsonNota.setSuccess(true);
				return jsonNota;

			} else {
				throw new UnsupportedOperationException("unknown/missing action code : " + action);
			}
		} catch (Exception ex) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw ex;
		} finally {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

	Object actionSincronizzaDaAnagrafeSoc(String codAnagWelfargo, String codiceFiscaleWellfargo,
			String codiceAnagraficoComune, EntityManager entityManager, VistaAnagrafeDao vistaAnagrafeDao,
			Utenti connectedUser) throws Exception {

		VistaAnagrafe vista = getVistaAnagrafe(vistaAnagrafeDao, codiceAnagraficoComune, codiceFiscaleWellfargo);

		AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
		AnagrafeSoc anagrafe = anagrafeSocDao.findByCodAna(codAnagWelfargo);

		// se devo inserire la nota condivisa devo prendermi le differenze prima di
		// allineare le anagrafiche.
		List<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differencesList = vistaAnagrafeDao
				.getVistaAnagraficaToAnagrafeSocDifferences(vista, anagrafe);

		AnagraficheSyncService.allineaWellfargoFromComune(codiceFiscaleWellfargo, entityManager, codAnagWelfargo,
				vistaAnagrafeDao, codiceAnagraficoComune);

		boolean devoCreareNotaCondivisa = devoCreareNotaCondivisa(anagrafe);

		getLogger()
				.debug("richiesta sincronizzazione anagrafe, devo creare nota condivisa: " + devoCreareNotaCondivisa);

		if (devoCreareNotaCondivisa) {
			String messaggioEsteso = preparaMessaggioEsteso(differencesList, anagrafe.getCreationDate(),
					anagrafe.getCreationUser());

			NoteCondivise noteCondivise = new NoteCondivise();
			noteCondivise.setAnagrafeSoc(anagrafe);
			noteCondivise.setCodUte(connectedUser);
			noteCondivise.setDtInserimento(new Date());
			noteCondivise.setEsteso(messaggioEsteso);
			noteCondivise.setTitolo("retifica anagrafica"); // max 40 chars

			NoteCondiviseDao noteCondiviseDao = new NoteCondiviseDao(entityManager);
			noteCondiviseDao.insert(noteCondivise);

			anagrafe.setDataUltimaRetifica(new Date());
			anagrafeSocDao.update(anagrafe);
		}

		return JsonBuilder.newInstance().buildResponse();
	}

	String preparaMessaggioEsteso(List<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differencesList,
			Date creationDate, Utenti creationUser) {
		String message = Joiner.on(" ").join(Lists.transform(differencesList, differenceToMessageTransformer));
		getLogger().debug("message: " + message + ", " + differencesList.size());

		try {
		} catch (Exception e) {
		}

		String nominativo = "";
		try {
			nominativo = creationUser.getCognomeNome();
			if (nominativo == null || nominativo.trim().length() == 0 || nominativo.contains("null null")) {
				nominativo = "N.D.";
			}
		} catch (Exception e) {
			nominativo = "N.D.";
		}

		String associazione = "";
		try {
			associazione = creationUser.getAssociazione().getNome();
			if (associazione == null || associazione.trim().length() == 0) {
				associazione = "N.D.";
			}
		} catch (Exception e) {
			associazione = "associazione non disponibile";
		}

		String preambolo = "dati retificati da anagrafe comunale. ";
		String finalMessage = preambolo + message;

		getLogger().debug(finalMessage.length() + " - " + finalMessage);

		if (finalMessage.length() > 200) {
			finalMessage = preambolo;
		}
		getLogger().debug(finalMessage.length() + " - " + finalMessage);
		return finalMessage;
	}

	/**
	 * @param anagrafe
	 * @return true se questa anagrafe è stata inserita da un utente non associato
	 *         al comune e non è mai stata retificata
	 */
	boolean devoCreareNotaCondivisa(AnagrafeSoc anagrafe) {
		boolean result = false;

		boolean utenteNonAssociatoAlComune = false;
		try {
			utenteNonAssociatoAlComune = !COMUNE_DI_TRIESTE
					.equalsIgnoreCase(anagrafe.getCreationUser().getAssociazione().getNome());
			boolean dataUltimaRetificaIsNull = anagrafe.getDataUltimaRetifica() == null;

			getLogger().debug("utenteNonAssociatoAlComune: " + utenteNonAssociatoAlComune
					+ ", dataUltimaRetificaIsNull:" + dataUltimaRetificaIsNull);

			if (utenteNonAssociatoAlComune && dataUltimaRetificaIsNull) {
				result = true;
			}
		} catch (NullPointerException e) {
			// non creare la nota condivisa
		}

		return result;
	}

	Object actionVerifica(EntityManager entityManager, String codiceAnagraficoWellfarGo,
			VistaAnagrafeDao vistaAnagrafeDao, String codiceAnagraficoComune, String codiceFiscale)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Preconditions.checkNotNull(codiceAnagraficoWellfarGo,
				"Attenzione! Non è stato possibile effettuare la ricerca l'utente. Verifica che l'utente sia stato salvato in banca dati prima di procedere con la verifica");

		AnagrafeSoc anagrafe = new AnagrafeSocDao(entityManager).findByCodAna(codiceAnagraficoWellfarGo);
		if (codiceAnagraficoComune == null && codiceFiscale == null) {
			throw new IllegalArgumentException(
					"Attenzione : Almeno uno fra codice fiscale e codice anagrafico deve essere valorizzato");
		}
		VistaAnagrafe vista = getVistaAnagrafe(vistaAnagrafeDao, codiceAnagraficoComune, codiceFiscale);
		Preconditions.checkNotNull(vista, "utente non trovato in anagrafe comunale per cod = {}",
				codiceAnagraficoComune);

		List<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differencesList = vistaAnagrafeDao
				.getVistaAnagraficaToAnagrafeSocDifferences(vista, anagrafe);

		JsonBuilder jsonBuilder = JsonBuilder.newInstance();
		if (differencesList.isEmpty()) {
			jsonBuilder.withMessage(MESSAGE_VISTA_ANAGRAFE_NESSUNA_MODIFICA).withValue("canUpdate", false);
		} else {
			String message = Joiner.on("<br />").join(Lists.transform(differencesList, differenceToMessageTransformer));
			jsonBuilder.withMessage(message).withValue("canUpdate", true);
		}
		JsonResponse jsonResponse = jsonBuilder.buildResponse();
		return jsonResponse;

	}

	VistaAnagrafe getVistaAnagrafe(VistaAnagrafeDao vistaAnagrafeDao, String codiceAnagraficoComune,
			String codiceFiscale) {
		VistaAnagrafe vista = null;
		if (codiceAnagraficoComune != null) {
			vista = vistaAnagrafeDao.findByNumeroIndividuale(codiceAnagraficoComune);
		} else {
			vista = vistaAnagrafeDao.findByCodiceFiscale(codiceFiscale);
		}
		return vista;
	}

	private LogAnagrafe getLogAnagrafe(AnagrafeSoc anagrafe, @Nullable Pai pai, Utenti connectedUser) {
		LogAnagrafe log = new LogAnagrafe();
		log.setCodAna(anagrafe);
		log.setCodPai(pai);
		log.setCodUte(connectedUser);
		log.setTsEve(new Date());
		return log;
	}


	private final Map<String, String> nomeCampoToDesCampo;
	{
		Map<String, String> map = Maps.newHashMap();
		map.put("codStatoCitt", "nazionalita");
		map.put("codFisc", "codice fiscale");
		map.put("cognomeConiuge", "cognome coniuge");
		map.put("dtMorte", "data decesso");
		map.put("dtNasc", "data di nascita");
		map.put("nome", "nome");
		map.put("cognome", "cognome");
		map.put("luogoNascita", "luogo di nascita");
		map.put("luogoRersidenza", "luogo di residenza");
		map.put("flgSex", "sesso");
		map.put("codAnaFamCom", "codice nucleo familiare");
		map.put("codAnaCom", "codice anagrafico");
		map.put("idParamPosAna", "posizione anagrafica");
		nomeCampoToDesCampo = Collections.unmodifiableMap(map);
	}

	private final Function<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference, String> differenceToMessageTransformer = new Function<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference, String>() {
		public String apply(VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference difference) {
			return new StringBuilder().append("Il valore del campo ")
					.append(MoreObjects.firstNonNull(nomeCampoToDesCampo.get(difference.getPropertyName()),
							difference.getPropertyName()))
					.append(" in anagrafe è ").append(difference.getVistaAnagrafeStringValue())
					.append(", in welfarego è ").append(difference.getAnagrafeSocStringValue()).toString();
		}
	};


	private ParametriIndata getParente(EntityManager em) {
		ParametriIndataDao dao = new ParametriIndataDao(em);
		ParametriIndata parente = dao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
		return parente;
	}

	private AnagrafeFam createAnagrafeFam(Integer codAnag, AnagrafeSoc anagrafeSociale, EntityManager em)
			throws NumberFormatException {
		AnagrafeFam fam = new AnagrafeFam();
		AnagrafeFamPK key = new AnagrafeFamPK();
		key.setCodAna(codAnag);
		key.setCodAnaFam(anagrafeSociale.getCodAna());
		fam.setAnagrafeFamPK(key);
		ParametriIndata parente = getParente(em);
		fam.setCodQual(parente);
		return fam;
	}

	private AnagrafeSoc copiaVistaInAnagrafe(EntityManager em, int numeroIndividuale) throws Exception {
		VistaAnagrafeDao vistaAnagraficaDao = new VistaAnagrafeDao(em);
		VistaAnagrafe anagrafeComunale = vistaAnagraficaDao.findByNumeroIndividuale(numeroIndividuale);
		AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
		AnagrafeSoc anagrafeSociale = serializer.vistaAnagraficaToAnagrafeSoc(anagrafeComunale);
		return anagrafeSociale;
	}

}
