
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per progettoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="progettoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="problematiche" type="{http://cartellasociale.sanita.insiel.it}problematicheType"/&amp;gt;
 *         &amp;lt;element name="risorse" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
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
@XmlType(name = "progettoType", propOrder = {
    "problematiche",
    "risorse",
    "note",
    "dataModifica"
})
public class ProgettoType {

    @XmlElement(required = true)
    protected ProblematicheType problematiche;
    protected String risorse;
    protected String note;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataModifica;

    /**
     * Recupera il valore della proprietà problematiche.
     * 
     * @return
     *     possible object is
     *     {@link ProblematicheType }
     *     
     */
    public ProblematicheType getProblematiche() {
        return problematiche;
    }

    /**
     * Imposta il valore della proprietà problematiche.
     * 
     * @param value
     *     allowed object is
     *     {@link ProblematicheType }
     *     
     */
    public void setProblematiche(ProblematicheType value) {
        this.problematiche = value;
    }

    /**
     * Recupera il valore della proprietà risorse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRisorse() {
        return risorse;
    }

    /**
     * Imposta il valore della proprietà risorse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRisorse(String value) {
        this.risorse = value;
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

}
