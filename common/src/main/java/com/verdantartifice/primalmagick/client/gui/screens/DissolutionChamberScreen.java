package com.verdantartifice.primalmagick.client.gui.screens;

import com.verdantartifice.primalmagick.client.gui.recipe_book.DissolutionChamberRecipeBookComponent;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.menus.DissolutionChamberMenu;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for the dissolution chamber block.
 * 
 * @author Daedalus4096
 */
public class DissolutionChamberScreen extends AbstractRecipeBookScreen<DissolutionChamberMenu> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/dissolution_chamber.png");
    protected static final Identifier PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");

    protected ManaGaugeWidget manaGauge;

    public DissolutionChamberScreen(DissolutionChamberMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, new DissolutionChamberRecipeBookComponent(screenMenu), inv, titleIn);
    }

    @Override
    @NotNull
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 80, this.topPos + 52);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 27;
        this.inventoryLabelX = 27;
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Sources.EARTH, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    protected void onRecipeBookButtonClick() {
        super.onRecipeBookButtonClick();
        this.manaGauge.setPosition(this.leftPos + 10, this.topPos + 6);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Animate progress indicator
        int cook = this.menu.getDissolutionProgressionScaled();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 78, this.topPos + 34, cook, 16);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
    }
}
