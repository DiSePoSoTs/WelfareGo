/**
 * 
 */
package it.wego.welfarego.importazione.servlet;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.commons.utils.ImportazioneUtils;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.*;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Fabio Bonaccorso Servlet per l'importazione automatizzata di
 *         interventi vecchi.
 *
 */
@SuppressWarnings("deprecation")
public class ImportazioneForm extends JsonServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String CODICE_FISCALE = "Codice fiscale", DATA_PARTENZA = "Data di partenza",
			DATA_FINE = "Data di fine", DURATA_MESI = "Durata mesi", QUANTITA = "Quantita", ID = "id",
			PERIODO = "periodo", COD_FISC_DELEGATO = "Codice fiscale delegato", IBAN_DELEGATO = "Iban pagamenti",
			ANNO = "anno", DA_GENERARE = "Da generare", IMPEGNO = "impegno", ANNO_BUDGET = "anno budget",
			STATO_INTERVENTO = "stato";

	// indica l'ultima cella a non contenere un dato non specifico
	private final static int fineDatiStandard = 7;

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		EntityManager entityManager = Connection.getEntityManager();
		String tipoParametro = getParameter("tipo_intervento");
		String tipoImportazione = getParameter("tipo_import");

		byte[] fileData = Base64.decodeBase64(getParameter("file" + MULTIPART_BASE64));
		File temp = File.createTempFile("Importazione", ".xls");
		FileUtils.writeByteArrayToFile(temp, fileData);
		Preconditions.checkNotNull(fileData);
		Preconditions.checkNotNull(Strings.emptyToNull(tipoParametro));
		Preconditions.checkNotNull(Strings.emptyToNull(tipoImportazione));
		Map<Integer, String> campiHeader = new HashMap<Integer, String>();
		HSSFWorkbook xlsWorkbook = new HSSFWorkbook(new FileInputStream(temp));
		try {
			HSSFSheet xlsSheet = xlsWorkbook.getSheetAt(0);
			Row primaRiga = xlsSheet.getRow(0);
			// mi prendo tutti gli header della prima riga in modo da avere l'ordine dei
			// dati
			for (int cn = 0; cn < primaRiga.getLastCellNum(); cn++) {
				Cell c = primaRiga.getCell(cn);
				if (c == null || c.getCellType() == CellType.BLANK) {
					// Can't be this cell - it's empty
					continue;
				}
				if (c.getCellType() == CellType.STRING) {
					String valore = c.getStringCellValue();
					campiHeader.put(cn, valore);
					getLogger().info("Cella header" + valore);
				}

			}

			TipologiaIntervento tipInt = new TipologiaInterventoDao(entityManager).findByCodTipint(tipoParametro);
			if (tipoImportazione.equals("P")) {
				importaPagamenti(xlsSheet, campiHeader, entityManager, tipInt);
			} else {
				importaInterventi(xlsSheet, campiHeader, entityManager, tipInt);

			}
			getLogger().info("CHiamato correttamente il metodo che dovremo implementare" + tipoParametro);
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=" + temp.getName());
			xlsWorkbook.write(response.getOutputStream());
			// return JsonBuilder.newInstance().withSuccess(true).buildResponse();
			return SKIP_RESPONSE;
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Attenzione errore nel caricamento file " + e.getMessage());
			return JsonBuilder.newInstance().withMessage(
					"Errore nel caricamento del file. Assicurarsi che il formato del file sia XLS. Messaggio di errore: "
							+ e.getMessage())
					.withSuccess(false).buildResponse();
		}

	}

	/**
	 * Metodo per l'importazione dei pagamenti
	 * 
	 * @param xlsSheet
	 * @param campiHeader
	 * @param entityManager
	 * @param tipInt
	 * @return
	 */
	private HSSFSheet importaPagamenti(HSSFSheet xlsSheet, Map<Integer, String> campiHeader,
			EntityManager entityManager, TipologiaIntervento tipInt) {
		Iterator<Row> rowIterator = xlsSheet.iterator();
		PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
		PaiInterventoMeseDao pmdao = new PaiInterventoMeseDao(entityManager);
		MapDatiSpecificiInterventoDao mdao = new MapDatiSpecificiInterventoDao(entityManager);
		BudgetTipoInterventoDao bdao = new BudgetTipoInterventoDao(entityManager);
		// skippiamo l'header.
		if(rowIterator.hasNext())
			rowIterator.next();
		
		while (rowIterator.hasNext()) {
			Row riga = rowIterator.next();
			String errore = "OK";
			PaiIntervento intervento = null;
			BigDecimal quantita = BigDecimal.ZERO;
			Short annoBudget = null;
			String impegnoBudget = null;

			List<Periodo> periodi = new ArrayList<Periodo>();
			for (int cn = 0; cn < riga.getLastCellNum(); cn++) {
				Cell cella = riga.getCell(cn);
				// qui faremo coseeeeeeeee
				// per prima cosa controllo che la riga non sia già in errore.. in questo caso
				// inutile continuare
				if (errore.equals("OK")) {
					String tipoCella = campiHeader.get(cn);
					// cella id
					if (tipoCella.equals(ID)) {
						String id = cella.getStringCellValue();
						if (Strings.isNullOrEmpty(id)) {
							errore = "ATTENZIONE STRINGA ID VUOTA";
							break;
						}
						// id welfarego
						if (id.contains("/")) {
							String[] parts = id.split("/");
							String pai = parts[0];
							String cnt = parts[1];
							intervento = pdao.findByKey(Integer.valueOf(pai), tipInt.getCodTipint(),
									Integer.valueOf(cnt));

						}
						// id simia
						else {
							try {
								intervento = mdao.findInterventoSimia(id).getPaiIntervento();
							} catch (Exception e) {
								e.printStackTrace();
								errore = "Attenzione più interventi SIMIA trovati per questo id " + id;
								break;
							}
						}
						if (intervento == null) {
							errore = "NON È STATO TROVATO ALCUN INTERVENTO ASSOCIATO A QUESTO ID ";
							break;
						}
					}
					// fine controllo cella id
					else if (tipoCella.equals(CODICE_FISCALE)) {
						String codiceFiscale = cella.getStringCellValue();
						if (codiceFiscale.length() != 16) {
							errore = "CODICE FISCALE DI LUNGHEZZA NON CORRETTA";
							break;
						}
						if (!intervento.getPai().getAnagrafeSoc().getCodFisc().equals(codiceFiscale)) {
							errore = "CODICE FISCALE NON CORRISPONDENTE AL CODICE FISCALE DEL PAI";
							break;
						}
					}
					// fine controllo codice fiscale
					else if (tipoCella.equals(PERIODO)) {
						String periodiString = cella.getStringCellValue();
						if (Strings.isNullOrEmpty(periodiString)) {
							errore = "ATTENZIONE CAMPO PERIODO VUOTO";
							break;
						}
						// bimestre
						if (periodiString.contains("-")) {
							String[] parts = periodiString.split("-");
							Periodo mese1 = new Periodo();
							mese1.setMese(parts[0]);
							Periodo mese2 = new Periodo();
							mese2.setMese(parts[1]);
							periodi.add(mese1);
							periodi.add(mese2);

						}
						// mese unico
						else {
							Periodo mese = new Periodo();
							mese.setMese(periodiString);
							periodi.add(mese);
						}
					}
					// fine controllo periodo
					else if (tipoCella.equals(ANNO)) {
						String annoString = cella.getStringCellValue();
						if (Strings.isNullOrEmpty(annoString)) {
							errore = "ATTENZIONE CAMPO ANNO VUOTO";
							break;
						}
						// ci sono più anni (bimestre a scavalco)
						if (annoString.contains("-")) {
							String[] parts = annoString.split("-");
							periodi.get(0).setAnno(parts[0]);
							periodi.get(1).setAnno(parts[1]);
						}
						// singolo anno
						else {
							for (int i = 0; i < periodi.size(); i++) {
								periodi.get(i).setAnno(annoString);
							}
						}
					}
					// fine controllo anno
					else if (tipoCella.equals(QUANTITA)) {
						try {
							String quantità = NumberToTextConverter.toText(cella.getNumericCellValue());
							quantita = new BigDecimal(quantità);
						} catch (Exception e) {
							e.printStackTrace();
							errore = "PROBLEMI NELLA LETTURA DELLA QUANTITA";
							break;
						}
					}

					// fine controllo quantita
					else if (tipoCella.equals(IMPEGNO)) {
						impegnoBudget = cella.getStringCellValue();
						if (Strings.isNullOrEmpty(impegnoBudget)) {
							errore = "ATTENZIONE CAMPO IMPEGNO BUDGET  VUOTO";
							break;

						}
					} else if (tipoCella.equals(ANNO_BUDGET)) {

						annoBudget = (short) cella.getNumericCellValue();
						if (annoBudget == 0) {
							errore = "ATTENZIONE CAMPO ANNO  VUOTO";
							break;

						}
					}

					// fine controllo quantita
					else if (tipoCella.equals(DA_GENERARE)) {

						// ora che ho tutti gli elemtni posso crerare i PIM
						if (periodi.size() == 0) {
							errore = "ATTENZIONE NON CI SONO PERIODI VERIFICARE LA CORRETTEZZA DELLE INFO";
							break;
						}
						for (int i = 0; i < periodi.size(); i++) {
							BudgetTipIntervento budget = bdao.findByKey(tipInt.getCodTipint(), annoBudget.toString(),
									impegnoBudget);
							if (budget == null) {
								errore = "BUDGET PER QUEST'ANNO E IMPEGNO NON TROVATO PER L'INTERVENTO CORRENTE";
								break;
							}
							boolean update = true;
							PaiInterventoMese pim = null;
							pim = pmdao.findByKey(Integer.valueOf(intervento.getPai().getCodPai()),
									tipInt.getCodTipint(), intervento.getPaiInterventoPK().getCntTipint(),
									Short.valueOf(periodi.get(i).getAnno()), Short.valueOf(periodi.get(i).getMese()),
									budget.getBudgetTipInterventoPK().getCodAnno(),
									budget.getBudgetTipInterventoPK().getCodImpe());
							if (pim == null) {

								pim = new PaiInterventoMese(Integer.valueOf(intervento.getPai().getCodPai()),
										tipInt.getCodTipint(), intervento.getPaiInterventoPK().getCntTipint(),
										Short.valueOf(periodi.get(i).getAnno()),
										Short.valueOf(periodi.get(i).getMese()),
										budget.getBudgetTipInterventoPK().getCodAnno(),
										budget.getBudgetTipInterventoPK().getCodImpe());
								update = false;
							}

							pim.setBdgPrevEur(quantita);
							pim.setBdgPrevQta(quantita);
							if (cella.getStringCellValue().equals("SI")) {
								pim.setBdgConsEur(quantita);
								pim.setBdgConsQta(quantita);
							}
							pim.setFlgProp('N');
							try {
								if (update == true) {
									pmdao.update(pim);
									getLogger().info("PIM ggiornato  per" + pim.getPaiInterventoMesePK().toString());
								} else {
									pmdao.insert(pim);
									getLogger().info("PIM inserito  per" + pim.getPaiInterventoMesePK().toString());
								}
							} catch (Exception e) {
								e.printStackTrace();
								errore = "ERRORE AL MOMENTO DEL SALVATAGGIO";
								break;
							}

						}
					} else {
						errore = "ATTENZIONE IL CAMPO " + tipoCella
								+ "NON È STATO TROVATO FRA GLI HEADER DEL PAGAMENTO:CONTROLLARE LA CORRETTEZZA DELL INTESTAZIONE DEL FILE";
					}

				}

			}
			Cell cell = riga.createCell(riga.getLastCellNum());
			cell.setCellType(CellType.STRING);
			cell.setCellValue(errore);
		}
		getLogger().info("Importazione pagamenti completata");
		return xlsSheet;
	}

	/**
	 * metodo di importazione degli interventi.
	 * 
	 * @param xlsSheet
	 * @param campiHeader
	 * @param entityManager
	 * @param tipInt
	 * @return
	 */
	private HSSFSheet importaInterventi(HSSFSheet xlsSheet, Map<Integer, String> campiHeader,
			EntityManager entityManager, TipologiaIntervento tipInt) {
		DatiSpecificiDao ddao = new DatiSpecificiDao(entityManager);
		Iterator<Row> rowIterator = xlsSheet.iterator();
		// skippiamo l'header.
		if(rowIterator.hasNext())
			rowIterator.next();
		
		while (rowIterator.hasNext()) {
			Row riga = rowIterator.next();
			AnagrafeSoc utente = null;
			Pai paiUtente = null;
			PaiIntervento intervento = new PaiIntervento();

			String errore = "OK";
			String paiCreato = "";
			for (int cn = 0; cn < riga.getLastCellNum(); cn++) {
				Cell cella = riga.getCell(cn);
				// qui faremo coseeeeeeeee
				// per prima cosa controllo che la riga non sia già in errore.. in questo caso
				// inutile continuare
				if (errore.equals("OK")) {

					String tipoCella = campiHeader.get(cn);
					// controllo che sia una cella di dati standard e non dati specifici
					if (cn <= fineDatiStandard) {
						// significa che è una cella dei dati standard
						// codice fiscale
						if (tipoCella.equals(CODICE_FISCALE)) {
							String codiceFiscale = cella.getStringCellValue();
							if (codiceFiscale.length() != 16) {
								errore = "CODICE FISCALE DI LUNGHEZZA NON CORRETTA";
								break;
							}
							// FASE UNO CONTROLLIAMO SE ESISTE IN WELFAREGO E IN CASO IMPORTIAMO DA ANAGRAFE
							try {
								utente = fileToAnagrafeSoc(codiceFiscale, entityManager);
							} catch (Exception e) {
								errore = "PROBLEMI NELLA CREAZIONE UTENTE" + e.getMessage();
								break;
							}
							if (utente == null) {
								errore = "CODICE FISCALE NON TROVATO NE IN ANAGRAFE NE IN WELFAREGO";
								break;
							} // fine fase 2
								// FASE 2 se non ha un pai aperto lo apriamo.
							try {
								PaiDao pdao = new PaiDao(entityManager);
								// caso semplice...c'è già un pai aperto!
								if (pdao.findLastPai(utente.getCodAna()) != null) {
									paiUtente = pdao.findLastPai(utente.getCodAna());

									getLogger().info("Pai già presente ");
								} else {
									paiUtente = fileToWelfaregoPai(utente, entityManager);
									paiCreato = "APERTO NUOVO PAI";
									getLogger().info("creazione pai  PAI avvenuta correttamente");
								}

							} catch (Exception e) {
								e.printStackTrace();
								errore = "ERRORE NELLA CREAZIONE DEL PAI" + e.getMessage();
								break;
							} // fine fase 2

						} // fine operazioni codice fiscale
							// data di partenza
						else if (tipoCella.equals(DATA_PARTENZA)) {
							try {
								if (cella.getDateCellValue() == null) {
									errore = "DATA DI PARTENZA NULLA O NON CORRETTA";
								}
								intervento.setDataAvvioProposta(cella.getDateCellValue());
								intervento.setDtAvvio(cella.getDateCellValue());
								intervento.setDataRichiestaApprovazione(cella.getDateCellValue());
								intervento.setDtApe(cella.getDateCellValue());

							} catch (Exception e) {
								e.printStackTrace();
								errore = "LA DATA DI PARTENZA NON È IN FORMATO DATA";
								break;
							}

						} else if (tipoCella.equals(DATA_FINE)) {
							if (tipInt.getFlgFineDurata() == TipologiaIntervento.FLG_FINE_DURATA_F) {
								try {
									intervento.setDtFine(cella.getDateCellValue());
								} catch (Exception e) {
									e.printStackTrace();
									errore = "LA DATA DI FINE NON È IN FORMATO DATA";
									break;
								}
							}
						} else if (tipoCella.equals(STATO_INTERVENTO)) {
							String statoStringa = cella.getStringCellValue();
							if (Strings.isNullOrEmpty(statoStringa)) {
								errore = "ATTENZIONE STATO DELL'INTERVENTO NON SPECIFICATO";
								break;
							}
							char stato = statoStringa.charAt(0);
							intervento.setStatoInt(stato);

						}

						else if (tipoCella.equals(DURATA_MESI)) {
							if (tipInt.getFlgFineDurata() == TipologiaIntervento.FLG_FINE_DURATA_D) {
								try {
									intervento.setDurMesi((int) (cella.getNumericCellValue()));
								} catch (Exception e) {
									e.printStackTrace();
									errore = "ATTENZIONE LA DURATA MESI NON È UN NUMERO";
									break;
								}
							}
						} else if (tipoCella.equals(QUANTITA)) {
							try {
								String quantità = NumberToTextConverter.toText(cella.getNumericCellValue());
								intervento.setQuantita(new BigDecimal(quantità));
							} catch (Exception e) {
								e.printStackTrace();
								errore = "PROBLEMI NELLA LETTURA DELLA QUANTITA";
								break;
							}

						} else if (tipoCella.equals(COD_FISC_DELEGATO)) {
							if (cella != null) {
								String codiceFiscale = cella.getStringCellValue();
								if (!Strings.isNullOrEmpty(codiceFiscale)) {
									try {
										AnagrafeSoc delegato = definisciDelegato(codiceFiscale, entityManager);
										intervento.setDsCodAnaBenef(delegato);
									} catch (Exception e) {
										errore = "PROBLEMI NELL IMPORT DELEGATO";
										e.printStackTrace();
										break;
									}
								} else {
									intervento.setDsCodAnaBenef(paiUtente.getAnagrafeSoc());
								}

							} else {
								intervento.setDsCodAnaBenef(paiUtente.getAnagrafeSoc());
							}

						} else if (tipoCella.equals(IBAN_DELEGATO)) {
							if (cella != null) {
								String iban = cella.getStringCellValue();
								if (!Strings.isNullOrEmpty(iban)) {
									intervento.setIbanDelegato(iban);
								}
							}

						} else {
							errore = "ATTENZIONE IL CAMPO " + tipoCella
									+ "NON È STATO TROVATO FRA GLI HEADER DELL'INTERVENTO:CONTROLLARE LA CORRETTEZZA DELL INTESTAZIONE DEL FILE";
						}
						// se il contatore è maggiore di 4 significa che abbiamo esaurito i campi
						// header... possiamo provare a inserire il nostro intervento
						if (cn == fineDatiStandard && errore.equals("OK")) {
							getLogger().info("Provvediamo a inserire l'intervento");
							try {
								inserisciIntervento(intervento, paiUtente, tipInt, entityManager);
								inserisciEventoImportazione(intervento, entityManager);
								getLogger().info("Intervento correttamente inserito");
							} catch (Exception e) {
								e.printStackTrace();
								errore = "ERRORE NELL'INSERIMENTO INTERVENTO";
								break;

							}
						}

					}
					// inseriamo dati specifici.
					else {
						DatiSpecifici dato = ddao.findByCodCampo(tipoCella);
						if (dato == null) {
							errore = "ATTENZIONE IL DATO SPECIFICO " + tipoCella + " NON ESISTE IN DATABASE";
							break;
						} else {
							try {
								inserisciDatoSpecifico(dato, cella, intervento, entityManager);
							} catch (Exception e) {
								errore = "ATTENZIONE PROBLEMI NELL'INSERIMENTO DEL DATO SPECIFICO" + tipoCella;
							}
						}

					}
				}

			}

			Cell cell = riga.createCell(riga.getLastCellNum());
			cell.setCellType(CellType.STRING);
			cell.setCellValue(errore);
			Cell paiCell = riga.createCell(riga.getLastCellNum());
			paiCell.setCellType(CellType.STRING);
			paiCell.setCellValue(paiCreato);

		}
		return xlsSheet;
	}

	/**
	 * Metodo che cerca dal codice fiscale se l'utente è in welfarego e se no lo
	 * importa da anagrafe
	 * 
	 * @param codiceFiscale
	 * @param em
	 * @return
	 * @throws Exception
	 */
	private AnagrafeSoc fileToAnagrafeSoc(String codiceFiscale, EntityManager em) throws Exception {
		AnagrafeSocDao adao = new AnagrafeSocDao(em);
		VistaAnagrafeDao vdao = new VistaAnagrafeDao(em);
		if (adao.findByCodFisc(codiceFiscale) != null) {
			// caso possibile ... dati incompleti su welfarego.... per quanto riguarda la
			// residenza
			AnagrafeSoc ana = adao.findByCodFisc(codiceFiscale);
			if (ana.getLuogoResidenza() == null || Strings.isNullOrEmpty(ana.getLuogoResidenza().getViaText())
					|| Strings.isNullOrEmpty(ana.getLuogoResidenza().getCivicoText())) {
				VistaAnagrafe va = vdao.findByCodiceFiscale(codiceFiscale);
				ana.setLuogoResidenza(new LuogoDao(em).newLuogo(
						MoreObjects.firstNonNull(va.getCodiceStatoResidenza(), Stato.COD_STATO_ITALIA),
						va.getDescrizioneStatoResidenza(), va.getCodiceProvinciaREsidenza(),
						va.getDescrizioneProvinciaResidenza(), va.getCodiceComuneResidenza(),
						va.getDescrizioneComuneResidenza(), va.getCodiceViaResidenza(), va.getDescrizioneViaResidenza(),
						va.getCodiceCivicoResidenza(), va.getDescrizioneCivicoResidenza(), va.getCap()));
				adao.update(ana);
				getLogger().info("Fatto l'update da anagrafe del luogo di residenza");
			}
			getLogger().info("IMPORTAZIONE CF" + codiceFiscale + "gia presente in welfarego");

			return adao.findByCodFisc(codiceFiscale);
		}

		// in caso contrario cominciamo a cercare in anagrafe.....
		VistaAnagrafe va = vdao.findByCodiceFiscale(codiceFiscale);
		// se il tipo non è nemmeno in anagrafe... c'è qualcosa che non va...
		if (va == null) {
			return null;
		}
		// e.getTransaction().begin();
		AnagrafeSoc result = vdao.anagrafeComune2AnagrafeWego(va);
		adao.insert(result);
		CartellaSociale cs = new CartellaSociale();
		cs.setCodAna(result.getCodAna());
		cs.setDtApCs(new Date());
		new CartellaDao(em).insert(cs);
		// importiamo anche la famiglia utente
		// dobbiamo sincronizzare la famiglia ....
		AnagrafeFamigliaDao famigliaDao = new AnagrafeFamigliaDao(em);

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
						AnagrafeFam nuovoMembro = createAnagrafeFam(result.getCodAna(), familiareSoc, em);
						famigliaDao.insert(nuovoMembro);

					}
				}
				// se invece l'utente non è presente in welfarego ..lo creo e poi lo accoppio
				// col parente
				else {
					familiareSoc = copiaVistaInAnagrafe(em, familiareAnagrafe.getNumeroIndividuale());
					adao.insert(familiareSoc);
					AnagrafeFam nuovoMembro = createAnagrafeFam(result.getCodAna(), familiareSoc, em);
					famigliaDao.insert(nuovoMembro);

				}
			}
		}
		return result;

	}

	/**
	 * Importa il delegato dall'anagrafe oppure controlla che ci sia inw elfarego .
	 * 
	 * @param codFisc
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private AnagrafeSoc definisciDelegato(String codFisc, EntityManager e) throws Exception {
		VistaAnagrafeDao vdao = new VistaAnagrafeDao(e);
		AnagrafeSocDao adao = new AnagrafeSocDao(e);
		// primo heck vediamo se è gia in db
		AnagrafeSoc anagrafeSoc = adao.findByCodFisc(codFisc);
		if (anagrafeSoc != null) {
			return anagrafeSoc;
		}
		// in caso contrario cominciamo a cercare in anagrafe.....
		VistaAnagrafe va = vdao.findByCodiceFiscale(codFisc);
		// se il tipo non è nemmeno in anagrafe... c'è qualcosa che non va...
		if (va == null) {
			return null;
		}
		// e.getTransaction().begin();
		anagrafeSoc = vdao.anagrafeComune2AnagrafeWego(va);
		adao.insert(anagrafeSoc);
		return anagrafeSoc;

	}

	private AnagrafeSoc copiaVistaInAnagrafe(EntityManager em, int numeroIndividuale) throws Exception {
		VistaAnagrafeDao vistaAnagraficaDao = new VistaAnagrafeDao(em);
		VistaAnagrafe anagrafeComunale = vistaAnagraficaDao.findByNumeroIndividuale(numeroIndividuale);
		ImportazioneUtils utils = new ImportazioneUtils(em);
		AnagrafeSoc anagrafeSociale = utils.vistaAnagraficaToAnagrafeSoc(anagrafeComunale);
		return anagrafeSociale;
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
	 * 
	 * @param ana
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private Pai fileToWelfaregoPai(AnagrafeSoc ana, EntityManager e) throws Exception {
		PaiDao pdao = new PaiDao(e);
		CartellaDao cdao = new CartellaDao(e);
		UotIndirizzoDao udao = new UotIndirizzoDao(e);
		ParametriIndataDao padao = new ParametriIndataDao(e);

		// e.getTransaction().begin();
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

		pai.setIsee(BigDecimal.ZERO);
		pai.setNumFigli(0);
		pai.setNumNuc(0);
		// individuo la UOT in base all'indirizzo
		if (ana.getLuogoResidenza() != null) {
			String indirizzo = ana.getLuogoResidenza().getIndirizzoTextPerUot();
			String civico = ana.getLuogoResidenza().getCivicoText();
			String sub = null;
			getLogger().info("civio prima dell' if è" + civico);
			if (civico != null && civico.contains("/")) {
				sub = civico.substring(civico.indexOf("/") + 1);
				civico = civico.substring(0, civico.indexOf("/"));
				getLogger().info("Civico è " + civico);
				getLogger().info("Sub è " + sub);
			}
			if (Strings.isNullOrEmpty(civico)) {
				getLogger().error("civico a null per il seguente nominativo NON POSSO DETERMINARE IN CHE UOT STA "
						+ ana.getCognomeNome());
			}

			List<UotIndirizzo> uot = udao.findByIndirizzo(indirizzo, civico, sub);
			if (!uot.isEmpty()) {
				Integer uotnumber = uot.get(0).getUot();
				pai.setIdParamUot(padao.findUot(uotnumber));

			} else {
				getLogger().error("Non ho trovato nessuna UOT per il seguente indirizzo " + indirizzo
						+ "mettero di default la uot 1");
				pai.setIdParamUot(padao.findUot(1));
			}
		}

		pai.setCodUteAs(new UtentiDao(e).findByCodUte("1"));

		pdao.insert(pai);
		// e.getTransaction().commit();

		return pai;
	}

	/**
	 * Inserimento dell'intervento importato in database.
	 * 
	 * @param intervento
	 * @param pai
	 * @param tipInt
	 * @param em
	 * @throws Exception
	 */
	private void inserisciIntervento(PaiIntervento intervento, Pai pai, TipologiaIntervento tipInt, EntityManager em)
			throws Exception {
		AssociazioneDao associazioneDao = new AssociazioneDao(em);
		CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
		PaiInterventoDao pidao = new PaiInterventoDao(em);
		TipologiaInterventoDao tipologiaInterventoDao = new TipologiaInterventoDao(em);
		TipologiaIntervento tipologiaIntervento = tipologiaInterventoDao.findByCodTipint(tipInt.getCodTipint());
		PaiInterventoPK chiave = new PaiInterventoPK(pai.getCodPai(), tipInt.getCodTipint(),
				pidao.findMaxCnt(pai.getCodPai(), tipInt.getCodTipint()).intValue() + 1);
		intervento.setPaiInterventoPK(chiave);
		intervento.setPai(pai);
		intervento.setTipologiaIntervento(tipologiaIntervento);

		intervento.setUrgente('N');
		intervento.setCostoPrev(calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(intervento)));
		Associazione associazione = associazioneDao.findById(1); // comune di trieste
		intervento.setAssociazione(associazione);

		if (intervento.getStatoInt() == 'E') {
			intervento.setStatoAttuale(PaiIntervento.GESTIONE_ECONOMICA);
			intervento.setDtEsec(new Date());
		}
		if (intervento.getStatoInt() == 'A') {
			intervento.setStatoAttuale(PaiIntervento.APERTO);
		}
		if (intervento.getStatoInt() == 'C') {
			intervento.setStatoAttuale(PaiIntervento.CHIUSO);
			intervento.setDtChius(intervento.calculateDtFine());
			intervento.setDtEsec(new Date());
		}

		pidao.insert(intervento);

	}

	/**
	 * Inserisce l'evento per indicare che l'intervento è stato importato
	 * 
	 * @param pi
	 * @param em
	 */
	private void inserisciEventoImportazione(PaiIntervento pi, EntityManager em) {
		PaiEventoDao pedao = new PaiEventoDao(em);
		pedao.insertEvento(pi, "Importazione intervento");
	}

	private void inserisciDatoSpecifico(DatiSpecifici d, Cell cella, PaiIntervento p, EntityManager em)
			throws Exception {
		MapDatiSpecificiInterventoDao mdao = new MapDatiSpecificiInterventoDao(em);
		MapDatiSpecificiIntervento datoSpecificoIntervento = new MapDatiSpecificiIntervento(
				p.getPaiInterventoPK().getCodPai(), p.getPaiInterventoPK().getCodTipint(),
				p.getPaiInterventoPK().getCntTipint(), d.getCodCampo());

		char tipoCampo = d.getTipoCampo();

		try {
			if (tipoCampo == DatiSpecifici.DS_TESTO || tipoCampo == DatiSpecifici.DS_TESTO_MULTI) {

				String valore = cella.getStringCellValue();
				datoSpecificoIntervento.setValCampo(valore);

			}
			if (tipoCampo == DatiSpecifici.DS_NUM) {
				String valore = NumberToTextConverter.toText(cella.getNumericCellValue());
				datoSpecificoIntervento.setValCampo(valore);
			}
			if (tipoCampo == DatiSpecifici.DS_DATA) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
				Date data = cella.getDateCellValue();
				datoSpecificoIntervento.setValCampo(df.format(data));
			}
			if (tipoCampo == DatiSpecifici.DS_COMBO) {
				String valoreCella = null;
				if (cella.getCellType() == CellType.NUMERIC) {
					valoreCella = NumberToTextConverter.toText(cella.getNumericCellValue());
				} else {
					valoreCella = cella.getStringCellValue();
				}
				String valoriAmmessi = d.getValAmm();
				Gson gson = new GsonBuilder().serializeNulls().create();
				Type listType = new TypeToken<ArrayList<Entry>>() {
				}.getType();
				ArrayList<Entry> nodi = gson.fromJson(valoriAmmessi, listType);
				Iterator<Entry> i = nodi.iterator();
				while (i.hasNext()) {
					Entry e = i.next();
					if (e.getName().equals(valoreCella)) {
						datoSpecificoIntervento.setCodValCampo(e.getValue());
						break;
					}

				}

			}
			mdao.insert(datoSpecificoIntervento);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}

class Periodo {
	private String mese;
	private String anno;

	public String getMese() {
		return mese;
	}

	public void setMese(String mese) {
		this.mese = mese;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

}

class Entry {

	private String value;
	private String name;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[key=" + name + ", value=" + value + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
