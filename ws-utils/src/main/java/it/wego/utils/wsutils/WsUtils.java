package it.wego.utils.wsutils;

import com.google.common.base.Preconditions;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class WsUtils {

    private static final Logger logger = LoggerFactory.getLogger(WsUtils.class);

    public static OMElement invokeWs(Map<String, String> config) throws AxisFault, XMLStreamException {
        return invokeWs(config.get("endpoint"), config.get("operation"), config.get("requestPayload"));
    }

    public static OMElement invokeWs(String endpoint, String operation, String requestPayload) throws AxisFault, XMLStreamException {
        return invokeWs(endpoint, operation, AXIOMUtil.stringToOM(requestPayload));
    }

    public static OMElement invokeWs(String endpoint, String operation, OMElement requestPayload) throws AxisFault {
        Validate.notNull(endpoint);
        Validate.notNull(operation);
        Validate.notNull(requestPayload);
        logger.debug("invoking ws {} {}", endpoint, operation);
        ServiceClient serviceClient = new ServiceClient();

        Options options = new Options();
        options.setTo(new EndpointReference(endpoint));
        options.setAction(operation);
        serviceClient.setOptions(options);

        return serviceClient.sendReceive(requestPayload);
    }

    public static WsUtils getInstance() {
        return new WsUtils();
    }
    private Configuration configuration;
    private Template template;
    private String endpoint, operation, responseString, templateName;
    private Object data;
    private OMElement responseOMElement;

    private WsUtils() {
        configuration = new Configuration();
    }

    public WsUtils withConfiguration(Configuration configuration) {
        Preconditions.checkNotNull(configuration);
        this.configuration = configuration;
        return this;
    }

    public WsUtils withTemplate(Template template) {
        Preconditions.checkNotNull(template);
        this.template = template;
        return this;
    }

    public WsUtils withData(Object data) {
        Preconditions.checkNotNull(data);
        this.data = data;
        return this;
    }

    public WsUtils withEndpoint(String endpoint) {
        Preconditions.checkNotNull(endpoint);
        this.endpoint = endpoint;
        return this;
    }

    public WsUtils withOperation(String operation) {
        Preconditions.checkNotNull(operation);
        this.operation = operation;
        return this;
    }

    public WsUtils withTemplateName(String templateName) {
        Preconditions.checkNotNull(templateName);
        this.templateName = templateName;
        return this;
    }

    public WsUtils invokeWs() {
        Preconditions.checkNotNull(getTemplate());
        Preconditions.checkNotNull(data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            template.process(data, new OutputStreamWriter(byteArrayOutputStream));
        } catch (TemplateException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String payLoad = byteArrayOutputStream.toString();
        try {
            responseOMElement = invokeWs(endpoint, operation, payLoad);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
        Preconditions.checkNotNull(responseOMElement);
        return this;
    }

    public @Nullable Template getTemplate() {
        if (template == null && templateName != null) {
            try {
                template = configuration.getTemplate(templateName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return template;
    }

    public String getResponseString() {
        if (responseString == null) {
            responseString = getResponseOMElement().toString();
        }
        return responseString;
    }

    public OMElement getResponseOMElement() {
        if (responseOMElement == null) {
            invokeWs();
        }
        return responseOMElement;
    }
}
