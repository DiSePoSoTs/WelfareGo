/*
 * Copyright (C) 2012 aleph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.wego.utils.xml;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Set;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

/**
 *
 * @author aleph
 */
@Beta
public class WsUtils {

    private BindingProvider bindingProvider;
    private Logger suppliedLogger;
    private String dumpPrefix = "message", endpointUrl;
    private LogMode logMode = LogMode.NEW_FILES;

    private WsUtils() {
    }

    public static WsUtils newInstance() {
        return new WsUtils();
    }

    /**
     * will be cast to BindingProvider
     *
     * @param serviceInterface
     * @return
     */
    public WsUtils withServiceInterface(Object serviceInterface) {
        Preconditions.checkArgument(serviceInterface instanceof BindingProvider);
        return this.withBindingProvider((BindingProvider) serviceInterface);
    }

    public WsUtils withBindingProvider(BindingProvider bindingProvider) {
        this.bindingProvider = bindingProvider;
        return this;
    }

    public WsUtils withLogger(Logger suppliedLogger) {
        this.suppliedLogger = suppliedLogger;
        return this;
    }

    /**
     * NEW_FILES = create new temp files for each dump SAME_FILES = use
     * incremental counted files, useful for debug
     *
     * @param logMode
     * @return
     */
    public WsUtils withLogMode(LogMode logMode) {
        this.logMode = logMode;
        return this;
    }

    public WsUtils withEndpoint(String endpointUrl) {
        this.endpointUrl = Strings.emptyToNull(endpointUrl);
        return this;
    }

    public WsUtils withDumpPrefix(String dumpPrefix) {
        this.dumpPrefix = Strings.nullToEmpty(dumpPrefix);
        return this;
    }

    public WsUtils setEndpointForService() {
        Preconditions.checkNotNull(bindingProvider);
        Preconditions.checkNotNull(endpointUrl);
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        return this;
    }

    public WsUtils addLoggerToService() {
        Preconditions.checkNotNull(suppliedLogger);
        Preconditions.checkNotNull(dumpPrefix);
        if (suppliedLogger.isDebugEnabled()) {
            addHandlerToService(getWsLogger());
        }
        return this;
    }

    public static enum LogMode {

        NEW_FILES, SAME_FILES
    }

    public void addHandlerToService(SOAPHandler handler) {
        Binding binding = bindingProvider.getBinding();
        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = Lists.newArrayList();
        }
        handlerList.add(handler);
        binding.setHandlerChain(handlerList);
    }

    public SOAPHandler<SOAPMessageContext> getWsLogger() {

        return new SOAPHandler<SOAPMessageContext>() {
            private final Logger logger = suppliedLogger;
            private final String localDumpPrefix = dumpPrefix;
            private final LogMode logMode = WsUtils.this.logMode;
            private long counter = 1;

            private void log(SOAPMessageContext context) {
                if (logger.isDebugEnabled()) {
                    try {

                        File file = logMode.equals(LogMode.NEW_FILES) ? File.createTempFile(localDumpPrefix + "_", ".soap.xml") : new File(localDumpPrefix + "_" + (counter++) + ".soap.xml");
                        SOAPMessage message = context.getMessage();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        message.writeTo(out);
                        String formatXml = XmlUtils.getInstance().formatXml(out.toString());
                        FileUtils.writeStringToFile(file, formatXml);
                        logger.debug("dumped soap message {} to file {}", localDumpPrefix, file.getAbsolutePath());
                    } catch (Throwable e) {
                        logger.error("error logging message", e);
                    }
                }
            }

            public boolean handleMessage(SOAPMessageContext c) {
                log(c);
                return true;
            }

            public boolean handleFault(SOAPMessageContext c) {
                log(c);
                return true;
            }

            public void close(MessageContext c) {
            }

            public Set getHeaders() {
                return null;
            }
        };
    }
}
