package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.DissolutionBookCategory;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

/**
 * Definition for a dissolution tag recipe.  Similar to a normal dissolution recipe, but it outputs
 * a given tag rather than a specific stack.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipe implements IDissolutionRecipe {
    public static final MapCodec<DissolutionTagRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            IDissolutionRecipe.DissolutionCraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            TagKey.codec(Registries.ITEM).fieldOf("resultTag").forGetter(o -> o.resultTag),
            Codec.INT.optionalFieldOf("resultAmount", 1).forGetter(o -> o.resultAmount),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts)
    ).apply(instance, DissolutionTagRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DissolutionTagRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            IDissolutionRecipe.DissolutionCraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            TagKey.streamCodec(Registries.ITEM), o -> o.resultTag,
            ByteBufCodecs.VAR_INT, o -> o.resultAmount,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
            SourceList.STREAM_CODEC, sar -> sar.manaCosts,
            DissolutionTagRecipe::new);

    public static final RecipeSerializer<DissolutionTagRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final Recipe.CommonInfo commonInfo;
    protected final IDissolutionRecipe.DissolutionCraftingBookInfo bookInfo;
    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;
    @Nullable protected HolderLookup.Provider registries;

    public DissolutionTagRecipe(Recipe.CommonInfo commonInfo, IDissolutionRecipe.DissolutionCraftingBookInfo bookInfo,
                                TagKey<Item> resultTag, int resultAmount, Ingredient ingredient, SourceList manaCosts) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.resultTag = resultTag;
        this.resultAmount = resultAmount;
        this.ingredient = ingredient;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(@NotNull SingleRecipeInput input, @NotNull Level level) {
        // Save a copy of the registry access for later
        this.registries = level.registryAccess();
        return this.ingredient.test(input.item());
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull SingleRecipeInput input) {
        if (this.registries == null) {
            LOGGER.error("DissolutionTagRecipe#matches must be called at least once before calling assemble to get output!");
            return ItemStack.EMPTY;
        } else {
            return ItemUtils.getFirstItemFromTag(this.registries, this.resultTag).copyWithCount(this.resultAmount);
        }
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public DissolutionBookCategory category() {
        return this.bookInfo.category();
    }

    @Override
    @NotNull
    public String group() {
        return this.bookInfo.group();
    }

    @Override
    @NotNull
    public RecipeSerializer<DissolutionTagRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    @NotNull
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredient);
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new DissolutionRecipeDisplay(
                        this.ingredient.display(),
                        new SlotDisplay.TagSlotDisplay(this.resultTag), // FIXME Does this need a custom display to show count?
                        this.manaCosts,
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.DISSOLUTION_CHAMBER.get())
                )
        );
    }

    @Override
    public @NotNull SourceList getManaCosts() {
        return this.manaCosts;
    }
}
