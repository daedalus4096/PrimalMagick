package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.DesalinatorMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for desalinator block.
 *
 * @author Daedalus4096
 */
public class DesalinatorScreen extends AbstractContainerScreenPM<DesalinatorMenu> {
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/desalinator.png");
    protected static final ResourceLocation WATER_TEXTURE = ResourceUtils.loc("textures/block/water_still.png");
    protected static final ResourceLocation PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");
    protected static final ResourceLocation GAUGE_SPRITE = ResourceUtils.loc("desalinator/gauge_markers");
    protected static final int TANK_HEIGHT = 71;

    protected ManaGaugeWidget manaGauge;

    public DesalinatorScreen(DesalinatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 185;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 16, Sources.SUN, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Animate boil progress indicator
        int cook = this.menu.getBoilProgressionScaled();
        guiGraphics.blitSprite(PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 75, this.topPos + 44, cook, 16);

        // Draw water tank contents
        int height = Mth.floor(this.menu.getCurrentWaterAmount() * (float)TANK_HEIGHT / this.menu.getWaterCapacity());
        int dy = TANK_HEIGHT - height;
        guiGraphics.blit(WATER_TEXTURE, this.leftPos + 52, this.topPos + 17 + dy, 0, 0, 16, height);
        guiGraphics.blitSprite(GAUGE_SPRITE, this.leftPos + 52, this.topPos + 17, 16, 71);
    }
}
