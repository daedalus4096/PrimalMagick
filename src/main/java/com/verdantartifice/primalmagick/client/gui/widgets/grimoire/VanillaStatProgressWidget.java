package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.IVanillaStatRequirement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class VanillaStatProgressWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = PrimalMagick.resource("textures/gui/grimoire.png");
    
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
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the icon
        GuiUtils.renderIconFromDefinition(pGuiGraphics, this.iconDef, this.getX(), this.getY());
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.getX() + 8, this.getY(), 100.0F);
            pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 159, 207, 10, 10);
            pGuiGraphics.pose().popPose();
        }
        
        // Draw progress bar background
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17, 0.0F);
        pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 0, 234, 16, 2);
        pGuiGraphics.pose().popPose();
        
        // Draw progress bar foreground
        Minecraft mc = Minecraft.getInstance();
        int currentValue = this.requirement.getCurrentValue(mc.player);
        int px = (int)(16.0D * ((double)currentValue / (double)this.requirement.getThreshold()));
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX(), this.getY() + 17, 1.0F);
        pGuiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 0, 232, px, 2);
        pGuiGraphics.pose().popPose();
        
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
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
