package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.miase.jlibsedml.api.SedMLError;
import org.miase.jlibsedml.api.SedMLGlobalValidator;
import org.miase.jlibsedml.generated.ListOfModels;
import org.miase.jlibsedml.generated.SedML;
import org.xml.sax.SAXException;


public class ReadSedMLTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test1() throws FileNotFoundException, SAXException, JAXBException{
		SedMLGlobalValidator validator = new SedMLGlobalValidator();
		List<SedMLError> errors = validator.validate("TestData/sedMLBIOM21.xml");
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		assertEquals(0, errors.size());
		
	}
	
	@Test
	(expected=JAXBException.class)
	public void testInvalidXML() throws FileNotFoundException, SAXException, JAXBException{
		SedMLGlobalValidator validator = new SedMLGlobalValidator();
		List<SedMLError> errors = validator.validate("TestData/InvalidXMLsedMLBIOM21.xml");
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		
		
	}
	
	
}
