package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.EntryButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.SectionHeaderWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.UpcomingEntryWidget;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the list of available research entries in a discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplinePage extends AbstractPage {
    protected ResearchDiscipline discipline;
    protected List<Object> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public DisciplinePage(@Nonnull ResearchDiscipline discipline) {
        this(discipline, false);
    }
    
    public DisciplinePage(@Nonnull ResearchDiscipline discipline, boolean first) {
        this.discipline = discipline;
        this.firstPage = first;
    }
    
    @Nonnull
    public List<Object> getContents() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addContent(Object entry) {
        return this.contents.add(entry);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected Component getTitleText() {
        return Component.translatable(this.discipline.getNameTranslationKey());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, this.discipline.getIconLocation());
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = screen.getMinecraft();
        for (Object obj : this.getContents()) {
            if (obj instanceof ResearchEntry entry) {
                // If the current content object is a research entry, add a button for it to the screen
                Component text = Component.translatable(entry.getNameTranslationKey());
                if (entry.isAvailable(mc.player)) {
                    screen.addWidgetToScreen(new EntryButton(x + 12 + (side * 140), y, text, screen, entry, true));
                } else {
                    screen.addWidgetToScreen(new UpcomingEntryWidget(x + 12 + (side * 140), y, text, entry, true));
                }
            } else if (obj instanceof Component comp) {
                // If the current content object is a text component, add a section header with that text to the screen
                screen.addWidgetToScreen(new SectionHeaderWidget(x + 12 + (side * 140), y, comp));
            }
            y += 12;
        }
    }
}
