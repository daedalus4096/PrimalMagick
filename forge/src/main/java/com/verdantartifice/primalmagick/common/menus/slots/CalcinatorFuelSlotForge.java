package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;

/**
 * Custom GUI slot for calcinator fuel.
 * 
 * @author Daedalus4096
 */
public class CalcinatorFuelSlotForge extends FilteredSlotForge {
    protected static final Component FUEL_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.calcinator_gui.slot.fuel");

    protected final CalcinatorMenu container;

    public CalcinatorFuelSlotForge(CalcinatorMenu container, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition,
                new FilteredSlotProperties().filter(stack -> container.isFuel(stack) || isBucket(stack)).tooltip(FUEL_SLOT_TOOLTIP));
        this.container = container;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        // Only one empty bucket at a time
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }
    
    protected static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}
