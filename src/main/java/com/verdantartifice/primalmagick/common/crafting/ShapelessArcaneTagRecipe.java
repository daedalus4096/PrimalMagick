package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
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
    
    public ShapelessArcaneTagRecipe(String group, TagKey<Item> outputTag, int outputAmount, NonNullList<Ingredient> items, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts) {
        super(group, outputTag, outputAmount);
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
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

    public static class Serializer implements RecipeSerializer<ShapelessArcaneTagRecipe> {
        protected static final Codec<ShapelessArcaneTagRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
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
                    AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts)
                ).apply(instance, ShapelessArcaneTagRecipe::new);
        });
        
        @Override
        public Codec<ShapelessArcaneTagRecipe> codec() {
            return CODEC;
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
            
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, pBuffer.readResourceLocation());
            int resultAmount = pBuffer.readVarInt();
            
            return new ShapelessArcaneTagRecipe(group, resultTag, resultAmount, ingredients, requirement, manaCosts);
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
            pBuffer.writeResourceLocation(pRecipe.outputTag.location());
            pBuffer.writeVarInt(pRecipe.outputAmount);
        }
    }
}
