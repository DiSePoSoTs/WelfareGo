<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang3.Validate"%>
<%@page import="it.wego.welfarego.persistence.entities.Utenti"%>
<%@page import="com.liferay.portal.model.UserGroup"%>
<%@page import="it.wego.welfarego.utils.Log"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="it.wego.welfarego.utils.Utils"%>
<%@page import="it.wego.welfarego.users.UserType"%>
<%@page import="it.wego.welfarego.users.ConnectedUserInformations"%>

<%
    //La prendo da liferay
    String username = "test";
    String tipoUtente = "";

    Utils utils = new Utils();
    User liferayUser = utils.getUser(session);
    if (liferayUser != null) {
        Log.APP.debug("Informazioni utente: ");
        Log.APP.debug("Login: " + liferayUser.getLogin());
        Log.APP.debug("Screen name " + liferayUser.getScreenName());
        username = liferayUser.getScreenName();
        List<UserGroup> gruppi = liferayUser.getUserGroups();
        Log.APP.debug("Elenco gruppi");
        if (gruppi.size() > 2) {
            Log.APP.debug("Posso appartenere al più a 2 gruppi: tipo utente e UOT");
        }
        for (UserGroup gruppo : gruppi) {
            Log.APP.debug("Name: " + gruppo.getName());
            if (gruppo.getName().startsWith("UOT")) {
                Log.APP.debug("Escludo il gruppo UOT");
            } else {
                Log.APP.debug("Setto tipo utente a " + gruppo.getName());
                tipoUtente = gruppo.getName();
                break;
            }
        }
    } else {
        Log.APP.warn("Nessuna informazione utente");
    }

    Utenti utente = utils.getOperatore(username);
    Log.APP.debug("Setto usernamee a " + username);
    Validate.notNull(utente, "utente non presente in db welfarego per username '" + username + "'");
    tipoUtente = utente.getIdParamLvlAbil().getIdParam().getCodParam();
    Log.APP.debug("Setto tipo utente a " + tipoUtente);
    ConnectedUserInformations user = UserType.valueOf(tipoUtente).getUtente();
