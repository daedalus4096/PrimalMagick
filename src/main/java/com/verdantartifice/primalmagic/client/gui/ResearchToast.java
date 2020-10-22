package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * GUI element for the toast that shows when you complete a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchToast implements IToast {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/hud.png");
    
    protected ResearchEntry entry;
    
    public ResearchToast(ResearchEntry entry) {
        this.entry = entry;
    }
    
    @Override
    public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long delta) {
    	Minecraft mc = toastGui.getMinecraft();
    	
        // Render the toast background
    	mc.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        toastGui.blit(matrixStack, 0, 0, 0, 224, 160, 32);
        
        // Render the toast title text
        ITextComponent titleText = new TranslationTextComponent("primalmagic.toast.title");
        mc.fontRenderer.drawString(matrixStack, titleText.getString(), 6, 7, 0x551A8B);
        
        // Render the description of the completed research
        ITextComponent descText = new TranslationTextComponent(this.entry.getNameTranslationKey());
        String descStr = descText.getString();
        float width = mc.fontRenderer.getStringWidth(descStr);
        if (width > 148.0F) {
            // Scale down the research description to make it fit, if needed
            float scale = (148.0F / width);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(6.0F, 18.0F, 0.0F);
            RenderSystem.scalef(scale, scale, scale);
            mc.fontRenderer.drawString(matrixStack, descStr, 0, 0, Color.BLACK.getRGB());
            RenderSystem.popMatrix();
        } else {
        	mc.fontRenderer.drawString(matrixStack, descStr, 6, 18, Color.BLACK.getRGB());
        }
        
        // If the toast has been open long enough, hide it
        return (delta >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }

}
