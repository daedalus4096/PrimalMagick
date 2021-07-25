package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import net.minecraft.client.gui.components.toasts.Toast.Visibility;

/**
 * GUI element for the toast that shows when you complete a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchToast implements Toast {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/hud.png");
    
    protected ResearchEntry entry;
    
    public ResearchToast(ResearchEntry entry) {
        this.entry = entry;
    }
    
    @Override
    public Visibility render(PoseStack matrixStack, ToastComponent toastGui, long delta) {
    	Minecraft mc = toastGui.getMinecraft();
    	
        // Render the toast background
    	mc.getTextureManager().bind(TEXTURE);
        toastGui.blit(matrixStack, 0, 0, 0, 224, 160, 32);
        
        // Render the toast title text
        Component titleText = new TranslatableComponent("primalmagic.toast.title");
        mc.font.draw(matrixStack, titleText, 6, 7, 0x551A8B);
        
        // Render the description of the completed research
        Component descText = new TranslatableComponent(this.entry.getNameTranslationKey());
        float width = mc.font.width(descText.getString());
        if (width > 148.0F) {
            // Scale down the research description to make it fit, if needed
            float scale = (148.0F / width);
            matrixStack.pushPose();
            matrixStack.translate(6.0F, 18.0F, 0.0F);
            matrixStack.scale(scale, scale, scale);
            mc.font.draw(matrixStack, descText, 0, 0, Color.BLACK.getRGB());
            matrixStack.popPose();
        } else {
        	mc.font.draw(matrixStack, descText, 6, 18, Color.BLACK.getRGB());
        }
        
        // If the toast has been open long enough, hide it
        return (delta >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }

}
