/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.cartellasocialews;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import it.wego.persistence.PersistenceAdapter;
import it.wego.utils.cf.CFUtils;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaBaseType;
import it.wego.welfarego.cartellasocialews.beans.AnagraficaType;
import it.wego.welfarego.cartellasocialews.beans.ChiudiCartella;
import it.wego.welfarego.cartellasocialews.beans.ComuneType;
import it.wego.welfarego.cartellasocialews.beans.DettaglioDomiciliareType;
import it.wego.welfarego.cartellasocialews.beans.DettaglioMacrointerventoSADType;
import it.wego.welfarego.cartellasocialews.beans.DettaglioMicrointerventoSADType;
import it.wego.welfarego.cartellasocialews.beans.IndirizzoType;
import it.wego.welfarego.cartellasocialews.beans.InserimentoCartellaSociale;
import it.wego.welfarego.cartellasocialews.beans.NuovoInserimentoIntervento;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew.Domiciliare;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew.EconomicoNew;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta;
import it.wego.welfarego.cartellasocialews.beans.InterventoNewType.SpecificazioneNew.Residenziale;
import it.wego.welfarego.cartellasocialews.beans.IseeType;
import it.wego.welfarego.cartellasocialews.beans.ListaMicroInterventiSADType;
import it.wego.welfarego.cartellasocialews.beans.MicroProblematicaType;
import it.wego.welfarego.cartellasocialews.beans.ModificaAnagrafica;
import it.wego.welfarego.cartellasocialews.beans.ModificaIntervento;
import it.wego.welfarego.cartellasocialews.beans.ModificaProfilo;
import it.wego.welfarego.cartellasocialews.beans.ModificaProgetto;
import it.wego.welfarego.cartellasocialews.beans.NascitaType;
import it.wego.welfarego.cartellasocialews.beans.NuovaModificaIntervento;
import it.wego.welfarego.cartellasocialews.beans.ProblematicheType;
import it.wego.welfarego.cartellasocialews.beans.ProblematicheType.Macroproblematica;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Abilitazione;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiFamiliari;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiPersonali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.DatiProfessionali;
import it.wego.welfarego.cartellasocialews.beans.ProfiloType.Domicilio;
import it.wego.welfarego.cartellasocialews.beans.ProgettoType;
import it.wego.welfarego.cartellasocialews.beans.RiattivaCartella;
import it.wego.welfarego.cartellasocialews.beans.RilevanzaObiettiviType;
import it.wego.welfarego.cartellasocialews.beans.SADType;
import it.wego.welfarego.cartellasocialews.beans.SessoType;
import it.wego.welfarego.cartellasocialews.beans.SiNoType;
import it.wego.welfarego.cartellasocialews.beans.StatoType;
import it.wego.welfarego.cartellasocialews.beans.ToponimoType;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Luogo;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiMacroProblematica;
import it.wego.welfarego.persistence.entities.PaiMicroProblematica;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import jakarta.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 *
 * @author aleph
 */
public class CartellaSocialeWsDataUtils extends PersistenceAdapter {

    private static final String EMPTY_STRING = "",
            SINGLE_X = "X",
            DOUBLE_X = "XX",
            VERSIONE = "?",
            DEFAULT_CIVICO="1",
            DEFAULT_INDIRIZZO="PIAZZA DELL'UNITA'D'ITALIA",
            IP_CERTIFICATO_L104_DEFAULT = "38",
            IP_PROVVEDIMENTO_GIUDIZIARIO_DEFAULT = "19",
            MOTIVAZIONE_CHIUSURA_CARTELLA_DEFAULT = "60",
            FRONTEGGIAMENTO_DEFAULT = "1",
            RILEVANZA_DEFAULT = "4",
            OBBIETTIVO_PREVALENTE_DEFAULT = "2",
            STATO_CIVILE_DEFAULT = "1",
            TITOLO_DI_STUDIO_DEFAULT = "11",
            CONDIZIONE_PROFESSIONALE_DEFAULT = "8",
            TIPOLOGIA_MACROPROBLEMATICA_DEFAULT = "9",
            MICRO_PROBLEMATICA_DEFAULT = "64",
            RESIDENZA_DEFAULT="35",
            PRESENZA_PIU_ADDETTI_DEFAULT = "N";
    
    private static final Integer CITTADINANZA_DEFAULT = 1;
    private final static Properties parametriMapping = new Properties(); 
    
