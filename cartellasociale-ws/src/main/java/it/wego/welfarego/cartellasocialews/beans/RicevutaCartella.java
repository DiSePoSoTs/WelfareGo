
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
 *         &lt;element name="idCartella" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
    "idCartella"
})
@XmlRootElement(name = "RicevutaCartella")
public class RicevutaCartella
    extends RispostaBase
{

    protected Long idCartella;

    /**
     * Recupera il valore della proprietà idCartella.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdCartella() {
        return idCartella;
    }

    /**
     * Imposta il valore della proprietà idCartella.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdCartella(Long value) {
        this.idCartella = value;
    }

}
