package org.miase.jlibsedml.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Encapsulates a file-based model. This class can be used to populate a list of {@link IModelContent}
 * required for an ArchiveComponent that can be written.
 * @author Richard Adams
 *
 */
public class FileModelContent implements IModelContent {

	private File file; 
	/**
	 * @param file A non-null, readable model file.
	 */
	public FileModelContent(File file) {
		super();    
		Assert.checkNoNullArgs(file);
			
			this.file = file;
		}

		
        /**
         * Gets the file contents as a String
         * @return The contents, or an empty String if the file could not be read.
         */
		public String getContents() {
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			try{
			br  = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = br.readLine()) != null){
				sb.append(line);
			}
			
			}catch(Exception e){
			  return "";	
			}
			return sb.toString();	
		}
			
		
        /**
         * Gets the model name, which delegates to  the file's name.
         */
		public String getName() {
			return file.getName();
		}

}
