package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

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

}
