package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
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
public class ShapedArcaneRecipe extends AbstractStackCraftingRecipe<CraftingContainer> implements IShapedArcaneRecipePM {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    protected final ShapedRecipePattern pattern;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    
    public ShapedArcaneRecipe(String group, ItemStack output, ShapedRecipePattern pattern, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts) {
        super(group, output);
        this.pattern = pattern;
        this.requirement = requirement;
        this.manaCosts = manaCosts;
    }
    
    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
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

    public static class Serializer implements RecipeSerializer<ShapedArcaneRecipe> {
        public static final Codec<ShapedArcaneRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(r -> r.group),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
                ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(rsar -> rsar.requirement),
                SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(rsar -> rsar.manaCosts)
        ).apply(instance, ShapedArcaneRecipe::new));

        @Override
        public Codec<ShapedArcaneRecipe> codec() {
            return CODEC;
        }

        @Override
        public ShapedArcaneRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            ShapedRecipePattern pattern = ShapedRecipePattern.fromNetwork(buffer);
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            ItemStack stack = buffer.readItem();
            return new ShapedArcaneRecipe(group, stack, pattern, requirement, manaCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedArcaneRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.pattern.toNetwork(buffer);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            SourceList.toNetwork(buffer, recipe.manaCosts);
            buffer.writeItem(recipe.output);
        }
    }
}
