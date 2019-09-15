package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitRecipes {
    public static void initRecipeTypes() {
        RecipeTypesPM.ARCANE_CRAFTING = IRecipeType.register(PrimalMagic.MODID + ":arcane_crafting");
    }
    
    public static void initRecipeSerializers(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.register(new ShapelessArcaneRecipe.Serializer().setRegistryName(PrimalMagic.MODID, "arcane_crafting_shapeless"));
    }
}
