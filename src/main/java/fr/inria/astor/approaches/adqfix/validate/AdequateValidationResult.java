package fr.inria.astor.approaches.adqfix.validate;

import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.validation.results.TestResult;

public class AdequateValidationResult implements VariantValidationResult {

	private AdequateEvaluationResult adequateEvaluationResult;
	private TestCaseVariantValidationResult testCaseValidationResult;

	public AdequateValidationResult(AdequateEvaluationResult adqResult, TestCaseVariantValidationResult tcResult) {
		this.adequateEvaluationResult = adqResult;
		this.testCaseValidationResult = tcResult;
	}

	public AdequateValidationResult(AdequateEvaluationResult adqResult) {
		this(adqResult,null);
	}

	@Override
	public boolean isSuccessful() {
		if (!this.adequateEvaluationResult.isAdequate())
			return false;		
		if (this.testCaseValidationResult==null)
			return false;
		return this.testCaseValidationResult.isSuccessful();
	}
	
	public AdequateEvaluationResult getAdequateValuation(){
		return this.adequateEvaluationResult;
	}
	public TestCaseVariantValidationResult getTestCaseValuation(){
		return this.testCaseValidationResult;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.adequateEvaluationResult.isAdequate()){
			sb.append("adequate|");
			if (this.testCaseValidationResult==null)
				sb.append("noTestResult");
			else
				sb.append(this.testCaseValidationResult.toString());
		}else{
			sb.append("inadequate|");
			sb.append(this.adequateEvaluationResult.getAaffectedTestsCount()+"|"+this.adequateEvaluationResult.getUnaffectedTestsCount());
		}
		return sb.toString();
	}	
}
