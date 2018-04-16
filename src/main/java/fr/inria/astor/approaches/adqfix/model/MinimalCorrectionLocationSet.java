package fr.inria.astor.approaches.adqfix.model;

import java.util.List;

import fr.inria.astor.approaches.adqfix.mhs.model.DiagnosisCandidateBase;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;

public class MinimalCorrectionLocationSet extends DiagnosisCandidateBase<SuspiciousCode> {
	public MinimalCorrectionLocationSet(List<SuspiciousCode> suspicious){
		super(suspicious.toArray(new SuspiciousCode[suspicious.size()]));		
	}
}
