package fr.inria.astor.approaches.adqfix.mhs.model;

public class Diagnosis<CompType> extends DiagnosisCandidateBase<CompType> {	
	public Diagnosis(){}
	
	public Diagnosis(CompType ...compIDs){
		super(compIDs);
	}
}
