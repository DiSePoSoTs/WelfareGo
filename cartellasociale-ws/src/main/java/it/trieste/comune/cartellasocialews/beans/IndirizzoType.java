
package it.trieste.comune.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per IndirizzoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="IndirizzoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="toponimo" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}ToponimoType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndirizzoType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "toponimo"
})
public class IndirizzoType {

    @XmlElement(required = true)
    protected ToponimoType toponimo;

    /**
     * Recupera il valore della proprietà toponimo.
     * 
     * @return
     *     possible object is
     *     {@link ToponimoType }
     *     
     */
    public ToponimoType getToponimo() {
        return toponimo;
    }

    /**
     * Imposta il valore della proprietà toponimo.
     * 
     * @param value
     *     allowed object is
     *     {@link ToponimoType }
     *     
     */
    public void setToponimo(ToponimoType value) {
        this.toponimo = value;
    }

}
