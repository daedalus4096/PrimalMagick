package com.verdantartifice.primalmagic.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagic.common.containers.DissolutionChamberContainer;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the dissolution chamber block.
 * 
 * @author Daedalus4096
 */
public class DissolutionChamberScreen extends AbstractContainerScreen<DissolutionChamberContainer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/dissolution_chamber.png");
    protected static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    
    protected ManaGaugeWidget manaGauge;

    public DissolutionChamberScreen(DissolutionChamberContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.menu.getCurrentMana());
        this.manaGauge.setMaxMana(this.menu.getMaxMana());
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Source.EARTH, this.menu.getCurrentMana(), this.menu.getMaxMana()));
        
        this.addRenderableWidget(new ImageButton(this.leftPos + 80, this.topPos + 52, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            LOGGER.info("Toggling dissolution chamber recipe book visibility");
        }));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        // Don't draw title text
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        // Render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate progress indicator
        int cook = this.menu.getDissolutionProgressionScaled();
        this.blit(matrixStack, this.leftPos + 78, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
