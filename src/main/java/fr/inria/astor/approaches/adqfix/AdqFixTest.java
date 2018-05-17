package fr.inria.astor.approaches.adqfix;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.loop.population.PopulationConformation;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.main.evolution.AstorMain;

/**
 * Test of Astor in mode jgenprog
 * 
 * @author Matias Martinez, matias.martinez@inria.fr
 *
 */
public class AdqFixTest {

	public static Logger log = Logger.getLogger(Thread.currentThread().getName());
	File out = null;

	public AdqFixTest() {
		out = new File(ConfigurationProperties.getProperty("workingDirectory"));
	}

	public static void main(String[] args) throws Exception {
		AdqFixTest app = new AdqFixTest();
		//app.testMath85();
		app.testMath50Remove();
	}
	
	/**
	 * The fix is a replacement of an return statement
	 * 
	 * @throws Exception
	 */
	public void testMath85() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.distribution.NormalDistributionTest", "-location",
				new File("./examples/math_85").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes", "-bintestfolder",
				"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-stopfirst", "true",
				"-maxgen", "400", "-scope", "package", "-seed", "10", "-loglevel", "INFO" };
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());

	}
	


	
	public static String[] commandMath70(String dep, File out, int generations) {
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.analysis.solvers.BisectionSolverTest", "-location",
				new File("./examples/math_70").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes", "-bintestfolder",
				"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "10", "-maxgen", Integer.toString(generations),
				"-stopfirst", "true", "-maxtime", "5", "-loglevel", "INFO", "-parameters", "disablelog:false"

		};
		return args;
	}


	/**
	 * Testing injected bug at CharacterReader line 118, commit version 31be24.
	 * "org.jsoup.nodes.AttributesTest"+File.pathSeparator+"org.jsoup.nodes.DocumentTypeTest"
	 * +File.pathSeparator+"org.jsoup.nodes.NodeTest"+File.pathSeparator+"org.jsoup.parser.HtmlParserTest"
	 * 
	 * @throws Exception
	 */
	public void testJSoupParser31be24() throws Exception {
		String dep = new File("./examples/libs/junit-4.5.jar").getAbsolutePath();
		AstorMain main1 = new AstorMain();

		String[] args = new String[] { "-mode", "adqfix", "-location",
				new File("./examples/jsoup31be24").getAbsolutePath(), "-dependencies", dep,
				// The injected bug produces 4 failing cases in two files
				"-failing",
				"org.jsoup.parser.CharacterReaderTest" + File.pathSeparator + "org.jsoup.parser.HtmlParserTest",
				//
				"-package", "org.jsoup", "-javacompliancelevel", "7", "-stopfirst", "true",
				//
				"-flthreshold", "0.8", "-srcjavafolder", "/src/main/java/", "-srctestfolder", "/src/test/java/",
				"-binjavafolder", "/target/classes", "-bintestfolder", "/target/test-classes",
				//
				"-scope", "local", "-seed", "10", "-maxtime", "100", "-population", "1", "-maxgen", "250", "-saveall",
				"true" };
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
		// TODO: Problem printing CtThisAccess
		// pos += offset
		// time(sec)= 30
		// operation: ReplaceOp
		// location= org.jsoup.parser.CharacterReader
		// line= 118
		// original statement= pos -= offset
		// fixed statement= pos += offset
		// generation= 26
	}

	@SuppressWarnings("rawtypes")
	public void testMath50Remove() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.8.2.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, 
				"-mode", "adqfix",
