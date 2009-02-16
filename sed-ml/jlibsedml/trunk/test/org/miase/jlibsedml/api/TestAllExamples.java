package org.miase.jlibsedml.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests all .ml files in SedML 'Examples' directory
 * 
 * @author Richard Adams
 * 
 */
public class TestAllExamples {

	final File TEST_DIR = new File("TestData/");
	List<File> filtered = new ArrayList<File>();

	@Before
	public void setUp() throws Exception {
		File[] allfiles = TEST_DIR.listFiles();

		for (File f : allfiles) {
			if (f.getName().startsWith("sed")
					&& f.getName().matches(".+\\.xml$")) {

				filtered.add(f);
			}
		}
	}

	@Test
	public void testValidSedMLDocHasNoErrors() throws XMLException, IOException {

		for (File testFile : filtered) {

			SEDMLDocument doc = Libsedml.readDocument(testFile);
			List<SedMLError> errors = doc.getErrors();
			for (SedMLError error : errors) {
				System.out.println(error.getMessage());
			}
			assertEquals(0, errors.size());
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
