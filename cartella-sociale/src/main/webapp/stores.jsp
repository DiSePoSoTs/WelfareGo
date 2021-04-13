<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="it.wego.welfarego.persistence.utils.Connection"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="it.wego.welfarego.persistence.dao.TipologiaInterventoDao"%>
<%@page import="it.wego.welfarego.servlet.pai.TipologiaInterventoServlet"%>
<%@page import="it.wego.persistence.PersistenceAdapterFactory"%>
<%@page import="it.wego.persistence.PersistenceAdapter"%>
<%@page import="it.wego.welfarego.persistence.entities.TipologiaParametri"%>
<%@page import="it.wego.welfarego.persistence.entities.Parametri"%>
<%@page import="it.wego.welfarego.serializer.ParametriSerializer"%>
<%
    ParametriSerializer serializer = new ParametriSerializer();
    String anagraficaSegnalatoDaArray = serializer.serializeJavascriptArray(Parametri.SEGNALATO_DA);
    String anagraficaPosizione = serializer.serializeJavascriptArray(Parametri.POSIZIONE_ANAGRAFICA);
    String anagraficaNazionalita = serializer.serializeJavascriptArray(Parametri.NAZIONALITA);
    String anagraficaUOT = serializer.serializeJavascriptArray(Parametri.CODICE_UOT);
    String condizioneStatoCivile = serializer.serializeJavascriptArray(Parametri.STATO_CIVILE);
    String condizioneTitoloDiStudio = serializer.serializeJavascriptArray(Parametri.SCOLARITA);
    String condizioneCondizioneProfessionale = serializer.serializeJavascriptArray(Parametri.CONDIZIONE_PROFESSIONALE);
    String condizioneCondizioneFamigliare = serializer.serializeJavascriptArray(Parametri.TIPOLOGIA_NUCLEO_FAMIGLIARE);
    String condizioneAbitazione = serializer.serializeJavascriptArray(Parametri.TIPOLOGIA_ABITATIVA);
    String condizioneReddito = serializer.serializeJavascriptArray(Parametri.FORMA_DI_REDDITO);
    String condizioneStatoFisico = serializer.serializeJavascriptArray(Parametri.STATO_FISICO);
    String condizioneAccompagnamento = "[{value: 'S', name: 'Si'}, {value: 'N', name: 'No'}]";
    String habitat = "[{value: 'S', name: 'Si'}, {value: 'N', name: 'No'}]";
    String sesso = "[{value: 'M', name: 'Maschio'}, {value: 'F', name:'Femmina'}]";
    String famigliaGradoParentela = serializer.serializeJavascriptArray(Parametri.GRADO_DI_PARENTELA);
    String referentiQualifica = serializer.serializeJavascriptArray(Parametri.QUALIFICA);
    String paiDiagnosiSocialePrincipale = serializer.serializeJavascriptArray(Parametri.DIAGNOSI_SOCIALE_PRINCIPALE);

    String interventoClasse = serializer.serializeJavascriptArray(Parametri.CLASSE_INTERVENTO);
    String classe_interventi_WelfareGo  = serializer.serializeJavascriptArray(true, Parametri.CLASSE_INTERVENTO);

    String interventoEsito = serializer.serializeJavascriptArray(Parametri.ESITO_INTERVENTO);
    String interventoStato = "[{value: 'A', name: 'Aperto'}, {value: 'C', name: 'Chiuso'}, {value: 'E', name: 'Esecutivo'}, {value: 'S', name: 'Sospeso'}]";
    String motivazioneChiusura = serializer.serializeJavascriptArray(Parametri.MOTIVAZIONI_CHIUSURA);
    String interventoStruttura = serializer.serializeJavascriptArray(Parametri.STRUTTURE_ACCOGLIENZA);
    String interventoFasciaReddito = serializer.serializeJavascriptArray(Parametri.FASCIA_DI_REDDITO);
    String interventoAssistenzaFVG = "[{value: 'S', name: 'Si'}, {value: 'N', name: 'No'}]";
    String paiDemenza = "[{value: 'S', name: 'Si'}, {value: 'N', name: 'No'}]";
    String invaliditaCivile = "[{value: '', name: 'Non invalido'}, {value: '1', name: 'Invalido dal 34% al 66%'}, {value: '2', name: 'Invalido dal 67 al 73%'}, {value: '3', name: 'Invalido dal 74% al 99%'}, {value: '4', name: 'Invalido 100% - Necessitï¿½ di assistenza'}, {value: '5', name: 'Invalido 100% - Inabile al lavoro'}, {value: '6', name: 'Invalido 100% - Impossibilitï¿½ deambulazione'}]";
    

    String tipoIntervento;
    {
        EntityManager em = Connection.getEntityManager();
        tipoIntervento = new Gson().toJson(TipologiaInterventoServlet.buildStoreResponse(new TipologiaInterventoDao(em).findAllVisibili()).getData());
        em.close();
    }
    int fasciaRedditoDefault = serializer.fasciaDiRedditoDefault();
