package org.miase.jlibsedml.api;

import java.util.ArrayList;
import java.util.List;



import org.miase.jlibsedml.generated.UniformTimeCourse;

class UniformTimeCourseValidator  {
	static String START_AFTER_END ="Start of time course is before end of time course";
	static String INITIAL_AFTER_START ="Initial time  is after start of output start time";
	List<SedMLError> validate(UniformTimeCourse tc){
	  List<SedMLError> errors = new ArrayList<SedMLError>();
	  
	  if(tc.getOutputStartTime() >= tc.getOutputEndTime()){
		  errors.add(new SedMLError(0, START_AFTER_END, 1));
	  }
	  if(tc.getInitialTime() > tc.getOutputStartTime()){
		  errors.add(new SedMLError(0, INITIAL_AFTER_START,1));
	  }
	  
	  return errors;
	}

}
