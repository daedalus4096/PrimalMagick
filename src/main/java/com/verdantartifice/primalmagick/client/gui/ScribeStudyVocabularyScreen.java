package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the study vocabulary mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyScreen extends AbstractContainerScreen<ScribeStudyVocabularyMenu> {
    public ScribeStudyVocabularyScreen(ScribeStudyVocabularyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // TODO Auto-generated method stub
        
    }
}
