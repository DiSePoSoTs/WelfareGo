package it.wego.welfarego.cartellasocialews;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.utils.xml.XmlUtils;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class CartellasocialeWsMain {

	public static EntityManager entityManager;
	private final static Logger logger = LoggerFactory.getLogger(CartellasocialeWsMain.class);

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			logger.error("usage: $jar <dbConfig> <wsConfig> <operation> [<args>]");
			logger.error("operations: {}", (Object) Action.values());
			System.exit(1);
		}

		File dbConfig = new File(args[0]);
		if (dbConfig.exists()) {
			Properties properties = new Properties();
			properties.load(new FileInputStream(dbConfig));
			logger.info("db config {}", JsonBuilder.getGsonPrettyPrinting().toJson(properties));
			entityManager = Connection.getEntityManager(properties);
		}

		CartellaSocialeWsClient cartellaSocialeWsClient = CartellaSocialeWsClient.newInstance().withEntityManager(entityManager);

		File configFile = new File(args[1]);
		if (configFile.exists()) {
			Properties config = new Properties();
			config.load(new FileInputStream(configFile));
			logger.info("ws config {}", JsonBuilder.getGsonPrettyPrinting().toJson(config));
			cartellaSocialeWsClient.withParameters(Maps.fromProperties(config));
		}

		logger.info("system config {}", JsonBuilder.getGsonPrettyPrinting()
				.toJson(Maps.filterKeys(Maps.fromProperties(System.getProperties()), new Predicate<String>() {
					public boolean apply(String input) {
						return input.matches(".*(http|net).*");
					}
				})));

		Action action = Action.valueOf(args[2]);
		XmlUtils xmlUtils = XmlUtils.getInstance();

		try {
			Object response = null;
			switch (action) {
			case TEST:
				logger.info("just testing, doing nothing . . ");
				break;
			case TESTDB:
				logger.info("testing db connection");
				logger.info("pai count = {}, anag count = {}", new PaiDao(entityManager).countAll(), new AnagrafeSocDao(entityManager).countAll());
				break;
			case UPDATEANAGRAFICA:
				Preconditions.checkArgument(args.length == 4, "usage: ... UPDATEANAGRAFICA <codAna>");
				response = cartellaSocialeWsClient.withAnagrafeSoc(args[3]).modificaAnagrafica();
				break;
			case INSERIMENTOCARTELLASOCIALE:
				Preconditions.checkArgument(args.length == 4, "usage: ... INSERIMENTOCARTELLASOCIALE <codAna>");
				response = cartellaSocialeWsClient.withAnagrafeSoc(args[3]).inserimentoCartellaSociale();
				break;
			case TESTCONNECTION:
				response = cartellaSocialeWsClient.testConnection();
				break;
			case RUN:
				StartSyncWS.Sincronizza(entityManager);
				break;
			}
			if (response != null) {
				logger.info("got response = \n{}", xmlUtils.marshallJaxbObjectToIndentedString(response));
			}

		}
		catch (MalformedURLException me) {
            System.out.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
		catch(Exception e)
		{
			System.out.println("Exception: " + e);
		}
		finally {
			if (entityManager != null) {
				if (entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		}
		logger.info("done");
	}

	public static enum Action {

		TEST, TESTDB, UPDATEANAGRAFICA, INSERIMENTOCARTELLASOCIALE, TESTCONNECTION, RUN
	}
}
