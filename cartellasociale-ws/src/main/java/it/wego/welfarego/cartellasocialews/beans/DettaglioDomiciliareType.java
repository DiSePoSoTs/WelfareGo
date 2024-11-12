
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per DettaglioDomiciliareType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DettaglioDomiciliareType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="qtaMensili" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="obiettivi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="erogatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioDomiciliareType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "qtaMensili",
    "obiettivi",
    "erogatore"
})
public class DettaglioDomiciliareType {

    protected Integer qtaMensili;
    @XmlElement(nillable = true)
    protected String obiettivi;
    @XmlElement(nillable = true)
    protected String erogatore;

    /**
     * Recupera il valore della proprietà qtaMensili.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQtaMensili() {
        return qtaMensili;
    }

    /**
     * Imposta il valore della proprietà qtaMensili.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQtaMensili(Integer value) {
        this.qtaMensili = value;
    }

    /**
     * Recupera il valore della proprietà obiettivi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObiettivi() {
        return obiettivi;
    }

    /**
     * Imposta il valore della proprietà obiettivi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObiettivi(String value) {
        this.obiettivi = value;
    }

    /**
     * Recupera il valore della proprietà erogatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErogatore() {
        return erogatore;
    }

    /**
     * Imposta il valore della proprietà erogatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErogatore(String value) {
        this.erogatore = value;
    }

}
