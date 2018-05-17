package fr.inria.astor.approaches.adqfix.loop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import fr.inria.astor.approaches.adqfix.AdequateFixLocationStrategy;
import fr.inria.astor.approaches.adqfix.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.model.Diagnosis.SubsumeCheck;
import fr.inria.astor.approaches.adqfix.model.DiagnosisSet;
import fr.inria.astor.approaches.adqfix.uti.ProgramVariant_Helper;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.loop.population.PopulationConformation;
import fr.inria.astor.core.loop.population.PopulationController;
import fr.inria.astor.core.loop.population.ProgramVariantFactory;
import fr.inria.astor.core.setup.ConfigurationProperties;

public class AdequateChangeBasedFitnessPopulationController implements PopulationController {
	private Logger log = Logger.getLogger(Thread.currentThread().getName());

	public FitnessComparator comparator = new FitnessComparator();

	private AdequateFixLocationStrategy<SuspiciousCode> adequateFixLocationCheker;

	/**
	 * Select the program instances that will pass to the next generation. The
	 * rest are discarted.
	 * 
	 * @param instances2
	 * @param childVariants
	 */
	@Override
	public List<ProgramVariant> selectProgramVariantsForNextGeneration(List<ProgramVariant> parentVariants,
			List<ProgramVariant> childVariants, int populationSize, ProgramVariantFactory variantFactory,
			ProgramVariant original, int generation) {
		
		//TODO: implementing genetic selection operation
		
		List<ProgramVariant> solutionsFromGeneration = new ArrayList<ProgramVariant>();

		// It defines the new population (i.e., the population of the next
		// generation) with all new program variants created in the current
		// population.
		List<ProgramVariant> newPopulation = new ArrayList<>(childVariants);

		// If parents can be evolved, we add it to the new population.
		// Otherwise, they are discarded.
		if (ConfigurationProperties.getProperty("reintroduce").contains(PopulationConformation.PARENTS.toString())) {
			newPopulation.addAll(parentVariants);
		}

		int totalInstances = newPopulation.size();

		// The new population is sorted according to the fitness.
		Collections.sort(newPopulation, comparator);

		String variantsIds = "";

		// Only prints the population.
		for (ProgramVariant programVariant : newPopulation) {
			variantsIds += programVariant.getId() + "(f=" + programVariant.getFitness() + ")";
			if (programVariant.isSolution()) {
				solutionsFromGeneration.add(programVariant);
				variantsIds += "[SOL]";
			}
			variantsIds += ", ";
		}

		log.debug("Variants to next generation from: " + totalInstances + "-->IDs: (" + variantsIds + ")");

		// If solution can be evolved, we keep them, otherwise, they are
		// discarded.
		if (!ConfigurationProperties.getProperty("reintroduce").contains(PopulationConformation.SOLUTIONS.toString())) {
			newPopulation.removeAll(solutionsFromGeneration);
		}

		// We take the best X variants, where X is the size of the population
		int min = (newPopulation.size() > populationSize) ? populationSize : newPopulation.size();
		newPopulation = newPopulation.subList(0, min);

		variantsIds = "";
		for (ProgramVariant programVariant : newPopulation) {
			variantsIds += programVariant.getId() + "(f=" + programVariant.getFitness() + ")" + ", ";
		}
		log.debug("Selected to next generation: IDs" + totalInstances + "--> (" + variantsIds + ")");
		//
		// If the original variant has to be reintroduced on each generation, we
		// remove the "worst" variant from those selected in the previous step.
		if (ConfigurationProperties.getProperty("reintroduce").contains(PopulationConformation.ORIGINAL.toString())) {
			if (!newPopulation.isEmpty()) {
				// now replace for the "worse" child
				newPopulation.remove(newPopulation.size() - 1);
			}
		}

		// if the number of variant after L77 and L62 is lower than the
		// population size, we put in the population original variants until
		// arrive to the desired population size
		while (newPopulation.size() < populationSize) {
			ProgramVariant originalVariant = variantFactory.createProgramVariantFromAnother(original, generation);
			originalVariant.getOperations().clear();
			originalVariant.setParent(null);
			newPopulation.add(originalVariant);
		}

		return newPopulation;
	}
	
	/**
	 * Comparator to sort the variant in ascending mode according to the fitness
	 * values
	 * 
	 * @author Matias Martinez, matias.martinez@inria.fr
	 *
	 */
	public class FitnessComparator implements Comparator<ProgramVariant> {

		@Override
		public int compare(ProgramVariant o1, ProgramVariant o2) {
			int fitness = Double.compare(o1.getFitness(), o2.getFitness());
			if (fitness != 0)
				return fitness;
			//we prefer the one that have higher adequate modification score
			List<SuspiciousCode> mod1= ProgramVariant_Helper.getModifiedCodes(o1);
			List<SuspiciousCode> mod2 = ProgramVariant_Helper.getModifiedCodes(o1);
			
			DiagnosisSet<SuspiciousCode> dianosisSet = adequateFixLocationCheker.getDiagnosisSet();
			List<Diagnosis<SuspiciousCode>> subsumed1 = dianosisSet.findSubsumedSets(mod1);
			List<Diagnosis<SuspiciousCode>> subsumed2 = dianosisSet.findSubsumedSets(mod1);
			
			if (subsumed1.isEmpty() && subsumed2.isEmpty())
				return Integer.compare(o1.getId(), o2.getId());
			
			if (subsumed1.isEmpty() || subsumed2.isEmpty()){
				//one is inadequate the other is adquate or overAdequate
				int adq = Integer.compare(subsumed1.size(),subsumed2.size());
				if (adq!=0)
					return adq;
				return Integer.compare(o1.getId(), o2.getId());
			}
			
			boolean isMinAdq1 =subsumed1.get(0).checkSubsume(mod1)== SubsumeCheck.EQUAL; 
			boolean isMinAdq2 =subsumed2.get(0).checkSubsume(mod2)== SubsumeCheck.EQUAL;
			
			if (isMinAdq1 && isMinAdq2) {					
				//both mod1 and mod2 are minimal adequate => the newer is better
				return Integer.compare(o1.getId(), o2.getId());
			}
			
			if (isMinAdq1)
				return -1;
			if (isMinAdq2)
				return 1;
			
			//both are over adequate => the smaller is the better?!
			int sizeCompare = Integer.compare(mod1.size(), mod2.size());
			if (sizeCompare!=0)
				return sizeCompare;
			
			// inversed, we prefer have child variant first
			return Integer.compare(o1.getId(), o2.getId());
		}

	}

	public void setAdequateFixLocationChecker(AdequateFixLocationStrategy adequateFixLocationCheker) {
		this.adequateFixLocationCheker = adequateFixLocationCheker;
	}
}
