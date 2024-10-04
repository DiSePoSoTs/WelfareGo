
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per AnagraficaBaseType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="AnagraficaBaseType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="codiceNucleoFamiliare" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="codiceAscot" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="nome"&amp;gt;
 *           &amp;lt;simpleType&amp;gt;
 *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *             &amp;lt;/restriction&amp;gt;
 *           &amp;lt;/simpleType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="cognome"&amp;gt;
 *           &amp;lt;simpleType&amp;gt;
 *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *             &amp;lt;/restriction&amp;gt;
 *           &amp;lt;/simpleType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="codiceFiscale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}CodiceFiscaleType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="sesso" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SessoType"/&amp;gt;
 *         &amp;lt;element name="cittadinanza1" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="cittadinanza2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="nascita" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NascitaType"/&amp;gt;
 *         &amp;lt;element name="modalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="iban" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaBaseType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "codiceNucleoFamiliare",
    "codiceAscot",
    "nome",
    "cognome",
    "codiceFiscale",
    "sesso",
    "cittadinanza1",
    "cittadinanza2",
    "nascita",
    "modalitaPagamento",
    "iban"
})
public class AnagraficaBaseType {

    protected Long codiceNucleoFamiliare;
    protected Long codiceAscot;
    @XmlElement(required = true)
    protected String nome;
    @XmlElement(required = true)
    protected String cognome;
    protected String codiceFiscale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SessoType sesso;
    protected long cittadinanza1;
    protected Long cittadinanza2;
    @XmlElement(required = true)
    protected NascitaType nascita;
    protected String modalitaPagamento;
    protected String iban;

    /**
     * Recupera il valore della proprietà codiceNucleoFamiliare.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodiceNucleoFamiliare() {
        return codiceNucleoFamiliare;
    }

    /**
     * Imposta il valore della proprietà codiceNucleoFamiliare.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodiceNucleoFamiliare(Long value) {
        this.codiceNucleoFamiliare = value;
    }

    /**
     * Recupera il valore della proprietà codiceAscot.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodiceAscot() {
        return codiceAscot;
    }

    /**
     * Imposta il valore della proprietà codiceAscot.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodiceAscot(Long value) {
        this.codiceAscot = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     * 
     * @return
     *     possible object is
     *     {@link SessoType }
     *     
     */
    public SessoType getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link SessoType }
     *     
     */
    public void setSesso(SessoType value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà cittadinanza1.
     * 
     */
    public long getCittadinanza1() {
        return cittadinanza1;
    }

    /**
     * Imposta il valore della proprietà cittadinanza1.
     * 
     */
    public void setCittadinanza1(long value) {
        this.cittadinanza1 = value;
    }

    /**
     * Recupera il valore della proprietà cittadinanza2.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCittadinanza2() {
        return cittadinanza2;
    }

    /**
     * Imposta il valore della proprietà cittadinanza2.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCittadinanza2(Long value) {
        this.cittadinanza2 = value;
    }

    /**
     * Recupera il valore della proprietà nascita.
     * 
     * @return
     *     possible object is
     *     {@link NascitaType }
     *     
     */
    public NascitaType getNascita() {
        return nascita;
    }

    /**
     * Imposta il valore della proprietà nascita.
     * 
     * @param value
     *     allowed object is
     *     {@link NascitaType }
     *     
     */
    public void setNascita(NascitaType value) {
        this.nascita = value;
    }

    /**
     * Recupera il valore della proprietà modalitaPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalitaPagamento() {
        return modalitaPagamento;
    }

    /**
     * Imposta il valore della proprietà modalitaPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalitaPagamento(String value) {
        this.modalitaPagamento = value;
    }

    /**
     * Recupera il valore della proprietà iban.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIban() {
        return iban;
    }

    /**
     * Imposta il valore della proprietà iban.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIban(String value) {
        this.iban = value;
    }

}
