package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(PrimalMagic.MODID)
public class RecipeSerializersPM {
    public static final IRecipeSerializer<ShapelessArcaneRecipe> ARCANE_CRAFTING_SHAPELESS = null;
    public static final IRecipeSerializer<ShapedArcaneRecipe> ARCANE_CRAFTING_SHAPED = null;
    public static final IRecipeSerializer<WandAssemblyRecipe> WAND_ASSEMBLY_SPECIAL = null;
}
