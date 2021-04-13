package it.wego.welfarego.users;

import it.wego.welfarego.persistence.entities.Utenti;

/**
 *
 * @author giuseppe
 */
public class Amministratore implements ConnectedUserInformations {

    public String getTipoUtente() {
        return Utenti.ADMIN.toString();
    }

    public boolean isAnagraficaProvinciaResidenzaRO() {
        return false;
    }

    public boolean isAnagraficaProvinciaDomicilioRO() {
        return false;
    }

    public boolean isRegistraContattoButtonHidden() {
        return false;
    }


    public boolean isFissaAppuntamentoButtonHidden() {
        return false;
    }

    public boolean isDecessoButtonHidden() {
        return false;
    }

    public boolean isStampaCartellaButtonHidden() {
        return false;
    }

    public boolean isSalvaCartellaButtonHidden() {
        return false;
    }

    public boolean isRichiediApprovazioneButtonHidden() {
        return false;
    }

    public boolean isAnagraficaDataAperturaCartellaRO() {
        return true;
    }

    public boolean isAnagraficaAssistenteSocialeRO() {
        return false;
    }

    public boolean isAnagraficaSegnalatoRO() {
        return false;
    }

    public boolean isAnagraficaCodiceAnagraficoRO() {
        return false;
    }

    public boolean isAnagraficaCodiceNucleoFamigliareRO() {
        return false;
    }

    public boolean isAnagraficaNomeRO() {
        return false;
    }

    public boolean isAnagraficaCognomeRO() {
        return false;
    }

    public boolean isAnagraficaDataNascitaRO() {
        return false;
    }

    public boolean isAnagraficaSessoRO() {
        return false;
    }

    public boolean isAnagraficaRelazioneNucleoRO() {
        return false;
    }

    public boolean isAnagraficaCognomeConiugeRO() {
        return false;
    }

    public boolean isAnagraficaStatoNascitaRO() {
        return false;
    }

    public boolean isAnagraficaComuneNascitaRO() {
        return false;
    }

    public boolean isAnagraficanazionalitaRO() {
        return false;
    }

    public boolean isAnagraficaPosizioneAnagraficaRO() {
        return false;
    }

    public boolean isAnagraficaCodiceFiscaleRO() {
        return false;
    }

    public boolean isAnagraficaDataDecessoRO() {
        return false;
    }


    public boolean isAnagraficaStatoDomicilioRO() {
        return false;
    }


    public boolean isAnagraficaComuneDomicilioRO() {
        return false;
    }

