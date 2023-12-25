
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Esito operazione
 * 
 * <p>Classe Java per Esito complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Esito">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codEsito" type="{http://framework.cartellasociale.sanita.insiel.it}EsitoOperazioneEnum"/>
 *         &lt;element name="descrEsito" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}Descr256Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