%>
<script type="text/javascript">
    
    var wcs_anagraficaUOTArray = <%= anagraficaUOT%>;
    var wcs_anagraficaSegnalatoDaArray = <%= anagraficaSegnalatoDaArray%>;
    var wcs_anagraficaPosizioneArray = <%= anagraficaPosizione%>;
    var wcs_sessoArray = <%= sesso%>;
    var wcs_anagraficaNazionalitaArray = <%= anagraficaNazionalita%>;
    var wcs_condizioneStatoCivileArray = <%=condizioneStatoCivile%>;
    var wcs_condizioneTitoloDiStudioArray = <%=condizioneTitoloDiStudio%>;
    var wcs_condizioneCondizioneProfessionaleArray = <%=condizioneCondizioneProfessionale%>;
    var wcs_condizioneCondizioneFamigliareArray = <%=condizioneCondizioneFamigliare%>;
    var wcs_condizioneAbitazioneArray = <%=condizioneAbitazione%>;
    var wcs_condizioneRedditoArray = <%=condizioneReddito%>;
    var wcs_condizioneStatoFisicoArray = <%=condizioneStatoFisico%>;
    var wcs_condizioneAccompagnamentoArray = <%=condizioneAccompagnamento%>;
    var wcs_famigliaParentelaArray = <%=famigliaGradoParentela%>;
    var wcs_referentiQualificaArray = <%=referentiQualifica%>;
    var wcs_paiDiagnosiSocialePrincipaleArray = <%=paiDiagnosiSocialePrincipale%>;

    var wcs_interventoClasseArray = <%=interventoClasse%>;
    var wcs_classe_interventi_WelfarGo_Array = <%=classe_interventi_WelfareGo%>;

    var wcs_interventoEsitoArray = <%=interventoEsito%>;
    var wcs_motivazioneChiusuraArray = <%= motivazioneChiusura%>;
    var wcs_interventoStatoArray = <%= interventoStato%>;
    var wcs_interventoStrutturaArray = <%= interventoStruttura%>;
    var wcs_interventoFasciaDiRedditoArray = <%= interventoFasciaReddito%>;
    var wcs_interventoAssistenzaFVGArray = <%=  interventoAssistenzaFVG%>;
    var wcs_paiDemenzaArray = <%=  paiDemenza%>;
    var wcs_habitatArray = <%=  habitat%>;
    var wcs_fasciaDiRedditoDefaultCombo = '<%= fasciaRedditoDefault%>';
    var wcs_invaliditaCivileArray = <%= invaliditaCivile%>;

        
    Ext.Object.each({
        'tipologiaResidenzaStore':<%=serializer.serializeJavascriptArray(Parametri.TIPOLOGIA_RESIDENZA)%>,
        'certificatoL104Store':<%=serializer.serializeJavascriptArray(Parametri.CERTIFICATO_L104)%>,
        'provvedimentoGiudiziarioComboStore':<%=serializer.serializeJavascriptArray(Parametri.PROVVEDIMENTO_GIUDIZIARIO)%>,
        'famigliaQualificaParentelaComboStore':<%=serializer.serializeJavascriptArray(Parametri.QUALIFICA, Parametri.GRADO_DI_PARENTELA)%>,
        'referentiQualificaComboStore':<%=serializer.serializeJavascriptArray(Parametri.GRADO_DI_PARENTELA, Parametri.QUALIFICA)%>,
        'macroProblematicheStore':<%=serializer.serializeJavascriptArray(Parametri.MACRO_PROBLEMATICHE)%>,
        'microProblematicheStore':<%=serializer.serializeJavascriptArray(Parametri.MICRO_PROBLEMATICHE)%>,
        'problematicheRilevanzaComboStore':<%=serializer.serializeJavascriptArray(Parametri.PROBLEMATICA_RILEVANZA)%>,
        'problematicheFronteggiamentoComboStore':<%=serializer.serializeJavascriptArray(Parametri.PROBLEMATICA_FRONTEGGIAMENTO)%>,
        'problematicheObiettivoPrevalenteComboStore':<%=serializer.serializeJavascriptArray(Parametri.PROBLEMATICA_OBIETTIVO_PREVALENTE)%>
    }, function(storeId,data){
        Ext.create('Ext.data.Store',{
            storeId: storeId,
            fields: ['value','name','param','code'],
            data:data
        });
    });
    
    var wcs_anagraficaNazionalitaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_anagraficaNazionalitaArray
    });
    
    var wcs_anagraficaCittadinanzaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: <%= serializer.serializeCittadinanzaCombo() %>
    });

    var wcs_anagraficaUOTStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_anagraficaUOTArray
    });

    var wcs_anagraficaSegnalatoDaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_anagraficaSegnalatoDaArray
    });

    var wcs_anagraficaPosizioneStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_anagraficaPosizioneArray
    });

    var wcs_sessoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_sessoArray
    });

    var wcs_condizioneStatoCivileStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneStatoCivileArray
    });

    var wcs_condizioneTitoloDiStudioStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneTitoloDiStudioArray
    });

    var wcs_condizioneCondizioneProfessionaleStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneCondizioneProfessionaleArray
    });

    var wcs_condizioneCondizioneFamigliareStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneCondizioneFamigliareArray
    });

    var wcs_condizioneAbitazioneStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneAbitazioneArray
    });

    var wcs_condizioneRedditoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneRedditoArray
    });

    var wcs_condizioneStatoFisicoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneStatoFisicoArray
    });

    var wcs_condizioneAccompagnamentoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_condizioneAccompagnamentoArray
    });
    
    var wcs_invaliditaCivileCombo = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_invaliditaCivileArray
    });

    var wcs_famigliaParentelaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_famigliaParentelaArray
    });
    
    var wcs_referentiQualificaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_referentiQualificaArray
    });

    var wcs_paiDiagnosiSocialePrincipaleStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_paiDiagnosiSocialePrincipaleArray
    });

    var wcs_interventoClasseStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_classe_interventi_WelfarGo_Array
    });
      
    var wcs_interventoEsitoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_interventoEsitoArray
    });

    var wcs_interventoStatoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_interventoStatoArray
    });

    var wcs_interventoStrutturaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_interventoStrutturaArray
    });

    var wcs_interventoFasciaDiRedditoStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_interventoFasciaDiRedditoArray
    });

    var wcs_interventoAssistenzaFVGStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_interventoAssistenzaFVGArray
    });

    var wcs_motivazioneChiusuraStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_motivazioneChiusuraArray
    });

    var wcs_paiDemenzaStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_paiDemenzaArray
    });

    var wcs_paiHabitatStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: wcs_habitatArray
    });
    
    var wcs_tipologiaInterventoStore = new Ext.data.Store({
        fields: ['name','value',  'label', 'impStdCosto',  'flgFineDurata', 'maxDurataMesi','cntTipint' ,'codClasse'],
        data: <%= tipoIntervento %>
    });

</script>
