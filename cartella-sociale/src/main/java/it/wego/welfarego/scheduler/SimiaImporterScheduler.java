/**
 * 
 */
package it.wego.welfarego.scheduler;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import it.insiel.simia.interop.GestoreRichiestePortTypeProxy;
import it.insiel.simia.interop.data.beans.xsd.AnomalieRichiesta;
import it.insiel.simia.interop.data.beans.xsd.CoefficientiCalcolo;
import it.insiel.simia.interop.data.beans.xsd.Componenti;
import it.insiel.simia.interop.data.beans.xsd.Esito;
import it.insiel.simia.interop.data.beans.xsd.RichiestaCompleta;
import it.insiel.simia.interop.data.beans.xsd.RichiestaMin;
import it.insiel.simia.interop.data.beans.xsd.RichiesteDaEvadere;
import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.LuogoDao;
import it.wego.welfarego.persistence.dao.MapDatiSpecificiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.dao.UotIndirizzoDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeFamPK;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.UotIndirizzo;
import it.wego.welfarego.persistence.entities.Utenti.Ruoli;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fabio Bonaccorso Classe per l'importazione dei dai da SIMIA ( Insiel )
 *
 */
@SuppressWarnings("deprecation")
public class SimiaImporterScheduler implements MessageListener {
	protected static EntityManager entityManager;
	protected final static Logger logger = LoggerFactory.getLogger(SimiaImporterScheduler.class);
	public final String ISEE = "N_ISEE", MINORI = "N_NMIN", SCAGLIONE = "N_SCAG";
	public final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
	// duplicazione delle fascie dovuta al fatto che simia ci manda id diversi con
	// fascie uguali...
	private static final BigDecimal[][] calcolo = { { new BigDecimal(400), new BigDecimal(500), new BigDecimal(550) },
			{ new BigDecimal(360), new BigDecimal(460), new BigDecimal(510) },
			{ new BigDecimal(315), new BigDecimal(415), new BigDecimal(465) },
			{ new BigDecimal(235), new BigDecimal(335), new BigDecimal(385) },
			{ new BigDecimal(150), new BigDecimal(250), new BigDecimal(300) },
			{ new BigDecimal(70), new BigDecimal(170), new BigDecimal(220) },
			{ new BigDecimal(400), new BigDecimal(500), new BigDecimal(550) },
			{ new BigDecimal(360), new BigDecimal(460), new BigDecimal(510) },
			{ new BigDecimal(315), new BigDecimal(415), new BigDecimal(465) },
			{ new BigDecimal(235), new BigDecimal(335), new BigDecimal(385) },
			{ new BigDecimal(150), new BigDecimal(250), new BigDecimal(300) },
			{ new BigDecimal(70), new BigDecimal(170), new BigDecimal(220) },
			{ new BigDecimal(0), new BigDecimal(0), new BigDecimal(0) }, };

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.
	 * portal.kernel.messaging.Message)
	 */
	public void receive(Message arg0) {
		entityManager = Connection.getEntityManager();
		logger.info("Proviamo a recuperare le pratiche");
		GestoreRichiestePortTypeProxy proxy = new GestoreRichiestePortTypeProxy();
		RichiesteDaEvadere richieste = null;
		// prima fase : recupero delle richieste da evadere.
		try {
			richieste = proxy.getRichiesteDaEvadere();
			logger.info("simiaaaaa" + richieste.getEsito().getSTATUS());
			if (richieste.getEsito().getSTATUS() == 1) {
				throw new Exception("Problemi nella ricezione dell'elenco SIMIA HA TORNATO 1 ");
			}
		} catch (RemoteException e) {
			logger.error("SI È VERIFICATO UN ERRORE NEL RECUPERO DELLE RICHIESTEEEEEE");
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("PROBLEMA LATO SIMIA... SIMIA HA TORNATO 1 ");
			e.printStackTrace();
		} // fine prima fase.

		if (richieste != null && richieste.getElenco() != null) {
			// fase due interazione sulla richieste approvate e in richieste respinte
			logger.info("Abbiamo trovato un totale di richieste pari a :" + richieste.getElenco().length);
			List<RichiestaCompleta> approvate = new ArrayList<RichiestaCompleta>();
			List<RichiestaCompleta> respinte = new ArrayList<RichiestaCompleta>();
			Integer respintiImportati = 0;
			Integer approvatiImportati = 0;

			for (RichiestaMin r : richieste.getElenco()) {
				logger.debug(r.getStato());
				// contatore di debug

				if (r.getStato().equals("E") || r.getStato().equals("R")) {
					try {
						RichiestaCompleta completa = proxy.getRichiestaCompleta(r.getId_richiesta());
						if (r.getStato().equals("E")) {
							// verifichiamo che non sia un SIA
							if (completa.getRichiesta().getTIPO_MISURA() == 1) {
								logger.info("abbiamo trovato una domanda SIA id "
										+ completa.getRichiesta().getID_RICHIESTA());
								Esito esito = proxy.confermaRicezione(completa.getRichiesta().getID_RICHIESTA());
								if (esito != null && esito.getSTATUS() == 0) {
									logger.info("Abbiamo confermato la ricezione della richiesta SIA "
											+ completa.getRichiesta().getID_RICHIESTA());
								} else {
									logger.error("Attenzione esito non confermato per richiesta SIA "
											+ completa.getRichiesta().getID_RICHIESTA());
								}
							} else {
								approvate.add(completa);
							}

						} else {
							respinte.add(completa);
						}
					} catch (RemoteException e) {
						logger.error("Problemi nel recuperare la richiesta con il seguente ID " + r.getId_richiesta());
						e.printStackTrace();
					}
				}

			} // fine fase due
				// inizio fase 3 importazione utenti su welfarego
				// TEST
				// FIXME ONLY FOR TEST
			logger.info("Abbiamo un totale di richieste approvate pari a " + approvate.size() + "  e respinte pari a "
					+ respinte.size());

			TaskDao tdao = new TaskDao(entityManager);
			for (RichiestaCompleta respinta : respinte) {
				try {
					AnagrafeSoc richiedenteRespinto = simiaToWelfaregoAna(respinta, entityManager);

					if (richiedenteRespinto == null) {
						throw new Exception(
								"Attenzione il richiedente per il cf " + respinta.getRichiesta().getCF_RICHIEDENTE()
										+ "non è stato trovato nell'anagrafe o in welfarego");
					}
					logger.debug("Ho trovato l'anagrafe sociale per il cf" + richiedenteRespinto.getCodFisc());
					// procediamo a vedere se ha un PAI aperto...
					Pai paiUtente = simiaToWelfaregoPai(respinta, richiedenteRespinto, entityManager);
					if (paiUtente == null) {
						throw new Exception("Attenzione non è stato possibile trovare o creare un PAI per l'utente  "
								+ respinta.getRichiesta().getCF_RICHIEDENTE() + "con id richiesta"
								+ respinta.getRichiesta().getID_RICHIESTA());

					}
					logger.debug("Ho trovato il PAI per il cf" + richiedenteRespinto.getCodFisc());
					// una volta creato il pai provvediamo a creare l'intervento.
					logger.debug("Procediamo con la creazione dell'intervento respinto");
					PaiIntervento intervento = simiaToWelfaregoPaiIntervento(respinta, paiUtente, entityManager, true);
					if (intervento != null) {
						// tolto produzione lettera di respinto
						logger.debug("Ha funzionatto per cf " + richiedenteRespinto.getCognomeNome());

						Esito esito = proxy.confermaRicezione(respinta.getRichiesta().getID_RICHIESTA());
						if (esito != null && esito.getSTATUS() == 0) {
							logger.info("Abbiamo confermato la ricezione della richiesta respinta "
									+ respinta.getRichiesta().getID_RICHIESTA());
						} else {
							logger.error("Attenzione esito non confermato per richiesta approvata "
									+ respinta.getRichiesta().getID_RICHIESTA());
						}
						respintiImportati++;
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Errore nella creazione dei respinti " + e.getMessage());
					continue;
				}
			} // end for richieste repinte

			// inizio richieste approvate
			for (RichiestaCompleta approvata : approvate) {

				PaiIntervento intervento = null;
				PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
				PaiInterventoMeseDao pidao = new PaiInterventoMeseDao(entityManager);
				try {
					AnagrafeSoc richiedenteApprovato = simiaToWelfaregoAna(approvata, entityManager);
					if (richiedenteApprovato == null) {
						throw new Exception(
								"Attenzione il richiedente per il cf " + approvata.getRichiesta().getCF_RICHIEDENTE()
										+ "non è stato trovato nell'anagrafe o in welfarego");
					}
					logger.debug("Ho trovato l'anagrafe sociale per il cf" + richiedenteApprovato.getCodFisc());
					Pai paiUtente = simiaToWelfaregoPai(approvata, richiedenteApprovato, entityManager);
					if (paiUtente == null) {
						throw new Exception("Attenzione non è stato possibile trovare o creare un PAI per l'utente  "
								+ approvata.getRichiesta().getCF_RICHIEDENTE() + "con id richiesta"
								+ approvata.getRichiesta().getID_RICHIESTA());

					}
					logger.debug("Ho trovato il PAI  per il cf" + richiedenteApprovato.getCodFisc());
					logger.debug("Procediamo con la creazione dell'intervento");

					List<PaiIntervento> interventiStessoTipo = new PaiInterventoDao(entityManager)
							.findByCodPaiCodTipint(Integer.valueOf(paiUtente.getCodPai()), "EC100");

					Iterator<PaiIntervento> i = interventiStessoTipo.iterator();
					PaiIntervento interventoEsecutivoPrecedente = null;
					while (i.hasNext()) {
						PaiIntervento pi = i.next();
						if ((pi.getStatoInt() == PaiIntervento.STATO_INTERVENTO_ESECUTIVO
								|| pi.getStatoInt() == PaiIntervento.STATO_INTERVENTO_SOSPESO
								|| pi.getStatoInt() == PaiIntervento.STATO_INTERVENTO_CHIUSO)
								&& approvata.getRichiesta().getID_RICHIESTA()
										.equals(pi.getValDatoSpecifico("ds_simia") != null
												? Integer.valueOf(pi.getValDatoSpecifico("ds_simia"))
												: null) == true) {
							interventoEsecutivoPrecedente = pi;
							break;
						}
					}
					// proviamo la tattica numero due, se non è stato trovato con il codice
					// fiscale... proviamo con l'id intervento
					if (interventoEsecutivoPrecedente == null) {
						MapDatiSpecificiInterventoDao mdao = new MapDatiSpecificiInterventoDao(entityManager);
						MapDatiSpecificiIntervento m = mdao
								.findInterventoSimia(approvata.getRichiesta().getID_RICHIESTA().toString());
						if (m != null && (m.getPaiIntervento().getStatoInt() == PaiIntervento.STATO_INTERVENTO_ESECUTIVO
								|| m.getPaiIntervento().getStatoInt() == PaiIntervento.STATO_INTERVENTO_SOSPESO
								|| m.getPaiIntervento().getStatoInt() == PaiIntervento.STATO_INTERVENTO_CHIUSO)) {
							interventoEsecutivoPrecedente = m.getPaiIntervento();
						}
					}

					// se vi è un intervento esecutivo precedente.,,, lo aggiorno

					if (interventoEsecutivoPrecedente != null && interventoEsecutivoPrecedente.getStatoInt() != 'C') {
						updateInterventoPrecedente(approvata, interventoEsecutivoPrecedente, entityManager);
						logger.info(
								"Intervento aggiornato per id: richiesta " + approvata.getRichiesta().getID_RICHIESTA()
										+ " corrispondente all'utente " + approvata.getRichiesta().getCF_RICHIEDENTE());
						UniqueTasklist newTaskList = new UniqueTasklist("Intervento aggiornato da SIMIA", "N", "N");
						newTaskList.setPai(interventoEsecutivoPrecedente.getPai());
						newTaskList.setPaiIntervento(interventoEsecutivoPrecedente);
						newTaskList.setForm(entityManager.getReference(UniqueForm.class, "70"));
						newTaskList.setUot(paiUtente.getIdParamUot().getIdParam().getCodParam());
						newTaskList.setRuolo(Ruoli.OPERATORE_UOT_O_SEDE_CENTRALE.toString());
						newTaskList.setCampoFlow1(
								"L'intervento è stato aggiornato su Welfarego poichè alcuni dati sono cambiati in SIMIA");
						tdao = new TaskDao(entityManager);
						tdao.insert(newTaskList);
					}
					// se vi è un intervento vecchio... ma è chiuso......
					else if (interventoEsecutivoPrecedente != null
							&& interventoEsecutivoPrecedente.getStatoInt() == 'C') {
						// non fare niente poichè l'intervento è chiudo
						logger.info(
								"Trovato un intervento con lo stesso ID ma chiuso... non faccio nulla...ciao ciao ");
					}
					// se no nuovo intervento
					else {
						intervento = simiaToWelfaregoPaiIntervento(approvata, paiUtente, entityManager, false);
						if (intervento == null) {
							throw new Exception("Atteznione isee da aggiornare ci torneremo in seguito");
						}
						if (intervento.getDsCodAnaBenef() == null) {
							logger.error("Questo intervento non ha delegato");

							UniqueTasklist newTaskList = new UniqueTasklist("Integrare dati SIMIA", "N", "N");
							newTaskList.setPai(intervento.getPai());
							newTaskList.setPaiIntervento(intervento);
							newTaskList.setForm(entityManager.getReference(UniqueForm.class, "70"));
							newTaskList.setUot(paiUtente.getIdParamUot().getIdParam().getCodParam());
							newTaskList.setRuolo(Ruoli.OPERATORE_UOT_O_SEDE_CENTRALE.toString());
							newTaskList.setCampoFlow1(
									"Attenzione l'IBAN associato a questo intervento non corrisponde ad alcun utente in Welfarego.\n"
											+ "Completare i dati prima di procedere con la liquidazione.");
							tdao = new TaskDao(entityManager);
							tdao.insert(newTaskList);
							intervento.setDsCodAnaBenef(richiedenteApprovato);
							pdao.update(intervento);

						}

						pdao.passaInterventoInStatoEsecutivo(intervento);
						pidao.update(intervento);

						logger.info("Passato intervento in stato esecutivo");

					}

					Esito esito = proxy.confermaRicezione(approvata.getRichiesta().getID_RICHIESTA());
					if (esito != null && esito.getSTATUS() == 0) {
						logger.info("Abbiamo confermato la ricezione della richiesta approvata "
								+ approvata.getRichiesta().getID_RICHIESTA());
					} else {
						logger.error("Attenzione esito non confermato per richiesta approvata "
								+ approvata.getRichiesta().getID_RICHIESTA());
					}
					approvatiImportati++;

				} catch (Exception e) {
					if (intervento != null) {
						try {
							pidao.delete(intervento);
						} catch (Exception e1) {
							logger.error("errore durante la cancellazione");
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
					logger.error("Errore nella creazione degli approvati  " + e.getMessage());
					continue;
				}

			} // end for richieste approvate
			logger.info("Totale approvati importati con successo" + approvatiImportati + " su" + approvate.size());
		}

	}

	/**
	 * Metodo per prendere da una richiesta il richiedente e vedere se è in
	 * welfarego e se o aggiungerlo con tutto il nucleo.
	 * 
	 * @param from
	 * @param e
	 * @return
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private AnagrafeSoc simiaToWelfaregoAna(RichiestaCompleta from, EntityManager e)
			throws IllegalAccessException, InvocationTargetException, Exception {
		AnagrafeSocDao adao = new AnagrafeSocDao(e);
		VistaAnagrafeDao vdao = new VistaAnagrafeDao(e);
		String codiceFiscale = null;
		Integer idRappresentante = from.getRichiesta().getID_RAPPRESENTANTE();
		String codiceRappresentante = from.getRichiesta().getCF_RICHIEDENTE();
		if (idRappresentante == null) {
			logger.error("Id rappresentante di" + from.getRichiesta().getID_RICHIESTA() + " è null!!!");

			return null;
		}
		// se c'è un dichiarante esterno come richiedente il beneficiario sarà il primo
		// membro maggiorenne del nucleo familiare
		if (from.getDichiaranteEsterno() != null) {
			for (Componenti c : from.getElencoCom()) {
				// facciamo un check se è maggiorenne oppure se il dichiarante esterno è
				// miracolosamente uguale a un membro del nucleo familiare ( di norma non è
				// cosi...ma fanno danni !!!!
				if (from.getDichiaranteEsterno().getCF_COMP().equals(c.getCF_COMP()) || Years
						.yearsBetween(new LocalDate(c.getDATA_NASCITA().getTime()),
								new LocalDate(Calendar.getInstance().getTime()))
						.getYears() >= 18 && !c.getCF_COMP().equals(from.getRichiesta().getCF_RICHIEDENTE())) {
					codiceFiscale = c.getCF_COMP();
					break;
				}
			}
		}
		// se invece il rappresentante è un componente me lo vado a pescare
		else {
			for (Componenti c : from.getElencoCom()) {
				// misura di emergenza da verificare se lasciare o meno. (può essere che un id
				// rappresentante non sia uguale al id familiare ma il familiare sia comunque il
				// rappresentante)
				if (idRappresentante.equals(c.getID_COMPONENTE())
						|| (codiceRappresentante != null && codiceRappresentante.equals(c.getCF_COMP()))) {
					codiceFiscale = c.getCF_COMP();
					codiceFiscale = codiceFiscale.replaceAll("\\s", "");
					break;
				}
			}
		}

		// adesso che ho il codice fiscale corretto vediamo se è su welfarego.....
		// se c'è no problem lo ritorno...e abbiamo finito questo step.....
		if (adao.findByCodFisc(codiceFiscale) != null) {
			// caso possibile ... dati incompleti su welfarego.... per quanto riguarda la
			// residenza
			AnagrafeSoc ana = adao.findByCodFisc(codiceFiscale);
			if (ana.getLuogoResidenza() == null || Strings.isNullOrEmpty(ana.getLuogoResidenza().getViaText())
					|| Strings.isNullOrEmpty(ana.getLuogoResidenza().getCivicoText())) {
				VistaAnagrafe va = vdao.findByCodiceFiscale(codiceFiscale);
				ana.setLuogoResidenza(new LuogoDao(e).newLuogo(
						MoreObjects.firstNonNull(va.getCodiceStatoResidenza(), Stato.COD_STATO_ITALIA),
						va.getDescrizioneStatoResidenza(), va.getCodiceProvinciaREsidenza(),
						va.getDescrizioneProvinciaResidenza(), va.getCodiceComuneResidenza(),
						va.getDescrizioneComuneResidenza(), va.getCodiceViaResidenza(), va.getDescrizioneViaResidenza(),
						va.getCodiceCivicoResidenza(), va.getDescrizioneCivicoResidenza(), va.getCap()));
				adao.update(ana);
				logger.info("Fatto l'update da anagrafe del luogo di residenza");
			}
			logger.info("CF" + codiceFiscale + "gia presente in welfarego");

			return adao.findByCodFisc(codiceFiscale);
		}

		// in caso contrario cominciamo a cercare in anagrafe.....
		VistaAnagrafe va = vdao.findByCodiceFiscale(codiceFiscale);
		// se il tipo non è nemmeno in anagrafe... c'è qualcosa che non va...
		if (va == null) {
			return null;
		}

		AnagrafeSoc result = new AnagrafeSoc();
		result = vdao.anagrafeComune2AnagrafeWego(va);
		adao.insert(result);
		CartellaSociale cs = new CartellaSociale();
		cs.setCodAna(result.getCodAna());
		cs.setDtApCs(new Date());
		new CartellaDao(e).insert(cs);
		// importiamo anche la famiglia utente
		// dobbiamo sincronizzare la famiglia ....
		AnagrafeFamigliaDao famigliaDao = new AnagrafeFamigliaDao(e);

		// mi prendo la lista dei familiari ( che non sono morti )
		List<VistaAnagrafe> famigliaAnagrafica = vdao
				.findByNumeroFamigliaNonMorti(Integer.valueOf(result.getCodAnaFamCom()));
		// mi prendo la famiglia sociale
		List<AnagrafeFam> famigliaSociale = famigliaDao.getAnagrafeFamListMerge(result);

		for (VistaAnagrafe familiareAnagrafe : famigliaAnagrafica) {
			// per prima cosa evitiamo di pescare se stesso... perchè non so se il dao già
			// si occupi di cio
			if (!Integer.valueOf(result.getCodAnaCom()).equals(familiareAnagrafe.getNumeroIndividuale())) {
				// step 1 vediamo se il parente dell'utente in anagrafe c'è su welfarego
				AnagrafeSoc familiareSoc = adao.findByCodFiscCodAnaCom(familiareAnagrafe.getCodiceFiscale(),
						familiareAnagrafe.getNumeroIndividuale().toString());
				// se il familiare è presente in welfarego vedo se c'è la relazione di parentela
				if (familiareSoc != null) {
					boolean parentelaPresente = false;
					for (AnagrafeFam familiareSociale : famigliaSociale) {
						if (familiareSociale.getAnagrafeSocTarget().getCodAna().equals(familiareSoc.getCodAna())) {
							parentelaPresente = true;
						}

					}
					// se la parentela non c'è e non stiamo tentando di accoppiare l'utente con se
					// stesso .. la creo
					if (!parentelaPresente) {
						AnagrafeFam nuovoMembro = createAnagrafeFam(result.getCodAna(), familiareSoc, e);
						famigliaDao.insert(nuovoMembro);

					}
				}
				// se invece l'utente non è presente in welfarego ..lo creo e poi lo accoppio
				// col parente
				else {
					familiareSoc = copiaVistaInAnagrafe(e, familiareAnagrafe.getNumeroIndividuale());
					adao.insert(familiareSoc);
					AnagrafeFam nuovoMembro = createAnagrafeFam(result.getCodAna(), familiareSoc, e);
					famigliaDao.insert(nuovoMembro);

				}
			}
		}



		return result;
	}

	/**
	 * Importa il delegato dall'anagrafe.
	 * 
	 * @param codFisc
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private AnagrafeSoc importaDelegatoDaAnagrafe(String codFisc, EntityManager e) throws Exception {
		VistaAnagrafeDao vdao = new VistaAnagrafeDao(e);
		AnagrafeSocDao adao = new AnagrafeSocDao(e);
		// in caso contrario cominciamo a cercare in anagrafe.....
		VistaAnagrafe va = vdao.findByCodiceFiscale(codFisc);
		// se il tipo non è nemmeno in anagrafe... c'è qualcosa che non va...
		if (va == null) {
			return null;
		}

		AnagrafeSoc result = vdao.anagrafeComune2AnagrafeWego(va);
		adao.insert(result);
		return result;

	}

	// metodi privati che servono alla creazione della famiglia
	/**
	 * Creazione parentele
	 * 
	 * @param em
	 * @return
	 */
	private ParametriIndata getParente(EntityManager em) {
		ParametriIndataDao dao = new ParametriIndataDao(em);
		ParametriIndata parente = dao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
		return parente;
	}

	/**
	 * metodo per la creazione di un anagrafe familiare
	 * 
	 * @param codAnag
	 * @param anagrafeSociale
	 * @param em
	 * @return
	 * @throws NumberFormatException
	 */
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

	/**
	 * metodo per copiare una
	 * 
	 * @param em
	 * @param numeroIndividuale
	 * @return
	 * @throws Exception
	 */
	private AnagrafeSoc copiaVistaInAnagrafe(EntityManager em, int numeroIndividuale) throws Exception {
		VistaAnagrafeDao vistaAnagraficaDao = new VistaAnagrafeDao(em);
		VistaAnagrafe anagrafeComunale = vistaAnagraficaDao.findByNumeroIndividuale(numeroIndividuale);
		AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
		AnagrafeSoc anagrafeSociale = serializer.vistaAnagraficaToAnagrafeSoc(anagrafeComunale);
		return anagrafeSociale;
	}

	// INZIO SEZIONE CON METODI PER PAI
	/**
	 * Metodo che fa il controllo per vedere se ad un anagrafe sociale è associato
	 * un pai e se no lo crea con i dati provenienti da SIMIA
	 * 
	 * @param r
	 * @param ana
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private Pai simiaToWelfaregoPai(RichiestaCompleta r, AnagrafeSoc ana, EntityManager e) throws Exception {
		PaiDao pdao = new PaiDao(e);
		CartellaDao cdao = new CartellaDao(e);
		UotIndirizzoDao udao = new UotIndirizzoDao(e);
		ParametriIndataDao padao = new ParametriIndataDao(e);
		// caso semplice...c'è già un pai aperto!
		if (pdao.findLastPai(ana.getCodAna()) != null) {
			return pdao.findLastPai(ana.getCodAna());
		}

		CartellaSociale cs = cdao.findByCodAna(ana.getCodAna());
		// creo la cartella sociale se non c'è
		if (cs == null) {
			cs = new CartellaSociale(ana.getCodAna(), new Date());
			cdao.insert(cs);
		}
		Pai pai = new Pai();
		pai.setCartellaSociale(cs);
		pai.setDtApePai(new Date());
		pai.setFlgStatoPai('A');
		pai.setIdParamFascia(padao.findByTipParamDesParam("fa", "-"));
		BigDecimal isee = null;
		Integer numeroMinori = 0;
		for (CoefficientiCalcolo c : r.getCoefficientiCalcolo()) {
			if (c.getCHIAVE().equals(ISEE)) {
				if (c.getVALORE() != null) {
					isee = new BigDecimal(c.getVALORE());
				}
			} else if (c.getCHIAVE().equals(MINORI)) {
				if (c.getVALORE() != null) {
					numeroMinori = Integer.valueOf(c.getVALORE());
				}
			}
		}
		pai.setIsee(isee);
		pai.setNumFigli(numeroMinori);
		pai.setNumNuc(r.getElencoCom().length);
		// individuo la UOT in base all'indirizzo
		if (ana.getLuogoResidenza() != null) {
			String indirizzo = ana.getLuogoResidenza().getIndirizzoTextPerUot();
			String civico = ana.getLuogoResidenza().getCivicoText();
			String sub = null;
			logger.info("civio prima dell' if è" + civico);
			if (civico != null && civico.contains("/")) {
				sub = civico.substring(civico.indexOf("/") + 1);
				civico = civico.substring(0, civico.indexOf("/"));
				logger.info("Civico è " + civico);
				logger.info("Sub è " + sub);
			}
			if (Strings.isNullOrEmpty(civico)) {
				logger.error("civico a null per il seguente nominativo NON POSSO DETERMINARE IN CHE UOT STA "
						+ ana.getCognomeNome());
			}

			List<UotIndirizzo> uot = udao.findByIndirizzo(indirizzo, civico, sub != null ? sub : null);
			if (!uot.isEmpty()) {
				Integer uotnumber = uot.get(0).getUot();
				pai.setIdParamUot(padao.findUot(uotnumber));

			} else {
				logger.error("Non ho trovato nessuna UOT per il seguente indirizzo " + indirizzo
						+ "mettero di default la uot 1");
				pai.setIdParamUot(padao.findUot(1));
			}
		}

		pai.setCodUteAs(new UtentiDao(e).findByCodUte("1"));

		pdao.insert(pai);
		return pai;
	}

	/**
	 * Creazione dell'intervento su welfarego
	 * 
	 * @param r
	 * @param pai
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private PaiIntervento simiaToWelfaregoPaiIntervento(RichiestaCompleta r, Pai pai, EntityManager e, boolean respinto)
			throws Exception {
		PaiInterventoDao pdao = new PaiInterventoDao(e);
		AnagrafeSocDao adao = new AnagrafeSocDao(e);

		PaiIntervento paiIntervento = new PaiIntervento(pai.getCodPai(), "EC100",
				pdao.findMaxCnt(pai.getCodPai(), "EC100").intValue() + 1);
		paiIntervento.setDtApe(formatter.parseDateTime(r.getRichiesta().getDATA_RICEVIMENTO()).toDate());
		Date dataAvvio = findDataAvvio(r.getRichiesta().getDATA_RICEVIMENTO());
		paiIntervento.setDtAvvio(dataAvvio);
		paiIntervento.setDataAvvioProposta(dataAvvio);
		paiIntervento.setDurMesi(12);
		paiIntervento.setPai(pai);
		paiIntervento.setTipologiaIntervento(new TipologiaInterventoDao(e).findByCodTipint("EC100"));

		List<MapDatiSpecificiIntervento> datiSpecificiDaInserire = new ArrayList<MapDatiSpecificiIntervento>();
		MapDatiSpecificiIntervento dsCodice = new MapDatiSpecificiIntervento(pai.getCodPai(),
				paiIntervento.getPaiInterventoPK().getCodTipint(), paiIntervento.getPaiInterventoPK().getCntTipint(),
				"ds_simia");
		dsCodice.setValCampo(r.getRichiesta().getID_RICHIESTA().toString());
		datiSpecificiDaInserire.add(dsCodice);
		MapDatiSpecificiIntervento dsDettPag = new MapDatiSpecificiIntervento(pai.getCodPai(),
				paiIntervento.getPaiInterventoPK().getCodTipint(), paiIntervento.getPaiInterventoPK().getCntTipint(),
				"ds_dett_pagam");
		String s = new StringBuilder().append("IBAN:" + r.getQuadroD().getCCBIBAN() + "\n")
				.append("Istituto:" + r.getQuadroD().getCCBIST() + "\n")
				.append("Intestatario:" + r.getQuadroD().getCCBINT() + "\n")
				.append("Indirizzo intestatario:" + r.getQuadroD().getCCBDIP() + "\n")
				.append("CC/Postale:" + r.getQuadroD().getCCPNUM() + "\n")
				.append("Intestarario cc postale:" + r.getQuadroD().getCCPINT() + "\n")
				.append("Altro:" + r.getQuadroD().getDIVMOD()).toString();
		dsDettPag.setValCampo(s);
		datiSpecificiDaInserire.add(dsDettPag);

		// facciamo un check del beneficiario
		Integer modalitaPagamento = r.getQuadroD().getMODADD();

		if (modalitaPagamento != null) {
			MapDatiSpecificiIntervento dsModPag = new MapDatiSpecificiIntervento(pai.getCodPai(),
					paiIntervento.getPaiInterventoPK().getCodTipint(),
					paiIntervento.getPaiInterventoPK().getCntTipint(), "ds_tip_pagam");
			String codFiscBeneficiario = r.getQuadroD().getCF_BEN();
			AnagrafeSoc beneficiario = null;
			if (codFiscBeneficiario != null) {
				if (adao.findByCodFisc(codFiscBeneficiario) == null) {
					beneficiario = importaDelegatoDaAnagrafe(codFiscBeneficiario, entityManager);

				} else {
					beneficiario = adao.findByCodFisc(codFiscBeneficiario);
				}
			}

			// modalita pagamento contati...beneficiario uguale al richiedente.
			if (modalitaPagamento.equals(2)) {
				dsModPag.setCodValCampo("01");
				paiIntervento.setDsCodAnaBenef(pai.getAnagrafeSoc() != null ? pai.getAnagrafeSoc() : beneficiario);
			}
			// se la modalita pagamento è in cc bancario o postale.
			else if (modalitaPagamento.equals(0) || modalitaPagamento.equals(1)) {
				// dati specifici che possono aiutare l'amministrativo a capire chi sia il
				// delegato giusto

				// cerchiamo il delegato

				String iban = modalitaPagamento.equals(1) == true ? r.getQuadroD().getCCBIBAN()
						: r.getQuadroD().getCCPNUM();
				// AnagrafeSoc beneficiario = null;
				paiIntervento.setIbanDelegato(iban);

				if (beneficiario == null) {
					logger.error("non c'è beneficiario ");
					// metto comunque senza delega... e metto l'iban comunque
					dsModPag.setCodValCampo("03");

				} else {
					if (pai.getAnagrafeSoc() != null
							&& beneficiario.getCodAna().equals(pai.getAnagrafeSoc().getCodAna())) {
						dsModPag.setCodValCampo("03");
					} else {
						dsModPag.setCodValCampo("04");
					}
					paiIntervento.setDsCodAnaBenef(beneficiario);
				}

			}
			datiSpecificiDaInserire.add(dsModPag);

		} else {
			paiIntervento.setDsCodAnaBenef(null);
		}

		Integer numeroMinori = 0;
		Integer indiceTabella = 1;
		for (CoefficientiCalcolo c : r.getCoefficientiCalcolo()) {
			if (c.getCHIAVE().equals(SCAGLIONE)) {
				if (c.getVALORE() != null) {
					indiceTabella = Integer.valueOf(c.getVALORE());
				}
			} else if (c.getCHIAVE().equals(MINORI)) {
				if (c.getVALORE() != null) {
					numeroMinori = Integer.valueOf(c.getVALORE());
					numeroMinori = numeroMinori > 2 == true ? 2 : numeroMinori;
				}
			}

		}
		logger.info("Indice tabella ritornato da SIMIA -->" + indiceTabella);
		if (indiceTabella != 0) {
			paiIntervento.setQuantita(calcolo[indiceTabella - 1][numeroMinori]);
		} else {
			paiIntervento.setQuantita(BigDecimal.ZERO);
		}
		paiIntervento.setApprovazioneTecnica('N');
		paiIntervento.setUrgente('N');

		paiIntervento.setMapDatiSpecificiInterventoList(datiSpecificiDaInserire);
		if (respinto) {
			paiIntervento.setStatoAttuale(PaiIntervento.RESPINTO);
			paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
			paiIntervento.setDtChius(new Date());

		} else {
			paiIntervento.setStatoAttuale(PaiIntervento.GESTIONE_ECONOMICA);
			paiIntervento.setDtEsec(new Date());

		}
		pdao.insert(paiIntervento);

		return paiIntervento;

	}

	/**
	 * Trova la data di avvio dell'intervento.
	 * 
	 * @param dataRicevimento
	 * @return
	 */
	private Date findDataAvvio(String dataRicevimento) {

		DateTime dataRicevimentoFormatted = formatter.parseDateTime(dataRicevimento);
		int month = dataRicevimentoFormatted.getMonthOfYear();
		if (month == 12 || month == 11) {
			return new DateTime(dataRicevimentoFormatted.getYear() + 1, 1, 1, 0, 0).toDate();
		} else if ((month % 2) == 0) {
			return new DateTime(dataRicevimentoFormatted.getYear(), month + 1, 1, 0, 0).toDate();
		} else {
			return new DateTime(dataRicevimentoFormatted.getYear(), month + 2, 1, 0, 0).toDate();
		}

	}

	/**
	 * Aggiorna una richiesta poichè qualcosa è cambiato
	 * 
	 * @param r
	 * @param intervento
	 * @param e
	 * @throws Exception
	 */
	private void updateInterventoPrecedente(RichiestaCompleta r, PaiIntervento intervento, EntityManager e)
			throws Exception {
		Integer numeroMinori = 0;
		Integer indiceTabella = 1;
		BigDecimal isee = null;
		PaiDao pdao = new PaiDao(e);
		Pai pai = pdao.findPai(intervento.getPaiInterventoPK().getCodPai());
		for (CoefficientiCalcolo c : r.getCoefficientiCalcolo()) {
			if (c.getCHIAVE().equals(SCAGLIONE)) {
				if (c.getVALORE() != null) {
					indiceTabella = Integer.valueOf(c.getVALORE());
				}
			} else if (c.getCHIAVE().equals(MINORI)) {
				if (c.getVALORE() != null) {
					numeroMinori = Integer.valueOf(c.getVALORE());
					numeroMinori = numeroMinori > 2 == true ? 2 : numeroMinori;
				}
			} else if (c.getCHIAVE().equals(ISEE)) {
				if (c.getVALORE() != null) {
					isee = new BigDecimal(c.getVALORE());
				}
			}

		}
		intervento.setDatiOriginali(
				"Intervento modificato da SIMIA. QUantità prima delle modifica" + intervento.getQuantita());
		intervento.setQuantita(calcolo[indiceTabella - 1][numeroMinori]);
		intervento.setDatiOriginali(intervento.getDatiOriginali() + " dopo la modifica " + intervento.getQuantita()
				+ "In data" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		pai.setNumFigli(numeroMinori);
		pai.setNumNuc(r.getElencoCom().length);

		Date dataCambio = null;
		for (int i = 0; i < r.getAnomalieRichiesta().length; i++) {
			AnomalieRichiesta anal = r.getAnomalieRichiesta(i);
			if (anal.getDESCRIZIONE().startsWith("L'attestazione ISEE")) {
				String descrizione = anal.getDESCRIZIONE();
				String dataStr = descrizione.substring(descrizione.lastIndexOf("a") + 2);

				dataCambio = findDataAvvio(dataStr);

				pai.setIsee(isee);
				pai.setDtScadIsee(new SimpleDateFormat("dd/MM/yyyy").parse(dataStr));

				break;
			}

		}

		if (dataCambio == null) {
			dataCambio = new Date();
			dataCambio = findDataAvvio(new SimpleDateFormat("dd/MM/yyyy").format(dataCambio));

		}
		new PaiInterventoDao(e).update(intervento);
		// TODO togliere il servizio non sarà più cosi
		new PaiInterventoMeseDao(e).aggiornaQuantita(intervento.getPaiInterventoPK().getCodPai(),
				intervento.getPaiInterventoPK().getCodTipint(), intervento.getPaiInterventoPK().getCntTipint(),
				calcolo[indiceTabella - 1][numeroMinori], dataCambio);
		pdao.update(pai);
	}

}
