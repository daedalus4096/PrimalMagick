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
    public static final TagKey<Item> NUGGETS_QUARTZ = tag("nuggets/quartz");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}
