package com.verdantartifice.primalmagic.client.gui.recipe_book;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

/**
 * Filtering categories for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public enum ArcaneRecipeBookCategories {
    CRAFTING_SEARCH(new ItemStack(Items.COMPASS)),
    CRAFTING_BUILDING_BLOCKS(new ItemStack(Blocks.BRICKS)),
    CRAFTING_REDSTONE(new ItemStack(Items.REDSTONE)),
    CRAFTING_EQUIPMENT(new ItemStack(Items.IRON_AXE), new ItemStack(Items.GOLDEN_SWORD)),
    CRAFTING_MISC(new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.APPLE)),
    CRAFTING_ARCANE(new ItemStack(ItemsPM.GRIMOIRE.get())),
    UNKNOWN(new ItemStack(Items.BARRIER));
    
    public static final List<ArcaneRecipeBookCategories> CRAFTING_CATEGORIES = ImmutableList.of(CRAFTING_SEARCH, CRAFTING_ARCANE, CRAFTING_EQUIPMENT, CRAFTING_BUILDING_BLOCKS, CRAFTING_MISC, CRAFTING_REDSTONE);
    public static final Map<ArcaneRecipeBookCategories, List<ArcaneRecipeBookCategories>> AGGREGATE_CATEGORIES = ImmutableMap.of(CRAFTING_SEARCH, ImmutableList.of(CRAFTING_ARCANE, CRAFTING_EQUIPMENT, CRAFTING_BUILDING_BLOCKS, CRAFTING_MISC, CRAFTING_REDSTONE));

    private final List<ItemStack> itemIcons;
    
    private ArcaneRecipeBookCategories(ItemStack... icons) {
        this.itemIcons = ImmutableList.copyOf(icons);
    }
    
    @Nonnull
    public static List<ArcaneRecipeBookCategories> getCategories(ArcaneRecipeBookType type) {
        switch (type) {
        case CRAFTING:
            return CRAFTING_CATEGORIES;
        default:
            return ImmutableList.of();
        }
    }

    public List<ItemStack> getIconItems() {
        return this.itemIcons;
    }
}
