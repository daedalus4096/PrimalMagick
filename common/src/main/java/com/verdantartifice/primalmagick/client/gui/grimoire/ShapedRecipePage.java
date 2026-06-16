package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.List;
import java.util.Objects;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedRecipePage extends AbstractShapedRecipePage {
    protected final RecipeHolder<ShapedRecipe> recipe;
    protected final ShapedCraftingRecipeDisplay display;

    public ShapedRecipePage(RecipeHolder<ShapedRecipe> recipe, RegistryAccess registryAccess) {
        super(recipe.value().display().getFirst().craftingStation(), registryAccess);
        this.recipe = recipe;
        if (this.recipe.value().display().getFirst() instanceof ShapedCraftingRecipeDisplay d) {
            this.display = d;
        } else {
            throw new IllegalArgumentException("Recipe slot display not of expected type");
        }
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shaped_recipe_header";
    }

    @Override
    protected ItemStack getRecipeResult() {
        Minecraft mc = Minecraft.getInstance();
        ContextMap contextMap = SlotDisplayContext.fromLevel(Objects.requireNonNull(mc.level));
        return this.display.result().resolveForFirstStack(contextMap);
    }

    @Override
    protected List<Ingredient> getRecipeIngredients() {
        return this.display.ingredients();
    }

    @Override
    protected int getRecipeWidth() {
        return this.display.width();
    }

    @Override
    protected int getRecipeHeight() {
        return this.display.height();
    }
}
