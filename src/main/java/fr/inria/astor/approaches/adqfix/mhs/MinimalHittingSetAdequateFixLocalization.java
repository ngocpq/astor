package fr.inria.astor.approaches.adqfix.mhs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.inria.astor.approaches.adqfix.AdequateFixLocationStrategy;
import fr.inria.astor.approaches.adqfix.mhs.model.IObservationMatrix;
import fr.inria.astor.approaches.adqfix.mhs.model.ObservationMatrix;
import fr.inria.astor.approaches.adqfix.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.model.DiagnosisSet;
import fr.inria.astor.approaches.adqfix.validate.AdequateEvaluationResult;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;

public class MinimalHittingSetAdequateFixLocalization implements AdequateFixLocationStrategy<SuspiciousCode> {

	private List<SuspiciousCode> suspicious;
	static Logger logger = Logger.getLogger(MinimalHittingSetAdequateFixLocalization.class.getName());

	//List<MinimalCorrectionLocationSet> allMCSes;
	DiagnosisSet<SuspiciousCode> diagnosisSet;
	//IObservationMatrix<SuspiciousCode> observationMatrix;
	
	List<Integer> testCaseIds ;
	List<String> testCaseNames ;
	Map<String, Map<SuspiciousCode,Integer>> coverageMatrix_test2code;	
	Map<SuspiciousCode, Map<String,Integer>> coverageMatrix_code2test;
	
	int lamda = 1;
	int mcsSizeMax = 3;


	private final Map<String, Map<String, Integer>> testCaseIdMapping;

	public MinimalHittingSetAdequateFixLocalization(Map<String, Map<String, Integer>> map) {
		this.testCaseIdMapping = map;		
		logger.info("Failling tests: ");
		int i=0;
		for(Map<String, Integer> en:this.testCaseIdMapping.values()){
			for(String tc:en.keySet())
				logger.info(++i+") "+tc);
		}	
	}

	@Override
	public void initAdequateFixLocationStrategy(List<SuspiciousCode> suspicious, List<String> failingTestCases) {		
		this.suspicious = suspicious;
		
		this.testCaseIds = new ArrayList<>();
		this.testCaseNames = new ArrayList<>();
		for(int i=0;i<failingTestCases.size();i++){
			String tc = failingTestCases.get(i);
			Map<String, Integer> mapTestMethods = this.testCaseIdMapping.get(tc);
			if (mapTestMethods==null){
				logger.info("Failing test case not found in TestCaseIdMappping: '"+tc);
				continue;
			}
			for(Entry<String, Integer> en:mapTestMethods.entrySet()){
				testCaseNames.add(en.getKey());
				testCaseIds.add(en.getValue());
			}
		}
		int numTest = testCaseIds.size();	
		logger.info("# failing test cases: "+numTest);		
		
		this.coverageMatrix_code2test = new HashMap<>();
		this.coverageMatrix_test2code = new HashMap<>();
		IObservationMatrix<SuspiciousCode> matrix = new ObservationMatrix<SuspiciousCode>(numTest,suspicious);		
		for(int i=0;i<testCaseIds.size();i++){
			Integer tcID = testCaseIds.get(i);
			String tcName = testCaseNames.get(i);
			Map<SuspiciousCode, Integer> coveredStmt = new HashMap<>();
			this.coverageMatrix_test2code.put(tcName, coveredStmt);
			
			for (int j=0;j<this.suspicious.size();j++){
				SuspiciousCode stmt=this.suspicious.get(j);
				Map<Integer, Integer> coverMap = stmt.getCoverage();
				int frequent = 0;
				if (coverMap.containsKey(tcID)){
					frequent = coverMap.get(tcID);
				}
				
				if (frequent>0){
					matrix.set(i, j, 1);
					//save coverage matrix
					coveredStmt.put(stmt, frequent);
					
					Map<String, Integer> tests = this.coverageMatrix_code2test.get(stmt);
					if (tests==null){
						tests = new HashMap<>();
						this.coverageMatrix_code2test.put(stmt, tests);
					}
					if (tests.containsKey(tcName))
						logger.info("Warning: duplicate test coverage: "+tcName+", "+stmt);
					tests.put(tcName, frequent);					
				}else
					matrix.set(i, j, 0);								
			}
			matrix.setErrorTrace(i, IObservationMatrix.RESULT_FAILED);						
			
		}
		//compute Minimal hitting set		
		diagnosisSet = StaccatoAlgorithm.Staccato(matrix, lamda, mcsSizeMax);
		logger.info("#diagnosisSet: "+diagnosisSet.size());
		logger.debug("diagnosisSet: "+diagnosisSet.toString());		
	}

	@Override
	public List<Diagnosis<SuspiciousCode>> getAllAdequateFixes() {
		return this.diagnosisSet.getCandidateList();
	}

	@Override
	public DiagnosisSet<SuspiciousCode> getDiagnosisSet() {
		return this.diagnosisSet;
	}
	
	public AdequateEvaluationResult evaluateAdequate(ProgramVariant variant) {		
		List<OperatorInstance> operations = new ArrayList<>();
		for(int i= variant.getLastModificationPointAnalyzed();i<=variant.getGenerationSource();i++){
			List<OperatorInstance> lst = variant.getOperations(i);
			if (lst!=null)
				operations.addAll(lst);
		}
				
		Map<SuspiciousCode,List<OperatorInstance>> changedPoints = new HashMap<>();		
		for(OperatorInstance opInstance:operations){			
			ModificationPoint modifiedPoint = opInstance.getModificationPoint();
			if (modifiedPoint instanceof SuspiciousModificationPoint){
				SuspiciousCode suspoint = ((SuspiciousModificationPoint) modifiedPoint).getSuspicious();
				List<OperatorInstance> changedOperators = changedPoints.get(suspoint);
				if (changedOperators==null){
					changedOperators = new ArrayList<>();
					changedPoints.put(suspoint, changedOperators);
				}
				if (!changedOperators.contains(opInstance))
					changedOperators.add(opInstance);
			}					
		}
		
		List<String> affectedTests = new ArrayList<>();
		//get affected tests
		for(Entry<String, Map<SuspiciousCode, Integer>> en:this.coverageMatrix_test2code.entrySet()){
			String test = en.getKey();
			Map<SuspiciousCode, Integer> coverage = en.getValue();
			for(Entry<SuspiciousCode, List<OperatorInstance>> points:changedPoints.entrySet()){
				SuspiciousCode modPoint = points.getKey();
				//List<OperatorInstance> changeOps = points.getValue();				
				Integer frequent = coverage.get(modPoint);
				if (frequent!=null && frequent >0){
					affectedTests.add(test);
					break;
				}
			}
		}		
		//get unaffectedTests;		
		List<String> unaffectedTests = new ArrayList<>();
		for(String test:this.testCaseNames){
			if (!affectedTests.contains(test))
				unaffectedTests.add(test);
		}
		
		AdequateEvaluationResult adqEvaluation = new AdequateEvaluationResult(affectedTests, unaffectedTests);
		return adqEvaluation;
	}

	
}
