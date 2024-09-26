package com.verdantartifice.primalmagick.common.blocks.base;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Extension of the vanilla attached stem with a public constructor.
 *
 * @author Daedalus4096
 */
public class AttachedStemBlockPM extends AttachedStemBlock {
    public AttachedStemBlockPM(ResourceKey<Block> baseStem, ResourceKey<Block> fruit, ResourceKey<Item> seeds, BlockBehaviour.Properties properties) {
        super(baseStem, fruit, seeds, properties);
    }
}
