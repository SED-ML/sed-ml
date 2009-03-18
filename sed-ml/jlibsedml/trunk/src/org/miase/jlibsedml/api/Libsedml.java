package org.miase.jlibsedml.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.miase.jlibsedml.generated.SedML;
import org.xml.sax.SAXException;


/**
 * Provides an entry point to the SedML object model
 * @author Richard Adams
 *
 */
public class Libsedml {
	
	/**
	 * 
	 * @param fileName A non-null, readable file of a Sedml xml file
	 * @return A non null {@link SEDMLDocument}
	 * @throws XMLException for parsing errors
	 * @throws IOException If the file cannot be read
	 */
	public static SEDMLDocument readDocument (File  file) throws XMLException, IOException{
		SedML sedml = null;
		List<SedMLError> errors = new ArrayList<SedMLError>();
		try {
		sedml = loadDocument(new FileInputStream(file), errors);
		} catch (JAXBException e){
			throw new XMLException("Error reading file", e);
		}catch (SAXException e){
			throw new XMLException("Error reading file", e);
		} 
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return new SEDMLDocument(sedml, errors);
	}
	
	/**
	 * Generates a {@link SEDMLDocument} from a String representation of a Sedml document
	 * @param document
	 * @return
	 * @throws XMLException
	 * @throws IOException
	 */
	public static SEDMLDocument readDocument (String document)throws XMLException, IOException {
		SedML sedml = null;
		List<SedMLError> errors = new ArrayList<SedMLError>();
		ByteArrayInputStream bis = new ByteArrayInputStream(document.getBytes());
		try {
			sedml = loadDocument(bis, errors);
			} catch (JAXBException e){
				throw new XMLException("Error reading file", e);
			}catch (SAXException e){
				StringBuffer sb = new StringBuffer();
				sb.append(e.getMessage());
				if(e.getCause()!=null){
					sb.append(e.getCause().getMessage());
					for (StackTraceElement el : e.getStackTrace()){
						sb.append(el.toString());
					}
				}
				throw new XMLException("Error reading file " +sb.toString(), e);
			} 
			SemanticValidationManager.performSemanticValidation(sedml, errors);
			return new SEDMLDocument(sedml, errors);
	}
	/**
	 * Creates a new, empty SedML document
	 * @return A non-null, empty {@link SEDMLDocument}. This document is not free of errors after creation, as a 
	 * valid document 
	 */
	public static SEDMLDocument createDocument() {
		return new SEDMLDocument();
		
	}
	
	public static byte[] writeMiaseArchive(SEDMLDocument doc,
			List<File> modelFiles) throws XMLException {
		ZipOutputStream out = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			out = new ZipOutputStream(baos);
			for (File modelFile:modelFiles) {
				out.putNextEntry(new ZipEntry(modelFile.getName()));
				byte[] buf = new byte[4096];
				FileInputStream fis = new FileInputStream(modelFile);
				int bytesRead;
				while ((bytesRead = fis.read(buf)) != -1) {
					out.write(buf, 0, bytesRead);
				}
				fis.close();
			}
			out.putNextEntry(new ZipEntry("sedml.xml"));
			String sedmlString = doc.writeDocumentToString();

			byte[] array = sedmlString.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					sedmlString.getBytes());
			int bytesRead;
			while ((bytesRead = bais.read(array)) != -1) {
				out.write(array, 0, bytesRead);
			}
			out.close();

		} catch (Exception e) {
			throw new XMLException ("Error generating miase zip file", e);
		}  finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		if (baos!=null)
			return baos.toByteArray();
		else {
			return null;
		}
		
	}
	
	private static SedML loadDocument (InputStream is ,final List<SedMLError> errors) throws JAXBException, FileNotFoundException, SAXException{

		Unmarshaller unmarshaller = JAXBUtils.createUnmarshaller( errors);
	
		try {	
			SedML sedml = (SedML)unmarshaller.unmarshal(is);
			return sedml;
		} finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {}
		}
		
	}

}
