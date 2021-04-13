package it.wego.utils.xml;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author aleph
 */
public class XmlUtils {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static Logger slogger = LoggerFactory.getLogger(XmlUtils.class);
	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static final XPathFactory xPathFactory = XPathFactory.newInstance();
	private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private final XPath xPath = xPathFactory.newXPath();
	private final Supplier<DocumentBuilder> documentBuilderSupplier = Suppliers
			.memoize(new Supplier<DocumentBuilder>() {
				public DocumentBuilder get() {
					try {
						return documentBuilderFactory.newDocumentBuilder();
					} catch (ParserConfigurationException ex) {
						throw new RuntimeException(ex);
					}
				}
			});

	private final Supplier<Transformer> transformerSupplier = Suppliers.memoize(new Supplier<Transformer>() {
		public Transformer get() {
			try {
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
						configuration.includeHeader ? "no" : "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				return transformer;
			} catch (TransformerConfigurationException ex) {
				throw new RuntimeException(ex);
			}
		}
	});

	private final Supplier<Canonicalizer> canonicalizerSupplier = Suppliers.memoize(new Supplier<Canonicalizer>() {
		public Canonicalizer get() {
			try {
				return Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
			} catch (InvalidCanonicalizerException ex) {
				throw new RuntimeException(ex);
			}
		}
	});

	private final LoadingCache<String, XPathExpression> xpathExpressions = CacheBuilder.newBuilder().concurrencyLevel(1)
			.build(new CacheLoader<String, XPathExpression>() {
				@Override
				public XPathExpression load(String xpath) throws Exception {
					return xPath.compile(xpath);
				}
			});

	private final static LoadingCache<Class[], JAXBContext> jaxbContexts = CacheBuilder.newBuilder().concurrencyLevel(1)
			.build(new CacheLoader<Class[], JAXBContext>() {
				@Override
				public JAXBContext load(Class[] key) throws Exception {
					slogger.debug("preparing context for classes {}", key);
					return JAXBContext.newInstance(key);
				}
			});

	private final ValidationEventHandler jaxbValidationEventHandlerLogger = new ValidationEventHandler() {
		public boolean handleEvent(ValidationEvent event) {
			logger.debug("jaxb event : {}", event.toString());
			return true;
		}
	};

	private final LoadingCache<Class[], Marshaller> jaxbMarshallers = CacheBuilder.newBuilder().concurrencyLevel(1)
			.build(new CacheLoader<Class[], Marshaller>() {
				@Override
				public Marshaller load(Class[] key) throws Exception {
					Marshaller marshaller = jaxbContexts.get(key).createMarshaller();
					marshaller.setEventHandler(jaxbValidationEventHandlerLogger);
					marshaller.setProperty(Marshaller.JAXB_FRAGMENT,
							configuration.includeHeader ? Boolean.FALSE : Boolean.TRUE);// skip xml header
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					return marshaller;
				}
			});
	private final LoadingCache<Class[], Unmarshaller> jaxbUnmarshallers = CacheBuilder.newBuilder().concurrencyLevel(1)
			.build(new CacheLoader<Class[], Unmarshaller>() {
				@Override
				public Unmarshaller load(Class[] key) throws Exception {
					Unmarshaller unmarshaller = jaxbContexts.get(key).createUnmarshaller();
					unmarshaller.setEventHandler(jaxbValidationEventHandlerLogger);
					return unmarshaller;
				}
			});

	static {
		try {
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setIgnoringElementContentWhitespace(true);
			if (!Init.isInitialized()) {
				Init.init();
			}
		} catch (Exception ex) {
			slogger.error("error initializing xmlUtils", ex);
		}
	}
	private final XmlUtilsConfiguration configuration;

	private XmlUtils(XmlUtilsConfiguration configuration) {
		Preconditions.checkNotNull(configuration);
		this.configuration = configuration.clone();
	}

	/**
	 * same as getInstance()
	 */
	@Deprecated
	public static XmlUtils newInstance() {
		return getInstance();
	}

	/**
	 * return a shared instance of XmlUtils; remember to call instance.close() after
	 * work to return the borrowed instance
	 *
	 * @return
	 */
	public static XmlUtils getInstance() {
		return getInstance(new XmlUtilsConfiguration());
	}

	private final static LoadingCache<XmlUtilsConfiguration, ObjectPool> instancesPoolMap = CacheBuilder.newBuilder()
			.build(new CacheLoader<XmlUtilsConfiguration, ObjectPool>() {
				@Override
				public ObjectPool load(final XmlUtilsConfiguration xmlUtilsFactory) throws Exception {
					return new SoftReferenceObjectPool(new BasePooledObjectFactory<XmlUtils>() {
						@Override
						public XmlUtils create() throws Exception {
							return new XmlUtils(xmlUtilsFactory);
						}

						@Override
						public PooledObject<XmlUtils> wrap(XmlUtils obj) {
							return new DefaultPooledObject<XmlUtils>(obj);
						}
					});
				}
			});

