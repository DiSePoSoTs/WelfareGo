
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per personaRiferimentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="personaRiferimentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="datiBase" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}AnagraficaBaseType"/&amp;gt;
 *         &amp;lt;element name="residenza" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}IndirizzoType"/&amp;gt;
 *         &amp;lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="tipoRelazione" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="appartenenzaNucleoFam" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SiNoType"/&amp;gt;
 *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="modalitaPagamentoQuietanzante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="ibanQuietanzante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
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
    "note",
    "modalitaPagamentoQuietanzante",
    "ibanQuietanzante"
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
    protected String modalitaPagamentoQuietanzante;
    protected String ibanQuietanzante;

    /**
     * Recupera il valore della proprietà datiBase.
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
     * Imposta il valore della proprietà datiBase.
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
     * Recupera il valore della proprietà residenza.
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
     * Imposta il valore della proprietà residenza.
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
     * Recupera il valore della proprietà tipoRelazione.
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
     * Imposta il valore della proprietà tipoRelazione.
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
     * Recupera il valore della proprietà appartenenzaNucleoFam.
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
     * Imposta il valore della proprietà appartenenzaNucleoFam.
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
     * Recupera il valore della proprietà modalitaPagamentoQuietanzante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalitaPagamentoQuietanzante() {
        return modalitaPagamentoQuietanzante;
    }

    /**
     * Imposta il valore della proprietà modalitaPagamentoQuietanzante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalitaPagamentoQuietanzante(String value) {
        this.modalitaPagamentoQuietanzante = value;
    }

    /**
     * Recupera il valore della proprietà ibanQuietanzante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanQuietanzante() {
        return ibanQuietanzante;
    }

    /**
     * Imposta il valore della proprietà ibanQuietanzante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanQuietanzante(String value) {
        this.ibanQuietanzante = value;
    }

}
