package com.verdantartifice.primalmagick.client.gui.grimoire;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IShapedArcaneRecipePM;
import com.verdantartifice.primalmagick.common.crafting.IShapelessArcaneRecipePM;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Factory class to create an appropriate grimoire recipe page for a given recipe type.
 * 
 * @author Daedalus4096
 */
public class RecipePageFactory {
    @Nonnull
    public static AbstractRecipePage createPage(@Nonnull RecipeHolder<?> recipeHolder, RegistryAccess registryAccess) {
        Recipe<?> recipe = recipeHolder.value();
        if (recipe instanceof SmeltingRecipe sr) {
            return new SmeltingRecipePage(sr, registryAccess);
        } else if (recipe instanceof IRitualRecipe rr) {
            return new RitualRecipePage(rr, registryAccess);
        } else if (recipe instanceof IRunecarvingRecipe rr) {
            return new RunecarvingRecipePage(rr, registryAccess);
        } else if (recipe instanceof IConcoctingRecipe cr) {
            return new ConcoctingRecipePage(cr, registryAccess);
        } else if (recipe instanceof IDissolutionRecipe dr) {
            return new DissolutionRecipePage(dr, registryAccess);
        } else if (recipe instanceof IShapedArcaneRecipePM sar) {
            return new ShapedArcaneRecipePage(sar, registryAccess);
        } else if (recipe instanceof IShapelessArcaneRecipePM sar) {
            return new ShapelessArcaneRecipePage(sar, registryAccess);
        } else if (recipe instanceof IShapedRecipe sr) {
            return new ShapedRecipePage(sr, registryAccess);
        } else {
            return new ShapelessRecipePage(recipe, registryAccess);
        }
    }
}
