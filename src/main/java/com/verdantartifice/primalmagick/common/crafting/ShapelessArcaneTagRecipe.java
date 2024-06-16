package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessArcaneTagRecipe extends AbstractTagCraftingRecipe<CraftingContainer> implements IShapelessArcaneRecipePM {
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;

    public ShapelessArcaneTagRecipe(String group, TagKey<Item> outputTag, int outputAmount, NonNullList<Ingredient> items, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts,
            Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, outputTag, outputAmount);
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS_TAG.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
    
    @Override
    public boolean isSimple() {
        return this.isSimple;
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

    public static class Serializer implements RecipeSerializer<ShapelessArcaneTagRecipe> {
        @Override
        public Codec<ShapelessArcaneTagRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(sar -> sar.group),
                    TagKey.codec(Registries.ITEM).fieldOf("outputTag").forGetter(sar -> sar.outputTag),
                    Codec.INT.fieldOf("outputAmount").forGetter(sar -> sar.outputAmount),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless arcane recipe");
                        } else if (ingArray.length > ShapedArcaneRecipe.MAX_WIDTH * ShapedArcaneRecipe.MAX_HEIGHT) {
                            return DataResult.error(() -> "Too many ingredients for shapeless arcane recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(sar -> sar.recipeItems),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
                ).apply(instance, ShapelessArcaneTagRecipe::new)
            );
        }
        
        @Override
        public ShapelessArcaneTagRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            Optional<AbstractRequirement<?>> requirement = pBuffer.readOptional(AbstractRequirement::fromNetwork);
            
            SourceList manaCosts = SourceList.fromNetwork(pBuffer);
            
            int count = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(pBuffer));
            }
            
            Optional<Integer> baseExpOverride = pBuffer.readOptional(b -> b.readVarInt());
            Optional<Integer> bonusExpOverride = pBuffer.readOptional(b -> b.readVarInt());
            Optional<ResourceLocation> expGroup = pBuffer.readOptional(b -> b.readResourceLocation());
            Optional<ResearchDisciplineKey> discOverride = pBuffer.readOptional(ResearchDisciplineKey::fromNetwork);
            
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, pBuffer.readResourceLocation());
            int resultAmount = pBuffer.readVarInt();
            
            return new ShapelessArcaneTagRecipe(group, resultTag, resultAmount, ingredients, requirement, manaCosts, baseExpOverride, bonusExpOverride, expGroup, discOverride);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShapelessArcaneTagRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pBuffer.writeOptional(pRecipe.requirement, (b, r) -> r.toNetwork(b));
            SourceList.toNetwork(pBuffer, pRecipe.manaCosts);
            pBuffer.writeVarInt(pRecipe.recipeItems.size());
            for (Ingredient ingredient : pRecipe.recipeItems) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeOptional(pRecipe.baseExpertiseOverride, (b, e) -> b.writeVarInt(e));
            pBuffer.writeOptional(pRecipe.bonusExpertiseOverride, (b, e) -> b.writeVarInt(e));
            pBuffer.writeOptional(pRecipe.expertiseGroup, (b, g) -> b.writeResourceLocation(g));
            pBuffer.writeOptional(pRecipe.disciplineOverride, (b, d) -> d.toNetwork(b));
            pBuffer.writeResourceLocation(pRecipe.outputTag.location());
            pBuffer.writeVarInt(pRecipe.outputAmount);
        }
    }
}
