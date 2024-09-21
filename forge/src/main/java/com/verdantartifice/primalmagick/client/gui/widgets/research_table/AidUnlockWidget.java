package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Display widget for showing that a project was unlocked by a research aid in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class AidUnlockWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table_overlay.png");
    
    protected Block aidBlock;

    public AidUnlockWidget(int x, int y, @Nonnull Block aidBlock) {
        super(x, y, 8, 8, Component.empty());
        this.aidBlock = aidBlock;
        this.setTooltip(Tooltip.create(Component.translatable("label.primalmagick.research_table.unlock", this.aidBlock.getName())));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 198, 0, 8, 8);
        guiGraphics.pose().popPose();
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
