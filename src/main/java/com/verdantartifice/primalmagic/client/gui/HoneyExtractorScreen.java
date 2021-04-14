package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
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

    public HoneyExtractorScreen(HoneyExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        // Don't draw title text
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        // Render background texture
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        // Animate spin progress indicator
        int cook = this.container.getSpinProgressionScaled();
        this.blit(matrixStack, this.guiLeft + 75, this.guiTop + 34, 176, 0, cook + 1, 16);
        
        // Render mana bar
        int mana = this.container.getManaLevelScaled();
        Color manaColor = new Color(Source.SKY.getColor());
        RenderSystem.color4f(manaColor.getRed() / 255.0F, manaColor.getGreen() / 255.0F, manaColor.getBlue() / 255.0F, 1.0F);
        this.blit(matrixStack, this.guiLeft + 11, this.guiTop + 7 + (50 - mana), 177, 18, 10, mana);

        // Render mana bar markers
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(matrixStack, this.guiLeft + 10, this.guiTop + 6, 188, 17, 12, 52);
    }
}
