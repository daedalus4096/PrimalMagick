package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.IVanillaStatRequirement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class VanillaStatProgressWidget extends AbstractWidget {
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");
    private static final Identifier PROGRESS_FG = ResourceUtils.loc("grimoire/progress_foreground");
    private static final Identifier PROGRESS_BG = ResourceUtils.loc("grimoire/progress_background");
    
    protected final IVanillaStatRequirement requirement;
    protected final boolean isComplete;
    protected final IconDefinition iconDef;
    protected MutableComponent lastTooltip = Component.empty();
    protected MutableComponent tooltip = Component.empty();

    public VanillaStatProgressWidget(IVanillaStatRequirement requirement, int x, int y, boolean isComplete) {
        super(x, y, 16, 18, Component.empty());
        this.requirement = requirement;
        this.isComplete = isComplete;
        this.iconDef = requirement.getIconDefinition();
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the icon
        GuiUtils.renderIconFromDefinition(pGuiGraphics, this.iconDef, this.getX(), this.getY());
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX() + 8, this.getY());
            pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, COMPLETE, 0, 0, 10, 10);
            pGuiGraphics.pose().popMatrix();
        }
        
        // Draw progress bar background
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BG, 0, 0, 16, 2);
        pGuiGraphics.pose().popMatrix();
        
        // Draw progress bar foreground
        Minecraft mc = Minecraft.getInstance();
        int currentValue = this.requirement.getCurrentValue(mc.player);
        int px = (int)(16.0D * ((double)currentValue / (double)this.requirement.getThreshold()));
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_FG, 0, 0, px, 2);
        pGuiGraphics.pose().popMatrix();
        
        // Prepare the tooltip
        this.lastTooltip = this.tooltip;
        this.tooltip = this.getDescription();
        if (!this.lastTooltip.equals(this.tooltip)) {
            this.setTooltip(Tooltip.create(this.tooltip));
        }
    }
    
    protected MutableComponent getDescription() {
        Minecraft mc = Minecraft.getInstance();
        Component baseDescription = this.requirement.getStatDescription();
        String currentValue = this.requirement.getStat().format(Math.min(this.requirement.getCurrentValue(mc.player), this.requirement.getThreshold()));
        String maxValue = this.requirement.getStat().format(this.requirement.getThreshold());
        return Component.translatable("tooltip.primalmagick.stat_progress", baseDescription, currentValue, maxValue);
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
