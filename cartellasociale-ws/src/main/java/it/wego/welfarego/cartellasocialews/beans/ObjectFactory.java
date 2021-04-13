
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.wego.welfarego.cartellasocialews.beans package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DettaglioDomiciliareTypeErogatore_QNAME = new QName("http://tipigenerali.cartellasociale.sanita.insiel.it", "erogatore");
    private final static QName _DettaglioDomiciliareTypeObiettivi_QNAME = new QName("http://tipigenerali.cartellasociale.sanita.insiel.it", "obiettivi");
    private final static QName _InterventoNewTypeSubDettaglio_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "subDettaglio");
    private final static QName _InterventoNewTypeNote_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "note");
    private final static QName _ProfiloTypeDomicilioTelefono_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "telefono");
    private final static QName _ProfiloTypeDomicilioIdoneitaAbitazione_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "idoneitaAbitazione");
    private final static QName _ProfiloTypeDomicilioCondizioneAbitativa_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "condizioneAbitativa");
    private final static QName _ProfiloTypeDatiPersonaliTipoAssegno_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "tipoAssegno");
    private final static QName _ProfiloTypeDatiPersonaliAttesaAccompagnamento_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "attesaAccompagnamento");
    private final static QName _ProfiloTypeDatiPersonaliAssegnoAccompagnamento_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "assegnoAccompagnamento");
    private final static QName _SADTypeObiettivi_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "obiettivi");
    private final static QName _ProblematicheTypeMacroproblematicaNoteAltro_QNAME = new QName("http://cartellasociale.sanita.insiel.it", "noteAltro");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.wego.welfarego.cartellasocialews.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProblematicheType }
     * 
     */
    public ProblematicheType createProblematicheType() {
        return new ProblematicheType();
    }

    /**
     * Create an instance of {@link InterventoType }
     * 
     */
    public InterventoType createInterventoType() {
        return new InterventoType();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione }
     * 
     */
    public InterventoType.Specificazione createInterventoTypeSpecificazione() {
        return new InterventoType.Specificazione();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione.Economico }
     * 
     */
    public InterventoType.Specificazione.Economico createInterventoTypeSpecificazioneEconomico() {
        return new InterventoType.Specificazione.Economico();
    }

    /**
     * Create an instance of {@link InterventoNewType }
     * 
     */
    public InterventoNewType createInterventoNewType() {
        return new InterventoNewType();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew }
     * 
     */
    public InterventoNewType.SpecificazioneNew createInterventoNewTypeSpecificazioneNew() {
        return new InterventoNewType.SpecificazioneNew();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew.EconomicoNew }
     * 
     */
    public InterventoNewType.SpecificazioneNew.EconomicoNew createInterventoNewTypeSpecificazioneNewEconomicoNew() {
        return new InterventoNewType.SpecificazioneNew.EconomicoNew();
    }

    /**
     * Create an instance of {@link ProfiloType }
     * 
     */
    public ProfiloType createProfiloType() {
        return new ProfiloType();
    }

    /**
     * Create an instance of {@link AnagraficaType }
     * 
     */
    public AnagraficaType createAnagraficaType() {
        return new AnagraficaType();
    }

    /**
     * Create an instance of {@link AnagraficaType.DatiComuni }
     * 
     */
    public AnagraficaType.DatiComuni createAnagraficaTypeDatiComuni() {
        return new AnagraficaType.DatiComuni();
    }

    /**
     * Create an instance of {@link InserimentoCartellaSociale }
     * 
     */
    public InserimentoCartellaSociale createInserimentoCartellaSociale() {
        return new InserimentoCartellaSociale();
    }

    /**
     * Create an instance of {@link ProgettoType }
     * 
     */
    public ProgettoType createProgettoType() {
        return new ProgettoType();
    }

    /**
     * Create an instance of {@link RicevutaModificaAnagrafica }
     * 
     */
    public RicevutaModificaAnagrafica createRicevutaModificaAnagrafica() {
        return new RicevutaModificaAnagrafica();
    }

    /**
     * Create an instance of {@link RispostaBase }
     * 
     */
    public RispostaBase createRispostaBase() {
        return new RispostaBase();
    }

    /**
     * Create an instance of {@link Esito }
     * 
     */
    public Esito createEsito() {
        return new Esito();
    }

    /**
     * Create an instance of {@link Messaggio }
     * 
     */
    public Messaggio createMessaggio() {
        return new Messaggio();
    }

    /**
     * Create an instance of {@link NuovaModificaIntervento }
     * 
     */
    public NuovaModificaIntervento createNuovaModificaIntervento() {
        return new NuovaModificaIntervento();
    }

    /**
     * Create an instance of {@link RicevutaModificaIntervento }
     * 
     */
    public RicevutaModificaIntervento createRicevutaModificaIntervento() {
        return new RicevutaModificaIntervento();
    }

    /**
     * Create an instance of {@link RicevutaCartella }
     * 
     */
    public RicevutaCartella createRicevutaCartella() {
        return new RicevutaCartella();
    }

    /**
     * Create an instance of {@link RicevutaModificaDiario }
     * 
     */
    public RicevutaModificaDiario createRicevutaModificaDiario() {
        return new RicevutaModificaDiario();
    }

    /**
     * Create an instance of {@link AzioniBackOffice }
     * 
     */
    public AzioniBackOffice createAzioniBackOffice() {
        return new AzioniBackOffice();
    }

    /**
     * Create an instance of {@link RicevutaIntervento }
     * 
     */
    public RicevutaIntervento createRicevutaIntervento() {
        return new RicevutaIntervento();
    }

    /**
     * Create an instance of {@link RicevutaModificaProfilo }
     * 
     */
    public RicevutaModificaProfilo createRicevutaModificaProfilo() {
        return new RicevutaModificaProfilo();
    }

    /**
     * Create an instance of {@link RicevutaChiudiInterventiDaLista }
     * 
     */
    public RicevutaChiudiInterventiDaLista createRicevutaChiudiInterventiDaLista() {
        return new RicevutaChiudiInterventiDaLista();
    }

    /**
     * Create an instance of {@link RicevutaChiudiCartella }
     * 
     */
    public RicevutaChiudiCartella createRicevutaChiudiCartella() {
        return new RicevutaChiudiCartella();
    }

    /**
     * Create an instance of {@link RicevutaAzioniBackOffice }
     * 
     */
    public RicevutaAzioniBackOffice createRicevutaAzioniBackOffice() {
        return new RicevutaAzioniBackOffice();
    }

    /**
     * Create an instance of {@link ChiudiCartella }
     * 
     */
    public ChiudiCartella createChiudiCartella() {
        return new ChiudiCartella();
    }

    /**
     * Create an instance of {@link ModificaAnagrafica }
     * 
     */
    public ModificaAnagrafica createModificaAnagrafica() {
        return new ModificaAnagrafica();
    }

    /**
     * Create an instance of {@link ModificaIntervento }
     * 
     */
    public ModificaIntervento createModificaIntervento() {
        return new ModificaIntervento();
    }

    /**
     * Create an instance of {@link InserimentoPersonaRiferimento }
     * 
     */
    public InserimentoPersonaRiferimento createInserimentoPersonaRiferimento() {
        return new InserimentoPersonaRiferimento();
    }

    /**
     * Create an instance of {@link PersonaRiferimentoType }
     * 
     */
    public PersonaRiferimentoType createPersonaRiferimentoType() {
        return new PersonaRiferimentoType();
    }

    /**
     * Create an instance of {@link ModificaProfilo }
     * 
     */
    public ModificaProfilo createModificaProfilo() {
        return new ModificaProfilo();
    }

    /**
     * Create an instance of {@link InserimentoIntervento }
     * 
     */
    public InserimentoIntervento createInserimentoIntervento() {
        return new InserimentoIntervento();
    }

    /**
     * Create an instance of {@link ModificaPersonaRiferimento }
     * 
     */
    public ModificaPersonaRiferimento createModificaPersonaRiferimento() {
        return new ModificaPersonaRiferimento();
    }

    /**
     * Create an instance of {@link RicevutaPersonaRiferimento }
     * 
     */
    public RicevutaPersonaRiferimento createRicevutaPersonaRiferimento() {
        return new RicevutaPersonaRiferimento();
    }

    /**
     * Create an instance of {@link NuovoInserimentoIntervento }
     * 
     */
    public NuovoInserimentoIntervento createNuovoInserimentoIntervento() {
        return new NuovoInserimentoIntervento();
    }

    /**
     * Create an instance of {@link ModificaProgetto }
     * 
     */
    public ModificaProgetto createModificaProgetto() {
        return new ModificaProgetto();
    }

    /**
     * Create an instance of {@link RicevutaRiattivaCartella }
     * 
     */
    public RicevutaRiattivaCartella createRicevutaRiattivaCartella() {
        return new RicevutaRiattivaCartella();
    }

    /**
     * Create an instance of {@link RicevutaDiario }
     * 
     */
    public RicevutaDiario createRicevutaDiario() {
        return new RicevutaDiario();
    }

    /**
     * Create an instance of {@link RicevutaModificaProgetto }
     * 
     */
    public RicevutaModificaProgetto createRicevutaModificaProgetto() {
        return new RicevutaModificaProgetto();
    }

    /**
     * Create an instance of {@link RiattivaCartella }
     * 
     */
    public RiattivaCartella createRiattivaCartella() {
        return new RiattivaCartella();
    }

    /**
     * Create an instance of {@link InserimentoDiario }
     * 
     */
    public InserimentoDiario createInserimentoDiario() {
        return new InserimentoDiario();
    }

    /**
     * Create an instance of {@link DiarioType }
     * 
     */
    public DiarioType createDiarioType() {
        return new DiarioType();
    }

    /**
     * Create an instance of {@link ModificaDiario }
     * 
     */
    public ModificaDiario createModificaDiario() {
        return new ModificaDiario();
    }

    /**
     * Create an instance of {@link ListaMicroInterventiSADType }
     * 
     */
    public ListaMicroInterventiSADType createListaMicroInterventiSADType() {
        return new ListaMicroInterventiSADType();
    }

    /**
     * Create an instance of {@link DettaglioMacrointerventoSADType }
     * 
     */
    public DettaglioMacrointerventoSADType createDettaglioMacrointerventoSADType() {
        return new DettaglioMacrointerventoSADType();
    }

    /**
     * Create an instance of {@link SADType }
     * 
     */
    public SADType createSADType() {
        return new SADType();
    }

    /**
     * Create an instance of {@link DettaglioMicrointerventoSADType }
     * 
     */
    public DettaglioMicrointerventoSADType createDettaglioMicrointerventoSADType() {
        return new DettaglioMicrointerventoSADType();
    }

    /**
     * Create an instance of {@link MicroProblematicaType }
     * 
     */
    public MicroProblematicaType createMicroProblematicaType() {
        return new MicroProblematicaType();
    }

    /**
     * Create an instance of {@link DettaglioDomiciliareType }
     * 
     */
    public DettaglioDomiciliareType createDettaglioDomiciliareType() {
        return new DettaglioDomiciliareType();
    }

    /**
     * Create an instance of {@link RilevanzaObiettiviType }
     * 
     */
    public RilevanzaObiettiviType createRilevanzaObiettiviType() {
        return new RilevanzaObiettiviType();
    }

    /**
     * Create an instance of {@link ToponimoType }
     * 
     */
    public ToponimoType createToponimoType() {
        return new ToponimoType();
    }

    /**
     * Create an instance of {@link AnagraficaBaseType }
     * 
     */
    public AnagraficaBaseType createAnagraficaBaseType() {
        return new AnagraficaBaseType();
    }

    /**
     * Create an instance of {@link IndirizzoType }
     * 
     */
    public IndirizzoType createIndirizzoType() {
        return new IndirizzoType();
    }

    /**
     * Create an instance of {@link ComuneType }
     * 
     */
    public ComuneType createComuneType() {
        return new ComuneType();
    }

    /**
     * Create an instance of {@link IseeType }
     * 
     */
    public IseeType createIseeType() {
        return new IseeType();
    }

    /**
     * Create an instance of {@link SubDettaglioIntType }
     * 
     */
    public SubDettaglioIntType createSubDettaglioIntType() {
        return new SubDettaglioIntType();
    }

    /**
     * Create an instance of {@link NascitaType }
     * 
     */
    public NascitaType createNascitaType() {
        return new NascitaType();
    }

    /**
     * Create an instance of {@link StatoType }
     * 
     */
    public StatoType createStatoType() {
        return new StatoType();
    }

    /**
     * Create an instance of {@link AutenticazioneE }
     * 
     */
    public AutenticazioneE createAutenticazioneE() {
        return new AutenticazioneE();
    }

    /**
     * Create an instance of {@link RichiestaBase }
     * 
     */
    public RichiestaBase createRichiestaBase() {
        return new RichiestaBase();
    }

    /**
     * Create an instance of {@link Autenticazione }
     * 
     */
    public Autenticazione createAutenticazione() {
        return new Autenticazione();
    }

    /**
     * Create an instance of {@link ProblematicheType.Macroproblematica }
     * 
     */
    public ProblematicheType.Macroproblematica createProblematicheTypeMacroproblematica() {
        return new ProblematicheType.Macroproblematica();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione.Domiciliare }
     * 
     */
    public InterventoType.Specificazione.Domiciliare createInterventoTypeSpecificazioneDomiciliare() {
        return new InterventoType.Specificazione.Domiciliare();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione.Residenziale }
     * 
     */
    public InterventoType.Specificazione.Residenziale createInterventoTypeSpecificazioneResidenziale() {
        return new InterventoType.Specificazione.Residenziale();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione.Economico.Fap }
     * 
     */
    public InterventoType.Specificazione.Economico.Fap createInterventoTypeSpecificazioneEconomicoFap() {
        return new InterventoType.Specificazione.Economico.Fap();
    }

    /**
     * Create an instance of {@link InterventoType.Specificazione.Economico.FondoSolidarieta }
     * 
     */
    public InterventoType.Specificazione.Economico.FondoSolidarieta createInterventoTypeSpecificazioneEconomicoFondoSolidarieta() {
        return new InterventoType.Specificazione.Economico.FondoSolidarieta();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew.Domiciliare }
     * 
     */
    public InterventoNewType.SpecificazioneNew.Domiciliare createInterventoNewTypeSpecificazioneNewDomiciliare() {
        return new InterventoNewType.SpecificazioneNew.Domiciliare();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew.Residenziale }
     * 
     */
    public InterventoNewType.SpecificazioneNew.Residenziale createInterventoNewTypeSpecificazioneNewResidenziale() {
        return new InterventoNewType.SpecificazioneNew.Residenziale();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew }
     * 
     */
    public InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew createInterventoNewTypeSpecificazioneNewEconomicoNewFapNew() {
        return new InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew();
    }

    /**
     * Create an instance of {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta }
     * 
     */
    public InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta createInterventoNewTypeSpecificazioneNewEconomicoNewFondoSolidarieta() {
        return new InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta();
    }

    /**
     * Create an instance of {@link ProfiloType.Abilitazione }
     * 
     */
    public ProfiloType.Abilitazione createProfiloTypeAbilitazione() {
        return new ProfiloType.Abilitazione();
    }

    /**
     * Create an instance of {@link ProfiloType.DatiPersonali }
     * 
     */
    public ProfiloType.DatiPersonali createProfiloTypeDatiPersonali() {
        return new ProfiloType.DatiPersonali();
    }

    /**
     * Create an instance of {@link ProfiloType.DatiFamiliari }
     * 
     */
    public ProfiloType.DatiFamiliari createProfiloTypeDatiFamiliari() {
        return new ProfiloType.DatiFamiliari();
    }

    /**
     * Create an instance of {@link ProfiloType.DatiProfessionali }
     * 
     */
    public ProfiloType.DatiProfessionali createProfiloTypeDatiProfessionali() {
        return new ProfiloType.DatiProfessionali();
    }

    /**
     * Create an instance of {@link ProfiloType.Domicilio }
     * 
     */
    public ProfiloType.Domicilio createProfiloTypeDomicilio() {
        return new ProfiloType.Domicilio();
    }

    /**
     * Create an instance of {@link AnagraficaType.DatiComuni.Residenza }
     * 
     */
    public AnagraficaType.DatiComuni.Residenza createAnagraficaTypeDatiComuniResidenza() {
        return new AnagraficaType.DatiComuni.Residenza();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", name = "erogatore", scope = DettaglioDomiciliareType.class)
    public JAXBElement<String> createDettaglioDomiciliareTypeErogatore(String value) {
        return new JAXBElement<String>(_DettaglioDomiciliareTypeErogatore_QNAME, String.class, DettaglioDomiciliareType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", name = "obiettivi", scope = DettaglioDomiciliareType.class)
    public JAXBElement<String> createDettaglioDomiciliareTypeObiettivi(String value) {
        return new JAXBElement<String>(_DettaglioDomiciliareTypeObiettivi_QNAME, String.class, DettaglioDomiciliareType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubDettaglioIntType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "subDettaglio", scope = InterventoNewType.class)
    public JAXBElement<SubDettaglioIntType> createInterventoNewTypeSubDettaglio(SubDettaglioIntType value) {
        return new JAXBElement<SubDettaglioIntType>(_InterventoNewTypeSubDettaglio_QNAME, SubDettaglioIntType.class, InterventoNewType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "note", scope = InterventoNewType.class)
    public JAXBElement<String> createInterventoNewTypeNote(String value) {
        return new JAXBElement<String>(_InterventoNewTypeNote_QNAME, String.class, InterventoNewType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "telefono", scope = ProfiloType.Domicilio.class)
    public JAXBElement<String> createProfiloTypeDomicilioTelefono(String value) {
        return new JAXBElement<String>(_ProfiloTypeDomicilioTelefono_QNAME, String.class, ProfiloType.Domicilio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "idoneitaAbitazione", scope = ProfiloType.Domicilio.class)
    public JAXBElement<String> createProfiloTypeDomicilioIdoneitaAbitazione(String value) {
        return new JAXBElement<String>(_ProfiloTypeDomicilioIdoneitaAbitazione_QNAME, String.class, ProfiloType.Domicilio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "condizioneAbitativa", scope = ProfiloType.Domicilio.class)
    public JAXBElement<String> createProfiloTypeDomicilioCondizioneAbitativa(String value) {
        return new JAXBElement<String>(_ProfiloTypeDomicilioCondizioneAbitativa_QNAME, String.class, ProfiloType.Domicilio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "tipoAssegno", scope = ProfiloType.DatiPersonali.class)
    public JAXBElement<String> createProfiloTypeDatiPersonaliTipoAssegno(String value) {
        return new JAXBElement<String>(_ProfiloTypeDatiPersonaliTipoAssegno_QNAME, String.class, ProfiloType.DatiPersonali.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SiNoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "attesaAccompagnamento", scope = ProfiloType.DatiPersonali.class)
    public JAXBElement<SiNoType> createProfiloTypeDatiPersonaliAttesaAccompagnamento(SiNoType value) {
        return new JAXBElement<SiNoType>(_ProfiloTypeDatiPersonaliAttesaAccompagnamento_QNAME, SiNoType.class, ProfiloType.DatiPersonali.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "note", scope = ProfiloType.DatiPersonali.class)
    public JAXBElement<String> createProfiloTypeDatiPersonaliNote(String value) {
        return new JAXBElement<String>(_InterventoNewTypeNote_QNAME, String.class, ProfiloType.DatiPersonali.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SiNoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "assegnoAccompagnamento", scope = ProfiloType.DatiPersonali.class)
    public JAXBElement<SiNoType> createProfiloTypeDatiPersonaliAssegnoAccompagnamento(SiNoType value) {
        return new JAXBElement<SiNoType>(_ProfiloTypeDatiPersonaliAssegnoAccompagnamento_QNAME, SiNoType.class, ProfiloType.DatiPersonali.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "obiettivi", scope = SADType.class)
    public JAXBElement<String> createSADTypeObiettivi(String value) {
        return new JAXBElement<String>(_SADTypeObiettivi_QNAME, String.class, SADType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "noteAltro", scope = ProblematicheType.Macroproblematica.class)
    public JAXBElement<String> createProblematicheTypeMacroproblematicaNoteAltro(String value) {
        return new JAXBElement<String>(_ProblematicheTypeMacroproblematicaNoteAltro_QNAME, String.class, ProblematicheType.Macroproblematica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cartellasociale.sanita.insiel.it", name = "note", scope = InterventoType.class)
    public JAXBElement<String> createInterventoTypeNote(String value) {
        return new JAXBElement<String>(_InterventoNewTypeNote_QNAME, String.class, InterventoType.class, value);
    }

}
