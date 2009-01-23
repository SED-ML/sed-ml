package org.miase.jlibsedml.api;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Sedml12TEst {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testValidSedMLDocHasNoErrors() throws XMLException, IOException {
		SEDMLDocument doc = Libsedml.readDocument("TestData/sedMLBIOM12.xml");
		List<SedMLError> errors = doc.getErrors();
		for (SedMLError error: errors) {
			System.out.println(error.getMessage());
		}
		assertEquals(0, errors.size());
		
	}

	@After
	public void tearDown() throws Exception {
	}

}
