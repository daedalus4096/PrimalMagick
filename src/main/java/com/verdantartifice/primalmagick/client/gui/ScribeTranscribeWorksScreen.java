package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.ScribeTranscribeWorksMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the transcribe works mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeTranscribeWorksScreen extends AbstractContainerScreen<ScribeTranscribeWorksMenu> {
    public ScribeTranscribeWorksScreen(ScribeTranscribeWorksMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // TODO Auto-generated method stub
        
    }
}
