package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTagsForgeExt {
    public static final TagKey<Block> BOOKSHELVES = tag("bookshelves");
    
    private static TagKey<Block> tag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
    }
}
