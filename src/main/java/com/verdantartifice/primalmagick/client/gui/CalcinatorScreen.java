package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the calcinator block.
 * 
 * @author Daedalus4096
 */
public class CalcinatorScreen extends AbstractContainerScreen<CalcinatorMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/calcinator.png");

    public CalcinatorScreen(CalcinatorMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate burn indicator
        if (this.menu.isBurning()) {
            int burn = this.menu.getBurnLeftScaled();
            guiGraphics.blit(TEXTURE, this.leftPos + 34, this.topPos + 48 - burn, 176, 12 - burn, 14, burn + 1);
        }
        
        // Animate cook progress indicator
        int cook = this.menu.getCookProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 57, this.topPos + 34, 176, 14, cook + 1, 16);
    }
}
