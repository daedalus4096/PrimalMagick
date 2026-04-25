package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.DesalinatorMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * GUI screen for desalinator block.
 *
 * @author Daedalus4096
 */
public class DesalinatorScreen extends AbstractContainerScreenPM<DesalinatorMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/desalinator.png");
    protected static final Identifier WATER_TEXTURE = ResourceUtils.loc("textures/block/water_still.png");
    protected static final Identifier PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");
    protected static final Identifier GAUGE_SPRITE = ResourceUtils.loc("desalinator/gauge_markers");
    protected static final int TANK_HEIGHT = 71;

    protected ManaGaugeWidget manaGauge;

    public DesalinatorScreen(DesalinatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, 176, 185);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 16, Sources.SUN, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    public void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        super.extractContents(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void extractTooltip(@NonNull GuiGraphicsExtractor pGuiGraphics, int pX, int pY) {
        super.extractTooltip(pGuiGraphics, pX, pY);
        if (pX >= this.leftPos + 52 && pX <= this.leftPos + 68 && pY >= this.topPos + 17 && pY <= this.topPos + 88) {
            Component tankTooltip = Component.translatable("tooltip.primalmagick.desalinator.tank.water", this.menu.getCurrentWaterAmount(), this.menu.getWaterCapacity());
            pGuiGraphics.setTooltipForNextFrame(this.font, tankTooltip, pX, pY);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Animate boil progress indicator
        int cook = this.menu.getBoilProgressionScaled();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 75, this.topPos + 44, cook, 16);

        // Draw water tank contents
        int height = Mth.floor(this.menu.getCurrentWaterAmount() * (float)TANK_HEIGHT / this.menu.getWaterCapacity());
        int dy = TANK_HEIGHT - height;
        int tick = this.menu.getTile().getTicks();
        int frameOffsetY = tick % 28;
        int vOffset = 16 * frameOffsetY;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WATER_TEXTURE, this.leftPos + 52, this.topPos + 17 + dy, 16, height, 0, vOffset, 16, height, 16, 512);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, GAUGE_SPRITE, this.leftPos + 52, this.topPos + 17, 16, 71);
    }
}
