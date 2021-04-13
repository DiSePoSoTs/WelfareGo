/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Gabri
 */
public class SmartNamespaceContext implements NamespaceContext {

    NamedNodeMap rootAttributes;

    public SmartNamespaceContext(Document doc) {
        Element root = doc.getDocumentElement();
        this.rootAttributes = root.getAttributes();
    }

    public String getNamespaceURI(String prefix) {
        String nameSpacePrefix, nameSpaceValue;
        for (int i = 0; i < rootAttributes.getLength(); i++) {
            nameSpacePrefix = rootAttributes.item(i).getNodeName();
            nameSpaceValue = rootAttributes.item(i).getNodeValue();
            if (nameSpacePrefix.contains(prefix.subSequence(0, prefix.length()))) {
                return nameSpaceValue;
            }
        }
        return null;
    }

    public String getPrefix(String namespaceURI) {
        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }
}
