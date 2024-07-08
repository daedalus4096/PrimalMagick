package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

/**
 * Definition for a shaped arcane recipe.  Like a vanilla shaped recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapedRecipe}
 */
public class ShapedArcaneRecipe extends AbstractStackCraftingRecipe<CraftingInput> implements IShapedArcaneRecipePM {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    protected final ShapedRecipePattern pattern;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;
    
    public ShapedArcaneRecipe(String group, ItemStack output, ShapedRecipePattern pattern, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts, Optional<Integer> baseExpertiseOverride,
            Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, output);
        this.pattern = pattern;
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }
    
    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        return this.pattern.matches(inv);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.get();
    }

    @Override
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
    
    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public int getRecipeWidth() {
        return this.pattern.width();
    }

    @Override
    public int getRecipeHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        return ingredients.isEmpty() || ingredients.stream().filter(i -> {
            return !i.isEmpty() && i.getItems() != null;
        }).anyMatch(i -> {
            return ForgeHooks.hasNoElements(i);
        });
    }

    @Override
    public int getExpertiseReward(RegistryAccess registryAccess) {
        return this.baseExpertiseOverride.orElseGet(() -> {
            return this.getResearchTier(registryAccess).map(tier -> tier.getDefaultExpertise()).orElse(0);
        });
    }

    @Override
    public int getBonusExpertiseReward(RegistryAccess registryAccess) {
        return this.bonusExpertiseOverride.orElseGet(() -> {
            return this.getResearchTier(registryAccess).map(tier -> tier.getDefaultBonusExpertise()).orElse(0);
        });
    }

    @Override
    public Optional<ResourceLocation> getExpertiseGroup() {
        return this.expertiseGroup;
    }

    @Override
    public Optional<ResearchDisciplineKey> getResearchDisciplineOverride() {
        return this.disciplineOverride;
    }

    public static class Serializer implements RecipeSerializer<ShapedArcaneRecipe> {
        @Override
        public Codec<ShapedArcaneRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
                    ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(r -> r.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(r -> r.manaCosts),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
            ).apply(instance, ShapedArcaneRecipe::new));
        }

        @Override
        public ShapedArcaneRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            ShapedRecipePattern pattern = ShapedRecipePattern.fromNetwork(buffer);
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            Optional<Integer> baseExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<Integer> bonusExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<ResourceLocation> expGroup = buffer.readOptional(b -> b.readResourceLocation());
            Optional<ResearchDisciplineKey> discOverride = buffer.readOptional(ResearchDisciplineKey::fromNetwork);
            ItemStack stack = buffer.readItem();
            return new ShapedArcaneRecipe(group, stack, pattern, requirement, manaCosts, baseExpOverride, bonusExpOverride, expGroup, discOverride);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedArcaneRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.pattern.toNetwork(buffer);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            SourceList.toNetwork(buffer, recipe.manaCosts);
            buffer.writeOptional(recipe.baseExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.bonusExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.expertiseGroup, (b, g) -> b.writeResourceLocation(g));
            buffer.writeOptional(recipe.disciplineOverride, (b, d) -> d.toNetwork(b));
            buffer.writeItem(recipe.output);
        }
    }
}
