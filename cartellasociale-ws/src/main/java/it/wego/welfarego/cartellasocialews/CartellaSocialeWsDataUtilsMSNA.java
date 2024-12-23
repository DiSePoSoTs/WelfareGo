package it.wego.welfarego.cartellasocialews;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import it.wego.welfarego.cartellasocialews.beans.AnagraficaBaseType;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType;
import it.wego.welfarego.cartellasocialews.beans.IndirizzoType;
import it.wego.welfarego.cartellasocialews.beans.InserimentoCartellaSociale;
import it.wego.welfarego.cartellasocialews.beans.IseeType;
import it.wego.welfarego.cartellasocialews.beans.MicroProblematicaType;
import it.wego.welfarego.cartellasocialews.beans.NascitaType;
import it.wego.welfarego.cartellasocialews.beans.ProblematicheType;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType;
import it.wego.welfarego.cartellasocialews.beans.ProgettoType;
import it.wego.welfarego.cartellasocialews.beans.RilevanzaObiettiviType;
import it.wego.welfarego.cartellasocialews.beans.SessoType;
import it.wego.welfarego.cartellasocialews.beans.SiNoType;
import it.wego.welfarego.cartellasocialews.beans.StatoType;
import it.wego.welfarego.cartellasocialews.beans.ToponimoType;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType.DatiComuni;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType.DatiComuni.Residenza;
import it.wego.welfarego.cartellasocialews.beans.ProblematicheType.Macroproblematica;
import it.wego.welfarego.cartellasocialews.beans.ComuneType;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Abilitazione;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiFamiliari;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiPersonali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiProfessionali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Domicilio;
import it.wego.welfarego.persistence.entities.PaiMacroProblematica;

public class CartellaSocialeWsDataUtilsMSNA extends CartellaSocialeWSUtilsBase {

	private String[] rigaMSNA;
	
	public void setRigaMSNA(String[] rigaMSNA) {
		this.rigaMSNA = rigaMSNA;
	}

	public CartellaSocialeWsDataUtilsMSNA(CartellaSocialeWsClient cartellaSocialeWsClient) {
		super(cartellaSocialeWsClient);
	}

	private static final int CSV_COLONNA_NOME = 0;
	private static final int CSV_COLONNA_COGNOME = 1;
	private static final int CSV_COLONNA_SESSO = 2;
	private static final int CSV_COLONNA_CITTADINANZA = 3;
	private static final int CSV_COLONNA_CITTADINANZA_COD_INSIEL = 4;
	private static final int CSV_COLONNA_DATA_NASCITA = 5;
	private static final int CSV_COLONNA_STATO_NASCITA = 6;
	private static final int CSV_COLONNA_STATO_NASCITA_COD_CAT = 7;
	private static final int CSV_COLONNA_STATO_NASCITA_COD_ISTAT = 8;
	private static final int CSV_COLONNA_DATA_PRESA_IN_CARICO = 12;
	
	
	private static final String RESIDENZA_DEFAULT_MSNA = "36";
	private static final String STATO_CIVILE_MSNA_DEFAULT = "0";
	private static final String TIPO_NUCLEO_FAMILIARE_MSNA_DEFAULT = "1";
	private static final int NUMEROSITA_NUCLEO_FAM_MSNA_DEFAULT = 1;
	private static final String IP_STATO_INV_MSNA_DEFAULT = "37";
	private static final String TITOLO_DI_STUDIO_MSNA_DEFAULT = "1";
	private static final String TIPOLOGIA_ISEE_MSNA_DEFAULT = "1";
	private static final String IP_PROVVEDIMENTO_GIUDIZIARIO_MSNA_DEFAULT = "17";
	private static final String DEFAULT_INDIRIZZO_MSNA = "VIA MAZZINI";
	private static final String DEFAULT_CIVICO_MSNA = "25";
	private static final String TIPOLOGIA_MACROPROBLEMATICA_MSNA_DEFAULT = "1";
	private static final String MICRO_PROBLEMATICA_MSNA_DEFAULT = "3";
	private static final String RILEVANZA_MSNA_DEFAULT = "5";
	private static final String FRONTEGGIAMENTO_MSNA_DEFAULT = "3";
	private static final String OBIETTIVO_PREVALENTE_MSNA_DEFAULT = "3";
	private static final String IDENTIFICATIVO_STRUTTURA_MSNA = "7";
	private static final String DETTAGLIO_PROBLEMATICA_MSNA_DEFAULT = "INSERIMENTO D'UFFICIO";
	

