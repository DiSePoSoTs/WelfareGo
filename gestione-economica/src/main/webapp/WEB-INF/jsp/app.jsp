<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ page import="javax.portlet.*"%>

<%!
RenderResponse renderResponse;
%>
<portlet:defineObjects />

<portlet:resourceURL var="urlListaClasseTipoInterventoAcquisizioni" id="listaClasseTipoInterventoAcquisizioni" escapeXml="false" />
<portlet:resourceURL var="urlListaTipoInterventoAcquisizioni" id="listaTipoInterventoAcquisizioni" escapeXml="false" />
<portlet:resourceURL var="urlListaClasseTipoInterventoFatturazioni" id="listaClasseTipoInterventoFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlListaTipoInterventoFatturazioni" id="listaTipoInterventoFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlListaClasseTipoInterventoPagamenti" id="listaClasseTipoInterventoPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlListaTipoInterventoPagamenti" id="listaTipoInterventoPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlListaUOT" id="listaUOT" escapeXml="false" />
<portlet:resourceURL var="urlListaImpegni" id="listaImpegni" escapeXml="false" />


<portlet:resourceURL var="urlCercaAcquisizioni" id="cercaAcquisizioni" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioAcquisizioni" id="dettaglioAcquisizioni" escapeXml="false" />
<portlet:resourceURL var="urlModificaAcquisizioni" id="modificaAcquisizioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaAcquisizioni" id="salvaAcquisizioni" escapeXml="false" />

<portlet:resourceURL var="urlCercaFatture" id="cercaFatture" escapeXml="false" />
<portlet:resourceURL var="urlSalvaStatoFatture" id="salvaStatoFatture" escapeXml="false" />
<portlet:resourceURL var="urlCercaFattureDaGenerare" id="cercaFattureDaGenerare" escapeXml="false" />
<portlet:resourceURL var="urlInviaFatture" id="inviaFatture" escapeXml="false" />
<portlet:resourceURL var="urlAnteprimaFatture" id="anteprimaFatture" escapeXml="false" />

<portlet:resourceURL var="urlCercaVociFattura" id="cercaVociFattura" escapeXml="false" />
<portlet:resourceURL var="urlSalvaVociFattura" id="salvaVociFattura" escapeXml="false" />
<portlet:resourceURL var="urlCercaDatiFatturazioni" id="cercaDatiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaFattura" id="salvaFattura" escapeXml="false" />
<portlet:resourceURL var="urlCercaMesiPrecedentiFatturazioni" id="cercaMesiPrecedentiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaMesiPrecedentiFatturazioni" id="salvaMesiPrecedentiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlCercaQuoteObbligatiFatturazioni" id="cercaQuoteObbligatiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaQuoteObbligatiFatturazioni" id="salvaQuoteObbligatiFatturazioni" escapeXml="false" />



<portlet:resourceURL var="urlDettaglioVociFattura" id="dettaglioVociFattura" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioVociFattura" id="salvaDettaglioVociFattura" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioDatiFatturazioni" id="dettaglioDatiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioFattura" id="salvaDettaglioFattura" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioMesiPrecedentiFatturazioni" id="dettaglioMesiPrecedentiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioMesiPrecedentiFatturazioni" id="salvaDettaglioMesiPrecedentiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioQuoteObbligatiFatturazioni" id="dettaglioQuoteObbligatiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioQuoteObbligatiFatturazioni" id="salvaDettaglioQuoteObbligatiFatturazioni" escapeXml="false" />


<%/*Fattura Nuova*/%>
<portlet:resourceURL var="urlCercaMesiPrecedentiFatturazioniNuova" id="cercaMesiPrecedentiFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlSalvaMesiPrecedentiFatturazioniNuova" id="salvaMesiPrecedentiFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlCercaDatiFatturazioniNuova" id="cercaDatiFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlSalvaFatturaNuova" id="salvaFatturaNuova" escapeXml="false" />
<portlet:resourceURL var="urlCercaQuoteObbligatiFatturazioniNuova" id="cercaQuoteObbligatiFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlSalvaQuoteObbligatiFatturazioniNuova" id="salvaQuoteObbligatiFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlSalvaVociFatturazioniNuova" id="salvaVociFatturazioniNuova" escapeXml="false" />
<portlet:resourceURL var="urlCercaAnagraficaFatture" id="cercaAnagraficaFatture" escapeXml="false" />
<portlet:resourceURL var="urlListaTipologiaInterventiFatturazioniNuova" id="listaTipologiaInterventiFatturazioniNuova" escapeXml="false" />
<%/*Fine Fattura Nuova*/%>


