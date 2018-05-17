package fr.inria.astor.approaches.adqfix;

import java.util.ArrayList;
import java.util.List;

import fr.inria.astor.approaches.adqfix.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.uti.ProgramVariant_Helper;
import fr.inria.astor.approaches.jgenprog.operators.InsertAfterOp;
import fr.inria.astor.approaches.jgenprog.operators.InsertBeforeOp;
import fr.inria.astor.approaches.jgenprog.operators.RemoveOp;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.loop.population.TestCaseFitnessFunction;
import fr.inria.astor.core.loop.spaces.operators.AstorOperator;
import fr.inria.astor.util.PatchDiffCalculator;

public class AdequateFixLocationFitnessFunction extends TestCaseFitnessFunction{	
	AdequateFixLocationStrategy<SuspiciousCode> fixLocator;
	double adqWeight = 0.2; //TODO: read from configuration file
	
	@Override
	public double calculateFitnessValue(VariantValidationResult validationResult,
			ProgramVariant variant) {
		double originalScore = super.calculateFitnessValue(validationResult, variant);				
		
		double adequateModifyScore= computeAdequateModificationScore(variant);
		
		//new fitness result
		return (1-adqWeight)*originalScore +adqWeight*adequateModifyScore;		
	}
	
	public double computeAdequateModificationScore1(ProgramVariant variant){
		//TODO: ---- get modified statements ------ 
		PatchDiffCalculator cdiff = new PatchDiffCalculator();		
		//cdiff.getDiff(originalFile, newFile, fileName)
		
		//get minimal fix location
		 List<Diagnosis<SuspiciousCode>> minAdequateFixes = fixLocator.getAllAdequateFixes();
		
		//TODO: compute adequate score
		
		return 0;
	}
	
	public void setAdequateFixLocationChecker(AdequateFixLocationStrategy fixLocator){
		this.fixLocator = fixLocator;
	}
	
	
	public double computeAdequateModificationScore(ProgramVariant variant){		
		if (this.fixLocator==null)
			return 0;
		
		//modification points
		List<SuspiciousCode> modifiedLocs =ProgramVariant_Helper.getModifiedCodes(variant);
		if (modifiedLocs.isEmpty())
			return 0;
		
		//minimal adequate fix location
		List<Diagnosis<SuspiciousCode>> minAdqFixLocs = fixLocator.getAllAdequateFixes();
		
		//TODO: evaluate "modifiedLocs" based on "minAdqFixLocs"		
		
		return 0;
	}
}
