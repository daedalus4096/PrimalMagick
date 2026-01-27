package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.awt.Color;

/**
 * Root class for all grimoire pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPage implements GuiEventListener {
    private static final Identifier SEPARATOR_SPRITE = ResourceUtils.loc("grimoire/separator");

    /**
     * Render this grimoire page
     * 
     * @param side which side of the grimoire to render on, 0 for left, 1 for right
     * @param x
     * @param y
     * @param mouseX
     * @param mouseY
     */
    public abstract void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY);
    
    /**
     * Get the text for this page's title
     * @return the text for this page's title
     */
    protected abstract Component getTitleText();
    
    /**
     * Create the widgets to show on this grimoire page
     * 
     * @param screen the screen object to which widgets should be added
     * @param side which side of the grimoire to render on, 0 for left, 1 for right
     * @param x
     * @param y
     */
    public abstract void initWidgets(GrimoireScreen screen, int side, int x, int y);
    
    protected boolean renderTopTitleBar() {
        return true;
    }
    
    protected void renderTitle(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY, @Nullable Identifier icon) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        if (this.renderTopTitleBar()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SEPARATOR_SPRITE, x + 10 + (side * 140), y + 18, 96, 5);   // Render the separator bar above the title text
        }
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SEPARATOR_SPRITE, x + 10 + (side * 140), y + 35, 96, 5);   // Render the separator bar below the title text
        Component headerText = this.getTitleText();
        int width = mc.font.width(headerText.getString());
        int indent = 124;
        if (width <= 124) {
            guiGraphics.drawString(mc.font, headerText, x - 3 + (side * 140) + (indent / 2) - (width / 2), y + 25, Color.BLACK.getRGB(), false);
            if (icon != null) {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(x - 3 + (side * 140) + (indent / 2) - (width / 2) - 17, y + 21);
                guiGraphics.pose().scale(0.06F, 0.06F);
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, 0, 0, 0, 0, 255, 255, 256, 256);
                guiGraphics.pose().popMatrix();
            }
        } else {
            // Scale down the title text if necessary to make it fit on one line
            float scale = 124.0F / width;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x - 3 + (side * 140) + (indent / 2) - (width / 2 * scale), y + 25 + (1.0F * scale));
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.drawString(mc.font, headerText, 0, 0, Color.BLACK.getRGB(), false);
            if (icon != null) {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(x - 3 + (side * 140) + (indent / 2) - (width / 2 * scale) - 17, y + 21);
                guiGraphics.pose().scale(0.06F, 0.06F);
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, 0, 0, 0, 0, 255, 255, 256, 256);
                guiGraphics.pose().popMatrix();
            }
            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    public void setFocused(boolean pFocused) {
        // Do nothing
    }

    @Override
    public boolean isFocused() {
        return false;
    }
    
    public void tick() {
        // Do nothing by default
    }
}
