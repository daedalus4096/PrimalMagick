package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

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
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    
    public RunecarvingRecipe(String group, ItemStack result, Ingredient ingredient1, Ingredient ingredient2, Optional<AbstractRequirement<?>> requirement) {
        super(group, result);
        this.requirement = requirement;
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
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }

    public static class Serializer implements RecipeSerializer<RunecarvingRecipe> {
        @Override
        public Codec<RunecarvingRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(rr -> rr.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.output),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(rr -> rr.ingredient1),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(rr -> rr.ingredient2),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(rr -> rr.requirement)
                ).apply(instance, RunecarvingRecipe::new)
            );
        }
        
        @Override
        public RunecarvingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            Ingredient ing1 = Ingredient.fromNetwork(buffer);
            Ingredient ing2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new RunecarvingRecipe(group, result, ing1, ing2, requirement);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RunecarvingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }
    }
}
