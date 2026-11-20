package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlotNeoforge extends FilteredSlotNeoforge implements IWandSlot {
    protected static final Component WAND_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.wand");
    protected static final Component STAFF_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.staff");

    public WandSlotNeoforge(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(handler, slotModifier, index, xPosition, yPosition,
                new FilteredSlotProperties().background(TEXTURE).tooltip(allowStaves ? STAFF_SLOT_TOOLTIP : WAND_SLOT_TOOLTIP)
                        .filter(stack -> (stack.getItem() instanceof IWand) && (allowStaves || !(stack.getItem() instanceof IStaff))));
    }
}
