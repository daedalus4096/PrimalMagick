package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 */
public class ShapelessTagRecipe extends NormalCraftingRecipe {
    public static final MapCodec<ShapelessTagRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            TagKey.codec(Registries.ITEM).fieldOf("resultTag").forGetter(o -> o.resultTag),
            Codec.INT.optionalFieldOf("resultAmount", 1).forGetter(o -> o.resultAmount),
            Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients)
        ).apply(instance, ShapelessTagRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessTagRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            TagKey.streamCodec(Registries.ITEM), o -> o.resultTag,
            ByteBufCodecs.VAR_INT, o -> o.resultAmount,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
            ShapelessTagRecipe::new);

    public static final RecipeSerializer<ShapelessTagRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final List<Ingredient> ingredients;
    @Nullable protected HolderLookup.Provider registries;
    
    public ShapelessTagRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, TagKey<Item> resultTag, int resultAmount, List<Ingredient> ingredients) {
        super(commonInfo, bookInfo);
        this.resultTag = resultTag;
        this.resultAmount = resultAmount;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        // Save a copy of the registry access for later
        this.registries = level.registryAccess();

        if (input.ingredientCount() != this.ingredients.size()) {
            return false;
        } else {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.getFirst().test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput craftingInput) {
        if (this.registries == null) {
            LOGGER.error("ShapelessTagRecipe#matches must be called at least once before calling assemble to get output!");
            return ItemStack.EMPTY;
        } else {
            return ItemUtils.getFirstItemFromTag(this.registries, this.resultTag).copyWithCount(this.resultAmount);
        }
    }

    @Override
    @NotNull
    public RecipeSerializer<ShapelessTagRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        new SlotDisplay.TagSlotDisplay(this.resultTag), // FIXME Does this need a custom display to show count?
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    @Override
    @NotNull
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(this.ingredients);
    }
}
