package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.AbstractRunescribingAltarMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Base GUI screen for runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarScreen<T extends AbstractRunescribingAltarMenu> extends AbstractContainerScreenPM<T> {
    public AbstractRunescribingAltarScreen(T screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @NotNull
    protected abstract Identifier getTextureLocation();

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.getTextureLocation(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
