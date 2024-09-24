
package it.trieste.comune.cartellasocialews.beans;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per anagraficaType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="anagraficaType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="datiComuni"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="anagraficaBase" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}AnagraficaBaseType"/&amp;gt;
 *                   &amp;lt;element name="residenza"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;sequence&amp;gt;
 *                             &amp;lt;element name="indirizzoResidenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IndirizzoType"/&amp;gt;
 *                             &amp;lt;element name="tipologiaResidenza" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                             &amp;lt;element name="decorrenzaResidenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 *                           &amp;lt;/sequence&amp;gt;
 *                         &amp;lt;/restriction&amp;gt;
 *                       &amp;lt;/complexContent&amp;gt;
 *                     &amp;lt;/complexType&amp;gt;
 *                   &amp;lt;/element&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="comuneCartella" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="identificativo_sottostruttura_ssc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="operatoreRiferimento" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
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
@XmlType(name = "anagraficaType", propOrder = {
    "datiComuni",
    "comuneCartella",
    "identificativoSottostrutturaSsc",
    "operatoreRiferimento",
    "dataModifica"
})
public class AnagraficaType {

    @XmlElement(required = true)
    protected AnagraficaType.DatiComuni datiComuni;
    @XmlElement(required = true)
    protected String comuneCartella;
    @XmlElement(name = "identificativo_sottostruttura_ssc")
    protected String identificativoSottostrutturaSsc;
    @XmlElement(required = true)
    protected String operatoreRiferimento;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataModifica;

    /**
     * Recupera il valore della proprietà datiComuni.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaType.DatiComuni }
     *     
     */
    public AnagraficaType.DatiComuni getDatiComuni() {
        return datiComuni;
    }

    /**
     * Imposta il valore della proprietà datiComuni.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaType.DatiComuni }
     *     
     */
    public void setDatiComuni(AnagraficaType.DatiComuni value) {
        this.datiComuni = value;
    }

    /**
     * Recupera il valore della proprietà comuneCartella.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneCartella() {
        return comuneCartella;
    }

    /**
     * Imposta il valore della proprietà comuneCartella.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneCartella(String value) {
        this.comuneCartella = value;
    }

    /**
     * Recupera il valore della proprietà identificativoSottostrutturaSsc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoSottostrutturaSsc() {
        return identificativoSottostrutturaSsc;
    }

    /**
     * Imposta il valore della proprietà identificativoSottostrutturaSsc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoSottostrutturaSsc(String value) {
        this.identificativoSottostrutturaSsc = value;
    }

    /**
     * Recupera il valore della proprietà operatoreRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperatoreRiferimento() {
        return operatoreRiferimento;
    }

    /**
     * Imposta il valore della proprietà operatoreRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperatoreRiferimento(String value) {
        this.operatoreRiferimento = value;
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
     *         &amp;lt;element name="anagraficaBase" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}AnagraficaBaseType"/&amp;gt;
     *         &amp;lt;element name="residenza"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;sequence&amp;gt;
     *                   &amp;lt;element name="indirizzoResidenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IndirizzoType"/&amp;gt;
     *                   &amp;lt;element name="tipologiaResidenza" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *                   &amp;lt;element name="decorrenzaResidenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
     *                 &amp;lt;/sequence&amp;gt;
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
    @XmlType(name = "", propOrder = {
        "anagraficaBase",
        "residenza"
    })
    public static class DatiComuni {

        @XmlElement(required = true)
        protected AnagraficaBaseType anagraficaBase;
        @XmlElement(required = true)
        protected AnagraficaType.DatiComuni.Residenza residenza;

        /**
         * Recupera il valore della proprietà anagraficaBase.
         * 
         * @return
         *     possible object is
         *     {@link AnagraficaBaseType }
         *     
         */
        public AnagraficaBaseType getAnagraficaBase() {
            return anagraficaBase;
        }

        /**
         * Imposta il valore della proprietà anagraficaBase.
         * 
         * @param value
         *     allowed object is
         *     {@link AnagraficaBaseType }
         *     
         */
        public void setAnagraficaBase(AnagraficaBaseType value) {
            this.anagraficaBase = value;
        }

        /**
         * Recupera il valore della proprietà residenza.
         * 
         * @return
         *     possible object is
         *     {@link AnagraficaType.DatiComuni.Residenza }
         *     
         */
        public AnagraficaType.DatiComuni.Residenza getResidenza() {
            return residenza;
        }

        /**
         * Imposta il valore della proprietà residenza.
         * 
         * @param value
         *     allowed object is
         *     {@link AnagraficaType.DatiComuni.Residenza }
         *     
         */
        public void setResidenza(AnagraficaType.DatiComuni.Residenza value) {
            this.residenza = value;
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
         *         &amp;lt;element name="indirizzoResidenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IndirizzoType"/&amp;gt;
         *         &amp;lt;element name="tipologiaResidenza" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
         *         &amp;lt;element name="decorrenzaResidenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
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
            "indirizzoResidenza",
            "tipologiaResidenza",
            "decorrenzaResidenza"
        })
        public static class Residenza {

            @XmlElement(required = true)
            protected IndirizzoType indirizzoResidenza;
            @XmlElement(required = true)
            protected String tipologiaResidenza;
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar decorrenzaResidenza;

            /**
             * Recupera il valore della proprietà indirizzoResidenza.
             * 
             * @return
             *     possible object is
             *     {@link IndirizzoType }
             *     
             */
            public IndirizzoType getIndirizzoResidenza() {
                return indirizzoResidenza;
            }

            /**
             * Imposta il valore della proprietà indirizzoResidenza.
             * 
             * @param value
             *     allowed object is
             *     {@link IndirizzoType }
             *     
             */
            public void setIndirizzoResidenza(IndirizzoType value) {
                this.indirizzoResidenza = value;
            }

            /**
             * Recupera il valore della proprietà tipologiaResidenza.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipologiaResidenza() {
                return tipologiaResidenza;
            }

            /**
             * Imposta il valore della proprietà tipologiaResidenza.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipologiaResidenza(String value) {
                this.tipologiaResidenza = value;
            }

            /**
             * Recupera il valore della proprietà decorrenzaResidenza.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getDecorrenzaResidenza() {
                return decorrenzaResidenza;
            }

            /**
             * Imposta il valore della proprietà decorrenzaResidenza.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setDecorrenzaResidenza(XMLGregorianCalendar value) {
                this.decorrenzaResidenza = value;
            }

        }

    }

}
