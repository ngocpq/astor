package fr.inria.astor.approaches.exhaustive;

import java.util.ArrayList;
import java.util.List;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.approaches.jgenprog.operators.ReplaceOp;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.loop.spaces.ingredients.IngredientSpace;
import fr.inria.astor.core.loop.spaces.ingredients.ingredientSearch.CloneIngredientSearchStrategy4Exhaustive;
import fr.inria.astor.core.loop.spaces.operators.AstorOperator;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.manipulation.filters.SingleStatementFixSpaceProcessor;
import fr.inria.astor.core.manipulation.filters.TargetElementProcessor;
import fr.inria.astor.core.manipulation.sourcecode.VariableResolver;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.main.evolution.ExtensionPoints;
import fr.inria.main.evolution.PlugInLoader;
import spoon.reflect.code.CtCodeElement;

/**
 * 
 * @author Matias Martinez
 *
 */
public class ExhausitiveCloneEngine extends ExhaustiveIngredientBasedEngine {

	public ExhausitiveCloneEngine(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade)
			throws JSAPException {
		super(mutatorExecutor, projFacade);
		
		List<TargetElementProcessor<?>> ingredientProcessors = new ArrayList<TargetElementProcessor<?>>();
		// Fix Space
		ingredientProcessors.add(new SingleStatementFixSpaceProcessor());

		try {
			this.ingredientSpace = (IngredientSpace) PlugInLoader.loadPlugin(ExtensionPoints.INGREDIENT_STRATEGY_SCOPE,
					new Class[] { List.class }, new Object[] { ingredientProcessors });
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<OperatorInstance> createInstance(SuspiciousModificationPoint modificationPoint,
			AstorOperator astorOperator) {
		
		List<OperatorInstance> ops = new ArrayList<>();
		log.debug("\n---------\n STEP 1:-----Find Ingredients------");
		log.debug("\n->ModPoint: \n"+modificationPoint.getCodeElement());
		
		CloneIngredientSearchStrategy4Exhaustive repairengine = null;
		
		try {
			repairengine = new CloneIngredientSearchStrategy4Exhaustive(ingredientSpace);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<CtCodeElement>  ingredients = null;
		if (astorOperator instanceof ReplaceOp) {
			String type = modificationPoint.getCodeElement().getClass().getSimpleName();
			ingredients = ingredientSpace.getIngredients(modificationPoint.getCodeElement(), type);

		} else {
			ingredients = ingredientSpace.getIngredients(modificationPoint.getCodeElement());

		}
		
		ingredients = repairengine.getAllFixIngredient(modificationPoint, astorOperator);
		
		if(ingredients == null || ingredients.isEmpty()){
			log.debug("Any ingredient for mp "+ modificationPoint + " and op "+astorOperator);
			return ops;
		}
		
		log.debug("\n************\n STEP 2:  Apply ingredients. Number of ingredients " + ingredients.size());
		for (CtCodeElement ingredient : ingredients) {

				
			if(!VariableResolver.fitInPlace(modificationPoint.getContextOfModificationPoint(),
					ingredient)){
				log.debug("Ingredient not fix in place: "+ingredient);
				continue;
			}else{
				log.debug("Accepting Ingredient: "+ingredient);
			}
		
			List<OperatorInstance> instances = astorOperator.createOperatorInstance(modificationPoint);
			
			if (instances != null && instances.size() > 0) {
				
				for (OperatorInstance operatorInstance : instances) {
					
					operatorInstance.setModified(ingredient);
					
					ops.add(operatorInstance);
				}
			}
		}
		return ops;
	}
}
