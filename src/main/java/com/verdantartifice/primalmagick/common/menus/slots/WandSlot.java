package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlot extends FilteredSlot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_wand_slot");
    
    public WandSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(inventoryIn, index, xPosition, yPosition, 
                new FilteredSlot.Properties().background(TEXTURE).filter(stack -> (stack.getItem() instanceof IWand) && (allowStaves || !(stack.getItem() instanceof IStaff))));
    }
}
