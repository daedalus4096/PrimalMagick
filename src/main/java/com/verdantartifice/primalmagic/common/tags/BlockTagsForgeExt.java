package com.verdantartifice.primalmagic.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class BlockTagsForgeExt {
    public static final Tag<Block> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final Tag<Block> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final Tag<Block> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");

    private static Tag<Block> tag(String name) {
        return new BlockTags.Wrapper(new ResourceLocation("forge", name));
    }
}
