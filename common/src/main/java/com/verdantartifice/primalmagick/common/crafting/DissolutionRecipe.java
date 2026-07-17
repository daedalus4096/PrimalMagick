package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.DissolutionBookCategory;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Definition for a dissolution recipe.  Similar to a smelting recipe, but used by the dissolution chamber
 * instead of a furnace.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipe implements IDissolutionRecipe {
    public static final MapCodec<DissolutionRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            IDissolutionRecipe.DissolutionCraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts)
        ).apply(instance, DissolutionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DissolutionRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            IDissolutionRecipe.DissolutionCraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
            SourceList.STREAM_CODEC, sar -> sar.manaCosts,
            DissolutionRecipe::new);

    public static final RecipeSerializer<DissolutionRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    protected final Recipe.CommonInfo commonInfo;
    protected final IDissolutionRecipe.DissolutionCraftingBookInfo bookInfo;
    protected final ItemStackTemplate result;
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;

    public DissolutionRecipe(Recipe.CommonInfo commonInfo, IDissolutionRecipe.DissolutionCraftingBookInfo bookInfo,
                             ItemStackTemplate result, Ingredient ingredient, SourceList manaCosts) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.result = result;
        this.ingredient = ingredient;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(@NotNull SingleRecipeInput input, @NotNull Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull SingleRecipeInput input) {
        return this.result.create();
    }

    @Override
    public final boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public DissolutionBookCategory category() {
        return this.bookInfo.category();
    }

    @Override
    @NotNull
    public final String group() {
        return this.bookInfo.group();
    }

    @Override
    @NotNull
    public RecipeSerializer<DissolutionRecipe> getSerializer() {
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
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
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
