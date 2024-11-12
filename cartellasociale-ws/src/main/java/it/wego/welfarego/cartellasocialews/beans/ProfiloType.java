
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per profiloType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="profiloType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="abilitazione"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="dataPresaInCarico" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="datiPersonali"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="isee" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IseeType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="certificatoL104" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="disabilitaSensoriali" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="statoInvalidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="attesaInvalidita" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="attesaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="nonAutosufficiente" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="nonAutosufficienzaTemporanea" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="disabileGravissimo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="patologiaOncologica" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="faseTerminale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="provvedimentoGiudiziario" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="tipoAssegno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="msna" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="datiFamiliari"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="statoCivile" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="nucleoFamiliare" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="numerositaNucleoFam" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="datiProfessionali"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="titoloStudio" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="condizioneProfessionale" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="domicilio" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="toponimo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}ToponimoType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="condizioneAbitativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="idoneitaAbitazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="struttura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="dataModifica" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "profiloType", propOrder = {
    "abilitazione",
    "datiPersonali",
    "datiFamiliari",
    "datiProfessionali",
    "domicilio",
    "dataModifica"
})
public class ProfiloType {

    @XmlElement(required = true)
    protected ProfiloType.Abilitazione abilitazione;
    @XmlElement(required = true)
    protected ProfiloType.DatiPersonali datiPersonali;
    @XmlElement(required = true)
    protected ProfiloType.DatiFamiliari datiFamiliari;
    @XmlElement(required = true)
    protected ProfiloType.DatiProfessionali datiProfessionali;
    protected ProfiloType.Domicilio domicilio;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataModifica;

    /**
     * Recupera il valore della proprietà abilitazione.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType.Abilitazione }
     *     
     */
    public ProfiloType.Abilitazione getAbilitazione() {
        return abilitazione;
    }

    /**
     * Imposta il valore della proprietà abilitazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType.Abilitazione }
     *     
     */
    public void setAbilitazione(ProfiloType.Abilitazione value) {
        this.abilitazione = value;
    }

    /**
     * Recupera il valore della proprietà datiPersonali.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType.DatiPersonali }
     *     
     */
    public ProfiloType.DatiPersonali getDatiPersonali() {
        return datiPersonali;
    }

    /**
     * Imposta il valore della proprietà datiPersonali.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType.DatiPersonali }
     *     
     */
    public void setDatiPersonali(ProfiloType.DatiPersonali value) {
        this.datiPersonali = value;
    }

    /**
     * Recupera il valore della proprietà datiFamiliari.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType.DatiFamiliari }
     *     
     */
    public ProfiloType.DatiFamiliari getDatiFamiliari() {
        return datiFamiliari;
    }

    /**
     * Imposta il valore della proprietà datiFamiliari.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType.DatiFamiliari }
     *     
     */
    public void setDatiFamiliari(ProfiloType.DatiFamiliari value) {
        this.datiFamiliari = value;
    }

    /**
     * Recupera il valore della proprietà datiProfessionali.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType.DatiProfessionali }
     *     
     */
    public ProfiloType.DatiProfessionali getDatiProfessionali() {
        return datiProfessionali;
    }

    /**
     * Imposta il valore della proprietà datiProfessionali.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType.DatiProfessionali }
     *     
     */
    public void setDatiProfessionali(ProfiloType.DatiProfessionali value) {
        this.datiProfessionali = value;
    }

    /**
     * Recupera il valore della proprietà domicilio.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType.Domicilio }
     *     
     */
    public ProfiloType.Domicilio getDomicilio() {
        return domicilio;
    }

    /**
     * Imposta il valore della proprietà domicilio.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType.Domicilio }
     *     
     */
    public void setDomicilio(ProfiloType.Domicilio value) {
        this.domicilio = value;
    }

    /**
     * Recupera il valore della proprietà dataModifica.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataModifica() {
        return dataModifica;
    }

    /**
     * Imposta il valore della proprietà dataModifica.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataModifica(XMLGregorianCalendar value) {
        this.dataModifica = value;
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
     *         &amp;lt;element name="dataPresaInCarico" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
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
        "dataPresaInCarico"
    })
    public static class Abilitazione {

        @XmlElement(required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataPresaInCarico;

        /**
         * Recupera il valore della proprietà dataPresaInCarico.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataPresaInCarico() {
            return dataPresaInCarico;
        }

        /**
         * Imposta il valore della proprietà dataPresaInCarico.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataPresaInCarico(XMLGregorianCalendar value) {
            this.dataPresaInCarico = value;
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
     *         &amp;lt;element name="statoCivile" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="nucleoFamiliare" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="numerositaNucleoFam" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
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
        "statoCivile",
        "nucleoFamiliare",
        "numerositaNucleoFam"
    })
    public static class DatiFamiliari {

        @XmlElement(required = true)
        protected String statoCivile;
        @XmlElement(required = true)
        protected String nucleoFamiliare;
        protected int numerositaNucleoFam;

        /**
         * Recupera il valore della proprietà statoCivile.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatoCivile() {
            return statoCivile;
        }

        /**
         * Imposta il valore della proprietà statoCivile.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatoCivile(String value) {
            this.statoCivile = value;
        }

        /**
         * Recupera il valore della proprietà nucleoFamiliare.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNucleoFamiliare() {
            return nucleoFamiliare;
        }

        /**
         * Imposta il valore della proprietà nucleoFamiliare.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNucleoFamiliare(String value) {
            this.nucleoFamiliare = value;
        }

        /**
         * Recupera il valore della proprietà numerositaNucleoFam.
         * 
         */
        public int getNumerositaNucleoFam() {
            return numerositaNucleoFam;
        }

