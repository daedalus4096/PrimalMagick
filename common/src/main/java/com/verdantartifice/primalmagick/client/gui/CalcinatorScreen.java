package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the calcinator block.
 * 
 * @author Daedalus4096
 */
public class CalcinatorScreen extends AbstractContainerScreenPM<CalcinatorMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/calcinator.png");
    protected static final Identifier PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");
    protected static final Identifier BURN_SPRITE = ResourceUtils.loc("burn_indicator");

    public CalcinatorScreen(CalcinatorMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        
        // Animate burn indicator
        if (this.menu.isBurning()) {
            int burn = this.menu.getBurnLeftScaled();
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_SPRITE, 14, 14, 0, 14 - burn, this.leftPos + 34, this.topPos + 48 - burn, 14, burn);
        }
        
        // Animate cook progress indicator
        int cook = this.menu.getCookProgressionScaled();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 75, this.topPos + 44, cook, 16);
    }
}
