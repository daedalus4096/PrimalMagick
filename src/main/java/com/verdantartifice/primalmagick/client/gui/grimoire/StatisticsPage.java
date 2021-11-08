package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;

/**
 * Grimoire page showing the player's mod-relevant statistics (e.g. the number of times they cast
 * a spell).
 * 
 * @author Daedalus4096
 */
public class StatisticsPage extends AbstractPage {
    public static final String TOPIC = "stats";
    
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
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(matrixStack, side, x, y);
            y = content.getNextY(y);
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.stats_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
