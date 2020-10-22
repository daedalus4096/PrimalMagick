package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing a specific required research entry on the requirements page.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ResearchWidget extends Widget {
    protected static final ResourceLocation BAG_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/research/research_bag.png");
    protected static final ResourceLocation TUBE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/research/research_tube.png");
    protected static final ResourceLocation MAP_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/research/research_map.png");
    protected static final ResourceLocation UNKNOWN_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/research/research_unknown.png");
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    
    protected SimpleResearchKey key;
    protected boolean isComplete;
    
    public ResearchWidget(SimpleResearchKey key, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, StringTextComponent.EMPTY);
        this.key = key;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Pick the icon to show based on the prefix of the research key
        ResourceLocation loc;
        if (this.key.getRootKey().startsWith("m_")) {
            loc = MAP_TEXTURE;
        } else if (this.key.getRootKey().startsWith("b_")) {
            loc = BAG_TEXTURE;
        } else if (this.key.getRootKey().startsWith("t_")) {
            loc = TUBE_TEXTURE;
        } else {
            loc = UNKNOWN_TEXTURE;
        }
        
        // Render the icon
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getInstance().getTextureManager().bindTexture(loc);
        RenderSystem.translatef(this.x, this.y, 0.0F);
        RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        RenderSystem.popMatrix();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 8, this.y, 100.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
            this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
            RenderSystem.popMatrix();
        }
        
        if (this.isHovered()) {
            // Render tooltip
            ITextComponent text = new TranslationTextComponent("primalmagic.research." + this.key.getRootKey() + ".text");
            GuiUtils.renderCustomTooltip(Collections.singletonList(text), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
