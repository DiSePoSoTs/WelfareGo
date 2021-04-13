package it.wego.welfarego.users;

import it.wego.welfarego.persistence.entities.Utenti;

/**
 *
 * @author giuseppe
 */
public class OperatoreAccessoUOT implements ConnectedUserInformations {

    
    public String getTipoUtente() {
        return Utenti.OPERATORE_ACCESSO_UOT.toString();
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
        return true;
    }

    
    public boolean isAnagraficaDataAperturaCartellaRO() {
        return false;
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
        return true;
    }

    
    public boolean isInterventoRO() {
        return true;
    }

    
    public boolean isInterventoStatoRO() {
        return true;
    }

    
    public boolean isInterventoDataAperturaRO() {
        return true;
    }

    
    public boolean isInterventoDurataMesiRO() {
        return true;
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
        return true;
    }

    
    public boolean isInterventoDelegatoRiscossioneRO() {
        return true;
    }

	public boolean isInterventoDataInizioRO() {
		return true;
	}

	public boolean isMotivazioneHidden() {
		return false;
	}

	public boolean isDiarioHidden() {
		return true;
	}

	public boolean isAutorizzazzioneDisabled() {
		return true;
	}
}
