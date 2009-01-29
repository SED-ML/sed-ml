package org.miase.jlibsedml.api;

import java.util.ArrayList;
import java.util.List;

import org.miase.jlibsedml.generated.UniformTimeCourse;

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
