package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Display widget for showing that a project was unlocked by a research aid in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class AidUnlockWidget extends AbstractWidget {
    protected static final Identifier UNLOCK_SPRITE = ResourceUtils.loc("research_table/unlock");

    protected Block aidBlock;

    public AidUnlockWidget(int x, int y, @Nonnull Block aidBlock) {
        super(x, y, 8, 8, Component.empty());
        this.aidBlock = aidBlock;
        this.setTooltip(Tooltip.create(Component.translatable("label.primalmagick.research_table.unlock", this.aidBlock.getName())));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, UNLOCK_SPRITE, 0, 0, 8, 8);
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
