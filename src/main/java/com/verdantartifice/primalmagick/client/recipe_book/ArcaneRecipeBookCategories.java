package com.verdantartifice.primalmagick.client.recipe_book;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.concoctions.FuseType;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;

/**
 * Filtering categories for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public enum ArcaneRecipeBookCategories {
    CRAFTING_SEARCH(RecipeBookCategories.CRAFTING_SEARCH, new ItemStack(Items.COMPASS)),
    CRAFTING_BUILDING_BLOCKS(RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, new ItemStack(Blocks.BRICKS)),
    CRAFTING_REDSTONE(RecipeBookCategories.CRAFTING_REDSTONE, new ItemStack(Items.REDSTONE)),
    CRAFTING_EQUIPMENT(RecipeBookCategories.CRAFTING_EQUIPMENT, new ItemStack(Items.IRON_AXE), new ItemStack(Items.GOLDEN_SWORD)),
    CRAFTING_MISC(RecipeBookCategories.CRAFTING_MISC, new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.APPLE)),
    CRAFTING_ARCANE(RecipeBookCategories.UNKNOWN, new ItemStack(ItemsPM.GRIMOIRE.get())),
    CONCOCTER_SEARCH(RecipeBookCategories.UNKNOWN, new ItemStack(Items.COMPASS)),
    CONCOCTER_DRINKABLE(RecipeBookCategories.UNKNOWN, ConcoctionUtils.newConcoction(Potions.REGENERATION, ConcoctionType.TINCTURE)),
    CONCOCTER_BOMB(RecipeBookCategories.UNKNOWN, ConcoctionUtils.newBomb(Potions.POISON, FuseType.MEDIUM)),
    DISSOLUTION_SEARCH(RecipeBookCategories.UNKNOWN, new ItemStack(Items.COMPASS)),
    DISSOLUTION_ORES(RecipeBookCategories.UNKNOWN, new ItemStack(Items.RAW_GOLD)),
    DISSOLUTION_MISC(RecipeBookCategories.UNKNOWN, new ItemStack(Items.GRAVEL)),
    FURNACE_SEARCH(RecipeBookCategories.FURNACE_SEARCH, new ItemStack(Items.COMPASS)),
    FURNACE_FOOD(RecipeBookCategories.FURNACE_FOOD, new ItemStack(Items.PORKCHOP)),
    FURNACE_BLOCKS(RecipeBookCategories.FURNACE_BLOCKS, new ItemStack(Blocks.STONE)),
    FURNACE_MISC(RecipeBookCategories.FURNACE_MISC, new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.EMERALD)),
    UNKNOWN(RecipeBookCategories.UNKNOWN, new ItemStack(Items.BARRIER));
    
    public static final List<ArcaneRecipeBookCategories> CRAFTING_CATEGORIES = ImmutableList.of(CRAFTING_SEARCH, CRAFTING_ARCANE, CRAFTING_EQUIPMENT, CRAFTING_BUILDING_BLOCKS, CRAFTING_MISC, CRAFTING_REDSTONE);
    public static final List<ArcaneRecipeBookCategories> CONCOCTER_CATEGORIES = ImmutableList.of(CONCOCTER_SEARCH, CONCOCTER_DRINKABLE, CONCOCTER_BOMB);
    public static final List<ArcaneRecipeBookCategories> DISSOLUTION_CATEGORIES = ImmutableList.of(DISSOLUTION_SEARCH, DISSOLUTION_ORES, DISSOLUTION_MISC);
    public static final List<ArcaneRecipeBookCategories> FURNACE_CATEGORIES = ImmutableList.of(FURNACE_SEARCH, FURNACE_FOOD, FURNACE_BLOCKS, FURNACE_MISC);
    public static final Map<ArcaneRecipeBookCategories, List<ArcaneRecipeBookCategories>> AGGREGATE_CATEGORIES = ImmutableMap.of(
            CRAFTING_SEARCH, ImmutableList.of(CRAFTING_ARCANE, CRAFTING_EQUIPMENT, CRAFTING_BUILDING_BLOCKS, CRAFTING_MISC, CRAFTING_REDSTONE), 
            CONCOCTER_SEARCH, ImmutableList.of(CONCOCTER_DRINKABLE, CONCOCTER_BOMB), 
            DISSOLUTION_SEARCH, ImmutableList.of(DISSOLUTION_ORES, DISSOLUTION_MISC),
            FURNACE_SEARCH, ImmutableList.of(FURNACE_FOOD, FURNACE_BLOCKS, FURNACE_MISC));

    private final RecipeBookCategories vanillaCategory;
    private final List<ItemStack> itemIcons;
    
    private ArcaneRecipeBookCategories(RecipeBookCategories vanillaCategory, ItemStack... icons) {
        this.vanillaCategory = vanillaCategory;
        this.itemIcons = ImmutableList.copyOf(icons);
    }
    
    @Nonnull
    public static List<ArcaneRecipeBookCategories> getCategories(ArcaneRecipeBookType type) {
        return switch (type) {
            case CRAFTING -> CRAFTING_CATEGORIES;
            case CONCOCTER -> CONCOCTER_CATEGORIES;
            case DISSOLUTION -> DISSOLUTION_CATEGORIES;
            case FURNACE -> FURNACE_CATEGORIES;
        };
    }

    public List<ItemStack> getIconItems() {
        return this.itemIcons;
    }
    
    public RecipeBookCategories getVanillaCategory() {
        return this.vanillaCategory;
    }
}
