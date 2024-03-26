package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

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
        // TODO Auto-generated method stub

    }
}
