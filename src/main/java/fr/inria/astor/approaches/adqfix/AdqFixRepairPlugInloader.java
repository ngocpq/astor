package fr.inria.astor.approaches.adqfix;

import fr.inria.astor.approaches.adqfix.loop.AdequateChangeBasedFitnessPopulationController;
import fr.inria.astor.approaches.jgenprog.jGenProgPlugInLoader;
import fr.inria.astor.core.loop.AstorCoreEngine;
import fr.inria.astor.core.loop.population.PopulationController;

public class AdqFixRepairPlugInloader extends jGenProgPlugInLoader {
	@Override
	protected void loadFitnessFunction(AstorCoreEngine approach) throws Exception {
		// TODO Auto-generated method stub
		approach.setFitnessFunction(new AdequateFixLocationFitnessFunction());
	}
	
	@Override
	protected void loadFaultLocalization(AstorCoreEngine approach) throws Exception {
		approach.setFaultLocalization(new CoverageInfoCached_GZoltarFaultLocalization());
	}
	
	protected void loadPopulation(AstorCoreEngine approach) throws Exception {
		//super.loadPopulation(approach);
		PopulationController popController=new AdequateChangeBasedFitnessPopulationController();
		approach.setPopulationControler(popController);		
	}
	
	@Override
	protected void loadValidator(AstorCoreEngine approach) throws Exception {		
		AdqFixValidator validator = new AdqFixValidator();
		approach.setProgramValidator(validator);
	}
	
	@Override
	public void load(AstorCoreEngine approach) throws Exception {
		// TODO Auto-generated method stub
		super.load(approach);
	}
	
	@Override
	protected void loadOperatorSelectorStrategy(AstorCoreEngine approach) throws Exception {
		// TODO Auto-generated method stub
		super.loadOperatorSelectorStrategy(approach);
	}
	
	@Override
	public void loadOperatorSpaceDefinition(AstorCoreEngine approach) throws Exception {
		// TODO Auto-generated method stub
		super.loadOperatorSpaceDefinition(approach);
	}
}
