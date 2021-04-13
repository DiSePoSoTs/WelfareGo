/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.utils.xml.XmlUtils;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.Determine;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.xsd.pratica.Pratica;
import java.io.File;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author aleph
 */
public class RicevutaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	{
		logger.info("ready");
	}

	enum Action {
		PRINT
	}

	private Element createElement(Document document, String name, @Nullable String value) {
		Element element = document.createElement(name);
		if (value != null) {
			element.setTextContent(value);
		}
		return element;
	}

	private Element createAndAppendElement(Document document, @Nullable Element target, String name,
			@Nullable String value) {
		Element element = createElement(document, name, value);
		MoreObjects.firstNonNull(target, document.getDocumentElement()).appendChild(element);
		return element;
	}

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
		try {
			switch (getAction(Action.class)) {
			case PRINT: {
				logger.debug("printing ricevuta");
				persistenceAdapter.initTransaction();
				PaiIntervento paiIntervento = new PaiInterventoDao(persistenceAdapter.getEntityManager()).findByKey(
						getParameter("codPai", Integer.class), getParameter("codTipint"),
						getParameter("cntTipint", Integer.class));
				Validate.notNull(paiIntervento, "paiInterventoMese not found!");
				TipologiaIntervento tipologiaIntervento = paiIntervento.getTipologiaIntervento();
				Template template = tipologiaIntervento.getCodTmplRicevuta();
				Validate.notNull(template, "template not found!");
				int contatore = tipologiaIntervento.getContatoreRicevuta();
				logger.debug("counter for tipInt {} = {}", tipologiaIntervento.getCodTipint(), contatore);
				tipologiaIntervento.setContatoreRicevuta(contatore + 1);
				persistenceAdapter.commitTransaction();

				String data = Pratica.getXmlCartellaSociale(paiIntervento);
				{
					XmlUtils xmlUtils = XmlUtils.getInstance();
					Document document = xmlUtils.readXml(data);

					createAndAppendElement(document, null, "numeroRicevuta", Integer.toString(contatore));
					Element codiceInterventoElement = createAndAppendElement(document, null, "codiceIntervento", null);
					createAndAppendElement(document, codiceInterventoElement, "codPai",
							paiIntervento.getPaiInterventoPK().getCodPai().toString());
					createAndAppendElement(document, codiceInterventoElement, "codTipint",
							paiIntervento.getPaiInterventoPK().getCodTipint());
					createAndAppendElement(document, codiceInterventoElement, "cntTipint",
							paiIntervento.getPaiInterventoPK().getCntTipint().toString());

					Determine determina = paiIntervento.getDeterminaAssociata();
					createAndAppendElement(document, null, "numeroDetermina",
							determina == null ? null : determina.getNumDetermina());

					createAndAppendElement(document, null, "importoErogato",
							new PaiInterventoMeseDao(persistenceAdapter.getEntityManager())
									.sumBdgtConsPaiIntervento(paiIntervento).toString());

					createAndAppendElement(document, null, "dataErogazione", determina == null ? null
							: ISODateTimeFormat.dateTime().print(new DateTime(determina.getTsDetermina())));

					createAndAppendElement(document, null, "meseRiferimento",
							Integer.toString(new DateTime(paiIntervento.getDtAvvio()).getMonthOfYear()));

					createAndAppendElement(document, null, "scuola", "cippalippa");
					data = xmlUtils.xmlToString(document);
					if (logger.isDebugEnabled()) {
						File file = File.createTempFile("dataForTemplateRicevuta_", ".xml");
						FileUtils.writeStringToFile(file, data);
						logger.debug("dumped template xml to {}", file.getAbsolutePath());
					}
				}

				DynamicOdtUtils.newInstance().withTemplateBase64(template.getClobTmpl()).withDataXml(data)
						.withConfig(new ConfigurationDao(persistenceAdapter.getEntityManager())
								.getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
						.sendResponse(template.getDesTmpl(), response);
				return SKIP_RESPONSE;
			}
			default: {
				Validate.isTrue(false, "unreacheable");
				return null;
			}
			}
		} catch (Exception ex) {
			persistenceAdapter.rollbackTransaction();
			throw ex;
		} finally {
			persistenceAdapter.close();
		}
	}
}
