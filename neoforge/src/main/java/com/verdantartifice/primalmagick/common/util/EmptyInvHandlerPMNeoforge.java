package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EmptyInvHandlerPMNeoforge implements IItemHandlerPM {
    public static final EmptyInvHandlerPMNeoforge INSTANCE = new EmptyInvHandlerPMNeoforge();

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        throw new IndexOutOfBoundsException(0);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public ItemStack extractItem(ItemStack stack, boolean simulate) {
        throw new IndexOutOfBoundsException(0);
    }

    @Override
    public boolean transactSlots(boolean simulate, List<SlotOperation> slotOperations) {
        return false;
    }

    @Override
    public boolean transact(boolean simulate, List<HandlerOperation> handlerOperations) {
        return false;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public Container asContainer() {
        return EmptyContainerPMNeoforge.INSTANCE;
    }
}
