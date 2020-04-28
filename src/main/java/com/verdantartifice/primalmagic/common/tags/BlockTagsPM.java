package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Collection of custom-defined block tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class BlockTagsPM {
    public static final Tag<Block> BEDS = tag("beds");
    public static final Tag<Block> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final Tag<Block> CONCRETE = tag("concrete");
    public static final Tag<Block> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final Tag<Block> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final Tag<Block> DEAD_CORALS = tag("dead_corals");
    public static final Tag<Block> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final Tag<Block> SHULKER_BOXES = tag("shulker_boxes");
    public static final Tag<Block> SKYGLASS = tag("skyglass");
    public static final Tag<Block> SKYGLASS_PANES = tag("skyglass_panes");
    public static final Tag<Block> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final Tag<Block> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final Tag<Block> SUNWOOD_LOGS = tag("sunwood_logs");
    
    private static Tag<Block> tag(String name) {
        return new BlockTags.Wrapper(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
