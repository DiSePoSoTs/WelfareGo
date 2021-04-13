
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per listaMicroInterventiSADType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="listaMicroInterventiSADType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="microinterventoSad" type="{http://cartellasociale.sanita.insiel.it}DettaglioMicrointerventoSADType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaMicroInterventiSADType", propOrder = {
    "microinterventoSad"
})
public class ListaMicroInterventiSADType {

    @XmlElement(required = true)
    protected List<DettaglioMicrointerventoSADType> microinterventoSad;

    /**
     * Gets the value of the microinterventoSad property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the microinterventoSad property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMicrointerventoSad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DettaglioMicrointerventoSADType }
     * 
     * 
     */
    public List<DettaglioMicrointerventoSADType> getMicrointerventoSad() {
        if (microinterventoSad == null) {
            microinterventoSad = new ArrayList<DettaglioMicrointerventoSADType>();
        }
        return this.microinterventoSad;
    }

}
