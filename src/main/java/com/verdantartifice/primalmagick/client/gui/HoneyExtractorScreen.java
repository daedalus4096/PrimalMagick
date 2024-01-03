package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for honey extractor block.
 * 
 * @author Daedalus4096
 */
public class HoneyExtractorScreen extends AbstractContainerScreen<HoneyExtractorMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/honey_extractor.png");
    
    protected ManaGaugeWidget manaGauge;

    public HoneyExtractorScreen(HoneyExtractorMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.titleLabelX = 27;
        this.inventoryLabelX = 27;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Source.SKY, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate spin progress indicator
        int cook = this.menu.getSpinProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 75, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