    public boolean isAnagraficaCapDomicilioRO() {
        return false;
    }

    
    public boolean isAnagraficaViaDomicilioRO() {
        return false;
    }

    
    public boolean isAnagraficaCivicoDomicilioRO() {
        return false;
    }

    
    public boolean isAnagraficaTelefonoDomicilioRO() {
        return false;
    }

    
    public boolean isAnagraficaCellulareDomicilioRO() {
        return false;
    }

    
    public boolean isAnagraficaNoteRO() {
        return false;
    }

    
    public boolean isAnagraficaZonaRO() {
        return false;
    }

    
    public boolean isAnagraficaSottozonaRO() {
        return false;
    }

    
    public boolean isAnagraficaASSRO() {
        return false;
    }

    
    public boolean isAnagraficaEnteGestoreRO() {
        return false;
    }

    
    public boolean isAnagraficaDistrettoASSRO() {
        return false;
    }

    
    public boolean isAnagraficaMedicoBaseRO() {
        return false;
    }

    
    public boolean isAnagraficaUOTRO() {
        return false;
    }

    
    public boolean isAnagraficaStatoResidenzaRO() {
        return false;
    }

    
    public boolean isAnagraficaComuneResidenzaRO() {
        return false;
    }

    
    public boolean isAnagraficaCapResidenzaRO() {
        return false;
    }

    
    public boolean isAnagraficaViaResidenzaRO() {
        return false;
    }

    
    public boolean isAnagraficaCivicoResidenzaRO() {
        return false;
    }

    
    public boolean isAnagraficaEmailDomicilioRO() {
        return false;
    }

    
    public boolean isCondizioneStatoCivileRO() {
        return false;
    }

    
    public boolean isCondizioneScolaritaRO() {
        return false;
    }

    
    public boolean isCondizioneScolaritaHidden() {
        return false;
    }

    
    public boolean isCondizioneProfessionaleRO() {
        return false;
    }

    
    public boolean isCondizioneProfessionaleHidden() {
        return false;
    }

    
    public boolean isCondizioneFamigliareRO() {
        return false;
    }

    
    public boolean isCondizioneFamigliareHidden() {
        return false;
    }

    
    public boolean isCondizioneAbitazioneRO() {
        return false;
    }

    
    public boolean isCondizioneAbitazioneHidden() {
        return false;
    }

    
    public boolean isCondizioneStatoFisicoRO() {
        return false;
    }

    
    public boolean isCondizioneStatoFisicoHidden() {
        return false;
    }

    
    public boolean isCondizioneFormaRedditoRO() {
        return false;
    }

    
    public boolean isCondizioneFormaRedditoHidden() {
        return false;
    }

    
    public boolean isCondizioneRedditoMensileRO() {
        return false;
    }

    
    public boolean isCondizioneRedditoMensileHidden() {
        return false;
    }

    
    public boolean isCondizioneInvaliditaCivileRO() {
        return false;
    }

    
    public boolean isCondizioneInvaliditaCivileHidden() {
        return false;
    }

    
    public boolean isCondizioneAccompagnamentoRO() {
        return false;
    }

    
    public boolean isCondizioneAccompagnamentoHidden() {
        return false;
    }

    
    public boolean isCondizioneISEERO() {
        return false;
    }

    
    public boolean isCondizioneBancaRO() {
        return false;
    }

    
    public boolean isCondizioneCodNazioneRO() {
        return false;
    }

    
    public boolean isCondizioneContrRO() {
        return false;
    }

    
    public boolean isCondizioneCINRO() {
        return false;
    }

    
    public boolean isCondizioneCABRO() {
        return false;
    }

    
    public boolean isCondizioneABIRO() {
        return false;
    }

    
    public boolean isCondizioneCCorrenteRO() {
        return false;
    }

    
    public boolean isCondizioneIBANRO() {
        return false;
    }

    
    public boolean isCondizioneIBANButtonHidden() {
        return false;
    }

    
    public boolean isQualificaRO() {
        return false;
    }

    
    public boolean isAggiungiReferenteButtonHidden() {
        return false;
    }

    
    public boolean isRimuoviReferenteButtonHidden() {
        return false;
    }

    
    public boolean isPaiDataAperturaRO() {
        return true;
    }

    
    public boolean isPaiDataChiusuraRO() {
        return true;
    }

    
    public boolean isPaiMotivazioneChiusuraRO() {
        return false;
    }

    
    public boolean isPaiMotivazioneChiusuraHidden() {
        return false;
    }

    
    public boolean isPaiProtocolloRO() {
        return false;
    }

    
    public boolean isPaiDataProtocolloRO() {
        return false;
    }

    
    public boolean isPaiDisabilitaFieldsetHidden() {
        return false;
    }

    
    public boolean isPaiProfiloFieldsetHidden() {
        return false;
    }

    
    public boolean isPaiDisabilitaRO() {
        return false;
    }

    
    public boolean isPaiNumeroNucleoFamigliareRO() {
        return false;
    }

    
    public boolean isPaiNumeroFigliRO() {
        return false;
    }

    
    public boolean isPaiNumeroFigliConviventiRO() {
        return false;
    }

    
    public boolean isPaiNuovoButtonHidden() {
        return false;
    }

    
    public boolean isPaiChiudiButtonHidden() {
        return false;
    }

    
    public boolean isPaiCopiaButtonHidden() {
        return false;
    }

    
    public boolean isInterventoClasseRO() {
        return false;
    }

    
    public boolean isInterventoRO() {
        return false;
    }

    
    public boolean isInterventoStatoRO() {
        return false;
    }

    
    public boolean isInterventoDataAperturaRO() {
        return true;
    }

    
    public boolean isInterventoDurataMesiRO() {
        return false;
    }

    
    public boolean isInterventoDataEsecutivitaRO() {
        return true;
    }

    
    public boolean isInterventoDataChiusuraRO() {
        return false;
    }

    
    public boolean isInterventoNoteChiusuraRO() {
        return false;
    }

    
    public boolean isInterventoIndicatoriEsitoRO() {
        return false;
    }

    
    public boolean isInterventoDatiSpecificiRO() {
        return false;
    }

    
    public boolean isInterventoDelegatoRiscossioneRO() {
        return false;
    }

	public boolean isInterventoDataInizioRO() {

		return false;
	}

	public boolean isMotivazioneHidden() {

		return false;
	}

	public boolean isDiarioHidden() {

		return false;
	}

	public boolean isAutorizzazzioneDisabled() {

		return false;
	}
}
