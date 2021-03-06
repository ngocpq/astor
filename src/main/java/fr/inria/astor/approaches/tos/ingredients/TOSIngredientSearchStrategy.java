package fr.inria.astor.approaches.tos.ingredients;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import fr.inria.astor.approaches.tos.core.PatchGenerator;
import fr.inria.astor.approaches.tos.entity.TOSEntity;
import fr.inria.astor.approaches.tos.entity.TOSIngredient;
import fr.inria.astor.approaches.tos.entity.placeholders.Placeholder;
import fr.inria.astor.approaches.tos.entity.transf.Transformation;
import fr.inria.astor.core.entities.Ingredient;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.loop.spaces.ingredients.IngredientSearchStrategy;
import fr.inria.astor.core.loop.spaces.ingredients.IngredientSpace;
import fr.inria.astor.core.loop.spaces.operators.AstorOperator;
import fr.inria.astor.core.setup.RandomManager;
import fr.inria.astor.util.StringUtil;

/**
 * 
 * @author Matias Martinez
 *
 */
public class TOSIngredientSearchStrategy extends IngredientSearchStrategy {

	MultiKeyMap cacheInstances = new MultiKeyMap();
	protected static Logger log = Logger.getLogger(Thread.currentThread().getName());

	public TOSIngredientSearchStrategy(IngredientSpace space) {
		super(space);

		try {

		} catch (Exception e) {

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ingredient getFixIngredient(ModificationPoint modificationPoint, AstorOperator operationType) {

		List<Ingredient> baseIngedients = this.ingredientSpace.getIngredients(modificationPoint.getCodeElement(),
				modificationPoint.getCodeElement().getClass().getSimpleName());

		if (baseIngedients == null) {
			log.debug("No elements ingredients for mp " + modificationPoint);
			return null;
		}
		log.debug("elements for mp " + modificationPoint + " " + baseIngedients.size());

		int randomIndex = RandomManager.nextInt(baseIngedients.size());
		// We have randomly selected one Ingredient base
		Ingredient ingredientBaseSelected = baseIngedients.get(randomIndex);

		List<TOSIngredient> ingredientTransformed = getInstances(modificationPoint, ingredientBaseSelected);

		if (ingredientTransformed == null || ingredientTransformed.isEmpty()) {
			log.debug("No instance of ingredients for mp " + modificationPoint + " and "
					+ ingredientBaseSelected.getChacheCodeString());
			return null;
		}

		randomIndex = RandomManager.nextInt(ingredientTransformed.size());
		TOSIngredient ding = ingredientTransformed.remove(randomIndex);
		ding.generatePatch();
		return ding;

	}

	@SuppressWarnings("unchecked")
	public List<TOSIngredient> getInstances(ModificationPoint modificationPoint, Ingredient ingredientBaseSelected) {
		// Now, let's get the ingredients.
		List<TOSIngredient> ingredientTransformed = null;
		if (this.cacheInstances.containsKey(modificationPoint, ingredientBaseSelected.getChacheCodeString())) {
			ingredientTransformed = (List<TOSIngredient>) this.cacheInstances.get(modificationPoint,
					ingredientBaseSelected.getChacheCodeString());
			log.debug("Ingredients "+StringUtil.trunc(ingredientBaseSelected.getChacheCodeString())
					+ " cache of size " + ingredientTransformed.size());

		} else {

			log.debug("Not in cache, generating for " + modificationPoint + " and "
					+ ingredientBaseSelected.getChacheCodeString());

			PatchGenerator v = new PatchGenerator();

			TOSEntity tos = (TOSEntity) ingredientBaseSelected;
			ingredientTransformed = new ArrayList<>();
			for (Placeholder placeholder : tos.getPlaceholders()) {
				List<Transformation> transpl = placeholder.visit(modificationPoint, v);

				if (ingredientTransformed.isEmpty()) {
					for (Transformation transformation : transpl) {
						TOSIngredient tosIn = new TOSIngredient(tos.getCode(), tos);
						tosIn.getTransformations().add(transformation);
						ingredientTransformed.add(tosIn);
					}

				} else {
					for (Transformation transformation : transpl) {
						for (TOSIngredient tosIngredient : ingredientTransformed) {
							tosIngredient.getTransformations().add(transformation);
						}
					}
				}
			}

			this.cacheInstances.put(modificationPoint, ingredientBaseSelected.getChacheCodeString(),
					ingredientTransformed);

		}
		return ingredientTransformed;
	}

	public MultiKeyMap getCacheInstances() {
		return cacheInstances;
	}

}
