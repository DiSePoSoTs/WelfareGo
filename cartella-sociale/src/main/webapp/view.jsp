<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale"%>
<%@page import="java.text.ParseException"%>
<%@page import="javax.xml.bind.JAXBException"%>
<%@page import="it.wego.welfarego.insiel.cartellasociale.client.Client"%>
<%@page import="it.wego.welfarego.persistence.entities.Utenti"%>
<%@page import="it.wego.welfarego.persistence.dao.UtentiDao"%>
<%@page import="it.wego.welfarego.persistence.entities.AnagrafeSoc"%>
<%@page import="it.wego.welfarego.persistence.dao.AnagrafeSocDao"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="it.wego.welfarego.persistence.utils.Connection"%>
<%@page import="it.wego.welfarego.xsd.cartellasociale.Serializer"%>

<link rel="stylesheet" type="text/css" href="CartellaSociale/css/cartellasociale.css">
<link rel="stylesheet" type="text/css" href="/ext-6.2.0/build/classic/theme-gray/resources/theme-gray-all.css">
<link rel="stylesheet" type="text/css" href="/ext-6.2.0/build/classic/theme-gray/resources/theme-gray-all_1.css">
<link rel="stylesheet" type="text/css" href="/ext-6.2.0/build/classic/theme-gray/resources/theme-gray-all_2.css">
<link rel="stylesheet" type="text/css" href="CartellaSociale/virtualkeyboard/css/virtualkeyboard.css">

<script type="text/javascript" src="/ext-6.2.0/build/ext-all-debug.js"></script>
<script type="text/javascript" src="/ext-6.2.0/build/classic/theme-gray/theme-gray.js"></script>

<script type="text/javascript" src="/CartellaSociale/app/utils/utility.js"></script>
<script type="text/javascript" src="/CartellaSociale/app/utils/types.js"></script>
<script type="text/javascript" src="/CartellaSociale/app/utils/components.js"></script>
<script type="text/javascript" src="/CartellaSociale/app/utils/search.js"></script>
<script type="text/javascript" src="/CartellaSociale/app/utils/wcsUtils.js"></script>
<script type="text/javascript" src="/CartellaSociale/js/wego-utils.js"></script>
<%@ include file="variables.jsp"%>
<%@ include file="stores.jsp"%>

<script type="text/javascript">
   
    Ext.Loader.setConfig({
        enabled:true
    });

    Ext.Loader.setPath('app', 'app');
    Ext.Loader.setPath('Ext.ux', '/CartellaSociale/virtualkeyboard');
    Ext.require([
        'Ext.toolbar.Paging',
        'Ext.window.MessageBox',
        'Ext.tip.*'
        ]);

    Ext.application({
        name: 'wcs',
        appFolder: '/CartellaSociale/app',
        controllers: [
            'CartellaController',
            'AnagraficaController',
            'AppuntamentiController',
            'FamigliaController',
            'CondizioneController',
            'ReferentiController',
            'PaiController',
            'AgendaController',
            'DiarioController'
        ],
        launch: function() {
            Ext.create('wcs.view.cartella.CartellaForm', {
                renderTo:'wcs_panel'
            });
        }
    });
    
</script>
<div id="wcs_panel"></div>