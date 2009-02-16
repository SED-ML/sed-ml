package org.miase.jlibsedml.api;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ReadSedMLTest {
    private final File OK_DATA= new File("TestData/sedMLBIOM12.xml");
    private final File INVALID_DATA = new File ("TestData/InvalidXMLsedMLBIOM21.xml");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testValidSedMLDocHasNoErrors() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument(OK_DATA);
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		assertEquals(0, errors.size());
		
	}
	
	@Test
	(expected=XMLException.class)
	public void testInvalidXML() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument(INVALID_DATA);
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		
		
	}
	
	@Test
	public void testCanReadDocFromString() throws XMLException, IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(OK_DATA));
		StringBuffer sb = new StringBuffer();
		String line ="";
		while ( (line = br.readLine())!=null){
			sb.append(line);
		}
		
		SEDMLDocument doc = Libsedml.readDocument(sb.toString());
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		
		
	}
	
	

	@Test
	public void testValidationOfDocument() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument(OK_DATA);
		List<SedMLError> errors = doc.getErrors();
		assertFalse(doc.hasErrors());
		setInitialTimeCourseToIllegalValue(doc);
		
		doc.revalidate();
		assertEquals(1, errors.size());
	}
	
	@Test
	public void testSedMLDocumentReadWriteReadRoundtrip() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument(OK_DATA);
		File f = File.createTempFile("sedml", "xml");
		doc.writeDocument(f);
		doc = Libsedml.readDocument(f);
		assertFalse(doc.hasErrors());
	}

	private void setInitialTimeCourseToIllegalValue(SEDMLDocument doc) {
		doc.getSedMLModel().getListOfSimulations()
		                   .getUniformTimeCourses()
		                   .get(0)
		                   .setInitialTime(10000);
		
	}
	
	
}
