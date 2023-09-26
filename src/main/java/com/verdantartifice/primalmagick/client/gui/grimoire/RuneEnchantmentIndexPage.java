package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RuneEnchantmentButton;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Grimoire page showing the list of discovered rune enchantments.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentIndexPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("rune_enchantments", 0);

    protected List<Enchantment> contents = new ArrayList<>();
    protected boolean firstPage;

    public RuneEnchantmentIndexPage() {
        this(false);
    }
    
    public RuneEnchantmentIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        }
    }
    
    @Nonnull
    public List<Enchantment> getEnchantments() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addEnchantment(Enchantment enchant) {
        return this.contents.add(enchant);
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.rune_enchantment_header");
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each enchantment in the page's contents
        for (Enchantment enchant : this.getEnchantments()) {
            Component text = Component.translatable(enchant.getDescriptionId());
            screen.addWidgetToScreen(new RuneEnchantmentButton(x + 12 + (side * 140), y, text, screen, enchant));
            y += 12;
        }
    }
}
