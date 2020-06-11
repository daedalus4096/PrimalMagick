package com.verdantartifice.primalmagic.client.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.common.containers.AbstractRunescribingAltarContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base GUI screen for runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractRunescribingAltarScreen<T extends AbstractRunescribingAltarContainer> extends ContainerScreen<T> {
    public AbstractRunescribingAltarScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Nonnull
    protected abstract ResourceLocation getTextureLocation();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.getTextureLocation());
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
