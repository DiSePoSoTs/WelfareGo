
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DettaglioMicrointerventoSADType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DettaglioMicrointerventoSADType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipologiaMicroInterventoSad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