//				"-mode", "statement",
				"-population", "5",
				"-failing", "org.apache.commons.math.analysis.solvers.RegulaFalsiSolverTest", "-location",
				new File("./examples/math_50").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "5", "-flthreshold", "0.1", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "10", "-maxgen", "50", "-stopfirst", "true",
				"-maxtime", "5", "-ignoredtestcases", "org.apache.commons.math.util.FastMathTest" };
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
		if (solutions.size()==0)
			return;
		ProgramVariant variant = solutions.get(0);

		TestCaseVariantValidationResult validationResult = (TestCaseVariantValidationResult) variant
				.getValidationResult();

		System.out.println("Solution valid: "+validationResult.isRegressionExecuted());

	}

	@SuppressWarnings("rawtypes")
	public void testMath76() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.linear.SingularValueSolverTest", "-location",
				new File("./examples/math_76").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "5", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "6010", "-maxgen", "50", "-stopfirst", "true",
				"-maxtime", "2",

		};
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution empty: "+solutions.isEmpty());

	}

	@SuppressWarnings("rawtypes")
	public void testMath74() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegratorTest", "-location",
				new File("./examples/math_74").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "5", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "10", "-maxgen", "50", "-stopfirst", "true",
				"-maxtime", "2",

		};
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution empty: "+solutions.isEmpty());

	}

	@SuppressWarnings("rawtypes")
	public void testMath106UndoException() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/commons-discovery-0.2.jar").getAbsolutePath() + File.pathSeparator
				+ new File("./examples/libs/commons-logging-1.0.4.jar").getAbsolutePath() + File.pathSeparator
				+ new File("./examples/libs/junit-3.8.2.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.fraction.FractionFormatTest", "-location",
				new File("./examples/math_106").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "6010", "-maxgen", "50", "-stopfirst", "true",
				"-maxtime", "5",

		};
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution empty: "+solutions.isEmpty());
	}

	@SuppressWarnings("rawtypes")
	public void testMath70NotFailingAsArg() throws Exception {

		String originalFailing = "org.apache.commons.math.analysis.solvers.BisectionSolverTest";
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", //
				"-location", new File("./examples/math_70").getAbsolutePath(), "-package", "org.apache.commons",
				"-srcjavafolder", "/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-maxgen", "0",// Forced

		};
		System.out.println(Arrays.toString(args));
		main1.execute(args);
		List<String> deducedFailingTest = main1.getEngine().getProjectFacade().getProperties().getFailingTestCases();
		System.out.println("deducedFailingTest size: "+deducedFailingTest.size());
		log.debug("deduced: " + deducedFailingTest);
		System.out.println("deducedFailingTest contain originalFialing: "+deducedFailingTest.contains(originalFailing));
	}

	public void testMath70DiffOfSolution() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		int generations = 50;
		String[] args = commandMath70(dep, out, generations);
		//CommandSummary cs = new CommandSummary(args);
		//cs.command.put("-stopfirst", "true");

		//System.out.println(Arrays.toString(cs.flat()));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		ProgramVariant variant = solutions.get(0);
		System.out.println("pathDiff not empty: "+ !variant.getPatchDiff().isEmpty());
		System.out.println("AstorOutputStatus.STOP_BY_PATCH_FOUND eqaual to: "+ main1.getEngine().getOutputStatus());

		String diff = variant.getPatchDiff();
		log.debug("Patch: " + diff);

	}

	public void testMath70StopAtXVariantsSolution() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.analysis.solvers.BisectionSolverTest", "-location",
				new File("./examples/math_70").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes", "-bintestfolder",
				"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(),
				//
				"-scope", "package", "-seed", "10", "-maxgen", "1000", "-stopfirst", "false", "-maxtime", "10",
				"-population", "1", "-reintroduce", PopulationConformation.PARENTS.toString(),
				
				"-parameters", "maxnumbersolutions:2" // <-- 2
		};
		System.out.println(Arrays.toString(args));

		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("found 2 solution: "+solutions.size());

		 args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
					"org.apache.commons.math.analysis.solvers.BisectionSolverTest", "-location",
					new File("./examples/math_70").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
					"/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes", "-bintestfolder",
					"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
					out.getAbsolutePath(),
					//
					"-scope", "package", "-seed", "10", "-maxgen", "1000", "-stopfirst", "false", "-maxtime", "10",
					"-population", "1", "-reintroduce", PopulationConformation.PARENTS.toString(),
					
					"-parameters", "maxnumbersolutions:1" // <-- 1
			};
		main1.execute(args);

		solutions = main1.getEngine().getSolutions();
		System.out.println("Found 1 solution: "+solutions.size());
	}

}
