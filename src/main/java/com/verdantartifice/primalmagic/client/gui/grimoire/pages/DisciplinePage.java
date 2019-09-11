package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

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
}
