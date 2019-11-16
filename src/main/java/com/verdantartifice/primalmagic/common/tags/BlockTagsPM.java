package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class BlockTagsPM {
    public static final Tag<Block> BEDS = tag("beds");
    public static final Tag<Block> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final Tag<Block> CONCRETE = tag("concrete");
    public static final Tag<Block> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final Tag<Block> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final Tag<Block> DEAD_CORALS = tag("dead_corals");
    public static final Tag<Block> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final Tag<Block> SHULKER_BOXES = tag("shulker_boxes");
    public static final Tag<Block> SUNWOOD_LOGS = tag("sunwood_logs");
    
    private static Tag<Block> tag(String name) {
        return new BlockTags.Wrapper(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
