package org.miase.jlibsedml.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
         * Gets the contents of the this object's file as a String.
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
