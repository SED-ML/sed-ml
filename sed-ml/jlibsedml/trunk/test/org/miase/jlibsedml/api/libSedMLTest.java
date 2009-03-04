package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class libSedMLTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateDocumentIsValid() {
		SEDMLDocument doc = Libsedml.createDocument();
		assertNotNull(doc);
		assertFalse(doc.hasErrors());
	}
	
	@Test
	public void testEmptyDocumentCanBeSaved() throws Exception{
		SEDMLDocument doc = Libsedml.createDocument();
		File f = File.createTempFile("sedml", "xml");
		doc.writeDocument(f);
		SEDMLDocument doc2 = Libsedml.readDocument(f);
		assertNotNull(doc2);
		
	}

}
