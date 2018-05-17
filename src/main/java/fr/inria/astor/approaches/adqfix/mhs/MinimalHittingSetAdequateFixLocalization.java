package fr.inria.astor.approaches.adqfix.mhs;

import java.util.List;
import java.util.Map;

import fr.inria.astor.approaches.adqfix.AdequateFixLocationStrategy;
import fr.inria.astor.approaches.adqfix.mhs.model.IObservationMatrix;
import fr.inria.astor.approaches.adqfix.mhs.model.ObservationMatrix;
import fr.inria.astor.approaches.adqfix.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.model.DiagnosisSet;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;

public class MinimalHittingSetAdequateFixLocalization implements AdequateFixLocationStrategy<SuspiciousCode> {

	private List<SuspiciousCode> suspicious;
	

	//List<MinimalCorrectionLocationSet> allMCSes;
	DiagnosisSet<SuspiciousCode> diagnosisSet;
	
	int lamda = 1;
	int mcsSizeMax = 3;


	private final Map<String, Integer> testCaseIdMapping;

	public MinimalHittingSetAdequateFixLocalization(Map<String, Integer> testCaseIdMapping) {
		this.testCaseIdMapping = testCaseIdMapping;
	}

	@Override
	public void initAdequateFixLocationStrategy(List<SuspiciousCode> suspicious, List<String> failingTestCases) {		
		this.suspicious = suspicious;		
		int numTest = failingTestCases.size();		
		IObservationMatrix<SuspiciousCode> matrix = new ObservationMatrix<SuspiciousCode>(numTest,suspicious);		
		for(int i=0;i<failingTestCases.size();i++){
			String tc = failingTestCases.get(i);
			Integer tcID = this.testCaseIdMapping.get(tc);
			for (int j=0;j<this.suspicious.size();j++){
				SuspiciousCode stmt=this.suspicious.get(j);
				Map<Integer, Integer> coverMap = stmt.getCoverage();
				int frequent = 0;
				if (coverMap.containsKey(tcID)){
					frequent = coverMap.get(tcID);
				}
				
				if (frequent>0){
					matrix.set(i, j, 1);
				}else
					matrix.set(i, j, 0);
			}
			matrix.setErrorTrace(i, IObservationMatrix.RESULT_FAILED);
		}
		//compute Minimal hitting set		
		diagnosisSet = StaccatoAlgorithm.Staccato(matrix, lamda, mcsSizeMax);
		//this.allMCSes = StaccatoAlgorithm.Staccato(a, lamda, L);				
//		this.allMCSes = new ArrayList<>();
//		for(Diagnosis<SuspiciousCode> diagnosis:result.getCandidateList()){
//			MinimalCorrectionLocationSet mcs = new MinimalCorrectionLocationSet(diagnosis.getComponentIdList());
//			this.allMCSes.add(mcs);
//		}
	}

	@Override
	public List<Diagnosis<SuspiciousCode>> getAllAdequateFixes() {
		return this.diagnosisSet.getCandidateList();
	}

	@Override
	public DiagnosisSet<SuspiciousCode> getDiagnosisSet() {
		return this.diagnosisSet;
	}
	
	

	
}
