package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.research.Knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing required knowledge (e.g. observations).
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class KnowledgeWidget extends Widget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected Knowledge knowledge;
    protected boolean isComplete;
    
    public KnowledgeWidget(Knowledge knowledge, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, StringTextComponent.EMPTY);
        this.knowledge = knowledge;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.pushMatrix();
        
        // Draw knowledge type icon
        mc.getTextureManager().bindTexture(this.knowledge.getType().getIconLocation());
        RenderSystem.translatef(this.x, this.y, 0.0F);
        RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        
        RenderSystem.popMatrix();
        
        // Draw amount str
        ITextComponent amountText = new StringTextComponent(Integer.toString(this.knowledge.getAmount()));
        int width = mc.fontRenderer.getStringWidth(amountText.getString());
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
        RenderSystem.scaled(0.5D, 0.5D, 0.5D);
        mc.fontRenderer.drawStringWithShadow(matrixStack, amountText.getString(), 0.0F, 0.0F, this.isComplete ? Color.WHITE.getRGB() : Color.RED.getRGB());
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
            ITextComponent knowledgeText = new TranslationTextComponent(this.knowledge.getType().getNameTranslationKey());
            GuiUtils.renderCustomTooltip(Collections.singletonList(knowledgeText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