    static {
        try {
            parametriMapping.load(CartellaSocialeWsDataUtils.class.getResourceAsStream("/parametriMapping.properties"));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    private Pai pai;
    private AnagrafeSoc anagrafeSoc;
    private PaiIntervento paiIntervento;
    private final CartellaSocialeWsClient wsClient;
    private static final Map<Integer, String> defaultMicroproblematiche;
    static{
    	 Map<Integer, String> aMap = new HashMap<Integer, String>();
         aMap.put(1, "75");
         aMap.put(2 ,"17");
         aMap.put(3 ,"25");
         aMap.put(4 ,"32");
         aMap.put(5 ,"38");
         aMap.put(6 ,"46");
         aMap.put(7 ,"76");
         aMap.put(8 ,"60");
         aMap.put(9 ,"64");
         aMap.put(10 ,"69");
         defaultMicroproblematiche = Collections.unmodifiableMap(aMap);
    }
    
    private XMLGregorianCalendar getXmlDate(Date date){
    	
    	String dataFormattata = dateFormat1.format(date);
    	
    	try {
			XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormattata);
			return date2;
		} catch (DatatypeConfigurationException e) {
			 throw new RuntimeException(e);
		}


    }

    protected CartellaSocialeWsDataUtils(CartellaSocialeWsClient cartellaSocialeWsClient) {
        Preconditions.checkNotNull(cartellaSocialeWsClient);
        this.wsClient = cartellaSocialeWsClient;
        try {
            DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setPai(Pai pai) {
        Preconditions.checkNotNull(pai, "pai cannot be null");
        this.pai = pai;
        setAnagrafeSoc(pai.getAnagrafeSoc());
        if (paiIntervento != null && paiIntervento.getPai().getCodPai() != pai.getCodPai()) {
            paiIntervento = null;
        }
        if (paiIntervento == null) {
            paiIntervento = new PaiInterventoDao(getEntityManager()).findLastByCodPai(pai.getCodPai());
            getLogger().debug("loaded paiIntervento for pai = {} : {}", pai, paiIntervento);
        }
    }

    public void setPaiIntervento(PaiIntervento paiIntervento) {
        Preconditions.checkNotNull(paiIntervento, "paiIntervento cannot be null");
        this.paiIntervento = paiIntervento;
        setPai(paiIntervento.getPai());
    }

    public void setAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
        Preconditions.checkNotNull(anagrafeSoc, "anagrafeSoc cannot be null");
        this.anagrafeSoc = anagrafeSoc;
        if (pai != null && pai.getAnagrafeSoc().getCodAna() != anagrafeSoc.getCodAna()) {
            pai = null;
        }
        if (pai == null) {
            pai = new PaiDao(getEntityManager()).findLastPai(anagrafeSoc.getCodAna());
            if(pai==null){
            	getLogger().info("Attenzione non ci sono pai aperti");
            	pai = new PaiDao(getEntityManager()).findAnyLastPai(anagrafeSoc.getCodAna());
            }
            if (pai != null) {
                setPai(pai);
            }
            getLogger().debug("loaded pai for ana = {} : {}", anagrafeSoc, pai);
        }
    }

    public Pai getPai() {
        return pai;
    }

    public PaiIntervento getPaiIntervento() {
        return paiIntervento;
    }

    public AnagrafeSoc getAnagrafeSoc() {
        return anagrafeSoc;
    }

    public boolean shouldShredSource() {
        return Boolean.parseBoolean(wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.shredSource"));
    }

    public @Nullable
    String getCodiceOperatore() {
        return Strings.emptyToNull(wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.codiceOperatore"));
    }

    public @Nullable
    String getComuneCartella() {
        return Strings.emptyToNull(wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.comuneCartella"));
    }

    private @Nullable
    Integer getDurataMesi() {
        Preconditions.checkNotNull(paiIntervento);
        if(paiIntervento.getDtFine()!=null){
        	  int durata = Months.monthsBetween(new DateTime(paiIntervento.getDtAvvio()), new DateTime(paiIntervento.getDtFine())).getMonths();
              getLogger().debug("calcolata durata mesi da {} a {} : {}", new Object[]{paiIntervento.getDtAvvio(), paiIntervento.getDtFine(), durata});
              return durata;
        }
        else if (paiIntervento.getDurMesi() != null) {
            return paiIntervento.getDurMesi();
        }  else {
            return null;
        }
    }

    private String getMicroTipintCsrNew() {
        return paiIntervento.getTipologiaIntervento().getCodIntCsr();
    }

    private String getMacroTipintCsrNew() {
        return paiIntervento.getTipologiaIntervento().getCodTipintCsr();
    }

    public String requireCodiceOperatore() {
        String codiceOperatore = getCodiceOperatore();
        Preconditions.checkNotNull(codiceOperatore);
        return codiceOperatore;
    }

    public String requireComuneCartella() {
        String comuneCartella = getComuneCartella();
        Preconditions.checkNotNull(comuneCartella);
        return comuneCartella;
    }

    public Long requireIdCartella() {
        Preconditions.checkNotNull(Strings.emptyToNull(anagrafeSoc.getIdCsr()), "missing id csr");
        return Long.parseLong(anagrafeSoc.getIdCsr());
    }

    public Long requireIdIntervento() {
        Preconditions.checkNotNull(Strings.emptyToNull(paiIntervento.getIdCsr()), "missing id csr");
        return Long.parseLong(paiIntervento.getIdCsr());
    }

    public String getCurrentDate1() {
        return dateFormat1.format(new Date());
    }

    public @Nullable
    String shredIfNeeded(@Nullable String str) {
        if (str != null && shouldShredSource() && str.length() >= 2) {
            String head = str.substring(0, 1), tail = str.substring(str.length() - 1);
            return head + DigestUtils.sha512Hex(str + "justSomeSalt" + Strings.nullToEmpty(wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.shredSalt"))).toUpperCase().replaceAll("[^A-Z]*", "").substring(0, 10) + tail;
        } else {
            return str;
        }
    }

    private StatoType getStatoType(Luogo luogo) {
        StatoType statoType = new StatoType();
        statoType.setCodiceCatastale(luogo.getStato() == null ? EMPTY_STRING : Strings.nullToEmpty(luogo.getStato().getCodCatast()));
        statoType.setCodiceIstat(luogo.getStato() == null ? EMPTY_STRING : luogo.getStato().getCodIstat());
        statoType.setDescrizione(luogo.getStatoText() == null ? EMPTY_STRING : luogo.getStatoText());
        return statoType;
    }

    private ComuneType getComuneType(Luogo luogo) {
        ComuneType comuneType = new ComuneType();

        comuneType.setDescrizione(MoreObjects.firstNonNull(Strings.emptyToNull(luogo.getComuneText()), DOUBLE_X));
        if(luogo.getComune()!=null){
        comuneType.setCodiceCatastale(luogo.getComune().getCodCatast() == null ? EMPTY_STRING : Strings.nullToEmpty(luogo.getComune().getCodCatast()));
        comuneType.setCodiceIstat(luogo.getComune().getCodIstat() == null ? EMPTY_STRING : luogo.getComune().getCodIstat());
        }
        else {
        	comuneType.setCodiceCatastale(EMPTY_STRING);
        	comuneType.setCodiceIstat(EMPTY_STRING);
        }

        return comuneType;
    }

    private ToponimoType getToponimoType(Luogo luogo) {
        ToponimoType toponimoType = new ToponimoType();
        toponimoType.setStato(getStatoType(luogo));
        toponimoType.setComune(getComuneType(luogo));
        toponimoType.setCap(MoreObjects.firstNonNull(Strings.emptyToNull(luogo.getCap()), "34100"));
        toponimoType.setIndirizzo(MoreObjects.firstNonNull(shredIfNeeded(Strings.emptyToNull(luogo.getViaText())), DEFAULT_INDIRIZZO));
        toponimoType.setNumeroCivico(MoreObjects.firstNonNull(Strings.emptyToNull(luogo.getCivicoText()), DEFAULT_CIVICO));
        return toponimoType;
    }

    private IndirizzoType getIndirizzoType(Luogo luogo) {
        IndirizzoType indirizzoType = new IndirizzoType();
        indirizzoType.setToponimo(getToponimoType(luogo));
        return indirizzoType;
    }

    private AnagraficaType.DatiComuni.Residenza getResidenza() {
        AnagraficaType.DatiComuni.Residenza residenza = new AnagraficaType.DatiComuni.Residenza();
        residenza.setIndirizzoResidenza(getIndirizzoType(anagrafeSoc.getLuogoResidenza()));
        residenza.setDecorrenzaResidenza(null); //wathever
        if (anagrafeSoc.getIdParamTipologiaResidenza() == null) {
            getLogger().debug("tipologiaResidenza not found, using default ('35' = RESIDENZA)");
            residenza.setTipologiaResidenza(RESIDENZA_DEFAULT);
        } else {
      
        //residenza.setTipologiaResidenza(parametriMapping.getProperty(anagrafeSoc.getIdParamTipologiaResidenza().getIdParamIndata().toString()));
        residenza.setTipologiaResidenza(anagrafeSoc.getIdParamTipologiaResidenza().getDesParam());
        Preconditions.checkNotNull(residenza.getTipologiaResidenza(), "tipologia residenza null");
        }
        return residenza;
    }

    private NascitaType createNascitaType() {
        Preconditions.checkNotNull(anagrafeSoc);
        NascitaType nascitaType = new NascitaType();
        nascitaType.setData(anagrafeSoc.getDtNasc() == null ? null : getXmlDate(anagrafeSoc.getDtNasc()));
        nascitaType.setStato(getStatoType(anagrafeSoc.getLuogoNascita()));
        nascitaType.setComune(getComuneType(anagrafeSoc.getLuogoNascita()));

        return nascitaType;
    }

    private AnagraficaBaseType createAnagraficaBaseType() {
        Preconditions.checkNotNull(anagrafeSoc);
        AnagraficaBaseType anagraficaBase = new AnagraficaBaseType();
        anagraficaBase.setNome(shredIfNeeded(anagrafeSoc.getNome()));
        anagraficaBase.setCognome(shredIfNeeded(anagrafeSoc.getCognome()));

        anagraficaBase.setNascita(createNascitaType());


        Preconditions.checkNotNull(anagrafeSoc.getFlgSex(), "flgSex must not be null");
        Preconditions.checkArgument(anagrafeSoc.getFlgSex().matches("^[MF]$"), "flgSex must match [MF]");
        anagraficaBase.setSesso(anagrafeSoc.getFlgSex().equalsIgnoreCase("M") ? SessoType.M : SessoType.F);
        anagraficaBase.setCodiceFiscale(shouldShredSource() ? (CFUtils.buildCf(
                anagraficaBase.getNome(),
                anagraficaBase.getCognome(),
                anagrafeSoc.getDtNasc(),
                anagrafeSoc.getLuogoNascita().getComune() != null && !Strings.isNullOrEmpty(anagrafeSoc.getLuogoNascita().getComune().getCodCatast()) ? anagrafeSoc.getLuogoNascita().getComune().getCodCatast()
                : "Z" + anagraficaBase.getNascita().getStato().getCodiceCatastale(),
                anagrafeSoc.getFlgSex().equalsIgnoreCase("F"))) : (anagrafeSoc.getCodFisc().toUpperCase()));
        if (!Strings.isNullOrEmpty(anagrafeSoc.getCodAnaFamCom())) {
            anagraficaBase.setCodiceNucleoFamiliare(Long.parseLong(anagrafeSoc.getCodAnaFamCom()));
        }
        anagraficaBase.setCittadinanza1(anagrafeSoc.getCodStatoCitt() == null ? CITTADINANZA_DEFAULT : anagrafeSoc.getCodStatoCitt().getCodiceCittadinanzaInsiel());
        anagraficaBase.setCittadinanza2(null); // ....

        return anagraficaBase;
    }

    private AnagraficaType.DatiComuni createDatiComuni() {
        Preconditions.checkNotNull(anagrafeSoc);
        AnagraficaType.DatiComuni datiComuni = new AnagraficaType.DatiComuni();
        datiComuni.setAnagraficaBase(createAnagraficaBaseType());
        datiComuni.setResidenza(getResidenza());
        return datiComuni;
    }

    private AnagraficaType createAnagraficaType() {
        Preconditions.checkNotNull(anagrafeSoc, "anagrafeSoc must be not null");
        AnagraficaType anagraficaType = new AnagraficaType();
        anagraficaType.setOperatoreRiferimento(requireCodiceOperatore());
        anagraficaType.setComuneCartella(requireComuneCartella());
        anagraficaType.setDataModifica(getXmlDate(new Date()));
        anagraficaType.setDatiComuni(createDatiComuni());

        if (pai != null) {
            anagraficaType.setIdentificativoSottostrutturaSsc(Integer.toString(Integer.parseInt(pai.getIdParamUot().getIdParam().getCodParam()) / 100));
        }
        return anagraficaType;
    }

    private Domicilio createDomicilio() {
        Domicilio domicilio = new ProfiloType.Domicilio();
        if (!Strings.isNullOrEmpty(anagrafeSoc.getLuogoDomicilio().getComuneText())) {
            domicilio.setToponimo(getToponimoType(anagrafeSoc.getLuogoDomicilio()));
        } else {
            domicilio.setToponimo(getToponimoType(anagrafeSoc.getLuogoResidenza())); // domicilio e' obbligatorio . . quindi gli dobbiamo passare qualcosa . . 
        }

        return domicilio;
    }

    private Abilitazione createAbilitazione() {
        Abilitazione abilitazione = new ProfiloType.Abilitazione();
        //TODO check this . . 
        abilitazione.setDataPresaInCarico(getXmlDate((anagrafeSoc.getCartellaSociale().getDtApCs())));
        return abilitazione;
    }

    private String createTipoNucleoFamiliare() {

        // Va messo uno dei seguenti codici:
// 
//         1à         Persona sola
//         2à         Monogenitore con figli
//         3 à        Coppia senza figli
//        4 à        Coppia con figli
//         5 à        Altre tipologie
//         6 à        Nuclei familiari conviventi
    	if(anagrafeSoc.getCondFam()!=null){
    		return (parametriMapping.getProperty(anagrafeSoc.getCondFam().getIdParamIndata().toString()));
    	}
    	else {
        return "5";
    	}
    }

    private int createNumerositaNucleoFamiliare() {
        if (getPai() != null && getPai().getNumNuc() != null) {
            return getPai().getNumNuc();
        } else {
            return 1 + anagrafeSoc.getAnagrafeFamListAsAny().size();
        }
    }

    private DatiFamiliari createDatiFamiliari() {
        DatiFamiliari datiFamiliari = new ProfiloType.DatiFamiliari();
        datiFamiliari.setNucleoFamiliare(createTipoNucleoFamiliare());
        datiFamiliari.setNumerositaNucleoFam(createNumerositaNucleoFamiliare()); // TODO
        // TOTO DEFINIRE VALORE DI DEFAULT STATO CIVILE
        datiFamiliari.setStatoCivile(anagrafeSoc.getIdParamStatoCiv() == null ? STATO_CIVILE_DEFAULT : parametriMapping.getProperty(anagrafeSoc.getIdParamStatoCiv().getIdParamIndata().toString()));
        return datiFamiliari;
    }

    private @Nullable
    IseeType createIseeType() {
        if (pai != null && pai.getIsee() != null && pai.getDtScadIsee()!=null) {
            IseeType iseeType = new IseeType();
            iseeType.setValore(pai.getIsee());
            Preconditions.checkNotNull(pai.getDtScadIsee(), "manca la data di scadenza isee");
            iseeType.setDataScadenza(getXmlDate((pai.getDtScadIsee())));
            return iseeType;
        } else {
            return null;
        }
    }

    private DatiPersonali createDatiPersonali() {
        DatiPersonali datiPersonali = new ProfiloType.DatiPersonali();

        datiPersonali.setCertificatoL104(pai.getIdParamCertificatoL104() == null ? IP_CERTIFICATO_L104_DEFAULT : parametriMapping.getProperty(pai.getIdParamCertificatoL104().getIdParamIndata().toString()));
        datiPersonali.setDemenzaCertificata(SiNoType.valueOf(MoreObjects.firstNonNull(pai.getFlgDemenza(), SiNoType.N).toString()));
        datiPersonali.setStatoInvalidita(anagrafeSoc.getPercInvCiv()== null ? "" : parametriMapping.getProperty(anagrafeSoc.getPercInvCiv().toString()));
        datiPersonali.setProvvedimentoGiudiziario(pai.getIdParamProvvedimentoGiudiziario() == null ? IP_PROVVEDIMENTO_GIUDIZIARIO_DEFAULT : Integer.valueOf(pai.getIdParamProvvedimentoGiudiziario().getIdParam().getCodParam()).toString());
        datiPersonali.setIsee(createIseeType());
        JAXBElement<String> jaxbElement =  new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, "");
        
        datiPersonali.setNote(jaxbElement);
        return datiPersonali;
    }

    private DatiProfessionali createDatiProfessionali() {
        DatiProfessionali datiProfessionali = new ProfiloType.DatiProfessionali();
        datiProfessionali.setTitoloStudio(anagrafeSoc.getIdParamTit() == null ? TITOLO_DI_STUDIO_DEFAULT : parametriMapping.getProperty(anagrafeSoc.getIdParamTit().getIdParamIndata().toString()));

    	// INSERITO DEFAULT CONDIZIONE PROFESSIONALE
        datiProfessionali.setCondizioneProfessionale(anagrafeSoc.getIdParamCondProf() == null ? CONDIZIONE_PROFESSIONALE_DEFAULT : parametriMapping.getProperty(anagrafeSoc.getIdParamCondProf().getIdParamIndata().toString()));
        return datiProfessionali;
    }
    
    private ProfiloType createProfiloType() {
        ProfiloType profiloType = new ProfiloType();
        profiloType.setDataModifica(getXmlDate(new Date())); //wathever        
        profiloType.setDomicilio(createDomicilio());
        profiloType.setAbilitazione(createAbilitazione());
        profiloType.setDatiFamiliari(createDatiFamiliari());
        profiloType.setDatiPersonali(createDatiPersonali());
        profiloType.setDatiProfessionali(createDatiProfessionali());
        return profiloType;
    }

    private RilevanzaObiettiviType getRilevanzaObiettiviType(PaiMacroProblematica paiMacroProblematica) {
        RilevanzaObiettiviType rilevanzaObiettiviType = new RilevanzaObiettiviType();
        rilevanzaObiettiviType.setRilevanza(paiMacroProblematica.getIpRilevanza() == null ? RILEVANZA_DEFAULT : parametriMapping.getProperty(paiMacroProblematica.getIpRilevanza().getIdParamIndata().toString()));
        rilevanzaObiettiviType.setDettaglio(MoreObjects.firstNonNull(Strings.emptyToNull(paiMacroProblematica.getDettaglioNote()), DOUBLE_X)); // must be not null
        rilevanzaObiettiviType.setFronteggiamento(paiMacroProblematica.getIpFronteggiamento() == null ? FRONTEGGIAMENTO_DEFAULT : parametriMapping.getProperty(paiMacroProblematica.getIpFronteggiamento().getIdParamIndata().toString()));
        rilevanzaObiettiviType.setObiettivoPrevalente(paiMacroProblematica.getIpObiettivoPrevalente() == null ? OBBIETTIVO_PREVALENTE_DEFAULT
                : paiMacroProblematica.getIpObiettivoPrevalente().getIdParam().getCodParam().replaceFirst("^0*", ""));
        return rilevanzaObiettiviType;
    }

    private RilevanzaObiettiviType getRilevanzaObiettiviTypeDefault() {
        RilevanzaObiettiviType rilevanzaObiettiviType = new RilevanzaObiettiviType();
        rilevanzaObiettiviType.setRilevanza(RILEVANZA_DEFAULT);
        rilevanzaObiettiviType.setDettaglio(DOUBLE_X); // must be not null
        rilevanzaObiettiviType.setFronteggiamento(FRONTEGGIAMENTO_DEFAULT);
        rilevanzaObiettiviType.setObiettivoPrevalente(OBBIETTIVO_PREVALENTE_DEFAULT);
        return rilevanzaObiettiviType;
    }

	private Macroproblematica getMacroproblematica(PaiMacroProblematica paiMacroProblematica) {
		Macroproblematica macroproblematica = new ProblematicheType.Macroproblematica();

		JAXBElement<String> jNoteAltro = paiMacroProblematica.getDettaglioNote() == null ? 
				new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, SINGLE_X) : 
					new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, paiMacroProblematica.getDettaglioNote()); 
		
		macroproblematica.setNoteAltro(jNoteAltro);
		macroproblematica.setTipologiaMacroproblematica(Integer.valueOf(paiMacroProblematica.getIpMacroProblematica().getIdParam().getCodParam()).toString());
		macroproblematica.setRilevanzaObiettivi(getRilevanzaObiettiviType(paiMacroProblematica));
		if (paiMacroProblematica.getPaiMicroProblematicaList().size() > 0) {
			for (PaiMicroProblematica paiMicroProblematica : paiMacroProblematica.getPaiMicroProblematicaList()) {
				MicroProblematicaType microproblematica = new MicroProblematicaType();
				// modifica per inserimento date inizio e fine micro problematiche, se il pai è
				// chiuso allora inserisco la data di chiusura pai se no ladata di apertura e
				// lascio quella di chiusura in bianco
				microproblematica.setTipologiaMicroproblematica(Integer.valueOf(paiMicroProblematica.getIpMicroProblematica().getIdParam().getCodParam()).toString());
				microproblematica.setDataInizio(pai.getDtApePai()!=null ?  getXmlDate(pai.getDtApePai()) : getXmlDate(new Date()));
				if (pai.getDtChiusPai() != null) {
					microproblematica.setDataFine(getXmlDate(pai.getDtChiusPai()));
				}
				macroproblematica.getMicroproblematica().add(microproblematica);
			}
		} else {
			MicroProblematicaType microproblematica = new MicroProblematicaType();
			microproblematica.setTipologiaMicroproblematica(defaultMicroproblematiche.get(Integer.valueOf(paiMacroProblematica.getIpMacroProblematica().getIdParam().getCodParam())));
			microproblematica.setDataInizio(pai.getDtApePai() != null ? getXmlDate(pai.getDtApePai()) : getXmlDate(new Date()));
			if (pai.getDtChiusPai() != null) {
				microproblematica.setDataFine(getXmlDate(pai.getDtChiusPai()));
			}
			macroproblematica.getMicroproblematica().add(microproblematica);
		}
		
		Preconditions.checkArgument(!Strings.isNullOrEmpty( macroproblematica.getNoteAltro().getValue()) || !macroproblematica.getMicroproblematica().isEmpty(),
				"e' necessario valorizzare le note o almeno una microproblematica per macroproblematica");

		return macroproblematica;
	}

    private Macroproblematica getMacroproblematicaDefault() {
        Macroproblematica macroproblematica = new ProblematicheType.Macroproblematica();
        macroproblematica.setNoteAltro(null);
        macroproblematica.setTipologiaMacroproblematica(TIPOLOGIA_MACROPROBLEMATICA_DEFAULT);
        macroproblematica.setRilevanzaObiettivi(getRilevanzaObiettiviTypeDefault());
        MicroProblematicaType microproblematica = new MicroProblematicaType();
        microproblematica.setTipologiaMicroproblematica(MICRO_PROBLEMATICA_DEFAULT);
        microproblematica.setDataInizio(pai.getDtApePai()!=null ?  getXmlDate(pai.getDtApePai()) : getXmlDate(new Date()));
        macroproblematica.getMicroproblematica().add(microproblematica);
        macroproblematica.setNoteAltro(new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, SINGLE_X));

        return macroproblematica;
    }

    private ProblematicheType createProblematicheType() {
        Preconditions.checkNotNull(pai, "pai must not be null");
        ProblematicheType problematicheType = new ProblematicheType();
        // inserita problematica di default - verrà eventualemente modificata dai dati del db 
        if (pai.getPaiMacroProblematicaList().isEmpty()) {
            problematicheType.getMacroproblematica().add(getMacroproblematicaDefault());
        } else {
            for (PaiMacroProblematica paiMacroProblematica : pai.getPaiMacroProblematicaList()) {
                problematicheType.getMacroproblematica().add(getMacroproblematica(paiMacroProblematica));
            }
        }
        return problematicheType;
    }

    private ProgettoType createProgettoType() {
        ProgettoType progettoType = new ProgettoType();
        progettoType.setDataModifica(getXmlDate(new Date()));
        progettoType.setProblematiche(createProblematicheType());
        //TODO
        progettoType.setNote("");
        progettoType.setRisorse("");
        return progettoType;
    }

    // request data
    public ModificaAnagrafica createModificaAnagraficaRequest() {
        Preconditions.checkNotNull(anagrafeSoc, "anagrafeSoc must be not null");
        Preconditions.checkNotNull(getCodiceOperatore(), "missing codiceOperatore");
        Preconditions.checkNotNull(Strings.emptyToNull(anagrafeSoc.getIdCsr()), "missing id csr");
        ModificaAnagrafica modificaAnagraficaRequest = new ModificaAnagrafica();
        modificaAnagraficaRequest.setAnagrafica(createAnagraficaType());
        modificaAnagraficaRequest.setIdCartella(requireIdCartella());
        modificaAnagraficaRequest.setCodice(getCodiceOperatore());
        modificaAnagraficaRequest.setVersione(VERSIONE);
        return modificaAnagraficaRequest;
    }

    public InserimentoCartellaSociale createInserimentoCartellaSocialeRequest() {
        InserimentoCartellaSociale inserimentoCartellaSociale = new InserimentoCartellaSociale();
        inserimentoCartellaSociale.setCodice(requireCodiceOperatore());
        inserimentoCartellaSociale.setAnagrafica(createAnagraficaType());
        inserimentoCartellaSociale.setProfilo(createProfiloType());
        inserimentoCartellaSociale.setProgetto(createProgettoType());
        inserimentoCartellaSociale.setVersione(VERSIONE);
        return inserimentoCartellaSociale;
    }

    public ChiudiCartella createChiudiCartellaRequest() {
        ChiudiCartella chiudiCartella = new ChiudiCartella();
        chiudiCartella.setVersione(VERSIONE);
        chiudiCartella.setData(getXmlDate(new Date()));
        chiudiCartella.setCodiceOperatore(requireCodiceOperatore());
        chiudiCartella.setIdCartella(requireIdCartella());
        //TODO
        chiudiCartella.setMotivo(MOTIVAZIONE_CHIUSURA_CARTELLA_DEFAULT);
        chiudiCartella.setNote(EMPTY_STRING);
        return chiudiCartella;
    }

    public RiattivaCartella createRiattivaCartellaRequest() {
        RiattivaCartella riattivaCartella = new RiattivaCartella();
        riattivaCartella.setVersione(VERSIONE);
        riattivaCartella.setIdCartella(requireIdCartella());
        riattivaCartella.setCodiceOperatore(requireCodiceOperatore());
        return riattivaCartella;
    }

    public ModificaProfilo createModificaProfiloRequest() {
        ModificaProfilo modificaProfilo = new ModificaProfilo();
        modificaProfilo.setVersione(VERSIONE);
        modificaProfilo.setCodice(requireCodiceOperatore());
        modificaProfilo.setIdCartella(requireIdCartella());
        modificaProfilo.setProfilo(createProfiloType());
        return modificaProfilo;
    }

    public ModificaProgetto createModificaProgettoRequest() {
        ModificaProgetto modificaProgetto = new ModificaProgetto();
        modificaProgetto.setVersione(VERSIONE);
        modificaProgetto.setCodice(requireCodiceOperatore());
        modificaProgetto.setIdCartella(requireIdCartella());
        modificaProgetto.setProgetto(createProgettoType());
        return modificaProgetto;
    }
    
    private static final Map<String, String> codTipintToTipintFapMapping;

    static {
        /**
         * 45 TIPOLOGIA DI INTERVENTO FAP APA <br/> 46 TIPOLOGIA DI INTERVENTO
         * FAP APA + Vita indipendente <br/> 47 TIPOLOGIA DI INTERVENTO FAP CAF
         * <br/> 49 TIPOLOGIA DI INTERVENTO FAP Salute mentale <br/> 48
         * TIPOLOGIA DI INTERVENTO FAP Vita indipendente <br/>
         */
        Map<String, String> map = Maps.newHashMap();
        map.put(TipologiaIntervento.COD_TIPINT_FAP_APA, "45");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_APAANZIANI, "45");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_APADISABILI, "45");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_CAF, "47");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_CAFANZIANI, "47");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_CAFDISABILI, "47");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_SVI, "48");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_SOSTEGNO,"48");
        map.put(TipologiaIntervento.COD_TIPINT_FAP_DOMICILIARITAINNOVATIVA,"74");
        codTipintToTipintFapMapping = Collections.unmodifiableMap(map);
    }
    
    private final static Function<String, SiNoType> dsBoolCharToSiNoType = new Function<String, SiNoType>() {
        public SiNoType apply(String string) {
            if (Strings.isNullOrEmpty(string)) {
                return null;
            } else {
                return SiNoType.valueOf(string.toUpperCase().substring(0, 1));
            }
        }
    };

    private FapNew createFap() {
        FapNew fap = new FapNew();

        fap.setAbitareInclusivo(null);
        fap.setContestualePresenzaAddetti(EMPTY_STRING);
        fap.setDataDecorrenza(null);
        fap.setDataSegnalazione(null);
        fap.setDataUVM(null);
        fap.setDurataMesiUVM(0);
        fap.setEtaDataUVM(0);
        fap.setImportoMensile(new BigDecimal(0.0d));
        fap.setIndennitaAccompagnamento(null);
        fap.setIsee(null);
        fap.setListaAttesa(null);
        fap.setMotivoChiusura(EMPTY_STRING);
        fap.setNOreContratto(0);
        fap.setPunteggioCDRs(new BigDecimal(0.0d));
        fap.setPunteggioGeFi(0);
        fap.setPunteggioHansen(0);
        fap.setPunteggioKatz(0);
        fap.setPunteggioListaAttesa(0);
        fap.setTipologiaInterventoFap(codTipintToTipintFapMapping.get(paiIntervento.getTipologiaIntervento().getCodTipint()));

        //        fap.setDemenzaCertificata(MoreObjects.firstNonNull(dsBoolCharToSiNoType.apply(paiIntervento.getDsDemenza()),SiNoType.N));
//        fap.setDisabilitaSensoriale(MoreObjects.firstNonNull(dsBoolCharToSiNoType.apply(paiIntervento.getDsDisabilitaSensoriale()),SiNoType.N));
//        fap.setAssegnoAccompagnamento(MoreObjects.firstNonNull(dsBoolCharToSiNoType.apply(paiIntervento.getPai().getAnagrafeSoc().getFlgAccomp()), SiNoType.N));
//        fap.setDataUVD(getXmlDate(MoreObjects.firstNonNull(paiIntervento.getDsDataUVD(), paiIntervento.getDtAvvio())));
//        fap.setDurataMesiUVD(paiIntervento.getDsDurataUVD()!=null? paiIntervento.getDsDurataUVD().intValue():0);
//        fap.setPunteggioKatz(paiIntervento.getDsPunteggioScalaKatz() == null ? null : paiIntervento.getDsPunteggioScalaKatz());
        
        if(paiIntervento.getStatoInt()=='C'){
        	fap.setMotivoChiusura(paiIntervento.getNoteChius());
        }
        
        if (paiIntervento.getTipologiaIntervento().isFapSvi()) {
            //fap.setSostegnoMensVitaIndip(MoreObjects.firstNonNull(paiIntervento.getQuantita(),BigDecimal.ZERO));
            //fap.setPunteggioKatz(paiIntervento.getDsPunteggioScalaKatz() == null ? null : paiIntervento.getDsPunteggioScalaKatz());
        }

        if (paiIntervento.getTipologiaIntervento().isFapApa()) {
            //fap.setAssegnoMensAutonomAPA(MoreObjects.firstNonNull(paiIntervento.getQuantita(),BigDecimal.ZERO));
            fap.setIsee(createIseeType());
            fap.setNOreContratto(paiIntervento.getDsOreSettBadante() == null ? null : paiIntervento.getDsOreSettBadante().intValue());
            fap.setContestualePresenzaAddetti(Strings.isNullOrEmpty(paiIntervento.getDsPresenzaPiuAddetti()) ? PRESENZA_PIU_ADDETTI_DEFAULT : paiIntervento.getDsPresenzaPiuAddetti()); //TODO
        }

        if (paiIntervento.getTipologiaIntervento().isFapCaf()) {
            //fap.setContributoMensAiutoFam(MoreObjects.firstNonNull(paiIntervento.getQuantita(),BigDecimal.ZERO));
            fap.setIsee(createIseeType());
            fap.setNOreContratto(paiIntervento.getDsOreSettBadante() == null ? null : paiIntervento.getDsOreSettBadante().intValue());
            fap.setContestualePresenzaAddetti(Strings.isNullOrEmpty(paiIntervento.getDsPresenzaPiuAddetti()) ? PRESENZA_PIU_ADDETTI_DEFAULT : paiIntervento.getDsPresenzaPiuAddetti());
        }
        
        if (paiIntervento.getTipologiaIntervento().isFapDomiciliarita()) {
            //fap.setContributoMensAiutoFam(MoreObjects.firstNonNull(paiIntervento.getQuantita(),BigDecimal.ZERO));
            fap.setIsee(createIseeType());
        }
        
        if (paiIntervento.getDtChius()!=null) {
        	
			if (paiIntervento.getIndEsitoInt() != null) {
				ParametriIndataDao pid = new ParametriIndataDao(getEntityManager());
				ParametriIndata pi = pid.findOneByTipParamCodParam(Parametri.ESITO_INTERVENTO,paiIntervento.getIndEsitoInt());
				fap.setMotivoChiusura(pi.getTxt1Param());
			} else {
				fap.setMotivoChiusura("5");
			}
        }


        return fap;
    }

    private FondoSolidarieta createFondoSolidarieta() {
        FondoSolidarieta fondoSolidarieta = new EconomicoNew.FondoSolidarieta();
        fondoSolidarieta.setIsee(createIseeType());
        BigDecimal totaleErogato = new PaiInterventoMeseDao(getEntityManager()).sumBdgtConsPaiIntervento(paiIntervento);
        if(totaleErogato!=null && totaleErogato.compareTo(BigDecimal.ZERO)>0){
        fondoSolidarieta.setTotaleErogato(new PaiInterventoMeseDao(getEntityManager()).sumBdgtConsPaiIntervento(paiIntervento)); //TODO check this
        }
        else {
        	fondoSolidarieta.setTotaleErogato(paiIntervento.getCostoPrev());
        }
        return fondoSolidarieta;
    }

    private EconomicoNew createInterventoEconomicoDettaglio() {
    	EconomicoNew economico = new SpecificazioneNew.EconomicoNew();
        if (paiIntervento.getTipologiaIntervento().isFap()) {
            economico.setFapNew(createFap());
        }
        if (paiIntervento.getTipologiaIntervento().isFondoSolidarieta()) {
            economico.setFondoSolidarieta(createFondoSolidarieta());
        }
        return economico;
    }

    private DettaglioDomiciliareType createDettaglioDomiciliareType() {
        DettaglioDomiciliareType dettaglioDomiciliareType = new DettaglioDomiciliareType();
        dettaglioDomiciliareType.setQtaMensili(paiIntervento.getQuantita().round(MathContext.DECIMAL32).intValue()); // LOSS OF PRECISION!!!
        return dettaglioDomiciliareType;
    }

    private SADType createSADType() {
        SADType sadType = new SADType();
        sadType = createDettaglioMacrointerventoSADType(sadType);
        return sadType;
    }

    private SADType createDettaglioMacrointerventoSADType(SADType sadType) {
        HashMap<String, List<String>> lista = createMapMicroprestazioniSad();

        for (String key : lista.keySet()) {
            DettaglioMacrointerventoSADType dettaglioMacrointerventoSADType = new DettaglioMacrointerventoSADType();
            dettaglioMacrointerventoSADType.setTipologiaMacroInterventoSad(key);
            dettaglioMacrointerventoSADType.setArcoTemporale("");
            dettaglioMacrointerventoSADType.setErogatore("");
            dettaglioMacrointerventoSADType.setFrequenza(0);
            dettaglioMacrointerventoSADType.setNumeroVolte(0);
            dettaglioMacrointerventoSADType.setTotMin(0);
            dettaglioMacrointerventoSADType.setTotOre(0);
            dettaglioMacrointerventoSADType.setNote("");
            ListaMicroInterventiSADType listaMicro = new ListaMicroInterventiSADType();
            for (String micro : lista.get(key)) {
                DettaglioMicrointerventoSADType dett = new DettaglioMicrointerventoSADType();
                dett.setTipologiaMicroInterventoSad(micro);
                listaMicro.getMicrointerventoSad().add(dett);
            }
            dettaglioMacrointerventoSADType.setListaMicrointerventiCollegatiAttivi(listaMicro);
            sadType.getMacrointerventoSad().add(dettaglioMacrointerventoSADType);
        }

        return sadType;
    }

    private HashMap<String, List<String>> createMapMicroprestazioniSad() {
        HashMap<String, List<String>> mapMp = new HashMap<String, List<String>>();

        if (paiIntervento.getDsMicroprestazione() != null && !paiIntervento.getDsMicroprestazione().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione());
        }
        if (paiIntervento.getDsMicroprestazione2() != null && !paiIntervento.getDsMicroprestazione2().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione2());
        }
        if (paiIntervento.getDsMicroprestazione3() != null && !paiIntervento.getDsMicroprestazione3().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione3());
        }
        if (paiIntervento.getDsMicroprestazione4() != null && !paiIntervento.getDsMicroprestazione4().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione4());
        }
        if (paiIntervento.getDsMicroprestazione5() != null && !paiIntervento.getDsMicroprestazione5().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione5());
        }
        if (paiIntervento.getDsMicroprestazione6() != null && !paiIntervento.getDsMicroprestazione6().trim().equals("")) {
            mapMp = popolateMapMicroprestazioniSad(mapMp, paiIntervento.getDsMicroprestazione6());
        }
        return mapMp;
    }

    private HashMap<String, List<String>> popolateMapMicroprestazioniSad(HashMap<String, List<String>> mapMp, String valore) {
        List<String> listValue;
        String key = valore.substring(0, 1);
        if (mapMp.containsKey(key)) {
            listValue = mapMp.get(key);
        } else {
            listValue = new ArrayList<String>();
        }
        if (!listValue.contains(valore)) {
            listValue.add(valore);
        }
        mapMp.put(key, listValue);
        return mapMp;
    }

    private Domiciliare createInterventoDomiciliareDettaglio() {

        Domiciliare domiciliare = new Domiciliare();
        if (paiIntervento.getTipologiaIntervento().isSad()) {
            domiciliare.setSad(createSADType());
        } else {
            domiciliare.setDettaglio(createDettaglioDomiciliareType());
        }
        return domiciliare;
    }

    private Residenziale createInterventoResidenzialeDettaglio() {
        Residenziale residenziale = new Residenziale();

        if (paiIntervento.getDsStrutturaAcc1() != null && !paiIntervento.getDsStrutturaAcc1().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc1());
        }
        if (paiIntervento.getDsStrutturaAcc2() != null && !paiIntervento.getDsStrutturaAcc2().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc2());
        }
        if (paiIntervento.getDsStrutturaAcc3() != null && !paiIntervento.getDsStrutturaAcc3().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc3());
        }
        if (paiIntervento.getDsStrutturaAcc4() != null && !paiIntervento.getDsStrutturaAcc4().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc4());
        }
        if (paiIntervento.getDsStrutturaAcc5() != null && !paiIntervento.getDsStrutturaAcc5().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc5());
        }
        if (paiIntervento.getDsStrutturaAcc6() != null && !paiIntervento.getDsStrutturaAcc6().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc6());
        }
        if (paiIntervento.getDsStrutturaAcc7() != null && !paiIntervento.getDsStrutturaAcc7().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc7());
        }
        if (paiIntervento.getDsStrutturaAcc8() != null && !paiIntervento.getDsStrutturaAcc8().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc8());
        }
        if (paiIntervento.getDsStrutturaAcc9() != null && !paiIntervento.getDsStrutturaAcc9().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc9());
        }
        if (paiIntervento.getDsStrutturaAcc10() != null && !paiIntervento.getDsStrutturaAcc10().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc10());
        }
        if (paiIntervento.getDsStrutturaAcc11() != null && !paiIntervento.getDsStrutturaAcc11().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc11());
        }
        if (paiIntervento.getDsStrutturaAcc12() != null && !paiIntervento.getDsStrutturaAcc12().equals("")) {
            residenziale.setStruttura(paiIntervento.getDsStrutturaAcc12());
        }
        residenziale.setCodiceFamiglia("Non rilevata");

        return residenziale;
    }

    private SpecificazioneNew createSpecificazione() {
        SpecificazioneNew specificazione = new InterventoNewType.SpecificazioneNew();
        String codClasseTipint = paiIntervento.getTipologiaIntervento().getIdParamClasseTipint().getIdParam().getCodParam();
        if (Objects.equal(codClasseTipint, Parametri.CLASSE_TIPOLOGIA_INTERVENTO_ECONOMICI)) { //economico
            specificazione.setEconomicoNew(createInterventoEconomicoDettaglio());
        } else {
            specificazione.setEconomicoNew(null);
        }
        if (Objects.equal(codClasseTipint, Parametri.CLASSE_TIPOLOGIA_INTERVENTO_DOMICILIARI)) { //domiciliare
            specificazione.setDomiciliare(createInterventoDomiciliareDettaglio());
        } else {
            specificazione.setDomiciliare(null);
        }
        if (Objects.equal(codClasseTipint, Parametri.CLASSE_TIPOLOGIA_INTERVENTO_RESIDENZIALI ) || Objects.equal(codClasseTipint, Parametri.CLASSE_TIPOLOGIA_INTERVENTO_SEMI_RESIDENZIALI)) { //residenziale e semi residenziale
            specificazione.setResidenziale(createInterventoResidenzialeDettaglio());
        } else {
            specificazione.setResidenziale(null);
        }
        return specificazione;
    }

    public InterventoNewType createInterventoType() {
        Preconditions.checkNotNull(paiIntervento, "paiIntervento must be not null");
        InterventoNewType interventoType = new InterventoNewType();
        Preconditions.checkNotNull(paiIntervento.getDtApe(), "la data apertura intervento non puo' essere null");
        interventoType.setDataApertura(getXmlDate(paiIntervento.getDtAvvio()));
        interventoType.setDataChiusura(paiIntervento.getDtChius() == null ? null : getXmlDate(paiIntervento.getDtChius()));
        interventoType.setTipologiaIntervento(getMacroTipintCsrNew());
        interventoType.setDettaglio(getMicroTipintCsrNew());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(interventoType.getTipologiaIntervento()) && !Strings.isNullOrEmpty(interventoType.getDettaglio()), "tipologia o dettaglio tipologia intervento csr mancanti!");
        interventoType.setSpecificazioneNew(createSpecificazione());
        interventoType.setDurataPrevista(getDurataMesi());
        if (String.valueOf(paiIntervento.getStatoInt()).equals("C")) {
            interventoType.setNote(new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, paiIntervento.getNoteChius())); //uniche note che ho . . 
        } else {
            interventoType.setNote(new JAXBElement<String>(new QName(String.class.getSimpleName()), String.class, paiIntervento.getMotivazione()));
        }
        return interventoType;
    }

    public NuovoInserimentoIntervento createInserimentoInterventoRequest() {
        Preconditions.checkNotNull(paiIntervento, "paiIntervento must be not null");
        NuovoInserimentoIntervento inserimentoIntervento = new NuovoInserimentoIntervento();
        inserimentoIntervento.setVersione(VERSIONE);
        inserimentoIntervento.setIntervento(createInterventoType());
        inserimentoIntervento.setCodice(getCodiceOperatore());
        inserimentoIntervento.setDataModifica(getXmlDate(new Date()));
        inserimentoIntervento.setIdCartella(requireIdCartella());
        return inserimentoIntervento;
    }

    public NuovaModificaIntervento createModificaInterventoRequest() {
        Preconditions.checkNotNull(paiIntervento, "paiIntervento must be not null");
        NuovaModificaIntervento modificaIntervento = new NuovaModificaIntervento();
        modificaIntervento.setVersione(VERSIONE);
        modificaIntervento.setDataModifica(getXmlDate(new Date()));
        modificaIntervento.setCodice(requireCodiceOperatore());
        modificaIntervento.setIdCartella(requireIdCartella());
        modificaIntervento.setIdIntervento(requireIdIntervento());
        modificaIntervento.setIntervento(createInterventoType());
        return modificaIntervento;
    }
}
