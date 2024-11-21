package it.wego.welfarego.cartellasocialews;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.persistence.EntityManager;
import javax.xml.xpath.XPathExpressionException;
import it.wego.utils.xml.XmlUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.utils.xml.WsUtils;

import jakarta.xml.bind.JAXBException;

import it.wego.welfarego.cartellasocialews.beans.*;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CodaCsr;


import it.wego.welfarego.persistence.dao.CodaCsrDao;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.entities.Pai;

import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;

import it.wego.welfarego.persistence.dao.PaiDao;






/**
 * client per il webservice CSR idealmente, tutte le chiamate da welfarego per
 * CSR passano attraverso questa classe, che si occupa di 1) tradurre gli
 * oggetti welfarego in oggetti CSRws 2) effettuare le chiamate 3)
 * eventualmente, processare le risposte
 *
 * esempio d'uso:
 *
 * CartellaSocialeWsClient.newInstance()
 * .withServiceUrl(Parametri.getURLCSR(entityManager))
 * .withCodiceOperatore("6222") .withAnagrafeSoc(anagrafe)
 * .updateAnagraficaBg();
 *
 * nota: oggetto mutabile e quindi non thread-safe.
 *
 * @author aleph
 */
public class CartellaSocialeWsClient {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * single threaded executor: all requests for csrws will be queued on a single
	 * thread. for parallel execution, change this for a multithread executor
	 */

	private Cartella cartellaService;
	private Map<String, String> config = Maps.newHashMap(Maps.fromProperties(defaultConfig));
	private final CartellaSocialeWsDataUtils dataUtils = new CartellaSocialeWsDataUtils(this);
	private static final Properties defaultConfig = new Properties();
	private static final String MODIFICA_INTERVENTO = "MODIFICA_INTERVENTO";
	private static final String INSERIMENTO_INTERVENTO = "INSERIMENTO_INTERVENTO";
	private static final String INSERIMENTO_CARTELLA = "INSERIMENTO_CARTELLA";
	private static final String MODIFICA_CARTELLA = "MODIFICA_CARTELLA";
	private static final String RIATTIVA_CARTELLA = "RIATTIVA_CARTELLA";
	private static final String CHIUDI_CARTELLA = "CHIUDI_CARTELLA";
	private static final String MODIFICA_PROFILO = "MODIFICA_PROFILO";
	private static final String MODIFICA_PROGETTO = "MODIFICA_PROGETTO";
	private static final String CARTELLA_GIAPRESENTE = "invalid.cf";
	private static final String INTERVENTO_GIAPRESENTE = "intervento.giapresente";
	private static final String CARTELLA_CHIUSA = "(?s).*cartella.*CHIUSA.*";

