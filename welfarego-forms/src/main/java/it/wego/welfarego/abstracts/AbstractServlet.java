package it.wego.welfarego.abstracts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.wego.extjs.json.JsonBuilder;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.web.WebUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rappresenta una servlet astratta per la gestione delle richieste ajax, può
 * appoggiarsi ad un oggetto AbstractForm per la logica di funzionamento.
 *
 * @author aleph
 */
public abstract class AbstractServlet extends GenericAbstractServlet {

    private AbstractFormFactory abstractFormFactory;
    private Logger requestLogger = LoggerFactory.getLogger("it.wego.welfarego.REQUEST");

    public AbstractServlet(Class<? extends AbstractForm> formHandlerClass) {
        this.abstractFormFactory = new SimpleAbstractFormFactory(formHandlerClass);
    }

    public AbstractServlet(AbstractFormFactory abstractFormFactory) {
        this.abstractFormFactory = abstractFormFactory;
    }

    public AbstractFormFactory getHandlerFactory() {
        return abstractFormFactory;
    }

    public void setHandlerFactory(AbstractFormFactory abstractFormFactory) {
        this.abstractFormFactory = abstractFormFactory;
    }
    private final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method) throws ServletException, IOException {
        PrintWriter out = null;
        AbstractForm formHandler = null;
        Map<String, String> parameters = Collections.emptyMap();
        try {
            response.setContentType(getContentType() + ";charset=" + getCharacterEncoding());
            out = response.getWriter();
            parameters = WebUtils.getParametersMap(request);
            parameters.put(METHOD_PROP, method);
            formHandler = abstractFormFactory.createFormHandler(parameters); // new VerificaDatiForm()
            getLogger().debug("processing request formHandler: " + formHandler + ", action:" + parameters.get("action"));
            formHandler.setPortletSessionAttributes(getAttributes(request, response));
            formHandler.setAttribute(SERVLET_REQUEST_ATTR, request);
            formHandler.setAttribute(SERVLET_RESPONSE_ATTR, response);
            requestLogger.info("got request user, param : " + formHandler.getUserInfo() + " , " + prettyGson.toJson(parameters));
            formHandler.setParameters(parameters);
            switch (formHandler.getHandleMode()) {
                case JSON:
                    Object obj = formHandler.handleRequest();
//                    requestLogger.info("serving answer : " + prettyGson.toJson(obj));
                    formHandler.getGson().toJson(obj, out);
                    break;
                case RAW:
                    requestLogger.info("serving raw answer");
                    formHandler.handleRequest(out);
                    break;
            }
        } catch (Throwable e) {
            if (e.getCause() != null && e.getCause() instanceof java.lang.IllegalArgumentException && e.getCause().toString().matches(".*devi essere loggato.*")) {
                getLogger().debug("error serving request : {}", (Object) e.getCause());
            } else {
                getLogger().error("error serving request", e);
                getLogger().error("request data : {}", prettyGson.toJson(parameters));
               
            }
            requestLogger.error("error serving request", e);
            JsonBuilder.newInstance().withWriter(out).withError(e).buildResponse();
//            prettyGson.toJson(new JsonMessage(e), out);
        } finally {
            PersistenceAdapterFactory.releasePersistenceAdapter();
            out.flush();
            out.close();
        }
    }

    public interface AbstractFormFactory {

        AbstractForm createFormHandler(Map<String, String> parameters) throws Exception;
    }

    public static class SimpleAbstractFormFactory implements AbstractFormFactory {

        private Class<? extends AbstractForm> formHandlerClass;

        public SimpleAbstractFormFactory(Class<? extends AbstractForm> formHandlerClass) {
            this.formHandlerClass = formHandlerClass;
        }

        public AbstractForm createFormHandler(Map<String, String> parameters) throws Exception {
            return formHandlerClass.newInstance();
        }
    }
}
