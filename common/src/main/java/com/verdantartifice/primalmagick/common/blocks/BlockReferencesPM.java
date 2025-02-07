package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class BlockReferencesPM {
    public static final ResourceKey<Block> HYDROMELON = createKey("hydromelon");
    public static final ResourceKey<Block> HYDROMELON_STEM = createKey("hydromelon_stem");
    public static final ResourceKey<Block> ATTACHED_HYDROMELON_STEM = createKey("attached_hydromelon_stem");
    public static final ResourceKey<Block> GLOW_FIELD = createKey("glow_field");
    public static final ResourceKey<Block> SOUL_GLOW_FIELD = createKey("soul_glow_field");
    
    private static ResourceKey<Block> createKey(String id) {
        return ResourceKey.create(Registries.BLOCK, ResourceUtils.loc(id));
    }
}
