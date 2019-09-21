package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShapedArcaneRecipePage extends AbstractRecipePage {
    protected ShapedArcaneRecipe recipe;
    
    public ShapedArcaneRecipePage(ShapedArcaneRecipe recipe) {
        this.recipe = recipe;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shaped_arcane_recipe_header";
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // TODO Render ingredient stacks
        // TODO Render output stack
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
