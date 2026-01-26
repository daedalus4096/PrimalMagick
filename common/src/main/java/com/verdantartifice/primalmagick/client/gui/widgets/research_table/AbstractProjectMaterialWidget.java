package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.common.theorycrafting.materials.AbstractProjectMaterial;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Base class for a display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterialWidget<T extends AbstractProjectMaterial<T>> extends AbstractWidget {
    protected static final Identifier COMPLETE_SPRITE = ResourceUtils.loc("research_table/complete");
    protected static final Identifier CONSUMED_SPRITE = ResourceUtils.loc("research_table/consumed");
    protected static final Identifier BONUS_SPRITE = ResourceUtils.loc("research_table/bonus");

    protected final T material;
    protected final boolean complete;
    protected final boolean consumed;
    protected final boolean hasBonus;
    protected Component lastTooltip = Component.empty();
    protected Component tooltip = Component.empty();
    
    public AbstractProjectMaterialWidget(T material, int x, int y, Set<Block> surroundings) {
        super(x, y, 16, 16, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.material = material;
        this.hasBonus = material.getBonusReward() > 0.0D;
        this.consumed = material.isConsumed();
        this.complete = material.isSatisfied(mc.player, this.consumed ? Collections.emptySet() : surroundings);
    }
    
    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.complete) {
            // Render completion checkmark if appropriate
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(this.getX() + 8, this.getY());
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, COMPLETE_SPRITE, 0, 0, 10, 10);
            guiGraphics.pose().popMatrix();
        }
        if (this.consumed) {
            // Render consumption exclamation point if appropriate
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(this.getX() - 3, this.getY() - 2);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, CONSUMED_SPRITE, 0, 0, 10, 10);
            guiGraphics.pose().popMatrix();
        }
        if (this.hasBonus) {
            // Render bonus indicator if appropriate
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(this.getX() - 1, this.getY() + 10);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BONUS_SPRITE, 0, 0, 6, 5);
            guiGraphics.pose().popMatrix();
        }
        
        
        // Update tooltip if necessary
        this.lastTooltip = this.tooltip;
        List<Component> lines = new ArrayList<>(this.getHoverText());
        if (this.consumed) {
            lines.add(Component.translatable("tooltip.primalmagick.research_table.material.consumed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (this.hasBonus) {
            lines.add(Component.translatable("tooltip.primalmagick.research_table.material.has_bonus").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        this.tooltip = CommonComponents.joinLines(lines);
        if (!this.lastTooltip.equals(this.tooltip)) {
            this.setTooltip(Tooltip.create(this.tooltip));
        }
    }
    
    /**
     * Get the text component to show in a tooltip when this widget is hovered over.
     * 
     * @return the text component to show in a tooltip
     */
    protected abstract List<Component> getHoverText();
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
    }
}
