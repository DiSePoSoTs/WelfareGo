
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per anonymous complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{http://framework.cartellasociale.sanita.insiel.it}RispostaBase"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="idIntervento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
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
