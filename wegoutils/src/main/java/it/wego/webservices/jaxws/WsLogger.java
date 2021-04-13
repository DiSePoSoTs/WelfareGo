package it.wego.webservices.jaxws;

import it.wego.utils.xml.XmlUtils;
import java.util.Set;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.slf4j.Logger;

/**
 * 
 * @author aleph
 * @deprecated use it.wego.utils.xml.WsUtils
 */
@Deprecated
public class WsLogger implements SOAPHandler<SOAPMessageContext> {

    Logger logger;

    public WsLogger(Logger logger) throws Exception {
        this.logger = logger;
    }

    private void log(SOAPMessageContext context) {
        try {
            SOAPMessage message = context.getMessage();
            XmlUtils xmlUtils = XmlUtils.getInstance();
            String header = xmlUtils.xmlToString(message.getSOAPHeader()), body = xmlUtils.xmlToString(message.getSOAPBody());
            xmlUtils.close();
            String string = "\n\n" + header + "\n" + body + "\n";
            logger.debug(string);
        } catch (Exception e) {
            logger.error("error logging message", e);
        }
    }

    @Override
    public boolean handleMessage(SOAPMessageContext c) {
        log(c);
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext c) {
        log(c);
        return true;
    }

    @Override
    public void close(MessageContext c) {
    }

    @Override
    public Set getHeaders() {
        return null;
    }
}
