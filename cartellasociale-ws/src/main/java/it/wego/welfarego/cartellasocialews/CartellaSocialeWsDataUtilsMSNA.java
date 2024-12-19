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
import it.wego.welfarego.cartellasocialews.beans.NascitaType;
import it.wego.welfarego.cartellasocialews.beans.ProblematicheType;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType;
import it.wego.welfarego.cartellasocialews.beans.ProgettoType;
import it.wego.welfarego.cartellasocialews.beans.SessoType;
import it.wego.welfarego.cartellasocialews.beans.SiNoType;
import it.wego.welfarego.cartellasocialews.beans.StatoType;
import it.wego.welfarego.cartellasocialews.beans.ToponimoType;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType.DatiComuni;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType.DatiComuni.Residenza;
import it.wego.welfarego.cartellasocialews.beans.ComuneType;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Abilitazione;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiFamiliari;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiPersonali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiProfessionali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Domicilio;

public class CartellaSocialeWsDataUtilsMSNA extends CartellaSocialeWSUtilsBase {

	private String[] rigaMSNA;

	public void setRigaMSNA(String[] rigaMSNA) {
		this.rigaMSNA = rigaMSNA;
	}

	public CartellaSocialeWsDataUtilsMSNA(CartellaSocialeWsClient cartellaSocialeWsClient) {
		super(cartellaSocialeWsClient);
	}

	private static final String RESIDENZA_DEFAULT_MSNA = "36";
	private static final String STATO_CIVILE_MSNA_DEFAULT = "0";
	private static final String TIPO_NUCLEO_FAMILIARE_MSNA_DEFAULT = "1";
	private static final int NUMEROSITA_NUCLEO_FAM_MSNA_DEFAULT = 1;
	private static final String IP_STATO_INV_MSNA_DEFAULT = "37";
	private static final String TITOLO_DI_STUDIO_MSNA_DEFAULT = "1";

	public InserimentoCartellaSociale createInserimentoCartellaSocialeRequest(String[] rigaMSNA) {
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
		anagraficaType.setIdentificativoSottostrutturaSsc("7");
		return null;
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

	private ProgettoType createProgetto() {
		ProgettoType progettoType = new ProgettoType();
		progettoType.setDataModifica(getXmlDate(new Date()));
		progettoType.setProblematiche(createProblematiche());
		progettoType.setNote(EMPTY_STRING);
		progettoType.setRisorse(EMPTY_STRING);
		return progettoType;
	}

	private ProblematicheType createProblematiche() {
		// TODO Auto-generated method stub
		return null;
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
		toponimoType.setIndirizzo(DEFAULT_INDIRIZZO);
		toponimoType.setNumeroCivico(DEFAULT_CIVICO);
		return toponimoType;
	}

	private ComuneType getComuneDomicilio() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(COMUNE_DESCR_DEFAULT);
		comuneType.setCodiceCatastale(COMUNE_COD_CAT_DEFAULT);
		comuneType.setCodiceIstat(COMUNE_COD_ISTAT_DEFAULT);
		return comuneType;
	}

	private StatoType getStatoDomicilio() {
		StatoType statoType = new StatoType();
		statoType.setCodiceCatastale(EMPTY_STRING);
		statoType.setCodiceIstat(STATO_CODICE_ISTAT_DEFAULT);
		statoType.setDescrizione(STATO_DESCR_DEFAULT);
		return statoType;
	}

	private ToponimoType getToponimoResidenza() {
		ToponimoType toponimoType = new ToponimoType();
		toponimoType.setStato(getStatoResidenza());
		toponimoType.setComune(getComuneResidenza());
		toponimoType.setCap(EMPTY_STRING);
		toponimoType.setIndirizzo(EMPTY_STRING);
		toponimoType.setNumeroCivico(EMPTY_STRING);
		return toponimoType;
	}

