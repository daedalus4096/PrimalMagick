package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.Knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing required knowledge (e.g. observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = PrimalMagick.resource("textures/gui/grimoire.png");

    protected Knowledge knowledge;
    protected boolean isComplete;
    
    public KnowledgeWidget(Knowledge knowledge, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.knowledge = knowledge;
        this.isComplete = isComplete;
        this.setTooltip(Tooltip.create(Component.translatable(this.knowledge.getType().getNameTranslationKey())));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        guiGraphics.pose().pushPose();
        
        // Draw knowledge type icon
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        guiGraphics.blit(this.knowledge.getType().getIconLocation(), 0, 0, 0, 0, 255, 255);
        
        guiGraphics.pose().popPose();
        
        // Draw amount str
        Component amountText = Component.literal(Integer.toString(this.knowledge.getAmount()));
        int width = mc.font.width(amountText.getString());
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 5.0F);
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        guiGraphics.drawString(mc.font, amountText, 0, 0, this.isComplete ? Color.WHITE.getRGB() : Color.RED.getRGB());
        guiGraphics.pose().popPose();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 8, this.getY(), 100.0F);
            guiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 159, 207, 10, 10);
            guiGraphics.pose().popPose();
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
