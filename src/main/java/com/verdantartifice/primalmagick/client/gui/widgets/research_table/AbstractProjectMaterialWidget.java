package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.ArrayList;
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
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Base class for a display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterialWidget<T extends AbstractProjectMaterial> extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table_overlay.png");

    protected final T material;
    protected final boolean complete;
    protected final boolean consumed;
    protected final boolean hasBonus;
    
    public AbstractProjectMaterialWidget(T material, int x, int y, Set<Block> surroundings) {
        super(x, y, 16, 16, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.material = material;
        this.hasBonus = material.getBonusReward() > 0.0D;
        this.consumed = material.isConsumed();
        this.complete = material.isSatisfied(mc.player, this.consumed ? Collections.emptySet() : surroundings);
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
        
        List<Component> lines = new ArrayList<>(this.getHoverText());
        if (this.consumed) {
            lines.add(Component.translatable("tooltip.primalmagick.research_table.material.consumed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (this.hasBonus) {
            lines.add(Component.translatable("tooltip.primalmagick.research_table.material.has_bonus").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
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
