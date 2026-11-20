package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * Custom GUI slot for calcinator fuel.
 * 
 * @author Daedalus4096
 */
public class CalcinatorFuelSlotNeoforge extends FilteredSlotNeoforge {
    protected static final Component FUEL_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.calcinator_gui.slot.fuel");

    protected final CalcinatorMenu container;

    public CalcinatorFuelSlotNeoforge(CalcinatorMenu container, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition,
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
