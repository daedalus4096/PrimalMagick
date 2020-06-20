package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.RuneEnchantmentButton;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the list of discovered rune enchantments.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RuneEnchantmentIndexPage extends AbstractPage {
    public static final String TOPIC = "rune_enchantments";

    protected boolean firstPage;

    public RuneEnchantmentIndexPage() {
        this(false);
    }
    
    public RuneEnchantmentIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(side, x, y, mouseX, mouseY, null);
        }
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.rune_enchantment_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discovered rune enchantment
        for (Enchantment enchant : RuneManager.getRuneEnchantmentsSorted()) {
            if (ResearchManager.isResearchComplete(Minecraft.getInstance().player, SimpleResearchKey.parseRuneEnchantment(enchant))) {
                String text = new TranslationTextComponent(enchant.getName()).getFormattedText();
                screen.addWidgetToScreen(new RuneEnchantmentButton(x + 12 + (side * 140), y, text, screen, enchant));
                y += 12;
            }
        }
    }
}
