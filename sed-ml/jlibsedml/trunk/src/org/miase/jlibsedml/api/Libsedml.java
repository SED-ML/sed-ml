package org.miase.jlibsedml.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	 * @param fileName A non-null file Name
	 * @return A non null {@link SEDMLDocument}
	 * @throws XMLException for parsing errors
	 * @throws IOException If the file cannot be read
	 */
	public static SEDMLDocument readDocument (String  fileName) throws XMLException, IOException{
		SedML sedml = null;
		List<SedMLError> errors = new ArrayList<SedMLError>();
		try {
		sedml = loadDocument(fileName, errors);
		} catch (JAXBException e){
			throw new XMLException("Error reading file", e);
		}catch (SAXException e){
			throw new XMLException("Error reading file", e);
		} 
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return new SEDMLDocument(sedml, errors);
	}
	
	private static SedML loadDocument (String fileName ,final List<SedMLError> errors) throws JAXBException, FileNotFoundException, SAXException{

		Unmarshaller unmarshaller = JAXBUtils.createUnmarshaller( errors);
		FileInputStream fis =null;
		try {
			 fis =new FileInputStream(fileName);	
			SedML sedml = (SedML)unmarshaller.unmarshal(fis);
			
			return sedml;
		} finally{
			if(fis!=null)
				try {
					fis.close();
				} catch (IOException e) {}
		}
		
	}

}
