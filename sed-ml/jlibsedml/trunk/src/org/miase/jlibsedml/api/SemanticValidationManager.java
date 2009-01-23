package org.miase.jlibsedml.api;

import java.util.List;

import org.miase.jlibsedml.generated.SedML;

 class SemanticValidationManager {
	
	static void performSemanticValidation (SedML model, final List<SedMLError> errors) {
		errors.addAll(new ListOfSimulationsValidator().validate(model.getListOfSimulations()));
		
	}

}
