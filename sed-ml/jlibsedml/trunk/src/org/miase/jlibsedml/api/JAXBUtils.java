package org.miase.jlibsedml.api;

import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
 /*
  * PAckage scoped utility for marshalling/ unmarshalling data, should not be API 
  */
 class JAXBUtils {
	 
	  static final String SEDML_SCHEMA="sedml-version-0-release-1.xsd";
	  static final String SBML_MATHML_SCHEMA= "sbml-mathml.xsd";
	  static final String GENERATED_PACKAGE="org.miase.jlibsedml.generated";

	static Unmarshaller createUnmarshaller( final List<SedMLError> errors) throws JAXBException, SAXException {
		JAXBContext context= JAXBContext.newInstance(GENERATED_PACKAGE);
		
		Unmarshaller unmarshaller = context.createUnmarshaller();

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		//order is important here, sedml depends on mathml
		Schema schema2 = factory.newSchema(JAXBUtils.class.getResource(SBML_MATHML_SCHEMA));
		Schema schema = factory.newSchema(JAXBUtils.class.getResource(SEDML_SCHEMA));
		unmarshaller.setSchema(schema);

		unmarshaller.setEventHandler(new ValidationEventHandler (){			
			public boolean handleEvent(ValidationEvent event) {
			    errors.add(new SedMLError(event.getLocator().getLineNumber(), event.getMessage(), 
			    		 event.getSeverity()));    
				
				return true;
			}
			
		});
		return unmarshaller;
	}
	
	static Marshaller createMarshaller( final List<SedMLError> errors) throws JAXBException, SAXException {
		JAXBContext context= JAXBContext.newInstance(GENERATED_PACKAGE);
		Marshaller marshaller = context.createMarshaller();

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema2 = factory.newSchema(JAXBUtils.class.getResource(SBML_MATHML_SCHEMA));
		Schema schema = factory.newSchema(JAXBUtils.class.getResource(SEDML_SCHEMA));
		marshaller.setSchema(schema);

		marshaller.setEventHandler(new ValidationEventHandler (){			
			public boolean handleEvent(ValidationEvent event) {
			    errors.add(new SedMLError(event.getLocator().getLineNumber(), event.getMessage(), 
			    		 event.getSeverity()));    
				
				return true;
			}
			
		});
		return marshaller;
	}

}
