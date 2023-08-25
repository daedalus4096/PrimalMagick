package com.verdantartifice.primalmagick.client.toast;

import java.awt.Color;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI element for the toast that shows when you complete a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchToast implements Toast {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/hud.png");
    
    protected final ResearchEntry entry;
    protected final boolean isComplete;
    
    public ResearchToast(ResearchEntry entry, boolean isComplete) {
        this.entry = entry;
        this.isComplete = isComplete;
    }
    
    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastGui, long delta) {
        Minecraft mc = toastGui.getMinecraft();
        
        // Render the toast background
        guiGraphics.blit(TEXTURE, 0, 0, 0, 224, 160, 32);
        
        // Render the toast title text
        Component titleText = this.isComplete ? Component.translatable("label.primalmagick.toast.completed") : Component.translatable("label.primalmagick.toast.revealed");
        guiGraphics.drawString(mc.font, titleText, 6, 7, this.isComplete ? Source.VOID.getColor() : Source.INFERNAL.getColor(), false);
        
        // Render the description of the completed research
        Component descText = Component.translatable(this.entry.getNameTranslationKey());
        float width = mc.font.width(descText.getString());
        if (width > 148.0F) {
            // Scale down the research description to make it fit, if needed
            float scale = (148.0F / width);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(6.0F, 18.0F, 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawString(mc.font, descText, 0, 0, Color.BLACK.getRGB(), false);
            guiGraphics.pose().popPose();
        } else {
            guiGraphics.drawString(mc.font, descText, 6, 18, Color.BLACK.getRGB(), false);
        }
        
        // If the toast has been open long enough, hide it
        return (delta >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }

}
