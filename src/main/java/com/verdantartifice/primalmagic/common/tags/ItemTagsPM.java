package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Collection of custom-defined item tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsPM {
    public static final Tag<Item> BEDS = tag("beds");
    public static final Tag<Item> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final Tag<Item> CONCRETE = tag("concrete");
    public static final Tag<Item> CORAL_BLOCKS = tag("coral_blocks");
    public static final Tag<Item> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final Tag<Item> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final Tag<Item> DEAD_CORALS = tag("dead_corals");
    public static final Tag<Item> ESSENCES = tag("essences");
    public static final Tag<Item> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final Tag<Item> SHULKER_BOXES = tag("shulker_boxes");
    public static final Tag<Item> SKYGLASS = tag("skyglass");
    public static final Tag<Item> SKYGLASS_PANES = tag("skyglass_panes");
    public static final Tag<Item> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final Tag<Item> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final Tag<Item> SUNWOOD_LOGS = tag("sunwood_logs");
    
    public static final Tag<Item> ESSENCES_DUSTS = tag("essences/dusts");
    public static final Tag<Item> ESSENCES_TERRESTRIAL_DUSTS = tag("essences/terrestrial_dusts");
    public static final Tag<Item> ESSENCES_SHARDS = tag("essences/shards");
    public static final Tag<Item> ESSENCES_CRYSTALS = tag("essences/crystals");
    public static final Tag<Item> ESSENCES_CLUSTERS = tag("essences/clusters");
    
    public static final Tag<Item> INGOTS_PRIMALITE = tag("ingots/primalite");
    public static final Tag<Item> INGOTS_HEXIUM = tag("ingots/hexium");
    public static final Tag<Item> INGOTS_HALLOWSTEEL = tag("ingots/hallowsteel");
    public static final Tag<Item> NUGGETS_PRIMALITE = tag("nuggets/primalite");
    public static final Tag<Item> NUGGETS_HEXIUM = tag("nuggets/hexium");
    public static final Tag<Item> NUGGETS_HALLOWSTEEL = tag("nuggets/hallowsteel");
    public static final Tag<Item> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final Tag<Item> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final Tag<Item> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");

    private static Tag<Item> tag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
