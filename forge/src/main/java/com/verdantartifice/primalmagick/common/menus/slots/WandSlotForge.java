package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;
import net.minecraft.network.chat.Component;
import net.minecraftforge.items.IItemHandler;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlotForge extends FilteredSlotForge implements IWandSlot {
    protected static final Component WAND_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.wand");
    protected static final Component STAFF_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.staff");
    
    public WandSlotForge(IItemHandler inventoryIn, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(inventoryIn, index, xPosition, yPosition, 
                new FilteredSlotProperties().background(TEXTURE).tooltip(allowStaves ? STAFF_SLOT_TOOLTIP : WAND_SLOT_TOOLTIP)
                        .filter(stack -> (stack.getItem() instanceof IWand) && (allowStaves || !(stack.getItem() instanceof IStaff))));
    }
}
