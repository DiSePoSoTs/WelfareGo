
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCartella" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="personaRiferimento" type="{http://cartellasociale.sanita.insiel.it}personaRiferimentoType"/>
 *         &lt;element name="dataModifica" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}DataType"/>
 *         &lt;element name="codice" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}CodiceOperatoreType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versione" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idCartella",
    "personaRiferimento",
    "dataModifica",
    "codice"
})
@XmlRootElement(name = "InserimentoPersonaRiferimento")
public class InserimentoPersonaRiferimento {

    protected long idCartella;
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
