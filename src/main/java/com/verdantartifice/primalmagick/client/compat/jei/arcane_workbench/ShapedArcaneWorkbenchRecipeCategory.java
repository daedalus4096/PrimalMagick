package com.verdantartifice.primalmagick.client.compat.jei.arcane_workbench;

import java.util.Arrays;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Recipe category class for a arcane workbench recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedArcaneWorkbenchRecipeCategory extends RecipeCategoryPM<ShapedArcaneRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "shaped_arcane_workbench");

    public ShapedArcaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        super(ShapedArcaneRecipe.class, guiHelper, UID, "block.primalmagick.arcane_workbench");
        this.setBackground(guiHelper.createBlankDrawable(116, 54));
        this.setIcon(new ItemStack(ItemsPM.ARCANE_WORKBENCH.get()));
    }

    @Override
    public void setIngredients(ShapedArcaneRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ShapedArcaneRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        // Initialize recipe output
        guiItemStacks.init(0, false, 94, 18);
        guiItemStacks.set(0, recipe.getResultItem());
        
        // Initialize recipe inputs
        for (int index = 0; index < 9; index++) {
            int x = index % 3;
            int y = index / 3;
            guiItemStacks.init(index + 1, true, x * 18, y * 18);
            guiItemStacks.set(index, Arrays.asList(recipe.getIngredients().get(index).getItems()));
        }
    }

    @Override
    public void draw(ShapedArcaneRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
        // TODO Show mana costs and possibly research requirements
        super.draw(recipe, stack, mouseX, mouseY);
    }
}
