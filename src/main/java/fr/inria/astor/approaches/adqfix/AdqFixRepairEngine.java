package fr.inria.astor.approaches.adqfix;

import java.util.List;

import com.martiansoftware.jsap.JSAPException;

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
		
		adequateFixLocationStrategy = new MinimalHittingSetAdequateFixLocalization();				
	}
	

	@Override
	public void initPopulation(List<SuspiciousCode> suspicious) throws Exception {
		// TODO Auto-generated method stub
		super.initPopulation(suspicious);
		this.setFitnessFunction(fitnessFunction);
		adequateFixLocationStrategy.setSuspiciousCode(suspicious);
	}
}
