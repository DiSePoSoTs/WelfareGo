
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per personaRiferimentoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="personaRiferimentoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datiBase" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}AnagraficaBaseType"/>
 *         &lt;element name="residenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IndirizzoType"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoRelazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="appartenenzaNucleoFam" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/>
 *         &lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personaRiferimentoType", propOrder = {
    "datiBase",
    "residenza",
    "telefono",
    "tipoRelazione",
    "appartenenzaNucleoFam",
    "note"
})
public class PersonaRiferimentoType {

    @XmlElement(required = true)
    protected AnagraficaBaseType datiBase;
    @XmlElement(required = true)
    protected IndirizzoType residenza;
    protected String telefono;
    @XmlElement(required = true)
    protected String tipoRelazione;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SiNoType appartenenzaNucleoFam;
    protected String note;

    /**
     * Recupera il valore della propriet� datiBase.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaBaseType }
     *     
     */
    public AnagraficaBaseType getDatiBase() {
        return datiBase;
    }

    /**
     * Imposta il valore della propriet� datiBase.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaBaseType }
     *     
     */
    public void setDatiBase(AnagraficaBaseType value) {
        this.datiBase = value;
    }

    /**
     * Recupera il valore della propriet� residenza.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoType }
     *     
     */
    public IndirizzoType getResidenza() {
        return residenza;
    }

    /**
     * Imposta il valore della propriet� residenza.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoType }
     *     
     */
    public void setResidenza(IndirizzoType value) {
        this.residenza = value;
    }

    /**
     * Recupera il valore della propriet� telefono.
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
     * Imposta il valore della propriet� telefono.
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
     * Recupera il valore della propriet� tipoRelazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRelazione() {
        return tipoRelazione;
    }

    /**
     * Imposta il valore della propriet� tipoRelazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRelazione(String value) {
        this.tipoRelazione = value;
    }

    /**
     * Recupera il valore della propriet� appartenenzaNucleoFam.
     * 
     * @return
     *     possible object is
     *     {@link SiNoType }
     *     
     */
    public SiNoType getAppartenenzaNucleoFam() {
        return appartenenzaNucleoFam;
    }

    /**
     * Imposta il valore della propriet� appartenenzaNucleoFam.
     * 
     * @param value
     *     allowed object is
     *     {@link SiNoType }
     *     
     */
    public void setAppartenenzaNucleoFam(SiNoType value) {
        this.appartenenzaNucleoFam = value;
    }

    /**
     * Recupera il valore della propriet� note.
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
     * Imposta il valore della propriet� note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

}
