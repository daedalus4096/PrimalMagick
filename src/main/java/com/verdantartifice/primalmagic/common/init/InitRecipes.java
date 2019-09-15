package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.IForgeRegistry;

public class InitRecipes {
    public static void initRecipeSerializers(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.register(new ShapelessArcaneRecipe.Serializer().setRegistryName(PrimalMagic.MODID, "arcane_crafting_shapeless"));
    }
}
