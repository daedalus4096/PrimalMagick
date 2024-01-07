package com.verdantartifice.primalmagick.client.compat.jei;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.client.util.RecipeUtils;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Re-implementation of CategoryRecipeValidator from the full JEI mod, but only requiring the API.
 * 
 * @author Daedalus4096
 */
public class CategoryRecipeValidatorPM<T extends Recipe<?>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int INVALID_COUNT = -1;
    
    private final IRecipeCategory<RecipeHolder<T>> recipeCategory;
    private final int maxInputs;
    private final boolean checkSpecial;     // Check for valid inputs and outputs even for special recipes
    
    public CategoryRecipeValidatorPM(IRecipeCategory<RecipeHolder<T>> recipeCategory, int maxInputs) {
        this(recipeCategory, maxInputs, false);
    }

    public CategoryRecipeValidatorPM(IRecipeCategory<RecipeHolder<T>> recipeCategory, int maxInputs, boolean checkSpecial) {
        this.recipeCategory = recipeCategory;
        this.maxInputs = maxInputs;
        this.checkSpecial = checkSpecial;
    }

    public boolean isRecipeValid(RecipeHolder<T> recipe) {
        return hasValidInputsAndOutputs(recipe);
    }

    public boolean isRecipeHandled(RecipeHolder<T> recipe) {
        return this.recipeCategory.isHandled(recipe);
    }

    private boolean hasValidInputsAndOutputs(RecipeHolder<T> recipe) {
        if (recipe.value().isSpecial() && !this.checkSpecial) {
            return true;
        }
        
        ItemStack recipeOutput = RecipeUtils.getResultItem(recipe.value());
        if (recipeOutput == null || recipeOutput.isEmpty()) {
            LOGGER.error("Recipe has no output. {}", recipe.id().toString());
            return false;
        }
        
        List<Ingredient> ingredients = recipe.value().getIngredients();
        if (ingredients == null) {
            LOGGER.error("Recipe has no input Ingredients. {}", recipe.id().toString());
            return false;
        }
        
        int inputCount = getInputCount(ingredients);
        if (inputCount == INVALID_COUNT) {
            return false;
        } else if (inputCount > maxInputs) {
            LOGGER.error("Recipe has too many inputs. {}", recipe.id().toString());
            return false;
        } else if (inputCount == 0 && maxInputs > 0) {
            LOGGER.error("Recipe has no inputs. {}", recipe.id().toString());
            return false;
        }
        return true;
    }
    
    private static int getInputCount(List<Ingredient> ingredientList) {
        int inputCount = 0;
        for (Ingredient ingredient : ingredientList) {
            ItemStack[] input = ingredient.getItems();
            if (input == null) {
                return INVALID_COUNT;
            } else {
                inputCount++;
            }
        }
        return inputCount;
    }
}
