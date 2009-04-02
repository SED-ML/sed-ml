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
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.miase.jlibsedml.generated.SedML;
import org.xml.sax.SAXException;
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
 * Provides an entry point to the SedML object model
 * To read a Sedml file,
 * <pre>
 *   SEDMLDocument doc = Libsedml.readDocument(myFile);
 *   // get errors 
 *   List<SedMLError> errors = doc.getErrors();
 *   
 *   
 *   // alternatively, if you're creating a sedml document from scratch, do 
 *   doc = LibsedML.createDocument();
 *   //get model
 *   Sedml model = doc.getSedMLModel();
 *   
 *   // apply changes to your model using the SedML dom
 *   
 *   // write document back to file
 *   doc.writeDocument(myFile);
 *   
 *   // to write a .miase archive file with the model file included: 
 *   List<IModelFile> models = new ArrayList<IModelFile>();
 *   models.add(new FileModelContent(myModelFile));
 *   ArchiveComponents comps = new ArchiveComponents(models, doc);
 *   
 *   byte [] zippedBytes = LibSedml.writeMiaseAchive(comps);
 *   
 *   // persist your zipped archive
 * </pre>
 * 
 * @author Richard Adams
 * 
 */
public class Libsedml {
    public static String  SEDML_ARCHIVE_NAME = "sedml.xml";
	/**
	 * @param fileName
	 *            A non-null, readable file of a Sedml xml file
	 * @return A non null {@link SEDMLDocument}
	 * @throws XMLException
	 *             for parsing errors
	 * @throws IOException
	 *             If the file cannot be read
	 */
	public static SEDMLDocument readDocument(File file) throws XMLException,
			IOException {
		SedML sedml = null;
		List<SedMLError> errors = new ArrayList<SedMLError>();
		try {
			sedml = loadDocument(new FileInputStream(file), errors);
		} catch (JAXBException e) {
			throw new XMLException("Error reading file", e);
		} catch (SAXException e) {
			throw new XMLException("Error reading file", e);
		}
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return new SEDMLDocument(sedml, errors);
	}

	/**
	 * Generates a {@link SEDMLDocument} from a String representation of a Sedml
	 * document
	 * 
	 * @param document
	 * @return
	 * @throws XMLException
	 * @throws IOException
	 */
	public static SEDMLDocument readDocument(String document)
			throws XMLException, IOException {
		SedML sedml = null;
		List<SedMLError> errors = new ArrayList<SedMLError>();
		ByteArrayInputStream bis = new ByteArrayInputStream(document.getBytes());
		try {
			sedml = loadDocument(bis, errors);
		} catch (JAXBException e) {
			throw new XMLException("Error reading file", e);
		} catch (SAXException e) {
			StringBuffer sb = new StringBuffer();
			sb.append(e.getMessage());
			if (e.getCause() != null) {
				sb.append(e.getCause().getMessage());
				for (StackTraceElement el : e.getStackTrace()) {
					sb.append(el.toString());
				}
			}
			throw new XMLException("Error reading file " + sb.toString(), e);
		}
		SemanticValidationManager.performSemanticValidation(sedml, errors);
		return new SEDMLDocument(sedml, errors);
	}

	/**
	 * Creates a new, empty SedML document
	 * 
	 * @return A non-null, empty {@link SEDMLDocument}. This document is not
	 *         free of errors after creation, as a valid document contains at least one task,
	 *          for example.
	 */
	public static SEDMLDocument createDocument() {
		return new SEDMLDocument();

	}

	/**
	 * Generates a zip archive of a Sedml document and its constituent model
	 * files. Within the archive, the SedML document will called 'sedml.xml'
	 * 
	 * @param doc
	 *            A non-null, {@link SEDMLDocument}
	 * @param modelFiles
	 * @return A byte[] of the zipped contents, or null
	 * @throws XMLException
	 *             if any aspect of zipping could not be performed.
	 */
	public static byte[] writeMiaseArchive(ArchiveComponents components)
			throws XMLException {
		ZipOutputStream out = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			out = new ZipOutputStream(baos);
			for (IModelContent modelFile : components.getModelFiles()) {
				out.putNextEntry(new ZipEntry(modelFile.getName()));
				byte[] data = modelFile.getContents().getBytes();
				out.write(data);
				
			}
			out.putNextEntry(new ZipEntry(SEDML_ARCHIVE_NAME));
			String sedmlString = components.getSedmlDocument()
					.writeDocumentToString();

			byte[] array = sedmlString.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(sedmlString
					.getBytes());
			int bytesRead;
			while ((bytesRead = bais.read(array)) != -1) {
				out.write(array, 0, bytesRead);
			}
			out.close();

		} catch (Exception e) {
			throw new XMLException("Error generating miase zip file", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {// ignore
			}
		}
		
			return baos.toByteArray();
		

	}

	/**
	 * Reads an archive, which should be in miase format - i.e., a zip archive
	 * @param archive An {@link InputStream} onto the archive
	 * @return A non-null {@link ArchiveComponents} objects.
	 * @throws XMLException
	 * @throws {@link IllegalArgumentException} if parameter File
	 *             is null, unreadable, or does not end in ".miase" or ".zip".
	 */
	public static ArchiveComponents readMiaseArchive(InputStream archive)
			throws XMLException {
		if (archive == null) {
			throw new IllegalArgumentException();

		}
		
		ZipInputStream zis = new ZipInputStream(archive);
		ZipEntry entry = null;
		
		int read;
		List<IModelContent> contents = new ArrayList<IModelContent>();
		SEDMLDocument doc = null;
		try {
			while ((entry = zis.getNextEntry()) != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte [] buf = new byte[4096];
				while ((read = zis.read(buf)) != -1) {
					baos.write(buf, 0, read);
				}
				if (!entry.getName().equals(SEDML_ARCHIVE_NAME)){
				   IModelContent imc = new BaseModelContent(baos.toString(), entry.getName());
				   contents.add(imc);
					
				} else {
					doc = readDocument(baos.toString());
				}
			}
			return new ArchiveComponents(contents, doc);
		} catch (Exception e) {
			throw new XMLException("Error reading archive", e);
		} finally{
			try {
				zis.close();
			} catch (IOException e) {}//ignore
		}
		
	}

	private static SedML loadDocument(InputStream is,
			final List<SedMLError> errors) throws JAXBException,
			FileNotFoundException, SAXException {

		Unmarshaller unmarshaller = JAXBUtils.createUnmarshaller(errors);

		try {
			SedML sedml = (SedML) unmarshaller.unmarshal(is);
			return sedml;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}

	}

}
