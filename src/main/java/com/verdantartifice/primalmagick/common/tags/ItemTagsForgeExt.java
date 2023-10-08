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
    public static final TagKey<Item> MILK = tag("milk");
    public static final TagKey<Item> NUGGETS_COPPER = tag("nuggets/copper");
    public static final TagKey<Item> NUGGETS_QUARTZ = tag("nuggets/quartz");
    public static final TagKey<Item> NUGGETS_TIN = tag("nuggets/tin");
    public static final TagKey<Item> NUGGETS_LEAD = tag("nuggets/lead");
    public static final TagKey<Item> NUGGETS_SILVER = tag("nuggets/silver");
    public static final TagKey<Item> NUGGETS_URANIUM = tag("nuggets/uranium");
    public static final TagKey<Item> ORES_ROCK_SALT = tag("ores/rock_salt");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}
