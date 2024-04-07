package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.books.BookLanguage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the page elements for a language description.
 * 
 * @author Daedalus4096
 */
public class LinguisticsDescriptionPage extends AbstractPage {
    protected final Holder<BookLanguage> lang;
    protected final List<IPageElement> contents = new ArrayList<>();
    
    public LinguisticsDescriptionPage(Holder<BookLanguage> lang) {
        this.lang = lang;
    }
    
    @Nonnull
    public List<IPageElement> getElements() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addElement(IPageElement element) {
        return this.contents.add(element);
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        // This is never the first page in a topic
        return false;
    }

    @Override
    protected Component getTitleText() {
        // This is never the first page in a topic
        return CommonComponents.EMPTY;
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        y += 25;
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(guiGraphics, side, x, y);
            y = content.getNextY(y);
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