	public InserimentoCartellaSociale createInserimentoCartellaSocialeRequest() {
		InserimentoCartellaSociale inserimentoCartellaSociale = new InserimentoCartellaSociale();
		inserimentoCartellaSociale.setCodice(requireCodiceOperatore());
		inserimentoCartellaSociale.setAnagrafica(createAnagrafica());
		inserimentoCartellaSociale.setProfilo(createProfilo());
		inserimentoCartellaSociale.setProgetto(createProgetto());
		inserimentoCartellaSociale.setVersione(VERSIONE);
		return inserimentoCartellaSociale;
	}

	private AnagraficaType createAnagrafica() {
		Preconditions.checkNotNull(rigaMSNA, "rigaMSNA must be not null");
		AnagraficaType anagraficaType = new AnagraficaType();
		anagraficaType.setOperatoreRiferimento(requireCodiceOperatore());
		anagraficaType.setComuneCartella(requireComuneCartella());
		anagraficaType.setDataModifica(getXmlDate(new Date()));
		anagraficaType.setDatiComuni(createDatiComuni());
		anagraficaType.setIdentificativoSottostrutturaSsc(IDENTIFICATIVO_STRUTTURA_MSNA);
		return anagraficaType;
	}

	private DatiComuni createDatiComuni() {
		Preconditions.checkNotNull(rigaMSNA);
		AnagraficaType.DatiComuni datiComuni = new AnagraficaType.DatiComuni();
		datiComuni.setAnagraficaBase(createAnagraficaBase());
		datiComuni.setResidenza(getResidenza());
		return datiComuni;
	}

	private AnagraficaBaseType createAnagraficaBase() {
		Preconditions.checkNotNull(rigaMSNA);
		AnagraficaBaseType anagraficaBase = new AnagraficaBaseType();
		anagraficaBase.setNome(rigaMSNA[CSV_COLONNA_NOME]);
		anagraficaBase.setCognome(rigaMSNA[CSV_COLONNA_COGNOME]);
		anagraficaBase.setNascita(createNascita());
		Preconditions.checkNotNull(rigaMSNA[CSV_COLONNA_SESSO], "Sex must not be null");
		Preconditions.checkArgument(rigaMSNA[CSV_COLONNA_SESSO].matches("^[MF]$"), "flgSex must match [MF]");
		anagraficaBase.setSesso(rigaMSNA[CSV_COLONNA_SESSO].equalsIgnoreCase("M") ? SessoType.M : SessoType.F);
		anagraficaBase.setCittadinanza1(rigaMSNA[CSV_COLONNA_CITTADINANZA] == null ? CITTADINANZA_DEFAULT : Long.parseLong(rigaMSNA[CSV_COLONNA_CITTADINANZA_COD_INSIEL]));
		return anagraficaBase;
	}

	private NascitaType createNascita() {
		Preconditions.checkNotNull(rigaMSNA);
		NascitaType nascitaType = new NascitaType();
		nascitaType.setData(getXmlDate(rigaMSNA[CSV_COLONNA_DATA_NASCITA]));
		nascitaType.setStato(getStatoNascita());
		nascitaType.setComune(getComuneNascita());
		return nascitaType;
	}

	private StatoType getStatoNascita() {
		StatoType statoType = new StatoType();
		statoType.setCodiceCatastale(rigaMSNA[CSV_COLONNA_STATO_NASCITA_COD_CAT]);
		statoType.setCodiceIstat(rigaMSNA[CSV_COLONNA_STATO_NASCITA_COD_ISTAT]);
		statoType.setDescrizione(rigaMSNA[CSV_COLONNA_STATO_NASCITA]);
		return statoType;
	}

