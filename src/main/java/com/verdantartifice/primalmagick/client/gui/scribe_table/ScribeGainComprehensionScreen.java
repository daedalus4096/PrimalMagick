package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.VocabularyWidget;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeGainComprehensionMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the gain comprehension mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionScreen extends AbstractScribeTableScreen<ScribeGainComprehensionMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_gain_comprehension.png");
    
    protected VocabularyWidget vocabularyWidget;
    
    public ScribeGainComprehensionScreen(ScribeGainComprehensionMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 230;
        this.imageHeight = 222;
    }

    @Override
    protected ScribeTableMode getMode() {
        return ScribeTableMode.GAIN_COMPREHENSION;
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Don't render labels in this mode
    }

    @Override
    protected void init() {
        super.init();
        this.vocabularyWidget = this.addRenderableWidget(new VocabularyWidget(this.menu.getBookLanguage(), this.menu.getVocabularyCount(), this.leftPos + 207, this.topPos + 7));
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

        // Update the vocabulary widget based on the current language in the menu
        BookLanguage lang = this.menu.getBookLanguage();
        this.vocabularyWidget.visible = lang.isComplex();
        this.vocabularyWidget.setLanguage(lang);
        this.vocabularyWidget.setAmount(this.menu.getVocabularyCount());
    }
}