	private ComuneType getComuneResidenza() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(EMPTY_STRING);
		comuneType.setCodiceCatastale(EMPTY_STRING);
		comuneType.setCodiceIstat(EMPTY_STRING);
		return comuneType;
	}

	private StatoType getStatoResidenza() {
		StatoType statoType = new StatoType();
		statoType.setDescrizione(rigaMSNA[5]);
		statoType.setCodiceCatastale(rigaMSNA[6]);
		statoType.setCodiceIstat(rigaMSNA[7]);
		return statoType;
	}

	private IndirizzoType getIndirizzoResidenza() {
		IndirizzoType indirizzoType = new IndirizzoType();
		indirizzoType.setToponimo(getToponimoResidenza());
		return indirizzoType;
	}

	private Residenza getResidenza() {
		AnagraficaType.DatiComuni.Residenza residenza = new AnagraficaType.DatiComuni.Residenza();
		residenza.setIndirizzoResidenza(getIndirizzoResidenza());
		residenza.setDecorrenzaResidenza(null);
		residenza.setTipologiaResidenza(RESIDENZA_DEFAULT_MSNA);
		Preconditions.checkNotNull(residenza.getTipologiaResidenza(), "tipologia residenza null");
		return residenza;
	}

	private NascitaType createNascita() {
		Preconditions.checkNotNull(rigaMSNA);
		NascitaType nascitaType = new NascitaType();
		nascitaType.setData(getXmlDate(rigaMSNA[4]));
		nascitaType.setStato(getStatoNascita());
		nascitaType.setComune(getComuneNascita());
		return nascitaType;
	}

	private StatoType getStatoNascita() {
		StatoType statoType = new StatoType();
		statoType.setCodiceCatastale(rigaMSNA[6]);
		statoType.setCodiceIstat(rigaMSNA[7]);
		statoType.setDescrizione(rigaMSNA[5]);
		return statoType;
	}

	private ComuneType getComuneNascita() {
		ComuneType comuneType = new ComuneType();
		comuneType.setDescrizione(EMPTY_STRING);
		comuneType.setCodiceCatastale(EMPTY_STRING);
		comuneType.setCodiceIstat(EMPTY_STRING);
		return comuneType;
	}

	private AnagraficaBaseType createAnagraficaBase() {
		Preconditions.checkNotNull(rigaMSNA);
		AnagraficaBaseType anagraficaBase = new AnagraficaBaseType();
		anagraficaBase.setNome(rigaMSNA[0]);
		anagraficaBase.setCognome(rigaMSNA[1]);
		anagraficaBase.setNascita(createNascita());

		Preconditions.checkNotNull(rigaMSNA[2], "Sex must not be null");
		Preconditions.checkArgument(rigaMSNA[2].matches("^[MF]$"), "flgSex must match [MF]");
		anagraficaBase.setSesso(rigaMSNA[2].equalsIgnoreCase("M") ? SessoType.M : SessoType.F);
		anagraficaBase.setCittadinanza1(rigaMSNA[3] == null ? CITTADINANZA_DEFAULT : Long.parseLong(rigaMSNA[4]));
		anagraficaBase.setCittadinanza2(null); // ....

		return anagraficaBase;
	}

	private DatiComuni createDatiComuni() {
		Preconditions.checkNotNull(rigaMSNA);
		AnagraficaType.DatiComuni datiComuni = new AnagraficaType.DatiComuni();
		datiComuni.setAnagraficaBase(createAnagraficaBase());
		datiComuni.setResidenza(getResidenza());
		return datiComuni;
	}

	private Abilitazione createAbilitazione() {
		Abilitazione abilitazione = new ProfiloType.Abilitazione();
		abilitazione.setDataPresaInCarico(getXmlDate(rigaMSNA[19]));
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
		datiPersonali.setCertificatoL104(IP_CERTIFICATO_L104_DEFAULT);
		datiPersonali.setDemenzaCertificata(SiNoType.N);
		datiPersonali.setStatoInvalidita(IP_STATO_INV_MSNA_DEFAULT);
		datiPersonali.setProvvedimentoGiudiziario(IP_PROVVEDIMENTO_GIUDIZIARIO_DEFAULT);
		datiPersonali.setIsee(createIseeType());
		datiPersonali.setNote(EMPTY_STRING);
		return datiPersonali;
	}

	private @Nullable IseeType createIseeType() {

		IseeType iseeType = new IseeType();
		iseeType.setTipologiaIsee("1");
		iseeType.setDataScadenza(null);
		iseeType.setValore(new BigDecimal(0));
		return iseeType;
	}

	private DatiProfessionali createDatiProfessionali() {
		DatiProfessionali datiProfessionali = new ProfiloType.DatiProfessionali();
		datiProfessionali.setTitoloStudio(TITOLO_DI_STUDIO_MSNA_DEFAULT);
		datiProfessionali.setCondizioneProfessionale(CONDIZIONE_PROFESSIONALE_DEFAULT);
		return datiProfessionali;
	}

	public void setIdCsr(String string) {
		// TODO Auto-generated method stub

	}

}
