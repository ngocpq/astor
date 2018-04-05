package fr.inria.astor.approaches.adqfix;

import java.util.List;

import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.setup.ProjectRepairFacade;

public class MinimalHittingSetAdequateFixEvaluator implements AdequateFixEvaluator {

	private ProgramVariant originalProgram;

	public MinimalHittingSetAdequateFixEvaluator(ProjectRepairFacade projFacade) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double computeAdequateFixLocationScore(ProgramVariant variant){
		//TODO: not implemented
		
		//Get modification points
		List<OperatorInstance> operations = variant.getOperations(variant.getLastModificationPointAnalyzed());
		for(OperatorInstance op:operations){
			ModificationPoint modifiedPoint = op.getModificationPoint();
			String className = modifiedPoint.getCtClass().getQualifiedName();
			int line = modifiedPoint.getCodeElement().getPosition().getLine();			
		}
		
		//compute score
		//adequateFixChecker.generateMinimalAdequateFixLocations(modifiedLocation);
		return 0;
	}

	@Override
	public void setOriginalVariant(ProgramVariant originVariant) {
		this.originalProgram = originVariant;		
	}
}
