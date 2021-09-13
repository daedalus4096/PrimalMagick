package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.crafting.Recipe;

/**
 * GUI page to show recipe metadata in the grimoire.
 * 
 * @author Daedalus4096
 */
public class RecipeMetadataPage extends AbstractPage {
    protected final Recipe<?> recipe;
    protected final boolean firstPage;
    
    public RecipeMetadataPage(Recipe<?> recipe) {
        this(recipe, false);
    }
    
    public RecipeMetadataPage(Recipe<?> recipe, boolean firstPage) {
        this.recipe = recipe;
        this.firstPage = firstPage;
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }
        
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        // Render the metadata's discipline header
        mc.font.draw(matrixStack, new TranslatableComponent("primalmagic.grimoire.recipe_metadata.discipline").withStyle(ChatFormatting.UNDERLINE), x - 1 + (side * 138), y - 6, Color.BLACK.getRGB());
        y += mc.font.lineHeight;
        
        // TODO Render the metadata's discipline data
        y += 2 * mc.font.lineHeight;
        
        // Render the metadata's entry header
        mc.font.draw(matrixStack, new TranslatableComponent("primalmagic.grimoire.recipe_metadata.entry").withStyle(ChatFormatting.UNDERLINE), x - 1 + (side * 138), y - 6, Color.BLACK.getRGB());
        y += mc.font.lineHeight;
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
