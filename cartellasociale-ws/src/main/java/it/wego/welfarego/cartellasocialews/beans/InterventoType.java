
package it.wego.welfarego.cartellasocialews.beans;

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
 * &lt;p&gt;Classe Java per interventoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="interventoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="dataApertura" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *         &amp;lt;element name="dataChiusura" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="tipologiaIntervento" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="dettaglio" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="durataPrevista" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="specificazione" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;choice&amp;gt;
 *                   &amp;lt;element name="economico"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;choice&amp;gt;
 *                             &amp;lt;element name="fap"&amp;gt;
 *                               &amp;lt;complexType&amp;gt;
 *                                 &amp;lt;complexContent&amp;gt;
 *                                   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                                     &amp;lt;sequence&amp;gt;
 *                                       &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                                       &amp;lt;element name="dettaglioTipoIntervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
 *                                       &amp;lt;element name="disabilitaSensoriale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
 *                                       &amp;lt;element name="assegnoMensAutonomAPA" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="contributoMensAiutoFam" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="sostegnoMensVitaIndip" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="sostegnoMensProgSaluteMentale" minOccurs="0"&amp;gt;
 *                                         &amp;lt;simpleType&amp;gt;
 *                                           &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
 *                                             &amp;lt;fractionDigits value="2"/&amp;gt;
 *                                           &amp;lt;/restriction&amp;gt;
 *                                         &amp;lt;/simpleType&amp;gt;
 *                                       &amp;lt;/element&amp;gt;
 *                                       &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="dataUVD" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *                                       &amp;lt;element name="durataMesiUVD" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *                                       &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                                       &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
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
@XmlType(name = "interventoType", propOrder = {
    "dataApertura",
    "dataChiusura",
    "tipologiaIntervento",
    "dettaglio",
    "durataPrevista",
    "note",
    "specificazione"
})
public class InterventoType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataApertura;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataChiusura;
    @XmlElement(required = true, nillable = true)
    protected String tipologiaIntervento;
    @XmlElement(required = true, nillable = true)
    protected String dettaglio;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer durataPrevista;
    @XmlElementRef(name = "note", namespace = "http://cartellasociale.sanita.insiel.it", type = JAXBElement.class, required = false)
    protected JAXBElement<String> note;
    protected InterventoType.Specificazione specificazione;

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
     * Recupera il valore della proprietà specificazione.
     * 
     * @return
     *     possible object is
     *     {@link InterventoType.Specificazione }
     *     
     */
    public InterventoType.Specificazione getSpecificazione() {
        return specificazione;
    }

    /**
     * Imposta il valore della proprietà specificazione.
     * 
     * @param value
     *     allowed object is
     *     {@link InterventoType.Specificazione }
     *     
     */
    public void setSpecificazione(InterventoType.Specificazione value) {
        this.specificazione = value;
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
     *         &amp;lt;element name="economico"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;choice&amp;gt;
     *                   &amp;lt;element name="fap"&amp;gt;
     *                     &amp;lt;complexType&amp;gt;
     *                       &amp;lt;complexContent&amp;gt;
     *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                           &amp;lt;sequence&amp;gt;
     *                             &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *                             &amp;lt;element name="dettaglioTipoIntervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
     *                             &amp;lt;element name="disabilitaSensoriale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
     *                             &amp;lt;element name="assegnoMensAutonomAPA" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="contributoMensAiutoFam" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="sostegnoMensVitaIndip" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="sostegnoMensProgSaluteMentale" minOccurs="0"&amp;gt;
     *                               &amp;lt;simpleType&amp;gt;
     *                                 &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
     *                                   &amp;lt;fractionDigits value="2"/&amp;gt;
     *                                 &amp;lt;/restriction&amp;gt;
     *                               &amp;lt;/simpleType&amp;gt;
     *                             &amp;lt;/element&amp;gt;
     *                             &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="dataUVD" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
     *                             &amp;lt;element name="durataMesiUVD" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
     *                             &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                             &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
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
        "economico",
        "domiciliare",
        "residenziale"
    })
    public static class Specificazione {

        protected InterventoType.Specificazione.Economico economico;
        protected InterventoType.Specificazione.Domiciliare domiciliare;
        protected InterventoType.Specificazione.Residenziale residenziale;

        /**
         * Recupera il valore della proprietà economico.
         * 
         * @return
         *     possible object is
         *     {@link InterventoType.Specificazione.Economico }
         *     
         */
        public InterventoType.Specificazione.Economico getEconomico() {
            return economico;
        }

        /**
         * Imposta il valore della proprietà economico.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoType.Specificazione.Economico }
         *     
         */
        public void setEconomico(InterventoType.Specificazione.Economico value) {
            this.economico = value;
        }

        /**
         * Recupera il valore della proprietà domiciliare.
         * 
         * @return
         *     possible object is
         *     {@link InterventoType.Specificazione.Domiciliare }
         *     
         */
        public InterventoType.Specificazione.Domiciliare getDomiciliare() {
            return domiciliare;
        }

        /**
         * Imposta il valore della proprietà domiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoType.Specificazione.Domiciliare }
         *     
         */
        public void setDomiciliare(InterventoType.Specificazione.Domiciliare value) {
            this.domiciliare = value;
        }

        /**
         * Recupera il valore della proprietà residenziale.
         * 
         * @return
         *     possible object is
         *     {@link InterventoType.Specificazione.Residenziale }
         *     
         */
        public InterventoType.Specificazione.Residenziale getResidenziale() {
            return residenziale;
        }

        /**
         * Imposta il valore della proprietà residenziale.
         * 
         * @param value
         *     allowed object is
         *     {@link InterventoType.Specificazione.Residenziale }
         *     
         */
        public void setResidenziale(InterventoType.Specificazione.Residenziale value) {
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
         *         &amp;lt;element name="fap"&amp;gt;
         *           &amp;lt;complexType&amp;gt;
         *             &amp;lt;complexContent&amp;gt;
         *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *                 &amp;lt;sequence&amp;gt;
         *                   &amp;lt;element name="tipologiaInterventoFap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
         *                   &amp;lt;element name="dettaglioTipoIntervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
         *                   &amp;lt;element name="disabilitaSensoriale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
         *                   &amp;lt;element name="assegnoMensAutonomAPA" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="contributoMensAiutoFam" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="sostegnoMensVitaIndip" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="sostegnoMensProgSaluteMentale" minOccurs="0"&amp;gt;
         *                     &amp;lt;simpleType&amp;gt;
         *                       &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
         *                         &amp;lt;fractionDigits value="2"/&amp;gt;
         *                       &amp;lt;/restriction&amp;gt;
         *                     &amp;lt;/simpleType&amp;gt;
         *                   &amp;lt;/element&amp;gt;
         *                   &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="dataUVD" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
         *                   &amp;lt;element name="durataMesiUVD" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
         *                   &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *                   &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
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
            "fap",
            "fondoSolidarieta"
        })
        public static class Economico {

            protected InterventoType.Specificazione.Economico.Fap fap;
            protected InterventoType.Specificazione.Economico.FondoSolidarieta fondoSolidarieta;

            /**
             * Recupera il valore della proprietà fap.
             * 
             * @return
             *     possible object is
             *     {@link InterventoType.Specificazione.Economico.Fap }
             *     
             */
            public InterventoType.Specificazione.Economico.Fap getFap() {
                return fap;
            }

            /**
             * Imposta il valore della proprietà fap.
             * 
             * @param value
             *     allowed object is
             *     {@link InterventoType.Specificazione.Economico.Fap }
             *     
             */
            public void setFap(InterventoType.Specificazione.Economico.Fap value) {
                this.fap = value;
            }

            /**
             * Recupera il valore della proprietà fondoSolidarieta.
             * 
             * @return
             *     possible object is
             *     {@link InterventoType.Specificazione.Economico.FondoSolidarieta }
             *     
             */
            public InterventoType.Specificazione.Economico.FondoSolidarieta getFondoSolidarieta() {
                return fondoSolidarieta;
            }

            /**
             * Imposta il valore della proprietà fondoSolidarieta.
             * 
             * @param value
             *     allowed object is
             *     {@link InterventoType.Specificazione.Economico.FondoSolidarieta }
             *     
             */
            public void setFondoSolidarieta(InterventoType.Specificazione.Economico.FondoSolidarieta value) {
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
             *         &amp;lt;element name="dettaglioTipoIntervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="punteggioKatz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
             *         &amp;lt;element name="disabilitaSensoriale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
             *         &amp;lt;element name="assegnoMensAutonomAPA" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="contributoMensAiutoFam" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="sostegnoMensVitaIndip" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="sostegnoMensProgSaluteMentale" minOccurs="0"&amp;gt;
             *           &amp;lt;simpleType&amp;gt;
             *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&amp;gt;
             *               &amp;lt;fractionDigits value="2"/&amp;gt;
             *             &amp;lt;/restriction&amp;gt;
             *           &amp;lt;/simpleType&amp;gt;
             *         &amp;lt;/element&amp;gt;
             *         &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="dataUVD" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
             *         &amp;lt;element name="durataMesiUVD" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
             *         &amp;lt;element name="nOreContratto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="contestualePresenzaAddetti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
             *         &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
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
                "dettaglioTipoIntervento",
                "punteggioKatz",
                "demenzaCertificata",
                "disabilitaSensoriale",
                "assegnoMensAutonomAPA",
                "contributoMensAiutoFam",
                "sostegnoMensVitaIndip",
                "sostegnoMensProgSaluteMentale",
                "isee",
                "dataUVD",
                "durataMesiUVD",
                "nOreContratto",
                "contestualePresenzaAddetti",
                "assegnoAccompagnamento",
                "motivoChiusura"
            })
            public static class Fap {

                @XmlElement(required = true)
                protected String tipologiaInterventoFap;
                protected String dettaglioTipoIntervento;
                protected String punteggioKatz;
                @XmlElement(required = true)
                @XmlSchemaType(name = "string")
                protected SiNoType demenzaCertificata;
                @XmlElement(required = true)
                @XmlSchemaType(name = "string")
                protected SiNoType disabilitaSensoriale;
                protected BigDecimal assegnoMensAutonomAPA;
                protected BigDecimal contributoMensAiutoFam;
                protected BigDecimal sostegnoMensVitaIndip;
                protected BigDecimal sostegnoMensProgSaluteMentale;
                protected IseeType isee;
                @XmlElement(required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dataUVD;
                protected int durataMesiUVD;
                protected Integer nOreContratto;
                protected String contestualePresenzaAddetti;
                @XmlElement(required = true)
                @XmlSchemaType(name = "string")
                protected SiNoType assegnoAccompagnamento;
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
                 * Recupera il valore della proprietà dettaglioTipoIntervento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDettaglioTipoIntervento() {
                    return dettaglioTipoIntervento;
                }

                /**
                 * Imposta il valore della proprietà dettaglioTipoIntervento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDettaglioTipoIntervento(String value) {
                    this.dettaglioTipoIntervento = value;
                }

                /**
                 * Recupera il valore della proprietà punteggioKatz.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPunteggioKatz() {
                    return punteggioKatz;
                }

                /**
                 * Imposta il valore della proprietà punteggioKatz.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPunteggioKatz(String value) {
                    this.punteggioKatz = value;
                }

                /**
                 * Recupera il valore della proprietà demenzaCertificata.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getDemenzaCertificata() {
                    return demenzaCertificata;
                }

                /**
                 * Imposta il valore della proprietà demenzaCertificata.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setDemenzaCertificata(SiNoType value) {
                    this.demenzaCertificata = value;
                }

                /**
                 * Recupera il valore della proprietà disabilitaSensoriale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getDisabilitaSensoriale() {
                    return disabilitaSensoriale;
                }

                /**
                 * Imposta il valore della proprietà disabilitaSensoriale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setDisabilitaSensoriale(SiNoType value) {
                    this.disabilitaSensoriale = value;
                }

                /**
                 * Recupera il valore della proprietà assegnoMensAutonomAPA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getAssegnoMensAutonomAPA() {
                    return assegnoMensAutonomAPA;
                }

                /**
                 * Imposta il valore della proprietà assegnoMensAutonomAPA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setAssegnoMensAutonomAPA(BigDecimal value) {
                    this.assegnoMensAutonomAPA = value;
                }

                /**
                 * Recupera il valore della proprietà contributoMensAiutoFam.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getContributoMensAiutoFam() {
                    return contributoMensAiutoFam;
                }

                /**
                 * Imposta il valore della proprietà contributoMensAiutoFam.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setContributoMensAiutoFam(BigDecimal value) {
                    this.contributoMensAiutoFam = value;
                }

                /**
                 * Recupera il valore della proprietà sostegnoMensVitaIndip.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getSostegnoMensVitaIndip() {
                    return sostegnoMensVitaIndip;
                }

                /**
                 * Imposta il valore della proprietà sostegnoMensVitaIndip.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setSostegnoMensVitaIndip(BigDecimal value) {
                    this.sostegnoMensVitaIndip = value;
                }

                /**
                 * Recupera il valore della proprietà sostegnoMensProgSaluteMentale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getSostegnoMensProgSaluteMentale() {
                    return sostegnoMensProgSaluteMentale;
                }

                /**
                 * Imposta il valore della proprietà sostegnoMensProgSaluteMentale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setSostegnoMensProgSaluteMentale(BigDecimal value) {
                    this.sostegnoMensProgSaluteMentale = value;
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
                 * Recupera il valore della proprietà dataUVD.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDataUVD() {
                    return dataUVD;
                }

                /**
                 * Imposta il valore della proprietà dataUVD.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDataUVD(XMLGregorianCalendar value) {
                    this.dataUVD = value;
                }

                /**
                 * Recupera il valore della proprietà durataMesiUVD.
                 * 
                 */
                public int getDurataMesiUVD() {
                    return durataMesiUVD;
                }

                /**
                 * Imposta il valore della proprietà durataMesiUVD.
                 * 
                 */
                public void setDurataMesiUVD(int value) {
                    this.durataMesiUVD = value;
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
                 * Recupera il valore della proprietà assegnoAccompagnamento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SiNoType }
                 *     
                 */
                public SiNoType getAssegnoAccompagnamento() {
                    return assegnoAccompagnamento;
                }

                /**
                 * Imposta il valore della proprietà assegnoAccompagnamento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SiNoType }
                 *     
                 */
                public void setAssegnoAccompagnamento(SiNoType value) {
                    this.assegnoAccompagnamento = value;
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
