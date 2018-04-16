package fr.inria.astor.approaches.adqfix;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarClientMasterFaultLocalization;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarFaultLocalization;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.validation.results.TestCasesProgramValidationResult;
import fr.inria.main.evolution.AstorMain;

public class MainTestRun {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		AstorMain main1 = new AstorMain();
		String dep = new File("examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		args = commandMath70(dep, out, 50, 0.5, false);
		System.out.println(Arrays.toString(args));
		main1.execute(args);
		ProgramVariant pv = main1.getEngine().getVariants().get(0);
		
		assertTrue( pv.getModificationPoints().size() > 0);		
	}


	//static String JAVA_PATH = System.getenv("JAVA_HOME")+File.separator+"bin/";
	private static String[] commandMath70(String dep, File out, int generation, Double tr, boolean mansuper) {
//		String JAVA_PATH = "/usr/lib/jvm/java-7-oracle/bin/";
		String JAVA_PATH = System.getenv("JAVA_HOME")+File.separator+"bin/";
		String[] args = new String[] { "-dependencies", dep, 
				"-mode", "adqfix", 
//				"-mode", "jgenprog",
				"-failing", "org.apache.commons.math.analysis.solvers.BisectionSolverTest", 
				"-location", new File("examples/math_70").getAbsolutePath(), 
				"-package", "org.apache.commons", 
				"-srcjavafolder", "/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes", "-bintestfolder",
				"/target/test-classes", "-javacompliancelevel", "7", "-flthreshold", Double.toString(tr), 
				"-out", out.getAbsolutePath(), 
				"-scope", "local", 
				"-seed", "10", "-maxgen", Integer.toString(generation), "-stopfirst", "true",
				"-maxtime", "100", 
				"-faultlocalization", GZoltarFaultLocalization.class.getCanonicalName(),
//				"-faultlocalization", GZoltarClientMasterFaultLocalization.class.getCanonicalName(),
				"-jvm4testexecution", JAVA_PATH,
				"-loglevel", "DEBUG",//
				"-population", "1",
				"-tmax2","1920000",
				"-regressiontestcases4fl","org.apache.commons.math.analysis.solvers.BisectionSolverTest:org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactoryImplTest",
				(mansuper)?"-manipulatesuper":"",
		}; 
		return args;
	}

	public void testNewGzoltarMath70() throws Exception {

		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		String[] args = commandMath70(dep, out, 50, 0.5, false);
		System.out.println(Arrays.toString(args));
		main1.execute(args);
		ProgramVariant pv = main1.getEngine().getVariants().get(0);
		
		assertTrue( pv.getModificationPoints().size() > 0);

		boolean hasUniv = false;
		boolean hasBisection = false;
		for(ModificationPoint mp : pv.getModificationPoints()){
			if("UnivariateRealSolverImpl".equals(mp.getCtClass().getSimpleName()))
				hasUniv = true;;
			if("BisectionSolver".equals(mp.getCtClass().getSimpleName()))	
				hasBisection = true;
		}
		assertTrue(hasBisection);
		assertTrue(hasUniv);
		
		assertTrue( ((TestCasesProgramValidationResult)pv.getValidationResult()).getCasesExecuted() > 1000);
		
	}
	
	public String[] commandLang1(File out, boolean step) {
		String libsdir = new File("./examples/libs/lang_common_lib").getAbsolutePath();
		String dep = libsdir + File.separator + "cglib.jar" + File.pathSeparator //
				+ libsdir + File.separator + "commons-io.jar" + File.pathSeparator //
				+ File.separator + libsdir + File.separator + "asm.jar" + File.pathSeparator //
				+ File.separator + libsdir + File.separator + "easymock.jar";//
		String[] args = new String[] {
				///
				"-dependencies", dep, "-mode", "statement", // "-failing",
															// "org.apache.commons.lang3.math.NumberUtilsTest",
															// //
				"-location", new File("./examples/lang_1/").getAbsolutePath(),
				//
				"-package", "org.apache.commons",
				//
				"-srcjavafolder", "/src/main/java/",
				//
				"-srctestfolder", "/src/main/test/",
				//
				"-binjavafolder", "/target/classes/",
				//
				"-bintestfolder", "/target/test-classes/",
				//
				"-javacompliancelevel", "6",
				//
				"-flthreshold", "0.1",
				//
				"-population", "1",
				//
				"-out", out.getAbsolutePath(), "-scope", "package", "-seed", "6320", "-maxgen", "50",
				//
				"-stopfirst", "true", "-maxtime", "5", (step) ? "-testbystep" : "",
				//
				"-loglevel", "DEBUG",

				//
				"-faultlocalization", GZoltarClientMasterFaultLocalization.class.getCanonicalName(),

				//
				"-regressiontestcases4fl",
				"org.apache.commons.lang3.BooleanUtilsTest:org.apache.commons.lang3.math.NumberUtilsTest:org.apache.commons.lang3.reflect.ConstructorUtilsTest:org.apache.commons.lang3.reflect.MethodUtilsTest",

		};
		return args;
	}
}
