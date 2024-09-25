package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Grimoire page showing the page elements for a research stage.
 * 
 * @author Daedalus4096
 */
public class StagePage extends AbstractPage {
    protected final ResearchStage stage;
    protected final ResearchEntry parent;
    protected final List<IPageElement> contents = new ArrayList<>();
    protected final boolean firstPage;
    
    public StagePage(ResearchStage stage) {
        this(stage, false);
    }
    
    public StagePage(ResearchStage stage, boolean first) {
        Minecraft mc = Minecraft.getInstance();
        this.stage = stage;
        this.firstPage = first;
        this.parent = ResearchEntries.getEntry(mc.level.registryAccess(), stage.parentKey());
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
    protected Component getTitleText() {
        return this.parent == null ? Component.empty() : Component.translatable(this.parent.getNameTranslationKey());
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
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
