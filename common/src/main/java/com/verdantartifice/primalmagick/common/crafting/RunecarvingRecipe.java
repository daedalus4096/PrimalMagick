package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.RunecarvingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.inputs.RunecarvingRecipeInput;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe implements IRunecarvingRecipe {
    public static final MapCodec<RunecarvingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
            Ingredient.CODEC.fieldOf("etching").forGetter(o -> o.etching),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
            Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
            Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
            Identifier.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
            ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
        ).apply(instance, RunecarvingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RunecarvingRecipe> STREAM_CODEC = StreamCodecUtils.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.etching,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), sar -> sar.requirement,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.baseExpertiseOverride,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.bonusExpertiseOverride,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), sar -> sar.expertiseGroup,
            ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), sar -> sar.disciplineOverride,
            RunecarvingRecipe::new);

    public static final RecipeSerializer<RunecarvingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    protected final Recipe.CommonInfo commonInfo;
    protected final ItemStackTemplate result;
    protected final Ingredient base;
    protected final Ingredient etching;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<Identifier> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;
    @Nullable private PlacementInfo placementInfo;

    public RunecarvingRecipe(Recipe.CommonInfo commonInfo, ItemStackTemplate result, Ingredient base, Ingredient etching,
                             Optional<AbstractRequirement<?>> requirement, Optional<Integer> baseExpertiseOverride,
                             Optional<Integer> bonusExpertiseOverride, Optional<Identifier> expertiseGroup,
                             Optional<ResearchDisciplineKey> disciplineOverride) {
        this.commonInfo = commonInfo;
        this.result = result;
        this.base = base;
        this.etching = etching;
        this.requirement = requirement;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @Override
    public boolean matches(@NotNull RunecarvingRecipeInput inv, @NotNull Level worldIn) {
        return this.base.test(inv.base()) && this.etching.test(inv.etching());
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull RunecarvingRecipeInput inv) {
        return this.result.create();
    }

    @Override
    public final boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    @NotNull
    public String group() {
        return "";
    }

    @Override
    @NotNull
    public RecipeSerializer<RunecarvingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    public final PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = this.createPlacementInfo();
        }
        return this.placementInfo;
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new RunecarvingRecipeDisplay(
                        this.base.display(),
                        this.etching.display(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        this.requirement,
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.RUNECARVING_TABLE.get())
                )
        );
    }

    @NotNull
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.base, this.etching));
    }

    @Override
    @NotNull
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
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
