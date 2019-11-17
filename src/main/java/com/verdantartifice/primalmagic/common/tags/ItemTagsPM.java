package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ItemTagsPM {
    public static final Tag<Item> BEDS = tag("beds");
    public static final Tag<Item> COLORED_SHULKER_BOXES = tag("colored_shulker_boxes");
    public static final Tag<Item> CONCRETE = tag("concrete");
    public static final Tag<Item> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
    public static final Tag<Item> DEAD_CORAL_PLANTS = tag("dead_coral_plants");
    public static final Tag<Item> DEAD_CORALS = tag("dead_corals");
    public static final Tag<Item> MOONWOOD_LOGS = tag("moonwood_logs");
    public static final Tag<Item> SHULKER_BOXES = tag("shulker_boxes");
    public static final Tag<Item> SUNWOOD_LOGS = tag("sunwood_logs");
    
    private static Tag<Item> tag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