<portlet:resourceURL var="urlListaModalitaPagamantiFatturazioni" id="listaModalitaPagamantiFatturazioni" escapeXml="false" />
<portlet:resourceURL var="urlListaValoriIvaFatturazioni" id="listaValoriIvaFatturazioni" escapeXml="false" />

<portlet:resourceURL var="urlCercaPagamenti" id="cercaPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlSalvaStatoPagamenti" id="salvaStatoPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlCercaPagamentiDaGenerare" id="cercaPagamentiDaGenerare" escapeXml="false" />
<portlet:resourceURL var="urlInviaPagamenti" id="inviaPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlAnteprimaFilePagamenti" id="anteprimaFilePagamenti" escapeXml="false" />

<portlet:resourceURL var="urlCercaVociPagamento" id="cercaVociPagamento" escapeXml="false" />
<portlet:resourceURL var="urlSalvaVociPagamento" id="salvaVociPagamento" escapeXml="false" />
<portlet:resourceURL var="urlCercaDatiPagamento" id="cercaDatiPagamento" escapeXml="false" />
<portlet:resourceURL var="urlSalvaPagamento" id="salvaPagamento" escapeXml="false" />
<portlet:resourceURL var="urlSalvaNuoviPagamenti" id="salvaNuoviPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioVociPagamento" id="dettaglioVociPagamento" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioVociPagamento" id="salvaDettaglioVociPagamento" escapeXml="false" />
<portlet:resourceURL var="urlDettaglioDatiPagamento" id="dettaglioDatiPagamento" escapeXml="false" />
<portlet:resourceURL var="urlSalvaDettaglioPagamento" id="salvaDettaglioPagamento" escapeXml="false" />

<portlet:resourceURL var="urlListaModalitaPagamantiPagamenti" id="listaModalitaPagamantiPagamenti" escapeXml="false" />

<%/*Pagamento Nuovo*/%>
<portlet:resourceURL var="urlCercaDatiPagamentiNuovo" id="cercaDatiPagamentiNuovo" escapeXml="false" />
<portlet:resourceURL var="urlSalvaPagamentoNuovo" id="salvaPagamentoNuovo" escapeXml="false" />
<portlet:resourceURL var="urlSalvaVociPagamentiNuovo" id="salvaVociPagamentiNuovo" escapeXml="false" />
<portlet:resourceURL var="urlCercaAnagraficaPagamenti" id="cercaAnagraficaPagamenti" escapeXml="false" />
<portlet:resourceURL var="urlListaTipologiaInterventiPagamentiNuovo" id="listaTipologiaInterventiPagamentiNuovo" escapeXml="false" />
<portlet:resourceURL var="urlListaDecretiNuovo" id="listaDecretiNuovo" escapeXml="false" />
<%/*Fine Pagamento Nuovo*/%>

<portlet:resourceURL var="urlMotivazioneVariazioneSpesa" id="motivazioneVariazioneSpesa" escapeXml="false" />

