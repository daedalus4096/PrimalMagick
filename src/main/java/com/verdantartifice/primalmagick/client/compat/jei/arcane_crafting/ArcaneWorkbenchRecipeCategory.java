package com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Recipe category class for an arcane workbench recipe.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchRecipeCategory extends RecipeCategoryPM<IArcaneRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "arcane_workbench");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/arcane_workbench.png");
    private static final int MANA_COST_X_OFFSET = 64;
    private static final int MANA_COST_Y_OFFSET = 1;
    
    private final ICraftingGridHelper craftingGridHelper;
    private final IDrawableStatic manaCostIcon;

    public ArcaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        super(IArcaneRecipe.class, guiHelper, UID, "block.primalmagick.arcane_workbench");
        this.craftingGridHelper = guiHelper.createCraftingGridHelper(1);
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 116, 0, 16, 16);
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 116, 54));
        this.setIcon(new ItemStack(ItemsPM.ARCANE_WORKBENCH.get()));
    }

    @Override
    public void setIngredients(IArcaneRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IArcaneRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        // Initialize recipe output
        guiItemStacks.init(0, false, 94, 18);
        guiItemStacks.set(0, recipe.getResultItem());
        
        // Initialize recipe inputs
        for (int index = 0; index < 9; index++) {
            int x = index % 3;
            int y = index / 3;
            guiItemStacks.init(index + 1, true, x * 18, y * 18);
        }
        
        if (recipe instanceof IShapedRecipe<?> shapedRecipe) {
            this.craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(VanillaTypes.ITEM), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight());
        } else {
            this.craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(VanillaTypes.ITEM));
            recipeLayout.setShapeless();
        }
    }

    @Override
    public void draw(IArcaneRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(stack, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public List<Component> getTooltipStrings(IArcaneRecipe recipe, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.getManaCosts();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(new TranslatableComponent("primalmagick.crafting.mana_cost_header"));
            for (Source source : manaCosts.getSourcesSorted()) {
                tooltip.add(new TranslatableComponent("primalmagick.crafting.mana_tooltip", manaCosts.getAmount(source), source.getNameText()));
            }
            return tooltip;
        } else {
            return super.getTooltipStrings(recipe, mouseX, mouseY);
        }
    }
}
