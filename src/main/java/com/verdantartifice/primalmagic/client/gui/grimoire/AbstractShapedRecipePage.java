package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.RecipeTypeWidget;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Base class for grimoire shaped recipe pages.
 * 
 * @author Daedalus4096
 * @param <T> type of recipe, e.g. ShapedArcaneRecipe
 */
public abstract class AbstractShapedRecipePage<T extends IShapedRecipe<?>> extends AbstractRecipePage {
    protected T recipe;
    
    public AbstractShapedRecipePage(T recipe) {
        this.recipe = recipe;
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render ingredient stacks
        int recipeWidth = this.recipe.getRecipeWidth();
        int recipeHeight = this.recipe.getRecipeHeight();
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int i = 0; i < Math.min(recipeWidth, 3); i++) {
            for (int j = 0; j < Math.min(recipeHeight, 3); j++) {
                Ingredient ingredient = ingredients.get(i + j * recipeWidth);
                if (ingredient != null) {
                    screen.addWidgetToScreen(new IngredientWidget(ingredient, x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + (i * 32), y + 67 + (j * 32), screen));
                }
            }
        }
        
        // Render output stack
        ItemStack output = this.recipe.getResultItem();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, new TranslatableComponent(this.getRecipeTypeTranslationKey())));
    }
    
    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        super.render(matrixStack, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 51;
        int overlayHeight = 51;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderTexture(0, OVERLAY);
        
        // Render overlay background
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2), 0.0F);
        matrixStack.scale(2.0F, 2.0F, 1.0F);
        this.blit(matrixStack, -(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight);
        matrixStack.popPose();
    }
}
