package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SEDMLDocumentTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetErrors() {
		SEDMLDocument doc = Libsedml.createDocument();
		assertNotNull(doc.getErrors());
	}

	@Test
	public void testWriteDocumentToString() throws Exception {
		final String ROOT_ELEMENT="<sedml:sedML xmlns:sedml=\"http://www.biomodels.net/sed-ml\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\">";
		SEDMLDocument doc = Libsedml.createDocument();
		String s = doc.writeDocumentToString();
		assertTrue(s.contains(ROOT_ELEMENT));
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public void testGetChangedModelThrowSIAEIfModelIDDoesNotExist() throws Exception {
		//expected id ="model1" or "model2"
		SEDMLDocument doc = Libsedml.readDocument(new File("TestData/sedMLBIOM21.xml"));
		doc.getChangedModel("NON_EXISTENT_MODEL_ID", "original");
	}
	
	@Test
	public void testGetChangedModelDoesNotThrowIAEIfModelIDExists() throws Exception {
		//expected id ="model1" or "model2"
		SEDMLDocument doc = Libsedml.readDocument(new File("TestData/sedMLBIOM21.xml"));
		doc.getChangedModel("model1","original");
	}
	
	@Test
	public void testApplYModel2ChangesToBioModels21() throws Exception{
		String original=readOriginalModel(new File("TestData/BIOMD0000000021.xml"));
		assertFalse(original.contains("value=4.8"));
		SEDMLDocument doc = Libsedml.readDocument(new File("TestData/sedMLBIOM21.xml"));
		String changed=doc.getChangedModel("model2", original);
	        
		assertTrue(changed.contains("value=\"4.8\""));
		
	}

	private String readOriginalModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuffer sb=new StringBuffer();
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		return sb.toString();
	}

}
