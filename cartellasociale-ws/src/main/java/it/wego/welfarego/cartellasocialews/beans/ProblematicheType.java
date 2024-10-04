
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per problematicheType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="problematicheType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="macroproblematica" maxOccurs="unbounded"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="tipologiaMacroproblematica" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="microproblematica" type="{http://cartellasociale.sanita.insiel.it}microProblematicaType" maxOccurs="unbounded"/&amp;gt;
 *                   &amp;lt;element name="noteAltro" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="rilevanzaObiettivi" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}RilevanzaObiettiviType"/&amp;gt;
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
@XmlType(name = "problematicheType", propOrder = {
    "macroproblematica"
})
public class ProblematicheType {

    @XmlElement(required = true)
    protected List<ProblematicheType.Macroproblematica> macroproblematica;

    /**
     * Gets the value of the macroproblematica property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the macroproblematica property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getMacroproblematica().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link ProblematicheType.Macroproblematica }
     * 
     * 
     */
    public List<ProblematicheType.Macroproblematica> getMacroproblematica() {
        if (macroproblematica == null) {
            macroproblematica = new ArrayList<ProblematicheType.Macroproblematica>();
        }
        return this.macroproblematica;
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
     *         &amp;lt;element name="tipologiaMacroproblematica" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="microproblematica" type="{http://cartellasociale.sanita.insiel.it}microProblematicaType" maxOccurs="unbounded"/&amp;gt;
     *         &amp;lt;element name="noteAltro" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="rilevanzaObiettivi" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}RilevanzaObiettiviType"/&amp;gt;
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
        "tipologiaMacroproblematica",
        "microproblematica",
        "noteAltro",
        "rilevanzaObiettivi"
    })
    public static class Macroproblematica {

        @XmlElement(required = true, nillable = true)
        protected String tipologiaMacroproblematica;
        @XmlElement(required = true)
        protected List<MicroProblematicaType> microproblematica;
        @XmlElementRef(name = "noteAltro", namespace = "http://cartellasociale.sanita.insiel.it", type = JAXBElement.class, required = false)
        protected JAXBElement<String> noteAltro;
        @XmlElement(required = true, nillable = true)
        protected RilevanzaObiettiviType rilevanzaObiettivi;

        /**
         * Recupera il valore della proprietà tipologiaMacroproblematica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipologiaMacroproblematica() {
            return tipologiaMacroproblematica;
        }

        /**
         * Imposta il valore della proprietà tipologiaMacroproblematica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipologiaMacroproblematica(String value) {
            this.tipologiaMacroproblematica = value;
        }

        /**
         * Gets the value of the microproblematica property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the microproblematica property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getMicroproblematica().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link MicroProblematicaType }
         * 
         * 
         */
        public List<MicroProblematicaType> getMicroproblematica() {
            if (microproblematica == null) {
                microproblematica = new ArrayList<MicroProblematicaType>();
            }
            return this.microproblematica;
        }

        /**
         * Recupera il valore della proprietà noteAltro.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getNoteAltro() {
            return noteAltro;
        }

        /**
         * Imposta il valore della proprietà noteAltro.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setNoteAltro(JAXBElement<String> value) {
            this.noteAltro = value;
        }

        /**
         * Recupera il valore della proprietà rilevanzaObiettivi.
         * 
         * @return
         *     possible object is
         *     {@link RilevanzaObiettiviType }
         *     
         */
        public RilevanzaObiettiviType getRilevanzaObiettivi() {
            return rilevanzaObiettivi;
        }

        /**
         * Imposta il valore della proprietà rilevanzaObiettivi.
         * 
         * @param value
         *     allowed object is
         *     {@link RilevanzaObiettiviType }
         *     
         */
        public void setRilevanzaObiettivi(RilevanzaObiettiviType value) {
            this.rilevanzaObiettivi = value;
        }

    }

}
