<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.UUID"%>
<%@page import="biz.no_ip.anyplace.blinkfish.BlinkFish"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.logging.log4j.LogManager"%>
<%@page import="org.apache.logging.log4j.Logger"%>
<%
	Logger logger = LogManager.getLogger("it.wego.welfarego.jsp.jspLoader");
	logger.debug("loading data");
	String uid = "it.wego.welfarego.jspLoader_" + Thread.currentThread().getName();
	Map<String, Object> attrs = (Map<String, Object>) BlinkFish.map.get(uid);
	if (attrs != null) {
		Enumeration<String> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Object value = session.getAttribute(key);
			logger.debug("copying attr : " + key + " = " + value);
			attrs.put(key, value);
			session.setAttribute(key, value); //sembra inutile ma non lo e' . ..  
		}

		String id = UUID.randomUUID().toString();
		BlinkFish.map.put(id, attrs);

		StringBuilder sb = new StringBuilder();
		sb.append("\n <script>");
		sb.append("\n document.cookie = \"it.wego.welfarego.jsp.jspLoader.sessionSharingId=" + id + ";path=/\";");
		sb.append("\n </script>");
		out.print(sb.toString());

	} else {
		logger.warn("loader invoked but BlinkFish map not found!");
	}
%>