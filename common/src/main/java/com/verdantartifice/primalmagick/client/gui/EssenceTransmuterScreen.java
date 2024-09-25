package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.EssenceTransmuterMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the essence transmuter block.
 * 
 * @author Daedalus4096
 */
public class EssenceTransmuterScreen extends AbstractContainerScreenPM<EssenceTransmuterMenu> {
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/essence_transmuter.png");
    
    protected ManaGaugeWidget manaGauge;

    public EssenceTransmuterScreen(EssenceTransmuterMenu screenMenu, Inventory inv, Component titleIn) {
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
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Sources.MOON, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate spin progress indicator
        int cook = this.menu.getTransmuteProgressionScaled();
        guiGraphics.blit(TEXTURE, this.leftPos + 67, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
