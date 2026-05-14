package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Definition for a concocting recipe.  Similar to a shapeless arcane recipe, but used by the concocter
 * instead of an arcane workbench.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipe implements IConcoctingRecipe {
    public static final MapCodec<ConcoctingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            IConcoctingRecipe.ConcoctingCraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
            Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts)
        ).apply(instance, ConcoctingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConcoctingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            IConcoctingRecipe.ConcoctingCraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), sar -> sar.requirement,
            SourceList.STREAM_CODEC, sar -> sar.manaCosts,
            ConcoctingRecipe::new);

    public static final RecipeSerializer<ConcoctingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    protected final Recipe.CommonInfo commonInfo;
    protected final IConcoctingRecipe.ConcoctingCraftingBookInfo bookInfo;
    protected final ItemStackTemplate result;
    protected final List<Ingredient> ingredients;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;

    public ConcoctingRecipe(Recipe.CommonInfo commonInfo, IConcoctingRecipe.ConcoctingCraftingBookInfo bookInfo, ItemStackTemplate result, 
                            List<Ingredient> ingredients, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.result = result;
        this.ingredients = ingredients;
        this.requirement = requirement;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
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
        return this.result.create();
    }

    @Override
    public final boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    @NotNull
    public final String group() {
        return this.bookInfo.group();
    }

    @Override
    @NotNull
    public RecipeSerializer<ConcoctingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    @NotNull
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredients);
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new ConcoctingRecipeDisplay(
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        this.manaCosts,
                        this.requirement,
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.CONCOCTER.get())
                )
        );
    }

    @Override
    @NotNull
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    @NotNull
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
}
