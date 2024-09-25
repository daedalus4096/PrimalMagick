package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlot extends FilteredSlot {
    public static final ResourceLocation TEXTURE = ResourceUtils.loc("item/empty_wand_slot");
    protected static final Component WAND_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.wand");
    protected static final Component STAFF_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.wand_gui.slot.staff");
    
    public WandSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(inventoryIn, index, xPosition, yPosition, 
                new FilteredSlot.Properties().background(TEXTURE).tooltip(allowStaves ? STAFF_SLOT_TOOLTIP : WAND_SLOT_TOOLTIP)
                        .filter(stack -> (stack.getItem() instanceof IWand) && (allowStaves || !(stack.getItem() instanceof IStaff))));
    }
}
