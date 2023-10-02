package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ResearchWidget;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Grimoire page showing the page elements for a rune enchantment.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentPage extends AbstractPage {
    protected static final ResourceLocation OVERLAY = PrimalMagick.resource("textures/gui/grimoire_overlay.png");
    protected static final Supplier<SimpleResearchKey> UNKNOWN_RUNE = ResearchNames.simpleKey(ResearchNames.INTERNAL_UNKNOWN_RUNE);
    
    protected Enchantment enchant;
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public RuneEnchantmentPage(Enchantment enchant) {
        this(enchant, false);
    }
    
    public RuneEnchantmentPage(Enchantment enchant, boolean first) {
        this.enchant = enchant;
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
    protected Component getTitleText() {
        return Component.translatable(this.enchant.getDescriptionId());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        int startY = y;
        int indent = 84;
        int overlayWidth = 13;
        int overlayHeight = 13;
        
        // Draw title page and overlay background if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            y += 77;
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + (side * 140) + (indent / 2) - (overlayWidth / 2), startY + 49, 0.0F);
            guiGraphics.blit(OVERLAY, 0, 0, 0, 51, overlayWidth, overlayHeight);
            guiGraphics.blit(OVERLAY, 32, 0, 0, 51, overlayWidth, overlayHeight);
            guiGraphics.pose().popPose();
        } else {
            y += 25;
        }
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(guiGraphics, side, x, y);
            y = content.getNextY(y);
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        int indent = 124;
        int overlayWidth = 52;

        // Render rune item stacks if applicable
        if (this.isFirstPage() && side == 0 && RuneManager.hasRuneDefinition(this.enchant)) {
            RuneEnchantmentDefinition def = RuneManager.getRuneDefinition(this.enchant);
            
            int widgetXPos = x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2);
            if (RuneManager.isRuneKnown(mc.player, enchant, RuneType.VERB)) {
                screen.addWidgetToScreen(new ItemStackWidget(RuneItem.getRune(def.getVerb()), widgetXPos, y, false));
            } else {
                screen.addWidgetToScreen(new ResearchWidget(UNKNOWN_RUNE.get(), widgetXPos, y, false));
            }
            
            widgetXPos += 32;
            if (RuneManager.isRuneKnown(mc.player, enchant, RuneType.NOUN)) {
                screen.addWidgetToScreen(new ItemStackWidget(RuneItem.getRune(def.getNoun()), widgetXPos, y, false));
            } else {
                screen.addWidgetToScreen(new ResearchWidget(UNKNOWN_RUNE.get(), widgetXPos, y, false));
            }
            
            widgetXPos += 32;
            if (RuneManager.isRuneKnown(mc.player, enchant, RuneType.SOURCE)) {
                screen.addWidgetToScreen(new ItemStackWidget(RuneItem.getRune(def.getSource()), widgetXPos, y, false));
            } else {
                screen.addWidgetToScreen(new ResearchWidget(UNKNOWN_RUNE.get(), widgetXPos, y, false));
            }
        }
    }
}
