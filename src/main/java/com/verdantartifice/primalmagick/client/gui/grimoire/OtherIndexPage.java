package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.DisciplineButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RuneEnchantmentIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.StatisticsButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.TipsButton;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;


/**
 * Grimoire page showing a list of topics not directly tied to research entries.
 * 
 * @author Daedalus4096
 */
public class OtherIndexPage extends AbstractPage {
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.other_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        Component text;
        
        // Make room for page title
        y += 24;
        
        // Add attunements button if attunements are unlocked
        if (ResearchManager.isResearchComplete(mc.player, ResearchNames.ATTUNEMENTS.get().simpleKey())) {
            text = Component.translatable("grimoire.primalmagick.attunement_header");
            Button button = screen.addWidgetToScreen(new AttunementIndexButton(x + 12 + (side * 140), y, text, screen));
            y += button.getHeight();
        }
        
        // Add recipes button
        ResearchEntry firstSteps = ResearchEntries.getEntry(SimpleResearchKey.FIRST_STEPS);
        if (firstSteps != null && (firstSteps.isComplete(mc.player) || firstSteps.isInProgress(mc.player))) {
            text = Component.translatable("grimoire.primalmagick.recipe_index_header");
            Button button = screen.addWidgetToScreen(new RecipeIndexButton(x + 12 + (side * 140), y, text, screen));
            y += button.getHeight();
        }
        
        // Add rune enchantments button if rune enchantments are unlocked
        if (ResearchManager.isResearchComplete(mc.player, ResearchNames.UNLOCK_RUNE_ENCHANTMENTS.get().simpleKey())) {
            text = Component.translatable("grimoire.primalmagick.rune_enchantment_header");
            Button button = screen.addWidgetToScreen(new RuneEnchantmentIndexButton(x + 12 + (side * 140), y, text, screen));
            y += button.getHeight();
        }
        
        // Add scans button if at least one scan is unlocked
        ResearchDiscipline scans = ResearchDisciplines.getDiscipline("SCANS");
        if (scans != null && scans.getUnlockResearchKey().isKnownBy(mc.player)) {
            text = Component.translatable(scans.getNameTranslationKey());
            Button button = screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, text, screen, scans, true, true));
            y += button.getHeight();
        }
        
        // Add statistics button
        text = Component.translatable("grimoire.primalmagick.stats_header");
        Button button = screen.addWidgetToScreen(new StatisticsButton(x + 12 + (side * 140), y, text, screen));
        y += button.getHeight();
        
        // Add tips button
        text = Component.translatable("grimoire.primalmagick.tips_header");
        Button tipsButton = screen.addWidgetToScreen(new TipsButton(x + 12 + (side * 140), y, text, screen));
        y += tipsButton.getHeight();
    }
}
