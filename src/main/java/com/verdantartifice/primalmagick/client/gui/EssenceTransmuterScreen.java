package com.verdantartifice.primalmagick.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagick.common.containers.EssenceTransmuterContainer;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the essence transmuter block.
 * 
 * @author Daedalus4096
 */
public class EssenceTransmuterScreen extends AbstractContainerScreen<EssenceTransmuterContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/essence_transmuter.png");
    
    protected ManaGaugeWidget manaGauge;

    public EssenceTransmuterScreen(EssenceTransmuterContainer screenContainer, Inventory inv, Component titleIn) {
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
        this.manaGauge = this.addRenderableWidget(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Source.MOON, this.menu.getCurrentMana(), this.menu.getMaxMana()));
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
        
        // Animate spin progress indicator
        int cook = this.menu.getTransmuteProgressionScaled();
        this.blit(matrixStack, this.leftPos + 67, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
