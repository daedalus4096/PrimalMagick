package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.DisciplineButton;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the list of available disciplines.
 * 
 * @author Daedalus4096
 */
public class DisciplineIndexPage extends AbstractPage {
    protected List<ResearchDiscipline> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public DisciplineIndexPage() {
        this(false);
    }
    
    public DisciplineIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Nonnull
    public List<ResearchDiscipline> getDisciplines() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addDiscipline(ResearchDiscipline discipline) {
        return this.contents.add(discipline);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.index_header");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discipline in the page contents
        for (ResearchDiscipline discipline : this.getDisciplines()) {
            Component text = Component.translatable(discipline.getNameTranslationKey());
            DisciplineButton button = screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, text, screen, discipline, true, true));
            y += button.getHeight();
        }
    }
}
