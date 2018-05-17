package fr.inria.astor.approaches.adqfix;

import java.util.List;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.approaches.adqfix.loop.AdequateChangeBasedFitnessPopulationController;
import fr.inria.astor.approaches.adqfix.mhs.MinimalHittingSetAdequateFixLocalization;
import fr.inria.astor.approaches.jgenprog.JGenProg;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.setup.ProjectRepairFacade;

public class AdqFixRepairEngine extends JGenProg{

	AdequateFixLocationStrategy adequateFixLocationStrategy;
	//AdequateFixEvaluator adequateFixEvaluator;
	
	public AdqFixRepairEngine(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade) throws JSAPException {
		super(mutatorExecutor, projFacade);
		this.pluginLoaded = new AdqFixRepairPlugInloader();						
	}
	

	@Override
	public void initPopulation(List<SuspiciousCode> suspicious) throws Exception {
		
		CoverageInfoCached_GZoltarFaultLocalization ffl = (CoverageInfoCached_GZoltarFaultLocalization)this.getFaultLocalization();
		AdequateFixLocationFitnessFunction fitnessFunc = (AdequateFixLocationFitnessFunction)this.getFitnessFunction();
		
		adequateFixLocationStrategy = new MinimalHittingSetAdequateFixLocalization(ffl.getTestCaseIdMapping());		
		
		adequateFixLocationStrategy.initAdequateFixLocationStrategy(suspicious,this.getProjectFacade().getProperties().getFailingTestCases());
		
		fitnessFunc.setAdequateFixLocationChecker(adequateFixLocationStrategy);
		
		if (this.getPopulationControler() instanceof AdequateChangeBasedFitnessPopulationController){
			((AdequateChangeBasedFitnessPopulationController)this.getPopulationControler()).setAdequateFixLocationChecker(adequateFixLocationStrategy);
		}
		
		super.initPopulation(suspicious);
	}
}
