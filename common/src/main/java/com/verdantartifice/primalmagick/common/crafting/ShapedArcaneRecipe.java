package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.display.ExpertiseRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Definition for a shaped arcane recipe.  Like a vanilla shaped recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 */
public class ShapedArcaneRecipe extends NormalArcaneCraftingRecipe {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    public static final MapCodec<ShapedArcaneRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            IArcaneRecipe.ArcaneCraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
            ShapedRecipePattern.MAP_CODEC.forGetter((o) -> o.pattern),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
            SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
            Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
            Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
            Identifier.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
            ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
        ).apply(instance, ShapedArcaneRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedArcaneRecipe> STREAM_CODEC = StreamCodecUtils.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            IArcaneRecipe.ArcaneCraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            ShapedRecipePattern.STREAM_CODEC, o -> o.pattern,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), sar -> sar.requirement,
            SourceList.STREAM_CODEC, sar -> sar.manaCosts,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.baseExpertiseOverride,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), sar -> sar.bonusExpertiseOverride,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), sar -> sar.expertiseGroup,
            ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), sar -> sar.disciplineOverride,
            ShapedArcaneRecipe::new);

    public static final RecipeSerializer<ShapedArcaneRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    protected final ShapedRecipePattern pattern;
    protected final ItemStackTemplate result;
    
    public ShapedArcaneRecipe(Recipe.CommonInfo commonInfo, IArcaneRecipe.ArcaneCraftingBookInfo bookInfo,
                              ItemStackTemplate result, ShapedRecipePattern pattern, Optional<AbstractRequirement<?>> requirement,
                              SourceList manaCosts, Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride,
                              Optional<Identifier> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(commonInfo, bookInfo, requirement, manaCosts, baseExpertiseOverride, bonusExpertiseOverride, expertiseGroup, disciplineOverride);
        this.result = result;
        this.pattern = pattern;
    }
    
    @Override
    @NotNull
    public RecipeSerializer<ShapedArcaneRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    @NotNull
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    @NotNull
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        return this.pattern.matches(input);
    }

    @NotNull
    public ItemStack assemble(@NotNull CraftingInput input) {
        return this.result.create();
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapedArcaneCraftingRecipeDisplay(
                        this.getWidth(),
                        this.getHeight(),
                        this.pattern.ingredients().stream().map(ingOpt -> ingOpt.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        this.manaCosts,
                        this.requirement,
                        new ExpertiseRecipeDisplay(this, Minecraft.getInstance().player.registryAccess()),
                        new SlotDisplay.ItemSlotDisplay(ItemsPM.ARCANE_WORKBENCH.get())
                )
        );
    }
}
