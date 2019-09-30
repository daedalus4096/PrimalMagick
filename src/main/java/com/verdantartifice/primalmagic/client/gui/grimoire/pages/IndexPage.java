package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.DisciplineButton;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IndexPage extends AbstractPage {
    protected List<ResearchDiscipline> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public IndexPage() {
        this(false);
    }
    
    public IndexPage(boolean first) {
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
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.index_header";
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
        for (ResearchDiscipline discipline : this.getDisciplines()) {
            String text = (new TranslationTextComponent(discipline.getNameTranslationKey())).getFormattedText();
            screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, text, screen, discipline));
            y += 12;
        }
    }
}
