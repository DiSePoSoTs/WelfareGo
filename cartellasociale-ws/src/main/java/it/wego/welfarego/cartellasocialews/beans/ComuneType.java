
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ComuneType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ComuneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceCatastale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceIstat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComuneType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "codiceCatastale",
    "codiceIstat",
    "descrizione"
})
public class ComuneType {

    @XmlElement(required = true)
    protected String codiceCatastale;
    @XmlElement(required = true)
    protected String codiceIstat;
    @XmlElement(required = true)
    protected String descrizione;

    /**
     * Recupera il valore della propriet� codiceCatastale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceCatastale() {
        return codiceCatastale;
    }

    /**
     * Imposta il valore della propriet� codiceCatastale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceCatastale(String value) {
        this.codiceCatastale = value;
    }

    /**
     * Recupera il valore della propriet� codiceIstat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIstat() {
        return codiceIstat;
    }

    /**
     * Imposta il valore della propriet� codiceIstat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIstat(String value) {
        this.codiceIstat = value;
    }

    /**
     * Recupera il valore della propriet� descrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della propriet� descrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

}
