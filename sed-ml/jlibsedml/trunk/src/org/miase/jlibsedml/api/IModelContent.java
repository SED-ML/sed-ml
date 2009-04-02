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
/**
 * Encapsulates a model referenced in the sedml file
 * @author Richard Adams
 *
 */
public interface IModelContent {
  /**
   * A name for the model; typically this will be used to identify the model in the archive
   * This method should return the same name as is specified in 'source' field of the sedml model definition.
   * @return A non-null, non-empty string
   */
  String getName();
  
  /**
   * Gets the contents of a model as a String
   * @return A non-null, non-empty string
   */
  String getContents();
}
