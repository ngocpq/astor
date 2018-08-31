package fr.inria.astor.approaches.adqfix;

import fr.inria.astor.approaches.adqfix.validate.AdequateEvaluationResult;
import fr.inria.astor.approaches.adqfix.validate.AdequateValidationResult;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.astor.core.validation.ProgramVariantValidator;
import fr.inria.astor.core.validation.processbased.ProcessValidator;

public class AdqFixValidator extends ProgramVariantValidator{
	ProcessValidator processValidator = new ProcessValidator();
	AdequateFixLocationStrategy<SuspiciousCode> fixLocator;
	@Override
	public VariantValidationResult validate(ProgramVariant mutatedVariant, ProjectRepairFacade projectFacade) {		
		if (fixLocator==null)
			return processValidator.validate(mutatedVariant, projectFacade);
		
		AdequateEvaluationResult adqResult = fixLocator.evaluateAdequate(mutatedVariant);
				
		VariantValidationResult result;
		if (adqResult.isAdequate()){
			TestCaseVariantValidationResult tcResult= processValidator.validate(mutatedVariant, projectFacade);
			result = new AdequateValidationResult(adqResult,tcResult);
		}else{			
			result = new AdequateValidationResult(adqResult);
		}
		return result;
	}
	public void setAdequateFixLocationChecker(AdequateFixLocationStrategy<SuspiciousCode> adequateFixLocationStrategy) {
		this.fixLocator = adequateFixLocationStrategy;
	}
}
