package fr.inria.astor.approaches.adqfix;

import java.util.List;

import fr.inria.astor.approaches.adqfix.model.MinimalCorrectionLocationSet;
import fr.inria.astor.approaches.jgenprog.operators.InsertAfterOp;
import fr.inria.astor.approaches.jgenprog.operators.InsertBeforeOp;
import fr.inria.astor.approaches.jgenprog.operators.RemoveOp;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.loop.population.TestCaseFitnessFunction;
import fr.inria.astor.core.loop.spaces.operators.AstorOperator;

public class AdequateFixLocationFitnessFunction extends TestCaseFitnessFunction{	
	//AdequateFixEvaluator adequateFixEvaluator;
	AdequateFixLocationStrategy fixLocator;
	double adqWeight = 0.2; //TODO: read from config
	@Override
	public double calculateFitnessValue(VariantValidationResult validationResult, ProgramVariant variant) {
		//original fitness function result
		double originalScore = super.calculateFitnessValue(validationResult, variant);				
		
		//compute adequate score for the modification
		double adqScore= computeAdequateModificationScore(variant);
		
		//new fitness result
		return (1-adqWeight)*originalScore +adqWeight*adqScore;		
	}
	
	public void setAdequateFixLocationChecker(AdequateFixLocationStrategy fixLocator){
		this.fixLocator = fixLocator;
	}
	
	public double computeAdequateModificationScore(ProgramVariant variant){
		//Get modification points
		List<OperatorInstance> operations = variant.getOperations(variant.getLastModificationPointAnalyzed());
		for(OperatorInstance opInstance:operations){
			AstorOperator astorOp = opInstance.getOperationApplied();
			if (astorOp instanceof InsertAfterOp ){
				
			}else if (astorOp instanceof InsertBeforeOp ){
				
			}else if (astorOp instanceof RemoveOp ){
				
			}
			
			ModificationPoint modifiedPoint = opInstance.getModificationPoint();
			String className = modifiedPoint.getCtClass().getQualifiedName();
			int line = modifiedPoint.getCodeElement().getPosition().getLine();			
		}
		
		//compute score
		List<MinimalCorrectionLocationSet> adequateFix = fixLocator.getAllAdequateFixes();
		
		return 0;
	}
}
