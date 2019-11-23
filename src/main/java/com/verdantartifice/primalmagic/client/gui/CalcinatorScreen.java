package com.verdantartifice.primalmagic.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CalcinatorScreen extends ContainerScreen<CalcinatorContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/calcinator.png");

    public CalcinatorScreen(CalcinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        // Animate burn indicator
        if (this.container.isBurning()) {
            int burn = this.container.getBurnLeftScaled();
            this.blit(this.guiLeft + 34, this.guiTop + 48 - burn, 176, 12 - burn, 14, burn + 1);
        }
        
        // Animate cook progress indicator
        int cook = this.container.getCookProgressionScaled();
        this.blit(this.guiLeft + 57, this.guiTop + 34, 176, 14, cook + 1, 16);
    }
}
