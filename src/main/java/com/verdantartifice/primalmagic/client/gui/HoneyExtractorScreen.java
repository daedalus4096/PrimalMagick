package com.verdantartifice.primalmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaGaugeWidget;
import com.verdantartifice.primalmagic.common.containers.HoneyExtractorContainer;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for honey extractor block.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class HoneyExtractorScreen extends ContainerScreen<HoneyExtractorContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/honey_extractor.png");
    
    protected ManaGaugeWidget manaGauge;

    public HoneyExtractorScreen(HoneyExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.manaGauge.setCurrentMana(this.container.getCurrentMana());
        this.manaGauge.setMaxMana(this.container.getMaxMana());
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.manaGauge = this.addButton(new ManaGaugeWidget(this.guiLeft + 10, this.guiTop + 6, Source.SKY, this.container.getCurrentMana(), this.container.getMaxMana()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        // Don't draw title text
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        // Render background texture
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        // Animate spin progress indicator
        int cook = this.container.getSpinProgressionScaled();
        this.blit(matrixStack, this.guiLeft + 75, this.guiTop + 34, 176, 0, cook + 1, 16);
    }
}
