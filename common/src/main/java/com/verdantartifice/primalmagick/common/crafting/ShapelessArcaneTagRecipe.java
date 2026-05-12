package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 */
public class ShapelessArcaneTagRecipe extends NormalArcaneCraftingRecipe {
    public static final MapCodec<ShapelessArcaneTagRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            IArcaneRecipe.ArcaneCraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            TagKey.codec(Registries.ITEM).fieldOf("resultTag").forGetter(o -> o.resultTag),
            Codec.INT.optionalFieldOf("resultAmount", 1).forGetter(o -> o.resultAmount),
            Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
            Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
            Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
            Identifier.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
            ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
    ).apply(instance, ShapelessArcaneTagRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessArcaneTagRecipe> STREAM_CODEC = StreamCodecUtils.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            IArcaneRecipe.ArcaneCraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            TagKey.streamCodec(Registries.ITEM), o -> o.resultTag,
            ByteBufCodecs.VAR_INT, o -> o.resultAmount,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), sar -> sar.requirement,
            SourceList.STREAM_CODEC, sar -> sar.manaCosts,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.baseExpertiseOverride,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.bonusExpertiseOverride,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), sar -> sar.expertiseGroup,
            ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), sar -> sar.disciplineOverride,
            ShapelessArcaneTagRecipe::new);

    public static final RecipeSerializer<ShapelessArcaneTagRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final List<Ingredient> ingredients;
    @Nullable protected HolderLookup.Provider registries;

    public ShapelessArcaneTagRecipe(Recipe.CommonInfo commonInfo, IArcaneRecipe.ArcaneCraftingBookInfo bookInfo,
                                 TagKey<Item> resultTag, int resultAmount, List<Ingredient> ingredients, Optional<AbstractRequirement<?>> requirement,
                                 SourceList manaCosts, Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride,
                                 Optional<Identifier> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(commonInfo, bookInfo, requirement, manaCosts, baseExpertiseOverride, bonusExpertiseOverride, expertiseGroup, disciplineOverride);
        this.resultTag = resultTag;
        this.resultAmount = resultAmount;
        this.ingredients = ingredients;
    }

    @Override
    @NotNull
    public RecipeSerializer<ShapelessArcaneTagRecipe> getSerializer() {
        return SERIALIZER;
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
            LOGGER.error("ShapelessArcaneTagRecipe#matches must be called at least once before calling assemble to get output!");
            return ItemStack.EMPTY;
        } else {
            return ItemUtils.getFirstItemFromTag(this.registries, this.resultTag).copyWithCount(this.resultAmount);
        }
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapelessArcaneCraftingRecipeDisplay(
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        new SlotDisplay.TagSlotDisplay(this.resultTag),
                        this.manaCosts,
                        this.requirement,
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.ARCANE_WORKBENCH.get())
                )
        );
    }

    @Override
    @NotNull
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(this.ingredients);
    }
}
