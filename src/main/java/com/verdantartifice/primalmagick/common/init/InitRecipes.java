package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlock;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlockTag;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

/**
 * Point of registration for mod recipe types, as well as other crafting related things.
 * 
 * @author Daedalus4096
 */
public class InitRecipes {
    public static void initWandTransforms() {
        WandTransforms.register(new WandTransformBlockTag(BlockTagsForgeExt.BOOKSHELVES, new ItemStack(ItemsPM.GRIMOIRE.get()), CompoundResearchKey.from(false, SimpleResearchKey.parse("t_got_dream"), SimpleResearchKey.parse("FIRST_STEPS"))));
        WandTransforms.register(new WandTransformBlock(Blocks.CRAFTING_TABLE, new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), CompoundResearchKey.from(SimpleResearchKey.parse("FIRST_STEPS@1"))));
        WandTransforms.register(new WandTransformBlock(Blocks.FURNACE, new ItemStack(BlocksPM.ESSENCE_FURNACE.get()), CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_ALCHEMY@1"))));
    }
}
