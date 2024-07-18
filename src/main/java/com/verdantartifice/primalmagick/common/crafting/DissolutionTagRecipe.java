package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

/**
 * Definition for a dissolution tag recipe.  Similar to a normal dissolution recipe, but it outputs
 * a given tag rather than a specific stack.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipe extends AbstractTagCraftingRecipe<SingleRecipeInput> implements IDissolutionRecipe {
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;
    
    public DissolutionTagRecipe(String group, TagKey<Item> recipeOutputTag, int recipeOutputAmount, Ingredient ingredient, SourceList manaCosts) {
        super(group, recipeOutputTag, recipeOutputAmount);
        this.ingredient = ingredient;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.DISSOLUTION_TAG.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    public static class Serializer implements RecipeSerializer<DissolutionTagRecipe> {
        protected static final MapCodec<DissolutionTagRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> {
            return instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(dr -> dr.group),
                    TagKey.codec(Registries.ITEM).fieldOf("outputTag").forGetter(dr -> dr.outputTag),
                    Codec.INT.fieldOf("outputAmount").forGetter(dr -> dr.outputAmount),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(dr -> dr.ingredient),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(dr -> dr.manaCosts)
                ).apply(instance, DissolutionTagRecipe::new);
        });
        
        protected static final StreamCodec<RegistryFriendlyByteBuf, DissolutionTagRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, r -> r.group,
                StreamCodecUtils.tagKey(Registries.ITEM), r -> r.outputTag,
                ByteBufCodecs.VAR_INT, r -> r.outputAmount,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                SourceList.STREAM_CODEC, r -> r.manaCosts,
                DissolutionTagRecipe::new);
        
        @Override
        public MapCodec<DissolutionTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DissolutionTagRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
