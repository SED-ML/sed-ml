package org.miase.jlibsedml.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.miase.jlibsedml.generated.ListOfSimulations;
import org.miase.jlibsedml.generated.SedML;
import org.xml.sax.SAXException;

public class SedMLGlobalValidator {
	private List<SedMLError> errors = new ArrayList<SedMLError>();
	public static final String SEDML_SCHEMA="sedml-version-0-release-1.xsd";
	public static final String SBML_MATHML_SCHEMA= "sbml-mathml.xsd";
	
	/**
	 * Clears error log and revalidates, no state is kept between 
	 *  invocations
	 * @param fileName
	 * @return A <code>List</code> of {@link SedMLError}
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public List<SedMLError> validate (String fileName) throws FileNotFoundException, JAXBException, SAXException {
	    errors = new ArrayList<SedMLError>();
	    
	    SedML doc = loadDocument(fileName);
	    
	    validateListOfSimulations(doc.getListOfSimulations());
		
		return errors;
	}
	
	private void validateListOfSimulations(ListOfSimulations listOfSimulations) {
		
		
	}

	SedML loadDocument (String fileName) throws JAXBException, FileNotFoundException, SAXException{
		JAXBContext context= JAXBContext.newInstance("org.miase.jlibsedml.generated");
		Unmarshaller unmarshaller = context.createUnmarshaller();

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema2 = factory.newSchema(getClass().getResource(SBML_MATHML_SCHEMA));
		Schema schema = factory.newSchema(getClass().getResource(SEDML_SCHEMA));
		unmarshaller.setSchema(schema);

		unmarshaller.setEventHandler(new ValidationEventHandler (){			
			public boolean handleEvent(ValidationEvent event) {
			    errors.add(new SedMLError(event.getLocator().getLineNumber(), event.getMessage(), 
			    		 event.getSeverity()));    
				
				return true;
			}
			
		});
		SedML sedml = (SedML)unmarshaller.unmarshal(new FileInputStream(fileName));
		
		return sedml;
	}


}
