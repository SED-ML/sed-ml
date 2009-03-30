package org.miase.jlibsedml.api;

import java.util.List;

/**
 * Encapsulates the contents of an archive file.
 * @author Richard Adams
 *
 */
public class ArchiveComponents {
	

	private List<IModelContent> modelFiles;
	private SEDMLDocument sedmlDoc;
	
	/**
	 * 
	 * @param modelFiles a non-null, but possible empty list of <code>IModelContent</code> objects.
	 * @param sedmlDoc A {@link SEDMLDocument}
	 * @throws IllegalArgumentException if either parameter is null.
	 */
	public ArchiveComponents(List<IModelContent> modelFiles, SEDMLDocument sedmlDoc) {
		super();
		Assert.checkNoNullArgs(modelFiles, sedmlDoc);
		this.modelFiles = modelFiles;
		this.sedmlDoc = sedmlDoc;
	}
	
	/**
	 * Gets the list of files held by this object. Modifying this List <em>WILL</em> modify the contents of the List.
	 * @return a <code>List</code> of model files, not null but may be empty
	 */
	public List<IModelContent> getModelFiles() {
		return modelFiles;
	}
	
	/**
	 * Removes a model content from this object.
	 * @param toRemove A Non-null {@link IModelContent} object.
	 * @return <code>true</code> if <code>toRemove</code> was removed.
	 */
	public boolean removeModelContent (IModelContent toRemove){
		return modelFiles.remove(toRemove);
	}
	/**
	 * Adds a model file to the list of files to be included in the archive.
	 * @param toAdd
	 * @return <code>true</code> if model file added.
	 */
	public boolean addModelContent (IModelContent toAdd){
		return modelFiles.add(toAdd);
	}
    /**
     * Gets the SEMLDocument
     * @return A {@link SEDMLDocument}, which will not be null.
     */
	public SEDMLDocument getSedmlDocument() {
		return sedmlDoc;
	}
	
	
	
	
	
	
	
	
}
