package org.miase.jlibsedml.api;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ReadSedMLTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testValidSedMLDocHasNoErrors() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument("TestData/sedMLBIOM21.xml");
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		assertEquals(0, errors.size());
		
	}
	
	@Test
	(expected=XMLException.class)
	public void testInvalidXML() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument("TestData/InvalidXMLsedMLBIOM21.xml");
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		
		
	}
	
	

	@Test
	public void testValidationOfDocument() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument("TestData/sedMLBIOM21.xml");
		List<SedMLError> errors = doc.getErrors();
		assertFalse(doc.hasErrors());
		setInitialTimeCourseToIllegalValue(doc);
		
		doc.revalidate();
		assertEquals(1, errors.size());
	}
	
	@Test
	public void testSedMLDocumentReadWriteReadRoundtrip() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument("TestData/sedMLBIOM21.xml");
		File f = File.createTempFile("sedml", "xml");
		doc.writeDocument(f);
		doc = Libsedml.readDocument(f.getAbsolutePath());
		assertFalse(doc.hasErrors());
	}

	private void setInitialTimeCourseToIllegalValue(SEDMLDocument doc) {
		doc.getSedMLModel().getListOfSimulations()
		                   .getUniformTimeCourses()
		                   .get(0)
		                   .setInitialTime(10000);
		
	}
	
	
}
