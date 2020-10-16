package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

/**
 * Collection of custom-defined block tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class BlockTagsPM {
    public static final IOptionalNamedTag<Block> BEDS = tag("beds");
    public static final IOptionalNamedTag<Block> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final IOptionalNamedTag<Block> CONCRETE = tag("concrete");
    public static final IOptionalNamedTag<Block> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final IOptionalNamedTag<Block> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final IOptionalNamedTag<Block> DEAD_CORALS = tag("dead_corals");
    public static final IOptionalNamedTag<Block> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final IOptionalNamedTag<Block> RITUAL_CANDLES = tag("ritual_candles");
    public static final IOptionalNamedTag<Block> SHULKER_BOXES = tag("shulker_boxes");
    public static final IOptionalNamedTag<Block> SKYGLASS = tag("skyglass");
    public static final IOptionalNamedTag<Block> SKYGLASS_PANES = tag("skyglass_panes");
    public static final IOptionalNamedTag<Block> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final IOptionalNamedTag<Block> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");
    public static final IOptionalNamedTag<Block> SUNWOOD_LOGS = tag("sunwood_logs");
    
    private static IOptionalNamedTag<Block> tag(String name) {
    	return BlockTags.createOptional(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
