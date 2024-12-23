package it.wego.welfarego.cartellasocialews;

import java.io.File;
import java.io.FileReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import it.wego.welfarego.persistence.dao.CodaCsrDao;
import it.wego.welfarego.persistence.entities.CodaCsr;

public class SyncWS {

	private EntityManager em;

	private File fileCsvMSNA;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private SyncWS() {
		logger.debug("new instance");
	}

	public static SyncWS newInstance() {
		return new SyncWS();
	}

	public SyncWS withEntityManager(EntityManager em) {
		this.em = em;
		return this;
	}

	public SyncWS withCsvFileMSNA(File file) {
		this.fileCsvMSNA = file;
		return this;
	}

	public void sincronizzaMSNAWs() {

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(fileCsvMSNA)).withCSVParser(csvParser) // custom CSV parser
				.withSkipLines(1) // skip the first line, header info
				.build()) {
			List<String[]> r = reader.readAll();
			r.forEach(x -> {
					try {
						CartellaSocialeWsClient.newInstance().inserimentoCartellaSocialeMSNA(x);
					} catch (KeyManagementException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			});
		}
		catch (Exception e) {
			String errore = e.getMessage();
			logger.debug("errore -", errore);
		}
	}

	public void sincronizzaWS() {

		CodaCsrDao csrd = new CodaCsrDao(em);

		List<CodaCsr> coda = csrd.findCodaDaEvadere();

		for (CodaCsr csr : coda) {
			try {
				switch (csr.getAzione()) {
				case CodaCsr.SINCRONIZZAZIONE_INSERISCI_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withAnagrafeSoc(csr.getCodAna().toString()).inserimentoCartellaSociale();
					break;
				case CodaCsr.SINCRONIZZAZIONE_MODIFICA_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withAnagrafeSoc(csr.getCodAna().toString()).modificaCartella();
					break;

				case CodaCsr.SINCRONIZZAZIONE_INSERISCI_INTERVENTO:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withPaiIntervento(csr.getCodPai().toString(), csr.getCodTipint(),
									csr.getCntTipint().toString())
							.inserimentoIntervento();
					break;

				case CodaCsr.SINCRONIZZAZIONE_MODIFICA_INTERVENTO:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withPaiIntervento(csr.getCodPai().toString(), csr.getCodTipint(),
									csr.getCntTipint().toString())
							.modificaIntervento();
					break;

				case CodaCsr.SINCRONIZZAZIONE_CHIUDI_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withAnagrafeSoc(csr.getCodAna().toString()).chiudiCartellaSociale();
					break;

				case CodaCsr.SINCRONIZZAZIONE_RIATTIVA_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase()
							.withAnagrafeSoc(csr.getCodAna().toString()).riattivaCartellaSociale();
					break;
				default:
					break;
				}

				em.getTransaction().begin();
				csr.setDtCallcsr(new Timestamp(System.currentTimeMillis()));
				csr.setDtInscsr(new Timestamp(System.currentTimeMillis()));
				csr.setTestoErrore("OK");
				em.merge(csr);
			} catch (Exception ex) {
				if (!em.getTransaction().isActive()) {
					em.getTransaction().begin();
				}
				csr.setDtCallcsr(new Timestamp(System.currentTimeMillis()));
				csr.setNumeroTentativi(csr.getNumeroTentativi() + 1);
				String messaggio = "";
				if (ex.getMessage() != null) {
					messaggio = ex.getMessage().substring(0,
							(ex.getMessage().length() <= 3900 ? ex.getMessage().length() : 3900));
				}
				csr.setTestoErrore(messaggio);
				em.merge(csr);
			} finally {
				if (em.getTransaction().isActive()) {
					em.getTransaction().commit();
				}
			}
		}
		em.close();
	}
}
