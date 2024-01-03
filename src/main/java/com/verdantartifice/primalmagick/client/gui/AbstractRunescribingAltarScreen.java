package com.verdantartifice.primalmagick.client.gui;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.AbstractRunescribingAltarMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Base GUI screen for runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarScreen<T extends AbstractRunescribingAltarMenu> extends AbstractContainerScreen<T> {
    public AbstractRunescribingAltarScreen(T screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Nonnull
    protected abstract ResourceLocation getTextureLocation();

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(this.getTextureLocation(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
