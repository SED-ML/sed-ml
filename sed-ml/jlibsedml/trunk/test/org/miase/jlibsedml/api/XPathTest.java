package org.miase.jlibsedml.api;


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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XPathTest {

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
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expr = xpath.compile("/sbml/model/listOfParameters/parameter[@id='V_mT']/@value");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getNodeValue());
			nodes.item(i).setNodeValue("0.28");
			System.out.println(nodes.item(i).getNodeValue());
//		    NamedNodeMap map = nodes.item(i).getAttributes();
//		    System.out.println(map.getNamedItem("id").getNodeValue());
		}
	}

}
