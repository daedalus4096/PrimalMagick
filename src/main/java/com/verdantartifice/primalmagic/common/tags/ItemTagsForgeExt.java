package com.verdantartifice.primalmagic.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Collection of custom-defined Forge extension item tags.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsForgeExt {
    public static final Tag<Item> DUSTS_IRON = tag("dusts/iron");
    public static final Tag<Item> DUSTS_GOLD = tag("dusts/gold");
    public static final Tag<Item> NUGGETS_QUARTZ = tag("nuggets/quartz");

    private static Tag<Item> tag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation("forge", name));
    }
}
