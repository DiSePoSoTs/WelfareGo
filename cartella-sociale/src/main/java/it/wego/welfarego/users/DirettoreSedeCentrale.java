package it.wego.welfarego.users;

import it.wego.welfarego.persistence.entities.Utenti;

/**
 *
 * @author giuseppe
 */
public class DirettoreSedeCentrale implements ConnectedUserInformations {

    
    public String getTipoUtente() {
        return Utenti.DIRIGENTE_SEDE_CENTRALE.toString();
    }

        
    public boolean isAnagraficaProvinciaResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaProvinciaDomicilioRO() {
        return true;
    }

    
    public boolean isRegistraContattoButtonHidden() {
        return true;
    }

    
    public boolean isFissaAppuntamentoButtonHidden() {
        return true;
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
        return true;
    }

    
    public boolean isAnagraficaDataAperturaCartellaRO() {
        return true;
    }

    
    public boolean isAnagraficaAssistenteSocialeRO() {
        return true;
    }

    
    public boolean isAnagraficaSegnalatoRO() {
        return true;
    }

    
    public boolean isAnagraficaCodiceAnagraficoRO() {
        return true;
    }

    
    public boolean isAnagraficaCodiceNucleoFamigliareRO() {
        return true;
    }

    
    public boolean isAnagraficaNomeRO() {
        return true;
    }

    
    public boolean isAnagraficaCognomeRO() {
        return true;
    }

    
    public boolean isAnagraficaDataNascitaRO() {
        return true;
    }

    
    public boolean isAnagraficaSessoRO() {
        return true;
    }

    
    public boolean isAnagraficaRelazioneNucleoRO() {
        return true;
    }

    
    public boolean isAnagraficaCognomeConiugeRO() {
        return true;
    }

    
    public boolean isAnagraficaStatoNascitaRO() {
        return true;
    }

    
    public boolean isAnagraficaComuneNascitaRO() {
        return true;
    }

    
    public boolean isAnagraficanazionalitaRO() {
        return true;
    }

    
    public boolean isAnagraficaPosizioneAnagraficaRO() {
        return true;
    }

    
    public boolean isAnagraficaCodiceFiscaleRO() {
        return true;
    }

    
    public boolean isAnagraficaDataDecessoRO() {
        return true;
    }

    
    public boolean isAnagraficaStatoDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaComuneDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaCapDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaViaDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaCivicoDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaTelefonoDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaCellulareDomicilioRO() {
        return true;
    }

    
    public boolean isAnagraficaNoteRO() {
        return true;
    }

    
    public boolean isAnagraficaZonaRO() {
        return true;
    }

    
    public boolean isAnagraficaSottozonaRO() {
        return true;
    }

    
    public boolean isAnagraficaASSRO() {
        return true;
    }

    
    public boolean isAnagraficaEnteGestoreRO() {
        return true;
    }

    
    public boolean isAnagraficaDistrettoASSRO() {
        return true;
    }

    
    public boolean isAnagraficaMedicoBaseRO() {
        return true;
    }

    
    public boolean isAnagraficaUOTRO() {
        return true;
    }

    
    public boolean isAnagraficaStatoResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaComuneResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaCapResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaViaResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaCivicoResidenzaRO() {
        return true;
    }

    
    public boolean isAnagraficaEmailDomicilioRO() {
        return true;
    }

    
    public boolean isCondizioneStatoCivileRO() {
        return true;
    }

    
    public boolean isCondizioneScolaritaRO() {
        return true;
    }

    
    public boolean isCondizioneScolaritaHidden() {
        return true;
    }

    
    public boolean isCondizioneProfessionaleRO() {
        return true;
    }

    
    public boolean isCondizioneProfessionaleHidden() {
        return true;
    }

    
    public boolean isCondizioneFamigliareRO() {
        return true;
    }

    
    public boolean isCondizioneFamigliareHidden() {
        return true;
    }

    
    public boolean isCondizioneAbitazioneRO() {
        return true;
    }

    
    public boolean isCondizioneAbitazioneHidden() {
        return true;
    }

    
    public boolean isCondizioneStatoFisicoRO() {
        return true;
    }

    
    public boolean isCondizioneStatoFisicoHidden() {
        return true;
    }

    
    public boolean isCondizioneFormaRedditoRO() {
        return true;
    }

    
    public boolean isCondizioneFormaRedditoHidden() {
        return true;
    }

    
    public boolean isCondizioneRedditoMensileRO() {
        return true;
    }

    
    public boolean isCondizioneRedditoMensileHidden() {
        return true;
    }

    
    public boolean isCondizioneInvaliditaCivileRO() {
        return true;
    }

    
    public boolean isCondizioneInvaliditaCivileHidden() {
        return true;
    }

    
    public boolean isCondizioneAccompagnamentoRO() {
        return true;
    }

    
    public boolean isCondizioneAccompagnamentoHidden() {
        return true;
    }

    
    public boolean isCondizioneISEERO() {
        return true;
    }

    
    public boolean isCondizioneBancaRO() {
        return true;
    }

    
    public boolean isCondizioneCodNazioneRO() {
        return true;
    }

    
    public boolean isCondizioneContrRO() {
        return true;
    }

    
    public boolean isCondizioneCINRO() {
        return true;
    }

    
    public boolean isCondizioneCABRO() {
        return true;
    }

    
    public boolean isCondizioneABIRO() {
        return true;
    }

    
    public boolean isCondizioneCCorrenteRO() {
        return true;
    }

    
    public boolean isCondizioneIBANRO() {
        return true;
    }

    
    public boolean isCondizioneIBANButtonHidden() {
        return true;
    }

    
    public boolean isQualificaRO() {
        return true;
    }

    
    public boolean isAggiungiReferenteButtonHidden() {
        return true;
    }

    
    public boolean isRimuoviReferenteButtonHidden() {
        return true;
    }

    
    public boolean isPaiDataAperturaRO() {
        return true;
    }

    
    public boolean isPaiDataChiusuraRO() {
        return true;
    }

    
    public boolean isPaiMotivazioneChiusuraRO() {
        return true;
    }

    
    public boolean isPaiMotivazioneChiusuraHidden() {
        return true;
    }

    
    public boolean isPaiProtocolloRO() {
        return false;
    }

    
    public boolean isPaiDataProtocolloRO() {
        return false;
    }

    
    public boolean isPaiDisabilitaFieldsetHidden() {
        return true;
    }

    
    public boolean isPaiProfiloFieldsetHidden() {
        return true;
    }

    
    public boolean isPaiDisabilitaRO() {
        return true;
    }

    
    public boolean isPaiNumeroNucleoFamigliareRO() {
        return true;
    }

    
    public boolean isPaiNumeroFigliRO() {
        return true;
    }

    
    public boolean isPaiNumeroFigliConviventiRO() {
        return true;
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
        return true;
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
        return true;
    }

    
    public boolean isInterventoNoteChiusuraRO() {
        return true;
    }

    
    public boolean isInterventoIndicatoriEsitoRO() {
        return true;
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
		return true;
	}

	public boolean isAutorizzazzioneDisabled() {
		return false;
	}
}
