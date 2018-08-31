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
		//app.testMath50Remove();
		//app.testTime_27_AdqFix();
		app.testChart_15_AdqFix();
		//app.testChart_15_Genprog();
		//app.testChart_14_AdqFix();
		//app.testChart_25_AdqFix();
		//app.testChart_26_AdqFix();
	}
	
	void testDefects4j(){
		
	}
	
	void testChart_14_AdqFix() throws Exception{
		String location = "/tmp/chart_14_AdqFix";
		String lib = location+"/lib/";
		String argStr = "-mode  adqfix  -location  "+location
						+" -dependencies  " + lib
						//" -failing  org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests: "
						+" -failing org.jfree.chart.plot.junit.CategoryPlotTests#testRemoveRangeMarker:org.jfree.chart.plot.junit.CategoryPlotTests#testRemoveDomainMarker:org.jfree.chart.plot.junit.XYPlotTests#testRemoveRangeMarker:org.jfree.chart.plot.junit.XYPlotTests#testRemoveDomainMarker: "
						+" -package org.jfree -jvm4testexecution /usr/lib/jvm/java-8-oracle/bin/ -javacompliancelevel 4 "
						+" -maxgen 10000 -seed 353 -maxtime 180  "
						+" -scope package"
						//+" -scope  file "
						+" -stopfirst False -flthreshold 0.1 -population 5 -srcjavafolder source/ -srctestfolder tests/ -binjavafolder build/ -bintestfolder build-tests/"
						//+" -regressionforfaultlocalization False"
						+ " -loglevel FINE";
		//argStr += " -testbystep True";
		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
	}
	
	void testChart_25_AdqFix() throws Exception{
		String location = "/tmp/chart_25_AdqFix";
		String lib = location+"/lib/";
		String argStr = "-mode  adqfix  -location  "+location
						+" -dependencies  " + lib
						//" -failing  org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests: "
						+" -failing org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests#testDrawWithNullMeanVertical:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests#testDrawWithNullDeviationVertical:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests#testDrawWithNullMeanHorizontal:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests#testDrawWithNullDeviationHorizontal: "
						+" -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4 "
						+" -maxgen  500  -seed  300  -maxtime  180 "
						+" -scope  file "
						+" -stopfirst  True  -flthreshold  0.1  -population  5  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/ "
						//+" -regressionforfaultlocalization False"
						+ " -loglevel INFO";
		//argStr += " -testbystep True";
		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
	}
	
	void testChart_26_AdqFix() throws Exception{
		String location = "/tmp/chart_26_AdqFix";
		String lib = location+"/lib/";
		String argStr = "-mode  adqfix  -location  "+location
						+" -dependencies  " + lib
						+" -failing  org.jfree.chart.junit.AreaChartTests#testDrawWithNullInfo:org.jfree.chart.junit.BarChart3DTests#testDrawWithNullInfo:org.jfree.chart.junit.BarChartTests#testDrawWithNullInfo:org.jfree.chart.junit.GanttChartTests#testDrawWithNullInfo:org.jfree.chart.junit.GanttChartTests#testDrawWithNullInfo2:org.jfree.chart.junit.LineChart3DTests#testDrawWithNullInfo:org.jfree.chart.junit.LineChartTests#testDrawWithNullInfo:org.jfree.chart.junit.StackedAreaChartTests#testDrawWithNullInfo:org.jfree.chart.junit.StackedBarChart3DTests#testDrawWithNullInfo:org.jfree.chart.junit.StackedBarChartTests#testDrawWithNullInfo:org.jfree.chart.junit.WaterfallChartTests#testDrawWithNullInfo:org.jfree.chart.plot.junit.CategoryPlotTests#test1654215:org.jfree.chart.plot.junit.CategoryPlotTests#testSerialization3:org.jfree.chart.plot.junit.CategoryPlotTests#testSerialization4:org.jfree.chart.renderer.category.junit.BoxAndWhiskerRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.GroupedStackedBarRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.IntervalBarRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.LayeredBarRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.LevelRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.MinMaxCategoryRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.StatisticalBarRendererTests#testDrawWithNullInfo:org.jfree.chart.renderer.category.junit.StatisticalLineAndShapeRendererTests#testDrawWithNullInfo:"
						+" -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4 "
						+" -maxgen  10000  -seed  353  -maxtime  180  -scope  package  -stopfirst  False  "
						+" -flthreshold  0.1  -population  5  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/ "
						//+" -regressionforfaultlocalization False"
						+ " -loglevel INFO";
		//argStr += " -testbystep True";
		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
	}
	
	void testTime_27_AdqFix() throws Exception{
		String location = "/tmp/time_27_AdqFix";
		String lib = location+"/lib/";
		String argStr = "-mode  adqfix "
						+ " -location  "+location
						+ " -dependencies  "+lib
						+ " -failing  org.joda.time.format.TestPeriodFormatterBuilder: "
						+ "-package  org.joda  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/ "
						+ " -javacompliancelevel  5 "
						+ " -maxgen  500 "
						+ " -seed  300 "
						+ " -maxtime  180 "
						+ " -scope  package "
						+ " -stopfirst  True "
						+ " -flthreshold  0.1 "
						+ " -population  10 "
						+ " -srcjavafolder  src/main/java/ "
						+ " -srctestfolder  src/test/java/ "
						+ " -binjavafolder  build/classes/ "
						+ " -bintestfolder  build/tests/ "
						+ " -loglevel INFO";
		//argStr += " -testbystep True";
		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
	}
	
	void testChart_15_AdqFix() throws Exception{
		String location = "/tmp/chart_15_AdqFix";
		String libDir = location+"/lib/";
		//String argStr = "-mode  adqfix  -location  "+location+"  -dependencies  lib/  -failing  org.jfree.chart.plot.junit.PiePlot3DTests:  -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4  -maxgen  500  -seed  300  -maxtime  180  -scope  package  -stopfirst  True  -flthreshold  0.1  -population  10  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/";
		String argStr = "-mode  adqfix  -location   "+location+" -dependencies  "+libDir
				+"  -failing org.jfree.chart.plot.junit.PiePlot3DTests#testDrawWithNullDataset: "
				+" -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4  -maxgen  500  -seed  300  -maxtime  180  -scope  package  -stopfirst  True  -flthreshold  0.1  -population  10  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/";
		argStr+= " -loglevel INFO";		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
	}
	void testChart_15_Genprog() throws Exception{
		String location = "/tmp/chart_15_Genprog";
		String libDir = location+"/lib/";
		//String argStr = "-mode  adqfix  -location  "+location+"  -dependencies  lib/  -failing  org.jfree.chart.plot.junit.PiePlot3DTests:  -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4  -maxgen  500  -seed  300  -maxtime  180  -scope  package  -stopfirst  True  -flthreshold  0.1  -population  10  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/";
		String argStr = "-mode jGenProg -location   "+location+" -dependencies  "+libDir
				+"  -failing org.jfree.chart.plot.junit.PiePlot3DTests#testDrawWithNullDataset: "
				+" -package  org.jfree  -jvm4testexecution  /usr/lib/jvm/java-8-oracle/bin/  -javacompliancelevel  4  -maxgen  500  -seed  300  -maxtime  180  -scope  package  -stopfirst  True  -flthreshold  0.1  -population  10  -srcjavafolder  source/  -srctestfolder  tests/  -binjavafolder  build/  -bintestfolder  build-tests/";
		argStr+= " -loglevel INFO";		
		AstorMain main1 = new AstorMain();
		String[] args = argStr.split("\\s+");
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		System.out.println("Solution count: "+solutions.size());
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
