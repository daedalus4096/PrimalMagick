package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlock;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlockTag;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.OrRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.Tags;

/**
 * Point of registration for mod recipe types, as well as other crafting related things.
 * 
 * @author Daedalus4096
 */
public class InitRecipes {
    public static void initWandTransforms() {
        WandTransforms.register(new WandTransformBlockTag(Tags.Blocks.BOOKSHELVES, new ItemStack(ItemsPM.GRIMOIRE.get()), new OrRequirement(new ResearchRequirement(new ResearchEntryKey(ResearchEntries.GOT_DREAM)), new ResearchRequirement(new ResearchStageKey(ResearchEntries.FIRST_STEPS, 1)))));
        WandTransforms.register(new WandTransformBlock(Blocks.CRAFTING_TABLE, new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), new ResearchRequirement(new ResearchStageKey(ResearchEntries.FIRST_STEPS, 1))));
        WandTransforms.register(new WandTransformBlock(Blocks.FURNACE, new ItemStack(BlocksPM.ESSENCE_FURNACE.get()), new ResearchRequirement(new ResearchStageKey(ResearchEntries.BASIC_ALCHEMY, 1))));
    }
    
    public static void initCompostables() {
        ComposterBlock.COMPOSTABLES.put(ItemsPM.MOONWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.MOONWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.SUNWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.SUNWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.HALLOWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.HALLOWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.HYDROMELON_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.HYDROMELON.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ItemsPM.BLOOD_ROSE.get(), 0.85F);
    }
}
