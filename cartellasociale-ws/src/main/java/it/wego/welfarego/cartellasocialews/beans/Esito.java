
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Esito operazione
 * 
 * &lt;p&gt;Classe Java per Esito complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="Esito"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="codEsito" type="{http://framework.cartellasociale.sanita.insiel.it}EsitoOperazioneEnum"/&amp;gt;
 *         &amp;lt;element name="descrEsito" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}Descr256Type"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Esito", namespace = "http://framework.cartellasociale.sanita.insiel.it", propOrder = {
    "codEsito",
    "descrEsito"
})
public class Esito {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EsitoOperazioneEnum codEsito;
    @XmlElement(required = true)
    protected String descrEsito;

    /**
     * Recupera il valore della proprietà codEsito.
     * 
     * @return
     *     possible object is
     *     {@link EsitoOperazioneEnum }
     *     
     */
    public EsitoOperazioneEnum getCodEsito() {
        return codEsito;
    }

    /**
     * Imposta il valore della proprietà codEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoOperazioneEnum }
     *     
     */
    public void setCodEsito(EsitoOperazioneEnum value) {
        this.codEsito = value;
    }

    /**
     * Recupera il valore della proprietà descrEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrEsito() {
        return descrEsito;
    }

    /**
     * Imposta il valore della proprietà descrEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrEsito(String value) {
        this.descrEsito = value;
    }

}
