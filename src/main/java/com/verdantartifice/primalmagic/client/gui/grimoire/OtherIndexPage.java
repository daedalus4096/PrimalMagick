package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.AttunementIndexButton;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.DisciplineButton;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.StatisticsButton;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
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
        this.renderTitle(side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.other_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        String text;
        
        // Make room for page title
        y += 24;
        
        // Add attunements button if attunements are unlocked
        if (ResearchManager.isResearchComplete(Minecraft.getInstance().player, SimpleResearchKey.parse("ATTUNEMENTS"))) {
            text = (new TranslationTextComponent("primalmagic.grimoire.attunement_header")).getFormattedText();
            screen.addWidgetToScreen(new AttunementIndexButton(x + 12 + (side * 140), y, text, screen));
            y += 12;
        }
        
        // Add scans button if at least one scan is unlocked
        ResearchDiscipline scans = ResearchDisciplines.getDiscipline("SCANS");
        if (scans != null && scans.getUnlockResearchKey().isKnownBy(Minecraft.getInstance().player)) {
            text = (new TranslationTextComponent(scans.getNameTranslationKey())).getFormattedText();
            screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, text, screen, scans));
            y += 12;
        }
        
        // Add statistics button
        text = (new TranslationTextComponent("primalmagic.grimoire.stats_header")).getFormattedText();
        screen.addWidgetToScreen(new StatisticsButton(x + 12 + (side * 140), y, text, screen));
        y += 12;
    }
}
