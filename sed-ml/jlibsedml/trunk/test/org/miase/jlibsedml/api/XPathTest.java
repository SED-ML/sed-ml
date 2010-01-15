package org.miase.jlibsedml.api;


import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathTest {
	
	public class SBMLNamespaceContext implements NamespaceContext {

	    public String getNamespaceURI(String prefix) {
	        if (prefix == null) throw new NullPointerException("Null prefix");
	        else if ("sbml".equals(prefix)) return "http://www.sbml.org/sbml/level2";
	        else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
	        return XMLConstants.NULL_NS_URI;
	    }

	    // This method isn't necessary for XPath processing.
	    public String getPrefix(String uri) {
	        throw new UnsupportedOperationException();
	    }

	    // This method isn't necessary for XPath processing either.
	    public Iterator getPrefixes(String uri) {
	        throw new UnsupportedOperationException();
	    }

	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test1 () throws Exception {
			
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("TestData/BIOMD0000000021.xml");
		NodeList nl = doc.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {

			Node r = nl.item(i);
			Node model =r.getChildNodes().item(1);
			
			for (int j = 0; j < model.getChildNodes().getLength(); j++) {
				if (model.getChildNodes().item(j) != null)
				System.out.println(model.getChildNodes().item(j).getNodeName());
			}	
		}
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(new SBMLNamespaceContext());
		XPathExpression expr = xpath.compile("//sbml:model/sbml:listOfParameters/sbml:parameter[@id='V_mT']/@value");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		Node toChange=nodes.item(0);
		assertEquals("0.7", toChange.getNodeValue());
	}

}
