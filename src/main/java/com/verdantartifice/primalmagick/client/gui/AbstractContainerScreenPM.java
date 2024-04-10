package com.verdantartifice.primalmagick.client.gui;

import java.util.Optional;

import com.verdantartifice.primalmagick.common.menus.slots.IHasCyclingBackgrounds;
import com.verdantartifice.primalmagick.common.menus.slots.IHasTooltip;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Base class GUI for a container screen.  Provides some common functionality to extenders, such as showing tooltips
 * when appropriate for slots that have them.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractContainerScreenPM<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public AbstractContainerScreenPM(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.menu.slots.stream().forEach(slot -> {
            if (slot instanceof IHasCyclingBackgrounds bgSlot) {
                bgSlot.tickBackgrounds();
            }
        });
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        
        // Render filtered slot tooltips if appropriate
        Optional<Component> tooltipOpt = Optional.empty();
        if (this.hoveredSlot instanceof IHasTooltip tooltipSlot && tooltipSlot.shouldShowTooltip()) {
            tooltipOpt = Optional.ofNullable(tooltipSlot.getTooltip());
        }
        tooltipOpt.ifPresent(tooltip -> pGuiGraphics.renderTooltip(this.font, tooltip, pX, pY));
    }
}