%>
<script type="text/javascript">
    log=function(){
        if(window.console&&console.debug){
            console.debug.apply(console,arguments)
        }
    }
    var wcs_codOperatore = '<%= utente.getCodUte()%>';
    var wcs_codiceFiscaleOperatore = '<%= utente.getUsername()%>';
    salvaUtenteInSessione(wcs_codiceFiscaleOperatore);

    var wcs_registraContattoButtonHidden = '<%= user.isRegistraContattoButtonHidden()%>';
    var wcs_fissaAppuntamentoButtonHidden = '<%= user.isFissaAppuntamentoButtonHidden()%>';
    var wcs_decessoButtonHidden = '<%= user.isDecessoButtonHidden()%>';
    var wcs_stampaCartellaButtonHidden = <%= user.isStampaCartellaButtonHidden()%>;
    var wcs_salvaCartellaButtonHidden = <%= user.isSalvaCartellaButtonHidden()%>;
    var wcs_richiediApprovazioneButtonHidden = <%= user.isRichiediApprovazioneButtonHidden()%>;

    //Anagrafica
    var wcs_anagraficaProvinciaResidenzaRO = <%= user.isAnagraficaProvinciaResidenzaRO()%>;
    var wcs_anagraficaProvinciaDomicilioRO = <%= user.isAnagraficaProvinciaDomicilioRO()%>;
    var wcs_anagraficaDataAperturaCartellaRO = <%= user.isAnagraficaDataAperturaCartellaRO()%>;
    var wcs_anagraficaAssistenteSocialeRO = <%= user.isAnagraficaAssistenteSocialeRO()%>;
    var wcs_anagraficaSegnalatoRO = <%= user.isAnagraficaSegnalatoRO()%>;
    var wcs_anagraficaCodiceAnagraficoRO = <%= user.isAnagraficaCodiceAnagraficoRO()%>;
    var wcs_anagraficaCodiceNucleoFamigliareRO = <%= user.isAnagraficaCodiceNucleoFamigliareRO()%>;
    var wcs_anagraficaNomeRO = <%= user.isAnagraficaNomeRO()%>;
    var wcs_anagraficaCognomeRO = <%= user.isAnagraficaCognomeRO()%>;
    var wcs_anagraficaDataNascitaRO = <%= user.isAnagraficaDataNascitaRO()%>;
    var wcs_anagraficaSessoRO = <%= user.isAnagraficaSessoRO()%>;
    var wcs_anagraficaRelazioneNucleoRO = <%= user.isAnagraficaRelazioneNucleoRO()%>;
    var wcs_anagraficaCognomeConiugeRO= <%= user.isAnagraficaCognomeConiugeRO()%>;
    var wcs_anagraficaStatoNascitaRO = <%= user.isAnagraficaStatoNascitaRO()%>;
    var wcs_anagraficaComuneNascitaRO = <%= user.isAnagraficaComuneNascitaRO()%>;
    var wcs_anagraficanazionalitaRO = <%= user.isAnagraficanazionalitaRO()%>;
    var wcs_anagraficaPosizioneAnagraficaRO = <%= user.isAnagraficaPosizioneAnagraficaRO()%>;
    var wcs_anagraficaCodiceFiscaleRO = <%= user.isAnagraficaCodiceFiscaleRO()%>;
    var wcs_anagraficaDataDecessoRO = <%= user.isAnagraficaDataDecessoRO()%>;
    var wcs_anagraficaStatoDomicilioRO = <%= user.isAnagraficaStatoDomicilioRO()%>;
    var wcs_anagraficaComuneDomicilioRO = <%= user.isAnagraficaComuneDomicilioRO()%>;
    var wcs_anagraficaCapDomicilioRO = <%= user.isAnagraficaCapDomicilioRO()%>;
    var wcs_anagraficaViaDomicilioRO = <%= user.isAnagraficaViaDomicilioRO()%>;
    var wcs_anagraficaCivicoDomicilioRO = <%= user.isAnagraficaCivicoDomicilioRO()%>;
    var wcs_anagraficaTelefonoDomicilioRO = <%= user.isAnagraficaTelefonoDomicilioRO()%>;
    var wcs_anagraficaCellulareDomicilioRO = <%= user.isAnagraficaCellulareDomicilioRO()%>;
    var wcs_anagraficaNoteRO = <%= user.isAnagraficaNoteRO()%>;
    var wcs_anagraficaZonaRO = <%= user.isAnagraficaZonaRO()%>;
    var wcs_anagraficaSottozonaRO = <%= user.isAnagraficaSottozonaRO()%>;
    var wcs_anagraficaASSRO = <%= user.isAnagraficaASSRO()%>;
    var wcs_anagraficaEnteGestoreRO = <%= user.isAnagraficaEnteGestoreRO()%>;
    var wcs_anagraficaDistrettoASSRO = <%= user.isAnagraficaDistrettoASSRO()%>;
    var wcs_anagraficaMedicoBaseRO = <%= user.isAnagraficaMedicoBaseRO()%>;
    var wcs_anagraficaUOTRO = <%= user.isAnagraficaUOTRO()%>;
    var wcs_anagraficaStatoResidenzaRO = <%= user.isAnagraficaStatoResidenzaRO()%>;
    var wcs_anagraficaComuneResidenzaRO = <%= user.isAnagraficaComuneResidenzaRO()%>;
    var wcs_anagraficaCapResidenzaRO = <%= user.isAnagraficaCapResidenzaRO()%>;
    var wcs_anagraficaViaResidenzaRO = <%= user.isAnagraficaViaResidenzaRO()%>;
    var wcs_anagraficaCivicoResidenzaRO = <%= user.isAnagraficaCivicoResidenzaRO()%>;
    var wcs_anagraficaEmailDomicilioRO = <%= user.isAnagraficaEmailDomicilioRO()%>;

    //Condizione
    var wcs_condizioneStatoCivileRO = <%= user.isCondizioneStatoCivileRO()%>;
    var wcs_condizioneScolaritaRO = <%= user.isCondizioneScolaritaRO()%>;
    var wcs_condizioneScolaritaHidden = <%= user.isCondizioneScolaritaHidden()%>;
    var wcs_condizioneProfessionaleRO = <%= user.isCondizioneProfessionaleRO()%>;
    var wcs_condizioneProfessionaleHidden = <%= user.isCondizioneProfessionaleHidden()%>;
    var wcs_condizioneFamigliareRO = <%= user.isCondizioneFamigliareRO()%>;
    var wcs_condizioneFamigliareHidden = <%= user.isCondizioneFamigliareHidden()%>;
    var wcs_condizioneAbitazioneRO = <%= user.isCondizioneAbitazioneRO()%>;
    var wcs_condizioneAbitazioneHidden = <%= user.isCondizioneAbitazioneHidden()%>;
    var wcs_condizioneStatoFisicoRO = <%= user.isCondizioneStatoFisicoRO()%>;
    var wcs_condizioneStatoFisicoHidden = <%= user.isCondizioneStatoFisicoHidden()%>;
    var wcs_condizioneFormaRedditoRO = <%= user.isCondizioneFormaRedditoRO()%>;
    var wcs_condizioneFormaRedditoHidden = <%= user.isCondizioneFormaRedditoHidden()%>;
    var wcs_condizioneRedditoMensileRO = <%= user.isCondizioneRedditoMensileRO()%>;
    var wcs_condizioneRedditoMensileHidden = <%= user.isCondizioneRedditoMensileHidden()%>;
    var wcs_condizioneInvaliditaCivileRO = <%= user.isCondizioneInvaliditaCivileRO()%>;
    var wcs_condizioneInvaliditaCivileHidden = <%= user.isCondizioneInvaliditaCivileHidden()%>;
    var wcs_condizioneAccompagnamentoRO = <%= user.isCondizioneAccompagnamentoRO()%>;
    var wcs_condizioneAccompagnamentoHidden = <%= user.isCondizioneAccompagnamentoHidden()%>;
    var wcs_condizioneISEERO = <%= user.isCondizioneISEERO()%>;
    var wcs_condizioneBancaRO = <%= user.isCondizioneBancaRO()%>;
    var wcs_condizioneCodNazioneRO = <%= user.isCondizioneCodNazioneRO()%>;
    var wcs_condizioneContrRO = <%= user.isCondizioneContrRO()%>;
    var wcs_condizioneCINRO = <%= user.isCondizioneCINRO()%>;
    var wcs_condizioneCABRO = <%= user.isCondizioneCABRO()%>;
    var wcs_condizioneABIRO = <%= user.isCondizioneABIRO()%>;
    var wcs_condizioneCCorrenteRO = <%= user.isCondizioneCCorrenteRO()%>;
    var wcs_condizioneIBANRO = <%= user.isCondizioneIBANRO()%>;
    var wcs_condizioneIBANButtonHidden = <%= user.isCondizioneIBANButtonHidden()%>;

    //Referenti
    var wcs_referentiQualificaRO = <%= user.isQualificaRO()%>;
    var wcs_referentiAggiungiReferenteButtonHidden = <%= user.isAggiungiReferenteButtonHidden()%>;
    var wcs_referentiRimuoviReferenteButtonHidden = <%= user.isRimuoviReferenteButtonHidden()%>;

    //PAI
    var wcs_paiDataAperturaRO = <%= user.isPaiDataAperturaRO()%>;
    var wcs_paiDataChiusuraRO = <%= user.isPaiDataChiusuraRO()%>;
    var wcs_paiPaiMotivazioneChiusuraRO = <%= user.isPaiMotivazioneChiusuraRO()%>;
    var wcs_paiMotivazioneChiusuraHidden = <%= user.isPaiMotivazioneChiusuraHidden()%>;
    var wcs_paiPaiProtocolloRO = <%= user.isPaiProtocolloRO()%>;
    var wcs_paiDataProtocolloRO = <%= user.isPaiDataProtocolloRO()%>;
    var wcs_paiDisabilitaFieldsetHidden = <%= user.isPaiDisabilitaFieldsetHidden()%>;
    var wcs_paiProfiloFieldsetHidden = <%= utente.getProfilo()=='N'?true: false%>;
    var wcs_paiDisabilitaRO = <%=  utente.getProblematiche()=='N'?true:false%>;
    var wcs_paiNumeroNucleoFamigliareRO = <%= user.isPaiNumeroNucleoFamigliareRO()%>;
    var wcs_paiNumeroFigliRO = <%= user.isPaiNumeroFigliRO()%>;
    var wcs_paiNumeroFigliConviventiRO = <%= user.isPaiNumeroFigliConviventiRO()%>;
    var wcs_paiNuovoButtonHidden = <%= user.isPaiNuovoButtonHidden()%>;
    var wcs_paiChiudiButtonHidden = <%= user.isPaiChiudiButtonHidden()%>;
    var wcs_paiCopiaButtonHidden = <%= user.isPaiCopiaButtonHidden()%>;

    //Intervento
    var wcs_interventoClasseRO = <%= user.isInterventoClasseRO()%>;
    var wcs_interventoRO = <%= user.isInterventoRO()%>;
    var wcs_interventoStatoRO = <%= user.isInterventoStatoRO()%>;
    var wcs_interventoDataAperturaRO = <%= user.isInterventoDataAperturaRO()%>;
    var wcs_interventoDurataMesiRO = <%= user.isInterventoDurataMesiRO()%>;
    var wcs_interventoDataEsecutivitaRO = <%= user.isInterventoDataEsecutivitaRO()%>;
    var wcs_interventoDataChiusuraRO = <%= user.isInterventoDataChiusuraRO()%>;
    var wcs_interventoNoteChiusuraRO = <%= user.isInterventoNoteChiusuraRO()%>;
    var wcs_interventoIndicatoriEsitoRO = <%= user.isInterventoIndicatoriEsitoRO()%>;
    var wcs_interventoDatiSpecificiRO = <%= user.isInterventoDatiSpecificiRO()%>;
    var wcs_interventoDelegatoRiscossioneRO = <%= user.isInterventoDelegatoRiscossioneRO()%>;
    var wcs_interventoDataInizioRO = <%= user.isInterventoDataInizioRO()%>;
    var wcs_interventoMotivazioneHidden =  <%= utente.getMotivazione()=='N'?true:false%>;
    var wcs_isAutorizzazioneDisabled = <%= user.isAutorizzazzioneDisabled() %>
    var wcs_interventoCivilmenteObbligatoCodAna;
    
    
    // diario
    
    var wcs_diarioHidden =  <%= utente.getMotivazione()=='N'?true:user.isDiarioHidden()%>;
    var wcs_isModified = false;
    var wcs_isModifiedNoButtonClicked = false;
</script>