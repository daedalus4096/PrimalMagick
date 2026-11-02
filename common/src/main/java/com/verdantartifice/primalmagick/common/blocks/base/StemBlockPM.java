package com.verdantartifice.primalmagick.common.blocks.base;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Extension of the vanilla stem with a public constructor.
 *
 * @author Daedalus4096
 */
public class StemBlockPM extends StemBlock {
    public StemBlockPM(ResourceKey<Block> fruit, ResourceKey<Block> attachedStem, ResourceKey<Item> seeds, TagKey<Block> stemSupportBlocks, TagKey<Block> fruitSupportBlocks, BlockBehaviour.Properties properties) {
        super(fruit, attachedStem, seeds, stemSupportBlocks, fruitSupportBlocks, properties);
    }
}
