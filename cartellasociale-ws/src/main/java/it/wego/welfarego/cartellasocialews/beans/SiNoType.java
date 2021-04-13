
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SiNoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="SiNoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SiNoType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it")
@XmlEnum
public enum SiNoType {

    S,
    N;

    public String value() {
        return name();
    }

    public static SiNoType fromValue(String v) {
        return valueOf(v);
    }

}
