
package it.trieste.comune.cartellasocialews.beans;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per interventoNewType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="interventoNewType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="dataApertura" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *         &amp;lt;element name="dataChiusura" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="tipologiaIntervento" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="dettaglio" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="subDettaglio" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SubDettaglioIntType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="durataPrevista" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="importoTotaleErogato" minOccurs="0"&amp;gt;
 *           &amp;lt;simpleType&amp;gt;
 *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *               &amp;lt;fractionDigits value="2"/&amp;gt;
 *             &amp;lt;/restriction&amp;gt;
 *           &amp;lt;/simpleType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="specificazioneNew" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;choice&amp;gt;
 *                   &amp;lt;element name="economicoNew"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;choice&amp;gt;
 *                             &amp;lt;element name="fapNew"&amp;gt;
 *                               &amp;lt;complexType&amp;gt;
 *                                 &amp;lt;complexContent&amp;gt;
 *                                   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                                     &amp;lt;sequence&amp;gt;
 *                                       &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                                       &amp;lt;element name="abitareInclusivo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
 *                                       &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="punteggioGeFi" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="punteggioHansen" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="punteggioCDRs" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="1"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="importoMensile" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="dataUVM" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *                                       &amp;lt;element name="etaDataUVM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="durataMesiUVM" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *                                       &amp;lt;element name="dataSegnalazione" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="dataDecorrenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="listaAttesa" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="punteggioListaAttesa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="indennitaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
 *                                       &amp;lt;element name="motivoChiusura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                                     &amp;lt;/sequence&amp;gt;
 *                                   &amp;lt;/restriction&amp;gt;
 *                                 &amp;lt;/complexContent&amp;gt;
 *                               &amp;lt;/complexType&amp;gt;
 *                             &amp;lt;/element&amp;gt;
 *                             &amp;lt;element name="fondoSolidarieta"&amp;gt;
 *                               &amp;lt;complexType&amp;gt;
 *                                 &amp;lt;complexContent&amp;gt;
 *                                   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                                     &amp;lt;sequence&amp;gt;
 *                                       &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="totaleErogato"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                     &amp;lt;/sequence&amp;gt;
 *                                   &amp;lt;/restriction&amp;gt;
 *                                 &amp;lt;/complexContent&amp;gt;
 *                               &amp;lt;/complexType&amp;gt;
 *                             &amp;lt;/element&amp;gt;
 *                           &amp;lt;/choice&amp;gt;
 *                         &amp;lt;/restriction&amp;gt;
 *                       &amp;lt;/complexContent&amp;gt;
 *                     &amp;lt;/complexType&amp;gt;
 *                   &amp;lt;/element&amp;gt;
 *                   &amp;lt;element name="domiciliare"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;choice&amp;gt;
 *                             &amp;lt;element name="dettaglio" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DettaglioDomiciliareType"/&amp;gt;
 *                             &amp;lt;element name="sad" type="{http://cartellasociale.sanita.insiel.it}SADType"/&amp;gt;
 *                           &amp;lt;/choice&amp;gt;
 *                         &amp;lt;/restriction&amp;gt;
 *                       &amp;lt;/complexContent&amp;gt;
 *                     &amp;lt;/complexType&amp;gt;
 *                   &amp;lt;/element&amp;gt;
 *                   &amp;lt;element name="residenziale"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;sequence&amp;gt;
 *                             &amp;lt;element name="struttura" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                             &amp;lt;element name="codice_famiglia" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                           &amp;lt;/sequence&amp;gt;
 *                         &amp;lt;/restriction&amp;gt;
 *                       &amp;lt;/complexContent&amp;gt;
 *                     &amp;lt;/complexType&amp;gt;
 *                   &amp;lt;/element&amp;gt;
 *                 &amp;lt;/choice&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interventoNewType", propOrder = {
    "dataApertura",
    "dataChiusura",
    "tipologiaIntervento",
    "dettaglio",
    "subDettaglio",
    "durataPrevista",
    "note",
    "importoTotaleErogato",
    "specificazioneNew"
})
public class InterventoNewType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataApertura;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataChiusura;
    @XmlElement(required = true, nillable = true)
    protected String tipologiaIntervento;
    @XmlElement(required = true, nillable = true)
    protected String dettaglio;
    @XmlElementRef(name = "subDettaglio", namespace = "http://cartellasociale.sanita.insiel.it", type = JAXBElement.class, required = false)
    protected JAXBElement<SubDettaglioIntType> subDettaglio;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer durataPrevista;
    @XmlElementRef(name = "note", namespace = "http://cartellasociale.sanita.insiel.it", type = JAXBElement.class, required = false)
    protected JAXBElement<String> note;
    protected BigDecimal importoTotaleErogato;
    protected InterventoNewType.SpecificazioneNew specificazioneNew;

    /**
     * Recupera il valore della proprietà dataApertura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApertura() {
        return dataApertura;
    }

    /**
     * Imposta il valore della proprietà dataApertura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApertura(XMLGregorianCalendar value) {
        this.dataApertura = value;
    }

    /**
     * Recupera il valore della proprietà dataChiusura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataChiusura() {
        return dataChiusura;
    }

    /**
     * Imposta il valore della proprietà dataChiusura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataChiusura(XMLGregorianCalendar value) {
        this.dataChiusura = value;
    }

    /**
     * Recupera il valore della proprietà tipologiaIntervento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipologiaIntervento() {
        return tipologiaIntervento;
    }

    /**
     * Imposta il valore della proprietà tipologiaIntervento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipologiaIntervento(String value) {
        this.tipologiaIntervento = value;
    }

    /**
     * Recupera il valore della proprietà dettaglio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettaglio() {
        return dettaglio;
    }

    /**
     * Imposta il valore della proprietà dettaglio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettaglio(String value) {
        this.dettaglio = value;
    }

    /**
     * Recupera il valore della proprietà subDettaglio.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SubDettaglioIntType }{@code >}
     *     
     */
    public JAXBElement<SubDettaglioIntType> getSubDettaglio() {
        return subDettaglio;
    }

    /**
     * Imposta il valore della proprietà subDettaglio.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SubDettaglioIntType }{@code >}
     *     
     */
    public void setSubDettaglio(JAXBElement<SubDettaglioIntType> value) {
        this.subDettaglio = value;
    }

    /**
     * Recupera il valore della proprietà durataPrevista.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDurataPrevista() {
        return durataPrevista;
    }

    /**
     * Imposta il valore della proprietà durataPrevista.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDurataPrevista(Integer value) {
        this.durataPrevista = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNote(JAXBElement<String> value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà importoTotaleErogato.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoTotaleErogato() {
        return importoTotaleErogato;
    }

    /**
     * Imposta il valore della proprietà importoTotaleErogato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoTotaleErogato(BigDecimal value) {
        this.importoTotaleErogato = value;
    }

    /**
     * Recupera il valore della proprietà specificazioneNew.
     * 
     * @return
     *     possible object is
     *     {@link InterventoNewType.SpecificazioneNew }
     *     
     */
    public InterventoNewType.SpecificazioneNew getSpecificazioneNew() {
        return specificazioneNew;
    }

    /**
     * Imposta il valore della proprietà specificazioneNew.
     * 
     * @param value
     *     allowed object is
     *     {@link InterventoNewType.SpecificazioneNew }
     *     
     */
    public void setSpecificazioneNew(InterventoNewType.SpecificazioneNew value) {
        this.specificazioneNew = value;
    }


    /**
     * &lt;p&gt;Classe Java per anonymous complex type.
     * 
     * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * &lt;pre&gt;
     * &amp;lt;complexType&amp;gt;
     *   &amp;lt;complexContent&amp;gt;
     *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *       &amp;lt;choice&amp;gt;
     *         &amp;lt;element name="economicoNew"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;choice&amp;gt;
     *                   &amp;lt;element name="fapNew"&amp;gt;
     *                     &amp;lt;complexType&amp;gt;
     *                       &amp;lt;complexContent&amp;gt;
     *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                           &amp;lt;sequence&amp;gt;
     *                             &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *                             &amp;lt;element name="abitareInclusivo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
     *                             &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="punteggioGeFi" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="punteggioHansen" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="punteggioCDRs" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="1"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="importoMensile" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="dataUVM" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
     *                             &amp;lt;element name="etaDataUVM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="durataMesiUVM" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
     *                             &amp;lt;element name="dataSegnalazione" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="dataDecorrenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="listaAttesa" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="punteggioListaAttesa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="indennitaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
     *                             &amp;lt;element name="motivoChiusura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                           &amp;lt;/sequence&amp;gt;
     *                         &amp;lt;/restriction&amp;gt;
     *                       &amp;lt;/complexContent&amp;gt;
     *                     &amp;lt;/complexType&amp;gt;
     *                   &amp;lt;/element&amp;gt;
     *                   &amp;lt;element name="fondoSolidarieta"&amp;gt;
     *                     &amp;lt;complexType&amp;gt;
     *                       &amp;lt;complexContent&amp;gt;
     *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                           &amp;lt;sequence&amp;gt;
     *                             &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="totaleErogato"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                           &amp;lt;/sequence&amp;gt;
     *                         &amp;lt;/restriction&amp;gt;
     *                       &amp;lt;/complexContent&amp;gt;
     *                     &amp;lt;/complexType&amp;gt;
     *                   &amp;lt;/element&amp;gt;
     *                 &amp;lt;/choice&amp;gt;
     *               &amp;lt;/restriction&amp;gt;
     *             &amp;lt;/complexContent&amp;gt;
     *           &amp;lt;/complexType&amp;gt;
     *         &amp;lt;/element&amp;gt;
     *         &amp;lt;element name="domiciliare"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;choice&amp;gt;
     *                   &amp;lt;element name="dettaglio" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DettaglioDomiciliareType"/&amp;gt;
     *                   &amp;lt;element name="sad" type="{http://cartellasociale.sanita.insiel.it}SADType"/&amp;gt;
     *                 &amp;lt;/choice&amp;gt;
     *               &amp;lt;/restriction&amp;gt;
     *             &amp;lt;/complexContent&amp;gt;
     *           &amp;lt;/complexType&amp;gt;
     *         &amp;lt;/element&amp;gt;
     *         &amp;lt;element name="residenziale"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;sequence&amp;gt;
     *                   &amp;lt;element name="struttura" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *                   &amp;lt;element name="codice_famiglia" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *                 &amp;lt;/sequence&amp;gt;
     *               &amp;lt;/restriction&amp;gt;
     *             &amp;lt;/complexContent&amp;gt;
     *           &amp;lt;/complexType&amp;gt;
     *         &amp;lt;/element&amp;gt;
     *       &amp;lt;/choice&amp;gt;
     *     &amp;lt;/restriction&amp;gt;
     *   &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "economicoNew",
        "domiciliare",
        "residenziale"
    })
    public static class SpecificazioneNew {

        protected InterventoNewType.SpecificazioneNew.EconomicoNew economicoNew;
        protected InterventoNewType.SpecificazioneNew.Domiciliare domiciliare;
        protected InterventoNewType.SpecificazioneNew.Residenziale residenziale;

        /**
         * Recupera il valore della proprietà economicoNew.
         * 
         * @return
         *     possible object is
         *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew }
         *     
         */
        public InterventoNewType.SpecificazioneNew.EconomicoNew getEconomicoNew() {
            return economicoNew;
        }

        /**
         * Imposta il valore della proprietà economicoNew.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew }
         *     
         */
        public void setEconomicoNew(InterventoNewType.SpecificazioneNew.EconomicoNew value) {
            this.economicoNew = value;
        }

        /**
         * Recupera il valore della proprietà domiciliare.
         * 
         * @return
         *     possible object is
         *     {@link InterventoNewType.SpecificazioneNew.Domiciliare }
         *     
         */
        public InterventoNewType.SpecificazioneNew.Domiciliare getDomiciliare() {
            return domiciliare;
        }

        /**
         * Imposta il valore della proprietà domiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoNewType.SpecificazioneNew.Domiciliare }
         *     
         */
        public void setDomiciliare(InterventoNewType.SpecificazioneNew.Domiciliare value) {
            this.domiciliare = value;
        }

        /**
         * Recupera il valore della proprietà residenziale.
         * 
         * @return
         *     possible object is
         *     {@link InterventoNewType.SpecificazioneNew.Residenziale }
         *     
         */
        public InterventoNewType.SpecificazioneNew.Residenziale getResidenziale() {
            return residenziale;
        }

        /**
         * Imposta il valore della proprietà residenziale.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoNewType.SpecificazioneNew.Residenziale }
         *     
         */
        public void setResidenziale(InterventoNewType.SpecificazioneNew.Residenziale value) {
            this.residenziale = value;
        }


        /**
         * &lt;p&gt;Classe Java per anonymous complex type.
         * 
         * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * &lt;pre&gt;
         * &amp;lt;complexType&amp;gt;
         *   &amp;lt;complexContent&amp;gt;
         *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *       &amp;lt;choice&amp;gt;
         *         &amp;lt;element name="dettaglio" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DettaglioDomiciliareType"/&amp;gt;
         *         &amp;lt;element name="sad" type="{http://cartellasociale.sanita.insiel.it}SADType"/&amp;gt;
         *       &amp;lt;/choice&amp;gt;
         *     &amp;lt;/restriction&amp;gt;
         *   &amp;lt;/complexContent&amp;gt;
         * &amp;lt;/complexType&amp;gt;
         * &lt;/pre&gt;
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "dettaglio",
            "sad"
        })
        public static class Domiciliare {

            protected DettaglioDomiciliareType dettaglio;
            protected SADType sad;

            /**
             * Recupera il valore della proprietà dettaglio.
             * 
             * @return
             *     possible object is
             *     {@link DettaglioDomiciliareType }
             *     
             */
            public DettaglioDomiciliareType getDettaglio() {
                return dettaglio;
            }

            /**
             * Imposta il valore della proprietà dettaglio.
             * 
             * @param value
             *     allowed object is
             *     {@link DettaglioDomiciliareType }
             *     
             */
            public void setDettaglio(DettaglioDomiciliareType value) {
                this.dettaglio = value;
            }

            /**
             * Recupera il valore della proprietà sad.
             * 
             * @return
             *     possible object is
             *     {@link SADType }
             *     
             */
            public SADType getSad() {
                return sad;
            }

            /**
             * Imposta il valore della proprietà sad.
             * 
             * @param value
             *     allowed object is
             *     {@link SADType }
             *     
             */
            public void setSad(SADType value) {
                this.sad = value;
            }

        }


        /**
         * &lt;p&gt;Classe Java per anonymous complex type.
         * 
         * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * &lt;pre&gt;
         * &amp;lt;complexType&amp;gt;
         *   &amp;lt;complexContent&amp;gt;
         *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *       &amp;lt;choice&amp;gt;
         *         &amp;lt;element name="fapNew"&amp;gt;
         *           &amp;lt;complexType&amp;gt;
         *             &amp;lt;complexContent&amp;gt;
         *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *                 &amp;lt;sequence&amp;gt;
         *                   &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
         *                   &amp;lt;element name="abitareInclusivo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
         *                   &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="punteggioGeFi" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="punteggioHansen" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="punteggioCDRs" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="1"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="importoMensile" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="dataUVM" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
         *                   &amp;lt;element name="etaDataUVM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="durataMesiUVM" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
         *                   &amp;lt;element name="dataSegnalazione" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="dataDecorrenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="listaAttesa" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="punteggioListaAttesa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="indennitaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
         *                   &amp;lt;element name="motivoChiusura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *                 &amp;lt;/sequence&amp;gt;
         *               &amp;lt;/restriction&amp;gt;
         *             &amp;lt;/complexContent&amp;gt;
         *           &amp;lt;/complexType&amp;gt;
         *         &amp;lt;/element&amp;gt;
         *         &amp;lt;element name="fondoSolidarieta"&amp;gt;
         *           &amp;lt;complexType&amp;gt;
         *             &amp;lt;complexContent&amp;gt;
         *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *                 &amp;lt;sequence&amp;gt;
         *                   &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="totaleErogato"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                 &amp;lt;/sequence&amp;gt;
         *               &amp;lt;/restriction&amp;gt;
         *             &amp;lt;/complexContent&amp;gt;
         *           &amp;lt;/complexType&amp;gt;
         *         &amp;lt;/element&amp;gt;
         *       &amp;lt;/choice&amp;gt;
         *     &amp;lt;/restriction&amp;gt;
         *   &amp;lt;/complexContent&amp;gt;
         * &amp;lt;/complexType&amp;gt;
         * &lt;/pre&gt;
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "fapNew",
            "fondoSolidarieta"
        })
        public static class EconomicoNew {

            protected InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew fapNew;
            protected InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta fondoSolidarieta;

            /**
             * Recupera il valore della proprietà fapNew.
             * 
             * @return
             *     possible object is
             *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew }
             *     
             */
            public InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew getFapNew() {
                return fapNew;
            }

            /**
             * Imposta il valore della proprietà fapNew.
             * 
             * @param value
             *     allowed object is
             *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew }
             *     
             */
            public void setFapNew(InterventoNewType.SpecificazioneNew.EconomicoNew.FapNew value) {
                this.fapNew = value;
            }

            /**
             * Recupera il valore della proprietà fondoSolidarieta.
             * 
             * @return
             *     possible object is
             *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta }
             *     
             */
            public InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta getFondoSolidarieta() {
                return fondoSolidarieta;
            }

            /**
             * Imposta il valore della proprietà fondoSolidarieta.
             * 
             * @param value
             *     allowed object is
             *     {@link InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta }
             *     
             */
            public void setFondoSolidarieta(InterventoNewType.SpecificazioneNew.EconomicoNew.FondoSolidarieta value) {
                this.fondoSolidarieta = value;
            }


            /**
             * &lt;p&gt;Classe Java per anonymous complex type.
             * 
             * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * &lt;pre&gt;
             * &amp;lt;complexType&amp;gt;
             *   &amp;lt;complexContent&amp;gt;
             *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
             *       &amp;lt;sequence&amp;gt;
             *         &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
             *         &amp;lt;element name="abitareInclusivo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
             *         &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="punteggioGeFi" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="punteggioHansen" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="punteggioCDRs" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="1"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="importoMensile" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="dataUVM" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
             *         &amp;lt;element name="etaDataUVM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="durataMesiUVM" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
             *         &amp;lt;element name="dataSegnalazione" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="dataDecorrenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="listaAttesa" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="punteggioListaAttesa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="indennitaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
             *         &amp;lt;element name="motivoChiusura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
             *       &amp;lt;/sequence&amp;gt;
             *     &amp;lt;/restriction&amp;gt;
             *   &amp;lt;/complexContent&amp;gt;
             * &amp;lt;/complexType&amp;gt;
             * &lt;/pre&gt;
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "tipologiaInterventoFap",
                "abitareInclusivo",
                "punteggioKatz",
                "punteggioGeFi",
                "punteggioHansen",
                "punteggioCDRs",
                "importoMensile",
                "isee",
                "dataUVM",
                "etaDataUVM",
                "durataMesiUVM",
                "dataSegnalazione",
                "dataDecorrenza",
                "listaAttesa",
                "punteggioListaAttesa",
                "nOreContratto",
                "contestualePresenzaAddetti",
                "indennitaAccompagnamento",
                "motivoChiusura"
            })
            public static class FapNew {

                @XmlElement(required = true)
                protected String tipologiaInterventoFap;
                @XmlElement(required = true)
                @XmlSchemaType(name = "string")
                protected SiNoType abitareInclusivo;
                protected Integer punteggioKatz;
                protected Integer punteggioGeFi;
                protected Integer punteggioHansen;
                protected BigDecimal punteggioCDRs;
                protected BigDecimal importoMensile;
                protected IseeType isee;
                @XmlElement(required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dataUVM;
                protected Integer etaDataUVM;
                protected int durataMesiUVM;
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dataSegnalazione;
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dataDecorrenza;
                @XmlSchemaType(name = "string")
                protected SiNoType listaAttesa;
                protected Integer punteggioListaAttesa;
                protected Integer nOreContratto;
                protected String contestualePresenzaAddetti;
                @XmlElement(required = true)
                @XmlSchemaType(name = "string")
                protected SiNoType indennitaAccompagnamento;
                protected String motivoChiusura;

                /**
                 * Recupera il valore della proprietà tipologiaInterventoFap.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTipologiaInterventoFap() {
                    return tipologiaInterventoFap;
                }

                /**
                 * Imposta il valore della proprietà tipologiaInterventoFap.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTipologiaInterventoFap(String value) {
                    this.tipologiaInterventoFap = value;
                }

                /**
                 * Recupera il valore della proprietà abitareInclusivo.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getAbitareInclusivo() {
                    return abitareInclusivo;
                }

                /**
                 * Imposta il valore della proprietà abitareInclusivo.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setAbitareInclusivo(SiNoType value) {
                    this.abitareInclusivo = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioKatz.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getPunteggioKatz() {
                    return punteggioKatz;
                }

                /**
                 * Imposta il valore della proprietà punteggioKatz.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setPunteggioKatz(Integer value) {
                    this.punteggioKatz = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioGeFi.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getPunteggioGeFi() {
                    return punteggioGeFi;
                }

                /**
                 * Imposta il valore della proprietà punteggioGeFi.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setPunteggioGeFi(Integer value) {
                    this.punteggioGeFi = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioHansen.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getPunteggioHansen() {
                    return punteggioHansen;
                }

                /**
                 * Imposta il valore della proprietà punteggioHansen.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setPunteggioHansen(Integer value) {
                    this.punteggioHansen = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioCDRs.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getPunteggioCDRs() {
                    return punteggioCDRs;
                }

                /**
                 * Imposta il valore della proprietà punteggioCDRs.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setPunteggioCDRs(BigDecimal value) {
                    this.punteggioCDRs = value;
                }

                /**
                 * Recupera il valore della proprietà importoMensile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getImportoMensile() {
                    return importoMensile;
                }

                /**
                 * Imposta il valore della proprietà importoMensile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setImportoMensile(BigDecimal value) {
                    this.importoMensile = value;
                }

                /**
                 * Recupera il valore della proprietà isee.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IseeType }
                 *     
                 */
                public IseeType getIsee() {
                    return isee;
                }

                /**
                 * Imposta il valore della proprietà isee.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IseeType }
                 *     
                 */
                public void setIsee(IseeType value) {
                    this.isee = value;
                }

                /**
                 * Recupera il valore della proprietà dataUVM.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDataUVM() {
                    return dataUVM;
                }

                /**
                 * Imposta il valore della proprietà dataUVM.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDataUVM(XMLGregorianCalendar value) {
                    this.dataUVM = value;
                }

                /**
                 * Recupera il valore della proprietà etaDataUVM.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getEtaDataUVM() {
                    return etaDataUVM;
                }

                /**
                 * Imposta il valore della proprietà etaDataUVM.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setEtaDataUVM(Integer value) {
                    this.etaDataUVM = value;
                }

                /**
                 * Recupera il valore della proprietà durataMesiUVM.
                 * 
                 */
                public int getDurataMesiUVM() {
                    return durataMesiUVM;
                }

                /**
                 * Imposta il valore della proprietà durataMesiUVM.
                 * 
                 */
                public void setDurataMesiUVM(int value) {
                    this.durataMesiUVM = value;
                }

                /**
                 * Recupera il valore della proprietà dataSegnalazione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDataSegnalazione() {
                    return dataSegnalazione;
                }

                /**
                 * Imposta il valore della proprietà dataSegnalazione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDataSegnalazione(XMLGregorianCalendar value) {
                    this.dataSegnalazione = value;
                }

                /**
                 * Recupera il valore della proprietà dataDecorrenza.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDataDecorrenza() {
                    return dataDecorrenza;
                }

                /**
                 * Imposta il valore della proprietà dataDecorrenza.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDataDecorrenza(XMLGregorianCalendar value) {
                    this.dataDecorrenza = value;
                }

                /**
                 * Recupera il valore della proprietà listaAttesa.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getListaAttesa() {
                    return listaAttesa;
                }

                /**
                 * Imposta il valore della proprietà listaAttesa.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setListaAttesa(SiNoType value) {
                    this.listaAttesa = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioListaAttesa.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getPunteggioListaAttesa() {
                    return punteggioListaAttesa;
                }

                /**
                 * Imposta il valore della proprietà punteggioListaAttesa.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setPunteggioListaAttesa(Integer value) {
                    this.punteggioListaAttesa = value;
                }

                /**
                 * Recupera il valore della proprietà nOreContratto.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getNOreContratto() {
                    return nOreContratto;
                }

                /**
                 * Imposta il valore della proprietà nOreContratto.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setNOreContratto(Integer value) {
                    this.nOreContratto = value;
                }

                /**
                 * Recupera il valore della proprietà contestualePresenzaAddetti.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getContestualePresenzaAddetti() {
                    return contestualePresenzaAddetti;
                }

                /**
                 * Imposta il valore della proprietà contestualePresenzaAddetti.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setContestualePresenzaAddetti(String value) {
                    this.contestualePresenzaAddetti = value;
                }

                /**
                 * Recupera il valore della proprietà indennitaAccompagnamento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getIndennitaAccompagnamento() {
                    return indennitaAccompagnamento;
                }

                /**
                 * Imposta il valore della proprietà indennitaAccompagnamento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setIndennitaAccompagnamento(SiNoType value) {
                    this.indennitaAccompagnamento = value;
                }

                /**
                 * Recupera il valore della proprietà motivoChiusura.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getMotivoChiusura() {
                    return motivoChiusura;
                }

                /**
                 * Imposta il valore della proprietà motivoChiusura.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setMotivoChiusura(String value) {
                    this.motivoChiusura = value;
                }

            }


            /**
             * &lt;p&gt;Classe Java per anonymous complex type.
             * 
             * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * &lt;pre&gt;
             * &amp;lt;complexType&amp;gt;
             *   &amp;lt;complexContent&amp;gt;
             *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
             *       &amp;lt;sequence&amp;gt;
             *         &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="totaleErogato"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *       &amp;lt;/sequence&amp;gt;
             *     &amp;lt;/restriction&amp;gt;
             *   &amp;lt;/complexContent&amp;gt;
             * &amp;lt;/complexType&amp;gt;
             * &lt;/pre&gt;
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "isee",
                "totaleErogato"
            })
            public static class FondoSolidarieta {

                protected IseeType isee;
                @XmlElement(required = true)
                protected BigDecimal totaleErogato;

                /**
                 * Recupera il valore della proprietà isee.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IseeType }
                 *     
                 */
                public IseeType getIsee() {
                    return isee;
                }

                /**
                 * Imposta il valore della proprietà isee.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IseeType }
                 *     
                 */
                public void setIsee(IseeType value) {
                    this.isee = value;
                }

                /**
                 * Recupera il valore della proprietà totaleErogato.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getTotaleErogato() {
                    return totaleErogato;
                }

                /**
                 * Imposta il valore della proprietà totaleErogato.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setTotaleErogato(BigDecimal value) {
                    this.totaleErogato = value;
                }

            }

        }


        /**
         * &lt;p&gt;Classe Java per anonymous complex type.
         * 
         * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * &lt;pre&gt;
         * &amp;lt;complexType&amp;gt;
         *   &amp;lt;complexContent&amp;gt;
         *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *       &amp;lt;sequence&amp;gt;
         *         &amp;lt;element name="struttura" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
         *         &amp;lt;element name="codice_famiglia" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
         *       &amp;lt;/sequence&amp;gt;
         *     &amp;lt;/restriction&amp;gt;
         *   &amp;lt;/complexContent&amp;gt;
         * &amp;lt;/complexType&amp;gt;
         * &lt;/pre&gt;
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "struttura",
            "codiceFamiglia"
        })
        public static class Residenziale {

            @XmlElement(required = true, nillable = true)
            protected String struttura;
            @XmlElement(name = "codice_famiglia", required = true, nillable = true)
            protected String codiceFamiglia;

            /**
             * Recupera il valore della proprietà struttura.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStruttura() {
                return struttura;
            }

            /**
             * Imposta il valore della proprietà struttura.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStruttura(String value) {
                this.struttura = value;
            }

            /**
             * Recupera il valore della proprietà codiceFamiglia.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodiceFamiglia() {
                return codiceFamiglia;
            }

            /**
             * Imposta il valore della proprietà codiceFamiglia.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodiceFamiglia(String value) {
                this.codiceFamiglia = value;
            }

        }

    }

}
