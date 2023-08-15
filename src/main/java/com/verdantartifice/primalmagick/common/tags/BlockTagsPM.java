package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Collection of custom-defined block tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class BlockTagsPM {
    public static final TagKey<Block> BEDS = tag("beds");
    public static final TagKey<Block> BOUNTY_CROPS = tag("bounty_crops");
    public static final TagKey<Block> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final TagKey<Block> CONCRETE = tag("concrete");
    public static final TagKey<Block> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final TagKey<Block> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final TagKey<Block> DEAD_CORALS = tag("dead_corals");
    public static final TagKey<Block> ENCHANTING_TABLES = tag("enchanting_tables");
    public static final TagKey<Block> HALLOWOOD_LOGS = tag("hallowood_logs");
    public static final TagKey<Block> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final TagKey<Block> RITUAL_CANDLES = tag("ritual_candles");
    public static final TagKey<Block> SHULKER_BOXES = tag("shulker_boxes");
    public static final TagKey<Block> SKYGLASS = tag("skyglass");
    public static final TagKey<Block> SKYGLASS_PANES = tag("skyglass_panes");
    public static final TagKey<Block> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final TagKey<Block> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final TagKey<Block> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final TagKey<Block> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final TagKey<Block> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");
    public static final TagKey<Block> SUNWOOD_LOGS = tag("sunwood_logs");
    public static final TagKey<Block> TREEFOLK_FERTILIZE_EXEMPT = tag("treefolk_fertilize_exempt");
    
    public static final TagKey<Block> MAY_PLACE_SUNWOOD_SAPLINGS = tag("may_place/sunwood_sapling");
    public static final TagKey<Block> MAY_PLACE_MOONWOOD_SAPLINGS = tag("may_place/moonwood_sapling");
    public static final TagKey<Block> MAY_PLACE_HALLOWOOD_SAPLINGS = tag("may_place/hallowood_sapling");
    
    private static TagKey<Block> tag(String name) {
        return BlockTags.create(PrimalMagick.resource(name));
    }
}
