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
package it.wego.extjs.servlet;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import it.wego.extjs.json.JsonBuilder;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;


import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public abstract class AbstractJsonServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static final Object SKIP_RESPONSE = new Object();
	
    public static final String ACTION = "action", DATA = "data";
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private Gson gson = new Gson();
    
    private ThreadLocal<JsonServletRequest> currentRequest = new ThreadLocal<JsonServletRequest>();
    
    public static final String MULTIPART_FILENAME = "_filename", MULTIPART_BASE64 = "_base64", MULTIPART_CONTENT_TYPE = "_content_type";

    private class JsonServletRequest {

        private final HttpServletRequest httpServletRequest;
        private Map<String, Object> multipartData;
        private Map<String, String> allParameters;

        public JsonServletRequest(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
        }

        public HttpServletRequest getHttpServletRequest() {
            return httpServletRequest;
        }

        public boolean isMultipart() {
            return !getMultipartData().isEmpty();
        }

        public Map<String, Object> getMultipartData() {
            if (multipartData == null) {
                if (ServletFileUpload.isMultipartContent(getHttpServletRequest())) {
                    multipartData = Maps.newHashMap();
                    try {
                        FileItemFactory fileItemFactory = new DiskFileItemFactory();
                        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
                        FileItemIterator itemIterator = servletFileUpload.getItemIterator(httpServletRequest);
                        while (itemIterator.hasNext()) {
                            FileItemStream fileItemStream = itemIterator.next();
                            String name = fileItemStream.getFieldName();
                            if (fileItemStream.isFormField()) {
                                String value = IOUtils.toString(fileItemStream.openStream());
                                multipartData.put(name, value);
                            } else {
                                byte[] data = IOUtils.toByteArray(fileItemStream.openStream());
                                String fileName = new File(fileItemStream.getName()).getName(), contentType = fileItemStream.getContentType();
                                multipartData.put(name, data);
                                multipartData.put(name + MULTIPART_FILENAME, fileName);
                                multipartData.put(name + MULTIPART_CONTENT_TYPE, contentType);
                                multipartData.put(name + MULTIPART_BASE64, Base64.encodeBase64String(data));
                            }
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    multipartData = Collections.emptyMap();
                }
            }
            return multipartData;
        }

        public @Nullable
        String getParameter(String key) {
            return Optional.fromNullable(getHttpServletRequest().getParameter(key)).or(Optional.fromNullable((String) getMultipartData().get(key))).orNull();
        }

        private Map<String, String> getParameters() {
            if (allParameters == null) {
                allParameters = Maps.newHashMap();
                for (Entry entry : Iterables.concat(getMultipartData().entrySet(), getHttpServletRequest().getParameterMap().entrySet())) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        allParameters.put(key, (String) value);
                    } else if (value instanceof String[] && ((String[]) value).length > 0) {
                        allParameters.put(key, ((String[]) value)[0]);
                    } else {
                        // do nothing
                    }
                }
            }
            return allParameters;
        }
    }

    public static enum Method {

        GET, POST, PUT, DELETE
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Deprecated
    public String getActionStr() {
        return getAction();
    }

    public String getAction() {
        return getParameter(ACTION);
    }

    public String getRequestPayload() {
        try {
            return IOUtils.toString(getHttpServletRequest().getReader());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public @Nullable
    Map<String, String> getRequestPayloadAsMap() {
        String data = Strings.emptyToNull(getRequestPayload());
        return data == null ? null : gson.<Map<String, String>>fromJson(data, JsonBuilder.MAP_OF_STRINGS);
    }

    /**
     *
     * @return @deprecated use getHttpServletRequest() or getParameter(String
     * key)
     */
    @Deprecated
    public HttpServletRequest getRequest() {
        return currentRequest.get().getHttpServletRequest();
    }

    public HttpServletRequest getHttpServletRequest() {
        return currentRequest.get().getHttpServletRequest();
    }

    public @Nullable
    String getParameter(String key) {
        return currentRequest.get().getParameter(key);
    }

    public <E extends Enum<E>> E getAction(Class<E> clazz) {
        String actionStr = getAction();
        return Strings.isNullOrEmpty(actionStr) ? null : Enum.valueOf(clazz, actionStr.toUpperCase());
    }
    
    private final static DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    public <T> T getParameter(String key, Class<T> clazz) {
    	
        String dataStr = getParameter(key);
        
        if (Strings.isNullOrEmpty(dataStr)) {
            return null;
        }
        
        if (Objects.equal(clazz.getPackage().getName(), "java.lang")) {
            return (T) ConvertUtils.convert(dataStr, clazz);
        }
        
        if (Objects.equal(clazz, Date.class)) {
            if (dataStr.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$")) {
                try {
                    return (T) dateFormat1.parse(dataStr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Preconditions.checkArgument(false, "unparsable date format for date '%s'", dataStr);
            }
        }
        if (Objects.equal(clazz, DateTime.class)) {
            if (dataStr.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$")) {
                return (T) ISODateTimeFormat.date().parseDateTime(dataStr);
            } else {
                return (T) ISODateTimeFormat.dateTime().parseDateTime(dataStr);
            }
        }
        if (Objects.equal(clazz, LocalDateTime.class)) {
            return (T) new LocalDateTime(dataStr);
        }
        return gson.fromJson(dataStr, clazz);
    }

    public <T> T getParameter(String key, Type type) {
        String dataStr = getParameter(key);
        return (T) (Strings.isNullOrEmpty(dataStr) ? null : gson.fromJson(dataStr, type));
    }

    public Map<String, String> getData() {
        return getParameter(DATA, JsonBuilder.MAP_OF_STRINGS);
    }

    public <T> T getData(Class<T> clazz) {
        return getParameter(DATA, clazz);
    }

    public Map<String, String> getParameters() {
        return currentRequest.get().getParameters();
    }

    protected abstract Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception;

    private PrintWriter getWriter(PrintWriter writer, HttpServletResponse response) throws IOException {
        if (writer != null) {
            return writer;
        }
        if (currentRequest.get().isMultipart()) {
            response.setContentType("text/html; charset=UTF-8"); //workaround for iframe ajas file submit
        } else {
            response.setContentType("application/json; charset=UTF-8");
        }
        return response.getWriter();
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws IOException {
        PrintWriter writer = null;
        currentRequest.set(new JsonServletRequest(request));
        try {
            Object object = handleJsonRequest(request, response, method);
            if (SKIP_RESPONSE != object) {
                writer = getWriter(writer, response);
                gson.toJson(object, writer);
            }
        } catch (Exception ex) {
            logError(ex);
            writer = getWriter(writer, response);
            gson.toJson(JsonBuilder.newInstance().withError(ex).buildResponse(), writer);
        } finally {
            currentRequest.remove();
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    private void logError(Exception ex) {
        try {
            logger.error("error", ex);
            if (logger.isDebugEnabled()) {
                logger.debug("above request form {} with data : {}", getRequest().getRemoteAddr(), JsonBuilder.getGsonPrettyPrinting().toJson(getParameters()));
            }
        } catch (Exception ex2) {
            ex.printStackTrace();
            ex2.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, Method.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, Method.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, Method.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, Method.DELETE);
    }
}
