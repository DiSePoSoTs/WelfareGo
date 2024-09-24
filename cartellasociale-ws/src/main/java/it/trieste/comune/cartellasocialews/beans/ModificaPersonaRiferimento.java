
package it.trieste.comune.cartellasocialews.beans;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &amp;lt;element name="idCartella" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="idPersonaRiferimento" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="personaRiferimento" type="{http://cartellasociale.sanita.insiel.it}personaRiferimentoType"/&amp;gt;
 *         &amp;lt;element name="dataModifica" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/&amp;gt;
 *         &amp;lt;element name="codice" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}CodiceOperatoreType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *       &amp;lt;attribute name="versione" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idCartella",
    "idPersonaRiferimento",
    "personaRiferimento",
    "dataModifica",
    "codice"
})
@XmlRootElement(name = "ModificaPersonaRiferimento")
public class ModificaPersonaRiferimento {

    protected long idCartella;
    protected long idPersonaRiferimento;
    @XmlElement(required = true)
    protected PersonaRiferimentoType personaRiferimento;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataModifica;
    @XmlElement(required = true)
    protected String codice;
    @XmlAttribute(name = "versione", required = true)
    protected String versione;

    /**
     * Recupera il valore della proprietà idCartella.
     * 
     */
    public long getIdCartella() {
        return idCartella;
    }

    /**
     * Imposta il valore della proprietà idCartella.
     * 
     */
    public void setIdCartella(long value) {
        this.idCartella = value;
    }

    /**
     * Recupera il valore della proprietà idPersonaRiferimento.
     * 
     */
    public long getIdPersonaRiferimento() {
        return idPersonaRiferimento;
    }

    /**
     * Imposta il valore della proprietà idPersonaRiferimento.
     * 
     */
    public void setIdPersonaRiferimento(long value) {
        this.idPersonaRiferimento = value;
    }

    /**
     * Recupera il valore della proprietà personaRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link PersonaRiferimentoType }
     *     
     */
    public PersonaRiferimentoType getPersonaRiferimento() {
        return personaRiferimento;
    }

    /**
     * Imposta il valore della proprietà personaRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonaRiferimentoType }
     *     
     */
    public void setPersonaRiferimento(PersonaRiferimentoType value) {
        this.personaRiferimento = value;
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
     * Recupera il valore della proprietà codice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprietà codice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodice(String value) {
        this.codice = value;
    }

    /**
     * Recupera il valore della proprietà versione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersione() {
        return versione;
    }

    /**
     * Imposta il valore della proprietà versione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersione(String value) {
        this.versione = value;
    }

}