        /**
         * Imposta il valore della proprietà numerositaNucleoFam.
         * 
         */
        public void setNumerositaNucleoFam(int value) {
            this.numerositaNucleoFam = value;
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
     *         &amp;lt;element name="certificatoL104" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="demenzaCertificata" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="disabilitaSensoriali" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="statoInvalidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="attesaInvalidita" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="assegnoAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="attesaAccompagnamento" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="nonAutosufficiente" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="nonAutosufficienzaTemporanea" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="disabileGravissimo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="patologiaOncologica" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="faseTerminale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="provvedimentoGiudiziario" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="tipoAssegno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="msna" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType" minOccurs="0"/&amp;gt;
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
        "certificatoL104",
        "demenzaCertificata",
        "disabilitaSensoriali",
        "statoInvalidita",
        "attesaInvalidita",
        "assegnoAccompagnamento",
        "attesaAccompagnamento",
        "nonAutosufficiente",
        "nonAutosufficienzaTemporanea",
        "disabileGravissimo",
        "patologiaOncologica",
        "faseTerminale",
        "provvedimentoGiudiziario",
        "tipoAssegno",
        "note",
        "msna"
    })
    public static class DatiPersonali {

        protected IseeType isee;
        @XmlElement(required = true, nillable = true)
        protected String certificatoL104;
        @XmlSchemaType(name = "string")
        protected SiNoType demenzaCertificata;
        @XmlSchemaType(name = "string")
        protected SiNoType disabilitaSensoriali;
        protected String statoInvalidita;
        @XmlSchemaType(name = "string")
        protected SiNoType attesaInvalidita;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType assegnoAccompagnamento;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType attesaAccompagnamento;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType nonAutosufficiente;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType nonAutosufficienzaTemporanea;
        @XmlElement(nillable = true)
        protected String disabileGravissimo;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType patologiaOncologica;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType faseTerminale;
        @XmlElement(required = true, nillable = true)
        protected String provvedimentoGiudiziario;
        @XmlElement(nillable = true)
        protected String tipoAssegno;
        @XmlElement(nillable = true)
        protected String note;
        @XmlElement(nillable = true)
        @XmlSchemaType(name = "string")
        protected SiNoType msna;

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
         * Recupera il valore della proprietà certificatoL104.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCertificatoL104() {
            return certificatoL104;
        }

        /**
         * Imposta il valore della proprietà certificatoL104.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCertificatoL104(String value) {
            this.certificatoL104 = value;
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
         * Recupera il valore della proprietà disabilitaSensoriali.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getDisabilitaSensoriali() {
            return disabilitaSensoriali;
        }

        /**
         * Imposta il valore della proprietà disabilitaSensoriali.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setDisabilitaSensoriali(SiNoType value) {
            this.disabilitaSensoriali = value;
        }

        /**
         * Recupera il valore della proprietà statoInvalidita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatoInvalidita() {
            return statoInvalidita;
        }

        /**
         * Imposta il valore della proprietà statoInvalidita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatoInvalidita(String value) {
            this.statoInvalidita = value;
        }

        /**
         * Recupera il valore della proprietà attesaInvalidita.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getAttesaInvalidita() {
            return attesaInvalidita;
        }

        /**
         * Imposta il valore della proprietà attesaInvalidita.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setAttesaInvalidita(SiNoType value) {
            this.attesaInvalidita = value;
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
         * Recupera il valore della proprietà attesaAccompagnamento.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getAttesaAccompagnamento() {
            return attesaAccompagnamento;
        }

        /**
         * Imposta il valore della proprietà attesaAccompagnamento.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setAttesaAccompagnamento(SiNoType value) {
            this.attesaAccompagnamento = value;
        }

        /**
         * Recupera il valore della proprietà nonAutosufficiente.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getNonAutosufficiente() {
            return nonAutosufficiente;
        }

        /**
         * Imposta il valore della proprietà nonAutosufficiente.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setNonAutosufficiente(SiNoType value) {
            this.nonAutosufficiente = value;
        }

        /**
         * Recupera il valore della proprietà nonAutosufficienzaTemporanea.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getNonAutosufficienzaTemporanea() {
            return nonAutosufficienzaTemporanea;
        }

        /**
         * Imposta il valore della proprietà nonAutosufficienzaTemporanea.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setNonAutosufficienzaTemporanea(SiNoType value) {
            this.nonAutosufficienzaTemporanea = value;
        }

        /**
         * Recupera il valore della proprietà disabileGravissimo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDisabileGravissimo() {
            return disabileGravissimo;
        }

        /**
         * Imposta il valore della proprietà disabileGravissimo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDisabileGravissimo(String value) {
            this.disabileGravissimo = value;
        }

        /**
         * Recupera il valore della proprietà patologiaOncologica.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getPatologiaOncologica() {
            return patologiaOncologica;
        }

        /**
         * Imposta il valore della proprietà patologiaOncologica.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setPatologiaOncologica(SiNoType value) {
            this.patologiaOncologica = value;
        }

        /**
         * Recupera il valore della proprietà faseTerminale.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getFaseTerminale() {
            return faseTerminale;
        }

        /**
         * Imposta il valore della proprietà faseTerminale.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setFaseTerminale(SiNoType value) {
            this.faseTerminale = value;
        }

        /**
         * Recupera il valore della proprietà provvedimentoGiudiziario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProvvedimentoGiudiziario() {
            return provvedimentoGiudiziario;
        }

        /**
         * Imposta il valore della proprietà provvedimentoGiudiziario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProvvedimentoGiudiziario(String value) {
            this.provvedimentoGiudiziario = value;
        }

        /**
         * Recupera il valore della proprietà tipoAssegno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoAssegno() {
            return tipoAssegno;
        }

        /**
         * Imposta il valore della proprietà tipoAssegno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoAssegno(String value) {
            this.tipoAssegno = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

        /**
         * Recupera il valore della proprietà msna.
         * 
         * @return
         *     possible object is
         *     {@link SiNoType }
         *     
         */
        public SiNoType getMsna() {
            return msna;
        }

        /**
         * Imposta il valore della proprietà msna.
         * 
         * @param value
         *     allowed object is
         *     {@link SiNoType }
         *     
         */
        public void setMsna(SiNoType value) {
            this.msna = value;
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
     *         &amp;lt;element name="titoloStudio" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="condizioneProfessionale" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
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
        "titoloStudio",
        "condizioneProfessionale"
    })
    public static class DatiProfessionali {

        @XmlElement(required = true)
        protected String titoloStudio;
        @XmlElement(required = true)
        protected String condizioneProfessionale;

        /**
         * Recupera il valore della proprietà titoloStudio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitoloStudio() {
            return titoloStudio;
        }

        /**
         * Imposta il valore della proprietà titoloStudio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitoloStudio(String value) {
            this.titoloStudio = value;
        }

        /**
         * Recupera il valore della proprietà condizioneProfessionale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCondizioneProfessionale() {
            return condizioneProfessionale;
        }

        /**
         * Imposta il valore della proprietà condizioneProfessionale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCondizioneProfessionale(String value) {
            this.condizioneProfessionale = value;
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
     *         &amp;lt;element name="toponimo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}ToponimoType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="condizioneAbitativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="idoneitaAbitazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="struttura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
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
        "toponimo",
        "telefono",
        "condizioneAbitativa",
        "idoneitaAbitazione",
        "struttura"
    })
    public static class Domicilio {

        protected ToponimoType toponimo;
        @XmlElement(nillable = true)
        protected String telefono;
        @XmlElement(nillable = true)
        protected String condizioneAbitativa;
        @XmlElement(nillable = true)
        protected String idoneitaAbitazione;
        @XmlElement(nillable = true)
        protected String struttura;

        /**
         * Recupera il valore della proprietà toponimo.
         * 
         * @return
         *     possible object is
         *     {@link ToponimoType }
         *     
         */
        public ToponimoType getToponimo() {
            return toponimo;
        }

        /**
         * Imposta il valore della proprietà toponimo.
         * 
         * @param value
         *     allowed object is
         *     {@link ToponimoType }
         *     
         */
        public void setToponimo(ToponimoType value) {
            this.toponimo = value;
        }

        /**
         * Recupera il valore della proprietà telefono.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTelefono() {
            return telefono;
        }

        /**
         * Imposta il valore della proprietà telefono.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTelefono(String value) {
            this.telefono = value;
        }

        /**
         * Recupera il valore della proprietà condizioneAbitativa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCondizioneAbitativa() {
            return condizioneAbitativa;
        }

        /**
         * Imposta il valore della proprietà condizioneAbitativa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCondizioneAbitativa(String value) {
            this.condizioneAbitativa = value;
        }

        /**
         * Recupera il valore della proprietà idoneitaAbitazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdoneitaAbitazione() {
            return idoneitaAbitazione;
        }

        /**
         * Imposta il valore della proprietà idoneitaAbitazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdoneitaAbitazione(String value) {
            this.idoneitaAbitazione = value;
        }

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

    }

}
