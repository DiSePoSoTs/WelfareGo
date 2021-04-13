
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RilevanzaObiettiviType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RilevanzaObiettiviType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rilevanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fronteggiamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="obiettivoPrevalente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dettaglio" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RilevanzaObiettiviType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "rilevanza",
    "fronteggiamento",
    "obiettivoPrevalente",
    "dettaglio"
})
public class RilevanzaObiettiviType {

    @XmlElement(required = true)
    protected String rilevanza;
    @XmlElement(required = true, nillable = true)
    protected String fronteggiamento;
    @XmlElement(required = true, nillable = true)
    protected String obiettivoPrevalente;
    @XmlElement(required = true)
    protected String dettaglio;

    /**
     * Recupera il valore della proprietà rilevanza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRilevanza() {
        return rilevanza;
    }

    /**
     * Imposta il valore della proprietà rilevanza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRilevanza(String value) {
        this.rilevanza = value;
    }

    /**
     * Recupera il valore della proprietà fronteggiamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFronteggiamento() {
        return fronteggiamento;
    }

    /**
     * Imposta il valore della proprietà fronteggiamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFronteggiamento(String value) {
        this.fronteggiamento = value;
    }

    /**
     * Recupera il valore della proprietà obiettivoPrevalente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObiettivoPrevalente() {
        return obiettivoPrevalente;
    }

    /**
     * Imposta il valore della proprietà obiettivoPrevalente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObiettivoPrevalente(String value) {
        this.obiettivoPrevalente = value;
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

}
