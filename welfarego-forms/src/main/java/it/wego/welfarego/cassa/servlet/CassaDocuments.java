/**
 * 
 */
package it.wego.welfarego.cassa.servlet;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.utils.xml.XmlUtils;
import it.wego.web.WebUtils;
import it.wego.welfarego.commons.utils.ReportUtils;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.RicevutaCassaDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.RicevutaCassa;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author fabio
 * Produzione documenti per la cassa a mani
 *
 */
public class CassaDocuments extends JsonServlet {
	public final String INTERVENTO_CASSA_CODE ="AD009";
	
	 private Logger logger = LoggerFactory.getLogger(this.getClass());
	 {
	        logger.info("ready");
	    }

	    enum Action {

	       RICEVUTA,
	       REPORT
	    }
	    
	    private Element createElement(Document document, String name, @Nullable String value) {
	        Element element = document.createElement(name);
	        if (value != null) {
	            element.setTextContent(value);
	        }
	        return element;
	    }

	    private Element createAndAppendElement(Document document, @Nullable Element target, String name, @Nullable String value) {
	        Element element = createElement(document, name, value);
	        MoreObjects.firstNonNull(target, document.getDocumentElement()).appendChild(element);
	        return element;
	    }

	/* (non-Javadoc)
	 * @see it.wego.extjs.servlet.AbstractJsonServlet#handleJsonRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, it.wego.extjs.servlet.AbstractJsonServlet.Method)
	 */
	@Override
	protected Object handleJsonRequest(HttpServletRequest request,
			HttpServletResponse response, Method method) throws Exception {
		  PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
	  
	    switch(getAction(Action.class)) {
	       case RICEVUTA:{
	    	    logger.debug("Stampa ricevuta ");
	    	    RicevutaCassaDao dao = new RicevutaCassaDao(persistenceAdapter.getEntityManager());
	    		ConfigurationDao cdao = new ConfigurationDao(persistenceAdapter.getEntityManager());
	    		
	    		String codPai = getParameter("codPai");
	    		String cntTipInt = getParameter("cntTipInt");
	    		RicevutaCassa ricevuta = dao.findRicevutaById(Integer.parseInt(codPai), Integer.parseInt(cntTipInt));
	    		Preconditions.checkNotNull(ricevuta, "Attenzione, non c'è nessuna ricevuta corrispondente a questo intervento.Conttattare l'assentenza");
	    		Template template =  new TemplateDao(persistenceAdapter.getEntityManager()).findByCodTemplate(cdao.getConfig("template.ricevuta.cassa"));
	    		Preconditions.checkNotNull(template,"Attenzione non è stato definito un template di ricevuta");
	    		PaiIntervento intervento = new PaiInterventoDao(persistenceAdapter.getEntityManager()).findByKey(Integer.parseInt(codPai), "AD009",Integer.parseInt(cntTipInt));
	    		String data = Pratica.getXmlCartellaSociale(intervento);
	    		{
	    			 XmlUtils xmlUtils = XmlUtils.getInstance();
	                 Document document = xmlUtils.readXml(data);
	                 createAndAppendElement(document, null, "numeroRicevuta", Integer.toString(ricevuta.getNumeroRicevuta()));
	                 Element codiceInterventoElement = createAndAppendElement(document, null, "codiceIntervento", null);
	                 createAndAppendElement(document, codiceInterventoElement, "codPai", codPai);
	                 createAndAppendElement(document, codiceInterventoElement, "codTipint", INTERVENTO_CASSA_CODE);
	                 createAndAppendElement(document, codiceInterventoElement, "cntTipint", cntTipInt);
	                 String numDetermina = intervento.getNumDetermina();
	                 createAndAppendElement(document, null, "numeroDetermina", numDetermina == null ? null : numDetermina);
	                 createAndAppendElement(document, null, "importoErogato", ricevuta.getImporto().toString());
	                 createAndAppendElement(document, null, "dataErogazione", numDetermina == null ? null : new SimpleDateFormat("dd/MM/yyyy").format(intervento.getDtEsec()));
	            
	                 data = xmlUtils.xmlToString(document);
	                
	               
	    		}
	    		 DynamicOdtUtils.newInstance()
	             .withTemplateBase64(template.getClobTmpl())
	             .withDataXml(data)
	             .withConfig(cdao.getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
	             .sendResponse(template.getDesTmpl(), response);
	             return new Object();
	    		
	         }
	       case REPORT:{
	    	   String nomeReport = getParameter("tipo");
	    	   Map<String, String> parameters = WebUtils.getParametersMap(request);
	    	   OutputStream out = response.getOutputStream();
	    	  try{
	    	   byte [] resultContent = null;
	    	   resultContent = ReportUtils.executeGenericReport(nomeReport +".jasper", ReportUtils.XLS, parameters);
	    	   response.setContentType("application/vnd.oasis.opendocument.spreadsheet");
               response.setHeader("Content-disposition", ": attachment;filename=" + nomeReport.replaceAll("[^a-zA-Z0-9]+", "_") + ".ods");
               response.setContentLength(resultContent.length);
               out.write(resultContent);
               out.flush();
               return new Object();
	    	  }
	    	  catch(Exception e){
	    		  response.setContentType("text/html;charset=UTF-8");
	              out.write(e.getMessage().getBytes());
	              logger.error("Errore durante la generazione del report", e);
	    	  }
	    	  finally{
	    		  out.close();
	    	  }
	       }
	       default: {
               Validate.isTrue(false, "unreacheable");
               return null;
           }
	       
	    }
		
	}

}
