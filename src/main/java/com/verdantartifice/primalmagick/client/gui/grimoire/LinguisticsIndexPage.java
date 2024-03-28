package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.List;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.LinguisticsButton;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the list of discovered static book languages.
 * 
 * @author Daedalus4096
 */
public class LinguisticsIndexPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("linguistics", 0);

    protected boolean firstPage;

    public LinguisticsIndexPage() {
        this(false);
    }
    
    public LinguisticsIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        }
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.linguistics_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discovered language
        Minecraft mc = Minecraft.getInstance();
        List<BookLanguage> known = BookLanguagesPM.LANGUAGES.get().getValues().stream().filter(lang -> LinguisticsManager.isLanguageKnown(mc.player, lang)).sorted((a, b) -> {
            return a.getName().getString().compareTo(b.getName().getString());
        }).toList();
        for (BookLanguage lang : known) {
            screen.addWidgetToScreen(new LinguisticsButton(x + 12 + (side * 140), y, lang.getName(), screen, lang));
            y += 12;
        }
    }
}
