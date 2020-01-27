package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.StatisticsButton;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing a list of topics not directly tied to research entries.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class OtherIndexPage extends AbstractPage {
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        this.renderTitle(side, x, y, mouseX, mouseY);
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.other_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Make room for page title
        y += 24;
        
        // TODO Add attunements button if attunements are unlocked
        // TODO Add scans button if at least one scan is unlocked
        
        // Add statistics button
        String text = (new TranslationTextComponent("primalmagic.grimoire.stats_header")).getFormattedText();
        screen.addWidgetToScreen(new StatisticsButton(x + 12 + (side * 140), y, text, screen));
        y += 12;
    }
}
