package it.wego.webservices.jaxws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import org.slf4j.Logger;

/**
 * 
 * @author aleph
 * @deprecated use it.wego.utils.xml.WsUtils
 */
@Deprecated
public class WsManager {

    public static void setEndPoint(BindingProvider bindingProvider, String endPoint) throws Exception {
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
    }

    public static void logService(BindingProvider bindingProvider, Logger logger) throws Exception {
        if (logger.isDebugEnabled()) {
            WsLogger loggingHandler = new WsLogger(logger);
            addHandler(bindingProvider, loggingHandler);
        }
    }

    public static void addHandler(BindingProvider bindingProvider, SOAPHandler handler) throws Exception {
        Binding binding = bindingProvider.getBinding();

        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = new ArrayList<Handler>();
        }
        handlerList.add(handler);
        binding.setHandlerChain(handlerList);

    }
}
