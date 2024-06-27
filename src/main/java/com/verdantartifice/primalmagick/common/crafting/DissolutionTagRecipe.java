package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a dissolution tag recipe.  Similar to a normal dissolution recipe, but it outputs
 * a given tag rather than a specific stack.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipe extends AbstractTagCraftingRecipe<CraftingInput> implements IDissolutionRecipe {
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;
    
    public DissolutionTagRecipe(String group, TagKey<Item> recipeOutputTag, int recipeOutputAmount, Ingredient ingredient, SourceList manaCosts) {
        super(group, recipeOutputTag, recipeOutputAmount);
        this.ingredient = ingredient;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
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
        protected static final Codec<DissolutionTagRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(dr -> dr.group),
                    TagKey.codec(Registries.ITEM).fieldOf("outputTag").forGetter(dr -> dr.outputTag),
                    Codec.INT.fieldOf("outputAmount").forGetter(dr -> dr.outputAmount),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(dr -> dr.ingredient),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(dr -> dr.manaCosts)
                ).apply(instance, DissolutionTagRecipe::new);
        });
        
        @Override
        public Codec<DissolutionTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public DissolutionTagRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            Ingredient ing = Ingredient.fromNetwork(buffer);
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, buffer.readResourceLocation());
            int resultAmount = buffer.readVarInt();
            return new DissolutionTagRecipe(group, resultTag, resultAmount, ing, manaCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DissolutionTagRecipe recipe) {
            buffer.writeUtf(recipe.group);
            SourceList.toNetwork(buffer, recipe.manaCosts);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeResourceLocation(recipe.outputTag.location());
            buffer.writeVarInt(recipe.outputAmount);
        }
    }
}
