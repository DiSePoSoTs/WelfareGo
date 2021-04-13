/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.abstracts;

import biz.no_ip.anyplace.blinkfish.BlinkFish;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericAbstractServlet extends HttpServlet {

    public static final String METHOD_POST = "POST",
            METHOD_GET = "GET",
            METHOD_PROP = "it.wego.welfarego.abstracts.AbstractServlet.method",
            SESSION_ATTR = "it.wego.welfarego.abstracts.AbstractServlet.session",
            SERVLET_REQUEST_ATTR = "it.wego.welfarego.abstracts.AbstractServlet.request",
            SERVLET_RESPONSE_ATTR = "it.wego.welfarego.abstracts.AbstractServlet.response";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String contentType = "application/json", characterEncoding = "UTF-8";

    @Override
    public void init() {
        logger.debug("servlet ready");
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, METHOD_GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, METHOD_POST);
    }

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response, String method) throws ServletException, IOException;

    @Override
    public String getServletInfo() {
        return "AbstractServlet Servlet";
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Object> getAttributes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> attrs = new HashMap<String, Object>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Enumeration keys = session.getAttributeNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                Object value = session.getAttribute(key);
                attrs.put(key, value);
            }
            attrs.put(SESSION_ATTR, session);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase("it.wego.welfarego.jsp.jspLoader.sessionSharingId")) {
                Map<String, Object> attrFromCookie = (Map<String, Object>) BlinkFish.map.get(cookie.getValue());
                attrs.putAll(attrFromCookie);
            }
        }
        //getLogger().debug("loaded session attrs : " + JSonUtils.getGsonPrettyPrinting().toJson(attrs));
        return attrs;
    }
}
