package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneCraftingBookCategory;
import com.verdantartifice.primalmagick.common.util.CraftingUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface IConcoctingRecipe extends Recipe<CraftingInput>, IHasManaCost, IHasRequirement, IArcaneRecipeBookItem {
    int MAX_WIDTH = 3;
    int MAX_HEIGHT = 3;
    
    @Override
    @NotNull
    default RecipeType<IConcoctingRecipe> getType() {
        return RecipeTypesPM.CONCOCTING.get();
    }

    @NotNull
    RecipeSerializer<? extends IConcoctingRecipe> getSerializer();

    // FIXME Should this be a different category?
    default ArcaneCraftingBookCategory category() {
        return ArcaneCraftingBookCategory.ARCANE;
    }

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return CraftingUtils.defaultCraftingReminder(input);
    }

    default RecipeBookCategory recipeBookCategory() {
        // FIXME Tie into datapacked recipe book category system
    }

    record ConcoctingCraftingBookInfo(ArcaneCraftingBookCategory category, String group) implements Recipe.BookInfo<ArcaneCraftingBookCategory> {
        public static final MapCodec<IConcoctingRecipe.ConcoctingCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(ArcaneCraftingBookCategory.CODEC, ArcaneCraftingBookCategory.ARCANE, IConcoctingRecipe.ConcoctingCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, IConcoctingRecipe.ConcoctingCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(ArcaneCraftingBookCategory.STREAM_CODEC, IConcoctingRecipe.ConcoctingCraftingBookInfo::new);
    }
}
