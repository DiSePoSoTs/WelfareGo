<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.wego.welfarego.utils.ComboStoreUtils"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <!-- <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/> -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Luogo Fieldset Test</title>
        <script type="text/javascript" src="ext/ext-all.js"></script>
        <link rel="stylesheet" type="text/css" href="ext/resources/css/cartellasociale.css">
        <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css">
        <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all-gray.css">
        <link rel="stylesheet" type="text/css" href="ext/virtualkeyboard/css/virtualkeyboard.css">
        <!--        <script type="text/javascript" src="/CartellaSociale/app/utils/utility.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/types.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/components.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/search.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/paiformutils.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/paiformutils.js"></script>
                <script type="text/javascript" src="/CartellaSociale/app/utils/wcsUtils.js"></script>-->
        <!--<script type="text/javascript" src="/CartellaSociale/cartellasociale.js"></script>-->
        <script type="text/javascript">
            var wcs=window.wcs=window.wcs||{};
            wcs.data=wcs.data||{};
            <%
                ComboStoreUtils comboStoreUtils=new ComboStoreUtils();
                try{
            %>
            wcs.data.combo_stato=<%=comboStoreUtils.getStatoComboDataStr()%>;
            wcs.data.combo_provincia=<%=comboStoreUtils.getProvinciaComboDataStr()%>;
            wcs.data.combo_comune=<%=comboStoreUtils.getComuneComboDataStr()%>;
            wcs.data.combo_via=<%=comboStoreUtils.getViaComboDataStr()%>;
            wcs.data.combo_civico=<%=comboStoreUtils.getCivicoComboDataStr()%>;
            <% 
                }finally{
                    comboStoreUtils.close();
                }
            %>
            Ext.Loader.setConfig({
                enabled: true
            });
            Ext.Loader.setPath('wcs', 'app');
            Ext.Loader.setPath('Ext.ux', '/CartellaSociale/virtualkeyboard');
            Ext.require([
                'Ext.toolbar.Paging',
                'Ext.window.MessageBox',
                'Ext.tip.*'
            ]);
//            Ext.application({
//                name: 'wcs',
//                appFolder: '/CartellaSociale/app',
//                controllers: [],
//                launch: function() {
            Ext.require('wcs.view.common.LuogoFieldSet');
            Ext.onReady(function() {
                Ext.create('Ext.form.FormPanel', {
                    title: 'Sample',
                    width: 1000,
                    bodyPadding: 10,
                    renderTo: 'ext_root_div',
                    id:'test_form',
                    items: [{
                            xtype: 'textfield',
                            fieldLabel: 'afield'
                        }, {
                            id:'test_residenza',
                            xtype: 'wcs_luogofieldset',
                            title: 'Dati Residenza',
                            namePrefix:'residenza',
                            extraLabel:'di residenza',
                            fieldDefaults:{
                                allowBlank:false
                            }
                        }]
                });
            });
//                }
//            });
        </script>
    </head>
    <body>
        <div id="ext_root_div"></div>
    </body>
</html>