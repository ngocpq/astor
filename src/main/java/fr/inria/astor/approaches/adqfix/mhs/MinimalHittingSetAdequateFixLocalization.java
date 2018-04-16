package fr.inria.astor.approaches.adqfix.mhs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.inria.astor.approaches.adqfix.AdequateFixLocationStrategy;
import fr.inria.astor.approaches.adqfix.mhs.model.DiagnosisSet;
import fr.inria.astor.approaches.adqfix.mhs.model.IObservationMatrix;
import fr.inria.astor.approaches.adqfix.model.MinimalCorrectionLocationSet;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;

public class MinimalHittingSetAdequateFixLocalization implements AdequateFixLocationStrategy {

	private List<SuspiciousCode> suspicious;

	List<MinimalCorrectionLocationSet> allMCSes;
	
	int lamda = 1;
	int mcsSizeMax = 3;
	
	@Override
	public void setSuspiciousCode(List<SuspiciousCode> suspicious) {
		this.suspicious = suspicious;

		Map<Integer,List<SuspiciousCode>> mapCoverage = new HashMap<>();
		for(SuspiciousCode stmt:this.suspicious){
			Map<Integer, Integer> coverMap = stmt.getCoverage();
			for(Entry<Integer, Integer> en:coverMap.entrySet()){
				Integer tcId = en.getKey();
				int hitCount = en.getValue();
				List<SuspiciousCode> cover = mapCoverage.get(tcId);
				if (cover==null){
					cover =new ArrayList<>();
					mapCoverage.put(tcId, cover);
				}
				if (hitCount>0)
					cover.add(stmt);
			}
		}
		
		IObservationMatrix<SuspiciousCode> matrix = null;// new ObservationMatrix()		
		//compute Minimal hitting set		
		DiagnosisSet<SuspiciousCode> result = StaccatoAlgorithm.Staccato(matrix, lamda, mcsSizeMax);
		//this.allMCSes = StaccatoAlgorithm.Staccato(a, lamda, L);
		
		result.getCandidateList();
	}

	@Override
	public List<MinimalCorrectionLocationSet> getAllAdequateFixes() {
		return this.allMCSes;
	}

}
