//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.22 at 06:46:27 PM CEST 
//


package it.wego.welfarego.xsd.cartellasociale;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipo_intervento_fondo_autonomia_possibile_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipo_intervento_fondo_autonomia_possibile_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="da2200"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipo_intervento_fondo_autonomia_possibile_type")
@XmlEnum
public enum TipoInterventoFondoAutonomiaPossibileType {

    @XmlEnumValue("da2200")
    DA_2200("da2200");
    private final String value;

    TipoInterventoFondoAutonomiaPossibileType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoInterventoFondoAutonomiaPossibileType fromValue(String v) {
        for (TipoInterventoFondoAutonomiaPossibileType c: TipoInterventoFondoAutonomiaPossibileType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
