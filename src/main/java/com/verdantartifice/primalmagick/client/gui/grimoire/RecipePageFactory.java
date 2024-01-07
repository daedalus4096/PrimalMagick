package com.verdantartifice.primalmagick.client.gui.grimoire;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.crafting.ConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.DissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.RunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
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
    public static AbstractRecipePage createPage(@Nonnull RecipeHolder<?> recipeHolder, RegistryAccess registryAccess) {
        Recipe<?> recipe = recipeHolder.value();
        if (recipe instanceof ShapelessArcaneRecipe sar) {
            return new ShapelessArcaneRecipePage(sar, registryAccess);
        } else if (recipe instanceof ShapedArcaneRecipe sar) {
            return new ShapedArcaneRecipePage(sar, registryAccess);
        } else if (recipe instanceof ShapelessRecipe sr) {
            return new ShapelessRecipePage(sr, registryAccess);
        } else if (recipe instanceof ShapedRecipe sr) {
            return new ShapedRecipePage(sr, registryAccess);
        } else if (recipe instanceof RitualRecipe rr) {
            return new RitualRecipePage(rr, registryAccess);
        } else if (recipe instanceof RunecarvingRecipe rr) {
            return new RunecarvingRecipePage(rr, registryAccess);
        } else if (recipe instanceof ConcoctingRecipe cr) {
            return new ConcoctingRecipePage(cr, registryAccess);
        } else if (recipe instanceof SmeltingRecipe sr) {
            return new SmeltingRecipePage(sr, registryAccess);
        } else if (recipe instanceof DissolutionRecipe dr) {
            return new DissolutionRecipePage(dr, registryAccess);
        } else {
            return null;
        }
    }
}
