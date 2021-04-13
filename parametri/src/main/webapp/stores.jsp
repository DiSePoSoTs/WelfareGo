<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.wego.welfarego.persistence.entities.Parametri"%>
<%@page import="it.wego.welfarego.parametri.serializer.ParametriSerializer"%>
<%
    ParametriSerializer serializer = new ParametriSerializer();

    String anagraficaJson = serializer.serializeParametriJavascriptArray(Parametri.CODICE_UOT);
    String serviziJson = serializer.serializeParametriJavascriptArray(Parametri.CODICE_DEL_SERVIZIO);
    String abilitazioniJson = serializer.serializeParametriJavascriptArray(Parametri.LIVELLO_ABILITAZIONE);
    String associazioneJson = serializer.serializeAssociazioni();
    String strutturaJson = serializer.serializeParametriJavascriptArray(Parametri.STRUTTURE_ACCOGLIENZA);
    String unitamisureJson = serializer.serializeParametriJavascriptArray(Parametri.UNITA_MISURA);
    String classeInterventoJson = serializer.serializeParametriJavascriptArray(Parametri.CLASSE_INTERVENTO);
    String aliquotaIvaJson = serializer.serializeParametriJavascriptArray(Parametri.ALIQUOTA_IVA);
    String poJson= serializer.serializeParametriJavascriptArray(Parametri.PO);

    String templatesJson = serializer.serializeTemplatesCombo();
    String templatesMayBeNullJson = serializer.serializeTemplatesMayBeNullCombo();

    String listeAttesaJson = serializer.serializeListeAttesaCombo();

    String tipologieParametri = serializer.serializeTipologieParametroCombo();
    serializer.close();
%>
<script type="text/javascript">
    //JSP VARIABILI TO JS

    // preparo lo store per i combo uot
    var wp_anagraficaUOTStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= anagraficaJson%>
    });


    // preparo lo store per i combo servizi
    var wp_serviziStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= serviziJson%>
    });


    // preparo lo store per i combo livello abilitazione
    var wp_abilitazioniStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= abilitazioniJson%>
    });
    
    // preparo lo store per i combo associazione (enti)
    var wp_associazioneStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= associazioneJson%>
    });
    
    // preparo lo store per i combo P.O.
    var wp_poStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= poJson%>
    });

    // preparo lo store per i template
    var wp_templatesStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= templatesJson%>
    });
    var wp_templatesStore_mayBeNull = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= templatesMayBeNullJson%>
    });
    var wp_aliquotaIvaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= aliquotaIvaJson%>
    });

    // preparo lo store per le strutture
    var wp_strutturaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        listeners: {
            beforeload: function() {
                return false;
            }
        }
    });

    wp_strutturaStore.loadData([{name: '-', value: null}]);
    wp_strutturaStore.loadData(<%=strutturaJson%>, true);

    // tipo parametro unita di misura
    var wp_listeAttesaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        listeners: {
            beforeload: function() {
                return false;
            }
        }
    });

    wp_listeAttesaStore.loadData([{name: '-', value: null}]);
    wp_listeAttesaStore.loadData(<%=listeAttesaJson%>, true);

    // tipo parametro unita di misura
    var wp_unitamisuraStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%=unitamisureJson%>
    });

    var wp_classeInterventoStore = Ext.data.Store({
        fields: ['value', 'name'],
        data: <%=classeInterventoJson%>
    });

    var wp_tipologieParametriStore = Ext.data.Store({
        fields: ['value', 'name'],
        data: <%=tipologieParametri%>
    });


    // combo con valori S/N e label Si/No
    var wp_siNoComboStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: [
            {"value": "S", "name": "Si"},
            {"value": "N", "name": "No"},
        ]
    });
    
    // combo con valori S/N/C e label Si/No/Solo proroga
    var wp_rinnovoComboStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: [
            {"value": "S", "name": "Si"},
            {"value": "N", "name": "No"},
            {"value": "P", "name": "Solo proroga"},
            {"value": "D", "name": "In fase di determina"},
            {"value": "B", "name": "Si con stesso budget."},
        ]
    });

    // combo con valori S/N e label Si/No
    var wp_fatturazioneComboStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: [
            {"value": "S", "name": "Si"},
            {"value": "N", "name": "No"},
            {"value": "C", "name": "Contributo Abbattimento"}
        ]
    });

    // combo con valori S/N e label Si/No
    var wp_tipiDatoComboStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: [
            {"value": "T", "name": "Testo ( 1 Linea )"},
            {"value": "X", "name": "Testo ( Multilinea )"},
            {"value": "I", "name": "Numerico"},
            {"value": "D", "name": "Data"},
            {"value": "L", "name": "Lista"},
        ]
    });


    // ricarico gli store che sono collegati tra loro nelle varie tab
    function refreshRemoteStores()
    {

        // qui andrò ad aggiornare i vari store dei cmp che
        // mi interessa mantenere sincronizzati

    }


</script>