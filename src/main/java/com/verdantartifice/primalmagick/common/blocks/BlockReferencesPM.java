package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class BlockReferencesPM {
    public static final ResourceKey<Block> HYDROMELON = createKey("hydromelon");
    public static final ResourceKey<Block> HYDROMELON_STEM = createKey("hydromelon_stem");
    public static final ResourceKey<Block> ATTACHED_HYDROMELON_STEM = createKey("attached_hydromelon_stem");
    
    private static ResourceKey<Block> createKey(String id) {
        return ResourceKey.create(Registries.BLOCK, PrimalMagick.resource(id));
    }
}
