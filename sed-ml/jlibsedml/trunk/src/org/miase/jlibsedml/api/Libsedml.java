package org.miase.jlibsedml.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	 * @param fileName A non-null, readable file 
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
	 * Generates a {@link SEDMLDocument} from a String representation of a document
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
	 * @return A non-null, empty document. 
	 */
	public static SEDMLDocument createDocument() {
		return new SEDMLDocument();
		
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
