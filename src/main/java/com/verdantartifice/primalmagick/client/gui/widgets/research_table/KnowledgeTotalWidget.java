package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.awt.Color;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Display widget for showing accumulated knowledge (e.g. observations) in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTotalWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table_overlay.png");

    protected IPlayerKnowledge.KnowledgeType type;
    protected LazyOptional<IPlayerKnowledge> knowledgeOpt;
    
    public KnowledgeTotalWidget(int x, int y, IPlayerKnowledge.KnowledgeType type) {
        super(x, y, 16, 19, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.type = type;
        this.knowledgeOpt = PrimalMagickCapabilities.getKnowledge(mc.player);
        this.setTooltip(Tooltip.create(Component.translatable(this.type.getNameTranslationKey())));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw knowledge type icon
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        guiGraphics.blit(this.type.getIconLocation(), 0, 0, 0, 0, 255, 255);        
        guiGraphics.pose().popPose();
        
        // Draw progress bar background
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY() + 17, 0.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 182, 2, 16, 2);
        guiGraphics.pose().popPose();
        
        this.knowledgeOpt.ifPresent(knowledge -> {
            // Draw amount str
            int levels = knowledge.getKnowledge(this.type);
            Component amountText = Component.literal(Integer.toString(levels));
            int width = mc.font.width(amountText);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 5.0F);
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            guiGraphics.drawString(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
            guiGraphics.pose().popPose();
            
            // Draw progress bar foreground
            int rawPoints = knowledge.getKnowledgeRaw(this.type);
            int levelPoints = rawPoints % this.type.getProgression();
            int px = (int)(16.0D * ((double)levelPoints / (double)this.type.getProgression()));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX(), this.getY() + 17, 1.0F);
            guiGraphics.blit(TEXTURE, 0, 0, 182, 0, px, 2);
            guiGraphics.pose().popPose();
        });
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
