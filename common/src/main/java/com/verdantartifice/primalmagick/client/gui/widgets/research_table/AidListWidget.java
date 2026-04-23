package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Display widget for showing all nearby research aids on the research table.
 * 
 * @author Daedalus4096
 */
public class AidListWidget extends AbstractWidget {
    protected static final Identifier AID_LIST_SPRITE = ResourceUtils.loc("research_table/aid_list");

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
    public void renderWidget(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.getX(), this.getY());
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, AID_LIST_SPRITE, 0, 0, 8, 8);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
    }
}
