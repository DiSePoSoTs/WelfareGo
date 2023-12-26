
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SADType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SADType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obiettivi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="macrointerventoSad" type="{http://cartellasociale.sanita.insiel.it}DettaglioMacrointerventoSADType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SADType", propOrder = {
    "obiettivi",
    "macrointerventoSad"
})
public class SADType {

    @XmlElementRef(name = "obiettivi", namespace = "http://cartellasociale.sanita.insiel.it", type = JAXBElement.class)
    protected JAXBElement<String> obiettivi;
    @XmlElement(required = true)
    protected List<DettaglioMacrointerventoSADType> macrointerventoSad;

    /**
     * Recupera il valore della proprietà obiettivi.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getObiettivi() {
        return obiettivi;
    }

    /**
     * Imposta il valore della proprietà obiettivi.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setObiettivi(JAXBElement<String> value) {
        this.obiettivi = value;
    }

    /**
     * Gets the value of the macrointerventoSad property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the macrointerventoSad property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMacrointerventoSad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DettaglioMacrointerventoSADType }
     * 
     * 
     */
    public List<DettaglioMacrointerventoSADType> getMacrointerventoSad() {
        if (macrointerventoSad == null) {
            macrointerventoSad = new ArrayList<DettaglioMacrointerventoSADType>();
        }
        return this.macrointerventoSad;
    }

}
