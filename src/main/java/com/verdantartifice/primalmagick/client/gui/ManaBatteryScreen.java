package com.verdantartifice.primalmagick.client.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for mana battery blocks.
 * 
 * @author Daedalus4096
 */
public class ManaBatteryScreen extends AbstractContainerScreen<ManaBatteryMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/mana_battery.png");
    protected static final int AVAILABLE_GAUGE_WIDTH = 116;
    protected static final int GAUGE_START_X = 57;
    protected static final int GAUGE_WIDTH = 12;
    
    protected final Map<Source, ManaGaugeWidget> manaGauges = new HashMap<>();

    public ManaBatteryScreen(ManaBatteryMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.imageWidth = 230;
        this.imageHeight = 164;
        this.inventoryLabelX = 34;
    }

    @Override
    protected void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();
        List<Source> knownSources = Source.SORTED_SOURCES.stream().filter(s -> s.isDiscovered(mc.player)).toList();
        int gapWidth = (AVAILABLE_GAUGE_WIDTH - (GAUGE_WIDTH * knownSources.size())) / (knownSources.size() - 1);
        int bonusEdge = AVAILABLE_GAUGE_WIDTH - (GAUGE_WIDTH * knownSources.size()) - (gapWidth * (knownSources.size() - 1));
        int xOffset = GAUGE_START_X + (bonusEdge / 2);
        for (Source source : knownSources) {
            this.manaGauges.put(source, this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + xOffset, this.topPos + 16, source, this.menu.getCurrentMana(source), this.menu.getMaxMana(source))));
            xOffset += (GAUGE_WIDTH + gapWidth);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauges.forEach((source, gauge) -> {
            gauge.setCurrentMana(this.menu.getCurrentMana(source));
            gauge.setMaxMana(this.menu.getMaxMana(source));
        });
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate charge progress indicator
        int charge = this.menu.getChargeProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 29, this.topPos + 34, 230, 0, charge + 1, 16);
    }
}
