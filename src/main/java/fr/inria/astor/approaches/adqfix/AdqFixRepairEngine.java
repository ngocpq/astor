package fr.inria.astor.approaches.adqfix;

import java.util.List;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.approaches.jgenprog.JGenProg;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.main.evolution.ExtensionPoints;

public class AdqFixRepairEngine extends JGenProg{

	AdequateFixEvaluator adequateFixLocationFactory;
	
	public AdqFixRepairEngine(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade) throws JSAPException {
		super(mutatorExecutor, projFacade);
		ConfigurationProperties.setProperty(ExtensionPoints.FAULT_LOCALIZATION.identifier, CoverageInfoCached_GZoltarFaultLocalization.class.getName());		
	}
	

	@Override
	public void initPopulation(List<SuspiciousCode> suspicious) throws Exception {
		// TODO Auto-generated method stub
		super.initPopulation(suspicious);		
		if (adequateFixLocationFactory==null && this.getFaultLocalization() instanceof CoverageInfoCached_GZoltarFaultLocalization){
			//CoverageInfoCached_GZoltarFaultLocalization sfl = (CoverageInfoCached_GZoltarFaultLocalization) this.getFaultLocalization();			
			adequateFixLocationFactory =new MinimalHittingSetAdequateFixEvaluator(this.projectFacade);
		}
	}
}
