package it.wego.welfarego.abstracts;

import biz.no_ip.anyplace.blinkfish.BlinkFish;
import it.wego.json.JSonUtils;
import it.wego.persistence.PersistenceAdapterFactory;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author aleph
 */
public class GenericFormPortlet extends GenericPortlet {

    public static final String HANDLER_CLASS_PROP_NAME = "handler", PORTLET_REQUEST = "PORTLET_REQUEST", PORTLET_NAMESPACE = "PORTLET_NAMESPACE", PORTLET_RESPONSE = "PORTLET_RESPONSE", PORTLET_CONTEXT = "PORTLET_CONTEXT";
    private Class<? extends AbstractForm> handlerClass;
    private Logger logger = LogManager.getLogger(getClass());

    public Logger getLogger() {
        return logger;
    }

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        try {
            String handlerName = config.getInitParameter(HANDLER_CLASS_PROP_NAME);
            if (handlerName != null) {
                handlerClass = (Class<? extends AbstractForm>) Class.forName(handlerName);
                getLogger().debug("initializing generic portlet with handler : " + handlerClass.toString());
            }
        } catch (Throwable e) {
            getLogger().error("error while initializing generic portlet", e);
        }
        if (handlerClass == null) {
            throw new PortletException("error while initializing generic portlet : unable to obtain handler class");
        }
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        getLogger().debug("handling view request");
        try {
            response.setContentType("text/html");
            AbstractForm portletForm = handlerClass.newInstance();
            portletForm.setParameters(parseParameters(request.getParameterMap()));
            portletForm.setAttribute(PORTLET_REQUEST, request);
            portletForm.setAttribute(PORTLET_RESPONSE, response);
            portletForm.setAttribute(PORTLET_NAMESPACE, response.getNamespace().replaceAll("(^_+|_+$)", ""));
            portletForm.setAttribute(PORTLET_CONTEXT, getPortletContext());
            if (getPortletContext() != null) {
                portletForm.setPortletSessionAttributes(getAttributes(request, response));
            }
            Writer writer = response.getWriter();
            ((AbstractForm.Viewable) portletForm).doView(writer);
            writer.flush();
            writer.close();
            PersistenceAdapterFactory.releasePersistenceAdapter();
        } catch (Exception ex) {
            getLogger().error("error while rendering portlet view", ex);
            PersistenceAdapterFactory.releasePersistenceAdapter();
            throw new PortletException(ex);
        }
    }

    public Map<String, Object> getAttributes(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        Map<String, Object> attrs = new HashMap<String, Object>();
        String uid = "it.wego.welfarego.jspLoader_" + Thread.currentThread().getName();
        BlinkFish.map.put(uid, attrs);
        getPortletContext().getRequestDispatcher("/loader.jsp").include(request, response);
        BlinkFish.map.remove(uid);
        return attrs;
    }

    private static Map<String, String> parseParameters(Map<String, String[]> parameterMap) {
        Map<String, String> res = new HashMap<String, String>();
        for (Entry<String, String[]> param : parameterMap.entrySet()) {
            String[] paramValue = param.getValue();
            String value = paramValue.length == 1 ? paramValue[0] : (JSonUtils.getGson().toJson(paramValue));
            res.put(param.getKey(), value);
        }
        return res;
    }
}
