package org.miase.jlibsedml.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
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
		
		
		
		File math = loadSchema(SBML_MATHML_SCHEMA);
		File sed = loadSchema(SEDML_SCHEMA);
	
		StreamSource s1= new StreamSource(math);
		StreamSource s2= new StreamSource(sed);
	
		
		Schema schema = factory.newSchema(new Source[]{s1,s2});
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

	private static File loadSchema(final String schema) {
		InputStream is2 = JAXBUtils.class.getResourceAsStream(schema);
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		   FileOutputStream fos2 =null;
		   File sed = null;
		try{
		byte [] buf = new byte[1024];
		
		int read = 0;
		while ( (read =is2.read(buf))!= -1 ){
			baos2.write(buf, 0, read);
		}
	    sed = File.createTempFile("xsd", "xsd");
        fos2 = new FileOutputStream(sed);
        fos2.write(baos2.toByteArray());
        fos2.flush();
		
		}catch(IOException ie ){
			System.out.println(ie.getMessage());
		} finally {
			try {
				if (fos2!=null){fos2.close();}
				baos2.close();
				is2.close();
			} catch (IOException e) {}
			
		}
		return sed;
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
