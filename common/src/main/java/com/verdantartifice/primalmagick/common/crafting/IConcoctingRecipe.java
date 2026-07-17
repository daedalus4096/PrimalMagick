package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ConcoctingBookCategory;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
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

    ConcoctingBookCategory category();

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return CraftingUtils.defaultCraftingReminder(input);
    }

    @NotNull
    default RecipeBookCategory recipeBookCategory() {
        return switch (this.category()) {
            case DRINKABLE -> RecipeBookCategoriesPM.CONCOCTER_DRINKABLE.get();
            case BOMB -> RecipeBookCategoriesPM.CONCOCTER_BOMB.get();
        };
    }

    record ConcoctingCraftingBookInfo(ConcoctingBookCategory category, String group) implements Recipe.BookInfo<ConcoctingBookCategory> {
        public static final MapCodec<IConcoctingRecipe.ConcoctingCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(ConcoctingBookCategory.CODEC, ConcoctingBookCategory.DRINKABLE, IConcoctingRecipe.ConcoctingCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, IConcoctingRecipe.ConcoctingCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(ConcoctingBookCategory.STREAM_CODEC, IConcoctingRecipe.ConcoctingCraftingBookInfo::new);
    }
}
