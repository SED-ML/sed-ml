package org.miase.jlibsedml.api;
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
/*
 * This is used as an implementation class for holding model content when reading a .miase file.
 */
class BaseModelContent implements IModelContent {
	private String content, name;
    
    BaseModelContent(String content, String name) {
		super();
		this.content = content;
		this.name = name;
	}

	
	public String getContents() {
		return content;
	}

	public String getName() {
		return name;
	}

}