	static {
		try {
			final Logger logger = LoggerFactory.getLogger(CartellaSocialeWsClient.class);
			InputStream defaultConfProp = CartellaSocialeWsClient.class.getResourceAsStream("/config.properties");
			if (defaultConfProp != null) {
				defaultConfig.load(defaultConfProp);
				logger.info("default ws config {}", JsonBuilder.getGsonPrettyPrinting().toJson(defaultConfig));
			} // if
			else {
				logger.info("default ws config is missing");
			} // else
			final String proxyUsername = System.getProperty("http.proxyUser"),
					proxyPassword = System.getProperty("http.proxyPassword");
			if (proxyUsername != null && proxyPassword != null) {
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						if (getRequestorType() == Authenticator.RequestorType.PROXY) {
							return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
						} else {
							return super.getPasswordAuthentication();
						}

					}
				});
			}
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	// **************************
	// INITIALIZATION
	// **************************
	//
	private CartellaSocialeWsClient() {
		logger.debug("new instance");
	}

	public static CartellaSocialeWsClient newInstance() throws NoSuchAlgorithmException, KeyManagementException {
		/* Create a trust manager that does not validate certificate chains */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {

			public boolean verify(String arg0, SSLSession arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		return new CartellaSocialeWsClient();
	}

	public CartellaSocialeWsClient withParameters(Map<String, String> config) {
		if (logger.isDebugEnabled()) {
			logger.debug("loaded config : {}", JsonBuilder.getGsonPrettyPrinting().toJson(config));
		}
		this.config.putAll(config);
		return this;
	}

	public CartellaSocialeWsClient withParameters(Properties config) {
		return withParameters(Maps.fromProperties(config));
	}

	public CartellaSocialeWsClient withPai(Pai pai) {
		dataUtils.setPai(pai);
		return this;
	}

	public CartellaSocialeWsClient withPai(String codPai) {
		dataUtils.setPai(dataUtils.getEntityManager().find(Pai.class, Integer.valueOf(codPai)));
		return this;
	}

	public CartellaSocialeWsClient withPaiIntervento(PaiIntervento paiIntervento) {
		dataUtils.setPaiIntervento(paiIntervento);
		return this;
	}

	public CartellaSocialeWsClient withAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
		dataUtils.setAnagrafeSoc(anagrafeSoc);
		return this;
	}

	public CartellaSocialeWsClient withAnagrafeSoc(String codAna) {
		AnagrafeSoc anagrafeSoc = dataUtils.getEntityManager().find(AnagrafeSoc.class, Integer.valueOf(codAna));
		Preconditions.checkNotNull(anagrafeSoc, "anagrafeSoc not found for codAna = '%s'", codAna);
		return this.withAnagrafeSoc(anagrafeSoc);
	}

	public CartellaSocialeWsClient withPaiIntervento(String codPai, String codTipint, String cntTipint) {
		PaiIntervento paiIntervento = new PaiInterventoDao(dataUtils.getEntityManager()).findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
		Preconditions.checkNotNull(paiIntervento, "paiIntervento not found for cod = '%s:%s:%s'", codPai, codTipint, cntTipint);
		return this.withPaiIntervento(paiIntervento);
	}

	public CartellaSocialeWsClient withEntityManager(EntityManager entityManager) {
		dataUtils.setEntityManager(entityManager);
		return this;
	}

	public CartellaSocialeWsClient loadConfigFromDatabase() {
		logger.debug("loading config from db");
		return this.withParameters(new ConfigurationDao(dataUtils.getEntityManager()).getConfigWithPrefix("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient."));
	}

	private boolean isEnabled() {
		boolean enabled = !Boolean
				.parseBoolean(config.get("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.disabled"));
		if (!enabled) {
			logger.debug("ws call disabled, skipping . . ");
		}
		return enabled;
	}

	public String getLogFile() {
		return Strings.emptyToNull(config.get("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.logFile"));
	}

	public @Nullable String getServiceUrl() {
		return Strings.emptyToNull(config.get("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.endpointUrl"));
	}

	public CartellaSocialeWsDataUtils getDataUtils() {
		return dataUtils;
	}

	/**
	 * prepara e restituisce l'oggetto proxy per il ws CSR
	 *
	 * @return
	 */
	protected Cartella getCartellaService() {
		if (cartellaService == null) {
			Cartella_Service cartella_Service = new Cartella_Service();
			cartellaService = cartella_Service.getCartellaSOAP();

			WsUtils wsUtils = WsUtils.newInstance().withServiceInterface(cartellaService);
			wsUtils.withLogger(logger);
			wsUtils.withDumpPrefix("cartellaSocialeWs").withLogMode(WsUtils.LogMode.NEW_FILES).addLoggerToService();
			if (getLogFile() != null) {
				wsUtils.withDumpPrefix(getLogFile()).withLogMode(WsUtils.LogMode.SAME_FILES).addLoggerToService();
			}

			Preconditions.checkNotNull(getServiceUrl(), "url not set");
			logger.debug("webservice endopoint set to {}", getServiceUrl());
			wsUtils.withEndpoint(getServiceUrl()).setEndpointForService();
		}
		return cartellaService;
	}

	/**
	 * aggiorna l'anagrafica untente su CSR (in background)
	 *
	 * @return
	 */
	public RicevutaCartella inserimentoCartellaSociale() {
		logger.debug("inserimentoCartellaSociale");
		if (!isEnabled()) {
			return null;
		}
		InserimentoCartellaSociale richiesta = dataUtils.createInserimentoCartellaSocialeRequest();
		XmlUtils xmlUtils = XmlUtils.getInstance();
		try {
			logger.info("richiesta = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(richiesta));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RicevutaCartella ricevutaCartella = getCartellaService().inserimentoCartella(richiesta);
		Messaggio messaggio = checkResponse(ricevutaCartella, INSERIMENTO_CARTELLA);
		if (messaggio != null && messaggio.getCodice().equals(CARTELLA_GIAPRESENTE)) {
			// aggiorno il codice cartella ed effettuo la modifica cartella
			dataUtils.initTransaction();
			dataUtils.getAnagrafeSoc().setIdCsr(messaggio.getDescrizione().toString());
			dataUtils.commitTransaction();
			modificaAnagrafica();
			modificaProfilo();
			modificaProgetto();
		} else if (messaggio != null && messaggio.getDescrizione().matches(CARTELLA_CHIUSA)) {
			riattivaCartellaSociale();
			modificaAnagrafica();
			modificaProfilo();
			modificaProgetto();
		} else {
			Preconditions.checkNotNull(ricevutaCartella.getIdCartella(), "nessun id cartella restituito");
			logger.debug("id cartella csr = {} for anagrafeSoc = {}", ricevutaCartella.getIdCartella(),	dataUtils.getAnagrafeSoc());
			dataUtils.initTransaction();
			dataUtils.getAnagrafeSoc().setIdCsr(ricevutaCartella.getIdCartella().toString());
			dataUtils.commitTransaction();
		}
		return ricevutaCartella;
	}

	public RicevutaModificaAnagrafica modificaCartella() {
		logger.debug("modificaCartella");
		if (!isEnabled()) {
			return null;
		}
		RicevutaModificaAnagrafica ricevutaModificaAnagrafica = modificaAnagrafica();
		modificaProfilo();
		modificaProgetto();
		return ricevutaModificaAnagrafica;
	}

	public RicevutaModificaAnagrafica modificaAnagrafica() {
		logger.debug("modificaAnagrafica");
		if (!isEnabled()) {
			return null;
		}
		
		ModificaAnagrafica richiesta = dataUtils.createModificaAnagraficaRequest();
		
		XmlUtils xmlUtils = XmlUtils.getInstance();
		try {
			logger.info("richiesta = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(richiesta));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		RicevutaModificaAnagrafica ricevutaModificaAnagrafica = getCartellaService().modificaAnagrafica(richiesta);
		Messaggio messaggio = checkResponse(ricevutaModificaAnagrafica, MODIFICA_CARTELLA);
		if (messaggio != null && messaggio.getDescrizione().matches(CARTELLA_CHIUSA)) {
			riattivaCartellaSociale();
			modificaAnagrafica();
		}
		return ricevutaModificaAnagrafica;
	}

	public RicevutaModificaProfilo modificaProfilo() {
		logger.debug("modificaProfilo");
		if (!isEnabled()) {
			return null;
		}
		RicevutaModificaProfilo ricevutaModificaProfilo = getCartellaService()
				.modificaProfilo(dataUtils.createModificaProfiloRequest());
		checkResponse(ricevutaModificaProfilo, MODIFICA_PROFILO);
		return ricevutaModificaProfilo;
	}

	public RicevutaModificaProgetto modificaProgetto() {
		logger.debug("modificaProgetto");
		if (!isEnabled()) {
			return null;
		}
		RicevutaModificaProgetto ricevutaModificaProgetto = getCartellaService()
				.modificaProgetto(dataUtils.createModificaProgettoRequest());
		checkResponse(ricevutaModificaProgetto, MODIFICA_PROGETTO);
		return ricevutaModificaProgetto;
	}

	public RicevutaChiudiCartella chiudiCartellaSociale() {
		logger.debug("chiudiCartella");
		if (!isEnabled()) {
			return null;
		}
		ChiudiCartella richiesta = dataUtils.createChiudiCartellaRequest();
		XmlUtils xmlUtils = XmlUtils.getInstance();
		try {
			logger.info("richiesta = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(richiesta));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RicevutaChiudiCartella ricevutaChiudiCartella = getCartellaService().chiudiCartella(richiesta);
		checkResponse(ricevutaChiudiCartella, CHIUDI_CARTELLA);
		return ricevutaChiudiCartella;
	}

	public RicevutaRiattivaCartella riattivaCartellaSociale() {
		logger.debug("riattivaCartella");
		if (!isEnabled()) {
			return null;
		}
		
		RiattivaCartella richiesta = dataUtils.createRiattivaCartellaRequest();
		
		XmlUtils xmlUtils = XmlUtils.getInstance();
		try {
			logger.info("richiesta = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(richiesta));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RicevutaRiattivaCartella ricevutaRiattivaCartella = getCartellaService().riattivaCartella(richiesta);
		checkResponse(ricevutaRiattivaCartella, RIATTIVA_CARTELLA);
		return ricevutaRiattivaCartella;
	}

	public RicevutaIntervento inserimentoIntervento() {
		logger.debug("inserimentoIntervento");
		if (!isEnabled()) {
			return null;
		}
		
		NuovoInserimentoIntervento richiesta = dataUtils.createInserimentoInterventoRequest();
				
		XmlUtils xmlUtils = XmlUtils.getInstance();
		try {
			logger.info("richiesta = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(richiesta));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RicevutaIntervento ricevutaIntervento = getCartellaService().nuovoInserimentoIntervento(richiesta);
		Messaggio messaggio = checkResponse(ricevutaIntervento, INSERIMENTO_INTERVENTO);
		if (messaggio != null && messaggio.getDescrizione().matches(CARTELLA_CHIUSA)) {
			riattivaCartellaSociale();
			inserimentoIntervento();
		} else if (messaggio != null && messaggio.getCodice().equals(INTERVENTO_GIAPRESENTE)) {
			// aggiorno il codice cartella ed effettuo la modifica cartella
			dataUtils.initTransaction();
			dataUtils.getPaiIntervento().setIdCsr(messaggio.getDescrizione().toString());
			dataUtils.commitTransaction();
			modificaIntervento();
		} else {
			Preconditions.checkNotNull(ricevutaIntervento.getIdIntervento(), "nessun id intervento restituito");
			logger.debug("id intervento csr = {} for intervento = {}", ricevutaIntervento.getIdIntervento(), dataUtils.getPaiIntervento());
			dataUtils.initTransaction();
			dataUtils.getPaiIntervento().setIdCsr(ricevutaIntervento.getIdIntervento().toString());
			dataUtils.commitTransaction();
		}
		return ricevutaIntervento;
	}

	public RicevutaModificaIntervento modificaIntervento() {
		logger.debug("modificaIntervento");
		if (!isEnabled()) {
			return null;
		}
		RicevutaModificaIntervento ricevutaModificaIntervento = getCartellaService()
				.nuovaModificaIntervento(dataUtils.createModificaInterventoRequest());
		Messaggio messaggio = checkResponse(ricevutaModificaIntervento, MODIFICA_INTERVENTO);
		if (messaggio != null && messaggio.getDescrizione().matches(CARTELLA_CHIUSA)) {
			riattivaCartellaSociale();
			modificaIntervento();
		}
		return ricevutaModificaIntervento;
	}

	protected String getConfig(String key) {
		return config.get(key);
	}

	private Messaggio checkResponse(RispostaBase response, String azione) {
		Messaggio messaggio = null;
		Esito esito = response.getEsito();
		if (!response.getAvvisi().isEmpty()) {
			logger.warn("remote services return warnings = {}", response.getAvvisi().toString());
		}
		if (Objects.equal(esito.getCodEsito(), EsitoOperazioneEnum.ERRORI)) {
			if (response.getErrori().get(0).getDescrizione().matches(CARTELLA_CHIUSA)) {
				messaggio = response.getErrori().get(0);
			} else if (azione.equals(INSERIMENTO_CARTELLA)
					&& response.getErrori().get(0).getCodice().equals(CARTELLA_GIAPRESENTE)) {
				messaggio = response.getErrori().get(0);
			} else if (azione.equals(INSERIMENTO_INTERVENTO)
					&& response.getErrori().get(0).getCodice().equals(INTERVENTO_GIAPRESENTE)) {
				messaggio = response.getErrori().get(0);
			} else {
				throw new CartellaSocialeWsRemoteException(response);
			}
		}
		return messaggio;
		// TODO better check, warning handling . . .
	}

	public void sincronizzaCartellaSociale() {
		logger.debug("sincronizzaCartellaSociale");
		if (!isEnabled()) {
			return;
		}
		CodaCsrDao csd = new CodaCsrDao(dataUtils.getEntityManager());

		Preconditions.checkNotNull(dataUtils.getAnagrafeSoc(), "anagrafeSoc must not be null");
		if (Strings.isNullOrEmpty(dataUtils.getAnagrafeSoc().getIdCsr())) {
			PaiDao po = new PaiDao(dataUtils.getEntityManager());
			Pai p = po.findLastPai(dataUtils.getAnagrafeSoc().getCodAna());
			if (p == null) {
				return;
			}
		}
		CodaCsr cs = new CodaCsr();
		cs.setCodAna(new BigInteger(String.valueOf(dataUtils.getAnagrafeSoc().getCodAna())));
		cs.setDtInscoda(new Timestamp(System.currentTimeMillis()));
		Integer conta;
		if (Strings.isNullOrEmpty(dataUtils.getAnagrafeSoc().getIdCsr())) {
			conta = csd.countCodaCartellaSociale(dataUtils.getAnagrafeSoc().getCodAna(),
					CodaCsr.SINCRONIZZAZIONE_INSERISCI_CARTELLA);
			cs.setAzione(CodaCsr.SINCRONIZZAZIONE_INSERISCI_CARTELLA);
		} else {
			conta = csd.countCodaCartellaSociale(dataUtils.getAnagrafeSoc().getCodAna(),
					CodaCsr.SINCRONIZZAZIONE_MODIFICA_CARTELLA);
			cs.setAzione(CodaCsr.SINCRONIZZAZIONE_MODIFICA_CARTELLA);
		}
		if (conta > 0) {
			return;
		}
		cs.setNumeroTentativi(0);
		EntityManager em = dataUtils.getEntityManager();
		em.persist(cs);
	}

	public void sincronizzaCartellaSocialeWS() {
		logger.debug("sincronizzaCartellaSociale");
		if (!isEnabled()) {
			return;
		}
		Preconditions.checkNotNull(dataUtils.getAnagrafeSoc(), "anagrafeSoc must not be null");
		if (Strings.isNullOrEmpty(dataUtils.getAnagrafeSoc().getIdCsr())) {
			inserimentoCartellaSociale();
		} else {
			modificaAnagrafica();
			modificaProfilo();
			modificaProgetto();
		}
	}

	public void sincronizzaIntervento() {
		logger.debug("sincronizzaIntervento");
		if (!isEnabled()) {
			return;
		}
		CodaCsrDao csd = new CodaCsrDao(dataUtils.getEntityManager());
		CodaCsr cs = new CodaCsr();
		cs.setDtInscoda(new Timestamp(System.currentTimeMillis()));
		PaiIntervento paiIntervento = dataUtils.getPaiIntervento();
		if (paiIntervento.getAssociazione() == null) {
			throw new IllegalArgumentException("associazione is null: " + paiIntervento);
		}

		cs.setCodAna(new BigInteger(String.valueOf(paiIntervento.getPai().getCartellaSociale().getCodAna())));
		cs.setCodPai(new BigInteger(String.valueOf(paiIntervento.getPaiInterventoPK().getCodPai())));
		cs.setCodTipint(paiIntervento.getPaiInterventoPK().getCodTipint());
		cs.setCntTipint(new BigInteger(String.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint())));
		cs.setDtAvvio(paiIntervento.getDtAvvio());

		Preconditions.checkNotNull(paiIntervento, "paiIntervento must not be null");
		logger.info("Sto provando a inserire l'intervento: {}", dumpPkIntervento(paiIntervento));

		Integer conta;

		if (Strings.isNullOrEmpty(paiIntervento.getIdCsr())) { // nuovo intervento non sincronizzato
			conta = csd.countCodaInterventi(dataUtils.getAnagrafeSoc().getCodAna(),
					paiIntervento.getPaiInterventoPK().getCodPai(), paiIntervento.getPaiInterventoPK().getCodTipint(),
					paiIntervento.getPaiInterventoPK().getCntTipint(), CodaCsr.SINCRONIZZAZIONE_INSERISCI_INTERVENTO);
			cs.setAzione(CodaCsr.SINCRONIZZAZIONE_INSERISCI_INTERVENTO);
		} else {
			conta = csd.countCodaInterventi(dataUtils.getAnagrafeSoc().getCodAna(),
					paiIntervento.getPaiInterventoPK().getCodPai(), paiIntervento.getPaiInterventoPK().getCodTipint(),
					paiIntervento.getPaiInterventoPK().getCntTipint(), CodaCsr.SINCRONIZZAZIONE_MODIFICA_INTERVENTO);
			cs.setAzione(CodaCsr.SINCRONIZZAZIONE_MODIFICA_INTERVENTO);
		}
		if (conta > 0) {
			logger.info("conta è uguale a: " + conta);
			return;
		}
		cs.setNumeroTentativi(0);
		EntityManager em = dataUtils.getEntityManager();
		em.persist(cs);
	}

	public void sincronizzaInterventoWS() {
		logger.debug("sincronizzaIntervento");
		if (!isEnabled()) {
			return;
		}
		Preconditions.checkNotNull(dataUtils.getPaiIntervento(), "paiIntervento must not be null");
		if (Strings.isNullOrEmpty(dataUtils.getPaiIntervento().getIdCsr())) { // nuovo intervento non sincronizzato
			inserimentoIntervento();
		} else {
			modificaIntervento();
		}
	}

	public Boolean testSOAPConnection() throws URISyntaxException, MalformedURLException {
		java.net.URI serviceURL = new java.net.URI(getServiceUrl());
		Cartella_Service service = new Cartella_Service(serviceURL.toURL());
		URL wsdlurl = service.getWSDLDocumentLocation();
		logger.info("WSDL URL {}",  wsdlurl.toString());
		
		Cartella c = service.getCartellaSOAP();
		return true;
	}
	
	public String testConnection() throws MalformedURLException, IOException {

		logger.debug("testConnection");
		HttpURLConnection connection = (HttpURLConnection) new URL(getServiceUrl().replaceFirst("/*$", "") + "?wsdl").openConnection();
		connection.connect();
		DataInputStream dis = new DataInputStream(connection.getInputStream());

		String inputLine;

		while (dis.available() > 0) {
			try {
				inputLine = dis.readUTF();
			} catch (EOFException eofe) {
				System.out.println("End of file reached");
				break;
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

		}

		dis.close();

		String response = IOUtils.toString(connection.getInputStream(), "UTF-8");
		int responseCode = connection.getResponseCode();
		Preconditions.checkArgument(responseCode == 200, "error, response code %s, response message %s", responseCode,
				response);
		logger.debug("response code {}, response message {}", responseCode, response);
		return response;
	}

	public static String dumpPkIntervento(PaiIntervento item) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(item.getPaiInterventoPK());
		return jsonString;
	}

	public static class CartellaSocialeWsRemoteException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public CartellaSocialeWsRemoteException(RispostaBase rispostaBase1) {
			super("si sono verificati degli errori : " + rispostaBase1.getErrori().toString() + " '"
					+ rispostaBase1.getEsito().getDescrEsito() + "'");
		}
	}
}
