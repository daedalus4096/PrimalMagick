package com.verdantartifice.primalmagick.common.menus.slots;

import net.minecraft.network.chat.Component;

/**
 * Interface marking a GUI slot that has a tooltip that should be rendered under certain
 * circumstances.
 * 
 * @author Daedalus4096
 */
public interface IHasTooltip {
    boolean shouldShowTooltip();
    Component getTooltip();
}
