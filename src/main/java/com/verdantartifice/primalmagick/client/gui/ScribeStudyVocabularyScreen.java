package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.scribe_table.ScribeTableModeTabButton;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the study vocabulary mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyScreen extends AbstractContainerScreen<ScribeStudyVocabularyMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_study_vocabulary.png");
    
    protected final List<ScribeTableModeTabButton> tabButtons = new ArrayList<>();
    
    public ScribeStudyVocabularyScreen(ScribeStudyVocabularyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        int tabPosX = this.leftPos - 30;
        int tabPosY = this.topPos + 3;
        int tabCount = 0;
        this.tabButtons.clear();
        for (ScribeTableMode mode : ScribeTableMode.values()) {
            ScribeTableModeTabButton tab = new ScribeTableModeTabButton(mode);
            tab.setPosition(tabPosX, tabPosY + 27 * tabCount++);
            tab.setStateTriggered(mode == ScribeTableMode.STUDY_VOCABULARY);
            this.tabButtons.add(tab);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.tabButtons.forEach(tab -> tab.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick));
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Render background texture
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
