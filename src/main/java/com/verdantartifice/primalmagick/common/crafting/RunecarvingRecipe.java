package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe extends AbstractStackCraftingRecipe<Container> implements IRunecarvingRecipe {
    protected final CompoundResearchKey research;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    
    public RunecarvingRecipe(String group, CompoundResearchKey research, Ingredient ingredient1, Ingredient ingredient2, ItemStack result) {
        super(group, result);
        this.research = research;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 2;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient1, this.ingredient2);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RUNECARVING.get();
    }

    @Override
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
    }

    public static class Serializer implements RecipeSerializer<RunecarvingRecipe> {
        protected static final Codec<RunecarvingRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(rr -> rr.group),
                    CompoundResearchKey.CODEC.fieldOf("research").forGetter(rr -> rr.research),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(rr -> rr.ingredient1),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(rr -> rr.ingredient2),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.output)
                ).apply(instance, RunecarvingRecipe::new);
        });
        
        @Override
        public Codec<RunecarvingRecipe> codec() {
            return CODEC;
        }

        @Override
        public RunecarvingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            Ingredient ing1 = Ingredient.fromNetwork(buffer);
            Ingredient ing2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new RunecarvingRecipe(group, research, ing1, ing2, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RunecarvingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }
    }
}
