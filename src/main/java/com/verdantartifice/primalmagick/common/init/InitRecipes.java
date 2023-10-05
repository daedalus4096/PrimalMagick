package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlock;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlockTag;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

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
        WandTransforms.register(new WandTransformBlockTag(Tags.Blocks.BOOKSHELVES, new ItemStack(ItemsPM.GRIMOIRE.get()), CompoundResearchKey.from(false, SimpleResearchKey.find("t_got_dream").orElseThrow(), SimpleResearchKey.FIRST_STEPS)));
        WandTransforms.register(new WandTransformBlock(Blocks.CRAFTING_TABLE, new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), CompoundResearchKey.from(ResearchNames.FIRST_STEPS.get().simpleKey(1))));
        WandTransforms.register(new WandTransformBlock(Blocks.FURNACE, new ItemStack(BlocksPM.ESSENCE_FURNACE.get()), CompoundResearchKey.from(ResearchNames.BASIC_ALCHEMY.get().simpleKey(1))));
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
