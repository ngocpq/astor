package fr.inria.astor.approaches.adqfix;

import fr.inria.astor.approaches.jgenprog.jGenProgPlugInLoader;
import fr.inria.astor.core.faultlocalization.FaultLocalizationStrategy;
import fr.inria.astor.core.faultlocalization.cocospoon.CocoFaultLocalization;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarFaultLocalization;
import fr.inria.astor.core.loop.AstorCoreEngine;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.main.evolution.ExtensionPoints;
import fr.inria.main.evolution.PlugInLoader;

public class AdqFixRepairPlugInloader extends jGenProgPlugInLoader {
	@Override
	protected void loadFitnessFunction(AstorCoreEngine approach) throws Exception {
		// TODO Auto-generated method stub
		approach.setFitnessFunction(new AdequateFixLocationFitnessFunction());
	}
	
	@Override
	protected void loadFaultLocalization(AstorCoreEngine approach) throws Exception {
		// Fault localization
		String flvalue = ConfigurationProperties.getProperty("faultlocalization").toLowerCase();
		approach.setFaultLocalization(new CoverageInfoCached_GZoltarFaultLocalization());		
	}
}
