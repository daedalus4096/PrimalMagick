package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

/**
 * Collection of custom-defined item tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsPM {
    public static final IOptionalNamedTag<Item> BEDS = tag("beds");
    public static final IOptionalNamedTag<Item> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final IOptionalNamedTag<Item> CONCRETE = tag("concrete");
    public static final IOptionalNamedTag<Item> CORAL_BLOCKS = tag("coral_blocks");
    public static final IOptionalNamedTag<Item> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final IOptionalNamedTag<Item> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final IOptionalNamedTag<Item> DEAD_CORALS = tag("dead_corals");
    public static final IOptionalNamedTag<Item> ESSENCES = tag("essences");
    public static final IOptionalNamedTag<Item> HALLOWOOD_LOGS = tag("hallowood_logs");
    public static final IOptionalNamedTag<Item> MAGICAL_CLOTH = tag("magical_cloth");
    public static final IOptionalNamedTag<Item> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final IOptionalNamedTag<Item> RITUAL_CANDLES = tag("ritual_candles");
    public static final IOptionalNamedTag<Item> SHULKER_BOXES = tag("shulker_boxes");
    public static final IOptionalNamedTag<Item> SKYGLASS = tag("skyglass");
    public static final IOptionalNamedTag<Item> SKYGLASS_PANES = tag("skyglass_panes");
    public static final IOptionalNamedTag<Item> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final IOptionalNamedTag<Item> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final IOptionalNamedTag<Item> SUNWOOD_LOGS = tag("sunwood_logs");
    
    public static final IOptionalNamedTag<Item> ESSENCES_DUSTS = tag("essences/dusts");
    public static final IOptionalNamedTag<Item> ESSENCES_TERRESTRIAL_DUSTS = tag("essences/terrestrial_dusts");
    public static final IOptionalNamedTag<Item> ESSENCES_FORBIDDEN_DUSTS = tag("essences/forbidden_dusts");
    public static final IOptionalNamedTag<Item> ESSENCES_SHARDS = tag("essences/shards");
    public static final IOptionalNamedTag<Item> ESSENCES_TERRESTRIAL_SHARDS = tag("essences/terrestrial_shards");
    public static final IOptionalNamedTag<Item> ESSENCES_FORBIDDEN_SHARDS = tag("essences/forbidden_shards");
    public static final IOptionalNamedTag<Item> ESSENCES_CRYSTALS = tag("essences/crystals");
    public static final IOptionalNamedTag<Item> ESSENCES_TERRESTRIAL_CRYSTALS = tag("essences/terrestrial_crystals");
    public static final IOptionalNamedTag<Item> ESSENCES_FORBIDDEN_CRYSTALS = tag("essences/forbidden_crystals");
    public static final IOptionalNamedTag<Item> ESSENCES_CLUSTERS = tag("essences/clusters");
    public static final IOptionalNamedTag<Item> ESSENCES_TERRESTRIAL_CLUSTERS = tag("essences/terrestrial_clusters");
    public static final IOptionalNamedTag<Item> ESSENCES_FORBIDDEN_CLUSTERS = tag("essences/forbidden_clusters");
    
    public static final IOptionalNamedTag<Item> INGOTS_PRIMALITE = tag("ingots/primalite");
    public static final IOptionalNamedTag<Item> INGOTS_HEXIUM = tag("ingots/hexium");
    public static final IOptionalNamedTag<Item> INGOTS_HALLOWSTEEL = tag("ingots/hallowsteel");
    public static final IOptionalNamedTag<Item> NUGGETS_PRIMALITE = tag("nuggets/primalite");
    public static final IOptionalNamedTag<Item> NUGGETS_HEXIUM = tag("nuggets/hexium");
    public static final IOptionalNamedTag<Item> NUGGETS_HALLOWSTEEL = tag("nuggets/hallowsteel");
    public static final IOptionalNamedTag<Item> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final IOptionalNamedTag<Item> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final IOptionalNamedTag<Item> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");

    private static IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation(PrimalMagick.MODID, name));
    }
}
