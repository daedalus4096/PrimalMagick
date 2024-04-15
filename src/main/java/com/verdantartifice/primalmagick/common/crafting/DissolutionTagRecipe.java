package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a dissolution tag recipe.  Similar to a normal dissolution recipe, but it outputs
 * a given tag rather than a specific stack.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipe extends AbstractTagCraftingRecipe<Container> implements IDissolutionRecipe {
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;
    
    public DissolutionTagRecipe(String group, Ingredient ingredient, TagKey<Item> recipeOutputTag, int recipeOutputAmount, SourceList manaCosts) {
        super(group, recipeOutputTag, recipeOutputAmount);
        this.ingredient = ingredient;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(this.ingredient);
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
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(dr -> dr.ingredient),
                    TagKey.codec(Registries.ITEM).fieldOf("recipeOutputTag").forGetter(dr -> dr.outputTag),
                    Codec.INT.fieldOf("recipeOutputAmount").forGetter(dr -> dr.outputAmount),
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
            return new DissolutionTagRecipe(group, ing, resultTag, resultAmount, manaCosts);
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
