package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneCraftingBookCategory;
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

    // FIXME Should this be a different category?
    default ArcaneCraftingBookCategory category() {
        return ArcaneCraftingBookCategory.ARCANE;
    }

    default NonNullList<ItemStack> getRemainingItems(SingleRecipeInput input) {
        ItemStackTemplate remainder = input.item().getItem().getCraftingRemainder();
        return NonNullList.of(ItemStack.EMPTY, remainder != null ? remainder.create() : ItemStack.EMPTY);
    }

    default RecipeBookCategory recipeBookCategory() {
        // FIXME Tie into datapacked recipe book category system
    }

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    record DissolutionCraftingBookInfo(ArcaneCraftingBookCategory category, String group) implements Recipe.BookInfo<ArcaneCraftingBookCategory> {
        public static final MapCodec<IDissolutionRecipe.DissolutionCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(ArcaneCraftingBookCategory.CODEC, ArcaneCraftingBookCategory.ARCANE, IDissolutionRecipe.DissolutionCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, IDissolutionRecipe.DissolutionCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(ArcaneCraftingBookCategory.STREAM_CODEC, IDissolutionRecipe.DissolutionCraftingBookInfo::new);
    }
}
