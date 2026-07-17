package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.DissolutionBookCategory;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;

public interface IDissolutionRecipe extends Recipe<SingleRecipeInput>, IHasManaCost, IArcaneRecipeBookItem {
    @Override
    @NotNull
    default RecipeType<IDissolutionRecipe> getType() {
        return RecipeTypesPM.DISSOLUTION.get();
    }

    @NotNull
    RecipeSerializer<? extends IDissolutionRecipe> getSerializer();

    DissolutionBookCategory category();

    default NonNullList<ItemStack> getRemainingItems(SingleRecipeInput input) {
        ItemStackTemplate remainder = input.item().getItem().getCraftingRemainder();
        return NonNullList.of(ItemStack.EMPTY, remainder != null ? remainder.create() : ItemStack.EMPTY);
    }

    @NotNull
    default RecipeBookCategory recipeBookCategory() {
        return switch (this.category()) {
            case ORE -> RecipeBookCategoriesPM.DISSOLUTION_ORES.get();
            case MISC -> RecipeBookCategoriesPM.DISSOLUTION_MISC.get();
        };
    }

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    record DissolutionCraftingBookInfo(DissolutionBookCategory category, String group) implements Recipe.BookInfo<DissolutionBookCategory> {
        public static final MapCodec<IDissolutionRecipe.DissolutionCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(DissolutionBookCategory.CODEC, DissolutionBookCategory.MISC, IDissolutionRecipe.DissolutionCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, IDissolutionRecipe.DissolutionCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(DissolutionBookCategory.STREAM_CODEC, IDissolutionRecipe.DissolutionCraftingBookInfo::new);
    }
}
