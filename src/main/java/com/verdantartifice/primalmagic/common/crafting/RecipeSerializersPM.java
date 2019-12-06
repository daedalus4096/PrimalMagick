package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(PrimalMagic.MODID)
public class RecipeSerializersPM {
    public static final IRecipeSerializer<ShapelessArcaneRecipe> ARCANE_CRAFTING_SHAPELESS = null;
    public static final IRecipeSerializer<ShapedArcaneRecipe> ARCANE_CRAFTING_SHAPED = null;
    public static final SpecialRecipeSerializer<WandAssemblyRecipe> WAND_ASSEMBLY_SPECIAL = null;
    public static final SpecialRecipeSerializer<WandInscriptionRecipe> WAND_INSCRIPTION_SPECIAL = null;
    public static final SpecialRecipeSerializer<SpellcraftingRecipe> SPELLCRAFTING_SPECIAL = null;
}
