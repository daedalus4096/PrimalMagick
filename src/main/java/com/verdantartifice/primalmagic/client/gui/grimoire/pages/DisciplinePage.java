package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.EntryButton;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.SectionHeaderWidget;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
    protected String getTitleTranslationKey() {
        return this.discipline.getNameTranslationKey();
    }

    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(side, x, y, mouseX, mouseY);
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        for (Object obj : this.getContents()) {
            if (obj instanceof ResearchEntry) {
                ResearchEntry entry = (ResearchEntry)obj;
                String text = (new TranslationTextComponent(entry.getNameTranslationKey())).getFormattedText();
                screen.addWidgetToScreen(new EntryButton(x + 12 + (side * 140), y, text, screen, entry));
            } else if (obj instanceof ITextComponent) {
                String text = ((ITextComponent)obj).getFormattedText();
                screen.addWidgetToScreen(new SectionHeaderWidget(x + 12 + (side * 140), y, text));
            }
            y += 12;
        }
    }
}
