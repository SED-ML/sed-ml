package org.miase.jlibsedml.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.miase.jlibsedml.generated.ChangeAttribute;
import org.miase.jlibsedml.generated.ListOfChanges;
import org.miase.jlibsedml.generated.ListOfDataGenerators;
import org.miase.jlibsedml.generated.ListOfModels;
import org.miase.jlibsedml.generated.ListOfOutputs;
import org.miase.jlibsedml.generated.ListOfSimulations;
import org.miase.jlibsedml.generated.ListOfTasks;
import org.miase.jlibsedml.generated.Model;
import org.miase.jlibsedml.generated.SedML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/*
 *    Copyright 2009 Richard Adams

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
/**
 * Encapsulates a {@link SedML} model and provides additional validation
 * services.
 * 
 * @author Richard Adams
 * 
 */
public class SEDMLDocument {
	static final String SBML_XPATH_PREFIX = "sbml";

	private List<SedMLError> errors = new ArrayList<SedMLError>();

	private SedML sedml;

	/*
	 * No parameter can be null, errors can be empty list
	 * 
	 * @throws IllegalArgumentException if any arg is null.
	 */
	SEDMLDocument(SedML model, List<SedMLError> errors) {
		Assert.checkNoNullArgs(model, errors);
		this.sedml = model;
		this.errors = errors;
	}

	/**
	 * Constructs a document and a SedML model.
	 */
	public SEDMLDocument() {
		this.sedml = new SedML();
		ListOfSimulations los = new ListOfSimulations();
		sedml.setListOfSimulations(los);
		ListOfModels lom = new ListOfModels();
		for (Model m: lom.getModels()){
			m.setListOfChanges(new ListOfChanges());
		}
		sedml.setListOfModels(lom);
		ListOfDataGenerators lodg = new ListOfDataGenerators();
		sedml.setListOfDataGenerators(lodg);
		ListOfOutputs loo = new ListOfOutputs();
		sedml.setListOfOutputs(loo);
		ListOfTasks lot = new ListOfTasks();
		sedml.setListOfTasks(lot);
	}

	/**
	 * Gets a readonly list of this document's errors.
	 * @return An unmodifiable (read-only), non-null list of this document's
	 *         errors
	 */
	public List<SedMLError> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	/**
	 * A boolean test as to whether the model referenced by this document has errors or not.
	 * @return <code>true</code> if this document has at least one validation
	 *         error
	 */
	boolean hasErrors() {
		return errors.size() > 0;
	}

	/**
	 * Gets the Sedml model referenced contained in this document.
	 * 
	 * @return A non-null {@link SedML} object
	 */
	public SedML getSedMLModel() {
		return sedml;
	}

	/**
	 * Revalidates this document.
	 * 
	 * @return An unmodifiable, non-null <code>List</code> of errors
	 * @throws XMLException
	 */
	List<SedMLError> revalidate() throws XMLException {
		Marshaller m = null;

		try {
			m = JAXBUtils.createMarshaller(errors);

			// We're just marshalling to revalidate, don't want to actually
			// write the file
			m.marshal(sedml, new ByteArrayOutputStream());
		} catch (Exception e) {
			throw new XMLException("Error validating XML", e);
		}
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return getErrors();
	}

	public String toString() {
		return "SEdmlD ocument for " + sedml.getNotes();
	}

