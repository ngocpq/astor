package fr.inria.astor.approaches.adqfix.validate;

import java.util.List;

public class AdequateEvaluationResult {
	final List<String> affectedTests;
	final List<String> unaffectedTests;
	public AdequateEvaluationResult(List<String> affectedTests,List<String> unaffectedTests){
		this.affectedTests = affectedTests;
		this.unaffectedTests = unaffectedTests;
	}
	
	public boolean isAdequate(){
		return this.unaffectedTests==null || this.unaffectedTests.isEmpty();
	}

	public List<String> getAaffectedTests() {
		return this.affectedTests;
	}
	public List<String> getUnaffectedTests() {
		return this.unaffectedTests;
	}
	
	public int getAaffectedTestsCount() {
		if (this.affectedTests==null)
			return 0;
		return this.affectedTests.size();
	}
	public int getUnaffectedTestsCount() {
		if (this.unaffectedTests==null)
			return 0;
		return this.unaffectedTests.size();
	}
	
	@Override
	public String toString() {
		return this.isAdequate()?"adequate":"inadequate"+"|"+getAaffectedTestsCount()+"|"+getUnaffectedTestsCount();
	}
}
