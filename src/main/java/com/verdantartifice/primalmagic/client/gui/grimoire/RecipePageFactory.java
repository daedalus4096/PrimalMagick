package com.verdantartifice.primalmagic.client.gui.grimoire;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.crafting.ConcoctingRecipe;
import com.verdantartifice.primalmagic.common.crafting.DissolutionRecipe;
import com.verdantartifice.primalmagic.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagic.common.crafting.RunecarvingRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/**
 * Factory class to create an appropriate grimoire recipe page for a given recipe type.
 * 
 * @author Daedalus4096
 */
public class RecipePageFactory {
    @Nullable
    public static AbstractRecipePage createPage(@Nonnull Recipe<?> recipe) {
        if (recipe instanceof ShapelessArcaneRecipe) {
            return new ShapelessArcaneRecipePage((ShapelessArcaneRecipe)recipe);
        } else if (recipe instanceof ShapedArcaneRecipe) {
            return new ShapedArcaneRecipePage((ShapedArcaneRecipe)recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            return new ShapelessRecipePage((ShapelessRecipe)recipe);
        } else if (recipe instanceof ShapedRecipe) {
            return new ShapedRecipePage((ShapedRecipe)recipe);
        } else if (recipe instanceof RitualRecipe) {
            return new RitualRecipePage((RitualRecipe)recipe);
        } else if (recipe instanceof RunecarvingRecipe) {
            return new RunecarvingRecipePage((RunecarvingRecipe)recipe);
        } else if (recipe instanceof ConcoctingRecipe) {
            return new ConcoctingRecipePage((ConcoctingRecipe)recipe);
        } else if (recipe instanceof SmeltingRecipe) {
            return new SmeltingRecipePage((SmeltingRecipe)recipe);
        } else if (recipe instanceof DissolutionRecipe) {
            return new DissolutionRecipePage((DissolutionRecipe)recipe);
        } else {
            return null;
        }
    }
}