	/**
	 * Writes out a document to file . This operation will write valid and
	 * invalid documents, to check a adocument is valid, call hasErrors() before
	 * writing document.
	 * 
	 * @param file
	 *            A {@link File} that can be written to.
	 * @throws XMLException
	 *             if model cannot be marshalled properly
	 * @throws IllegalArgumentException
	 *             if <code>file</code> param is null
	 */
	public void writeDocument(File file) throws XMLException {
		Assert.checkNoNullArgs(file);
		Marshaller m = null;

		try {
			m = JAXBUtils.createMarshaller(errors);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					new PreferredMapper());

			m.marshal(sedml, new FileOutputStream(file));
		} catch (Exception e) {
			throw new XMLException("Error validating XML", e);
		}
	}

	/**
	 * Writes the document contents to formatted XML format, and returns it as a
	 * <code>String</code>.
	 * 
	 * @return A <code>String</code>
	 * @throws XMLException
	 *             if errors occur during XML generation.
	 */
	public String writeDocumentToString() throws XMLException {
		Marshaller m = null;

		try {
			m = JAXBUtils.createMarshaller(errors);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					new PreferredMapper());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			m.marshal(sedml, baos);
			return new String(baos.toByteArray());
		} catch (Exception e) {
			throw new XMLException("Error validating XML", e);
		}

	}

	private static class PreferredMapper extends NamespacePrefixMapper {
		@Override
		public String getPreferredPrefix(String namespaceUri,
				String suggestion, boolean requirePrefix) {
			if (namespaceUri.equals("http://www.biomodels.net/sed-ml")) {
				return "sedml";
			} else {
				return "math";
			}
		}
	}

	/**
	 * Gets a model variant, by applying whatever changes are defined in a model's List of changes.
	 * At present, only {@link ChangeAttribute} changes are supported.
	 * @param model_ID
	 *            The id of the model, which is a model defined in the ListOfModels element.
	 * @return A String representation of the changed model. The original model will be unchanged by 
	 * @throws IllegalArgumentException
	 *             if modelID not defined in ListOfModels
	 */
	public String getChangedModel(String model_ID, final String originalModel) throws XPathExpressionException, XMLException{
		checkModelIdExists(model_ID);
		String xmlString="";
		List<Model> modelRefs = sedml.getListOfModels().getModels();
		for (Model model : modelRefs) {
			if (model.getId().equals(model_ID)) {
				if(model.getListOfChanges()==null){
					continue;
				}
				List<ChangeAttribute> changes = model.getListOfChanges()
						.getChangeAttributes();
                try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setNamespaceAware(true); // never forget this!
				DocumentBuilder builder = factory.newDocumentBuilder();

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				Writer out = new OutputStreamWriter(bos, "UTF8");
				out.write(originalModel);
				out.close();
				bos.close();

				Document doc = builder.parse(new ByteArrayInputStream(bos
						.toByteArray()));

				XPathFactory xpf = XPathFactory.newInstance();
				XPath xpath = xpf.newXPath();
				xpath.setNamespaceContext(new PersonalNamespaceContext());
				for (ChangeAttribute change : changes) {
					XPathExpression expr;
					
						expr = xpath.compile(change.getTarget());

						Object result = expr.evaluate(doc,
								XPathConstants.NODESET);
						NodeList nodes = (NodeList) result;
						for (int i = 0; i < nodes.getLength(); i++) {
							nodes.item(i).setNodeValue(change.getNewValue());
						}
					
				}

				Transformer transformer = TransformerFactory.newInstance()
						.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				// initialize StreamResult with File object to save to file
				StreamResult result2 = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result2);

				xmlString = result2.getWriter().toString();
                }catch(Exception e){
                	throw new XMLException("Error generating new model" + e.getMessage(), e);
                }
				return xmlString;
			}
		}

		return "";

	}

	private class PersonalNamespaceContext implements NamespaceContext {

		public String getNamespaceURI(String prefix) {
			if (prefix == null)
				throw new NullPointerException("Null prefix");
			else if (SBML_XPATH_PREFIX.equals(prefix))
				return "http://www.sbml.org/sbml/level2";
			else if ("xml".equals(prefix))
				return XMLConstants.XML_NS_URI;
			return XMLConstants.NULL_NS_URI;
		}

		// This method isn't necessary for XPath processing.
		public String getPrefix(String uri) {
			throw new UnsupportedOperationException();
		}

		// This method isn't necessary for XPath processing either.
		public Iterator getPrefixes(String uri) {
			throw new UnsupportedOperationException();
		}

	}

	private Map<String, Model> getAllModelIDs(List<Model> modelRefs) {
		Map<String, Model> rc = new HashMap<String, Model>();
		for (Model m : modelRefs) {
			rc.put(m.getId(), m);
		}
		return rc;
	}

	private void checkModelIdExists(String model_ID) {
		List<Model> modelRefs = sedml.getListOfModels().getModels();
		boolean found = false;
		for (Model model : modelRefs) {
			if (model.getId().equals(model_ID)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException(
					"supplied model ID does not exist");
		}
	}

}
