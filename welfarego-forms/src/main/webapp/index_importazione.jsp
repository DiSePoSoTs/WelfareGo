<%--
    Document   : index
    Created on : 17-giu-2011, 9.37.33
    Author     : giuseppe
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.wego.welfarego.importazione.servlet.ImportazionePortletForm"%>
<%@page import="it.wego.persistence.PersistenceAdapter"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reports</title>
        <script type="text/javascript" src="ext/ext-all.js"></script>
        <link rel="stylesheet" type="text/css" href="css/extjs_liferay.css">
        <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css">
        <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all-gray.css">
        <link rel="stylesheet" type="text/css" href="css/cassa.css">
        <!--        <link rel="stylesheet" type="text/css" href="ext/virtualkeyboard/css/virtualkeyboard.css"> -->
    </head>
    <body>
<% 
    ImportazionePortletForm importazione = new ImportazionePortletForm();
    importazione.doView(out);
%>
    </body>
</html>
