package fr.inria.astor.approaches.adqfix.uti;

import java.util.ArrayList;
import java.util.List;

import fr.inria.astor.approaches.jgenprog.operators.InsertAfterOp;
import fr.inria.astor.approaches.jgenprog.operators.InsertBeforeOp;
import fr.inria.astor.approaches.jgenprog.operators.RemoveOp;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.loop.spaces.operators.AstorOperator;

public class ProgramVariant_Helper {

	public static List<SuspiciousCode> getModifiedCodes(ProgramVariant variant) {
		List<OperatorInstance> operations = new ArrayList<>();
		for(int i= variant.getLastModificationPointAnalyzed();i<=variant.getGenerationSource();i++){
			List<OperatorInstance> lst = variant.getOperations(i);
			if (lst!=null)
				operations.addAll(lst);
		}	
		
		List<SuspiciousCode> modifiedLocs = new ArrayList<>();
		for(OperatorInstance opInstance:operations){
			/*AstorOperator astorOp = opInstance.getOperationApplied();
			if (astorOp instanceof InsertAfterOp ){
				
			}else if (astorOp instanceof InsertBeforeOp ){
				
			}else if (astorOp instanceof RemoveOp ){
				
			}*/
			
			ModificationPoint modifiedPoint = opInstance.getModificationPoint();
			if (modifiedPoint instanceof SuspiciousModificationPoint){
				SuspiciousCode suspoint = ((SuspiciousModificationPoint) modifiedPoint).getSuspicious();
				if (!modifiedLocs.contains(suspoint))
					modifiedLocs.add(suspoint);
			}
			/*String className = modifiedPoint.getCtClass().getQualifiedName();
			int line = modifiedPoint.getCodeElement().getPosition().getLine();*/			
		}
		
		return modifiedLocs;
	}

}
