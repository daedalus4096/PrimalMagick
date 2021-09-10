package com.verdantartifice.primalmagic.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

/**
 * Collection of custom-defined Forge extension item tags.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class ItemTagsForgeExt {
    public static final IOptionalNamedTag<Item> DUSTS_IRON = tag("dusts/iron");
    public static final IOptionalNamedTag<Item> DUSTS_GOLD = tag("dusts/gold");
    public static final IOptionalNamedTag<Item> DUSTS_COPPER = tag("dusts/copper");
    public static final IOptionalNamedTag<Item> NUGGETS_QUARTZ = tag("nuggets/quartz");

    private static IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}
