package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a dissolution recipe.  Similar to a smelting recipe, but used by the dissolution chamber
 * instead of a furnace.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipe extends AbstractStackCraftingRecipe<CraftingInput> implements IDissolutionRecipe {
    protected final Ingredient ingredient;
    protected final SourceList manaCosts;
    
    public DissolutionRecipe(String group, ItemStack result, Ingredient ingredient, SourceList manaCosts) {
        super(group, result);
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
        return RecipeSerializersPM.DISSOLUTION.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    public static class Serializer implements RecipeSerializer<DissolutionRecipe> {
        protected static final Codec<DissolutionRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(dr -> dr.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(dr -> dr.output),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(dr -> dr.ingredient),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(dr -> dr.manaCosts)
                ).apply(instance, DissolutionRecipe::new);
        });
        
        @Override
        public Codec<DissolutionRecipe> codec() {
            return CODEC;
        }

        @Override
        public DissolutionRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            Ingredient ing = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new DissolutionRecipe(group, result, ing, manaCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DissolutionRecipe recipe) {
            buffer.writeUtf(recipe.group);
            SourceList.toNetwork(buffer, recipe.manaCosts);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }
    }
}