	private ComuneType getComuneNascita() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(rigaMSNA[CSV_COLONNA_STATO_NASCITA]);
		comuneType.setCodiceCatastale(EMPTY_STRING);
		comuneType.setCodiceIstat(EMPTY_STRING);
		return comuneType;
	}

	private Residenza getResidenza() {
		AnagraficaType.DatiComuni.Residenza residenza = new AnagraficaType.DatiComuni.Residenza();
		residenza.setIndirizzoResidenza(getIndirizzoResidenza());
		residenza.setDecorrenzaResidenza(null);
		residenza.setTipologiaResidenza(RESIDENZA_DEFAULT_MSNA);
		Preconditions.checkNotNull(residenza.getTipologiaResidenza(), "tipologia residenza null");
		return residenza;
	}

	private IndirizzoType getIndirizzoResidenza() {
		IndirizzoType indirizzoType = new IndirizzoType();
		indirizzoType.setToponimo(getToponimoResidenza());
		return indirizzoType;
	}

	private ToponimoType getToponimoResidenza() {
		ToponimoType toponimoType = new ToponimoType();
		toponimoType.setStato(getStatoResidenza());
		toponimoType.setComune(getComuneResidenza());
		toponimoType.setCap(CAP_DEFAULT);
		toponimoType.setIndirizzo(DEFAULT_INDIRIZZO_MSNA);
		toponimoType.setNumeroCivico(DEFAULT_CIVICO_MSNA);
		return toponimoType;
	}

	private StatoType getStatoResidenza() {
		StatoType statoType = new StatoType();
		statoType.setCodiceCatastale(EMPTY_STRING);
		statoType.setCodiceIstat(STATO_CODICE_ISTAT_DEFAULT);
		statoType.setDescrizione(STATO_DESCR_DEFAULT);
		return statoType;
	}

	private ComuneType getComuneResidenza() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(COMUNE_DESCR_DEFAULT);
		comuneType.setCodiceCatastale(COMUNE_COD_CAT_DEFAULT);
		comuneType.setCodiceIstat(COMUNE_COD_ISTAT_DEFAULT);
		return comuneType;
	}

	private ProfiloType createProfilo() {
		ProfiloType profiloType = new ProfiloType();
		profiloType.setDataModifica(getXmlDate(new Date()));
		profiloType.setDomicilio(createDomicilio());
		profiloType.setAbilitazione(createAbilitazione());
		profiloType.setDatiFamiliari(createDatiFamiliari());
		profiloType.setDatiPersonali(createDatiPersonali());
		profiloType.setDatiProfessionali(createDatiProfessionali());
		return profiloType;
	}

	private Domicilio createDomicilio() {
		Domicilio domicilio = new ProfiloType.Domicilio();
		domicilio.setToponimo(getToponimoDomicilio());
		return domicilio;
	}

	private ToponimoType getToponimoDomicilio() {
		ToponimoType toponimoType = new ToponimoType();
		toponimoType.setStato(getStatoDomicilio());
		toponimoType.setComune(getComuneDomicilio());
		toponimoType.setCap(CAP_DEFAULT);
		toponimoType.setIndirizzo(DEFAULT_INDIRIZZO_MSNA);
		toponimoType.setNumeroCivico(DEFAULT_CIVICO_MSNA);
		return toponimoType;
	}

	private StatoType getStatoDomicilio() {
		StatoType statoType = new StatoType();
		statoType.setCodiceCatastale(EMPTY_STRING);
		statoType.setCodiceIstat(STATO_CODICE_ISTAT_DEFAULT);
		statoType.setDescrizione(STATO_DESCR_DEFAULT);
		return statoType;
	}

	private ComuneType getComuneDomicilio() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(COMUNE_DESCR_DEFAULT);
		comuneType.setCodiceCatastale(COMUNE_COD_CAT_DEFAULT);
		comuneType.setCodiceIstat(COMUNE_COD_ISTAT_DEFAULT);
		return comuneType;
	}

	private Abilitazione createAbilitazione() {
		Abilitazione abilitazione = new ProfiloType.Abilitazione();
		abilitazione.setDataPresaInCarico(getXmlDate(rigaMSNA[CSV_COLONNA_DATA_PRESA_IN_CARICO]));
		return abilitazione;
	}

	private DatiFamiliari createDatiFamiliari() {
		DatiFamiliari datiFamiliari = new ProfiloType.DatiFamiliari();
		datiFamiliari.setNucleoFamiliare(TIPO_NUCLEO_FAMILIARE_MSNA_DEFAULT);
		datiFamiliari.setNumerositaNucleoFam(NUMEROSITA_NUCLEO_FAM_MSNA_DEFAULT);
		datiFamiliari.setStatoCivile(STATO_CIVILE_MSNA_DEFAULT);
		return datiFamiliari;
	}

	private DatiPersonali createDatiPersonali() {
		DatiPersonali datiPersonali = new ProfiloType.DatiPersonali();
		datiPersonali.setMsna(SiNoType.S);
		datiPersonali.setCertificatoL104(IP_CERTIFICATO_L104_DEFAULT);
		datiPersonali.setDemenzaCertificata(SiNoType.N);
		datiPersonali.setStatoInvalidita(IP_STATO_INV_MSNA_DEFAULT);
		datiPersonali.setProvvedimentoGiudiziario(IP_PROVVEDIMENTO_GIUDIZIARIO_MSNA_DEFAULT);
		datiPersonali.setIsee(createIseeType());
		datiPersonali.setNote(EMPTY_STRING);
		return datiPersonali;
	}

	private DatiProfessionali createDatiProfessionali() {
		DatiProfessionali datiProfessionali = new ProfiloType.DatiProfessionali();
		datiProfessionali.setTitoloStudio(TITOLO_DI_STUDIO_MSNA_DEFAULT);
		datiProfessionali.setCondizioneProfessionale(CONDIZIONE_PROFESSIONALE_DEFAULT);
		return datiProfessionali;
	}

	private ProgettoType createProgetto() {
		ProgettoType progettoType = new ProgettoType();
		progettoType.setDataModifica(getXmlDate(new Date()));
		progettoType.setProblematiche(createProblematiche());
		progettoType.setNote(EMPTY_STRING);
		progettoType.setRisorse(EMPTY_STRING);
		return progettoType;
	}

	private ProblematicheType createProblematiche() {
		ProblematicheType problematicheType = new ProblematicheType();
		problematicheType.getMacroproblematica().add(getMacroproblematicaDefault());
		return problematicheType;
	}

	private Macroproblematica getMacroproblematicaDefault() {
		Macroproblematica macroproblematica = new ProblematicheType.Macroproblematica();
		macroproblematica.setNoteAltro(null);
		macroproblematica.setTipologiaMacroproblematica(TIPOLOGIA_MACROPROBLEMATICA_MSNA_DEFAULT);
		macroproblematica.setRilevanzaObiettivi(getRilevanzaObiettiviTypeDefault());
		MicroProblematicaType microproblematica = new MicroProblematicaType();
		microproblematica.setTipologiaMicroproblematica(MICRO_PROBLEMATICA_MSNA_DEFAULT);
		microproblematica.setDataInizio(getXmlDate(rigaMSNA[CSV_COLONNA_DATA_PRESA_IN_CARICO])); //TODO DA CAPIRE
		macroproblematica.getMicroproblematica().add(microproblematica);
		return macroproblematica;
	}

	private RilevanzaObiettiviType getRilevanzaObiettiviTypeDefault() {
		RilevanzaObiettiviType rilevanzaObiettiviType = new RilevanzaObiettiviType();
		rilevanzaObiettiviType.setRilevanza(RILEVANZA_MSNA_DEFAULT);
		rilevanzaObiettiviType.setDettaglio(DETTAGLIO_PROBLEMATICA_MSNA_DEFAULT); 
		rilevanzaObiettiviType.setFronteggiamento(FRONTEGGIAMENTO_MSNA_DEFAULT);
		rilevanzaObiettiviType.setObiettivoPrevalente(OBIETTIVO_PREVALENTE_MSNA_DEFAULT);
		return rilevanzaObiettiviType;
	}

	private @Nullable IseeType createIseeType() {
		IseeType iseeType = new IseeType();
		iseeType.setTipologiaIsee(TIPOLOGIA_ISEE_MSNA_DEFAULT);
		iseeType.setDataScadenza(getXmlDate(new Date()));
		iseeType.setValore(new BigDecimal(0));
		return iseeType;
	}

}
