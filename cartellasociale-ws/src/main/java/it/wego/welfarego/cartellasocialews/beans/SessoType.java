
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SessoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="SessoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="M"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
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
