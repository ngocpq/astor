package fr.inria.astor.test.repair.approaches;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import fr.inria.astor.approaches.jgenprog.JGenProg;
import fr.inria.astor.approaches.jgenprog.operators.ReplaceOp;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.loop.population.PopulationConformation;
import fr.inria.astor.core.manipulation.sourcecode.VariableResolver;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.test.repair.core.BaseEvolutionaryTest;
import fr.inria.astor.util.CommandSummary;
import fr.inria.main.AstorOutputStatus;
import fr.inria.main.evolution.AstorMain;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

/**
 * Test of Astor in mode jgenprog
 * 
 * @author Matias Martinez, matias.martinez@inria.fr
 *
 */
public class AdqFixTest extends BaseEvolutionaryTest {

	public static Logger log = Logger.getLogger(Thread.currentThread().getName());
	File out = null;

	public AdqFixTest() {
		out = new File(ConfigurationProperties.getProperty("workingDirectory"));
	}

	public static void main(String[] args) throws Exception {
		AdqFixTest app = new AdqFixTest();
		app.testMath85();
	}
	
	/**
	 * The fix is a replacement of an return statement
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
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
		assertTrue(solutions.size() > 0);

	}
	


	 
	@SuppressWarnings("rawtypes")
	@Test
	@Ignore
	public void testMath70ThisKeyword() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		int generations = 0;
		String[] args = commandMath70(dep, out, generations);
		CommandSummary cs = new CommandSummary(args);
		cs.command.put("-flthreshold", "1");
		cs.command.put("-stopfirst", "true");
		cs.command.put("-loglevel", "INFO");
		cs.command.put("-saveall", "true");
		cs.append("-parameters", ("logtestexecution:true"));

		System.out.println(Arrays.toString(cs.flat()));
		main1.execute(cs.flat());

		assertEquals(1, main1.getEngine().getVariants().size());
		ProgramVariant variant = main1.getEngine().getVariants().get(0);

		JGenProg jgp = (JGenProg) main1.getEngine();
		ReplaceOp rop = new ReplaceOp();
		CtElement elMP1 = variant.getModificationPoints().get(0).getCodeElement();

		assertEquals(elMP1.toString(), "return solve(min, max)");
		System.out.println(elMP1);

		List<CtCodeElement> ingredients = jgp.getIngredientSearchStrategy().getIngredientSpace().getIngredients(elMP1,
				elMP1.getClass().getSimpleName());
		System.out.println(ingredients);
		CtCodeElement patch = ingredients.get(0);
		assertEquals(patch.toString(), "return solve(f, min, max)");

		OperatorInstance operation = new OperatorInstance();
		operation.setOriginal(elMP1);
		operation.setOperationApplied(rop);
		operation.setModificationPoint(variant.getModificationPoints().get(0));
		operation.defineParentInformation(variant.getModificationPoints().get(0));
		operation.setModified(patch);

		variant.putModificationInstance(0, operation);

		boolean changed = VariableResolver.changeShadowedVars(elMP1, patch);
		assertTrue(changed);
		System.out.println("Pach code before: " + patch);

		CtMethod mep = (CtMethod) elMP1.getParent(spoon.reflect.declaration.CtMethod.class);
		System.out.println("Parent before:\n" + mep);
		elMP1.replace(patch);
		System.out.println("Parent after:\n" + mep);
		System.out.println("Pach code after: " + patch);
		// assertEquals(patch.toString(),"return solve(this.f, min, max)");
		assertEquals(patch.toString(), "return solve(f, min, max)");

		patch.setImplicit(false);
		System.out.println("Pach code after impl: " + patch);

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


	@Test
	public void testArguments() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.analysis.solvers.BisectionSolverTest", "-location",
				new File("./examples/math_70").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes", "-bintestfolder",
				"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-out",
				out.getAbsolutePath(), "-scope", "package", "-seed", "10", "-stopfirst", "true", "-maxgen", "50",
				"-saveall", "false" };
		boolean correct = main1.processArguments(args);
		assertTrue(correct);

		String javahome = ConfigurationProperties.properties.getProperty("jvm4testexecution");

		assertNotNull(javahome);

		assertTrue(javahome.endsWith("bin"));
	}




	/**
	 * Testing injected bug at CharacterReader line 118, commit version 31be24.
	 * "org.jsoup.nodes.AttributesTest"+File.pathSeparator+"org.jsoup.nodes.DocumentTypeTest"
	 * +File.pathSeparator+"org.jsoup.nodes.NodeTest"+File.pathSeparator+"org.jsoup.parser.HtmlParserTest"
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
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
		assertEquals(1, solutions.size());
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
	@Test
	public void testMath50Remove() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.8.2.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = new String[] { "-dependencies", dep, "-mode", "adqfix", "-failing",
				"org.apache.commons.math.analysis.solvers.RegulaFalsiSolverTest", "-location",
				new File("./examples/math_50").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/main/java/", "-srctestfolder", "/src/test/java", "-binjavafolder", "/target/classes",
				"-bintestfolder", "/target/test-classes", "-javacompliancelevel", "5", "-flthreshold", "0.1", "-out",
				out.getAbsolutePath(), "-scope", "local", "-seed", "10", "-maxgen", "50", "-stopfirst", "true",
				"-maxtime", "5", "-ignoredtestcases", "org.apache.commons.math.util.FastMathTest" };
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		assertTrue(solutions.size() > 0);
		assertEquals(1, solutions.size());
		ProgramVariant variant = solutions.get(0);

		TestCaseVariantValidationResult validationResult = (TestCaseVariantValidationResult) variant
				.getValidationResult();

		assertTrue(validationResult.isRegressionExecuted());

	}

	@SuppressWarnings("rawtypes")
	@Test
	@Ignore
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
		assertTrue(solutions.isEmpty());

	}

	@SuppressWarnings("rawtypes")
	@Test
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
		assertTrue(solutions.isEmpty());

	}

	@SuppressWarnings("rawtypes")
	@Test
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
		assertTrue(solutions.isEmpty());

	}

	@SuppressWarnings("rawtypes")
	@Test
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
		assertNotNull(deducedFailingTest);
		assertEquals(1, deducedFailingTest.size());
		log.debug("deduced: " + deducedFailingTest);
		assertTrue(deducedFailingTest.contains(originalFailing));
	}


	@Test
	public void testMath70DiffOfSolution() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		int generations = 50;
		String[] args = commandMath70(dep, out, generations);
		CommandSummary cs = new CommandSummary(args);
		cs.command.put("-stopfirst", "true");

		System.out.println(Arrays.toString(cs.flat()));
		main1.execute(cs.flat());

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		assertTrue(solutions.size() > 0);
		assertEquals(1, solutions.size());
		ProgramVariant variant = solutions.get(0);
		assertFalse(variant.getPatchDiff().isEmpty());
		assertEquals(AstorOutputStatus.STOP_BY_PATCH_FOUND, main1.getEngine().getOutputStatus());

		String diff = variant.getPatchDiff();
		log.debug("Patch: " + diff);

	}

	@Test
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
				"-population", "1", "-reintroduce", PopulationConformation.PARENTS.toString()

		};
		System.out.println(Arrays.toString(args));
		CommandSummary command = new CommandSummary(args);

		command.command.put("-parameters", "maxnumbersolutions:2");
		main1.execute(command.flat());

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		assertEquals(2, solutions.size());

		command.command.put("-parameters", "maxnumbersolutions:1");
		main1.execute(command.flat());

		solutions = main1.getEngine().getSolutions();
		assertEquals(1, solutions.size());

	}



}
