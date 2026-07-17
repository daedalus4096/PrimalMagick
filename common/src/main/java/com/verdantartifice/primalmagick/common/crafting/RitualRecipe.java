package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.ExpertiseRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.RitualRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Definition for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipe implements IRitualRecipe {
    public static final MinMaxBounds.Ints ALLOWED_INSTABILITY_RANGE = MinMaxBounds.Ints.between(0, 10);

    public static final MapCodec<RitualRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(o -> o.ingredients),
            BlockIngredient.CODEC.listOf().fieldOf("props").forGetter(o -> o.props),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(o -> o.requirement),
            Codec.INT.fieldOf("instability").forGetter(o -> o.instability),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(o -> o.manaCosts),
            Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(o -> o.baseExpertiseOverride),
            Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(o -> o.bonusExpertiseOverride),
            Identifier.CODEC.optionalFieldOf("expertiseGroup").forGetter(o -> o.expertiseGroup),
            ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(o -> o.disciplineOverride)
        ).apply(instance, RitualRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = StreamCodecUtils.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
            BlockIngredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.props,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), o -> o.requirement,
            ByteBufCodecs.VAR_INT, o -> o.instability,
            SourceList.STREAM_CODEC, o -> o.manaCosts,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), o -> o.baseExpertiseOverride,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), o -> o.bonusExpertiseOverride,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), o -> o.expertiseGroup,
            ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), o -> o.disciplineOverride,
            RitualRecipe::new);
    public static final RecipeSerializer<RitualRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    protected final Recipe.CommonInfo commonInfo;
    protected final ItemStackTemplate result;
    protected final List<Ingredient> ingredients;
    protected final List<BlockIngredient> props;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final int instability;
    protected final SourceList manaCosts;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<Identifier> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;
    @Nullable private PlacementInfo placementInfo;

    public RitualRecipe(Recipe.CommonInfo commonInfo, ItemStackTemplate result, List<Ingredient> ingredients,
                        List<BlockIngredient> props, Optional<AbstractRequirement<?>> requirement, int instability,
                        SourceList manaCosts, Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride,
                        Optional<Identifier> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        this.commonInfo = commonInfo;
        this.result = result;
        this.ingredients = ingredients;
        this.props = props;
        this.requirement = requirement;
        this.instability = instability;
        this.manaCosts = manaCosts;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
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
    public ItemStack assemble(@NotNull CraftingInput input) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    @NotNull
    public String group() {
        // Ritual recipes don't show up in the recipe book
        return "";
    }

    @Override
    @NotNull
    public RecipeSerializer<RitualRecipe> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(this.ingredients);
    }

    @Override
    @NotNull
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = this.createPlacementInfo();
        }
        return this.placementInfo;
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new RitualRecipeDisplay(
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        this.props.stream().map(BlockIngredient::display).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        this.manaCosts,
                        this.instability,
                        this.requirement,
                        new ExpertiseRecipeDisplay(this, Minecraft.getInstance().player.registryAccess()),
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.RITUAL_ALTAR.get())
                )
        );
    }

    @Override
    @NotNull
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }

    @Override
    public @NotNull SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public List<BlockIngredient> getProps() {
        return this.props;
    }
    
    @Override
    public int getInstability() {
        return this.instability;
    }
    
    @Override
    public int getExpertiseReward(RegistryAccess registryAccess) {
        return this.baseExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultExpertise).orElse(0));
    }

    @Override
    public int getBonusExpertiseReward(RegistryAccess registryAccess) {
        return this.bonusExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultBonusExpertise).orElse(0));
    }

    @Override
    @NotNull
    public Optional<Identifier> getExpertiseGroup() {
        return this.expertiseGroup;
    }

    @Override
    @NotNull
    public Optional<ResearchDisciplineKey> getResearchDisciplineOverride() {
        return this.disciplineOverride;
    }
}
