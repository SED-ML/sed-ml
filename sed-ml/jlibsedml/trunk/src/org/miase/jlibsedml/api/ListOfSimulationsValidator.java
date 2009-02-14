package org.miase.jlibsedml.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.miase.jlibsedml.generated.ListOfSimulations;
import org.miase.jlibsedml.generated.UniformTimeCourse;
 /*Internal validator classm, not API
  * @author Richard Adams
  *
  */
 class ListOfSimulationsValidator {
	 
	static final String DUPLICATED_IDS="Duplicated IDs";
	List<SedMLError> validate(ListOfSimulations simList){
		  List<SedMLError> errors = new ArrayList<SedMLError>();
		  List<UniformTimeCourse> utcs = simList.getUniformTimeCourses();
		  Map<String, UniformTimeCourse> seen = new HashMap<String, UniformTimeCourse>();
		  for (UniformTimeCourse utc: utcs) {
			  errors.addAll(new UniformTimeCourseValidator().validate(utc));
			  if (seen.containsKey(utc.getId())){
				  errors.add(new SedMLError(0, DUPLICATED_IDS + " for " + utc.getId(), 0));
			  } else {
				  seen.put(utc.getId(), utc);
			  }
		  }
		  return errors;
	}
}
