package fr.inria.astor.approaches.adqfix;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.gzoltar.core.GZoltar;
import com.gzoltar.core.components.Statement;
import com.gzoltar.core.components.count.ComponentCount;
import com.gzoltar.core.instr.testing.TestResult;

import fr.inria.astor.approaches.adqfix.mhs.model.IObservationMatrix;
import fr.inria.astor.approaches.adqfix.mhs.model.ObservationMatrix;
import fr.inria.astor.core.faultlocalization.FaultLocalizationResult;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarFaultLocalization;
import fr.inria.astor.core.setup.ConfigurationProperties;

public class CoverageInfoCached_GZoltarFaultLocalization extends GZoltarFaultLocalization{

	static Logger logger = Logger.getLogger(CoverageInfoCached_GZoltarFaultLocalization.class.getName());

	//IObservationMatrix observationMatrix ;
	Map<String, Integer> mapfailingTestIndex ;
	public Map<String, Integer> getTestCaseIdMapping(){
		return this.mapfailingTestIndex;
	}
	
	protected FaultLocalizationResult searchSuspicious(String locationBytecode, List<String> testsToExecute,
			List<String> toInstrument, Set<String> cp, String srcFolder) throws Exception {

		List<String> failingTestCases = new ArrayList<String>();
		List<Integer> failingTestCasesID = new ArrayList<Integer>();
		
		Double thr = ConfigurationProperties.getPropertyDouble("flthreshold");
		logger.info("Gzoltar fault localization: min susp value parameter: " + thr);
		// 1. Instantiate GZoltar
		// here you need to specify the working directory where the tests will
		// be run. Can be the full or relative path.
		// Example: GZoltar gz = new
		// GZoltar("C:\\Personal\\develop\\workspaceEvolution\\testProject\\target\\classes");

		File projLocationFile = new File(locationBytecode + File.separator);
		String projLocationPath = projLocationFile.getAbsolutePath();
		logger.debug("Gzoltar run over: " + projLocationPath + " , does it exist? " + projLocationFile.exists());

		GZoltar gz = new GZoltar(projLocationPath + File.separator);

		// 2. Add Package/Class names to instrument
		// 3. Add Package/Test Case/Test Suite names to execute
		// Example: gz.addPackageToInstrument("org.test1.Person");
		for (String to : toInstrument) {
			gz.addPackageToInstrument(to);
		}
		if (cp != null || !cp.isEmpty()) {
			logger.info("-gz-Adding classpath: " + cp);
			gz.getClasspaths().addAll(cp);
		}
		for (String test : testsToExecute) {
			gz.addTestToExecute(test);
			gz.addClassNotToInstrument(test);
		}

		String testToAvoid = ConfigurationProperties.getProperty("gzoltartestpackagetoexclude");
		if (testToAvoid != null) {
			String[] testtoavoidarray = testToAvoid.split("_");
			for (String test : testtoavoidarray) {
				gz.addTestPackageNotToExecute(test);
			}
		}

		String packagetonotinstrument = ConfigurationProperties.getProperty("gzoltarpackagetonotinstrument");

		if (packagetonotinstrument != null) {
			String[] packages = packagetonotinstrument.split("_");
			for (String p : packages) {
				gz.addPackageNotToInstrument(p);

			}

		}

		gz.run();
		int[] sum = new int[2];
		mapfailingTestIndex = new HashMap<>();
		int testidex = 0;
		for (TestResult tr : gz.getTestResults()) {			
			String testName = tr.getName().split("#")[0];
			if (testName.startsWith("junit")) {
				continue;
			}
			sum[0]++;
			sum[1] += tr.wasSuccessful() ? 0 : 1;
			if (!tr.wasSuccessful()) {
				logger.info("Test failt: " + tr.getName());

				String testCaseName = testName.split("\\#")[0];
				if (!failingTestCases.contains(testCaseName)) {
					failingTestCases.add(testCaseName);			
					failingTestCasesID.add(testidex);
					
					mapfailingTestIndex.put(testCaseName, testidex);					
				}
			}
			testidex++;
		}

		int gzPositives = gz.getSuspiciousStatements().stream().filter(x -> x.getSuspiciousness() > 0)
				.collect(Collectors.toList()).size();

		logger.info("Gzoltar Test Result Total:" + sum[0] + ", fails: " + sum[1] + ", GZoltar suspicious "
				+ gz.getSuspiciousStatements().size() + ", with positive susp " + gzPositives);

		DecimalFormat df = new DecimalFormat("#.###");
		int maxSuspCandidates = ConfigurationProperties.getPropertyInt("maxsuspcandidates");

		List<Statement> gzCandidates = new ArrayList();

		for (Statement gzoltarStatement : gz.getSuspiciousStatements()) {
			String compName = gzoltarStatement.getMethod().getParent().getLabel();
			if (isSource(compName, srcFolder) && (!ConfigurationProperties.getPropertyBool("limitbysuspicious")
					|| (gzoltarStatement.getSuspiciousness() >= thr))) {
				gzCandidates.add(gzoltarStatement);

			}

		}
		// If we do not have candidate due the threshold is to high, we add all
		// as suspicious
		if (gzCandidates.isEmpty()) {
			gzCandidates.addAll(gz.getSuspiciousStatements());

		}

		if (!ConfigurationProperties.getPropertyBool("considerzerovaluesusp")) {
			gzCandidates.removeIf(susp -> (susp.getSuspiciousness() == 0));
		}

		// we order the suspicious DESC
		Collections.sort(gzCandidates, (o1, o2) -> Double.compare(o2.getSuspiciousness(), o1.getSuspiciousness()));

		// We select the best X candidates.
		int max = (gzCandidates.size() < maxSuspCandidates) ? gzCandidates.size() : maxSuspCandidates;

		List<SuspiciousCode> candidates = new ArrayList<SuspiciousCode>();

		for (int i = 0; i < max; i++) {
			Statement gzoltarStatement = gzCandidates.get(i);
			String compName = gzoltarStatement.getMethod().getParent().getLabel();

			logger.debug("Suspicious: line " + compName + " l: " + gzoltarStatement.getLineNumber() + ", susp "
					+ df.format(gzoltarStatement.getSuspiciousness()));
			SuspiciousCode suspcode = new SuspiciousCode(compName, gzoltarStatement.getMethod().toString(),
					gzoltarStatement.getLineNumber(), gzoltarStatement.getSuspiciousness(),
					gzoltarStatement.getCountMap());
			candidates.add(suspcode);
		}

		logger.info("Gzoltar found: " + candidates.size() + " with susp > " + thr + ", we consider: " + max);

		// ----------- init observationMatrix -----------
		
		/*IObservationMatrix<SuspiciousCode> matrix = new  ObservationMatrix<SuspiciousCode>(candidates.size(),failingTestCases.size());
		for (int i = 0; i < candidates.size(); i++) {
			SuspiciousCode stmt = candidates.get(i);
			//matrix.addColumn(colIndex, initValue);
			for (int j = 0; j < failingTestCases.size(); j++) {
				String tc = failingTestCases.get(j);
				TestResult result = mapfailingTestResults.get(tc);
				Integer tcIndex = mapfailingTestIndex.get(tc);
				
				int isCover = stmt.getCoverage().getOrDefault(tcIndex, 0);
				if (isCover>0)
					matrix.setHit(i, j, 1);
				else
					matrix.setHit(i, j, 0);
			}
			matrix.setErrorTrace(i, 1);			
		}
		
		this.observationMatrix = matrix;*/
		// ----------------------------------------------
		return new FaultLocalizationResult(candidates, failingTestCases);
	}

}
