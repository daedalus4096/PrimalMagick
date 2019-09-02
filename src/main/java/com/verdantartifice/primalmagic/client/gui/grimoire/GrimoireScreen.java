package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrimoireScreen extends ContainerScreen<GrimoireContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    
    public GrimoireScreen(GrimoireContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 256;
        this.ySize = 181;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        float relX = (this.width - this.xSize * 1.3F) / 2.0F;
        float relY = (this.height - this.ySize * 1.3F) / 2.0F;

        GlStateManager.pushMatrix();
        GlStateManager.translatef(relX, relY, 0.0F);
        GlStateManager.scalef(1.3F, 1.3F, 1.0F);
        this.blit(0, 0, 0, 0, this.xSize, this.ySize);
        GlStateManager.popMatrix();
    }
}
