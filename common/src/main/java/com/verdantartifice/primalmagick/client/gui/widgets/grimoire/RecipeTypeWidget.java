package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Display widget for showing a recipe type.  Used on the recipe pages.
 * 
 * @author Daedalus4096
 */
public class RecipeTypeWidget extends AbstractWidget {
    protected final SlotDisplay craftingStationDisplay;
    
    public RecipeTypeWidget(SlotDisplay slotDisplay, int x, int y, Component tooltip) {
        super(x, y, 16, 16, Component.empty());
        this.craftingStationDisplay = slotDisplay;
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Draw recipe station icon
        Minecraft mc = Minecraft.getInstance();
        ContextMap contextMap = SlotDisplayContext.fromLevel(Objects.requireNonNull(mc.level));
        GuiUtils.renderItemStack(pGuiGraphics, this.craftingStationDisplay.resolveForFirstStack(contextMap), this.getX(), this.getY(), this.getMessage().getString(), false);

        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
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
