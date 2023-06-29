package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.theorycrafting.AbstractProjectMaterial;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Base class for a display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterialWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/research_table_overlay.png");

    protected boolean complete;
    protected boolean consumed;
    protected boolean hasBonus;
    
    public AbstractProjectMaterialWidget(AbstractProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(x, y, 16, 16, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.hasBonus = material.getBonusReward() > 0.0D;
        this.consumed = material.isConsumed();
        this.complete = material.isSatisfied(mc.player, this.consumed ? Collections.emptySet() : surroundings);
        
        MutableComponent tooltip = Component.empty();
        if (this.consumed) {
            tooltip.append(Component.translatable("tooltip.primalmagick.research_table.material.consumed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            if (this.hasBonus) {
                tooltip.append("\n");
            }
        }
        if (this.hasBonus) {
            tooltip.append(Component.translatable("tooltip.primalmagick.research_table.material.has_bonus").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        this.setTooltip(Tooltip.create(tooltip));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.complete) {
            // Render completion checkmark if appropriate
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 8, this.getY(), 200.0F);
            guiGraphics.blit(TEXTURE, 0, 0, 162, 0, 10, 10);
            guiGraphics.pose().popPose();
        }
        if (this.consumed) {
            // Render consumption exclamation point if appropriate
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() - 3, this.getY() - 2, 200.0F);
            guiGraphics.blit(TEXTURE, 0, 0, 172, 0, 10, 10);
            guiGraphics.pose().popPose();
        }
        if (this.hasBonus) {
            // Render bonus indicator if appropriate
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() - 1, this.getY() + 10, 200.0F);
            guiGraphics.blit(TEXTURE, 0, 0, 215, 0, 6, 5);
            guiGraphics.pose().popPose();
        }
    }
    
    /**
     * Get the text component to show in a tooltip when this widget is hovered over.
     * 
     * @return the text component to show in a tooltip
     */
    protected abstract List<Component> getHoverText();
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
