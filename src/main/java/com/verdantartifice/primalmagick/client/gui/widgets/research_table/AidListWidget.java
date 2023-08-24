package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing all nearby research aids on the research table.
 * 
 * @author Daedalus4096
 */
public class AidListWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table_overlay.png");
    
    protected final List<Component> aidNames;

    public AidListWidget(int x, int y, List<Component> aidNames) {
        super(x, y, 8, 8, Component.empty());
        this.aidNames = aidNames;
        if (!this.aidNames.isEmpty()) {
            MutableComponent tooltip = Component.translatable("label.primalmagick.research_table.aid_header");
            aidNames.forEach(name -> tooltip.append(CommonComponents.NEW_LINE).append(name));
            this.setTooltip(Tooltip.create(tooltip));
        }
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 206, 0, 8, 8);
        guiGraphics.pose().popPose();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_) {
    }
}
