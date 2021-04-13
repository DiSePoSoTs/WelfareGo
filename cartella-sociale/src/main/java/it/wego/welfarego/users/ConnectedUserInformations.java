/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.users;

/**
 *
 * @author giuseppe
 */
public interface ConnectedUserInformations {

    public String getTipoUtente();

    //Pulsanti
    public boolean isRegistraContattoButtonHidden();
    public boolean isFissaAppuntamentoButtonHidden();
    public boolean isDecessoButtonHidden();
    public boolean isStampaCartellaButtonHidden();
    public boolean isSalvaCartellaButtonHidden();
    public boolean isRichiediApprovazioneButtonHidden();

    //Controllo sui campi
    //Anagrafica
    public boolean isAnagraficaDataAperturaCartellaRO();
    public boolean isAnagraficaAssistenteSocialeRO();
    public boolean isAnagraficaSegnalatoRO();
    public boolean isAnagraficaUOTRO();
    public boolean isAnagraficaCodiceAnagraficoRO();
    public boolean isAnagraficaCodiceNucleoFamigliareRO();
    public boolean isAnagraficaNomeRO();
    public boolean isAnagraficaCognomeRO();
    public boolean isAnagraficaDataNascitaRO();
    public boolean isAnagraficaSessoRO();
    public boolean isAnagraficaRelazioneNucleoRO();
    public boolean isAnagraficaCognomeConiugeRO();
    public boolean isAnagraficaStatoNascitaRO();
    public boolean isAnagraficaComuneNascitaRO();
    public boolean isAnagraficanazionalitaRO();
    public boolean isAnagraficaPosizioneAnagraficaRO();
    public boolean isAnagraficaCodiceFiscaleRO();
    public boolean isAnagraficaDataDecessoRO();
    public boolean isAnagraficaStatoResidenzaRO();
    public boolean isAnagraficaProvinciaResidenzaRO();
    public boolean isAnagraficaComuneResidenzaRO();
    public boolean isAnagraficaCapResidenzaRO();
    public boolean isAnagraficaViaResidenzaRO();
    public boolean isAnagraficaCivicoResidenzaRO();
    public boolean isAnagraficaStatoDomicilioRO();
    public boolean isAnagraficaProvinciaDomicilioRO();
    public boolean isAnagraficaComuneDomicilioRO();
    public boolean isAnagraficaCapDomicilioRO();
    public boolean isAnagraficaViaDomicilioRO();
    public boolean isAnagraficaCivicoDomicilioRO();
    public boolean isAnagraficaTelefonoDomicilioRO();
    public boolean isAnagraficaCellulareDomicilioRO();
    public boolean isAnagraficaEmailDomicilioRO();
    public boolean isAnagraficaNoteRO();
    public boolean isAnagraficaZonaRO();
    public boolean isAnagraficaSottozonaRO();
    public boolean isAnagraficaASSRO();
    public boolean isAnagraficaEnteGestoreRO();
    public boolean isAnagraficaDistrettoASSRO();
    public boolean isAnagraficaMedicoBaseRO();

    //Condizione
    public boolean isCondizioneStatoCivileRO();
    public boolean isCondizioneScolaritaRO();
    public boolean isCondizioneScolaritaHidden();
    public boolean isCondizioneProfessionaleRO();
    public boolean isCondizioneProfessionaleHidden();
    public boolean isCondizioneFamigliareRO();
    public boolean isCondizioneFamigliareHidden();
    public boolean isCondizioneAbitazioneRO();
    public boolean isCondizioneAbitazioneHidden();
    public boolean isCondizioneStatoFisicoRO();
    public boolean isCondizioneStatoFisicoHidden();
    public boolean isCondizioneFormaRedditoRO();
    public boolean isCondizioneFormaRedditoHidden();
    public boolean isCondizioneRedditoMensileRO();
    public boolean isCondizioneRedditoMensileHidden();
    public boolean isCondizioneInvaliditaCivileRO();
    public boolean isCondizioneInvaliditaCivileHidden();
    public boolean isCondizioneAccompagnamentoRO();
    public boolean isCondizioneAccompagnamentoHidden();
    public boolean isCondizioneISEERO();
    public boolean isCondizioneBancaRO();
    public boolean isCondizioneCodNazioneRO();
    public boolean isCondizioneContrRO();
    public boolean isCondizioneCINRO();
    public boolean isCondizioneCABRO();
    public boolean isCondizioneABIRO();
    public boolean isCondizioneCCorrenteRO();
    public boolean isCondizioneIBANRO();
    public boolean isCondizioneIBANButtonHidden();

    //Referenti
    public boolean isQualificaRO();
    public boolean isAggiungiReferenteButtonHidden();
    public boolean isRimuoviReferenteButtonHidden();

    //PAI
    public boolean isPaiDataAperturaRO();
    public boolean isPaiDataChiusuraRO();
    public boolean isPaiMotivazioneChiusuraRO();
    public boolean isPaiMotivazioneChiusuraHidden();
    public boolean isPaiProtocolloRO();
    public boolean isPaiDataProtocolloRO();
    public boolean isPaiDisabilitaFieldsetHidden();
    public boolean isPaiProfiloFieldsetHidden();
    public boolean isPaiDisabilitaRO();
    public boolean isPaiNumeroNucleoFamigliareRO();
    public boolean isPaiNumeroFigliRO();
    public boolean isPaiNumeroFigliConviventiRO();
    public boolean isPaiNuovoButtonHidden();
    public boolean isPaiChiudiButtonHidden();
    public boolean isPaiCopiaButtonHidden();

    //Intervento
    public boolean isInterventoClasseRO();
    public boolean isInterventoRO();
    public boolean isInterventoStatoRO();
    public boolean isInterventoDataAperturaRO();
    public boolean isInterventoDurataMesiRO();
    public boolean isInterventoDataEsecutivitaRO();
    public boolean isInterventoDataChiusuraRO();
    public boolean isInterventoNoteChiusuraRO();
    public boolean isInterventoIndicatoriEsitoRO();
    public boolean isInterventoDatiSpecificiRO();
    public boolean isInterventoDelegatoRiscossioneRO();
    public boolean isInterventoDataInizioRO();
    public boolean isMotivazioneHidden();
    public boolean isAutorizzazzioneDisabled();
    
    //diario
    public boolean isDiarioHidden();
}
