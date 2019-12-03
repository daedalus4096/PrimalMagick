package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandAssemblyRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandInscriptionRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandTransformBlock;
import com.verdantartifice.primalmagic.common.crafting.WandTransforms;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.registries.IForgeRegistry;

public class InitRecipes {
    public static void initRecipeTypes() {
        RecipeTypesPM.ARCANE_CRAFTING = IRecipeType.register(PrimalMagic.MODID + ":arcane_crafting");
    }
    
    public static void initRecipeSerializers(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.register(new ShapelessArcaneRecipe.Serializer().setRegistryName(PrimalMagic.MODID, "arcane_crafting_shapeless"));
        registry.register(new ShapedArcaneRecipe.Serializer().setRegistryName(PrimalMagic.MODID, "arcane_crafting_shaped"));
        registry.register(new SpecialRecipeSerializer<>(WandAssemblyRecipe::new).setRegistryName(PrimalMagic.MODID, "wand_assembly_special"));
        registry.register(new SpecialRecipeSerializer<>(WandInscriptionRecipe::new).setRegistryName(PrimalMagic.MODID, "wand_inscription_special"));
    }
    
    public static void initWandTransforms() {
        WandTransforms.register(new WandTransformBlock(Blocks.BOOKSHELF, new ItemStack(ItemsPM.GRIMOIRE), CompoundResearchKey.from(SimpleResearchKey.parse("FIRST_STEPS"))));
        WandTransforms.register(new WandTransformBlock(Blocks.CRAFTING_TABLE, new ItemStack(BlocksPM.ARCANE_WORKBENCH), CompoundResearchKey.from(SimpleResearchKey.parse("FIRST_STEPS"))));
        WandTransforms.register(new WandTransformBlock(Blocks.FURNACE, new ItemStack(BlocksPM.CALCINATOR), CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ALCHEMY"))));
    }
}
