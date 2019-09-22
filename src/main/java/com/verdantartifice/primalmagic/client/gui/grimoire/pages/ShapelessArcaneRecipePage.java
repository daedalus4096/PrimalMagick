package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ItemStackWidget;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShapelessArcaneRecipePage extends AbstractRecipePage {
    protected ShapelessArcaneRecipe recipe;
    
    public ShapelessArcaneRecipePage(ShapelessArcaneRecipe recipe) {
        this.recipe = recipe;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shapeless_arcane_recipe_header";
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 140;
        int overlayWidth = 52;

        // Render ingredient stacks
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int index = 0; index < Math.min(ingredients.size(), 9); index++) {
            Ingredient ingredient = ingredients.get(index);
            if (ingredient != null) {
                screen.addWidgetToScreen(new IngredientWidget(ingredient, x - 17 + (side * 152) + (indent / 2) - (overlayWidth / 2) + ((index % 3) * 32), y + 67 + ((index / 3) * 32)));
            }
        }
        
        // Render output stack
        ItemStack output = this.recipe.getRecipeOutput();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 15 + (side * 152) + (indent / 2) - (overlayWidth / 2), y + 30, false));
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        super.render(side, x, y, mouseX, mouseY);
        y += 28;
        
        int indent = 140;
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
        GlStateManager.translatef(x - 17 + (side * 152) + (indent / 2), y + 50 + (overlayHeight / 2), 0.0F);
        GlStateManager.scalef(2.0F, 2.0F, 1.0F);
        this.blit(-(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight);
        GlStateManager.popMatrix();
    }
}
