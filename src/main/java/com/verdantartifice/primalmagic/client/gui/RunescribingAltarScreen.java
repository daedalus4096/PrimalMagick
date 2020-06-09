package com.verdantartifice.primalmagic.client.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for the runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RunescribingAltarScreen extends ContainerScreen<RunescribingAltarContainer> {
    protected static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_9.png");
    protected static final Map<Integer, ResourceLocation> TEXTURES = Util.make(new HashMap<>(), map -> {
        map.put(3, new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_3.png"));
        map.put(5, new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_5.png"));
        map.put(7, new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_7.png"));
        map.put(9, new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_9.png"));
    });

    public RunescribingAltarScreen(RunescribingAltarContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Nonnull
    protected ResourceLocation getTextureLocation() {
        return TEXTURES.getOrDefault(this.container.getMaxRunes(), DEFAULT_TEXTURE);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.getTextureLocation());
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
