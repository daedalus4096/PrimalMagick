package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for mana battery blocks.
 * 
 * @author Daedalus4096
 */
public class ManaBatteryScreen extends AbstractContainerScreen<ManaBatteryMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/mana_battery.png");

    public ManaBatteryScreen(ManaBatteryMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate charge progress indicator
        int charge = this.menu.getChargeProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 23, this.topPos + 23, 230, 0, charge + 1, 16);
    }
}
