
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://framework.cartellasociale.sanita.insiel.it}RispostaBase">
 *       &lt;sequence>
 *         &lt;element name="idIntervento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idIntervento"
})
@XmlRootElement(name = "RicevutaIntervento")
public class RicevutaIntervento
    extends RispostaBase
{

    protected Long idIntervento;

    /**
     * Recupera il valore della proprietà idIntervento.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdIntervento() {
        return idIntervento;
    }

    /**
     * Imposta il valore della proprietà idIntervento.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdIntervento(Long value) {
        this.idIntervento = value;
    }

}
