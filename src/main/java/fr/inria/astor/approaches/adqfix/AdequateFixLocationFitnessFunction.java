package fr.inria.astor.approaches.adqfix;

import java.util.List;

import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.loop.population.TestCaseFitnessFunction;

public class AdequateFixLocationFitnessFunction extends TestCaseFitnessFunction{
	
	AdequateFixEvaluator adequateFixChecker;
	double adqWeight = 0.2;
	@Override
	public double calculateFitnessValue(VariantValidationResult validationResult, ProgramVariant variant) {
		double originalScore = super.calculateFitnessValue(validationResult, variant);
		
		if (adequateFixChecker==null)
			return originalScore;
		
		double adqScore= adequateFixChecker.computeAdequateFixLocationScore(variant);
		
		double fitnessScore = (1-adqWeight)*originalScore +adqWeight*adqScore;
		return fitnessScore;
	}
	
	public void setAdequateFixLocationChecker(AdequateFixEvaluator adequateFixEvaluator){
		this.adequateFixChecker = adequateFixEvaluator;
	}
}
