package fr.inria.astor.approaches.adqfix;

import java.util.List;

import fr.inria.astor.approaches.adqfix.model.MinimalCorrectionLocationSet;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.setup.ProjectRepairFacade;

public interface AdequateFixLocationStrategy{
	//void setSuspiciousCode(List<SuspiciousCode> suspicious);
	List<MinimalCorrectionLocationSet> getAllAdequateFixes();
	//List<AdequateFixLocation> geneneAdequateFixes(List<SuspiciousCode> suspicious);
	void initAdequateFixLocationStrategy(List<SuspiciousCode> suspicious, List<String> failingTestCases);
}