	private static XmlUtils getInstance(final XmlUtilsConfiguration configuration) {
		try {
			return (XmlUtils) instancesPoolMap.get(configuration).borrowObject();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static XmlUtilsConfiguration configure() {
		return new XmlUtilsConfiguration();
	}

	/**
	 * permit this instance to be returned to the poll, for future reusing
	 */
	public void close() {
		try {
			instancesPoolMap.get(configuration).returnObject(this);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public JAXBContext getJAXBContext(Class... classes) {
		return jaxbContexts.getUnchecked(classes);
	}

	public Marshaller getJAXBMarshaller(Class... classes) {
		return jaxbMarshallers.getUnchecked(classes);
	}

	public Unmarshaller getJAXBUnmarshaller(Class... classes) {
		return jaxbUnmarshallers.getUnchecked(classes);
	}

	public <V> V unmarshallJaxbObject(Class<V> clazz, Node node) throws JAXBException {
		JAXBElement<V> jaxbObj = getJAXBUnmarshaller(clazz).unmarshal(node, clazz);
		return jaxbObj.getValue();
	}

	/**
	 *
	 * @param <V>
	 * @param clazz
	 * @param xpath
	 * @param document
	 * @return
	 * @throws ExecutionException
	 * @throws XPathExpressionException
	 * @throws JAXBException
	 * @deprecated use unmarshallJaxbObject(Class<V> clazz, String xpath, Document
	 *             document)
	 */
	@Deprecated
	public <V> V extractJaxbObject(Class<V> clazz, String xpath, Document document)
			throws ExecutionException, XPathExpressionException, JAXBException {
		return unmarshallJaxbObject(clazz, xpath, document);
	}

	public <V> V unmarshallJaxbObject(Class<V> clazz, String xpath, Document document)
			throws ExecutionException, XPathExpressionException, JAXBException {
		return unmarshallJaxbObject(clazz, getNode(xpath, document));
	}

	/**
	 * note: lazy trasformation
	 *
	 * @param <V>
	 * @param clazz
	 * @param xpath
	 * @param document
	 * @return
	 * @throws ExecutionException
	 * @throws XPathExpressionException
	 * @throws JAXBException
	 */
	public <V> Iterable<V> unmarshallJaxbObjects(final Class<V> clazz, String xpath, Document document)
			throws ExecutionException, XPathExpressionException, JAXBException {
		return Iterables.transform(getNodes(xpath, document), new Function<Node, V>() {
			public V apply(Node input) {
				try {
					return unmarshallJaxbObject(clazz, input);
				} catch (JAXBException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	}

	public <V> void marshallJaxbObject(V object, Result result)
			throws ExecutionException, XPathExpressionException, JAXBException {
		getJAXBMarshaller(object instanceof JAXBElement ? ((JAXBElement) object).getDeclaredType() : object.getClass())
				.marshal(object, result);
	}

	/**
	 * marshall the object to xml, appending it to the supplied node
	 *
	 * @param <V>
	 * @param object
	 * @param node
	 * @throws ExecutionException
	 * @throws XPathExpressionException
	 * @throws JAXBException
	 */
	public <V> void marshallJaxbObject(V object, Node node)
			throws ExecutionException, XPathExpressionException, JAXBException {
		marshallJaxbObject(object, new DOMResult(node));
	}

	/**
	 *
	 * @param <V>
	 * @param object
	 * @return
	 * @throws ExecutionException
	 * @throws XPathExpressionException
	 * @throws JAXBException
	 * @deprecated use marshallJaxbObjectToByteArray(V object)
	 */
	@Deprecated
	public <V> byte[] marshallJaxbObject(V object) throws ExecutionException, XPathExpressionException, JAXBException {
		return marshallJaxbObjectToByteArray(object);
	}

	public <V> byte[] marshallJaxbObjectToByteArray(V object)
			throws ExecutionException, XPathExpressionException, JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		marshallJaxbObject(object, new StreamResult(out));
		return out.toByteArray();
	}

	public <V> String marshallJaxbObjectToString(V object)
			throws ExecutionException, XPathExpressionException, JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		marshallJaxbObject(object, new StreamResult(out));
		return out.toString();
	}

	public <V> String marshallJaxbObjectToIndentedString(V object)
			throws ExecutionException, XPathExpressionException, JAXBException {
		return xmlToString(readXml(marshallJaxbObjectToString(object)));
	}

	public <V> void mergeJaxbObject(V object, String xpath, Document document)
			throws ExecutionException, XPathExpressionException, JAXBException {
		Node hrefRootNode = getNode(xpath, document), parentNode = hrefRootNode.getParentNode();
		logger.debug("merging object to xml via jaxb, replacing {} ({}) into {}",
				new Object[] { hrefRootNode, xpath, parentNode });
		Element tempNode = document.createElement("temp_jaxb_merge");
		marshallJaxbObject(object, tempNode);
		Node newNode = tempNode.getChildNodes().item(0);
		parentNode.replaceChild(newNode, hrefRootNode);
	}

	public static String getNameSpacePrefixFromDocument(Document doc) throws Exception {
		Element root = doc.getDocumentElement();
		NamedNodeMap rootAttributes = root.getAttributes();
		String nameSpacePrefix, nameSpaceValue;
		Node xmlnsNode = rootAttributes.getNamedItem("xmlns");
		for (int i = 0; i < rootAttributes.getLength(); i++) {
			nameSpacePrefix = rootAttributes.item(i).getNodeName();
			nameSpaceValue = rootAttributes.item(i).getNodeValue();
			if (!nameSpacePrefix.toUpperCase().equals("XMLNS") && xmlnsNode.getNodeValue().equals(nameSpaceValue)) {
				return nameSpacePrefix.replaceAll("xmlns", "");
			}
		}
		return "";
	}

	public static void removeNameSpaceFromDocument(Document doc) throws Exception {
		Element root = doc.getDocumentElement();
		NamedNodeMap rootAttributes = root.getAttributes();
		String nameSpacePrefix;
		for (int i = 0; i < rootAttributes.getLength(); i++) {
			nameSpacePrefix = rootAttributes.item(i).getNodeName();
			root.removeAttribute(nameSpacePrefix);
		}

	}

	public String xmlToString(Node node) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			writeXml(node, out);
			return out.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String xmlToC14nString(Node node) {
		try {
			return new String(canonicalizerSupplier.get().canonicalizeSubtree(node));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void writeXml(Node node, OutputStream out) throws TransformerConfigurationException, TransformerException {
		transformerSupplier.get().transform(new DOMSource(node), new StreamResult(out));
	}

	public Document readXml(String xml) {
		try {
			return readXml(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception ex) {
			throw ex instanceof RuntimeException ? ((RuntimeException) ex) : new RuntimeException(ex);
		}
	}

	public Document readXml(InputStream in)
			throws TransformerConfigurationException, TransformerException, SAXException, IOException {
		return getDocumentBuilder().parse(in);
	}

	public DocumentBuilder getDocumentBuilder() {
		return documentBuilderSupplier.get();
	}

	public Number getNumber(String xpath, Node node) throws ExecutionException, XPathExpressionException {
		return (Number) xpathExpressions.get(xpath).evaluate(node, XPathConstants.NUMBER);
	}

	public Boolean getBoolean(String xpath, Node node) throws ExecutionException, XPathExpressionException {
		return (Boolean) xpathExpressions.get(xpath).evaluate(node, XPathConstants.BOOLEAN);
	}

	public String getString(String xpath, Node node) throws ExecutionException, XPathExpressionException {
		return (String) xpathExpressions.get(xpath).evaluate(node, XPathConstants.STRING);
	}

	public Iterable<String> getStrings(String xpath, Node node) throws ExecutionException, XPathExpressionException {
		return Iterables.transform(getNodes(xpath, node), getNodeTextContentFunction);
	}

	public Node getNode(String xpath, Node node) {
		try {
			return (Node) xpathExpressions.get(xpath).evaluate(node, XPathConstants.NODE);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Iterable<Node> getNodes(String xpath, Node node) throws XPathExpressionException, ExecutionException {
		return new NodeIterable((NodeList) xpathExpressions.get(xpath).evaluate(node, XPathConstants.NODESET));
	}

	private static final class NodeIterable implements Iterable<Node> {

		private final NodeList nodeList;

		public NodeIterable(NodeList nodeList) {
			this.nodeList = nodeList;
		}

		public Iterator<Node> iterator() {
			return new NodeIterator(nodeList);
		}
	}

	public String formatXml(String xml) {
		return xmlToString(readXml(xml));
	}

	private static final class NodeIterator implements Iterator<Node> {

		private final NodeList nodeList;
		private final int size;
		private int index = 0;

		public NodeIterator(NodeList nodeList) {
			this.nodeList = nodeList;
			this.size = nodeList.getLength();
		}

		public boolean hasNext() {
			return index < size;
		}

		public Node next() {
			Node node = nodeList.item(index);
			if (node == null) {
				throw new NoSuchElementException();
			}
			index++;
			return node;
		}

		public void remove() {
			throw new UnsupportedOperationException("node removal not supported");
		}
	}

	private static final Function<Node, String> getNodeTextContentFunction = new Function<Node, String>() {
		public String apply(Node input) {
			return input.getTextContent();
		}
	};

	public final static class XmlUtilsConfiguration implements Cloneable {

		private boolean includeHeader = false;

		private XmlUtilsConfiguration() {
		}

		public XmlUtils getXmlUtils() {
			return getInstance(this);
		}

		public XmlUtilsConfiguration includeHeader() {
			includeHeader = true;
			return this;
		}

		@Override
		protected XmlUtilsConfiguration clone() {
			try {
				return (XmlUtilsConfiguration) super.clone();
			} catch (CloneNotSupportedException ex) {
				throw new RuntimeException(ex);
			}
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 13 * hash + (this.includeHeader ? 1 : 0);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final XmlUtilsConfiguration other = (XmlUtilsConfiguration) obj;
			if (this.includeHeader != other.includeHeader) {
				return false;
			}
			return true;
		}
	}
}
