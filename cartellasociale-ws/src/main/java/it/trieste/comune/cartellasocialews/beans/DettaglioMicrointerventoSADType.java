
package it.trieste.comune.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per DettaglioMicrointerventoSADType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DettaglioMicrointerventoSADType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="tipologiaMicroInterventoSad" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioMicrointerventoSADType", propOrder = {
    "tipologiaMicroInterventoSad"
})
public class DettaglioMicrointerventoSADType {

    @XmlElement(required = true)
    protected String tipologiaMicroInterventoSad;

    /**
     * Recupera il valore della proprietà tipologiaMicroInterventoSad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipologiaMicroInterventoSad() {
        return tipologiaMicroInterventoSad;
    }

    /**
     * Imposta il valore della proprietà tipologiaMicroInterventoSad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipologiaMicroInterventoSad(String value) {
        this.tipologiaMicroInterventoSad = value;
    }

}
