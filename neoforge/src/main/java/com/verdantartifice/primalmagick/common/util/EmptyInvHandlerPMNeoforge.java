package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.inventory.IItemHandlerPMNeoforge;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmptyInvHandlerPMNeoforge implements IItemHandlerPMNeoforge {
    public static final EmptyInvHandlerPMNeoforge INSTANCE = new EmptyInvHandlerPMNeoforge();
    private static final EmptyResourceHandler<ItemResource> INNER = EmptyResourceHandler.instance();

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
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

    @Override
    public void set(int index, @NotNull ItemResource resource, int amount) {
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public int size() {
        return INNER.size();
    }

    @Override
    @NotNull
    public ItemResource getResource(int index) {
        return INNER.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return INNER.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, @NotNull ItemResource resource) {
        return INNER.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, @NotNull ItemResource resource) {
        return INNER.isValid(index, resource);
    }

    @Override
    public int insert(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        return INNER.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        return INNER.extract(index, resource, amount, transaction);
    }
}
