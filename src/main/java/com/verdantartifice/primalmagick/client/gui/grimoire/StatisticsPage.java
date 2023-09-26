package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the player's mod-relevant statistics (e.g. the number of times they cast
 * a spell).
 * 
 * @author Daedalus4096
 */
public class StatisticsPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("stats", 0);
    
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public StatisticsPage() {
        this(false);
    }
    
    public StatisticsPage(boolean first) {
        this.firstPage = first;
    }
    
    @Nonnull
    public List<IPageElement> getElements() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addElement(IPageElement element) {
        return this.contents.add(element);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(guiGraphics, side, x, y);
            y = content.getNextY(y);
        }
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.stats_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
