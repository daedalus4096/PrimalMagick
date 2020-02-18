package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the details of a discovered attunement.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AttunementPage extends AbstractPage {
    protected Source source;
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;

    public AttunementPage(@Nonnull Source source) {
        this(source, false);
    }
    
    public AttunementPage(@Nonnull Source source, boolean first) {
        this.source = source;
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
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(side, x, y, mouseX, mouseY);
            y += 53;
        } else {
            y += 25;
        }
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(side, x, y);
            y = content.getNextY(y);
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.source.getNameTranslationKey();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        if (this.isFirstPage()) {
            // TODO Render attunement meter
        }
    }

}
