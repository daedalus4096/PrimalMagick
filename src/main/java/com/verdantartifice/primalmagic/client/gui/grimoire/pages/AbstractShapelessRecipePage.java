package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ItemStackWidget;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base class for grimoire shapeless recipe pages.
 * 
 * @author Daedalus4096
 * @param <T> type of recipe, e.g. ShapelessArcaneRecipe
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractShapelessRecipePage<T extends IRecipe<?>> extends AbstractRecipePage {
    protected T recipe;
    
    public AbstractShapelessRecipePage(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 52;

        // Render ingredient stacks
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int index = 0; index < Math.min(ingredients.size(), 9); index++) {
            Ingredient ingredient = ingredients.get(index);
            if (ingredient != null) {
                screen.addWidgetToScreen(new IngredientWidget(ingredient, x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + ((index % 3) * 32), y + 67 + ((index / 3) * 32)));
            }
        }
        
        // Render output stack
        ItemStack output = this.recipe.getRecipeOutput();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        super.render(side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 52;
        int overlayHeight = 52;
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);        
        Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY);
        
        // Render overlay background
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translatef(x - 5 + (side * 140) + (indent / 2), y + 50 + (overlayHeight / 2), 0.0F);
        GlStateManager.scalef(2.0F, 2.0F, 1.0F);
        this.blit(-(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight);
        GlStateManager.popMatrix();
    }
}
