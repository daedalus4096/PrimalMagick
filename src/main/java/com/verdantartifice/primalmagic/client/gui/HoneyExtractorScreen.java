package com.verdantartifice.primalmagic.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagic.common.containers.HoneyExtractorContainer;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for honey extractor block.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class HoneyExtractorScreen extends AbstractContainerScreen<HoneyExtractorContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/honey_extractor.png");
    
    protected ManaGaugeWidget manaGauge;

    public HoneyExtractorScreen(HoneyExtractorContainer screenContainer, Inventory inv, Component titleIn) {
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
        this.manaGauge = this.addButton(new ManaGaugeWidget(this.leftPos + 10, this.topPos + 6, Source.SKY, this.menu.getCurrentMana(), this.menu.getMaxMana()));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        // Don't draw title text
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        // Render background texture
        this.minecraft.getTextureManager().bindForSetup(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate spin progress indicator
        int cook = this.menu.getSpinProgressionScaled();
        this.blit(matrixStack, this.leftPos + 75, this.topPos + 34, 176, 0, cook + 1, 16);
    }
}
