package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Collection of custom-defined item tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsPM {
    public static final TagKey<Item> ANALYSIS_TABLE_FORBIDDEN = tag("analysis_table_forbidden");
    public static final TagKey<Item> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final TagKey<Item> CONCRETE = tag("concrete");
    public static final TagKey<Item> CORAL_BLOCKS = tag("coral_blocks");
    public static final TagKey<Item> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final TagKey<Item> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final TagKey<Item> DEAD_CORALS = tag("dead_corals");
    public static final TagKey<Item> DEEP_STONE = tag("deep_stone");
    public static final TagKey<Item> ENCHANTING_TABLES = tag("enchanting_tables");
    public static final TagKey<Item> ESSENCES = tag("essences");
    public static final TagKey<Item> HALLOWOOD_LOGS = tag("hallowood_logs");
    public static final TagKey<Item> INFERNAL_SUPERCHARGE_FUEL = tag("infernal_supercharge_fuel");
    public static final TagKey<Item> MAGICKAL_CLOTH = tag("magickal_cloth");
    public static final TagKey<Item> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final TagKey<Item> RITUAL_CANDLES = tag("ritual_candles");
    public static final TagKey<Item> RUNE_BASES = tag("rune_bases");
    public static final TagKey<Item> RUNE_ETCHINGS = tag("rune_etchings");
    public static final TagKey<Item> RUNIC_TRIMMABLE_ARMOR = tag("runic_trimmable_armor");
    public static final TagKey<Item> RUNIC_TRIM_MATERIALS = tag("runic_trim_materials");
    public static final TagKey<Item> SHULKER_BOXES = tag("shulker_boxes");
    public static final TagKey<Item> SKYGLASS = tag("skyglass");
    public static final TagKey<Item> SKYGLASS_PANES = tag("skyglass_panes");
    public static final TagKey<Item> STAINED_SKYGLASS = tag("stained_skyglass");
    public static final TagKey<Item> STAINED_SKYGLASS_PANES = tag("stained_skyglass_panes");
    public static final TagKey<Item> STATIC_BOOKS = tag("static_books");
    public static final TagKey<Item> SUNWOOD_LOGS = tag("sunwood_logs");
    public static final TagKey<Item> SURFACE_STONE = tag("surface_stone");
    public static final TagKey<Item> TREEFOLK_LOVED = tag("treefolk_loved");
    
    public static final TagKey<Item> WARDABLE_ARMOR = tag("wardable_armor");
    
    public static final TagKey<Item> FOOD_BAKED_POTATO = tag("food/baked_potato");
    public static final TagKey<Item> FOOD_COOKED_BEEF = tag("food/cooked_beef");
    
    public static final TagKey<Item> ROBES = tag("robes");
    public static final TagKey<Item> ROBES_HELMETS = tag("robes/helmets");
    public static final TagKey<Item> ROBES_CHESTPLATES = tag("robes/chestplates");
    public static final TagKey<Item> ROBES_LEGGINGS = tag("robes/leggings");
    public static final TagKey<Item> ROBES_BOOTS = tag("robes/boots");
    
    public static final TagKey<Item> ESSENCES_DUSTS = tag("essences/dusts");
    public static final TagKey<Item> ESSENCES_TERRESTRIAL_DUSTS = tag("essences/terrestrial_dusts");
    public static final TagKey<Item> ESSENCES_FORBIDDEN_DUSTS = tag("essences/forbidden_dusts");
    public static final TagKey<Item> ESSENCES_SHARDS = tag("essences/shards");
    public static final TagKey<Item> ESSENCES_TERRESTRIAL_SHARDS = tag("essences/terrestrial_shards");
    public static final TagKey<Item> ESSENCES_FORBIDDEN_SHARDS = tag("essences/forbidden_shards");
    public static final TagKey<Item> ESSENCES_CRYSTALS = tag("essences/crystals");
    public static final TagKey<Item> ESSENCES_TERRESTRIAL_CRYSTALS = tag("essences/terrestrial_crystals");
    public static final TagKey<Item> ESSENCES_FORBIDDEN_CRYSTALS = tag("essences/forbidden_crystals");
    public static final TagKey<Item> ESSENCES_CLUSTERS = tag("essences/clusters");
    public static final TagKey<Item> ESSENCES_TERRESTRIAL_CLUSTERS = tag("essences/terrestrial_clusters");
    public static final TagKey<Item> ESSENCES_FORBIDDEN_CLUSTERS = tag("essences/forbidden_clusters");
    
    public static final TagKey<Item> INGOTS_PRIMALITE = tag("ingots/primalite");
    public static final TagKey<Item> INGOTS_HEXIUM = tag("ingots/hexium");
    public static final TagKey<Item> INGOTS_HALLOWSTEEL = tag("ingots/hallowsteel");
    public static final TagKey<Item> NUGGETS_PRIMALITE = tag("nuggets/primalite");
    public static final TagKey<Item> NUGGETS_HEXIUM = tag("nuggets/hexium");
    public static final TagKey<Item> NUGGETS_HALLOWSTEEL = tag("nuggets/hallowsteel");
    public static final TagKey<Item> STORAGE_BLOCKS_PRIMALITE = tag("storage_blocks/primalite");
    public static final TagKey<Item> STORAGE_BLOCKS_HEXIUM = tag("storage_blocks/hexium");
    public static final TagKey<Item> STORAGE_BLOCKS_HALLOWSTEEL = tag("storage_blocks/hallowsteel");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(PrimalMagick.resource(name));
    }
}