<script type="text/javascript" >
    var wp_url_servizi = {
        ListaClasseTipoInterventoAcquisizioni: "<%=renderResponse.encodeURL(urlListaClasseTipoInterventoAcquisizioni.toString())%>",
        ListaTipoInterventoAcquisizioni: "<%=renderResponse.encodeURL(urlListaTipoInterventoAcquisizioni.toString())%>",
        ListaClasseTipoInterventoFatturazioni: "<%=renderResponse.encodeURL(urlListaClasseTipoInterventoFatturazioni.toString())%>",
        ListaTipoInterventoFatturazioni: "<%=renderResponse.encodeURL(urlListaTipoInterventoFatturazioni.toString())%>",
        ListaClasseTipoInterventoPagamenti: "<%=renderResponse.encodeURL(urlListaClasseTipoInterventoPagamenti.toString())%>",
        ListaTipoInterventoPagamenti: "<%=renderResponse.encodeURL(urlListaTipoInterventoPagamenti.toString())%>",
        ListaUotStruttura: "<%=renderResponse.encodeURL(urlListaUOT.toString())%>",
        ListaImpegni:"<%=renderResponse.encodeURL(urlListaImpegni.toString())%>",

        CercaAcquisizioni: "<%=renderResponse.encodeURL(urlCercaAcquisizioni.toString())%>",
        DettaglioAcquisizioni: "<%=renderResponse.encodeURL(urlDettaglioAcquisizioni.toString())%>",
        SalvaDettaglioAcquisizioni: "<%=renderResponse.encodeURL(urlModificaAcquisizioni.toString())%>",
        SalvaAcquisizioni : "<%=renderResponse.encodeURL(urlSalvaAcquisizioni.toString())%>",
        
        
        CercaFatturazioni: "<%=renderResponse.encodeURL(urlCercaFatture.toString())%>",
        SalvaStatoFatturazioni: "<%=renderResponse.encodeURL(urlSalvaStatoFatture.toString())%>",
        CercaFatturazioniDaGenerare: "<%=renderResponse.encodeURL(urlCercaFattureDaGenerare.toString())%>",
        InviaFatturazioni: "<%=renderResponse.encodeURL(urlInviaFatture.toString())%>",
        AnteprimaFatturazioni:  "<%=renderResponse.encodeURL(urlAnteprimaFatture.toString())%>",

        CercaVociFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlCercaVociFattura.toString())%>",
        SalvaVociFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlSalvaVociFattura.toString())%>",
        CercaDatiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlCercaDatiFatturazioni.toString())%>",
        SalvaDatiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlSalvaFattura.toString())%>",
        CercaMesiPrecedentiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlCercaMesiPrecedentiFatturazioni.toString())%>",
        SalvaMesiPrecedentiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlSalvaMesiPrecedentiFatturazioni.toString())%>",
        CercaQuoteObbligatiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlCercaQuoteObbligatiFatturazioni.toString())%>",
        SalvaQuoteObbligatiFatturazioniNuovaSelezionata: "<%=renderResponse.encodeURL(urlSalvaQuoteObbligatiFatturazioni.toString())%>",

        DettaglioVociFatturazioni: "<%=renderResponse.encodeURL(urlDettaglioVociFattura.toString())%>",
        SalvaDettaglioVociFatturazioni: "<%=renderResponse.encodeURL(urlSalvaDettaglioVociFattura.toString())%>",
        DettaglioDatiFatturazioni: "<%=renderResponse.encodeURL(urlDettaglioDatiFatturazioni.toString())%>",
        SalvaDettaglioDatiFatturazioni: "<%=renderResponse.encodeURL(urlSalvaDettaglioFattura.toString())%>",
        DettaglioMesiPrecedentiFatturazioni: "<%=renderResponse.encodeURL(urlDettaglioMesiPrecedentiFatturazioni.toString())%>",
        SalvaDettaglioMesiPrecedentiFatturazioni: "<%=renderResponse.encodeURL(urlSalvaDettaglioMesiPrecedentiFatturazioni.toString())%>",
        DettaglioQuoteObbligateFatturazioni: "<%=renderResponse.encodeURL(urlDettaglioQuoteObbligatiFatturazioni.toString())%>",
        SalvaDettaglioQuoteObbligateFatturazioni: "<%=renderResponse.encodeURL(urlSalvaDettaglioQuoteObbligatiFatturazioni.toString())%>",

        CercaDatiFatturazioniNuova: "<%=renderResponse.encodeURL(urlCercaDatiFatturazioniNuova.toString())%>",
        SalvaDatiFatturazioniNuova: "<%=renderResponse.encodeURL(urlSalvaFatturaNuova.toString())%>",
        CercaMesiPrecedentiFatturazioniNuova: "<%=renderResponse.encodeURL(urlCercaMesiPrecedentiFatturazioniNuova.toString())%>",
        SalvaMesiPrecedentiFatturazioniNuova: "<%=renderResponse.encodeURL(urlSalvaMesiPrecedentiFatturazioniNuova.toString())%>",
        CercaQuoteObbligatiFatturazioniNuova: "<%=renderResponse.encodeURL(urlCercaQuoteObbligatiFatturazioniNuova.toString())%>",
        SalvaQuoteObbligatiFatturazioniNuova: "<%=renderResponse.encodeURL(urlSalvaQuoteObbligatiFatturazioniNuova.toString())%>",
        SalvaVociFatturazioniNuova: "<%=renderResponse.encodeURL(urlSalvaVociFatturazioniNuova.toString())%>",
        CercaAnagraficaFatture: "<%=renderResponse.encodeURL(urlCercaAnagraficaFatture.toString())%>",
        ListaTipologiaInterventiFatturazioniNuova: "<%=renderResponse.encodeURL(urlListaTipologiaInterventiFatturazioniNuova.toString())%>",


        ListaModalitaPagamentoFatturazioni: "<%=renderResponse.encodeURL(urlListaModalitaPagamantiFatturazioni.toString())%>",
        ListaValoriIvaFatturazioni: "<%=renderResponse.encodeURL(urlListaValoriIvaFatturazioni.toString())%>",
        
        CercaPagamenti: "<%=renderResponse.encodeURL(urlCercaPagamenti.toString())%>",
        SalvaStatoPagamenti: "<%=renderResponse.encodeURL(urlSalvaStatoPagamenti.toString())%>",
        CercaPagamentiDaGenerare: "<%=renderResponse.encodeURL(urlCercaPagamentiDaGenerare.toString())%>",
        InviaPagamenti: "<%=renderResponse.encodeURL(urlInviaPagamenti.toString())%>",
        anteprimaFilePagamenti: "<%=renderResponse.encodeURL(urlAnteprimaFilePagamenti.toString())%>",
        
        SalvaNuoviPagamenti: "<%=renderResponse.encodeURL(urlSalvaNuoviPagamenti.toString())%>",

        CercaVociPagamentiNuovoSelezionato: "<%=renderResponse.encodeURL(urlCercaVociPagamento.toString())%>",
        SalvaVociPagamentiNuovoSelezionato: "<%=renderResponse.encodeURL(urlSalvaVociPagamento.toString())%>",
        CercaDatiPagamentiNuovoSelezionato: "<%=renderResponse.encodeURL(urlCercaDatiPagamento.toString())%>",
        SalvaDatiPagamentiNuovoSelezionato: "<%=renderResponse.encodeURL(urlSalvaPagamento.toString())%>",

        DettaglioVociPagamenti: "<%=renderResponse.encodeURL(urlDettaglioVociPagamento.toString())%>",
        SalvaDettaglioVociPagamenti: "<%=renderResponse.encodeURL(urlSalvaDettaglioVociPagamento.toString())%>",
        DettaglioDatiPagamenti: "<%=renderResponse.encodeURL(urlDettaglioDatiPagamento.toString())%>",
        SalvaDettaglioDatiPagamenti: "<%=renderResponse.encodeURL(urlSalvaDettaglioPagamento.toString())%>",

        ListaModalitaPagamentoPagamenti: "<%=renderResponse.encodeURL(urlListaModalitaPagamantiPagamenti.toString())%>",

        DatiPagamentiNuovo: "<%=renderResponse.encodeURL(urlCercaDatiPagamentiNuovo.toString())%>",
        SalvaPagamentoNuovo: "<%=renderResponse.encodeURL(urlSalvaPagamentoNuovo.toString())%>",
        SalvaVociPagamentiNuovo: "<%=renderResponse.encodeURL(urlSalvaVociPagamentiNuovo.toString())%>",
        CercaAnagraficaPagamenti: "<%=renderResponse.encodeURL(urlCercaAnagraficaPagamenti.toString())%>",
        ListaTipologiaInterventiPagamentiNuovo: "<%=renderResponse.encodeURL(urlListaTipologiaInterventiPagamentiNuovo.toString())%>",
        ListaDecretiNuovo: "<%=renderResponse.encodeURL(urlListaDecretiNuovo.toString())%>",
        
        motivazioneVariazioneSpesa: "<%=renderResponse.encodeURL(urlMotivazioneVariazioneSpesa.toString())%>"
    };

</script>
<div id="wp_container_principale"></div>