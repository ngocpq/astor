package fr.inria.astor.approaches.adqfix;
import fr.inria.astor.core.entities.ProgramVariant;

public interface AdequateFixEvaluator {
	double computeAdequateFixLocationScore(ProgramVariant variant);
	void setOriginalVariant(ProgramVariant originVariant);
}
