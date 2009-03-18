package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class libSedMLTest {
    static final File TEST_ARCHIVE=new File ("TestData/sedml.zip");
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
	public void testCreateMiaseArchive() throws Exception{
		byte[] b = createMiaserrchive();
		assertTrue(b.length>0);
		System.out.println(b.length);
	}

	private byte[] createMiaserrchive() throws XMLException {
		SEDMLDocument doc = Libsedml.createDocument();
		File f = new File("TestData/BIOMD0000000012.xml");
		byte [] b = Libsedml.writeMiaseArchive(new ArchiveComponents(
				         Arrays.asList(
				        		  new IModelContent []{new FileModelContent(f)}), doc));
		return b;
	}
	
	@Test
	public void testReadMiaseArchive() throws Exception{
		ArchiveComponents ac = Libsedml.readArchive(new FileInputStream(TEST_ARCHIVE));
		assertNotNull(ac);
		assertNotNull(ac.getSedmlDocument());
		assertEquals("sbml", ac.getSedmlDocument().getSedMLModel()
				             .getListOfModels().getModels().get(0).getType());
		assertEquals(1, ac.getModelFiles().size());
		assertEquals("CopasiModel.sbml", ac.getModelFiles().get(0).getName());	
	}
	
	
	@Test
	public void testCreateWriteReadRoundTrip() throws Exception{
		byte [] miase = createMiaserrchive();
		File tmp = File.createTempFile("miase", "miase");
		
		FileOutputStream fos = new FileOutputStream(tmp);
		fos.write(miase);
		fos.flush();
		fos.close();
		ArchiveComponents ac = Libsedml.readArchive(new FileInputStream(tmp));
		assertEquals("BIOMD0000000012.xml", ac.getModelFiles().get(0).getName());
		tmp.deleteOnExit();
	}
	
	@Test
	public void testEmptyDocumentCanBeSaved() throws Exception{
		SEDMLDocument doc = Libsedml.createDocument();
		File f = new File("sedml.xml");
		doc.writeDocument(f);
		SEDMLDocument doc2 = Libsedml.readDocument(f);
		assertNotNull(doc2);
		
	}
	
	
		
	

}
