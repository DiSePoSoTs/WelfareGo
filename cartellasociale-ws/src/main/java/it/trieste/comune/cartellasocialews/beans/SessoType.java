
package it.trieste.comune.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per SessoType.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="SessoType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="F"/&amp;gt;
 *     &amp;lt;enumeration value="M"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "SessoType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it")
@XmlEnum
public enum SessoType {

    F,
    M;

    public String value() {
        return name();
    }

    public static SessoType fromValue(String v) {
        return valueOf(v);
    }

}
