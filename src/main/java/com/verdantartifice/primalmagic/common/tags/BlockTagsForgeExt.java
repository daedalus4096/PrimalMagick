package com.verdantartifice.primalmagic.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class BlockTagsForgeExt {
    public static final IOptionalNamedTag<Block> BOOKSHELVES = tag("bookshelves");
    
    private static IOptionalNamedTag<Block> tag(String name) {
        return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
}
