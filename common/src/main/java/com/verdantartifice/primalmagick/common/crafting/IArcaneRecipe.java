package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneCraftingBookCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

/**
 * Crafting recipe interface for an arcane recipe.  An arcane recipe is like a vanilla recipe,
 * but has a research requirement and an optional mana cost.
 *  
 * @author Daedalus4096
 */
public interface IArcaneRecipe extends Recipe<CraftingInput>, IHasManaCost, IHasRequirement, IHasExpertise, IArcaneRecipeBookItem {
    @Override
    @NotNull
    default RecipeType<IArcaneRecipe> getType() {
        return RecipeTypesPM.ARCANE_CRAFTING.get();
    }

    @NotNull
    RecipeSerializer<? extends IArcaneRecipe> getSerializer();

    default ArcaneCraftingBookCategory category() {
        return ArcaneCraftingBookCategory.ARCANE;
    }

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return defaultCraftingReminder(input);
    }

    static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); slot++) {
            Item item = input.getItem(slot).getItem();
            ItemStackTemplate remainder = item.getCraftingRemainder();
            result.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return result;
    }

    default RecipeBookCategory recipeBookCategory() {
        // FIXME Tie into datapacked recipe book category system
    }

    record ArcaneCraftingBookInfo(ArcaneCraftingBookCategory category, String group) implements Recipe.BookInfo<ArcaneCraftingBookCategory> {
        public static final MapCodec<ArcaneCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(ArcaneCraftingBookCategory.CODEC, ArcaneCraftingBookCategory.ARCANE, ArcaneCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(ArcaneCraftingBookCategory.STREAM_CODEC, ArcaneCraftingBookInfo::new);
    }
}
