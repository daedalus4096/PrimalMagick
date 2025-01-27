package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockExtensionTags {
    public static final TagKey<Block> ORES_ROCK_SALT = tag("ores/rock_salt");
    public static final TagKey<Block> ORES_TIN = tag("ores/tin");
    public static final TagKey<Block> ORES_LEAD = tag("ores/lead");
    public static final TagKey<Block> ORES_SILVER = tag("ores/silver");
    public static final TagKey<Block> ORES_URANIUM = tag("ores/uranium");
    
    public static final TagKey<Block> FURNACES = tag("furnaces");
    public static final TagKey<Block> MINEABLE_WITH_SHEARS = tag("mineable/shears");

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
