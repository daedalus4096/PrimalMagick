package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for wand charger block.
 * 
 * @author Daedalus4096
 */
public class WandChargerScreen extends AbstractContainerScreen<WandChargerMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/wand_charger.png");

    public WandChargerScreen(WandChargerMenu screenMenu, Inventory inv, Component titleIn) {
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
        
        // Animate charge progress indicator
        int charge = this.menu.getChargeProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 75, this.topPos + 34, 176, 0, charge + 1, 16);
    }

}
