package org.miase.jlibsedml.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.Marshaller;

import org.miase.jlibsedml.generated.ListOfChanges;
import org.miase.jlibsedml.generated.ListOfDataGenerators;
import org.miase.jlibsedml.generated.ListOfModels;
import org.miase.jlibsedml.generated.ListOfOutputs;
import org.miase.jlibsedml.generated.ListOfSimulations;
import org.miase.jlibsedml.generated.ListOfTasks;
import org.miase.jlibsedml.generated.SedML;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/**
 * Encapsulates a {@link SedML} model and provides additional validation services
 * @author Richard Adams
 *
 */
public class SEDMLDocument {
	
	private List<SedMLError> errors = new ArrayList<SedMLError>();
	
	private SedML sedml;
	 /*
	  * No parameter can be null, errors can be empty list
	  * @throws IllegalArgumentException if any arg is null.
	  */
	 SEDMLDocument(SedML model, List<SedMLError> errors) {
		 Assert.checkNoNullArgs(model, errors);
		this.sedml=model;
		this.errors=errors;
	}
	 
	public SEDMLDocument() {
		this.sedml = new SedML();
		ListOfSimulations los = new ListOfSimulations();
		sedml.setListOfSimulations(los);
		ListOfModels lom = new ListOfModels();
		sedml.setListOfModels(lom);
		ListOfDataGenerators lodg = new ListOfDataGenerators();
		sedml.setListOfDataGenerators(lodg);
		ListOfOutputs loo = new ListOfOutputs();
		sedml.setListOfOutputs(loo);
		ListOfTasks lot = new ListOfTasks();
		sedml.setListOfTasks(lot);
	} 
	/**
	 * 
	 * @return An unmodifiable (read-only), non-null list of this document's errors
	 */
	public List<SedMLError> getErrors () {
		return Collections.unmodifiableList(errors);
	}
	
	/**
	 * @return <code>true</code> if this document has at least one validation error
	 */
	boolean hasErrors (){
		return errors.size() >0;
	}
	
	 /**
	  * @return A non-null {@link SedML} object
	  */
	public SedML getSedMLModel () {
		return sedml;
	}
	/**
	 *  Revalidates this document
	 * @return An unmodifiable, non-null <code>List</code> of errors
	 * @throws XMLException
	 */
	List<SedMLError> revalidate () throws XMLException {
		Marshaller m = null;
	
		try {
		 m = JAXBUtils.createMarshaller( errors);
		 
	//	 We're just marshalling to revalidate, don't want to actually write the file
		 m.marshal(sedml, new ByteArrayOutputStream());
		}catch(Exception e) {
			throw new XMLException("Error validating XML" , e);
		}
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return getErrors();
	}
	
	public String toString (){
		return "SEdmlD ocument for " + sedml.getNotes();
	}
	
	/**
	 * Writes out a document to file . This operation will write valid and
	 * invalid documents, to check  a adocument is valid, call hasErrors()
	 * before writing document.
	 * @param file A {@link File} that can be written to.
	 * @throws XMLException if model cannot be marshalled properly
	 * @throws IllegalArgumentException if <code>file</code> param is null
	 */
	public void writeDocument (File file) throws XMLException {
		Assert.checkNoNullArgs(file);
		Marshaller m = null;
		
		try {
		 m = JAXBUtils.createMarshaller( errors);
	     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	     m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new PreferredMapper());

		 m.marshal(sedml, new FileOutputStream(file));
		}catch(Exception e) {
			throw new XMLException("Error validating XML" , e);
		}
	}
	
	private static class PreferredMapper extends NamespacePrefixMapper {
        @Override
        public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
           if(namespaceUri.equals("http://www.biomodels.net/sed-ml")){
        	   return "sedml";
           } else {
        	   return "math";
           }
        }
    }

	
	

}
