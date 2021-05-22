package com.verdantartifice.primalmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for the calcinator block.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class CalcinatorScreen extends ContainerScreen<CalcinatorContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/calcinator.png");

    public CalcinatorScreen(CalcinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        // Animate burn indicator
        if (this.container.isBurning()) {
            int burn = this.container.getBurnLeftScaled();
            this.blit(matrixStack, this.guiLeft + 34, this.guiTop + 48 - burn, 176, 12 - burn, 14, burn + 1);
        }
        
        // Animate cook progress indicator
        int cook = this.container.getCookProgressionScaled();
        this.blit(matrixStack, this.guiLeft + 57, this.guiTop + 34, 176, 14, cook + 1, 16);
    }
}
