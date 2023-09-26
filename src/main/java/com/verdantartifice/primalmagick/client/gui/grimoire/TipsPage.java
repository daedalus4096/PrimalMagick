package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the page elements for a game hint.
 * 
 * @author Daedalus4096
 */
public class TipsPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("tips", 0);

    protected final List<IPageElement> contents = new ArrayList<>();
    protected final boolean firstPage;
    protected boolean lastPage = false;
    
    public TipsPage() {
        this(false);
    }
    
    public TipsPage(boolean firstPage) {
        this.firstPage = firstPage;
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
    
    public boolean isLastPage() {
        return this.lastPage;
    }
    
    public void setLastPage(boolean value) {
        this.lastPage = value;
    }
    
    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.tips_header");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page and overlay background if applicable
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
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        if (this.isLastPage()) {
            screen.addWidgetToScreen(Button.builder(Component.translatable("grimoire.primalmagick.next_tip_button"), button -> {
                screen.invalidateCurrentTip();
                screen.gotoTopic(TOPIC, false);
            }).bounds(x + 16 + (side * 136), y + 116 + (this.isFirstPage() ? 0 : 23), 119, 20).build());
        }
    }
}
