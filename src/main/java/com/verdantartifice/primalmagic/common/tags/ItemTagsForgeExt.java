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
        return new ItemTags.Wrapper(new ResourceLocation("forge", name));
    }
}
