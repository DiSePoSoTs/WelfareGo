package it.wego.welfarego.abstracts;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 *
 * @author aleph
 */
public class GenericFormServlet extends AbstractServlet {

    public static final String HANDLER_CLASS_PROP_NAME = "handler",
            FACTORY_CLASS_PROP_NAME = "handlerFactory";

    public GenericFormServlet() {
        super((AbstractFormFactory) null);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            String handlerName = config.getInitParameter(HANDLER_CLASS_PROP_NAME);
            if (handlerName != null) {
                Class<? extends AbstractForm> handlerClass = (Class<? extends AbstractForm>) Class.forName(handlerName);
                getLogger().debug("initializing generic servlet with handler : " + handlerClass.toString());
                setHandlerFactory(new SimpleAbstractFormFactory(handlerClass));
            } else {
                String factoryName = config.getInitParameter(FACTORY_CLASS_PROP_NAME);
                if (factoryName != null) {
                    Class<? extends AbstractFormFactory> factoryClass = (Class<? extends AbstractFormFactory>) Class.forName(factoryName);
                    getLogger().debug("initializing generic servlet with handler factory : " + factoryClass.toString());
                    setHandlerFactory(factoryClass.newInstance());
                }
            }
        } catch (Throwable e) {
            getLogger().error("error while initializing generic servlet", e);
        }
        if (getHandlerFactory() == null) {
            throw new ServletException("error while initializing generic servlet : unable to obtain handler factory");
        }
    }
}
