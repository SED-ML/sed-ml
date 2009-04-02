package org.miase.jlibsedml.api;

import java.util.ArrayList;
import java.util.List;

import org.miase.jlibsedml.generated.UniformTimeCourse;
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
 * Internal validation class, not API.
 */
class UniformTimeCourseValidator  {
	static String START_AFTER_END ="Start of time course is before end of time course";
	static String INITIAL_AFTER_START ="Initial time  is after start of output start time";
	List<SedMLError> validate(UniformTimeCourse tc){
	  List<SedMLError> errors = new ArrayList<SedMLError>();
	  
	  if(tc.getOutputStartTime() >= tc.getOutputEndTime()){
		  SedMLError err = new SedMLError(0, START_AFTER_END, 1); 
		  
		  errors.add(err);
	  }
	  if(tc.getInitialTime() > tc.getOutputStartTime()){
		  SedMLError err= new SedMLError(0, INITIAL_AFTER_START,1);
		  err.setNode(tc);
		  errors.add(err);
	  }
	  
	  
	  
	  return errors;
	}
	



}
