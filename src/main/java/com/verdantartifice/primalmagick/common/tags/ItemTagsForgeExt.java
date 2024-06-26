package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Collection of custom-defined Forge extension item tags.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsForgeExt {
    public static final TagKey<Item> DUSTS_IRON = tag("dusts/iron");
    public static final TagKey<Item> DUSTS_GOLD = tag("dusts/gold");
    public static final TagKey<Item> DUSTS_COPPER = tag("dusts/copper");
    public static final TagKey<Item> DUSTS_TIN = tag("dusts/tin");
    public static final TagKey<Item> DUSTS_LEAD = tag("dusts/lead");
    public static final TagKey<Item> DUSTS_SILVER = tag("dusts/silver");
    public static final TagKey<Item> DUSTS_URANIUM = tag("dusts/uranium");
    public static final TagKey<Item> MILK = tag("milk");
    public static final TagKey<Item> NUGGETS_COPPER = tag("nuggets/copper");
    public static final TagKey<Item> NUGGETS_QUARTZ = tag("nuggets/quartz");
    public static final TagKey<Item> NUGGETS_TIN = tag("nuggets/tin");
    public static final TagKey<Item> NUGGETS_LEAD = tag("nuggets/lead");
    public static final TagKey<Item> NUGGETS_SILVER = tag("nuggets/silver");
    public static final TagKey<Item> NUGGETS_URANIUM = tag("nuggets/uranium");
    public static final TagKey<Item> ORES_ROCK_SALT = tag("ores/rock_salt");
    public static final TagKey<Item> ORES_TIN = tag("ores/tin");
    public static final TagKey<Item> ORES_LEAD = tag("ores/lead");
    public static final TagKey<Item> ORES_SILVER = tag("ores/silver");
    public static final TagKey<Item> ORES_URANIUM = tag("ores/uranium");
    public static final TagKey<Item> RAW_MATERIALS_TIN = tag("raw_materials/tin");
    public static final TagKey<Item> RAW_MATERIALS_LEAD = tag("raw_materials/lead");
    public static final TagKey<Item> RAW_MATERIALS_SILVER = tag("raw_materials/silver");
    public static final TagKey<Item> RAW_MATERIALS_URANIUM = tag("raw_materials/uranium");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
    }
}
