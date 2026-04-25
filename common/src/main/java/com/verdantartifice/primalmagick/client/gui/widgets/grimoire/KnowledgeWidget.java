package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Display widget for showing required knowledge (e.g. observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeWidget extends AbstractWidget {
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");

    protected KnowledgeType type;
    protected int amount;
    protected boolean isComplete;
    
    public KnowledgeWidget(KnowledgeType type, int amount, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.type = type;
        this.amount = amount;
        this.isComplete = isComplete;
        this.setTooltip(Tooltip.create(Component.translatable(this.type.getNameTranslationKey())));
    }
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        
        pGuiGraphics.pose().pushMatrix();
        
        // Draw knowledge type icon
        pGuiGraphics.pose().translate(this.getX(), this.getY());
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.type.getIconLocation(), 0, 0, 16, 16);
        
        pGuiGraphics.pose().popMatrix();
        
        // Draw amount str
        Component amountText = Component.literal(Integer.toString(this.amount));
        int width = mc.font.width(amountText.getString());
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12);
        pGuiGraphics.pose().scale(0.5F, 0.5F);
        pGuiGraphics.text(mc.font, amountText, 0, 0, this.isComplete ? Color.WHITE.getRGB() : Color.RED.getRGB());
        pGuiGraphics.pose().popMatrix();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX() + 8, this.getY());
            pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, COMPLETE, 0, 0, 10, 10);
            pGuiGraphics.pose().popMatrix();
        }
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
