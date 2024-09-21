package com.verdantartifice.primalmagick.client.compat.jei;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * List of all the recipe types added by the mod.
 * 
 * @author Daedalus4096
 */
public class JeiRecipeTypesPM {
    public static final RecipeType<RecipeHolder<IArcaneRecipe>> ARCANE_CRAFTING = RecipeType.createFromVanilla(RecipeTypesPM.ARCANE_CRAFTING.get());
    public static final RecipeType<RecipeHolder<IConcoctingRecipe>> CONCOCTING = RecipeType.createFromVanilla(RecipeTypesPM.CONCOCTING.get());
    public static final RecipeType<RecipeHolder<IRunecarvingRecipe>> RUNECARVING = RecipeType.createFromVanilla(RecipeTypesPM.RUNECARVING.get());
    public static final RecipeType<RecipeHolder<IDissolutionRecipe>> DISSOLUTION = RecipeType.createFromVanilla(RecipeTypesPM.DISSOLUTION.get());
    public static final RecipeType<RecipeHolder<IRitualRecipe>> RITUAL = RecipeType.createFromVanilla(RecipeTypesPM.RITUAL.get());
}
